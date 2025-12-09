package es;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária responsável por carregar dados de propriedades a partir de ficheiros CSV.
 * Utiliza a biblioteca Apache Commons CSV para um parsing robusto.
 */
public class CSVLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVLoader.class);

    /**
     * Carrega uma lista de objetos {@link Propriedade} a partir de um ficheiro CSV.
     * <p>
     * O método espera um ficheiro separado por ';' e ignora a primeira linha (cabeçalho).
     * Utiliza Apache Commons CSV para lidar corretamente com aspas e caracteres especiais.
     * </p>
     *
     * @param nomeFicheiro Nome do ficheiro CSV presente no classpath (ex: src/main/resources).
     * @return Lista de objetos {@code Propriedade} carregados com sucesso.
     */
    public static List<Propriedade> carregarPropriedades(String nomeFicheiro) {
        List<Propriedade> propriedades = new ArrayList<>();

        // Configuração do formato CSV: Delimitador ';' e ignorar linha de cabeçalho
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .setHeader()             // Assume que a primeira linha é cabeçalho
                .setSkipHeaderRecord(true) // Salta o cabeçalho na iteração
                .build();

        try (InputStream inputStream = CSVLoader.class.getClassLoader().getResourceAsStream(nomeFicheiro)) {

            if (inputStream == null) {
                LOGGER.error("Ficheiro '{}' não encontrado no classpath.", nomeFicheiro);
                return propriedades;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 CSVParser parser = new CSVParser(reader, format)) {

                LOGGER.info("A iniciar leitura do ficheiro: {}", nomeFicheiro);

                for (CSVRecord record : parser) {
                    try {
                        // Validação básica de colunas
                        if (record.size() < 10) {
                            LOGGER.warn("Linha {} ignorada: número insuficiente de colunas.", record.getRecordNumber());
                            continue;
                        }

                        // Extração e conversão de dados (acesso por índice para manter compatibilidade)
                        // 0: OBJECTID, 1: PAR_ID, 2: PAR_NUM, 3: Shape_Length, 4: Shape_Area
                        // 5: GEOMETRY, 6: OWNER, 7: FREGUESIA, 8: MUNICIPIO, 9: ILHA

                        int objectId = Integer.parseInt(record.get(0));
                        double parId = parseDouble(record.get(1));
                        String parNum = record.get(2);
                        double shapeLength = parseDouble(record.get(3));
                        double shapeArea = parseDouble(record.get(4));
                        String geometry = record.get(5);
                        String owner = record.get(6);
                        String freguesia = record.get(7);
                        String municipio = record.get(8);
                        String ilha = record.get(9);

                        Propriedade prop = new Propriedade(objectId, parId, parNum, shapeLength, shapeArea,
                                geometry, owner, freguesia, municipio, ilha);

                        propriedades.add(prop);

                    } catch (NumberFormatException e) {
                        LOGGER.warn("Erro de formato na linha {}: {}", record.getRecordNumber(), e.getMessage());
                    } catch (Exception e) {
                        LOGGER.error("Erro inesperado na linha {}: {}", record.getRecordNumber(), e.getMessage());
                    }
                }
                
                LOGGER.info("Leitura concluída. {} propriedades carregadas.", propriedades.size());
            }

        } catch (IOException e) {
            LOGGER.error("Erro fatal ao ler o ficheiro CSV: {}", e.getMessage(), e);
        }

        return propriedades;
    }

    /**
     * Auxiliar para converter strings numéricas substituindo vírgulas por pontos.
     */
    private static double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value.replace(",", "."));
    }
}