package es;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Classe utilitária que fornece métodos para o cálculo de métricas relacionadas
 * à área de propriedades, considerando conexidade através de um grafo de adjacências.
 */
public class AreaAvancada {

    /**
     * Calcula a área média corrigida de propriedades numa dada zona geográfica,
     * considerando que propriedades adjacentes pertencentes ao mesmo proprietário
     * formam um grupo único (componente conexo).
     *
     * <p>Exemplo: se um proprietário tiver 3 propriedades ligadas entre si,
     * essas 3 serão somadas como uma só unidade no cálculo da média.</p>
     *
     * @param propriedades      Lista total de propriedades
     * @param tipoNivel         Tipo de divisão geográfica: "freguesia", "municipio" ou "ilha"
     * @param valorNivel        Nome da zona geográfica a analisar (ex: "Funchal")
     * @param grafoAdjacencia   Mapa de adjacências (ObjectID → conjunto de vizinhos)
     * @return Área média corrigida por proprietário, considerando grupos adjacentes
     */
    public static double calcularAreaMediaCorrigida(List<Propriedade> propriedades,
                                                    String tipoNivel, String valorNivel,
                                                    Map<Integer, Set<Integer>> grafoAdjacencia) {

        // 1. Filtrar propriedades que pertencem à zona geográfica pedida
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> {
                    return switch (tipoNivel.toLowerCase()) {
                        case "ilha" -> p.getIlha().equalsIgnoreCase(valorNivel);
                        case "municipio" -> p.getMunicipio().equalsIgnoreCase(valorNivel);
                        case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valorNivel);
                        default -> false;
                    };
                })
                .toList();

        // 2. Agrupar propriedades por proprietário
        Map<String, List<Propriedade>> porProprietario = new HashMap<>();
        for (Propriedade p : filtradas) {
            porProprietario.computeIfAbsent(p.getOwner(), k -> new ArrayList<>()).add(p);
        }

        int totalGrupos = 0;
        double somaAreas = 0;

        // 3. Para cada proprietário, identificar grupos adjacentes (componentes conexas)
        for (Map.Entry<String, List<Propriedade>> entry : porProprietario.entrySet()) {
            List<Propriedade> props = entry.getValue();
            Set<Integer> visitados = new HashSet<>();

            for (Propriedade p : props) {
                if (!visitados.contains(p.getObjectId())) {
                    // Início de um novo grupo de propriedades conectadas
                    double areaGrupo = 0;
                    Stack<Integer> stack = new Stack<>();
                    stack.push(p.getObjectId());

                    while (!stack.isEmpty()) {
                        int atualId = stack.pop();
                        if (!visitados.add(atualId)) {continue;}

                        // Obter a propriedade atual pelo ID
                        Propriedade atual = props.stream()
                                .filter(x -> x.getObjectId() == atualId)
                                .findFirst().orElse(null);

                        if (atual != null) {
                            areaGrupo += atual.getShapeArea();

                            // Adicionar vizinhos do mesmo dono à stack
                            for (int vizinhoId : grafoAdjacencia.getOrDefault(atualId, Set.of())) {
                                boolean vizinhoEhDoMesmo = props.stream()
                                        .anyMatch(x -> x.getObjectId() == vizinhoId);
                                if (vizinhoEhDoMesmo) {stack.push(vizinhoId);}
                            }
                        }
                    }

                    // Grupo completo → soma a área total
                    somaAreas += areaGrupo;
                    totalGrupos++;
                }
            }
        }

        // 4. Calcular média (dividindo a soma total pelo número de grupos)
        return (totalGrupos > 0) ? somaAreas / totalGrupos : 0.0;
    }
}
