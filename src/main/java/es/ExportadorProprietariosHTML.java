package es;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Classe responsável por exportar um grafo de conexões entre proprietários para
 * ficheiros HTML e JS, permitindo visualização interativa com a biblioteca Vis.js.
 *
 * Cada proprietário é representado como um nó e as conexões representam
 * relações indiretas com base em propriedades adjacentes.
 */
public class ExportadorProprietariosHTML {

    /**
     * Representa um nó do grafo de proprietários, com posição, rótulo e título.
     */
    static class No {
        int id;
        String label;
        double x;
        double y;
        double size;
        String title;

        /**
         * Construtor do nó.
         *
         * @param id    ID do proprietário (convertido de String para int)
         * @param label Texto visível no nó
         * @param x     Posição X (não utilizada na renderização, mas necessária)
         * @param y     Posição Y (não utilizada na renderização, mas necessária)
         * @param size  Tamanho do nó
         * @param title Texto mostrado ao clicar no nó
         */
        public No(int id, String label, double x, double y, double size, String title) {
            this.id = id;
            this.label = label;
            this.x = x;
            this.y = y;
            this.size = size;
            this.title = title;
        }
    }

    /**
     * Representa uma ligação (aresta) entre dois proprietários.
     */
    static class Aresta {
        int from;
        int to;

        /**
         * Construtor da aresta.
         *
         * @param from ID do proprietário de origem
         * @param to   ID do proprietário de destino
         */
        public Aresta(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    /**
     * Exporta o grafo de conexões entre proprietários para ficheiros HTML e JS,
     * criando uma visualização interativa com base na estrutura do grafo fornecida.
     *
     * @param grafoProprietarios Mapa de conexões entre proprietários (ID em String)
     * @param pastaSaida         Pasta onde serão guardados os ficheiros
     * @param nomeHTML           Nome do ficheiro HTML a gerar
     * @param nomeJS             Nome do ficheiro JS a gerar
     */
    public static void exportar(Map<String, Set<String>> grafoProprietarios,
                                String pastaSaida, String nomeHTML, String nomeJS) {

        List<No> nos = new ArrayList<>();
        List<Aresta> arestas = new ArrayList<>();

        Map<String, Integer> donoToId = new HashMap<>();
        Map<Integer, String> idToDono = new HashMap<>();

        // Conversão de IDs de String para inteiros e criação dos nós
        for (String dono : grafoProprietarios.keySet()) {
            try {
                int id = Integer.parseInt(dono.trim());
                donoToId.put(dono, id);
                idToDono.put(id, dono);
                nos.add(new No(id, String.valueOf(id), 0, 0, 25, "Proprietario: " + dono));
            } catch (NumberFormatException ignored) {}
        }

        // Criar arestas entre os proprietários conectados (evitando duplicadas)
        for (Map.Entry<String, Set<String>> entry : grafoProprietarios.entrySet()) {
            Integer origemId = donoToId.get(entry.getKey());
            if (origemId == null) continue;
            for (String vizinho : entry.getValue()) {
                Integer destinoId = donoToId.get(vizinho);
                if (destinoId != null && origemId < destinoId) {
                    arestas.add(new Aresta(origemId, destinoId));
                }
            }
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        String caminhoJS = pastaSaida + nomeJS;
        String caminhoHTML = pastaSaida + nomeHTML;

        // Gerar ficheiro JS com nós e arestas
        try (FileWriter writer = new FileWriter(caminhoJS)) {
            writer.write("const nodes = ");
            writer.write(gson.toJson(nos));
            writer.write(";\n\nconst allEdges = ");
            writer.write(gson.toJson(arestas));
            writer.write(";");
        } catch (IOException e) {
            System.out.println("Erro ao escrever ficheiro JS: " + e.getMessage());
        }

        // Gerar ficheiro HTML com o grafo e interações
        try (FileWriter html = new FileWriter(caminhoHTML)) {
            html.write(String.format("""  
                <!DOCTYPE html>
                <html lang="pt">
                <head>
                  <meta charset="utf-8" />
                  <title>Grafo de Proprietarios</title>
                  <script type="text/javascript" src="https://unpkg.com/vis-network@9.1.2/dist/vis-network.min.js"></script>
                  <style>
                    #network {
                      width: 100%%;
                      height: 90vh;
                      border: 1px solid lightgray;
                    }
                  </style>
                </head>
                <body>
                  <h2>Grafo de Proprietarios</h2>
                  <div id="network"></div>
                  <script src="%s"></script>
                  <script type="text/javascript">
                    const container = document.getElementById("network");

                    const data = {
                      nodes: new vis.DataSet(nodes.map(n => ({...n, color: "#FF9900"}))),
                      edges: new vis.DataSet([])
                    };

                    const options = {
                      physics: {
                        enabled: true,
                        solver: "barnesHut",
                        barnesHut: {
                          gravitationalConstant: -500000,
                          springLength: 1200,
                          springConstant: 0.001
                        },
                        stabilization: {
                          enabled: true,
                          iterations: 300,
                          updateInterval: 25
                        }
                      },
                      nodes: {
                        shape: "dot",
                        size: 18,
                        font: {
                          size: 18,
                          face: "arial"
                        }
                      },
                      edges: {
                        smooth: false
                      },
                      interaction: {
                        hover: true,
                        tooltipDelay: 50,
                        dragNodes: true,
                        zoomView: true
                      }
                    };

                    const network = new vis.Network(container, data, options);

                    network.once("stabilizationIterationsDone", function () {
                      network.setOptions({ physics: false });
                      network.fit();
                    });

                    network.on("click", function (params) {
                      data.edges.clear();
                      if (params.nodes.length > 0) {
                        const nodeId = params.nodes[0];
                        const novas = allEdges.filter(e => e.from === nodeId || e.to === nodeId)
                          .map(e => ({...e, color: { color: "#0066CC" }}));

                        const vizinhos = novas.map(e => e.from === nodeId ? e.to : e.from);
                        const node = data.nodes.get(nodeId);
                        const label = node?.label || ("ID " + nodeId);

                        const vizinhosNomes = vizinhos.map(id => {
                          const n = data.nodes.get(id);
                          return n && n.title ? n.title.replace("Proprietario: ", "") : id.toString();
                        });

                        const vizinhosOrdenados = vizinhosNomes
                          .map(v => isNaN(v) ? v : parseInt(v))
                          .sort((a, b) => a - b)
                          .map(v => v.toString());

                        const vizinhosStr = vizinhosOrdenados.length > 0
                          ? "Vizinhos: " + vizinhosOrdenados.join(", ")
                          : "Sem vizinhos.";

                        alert("Proprietario: " + label + "\\n" + vizinhosStr);
                        data.edges.add(novas);
                      }
                    });
                  </script>
                </body>
                </html>
                """, nomeJS));
        } catch (IOException e) {
            System.out.println("Erro ao escrever HTML: " + e.getMessage());
        }

        System.out.println("Grafo de proprietarios exportado com sucesso para: " + caminhoHTML);
    }
}