package es;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe avançada para cálculo de estatísticas de área de propriedades.
 * 
 * Diferentemente da versão simples (AreaPropriedades), esta classe calcula:
 * - Média, mediana, desvio padrão
 * - Percentis (P25, P50, P75)
 * - Mínimo e máximo
 * - Ranking de regiões
 * - Análise comparativa multi-nível
 */
public class AreaAvancada {

    /**
     * Estatísticas detalhadas de área para uma região.
     */
    public static class EstatisticasArea {
        private final String nomeRegiao;
        private final int quantidadePropriedades;
        private final double areaMedia;
        private final double areaMediana;
        private final double areaTotal;
        private final double areaMinima;
        private final double areaMaxima;
        private final double desvioPadrao;
        private final double percentil25;
        private final double percentil75;
        
        public EstatisticasArea(String nomeRegiao, List<Double> areas) {
            this.nomeRegiao = nomeRegiao;
            this.quantidadePropriedades = areas.size();
            
            // Ordenar para cálculos
            List<Double> areasSorted = new ArrayList<>(areas);
            Collections.sort(areasSorted);
            
            // Área total e média
            this.areaTotal = areas.stream().mapToDouble(Double::doubleValue).sum();
            this.areaMedia = areaTotal / quantidadePropriedades;
            
            // Mediana
            this.areaMediana = calcularMediana(areasSorted);
            
            // Mínimo e máximo
            this.areaMinima = areasSorted.get(0);
            this.areaMaxima = areasSorted.get(areasSorted.size() - 1);
            
            // Desvio padrão
            double somaQuadrados = 0.0;
            for (double area : areas) {
                somaQuadrados += Math.pow(area - areaMedia, 2);
            }
            this.desvioPadrao = Math.sqrt(somaQuadrados / quantidadePropriedades);
            
            // Percentis
            this.percentil25 = calcularPercentil(areasSorted, 25);
            this.percentil75 = calcularPercentil(areasSorted, 75);
        }
        
        private double calcularMediana(List<Double> sorted) {
            int n = sorted.size();
            if (n % 2 == 0) {
                return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
            } else {
                return sorted.get(n / 2);
            }
        }
        
        private double calcularPercentil(List<Double> sorted, int percentil) {
            int n = sorted.size();
            double pos = (percentil / 100.0) * (n - 1);
            int lower = (int) Math.floor(pos);
            int upper = (int) Math.ceil(pos);
            
            if (lower == upper) {
                return sorted.get(lower);
            }
            
            double weight = pos - lower;
            return sorted.get(lower) * (1 - weight) + sorted.get(upper) * weight;
        }
        
        // Getters
        public String getNomeRegiao() { return nomeRegiao; }
        public int getQuantidadePropriedades() { return quantidadePropriedades; }
        public double getAreaMedia() { return areaMedia; }
        public double getAreaMediana() { return areaMediana; }
        public double getAreaTotal() { return areaTotal; }
        public double getAreaMinima() { return areaMinima; }
        public double getAreaMaxima() { return areaMaxima; }
        public double getDesvioPadrao() { return desvioPadrao; }
        public double getPercentil25() { return percentil25; }
        public double getPercentil75() { return percentil75; }
        
        @Override
        public String toString() {
            return String.format(
                "📊 Estatísticas para %s\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "Propriedades: %d\n" +
                "Área Total: %.2f m²\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "Média: %.2f m²\n" +
                "Mediana: %.2f m²\n" +
                "Desvio Padrão: %.2f m²\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "Mínimo: %.2f m²\n" +
                "P25: %.2f m²\n" +
                "P75: %.2f m²\n" +
                "Máximo: %.2f m²",
                nomeRegiao, quantidadePropriedades, areaTotal,
                areaMedia, areaMediana, desvioPadrao,
                areaMinima, percentil25, percentil75, areaMaxima
            );
        }
    }
    
    /**
     * Calcula estatísticas avançadas de área para uma região específica.
     *
     * @param propriedades Lista completa de propriedades
     * @param tipoNivel    Tipo de zona geográfica: "freguesia", "municipio" ou "ilha"
     * @param valorNivel   Nome da região específica
     * @return Estatísticas detalhadas ou null se não houver dados
     */
    public static EstatisticasArea calcularEstatisticasAvancadas(
            List<Propriedade> propriedades,
            String tipoNivel,
            String valorNivel) {
        
        List<Double> areas = propriedades.stream()
            .filter(p -> {
                String valor = switch (tipoNivel.toLowerCase()) {
                    case "freguesia" -> p.getFreguesia();
                    case "municipio" -> p.getMunicipio();
                    case "ilha" -> p.getIlha();
                    default -> null;
                };
                return valor != null && valor.equalsIgnoreCase(valorNivel);
            })
            .map(Propriedade::getShapeArea)
            .collect(Collectors.toList());
        
        if (areas.isEmpty()) {
            return null;
        }
        
        return new EstatisticasArea(valorNivel, areas);
    }
    
    /**
     * Calcula estatísticas avançadas para todas as regiões de um tipo.
     *
     * @param propriedades Lista de propriedades
     * @param tipoNivel    Tipo de zona: "freguesia", "municipio" ou "ilha"
     * @return Mapa com região → estatísticas
     */
    public static Map<String, EstatisticasArea> calcularTodasEstatisticas(
            List<Propriedade> propriedades,
            String tipoNivel) {
        
        // Agrupar áreas por região
        Map<String, List<Double>> areasPorRegiao = new HashMap<>();
        
        for (Propriedade p : propriedades) {
            String regiao = switch (tipoNivel.toLowerCase()) {
                case "freguesia" -> p.getFreguesia();
                case "municipio" -> p.getMunicipio();
                case "ilha" -> p.getIlha();
                default -> null;
            };
            
            if (regiao != null && !regiao.isEmpty()) {
                areasPorRegiao.computeIfAbsent(regiao, k -> new ArrayList<>())
                             .add(p.getShapeArea());
            }
        }
        
        // Calcular estatísticas para cada região
        Map<String, EstatisticasArea> resultado = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : areasPorRegiao.entrySet()) {
            resultado.put(entry.getKey(), new EstatisticasArea(entry.getKey(), entry.getValue()));
        }
        
        return resultado;
    }
    
    /**
     * Retorna o ranking das top N regiões por área média (ordem decrescente).
     *
     * @param estatisticas Mapa de estatísticas por região
     * @param n           Número de top resultados
     * @return Lista ordenada das top N regiões
     */
    public static List<EstatisticasArea> getRankingPorAreaMedia(
            Map<String, EstatisticasArea> estatisticas, int n) {
        
        return estatisticas.values().stream()
            .sorted((e1, e2) -> Double.compare(e2.getAreaMedia(), e1.getAreaMedia()))
            .limit(n)
            .collect(Collectors.toList());
    }
    
    /**
     * Retorna o ranking das top N regiões por área total (ordem decrescente).
     *
     * @param estatisticas Mapa de estatísticas por região
     * @param n           Número de top resultados
     * @return Lista ordenada das top N regiões
     */
    public static List<EstatisticasArea> getRankingPorAreaTotal(
            Map<String, EstatisticasArea> estatisticas, int n) {
        
        return estatisticas.values().stream()
            .sorted((e1, e2) -> Double.compare(e2.getAreaTotal(), e1.getAreaTotal()))
            .limit(n)
            .collect(Collectors.toList());
    }
    
    /**
     * Retorna o ranking das top N regiões por número de propriedades (ordem decrescente).
     *
     * @param estatisticas Mapa de estatísticas por região
     * @param n           Número de top resultados
     * @return Lista ordenada das top N regiões
     */
    public static List<EstatisticasArea> getRankingPorQuantidade(
            Map<String, EstatisticasArea> estatisticas, int n) {
        
        return estatisticas.values().stream()
            .sorted((e1, e2) -> Integer.compare(
                e2.getQuantidadePropriedades(), 
                e1.getQuantidadePropriedades()))
            .limit(n)
            .collect(Collectors.toList());
    }
    
    /**
     * Gera relatório comparativo entre duas regiões.
     *
     * @param est1 Estatísticas da primeira região
     * @param est2 Estatísticas da segunda região
     * @return String formatada com comparação
     */
    public static String compararRegioes(EstatisticasArea est1, EstatisticasArea est2) {
        double diffMedia = ((est1.getAreaMedia() - est2.getAreaMedia()) / est2.getAreaMedia()) * 100;
        double diffTotal = ((est1.getAreaTotal() - est2.getAreaTotal()) / est2.getAreaTotal()) * 100;
        int diffQtd = est1.getQuantidadePropriedades() - est2.getQuantidadePropriedades();
        
        return String.format(
            "📊 Comparação: %s vs %s\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Área Média: %+.1f%%\n" +
            "Área Total: %+.1f%%\n" +
            "Propriedades: %+d\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "%s tem %.0f%% mais propriedades\n" +
            "%s tem área média %.0f%% maior",
            est1.getNomeRegiao(), est2.getNomeRegiao(),
            diffMedia, diffTotal, diffQtd,
            diffQtd > 0 ? est1.getNomeRegiao() : est2.getNomeRegiao(),
            Math.abs(diffQtd * 100.0 / est2.getQuantidadePropriedades()),
            diffMedia > 0 ? est1.getNomeRegiao() : est2.getNomeRegiao(),
            Math.abs(diffMedia)
        );
    }
}