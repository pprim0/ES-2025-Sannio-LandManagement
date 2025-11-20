package es;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsável por gerar sugestões de troca avançadas entre propriedades rústicas,
 * tendo em conta múltiplos critérios: área, perímetro e coerência territorial (mesmo concelho).
 *
 * A troca é apenas sugerida se o score combinado dos três critérios for igual ou superior a 0.7.
 */
public class SugestaoTrocaAvancada {

    /**
     * Gera uma lista de sugestões de troca avançadas para o proprietário especificado.
     * Cada sugestão avalia similaridade de área, perímetro e localização territorial (concelho).
     *
     * @param propriedades Lista de todas as propriedades
     * @param donoAlvo     Proprietário para o qual se deseja gerar sugestões
     * @return Lista ordenada de sugestões de troca com score elevado
     */
    public static List<SugestaoTroca> gerar(List<Propriedade> propriedades, String donoAlvo) {
        List<SugestaoTroca> sugestoes = new ArrayList<>();

        Map<String, List<Propriedade>> porDono = new HashMap<>();
        Map<String, SugestaoTroca.EstatisticasProprietario> estatisticas = new HashMap<>();

        // Agrupar propriedades por dono e calcular estatísticas de área
        for (Propriedade p : propriedades) {
            porDono.computeIfAbsent(p.getOwner(), k -> new ArrayList<>()).add(p);
            estatisticas.computeIfAbsent(p.getOwner(), k -> new SugestaoTroca.EstatisticasProprietario()).adicionar(p);
        }

        // Iterar sobre pares de propriedades para avaliar trocas
        for (int i = 0; i < propriedades.size(); i++) {
            Propriedade p1 = propriedades.get(i);
            if (!p1.getOwner().equals(donoAlvo)){continue;}

            for (int j = 0; j < propriedades.size(); j++) {
                Propriedade p2 = propriedades.get(j);
                if (p1.getOwner().equals(p2.getOwner())) {continue;}

                // 1. Similaridade de área
                double diffArea = Math.abs(p1.getShapeArea() - p2.getShapeArea()) /
                        Math.max(p1.getShapeArea(), p2.getShapeArea());
                if (diffArea > 0.4) {continue;}
                double scoreArea = 1 - diffArea;

                // 2. Similaridade de perímetro
                double diffPerimetro = Math.abs(p1.getShapeLength() - p2.getShapeLength()) /
                        Math.max(p1.getShapeLength(), p2.getShapeLength());
                double scorePerimetro = 1 - diffPerimetro;

                // 3. Coerência territorial (mesmo concelho)
                double scoreCoerencia = calcularCoerenciaConcelho(p1, p2, porDono);

                // Score final com pesos equilibrados
                double scoreFinal = 0.34 * scoreArea + 0.33 * scorePerimetro + 0.33 * scoreCoerencia;

                // Apenas trocas com score alto são consideradas
                if (scoreFinal >= 0.7) {
                    double ganho = calcularGanhoAreaMedia(p1, p2, estatisticas);
                    SugestaoTroca s = new SugestaoTroca(p1, p2, ganho, scoreFinal);
                    s.setDetalhes(scoreArea, scorePerimetro, scoreCoerencia);
                    sugestoes.add(s);
                }
            }
        }

        // Ordenar por score decrescente
        sugestoes.sort(Comparator.comparingDouble(s -> -s.getScoreFinal()));
        return sugestoes;
    }

    /**
     * Calcula a coerência territorial entre duas propriedades com base nos concelhos dos respetivos donos.
     * Verifica se cada dono já possui propriedades no concelho do outro.
     *
     * @param p1       Primeira propriedade
     * @param p2       Segunda propriedade
     * @param porDono  Mapa de propriedades por dono
     * @return Score de coerência territorial entre 0.0 e 1.0
     */
    private static double calcularCoerenciaConcelho(Propriedade p1, Propriedade p2,
                                                    Map<String, List<Propriedade>> porDono) {
        String concelhoP1 = p1.getMunicipio();
        String concelhoP2 = p2.getMunicipio();

        long countP1ConcelhoP2 = porDono.get(p1.getOwner()).stream()
                .filter(p -> p.getMunicipio().equalsIgnoreCase(concelhoP2))
                .count();

        long countP2ConcelhoP1 = porDono.get(p2.getOwner()).stream()
                .filter(p -> p.getMunicipio().equalsIgnoreCase(concelhoP1))
                .count();

        double normalizadoP1 = countP1ConcelhoP2 / (double) porDono.get(p1.getOwner()).size();
        double normalizadoP2 = countP2ConcelhoP1 / (double) porDono.get(p2.getOwner()).size();

        return (normalizadoP1 + normalizadoP2) / 2.0;
    }

    /**
     * Calcula o ganho total de média de área para dois proprietários se trocarem as propriedades indicadas.
     *
     * @param p1           Primeira propriedade
     * @param p2           Segunda propriedade
     * @param estatisticas Mapa com estatísticas de área por proprietário
     * @return Variação somada nas médias de área dos dois proprietários
     */
    private static double calcularGanhoAreaMedia(Propriedade p1, Propriedade p2,
                                                 Map<String, SugestaoTroca.EstatisticasProprietario> estatisticas) {
        var e1 = estatisticas.get(p1.getOwner());
        var e2 = estatisticas.get(p2.getOwner());

        double atual1 = e1.media();
        double atual2 = e2.media();

        double nova1 = (e1.somaArea - p1.getShapeArea() + p2.getShapeArea()) / e1.total;
        double nova2 = (e2.somaArea - p2.getShapeArea() + p1.getShapeArea()) / e2.total;

        return (nova1 - atual1) + (nova2 - atual2);
    }
}