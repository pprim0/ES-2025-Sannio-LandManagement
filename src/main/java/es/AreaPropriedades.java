package es;

import java.util.*;

/**
 * Classe utilitária que fornece métodos para calcular áreas de propriedades
 * com base em divisões administrativas (freguesia, município ou ilha).
 * 
 * Implementa o ponto 4 do enunciado: cálculo de áreas por região geográfica.
 */
public class AreaPropriedades {

    /**
     * Calcula a área média das propriedades que pertencem a uma determinada zona
     * geográfica (freguesia, município ou ilha), filtrando apenas as que
     * correspondem ao nome fornecido.
     *
     * @param propriedades Lista completa de propriedades
     * @param tipoNivel    Tipo de zona geográfica a considerar: "freguesia", "municipio" ou "ilha"
     * @param valorNivel   Nome da freguesia, município ou ilha a filtrar (ex: "Funchal")
     * @return Área média das propriedades filtradas. Retorna 0.0 se nenhuma propriedade corresponder.
     */
    public static double calcularAreaMediaPorNivel(List<Propriedade> propriedades, 
                                                   String tipoNivel, 
                                                   String valorNivel) {
        double somaAreas = 0.0;
        int contador = 0;

        for (Propriedade p : propriedades) {
            String valorCampo = switch (tipoNivel.toLowerCase()) {
                case "freguesia" -> p.getFreguesia();
                case "municipio" -> p.getMunicipio();
                case "ilha" -> p.getIlha();
                default -> null;
            };

            if (valorCampo != null && valorCampo.equalsIgnoreCase(valorNivel)) {
                somaAreas += p.getShapeArea();
                contador++;
            }
        }

        return (contador > 0) ? somaAreas / contador : 0.0;
    }

    /**
     * Calcula a área total agrupada por freguesia.
     * Retorna um mapa com o nome da freguesia como chave e a área total como valor.
     *
     * @param propriedades Lista de propriedades
     * @return Mapa com freguesia → área total
     */
    public static Map<String, Double> calcularAreaTotalPorFreguesia(List<Propriedade> propriedades) {
        Map<String, Double> areas = new HashMap<>();
        
        for (Propriedade p : propriedades) {
            String freguesia = p.getFreguesia();
            if (freguesia != null && !freguesia.isEmpty()) {
                areas.merge(freguesia, p.getShapeArea(), Double::sum);
            }
        }
        
        return areas;
    }

    /**
     * Calcula a área total agrupada por município.
     * Retorna um mapa com o nome do município como chave e a área total como valor.
     *
     * @param propriedades Lista de propriedades
     * @return Mapa com município → área total
     */
    public static Map<String, Double> calcularAreaTotalPorMunicipio(List<Propriedade> propriedades) {
        Map<String, Double> areas = new HashMap<>();
        
        for (Propriedade p : propriedades) {
            String municipio = p.getMunicipio();
            if (municipio != null && !municipio.isEmpty()) {
                areas.merge(municipio, p.getShapeArea(), Double::sum);
            }
        }
        
        return areas;
    }

    /**
     * Calcula a área total agrupada por ilha.
     * Retorna um mapa com o nome da ilha como chave e a área total como valor.
     *
     * @param propriedades Lista de propriedades
     * @return Mapa com ilha → área total
     */
    public static Map<String, Double> calcularAreaTotalPorIlha(List<Propriedade> propriedades) {
        Map<String, Double> areas = new HashMap<>();
        
        for (Propriedade p : propriedades) {
            String ilha = p.getIlha();
            if (ilha != null && !ilha.isEmpty()) {
                areas.merge(ilha, p.getShapeArea(), Double::sum);
            }
        }
        
        return areas;
    }

    /**
     * Calcula a área média agrupada por tipo de nível administrativo.
     * Retorna um mapa com o nome da região como chave e a área média como valor.
     *
     * @param propriedades Lista de propriedades
     * @param tipoNivel    Tipo de zona geográfica: "freguesia", "municipio" ou "ilha"
     * @return Mapa com região → área média
     */
    public static Map<String, Double> calcularAreaMediaPorTipo(List<Propriedade> propriedades, 
                                                                String tipoNivel) {
        Map<String, List<Double>> areasAgrupadas = new HashMap<>();
        
        // Agrupar áreas por região
        for (Propriedade p : propriedades) {
            String chave = switch (tipoNivel.toLowerCase()) {
                case "freguesia" -> p.getFreguesia();
                case "municipio" -> p.getMunicipio();
                case "ilha" -> p.getIlha();
                default -> null;
            };
            
            if (chave != null && !chave.isEmpty()) {
                areasAgrupadas.computeIfAbsent(chave, k -> new ArrayList<>())
                             .add(p.getShapeArea());
            }
        }
        
        // Calcular média para cada região
        Map<String, Double> medias = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : areasAgrupadas.entrySet()) {
            double soma = entry.getValue().stream()
                               .mapToDouble(Double::doubleValue)
                               .sum();
            double media = soma / entry.getValue().size();
            medias.put(entry.getKey(), media);
        }
        
        return medias;
    }

    /**
     * Retorna as top N regiões ordenadas por área total (descendente).
     *
     * @param areas Mapa de regiões e áreas
     * @param n     Número de top resultados
     * @return Lista ordenada com top N entries
     */
    public static List<Map.Entry<String, Double>> getTopN(Map<String, Double> areas, int n) {
        return areas.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .toList();
    }
}