package es;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Locale;

/**
 * Classe responsável por exportar visualmente um grafo de adjacências entre propriedades
 * rústicas para ficheiros HTML e JS, com base na sua geometria espacial.
 *
 * Os nós são posicionados com base no centróide da geometria WKT e representados numa rede
 * interativa usando a biblioteca Vis.js.
 */
public class ExportadorAdjacenciasHTML {

    /**
     * Representa um nó do grafo (propriedade) com posição, rótulo e título.
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
         * @param id    ID da propriedade
         * @param label Texto exibido no nó
         * @param x     Coordenada X (centróide)
         * @param y     Coordenada Y (centróide invertido)
         * @param size  Tamanho do nó (escala pela área)
         * @param title Texto mostrado no alerta ao clicar
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
     * Representa uma aresta entre duas propriedades adjacentes.
     */
    static class Aresta {
        int from;
        int to;

        /**
         * Construtor da aresta.
         *
         * @param from ID da propriedade de origem
         * @param to   ID da propriedade de destino
         */
        public Aresta(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    /**
     * Exporta um grafo de adjacência de propriedades rústicas para ficheiros HTML e JS.
     * Os nós são baseados no centróide da geometria de cada propriedade e as ligações são
     * criadas com base nas adjacências do grafo.
     *
     * @param propriedades Lista de propriedades com geometria
     * @param grafo        Mapa de adjacências (ID -> conjunto de IDs vizinhos)
     * @param ficheiroHTML Caminho do ficheiro de saída HTML
     * @param ficheiroJS   Caminho do ficheiro de saída JS
     * @param limite       Número máximo de propriedades a incluir (para limitar visualização)
     */
    public static void exportar(List<Propriedade> propriedades, Map<Integer, Set<Integer>> grafo,
                                String ficheiroHTML, String ficheiroJS, int limite) {

        WKTReader reader = new WKTReader();
        Map<Integer, Propriedade> mapaProps = new HashMap<>();
        for (Propriedade p : propriedades) {
            mapaProps.put(p.getObjectId(), p);
        }

        List<No> nos = new ArrayList<>();
        List<Aresta> arestas = new ArrayList<>();
        Set<Integer> adicionados = new HashSet<>();
        int contador = 0;

        // Criar nós com base no centróide das geometrias
        for (Propriedade prop : propriedades) {
            if (contador >= limite) break;

            try {
                Geometry geom = reader.read(prop.getGeometry());
                if (!geom.isValid()) continue;

                Point centroide = geom.getCentroid();
                double x = centroide.getX();
                double y = -centroide.getY();  // eixo invertido para visualização

                double size = Math.min(50, Math.max(10, prop.getShapeArea() / 1000));
                String title = String.format(Locale.US,
                        "ID: %d | Dono: %s | Area: %.2f m2 | Freguesia: %s | Municipio: %s",
                        prop.getObjectId(),
                        prop.getOwner().replace("\"", "'"),
                        prop.getShapeArea(),
                        prop.getFreguesia().replace("\"", "'"),
                        prop.getMunicipio().replace("\"", "'"));

                nos.add(new No(prop.getObjectId(), String.valueOf(prop.getObjectId()), x, y, size, title));
                adicionados.add(prop.getObjectId());
                contador++;

            } catch (Exception ignored) {
            }
        }

        // Adicionar arestas entre propriedades adjacentes
        for (Integer origem : grafo.keySet()) {
            if (!adicionados.contains(origem)) continue;
            for (Integer destino : grafo.get(origem)) {
                if (adicionados.contains(destino)) {
                    arestas.add(new Aresta(origem, destino));
                }
            }
        }

        // Gerar ficheiro JS com os dados de nós e arestas
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        try (FileWriter writer = new FileWriter(ficheiroJS)) {
            writer.write("const nodes = ");
            writer.write(gson.toJson(nos));
            writer.write(";\n\nconst edges = ");
            writer.write(gson.toJson(arestas));
            writer.write(";");
        } catch (IOException e) {
            System.out.println("Erro ao escrever ficheiro JS: " + e.getMessage());
        }

        // Gerar ficheiro HTML com o grafo interativo
        try (FileWriter html = new FileWriter(ficheiroHTML)) {
            html.write("""  
                    <!DOCTYPE html>
                    <html lang="pt">
                    <head>
                      <meta charset="utf-8" />
                      <meta name="google" content="notranslate">
                      <title>Grafo de adjacencias</title>
                      <script type="text/javascript" src="https://unpkg.com/vis-network@9.1.2/dist/vis-network.min.js"></script>
                      <style>
                        #network {
                          width: 100%;
                          height: 90vh;
                          border: 1px solid lightgray;
                        }
                      </style>
                    </head>
                    <body>
                      <h2>Grafo de Propriedades Rusticas</h2>
                      <div id="network"></div>
                      <script src="adjacencias.js"></script>
                      <script type="text/javascript">
                        const container = document.getElementById("network");
                        const data = {
                          nodes: new vis.DataSet(nodes),
                          edges: new vis.DataSet(edges)
                        };
                        const options = {
                          physics: false,
                          interaction: {
                            dragNodes: false,
                            dragView: true,
                            zoomView: true
                          },
                          nodes: {
                            shape: "dot",
                            font: {
                              size: 16,
                              face: "arial",
                              color: "#000000"
                            },
                            color: "#97C2FC"
                          },
                          edges: {
                            color: "#cccccc"
                          }
                        };
                        const network = new vis.Network(container, data, options);
                        network.once("afterDrawing", function () {
                          network.fit({
                            animation: {
                              duration: 800,
                              easingFunction: "easeInOutQuad"
                            },
                            maxZoomLevel: 2
                          });
                        });
                        network.on("click", function (params) {
                          if (params.nodes.length > 0) {
                            const nodeId = params.nodes[0];
                            const node = nodes.find(n => n.id === nodeId);
                            if (node && node.title) {
                              alert(node.title);
                            }
                          }
                        });
                      </script>
                    </body>
                    </html>
                    """);
        } catch (IOException e) {
            System.out.println("Erro ao escrever HTML: " + e.getMessage());
        }

        System.out.println("Grafo exportado com sucesso! Abre o ficheiro: " + ficheiroHTML);
    }
}