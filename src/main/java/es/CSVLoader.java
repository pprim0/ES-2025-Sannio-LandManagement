package es;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária responsável por carregar dados de propriedades a partir de ficheiros CSV.
 */
public class CSVLoader {

    private static final String FICHEIRO_DEFAULT = "Madeira-Moodle-1.1.csv";

    /**
     * Carrega propriedades do ficheiro CSV padrão (Madeira-Moodle-1.1.csv).
     *
     * @return Lista de objetos Propriedade carregados com sucesso
     */
    public static List<Propriedade> carregar() {
        return carregarPropriedades(FICHEIRO_DEFAULT);
    }

    /**
     * Carrega uma lista de objetos {@link Propriedade} a partir de um ficheiro CSV com delimitador ';'.
     * <p>
     * O método ignora a primeira linha (assumida como cabeçalho) e tenta ler cada linha subsequente,
     * convertendo os campos em atributos da classe {@code Propriedade}. Linhas com erro são ignoradas.
     * </p>
     *
     * @param nomeFicheiro Nome do ficheiro CSV presente no classpath (por exemplo, dentro de {@code src/main/resources}).
     * @return Lista de objetos {@code Propriedade} carregados com sucesso.
     */
    public static List<Propriedade> carregarPropriedades(String nomeFicheiro) {
        List<Propriedade> propriedades = new ArrayList<>();

        // Tenta encontrar o ficheiro dentro do classpath (por ex: src/main/resources)
        try (InputStream inputStream = CSVLoader.class.getClassLoader().getResourceAsStream(nomeFicheiro)) {

            if (inputStream == null) {
                System.err.println("[ERRO] Ficheiro '" + nomeFicheiro + "' nao encontrado no classpath.");
                return propriedades;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String linha;
                boolean primeiraLinha = true;

                while ((linha = br.readLine()) != null) {
                    if (primeiraLinha) {
                        primeiraLinha = false;
                        continue; // Ignora cabeçalho
                    }

                    String[] campos = linha.split(";", -1); // Inclui campos vazios

                    try {
                        int objectId = Integer.parseInt(campos[0]);
                        double parId = Double.parseDouble(campos[1].replace(",", "."));
                        String parNum = campos[2];
                        double shapeLength = Double.parseDouble(campos[3].replace(",", "."));
                        double shapeArea = Double.parseDouble(campos[4].replace(",", "."));
                        String geometry = campos[5];
                        String owner = campos[6];
                        String freguesia = campos[7];
                        String municipio = campos[8];
                        String ilha = campos[9];

                        Propriedade prop = new Propriedade(objectId, parId, parNum, shapeLength, shapeArea,
                                geometry, owner, freguesia, municipio, ilha);

                        propriedades.add(prop);
                    } catch (Exception e) {
                        System.out.println("[AVISO] Linha invalida ignorada: " + linha.substring(0, Math.min(50, linha.length())));
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("[ERRO] Erro ao ler o ficheiro: " + e.getMessage());
        }

        System.out.println("[INFO] Carregadas " + propriedades.size() + " propriedades de " + nomeFicheiro);
        return propriedades;
    }
}