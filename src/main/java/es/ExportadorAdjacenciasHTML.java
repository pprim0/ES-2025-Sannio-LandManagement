package es;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Classe responsável por exportar visualmente um grafo de adjacências entre
 * propriedades
 * rústicas para ficheiros HTML e JS, com base na sua geometria espacial.
 *
 * Os nós são posicionados com base no centróide da geometria WKT e
 * representados numa rede
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
   * Exporta um grafo de adjacência de propriedades rústicas para ficheiros HTML e
   * JS.
   * Os nós são baseados no centróide da geometria de cada propriedade e as
   * ligações são
   * criadas com base nas adjacências do grafo.
   *
   * @param propriedades Lista de propriedades com geometria
   * @param grafo        Mapa de adjacências (ID -> conjunto de IDs vizinhos)
   * @param ficheiroHTML Caminho do ficheiro de saída HTML
   * @param ficheiroJS   Caminho do ficheiro de saída JS
   * @param limite       Número máximo de propriedades a incluir (para limitar
   *                     visualização)
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
      if (contador >= limite)
        break;

      try {
        Geometry geom = reader.read(prop.getGeometry());
        if (!geom.isValid())
          continue;

        Point centroide = geom.getCentroid();
        double x = centroide.getX();
        double y = -centroide.getY(); // eixo invertido para visualização

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
      if (!adicionados.contains(origem))
        continue;
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
      html.write("<!DOCTYPE html>\n");
      html.write("<html lang=\"pt\">\n<head>\n");
      html.write("  <meta charset=\"utf-8\" />\n");
      html.write("  <title>Grafo de Adjacências</title>\n");
      html.write("  <script src=\"https://unpkg.com/vis-network@9.1.2/dist/vis-network.min.js\"></script>\n");
      html.write("  <style>\n");
      html.write("    * { margin: 0; padding: 0; box-sizing: border-box; }\n");
      html.write("    body { font-family: 'Segoe UI', sans-serif; background: #f5f5f5; padding: 20px; }\n");
      html.write(
          "    header { background: white; padding: 20px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
      html.write("    h2 { color: #333; margin-bottom: 10px; }\n");
      html.write("    .stats { display: flex; gap: 30px; margin-top: 10px; color: #666; }\n");
      html.write("    .stat strong { color: #667eea; font-size: 18px; }\n");
      html.write(
          "    .controls { background: white; padding: 15px; border-radius: 10px; margin-bottom: 20px; display: flex; gap: 10px; flex-wrap: wrap; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
      html.write(
          "    .btn { padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 14px; background: #667eea; color: white; transition: all 0.3s; }\n");
      html.write("    .btn:hover { background: #5568d3; transform: translateY(-2px); }\n");
      html.write("    .btn-secondary { background: #6c757d; }\n");
      html.write("    .btn-secondary:hover { background: #5a6268; }\n");
      html.write(
          "    #search { padding: 10px; border: 2px solid #ddd; border-radius: 5px; font-size: 14px; width: 250px; }\n");
      html.write(
          "    #network { width: 100%; height: calc(100vh - 280px); border: 1px solid #ddd; border-radius: 10px; background: white; }\n");
      html.write("  </style>\n</head>\n<body>\n");
      html.write("  <header>\n");
      html.write("    <h2>📍 Grafo de Adjacências de Propriedades</h2>\n");
      html.write("    <div class=\"stats\">\n");
      html.write("      <div class=\"stat\"><strong>" + nos.size() + "</strong> propriedades</div>\n");
      html.write("      <div class=\"stat\"><strong>" + arestas.size() + "</strong> adjacências</div>\n");
      html.write("    </div>\n");
      html.write("  </header>\n");
      html.write("  <div class=\"controls\">\n");
      html.write("    <button class=\"btn\" onclick=\"zoomIn()\">🔍 Zoom +</button>\n");
      html.write("    <button class=\"btn\" onclick=\"zoomOut()\">🔍 Zoom -</button>\n");
      html.write("    <button class=\"btn btn-secondary\" onclick=\"resetView()\">↺ Reset</button>\n");
      html.write(
          "    <input type=\"text\" id=\"search\" placeholder=\"Buscar propriedade (ID)...\" onkeyup=\"searchNode()\">\n");
      html.write("  </div>\n");
      html.write("  <div id=\"network\"></div>\n");
      html.write("  <script src=\"adjacencias.js\"></script>\n");
      html.write("  <script>\n");
      html.write("    const container = document.getElementById('network');\n");
      html.write("    const data = { nodes: new vis.DataSet(nodes), edges: new vis.DataSet(edges) };\n");
      html.write("    const options = {\n");
      html.write("      physics: false,\n");
      html.write("      interaction: { dragNodes: false, dragView: true, zoomView: true },\n");
      html.write("      nodes: {\n");
      html.write("        shape: 'dot',\n");
      html.write("        font: { size: 14, face: 'Segoe UI', color: '#000000' },\n");
      html.write(
          "        color: { background: '#97C2FC', border: '#2B7CE9', highlight: { background: '#FFA500', border: '#FF8C00' } },\n");
      html.write("        borderWidth: 2\n");
      html.write("      },\n");
      html.write("      edges: { color: '#cccccc', width: 1 }\n");
      html.write("    };\n");
      html.write("    const network = new vis.Network(container, data, options);\n");
      html.write(
          "    network.once('afterDrawing', () => network.fit({ animation: { duration: 800, easingFunction: 'easeInOutQuad' }, maxZoomLevel: 2 }));\n");
      html.write("    network.on('click', (params) => {\n");
      html.write("      if (params.nodes.length > 0) {\n");
      html.write("        const node = nodes.find(n => n.id === params.nodes[0]);\n");
      html.write("        if (node && node.title) alert(node.title);\n");
      html.write("      }\n");
      html.write("    });\n");
      html.write(
          "    function zoomIn() { network.moveTo({ scale: network.getScale() * 1.2, animation: { duration: 300 } }); }\n");
      html.write(
          "    function zoomOut() { network.moveTo({ scale: network.getScale() * 0.8, animation: { duration: 300 } }); }\n");
      html.write(
          "    function resetView() { network.fit({ animation: { duration: 500, easingFunction: 'easeInOutQuad' } }); }\n");
      html.write("    function searchNode() {\n");
      html.write("      const searchValue = document.getElementById('search').value;\n");
      html.write("      if (!searchValue) { network.unselectAll(); return; }\n");
      html.write("      const nodeId = parseInt(searchValue);\n");
      html.write("      if (!isNaN(nodeId) && nodes.find(n => n.id === nodeId)) {\n");
      html.write("        network.selectNodes([nodeId]);\n");
      html.write(
          "        network.focus(nodeId, { scale: 1.5, animation: { duration: 500, easingFunction: 'easeInOutQuad' } });\n");
      html.write("      }\n");
      html.write("    }\n");
      html.write("  </script>\n");
      html.write("</body>\n</html>");
    } catch (IOException e) {
      System.out.println("Erro ao escrever HTML: " + e.getMessage());
    }

    System.out.println("Grafo exportado com sucesso! Abre o ficheiro: " + ficheiroHTML);
  }
}