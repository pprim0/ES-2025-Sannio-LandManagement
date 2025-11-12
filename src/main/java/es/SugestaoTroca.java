package es;

import java.util.*;

/**
 * Representa uma sugestão de troca entre duas propriedades de diferentes proprietários,
 * com base na similaridade das suas áreas e no ganho potencial na média das áreas dos proprietários.
 *
 * Esta versão simples usa apenas o critério da área para calcular o score.
 */
public class SugestaoTroca {

    /** Primeira propriedade envolvida na troca */
    private Propriedade p1;

    /** Segunda propriedade envolvida na troca */
    private Propriedade p2;

    /** Ganho total na média de áreas dos dois proprietários caso a troca ocorra */
    private double ganhoAreaMedia;

    /** Score final da troca, baseado nos critérios definidos (nesta versão: apenas área) */
    private double scoreFinal;

    /** Score baseado na similaridade da área */
    private double scoreArea;

    /** Score baseado na similaridade do perímetro (não usado nesta versão) */
    private double scorePerimetro;

    /** Score baseado na coerência territorial (não usado nesta versão) */
    private double scoreCoerencia;

    /**
     * Construtor da sugestão de troca.
     *
     * @param p1             Primeira propriedade
     * @param p2             Segunda propriedade
     * @param ganhoAreaMedia Ganho esperado na média das áreas
     * @param scoreFinal     Score total da sugestão
     */
    public SugestaoTroca(Propriedade p1, Propriedade p2, double ganhoAreaMedia, double scoreFinal) {
        this.p1 = p1;
        this.p2 = p2;
        this.ganhoAreaMedia = ganhoAreaMedia;
        this.scoreFinal = scoreFinal;
    }

    // Getters
    public Propriedade getP1() { return p1; }
    public Propriedade getP2() { return p2; }
    public double getGanhoAreaMedia() { return ganhoAreaMedia; }
    public double getScoreFinal() { return scoreFinal; }
    public double getScoreArea() { return scoreArea; }
    public double getScorePerimetro() { return scorePerimetro; }
    public double getScoreCoerencia() { return scoreCoerencia; }

    /**
     * Define os detalhes da sugestão em termos de scores parciais.
     *
     * @param scoreArea       Similaridade da área
     * @param scorePerimetro  Similaridade do perímetro
     * @param scoreCoerencia  Grau de coerência territorial
     */
    public void setDetalhes(double scoreArea, double scorePerimetro, double scoreCoerencia) {
        this.scoreArea = scoreArea;
        this.scorePerimetro = scorePerimetro;
        this.scoreCoerencia = scoreCoerencia;
    }

    /**
     * Representação textual da sugestão de troca, incluindo área, score e detalhes.
     *
     * @return String com descrição da troca
     */
   @Override
    public String toString() {
        return String.format(Locale.US,
                "Trocar [%s - %.2fm2] com [%s - %.2fm2] | Ganho: %.2f | Score: %.2f\n - Area: %.2f | Perimetro: %.2f | Coerencia: %.2f",
                p1.getOwner(), p1.getShapeArea(),
                p2.getOwner(), p2.getShapeArea(),
                ganhoAreaMedia, scoreFinal,
                scoreArea, scorePerimetro, scoreCoerencia
        );
    }

    /**
     * Gera uma lista de sugestões de troca para o proprietário indicado,
     * considerando apenas trocas com outros proprietários com áreas semelhantes.
     *
     * @param propriedades Lista de todas as propriedades
     * @param donoAlvo     Proprietário para o qual gerar sugestões
     * @return Lista de sugestões ordenadas por score decrescente
     */
    public static List<SugestaoTroca> gerar(List<Propriedade> propriedades, String donoAlvo) {
        List<SugestaoTroca> sugestoes = new ArrayList<>();

        Map<String, List<Propriedade>> porDono = new HashMap<>();
        Map<String, EstatisticasProprietario> estatisticas = new HashMap<>();

        for (Propriedade p : propriedades) {
            porDono.computeIfAbsent(p.getOwner(), k -> new ArrayList<>()).add(p);
            estatisticas.computeIfAbsent(p.getOwner(), k -> new EstatisticasProprietario()).adicionar(p);
        }

        for (int i = 0; i < propriedades.size(); i++) {
            Propriedade p1 = propriedades.get(i);
            if (!p1.getOwner().equals(donoAlvo)) continue;

            for (int j = i + 1; j < propriedades.size(); j++) {
                Propriedade p2 = propriedades.get(j);
                if (p1.getOwner().equals(p2.getOwner())) continue;

                double diffArea = Math.abs(p1.getShapeArea() - p2.getShapeArea()) /
                        Math.max(p1.getShapeArea(), p2.getShapeArea());

                if (diffArea > 0.3) continue;

                double scoreArea = 1 - diffArea;
                double scoreFinal = scoreArea;
                double ganho = calcularGanhoAreaMedia(p1, p2, estatisticas);

                SugestaoTroca s = new SugestaoTroca(p1, p2, ganho, scoreFinal);
                s.setDetalhes(scoreArea, 0.0, 0.0);
                sugestoes.add(s);
            }
        }

        sugestoes.sort(Comparator.comparingDouble(s -> -s.scoreFinal));
        return sugestoes;
    }

    /**
     * Classe interna auxiliar para guardar estatísticas de um proprietário
     * (número de propriedades e soma das áreas).
     */
    public static class EstatisticasProprietario {
        public double somaArea;
        public int total;

        public EstatisticasProprietario() {
            this.somaArea = 0;
            this.total = 0;
        }

        /**
         * Adiciona uma propriedade às estatísticas do proprietário.
         * @param p Propriedade a incluir
         */
        public void adicionar(Propriedade p) {
            somaArea += p.getShapeArea();
            total++;
        }

        /**
         * Calcula a média de área das propriedades do proprietário.
         * @return Média da área
         */
        public double media() {
            return total == 0 ? 0 : somaArea / total;
        }
    }

    /**
     * Calcula o ganho total de média de área para dois proprietários,
     * caso façam a troca entre duas propriedades.
     *
     * @param p1            Primeira propriedade
     * @param p2            Segunda propriedade
     * @param estatisticas  Mapa com estatísticas por proprietário
     * @return Diferença total na média de áreas (positiva = melhoria)
     */
    private static double calcularGanhoAreaMedia(Propriedade p1, Propriedade p2,
                                                 Map<String, EstatisticasProprietario> estatisticas) {
        EstatisticasProprietario e1 = estatisticas.get(p1.getOwner());
        EstatisticasProprietario e2 = estatisticas.get(p2.getOwner());

        double atual1 = e1.media();
        double atual2 = e2.media();

        double nova1 = (e1.somaArea - p1.getShapeArea() + p2.getShapeArea()) / e1.total;
        double nova2 = (e2.somaArea - p2.getShapeArea() + p1.getShapeArea()) / e2.total;

        return (nova1 - atual1) + (nova2 - atual2);
    }
}
