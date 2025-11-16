package es;

import java.util.*;

/**
 * Classe principal para testar os exportadores HTML.
 * Carrega propriedades do CSV e gera visualizações HTML interativas.
 */
public class TestarExportadores {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== TESTANDO EXPORTADORES HTML ===\n");
        
        // 1. Carregar propriedades do CSV
        System.out.println("1. Carregando propriedades do CSV...");
        List<Propriedade> todasProps = CSVLoader.carregarPropriedades("data/Madeira-Moodle-1.1.csv");
        
        if (todasProps.isEmpty()) {
            System.err.println("ERRO: Nenhuma propriedade carregada!");
            return;
        }
        
        // LIMITAR para teste rápido (senão demora MUITO com 35k propriedades!)
        int limite_props = Math.min(500, todasProps.size());
        List<Propriedade> propriedades = todasProps.subList(0, limite_props);
        
        System.out.println("   Total no CSV: " + todasProps.size() + " propriedades");
        System.out.println("   Usando: " + propriedades.size() + " propriedades para teste rapido\n");
        
        // 2. EXPORTAR GRAFO DE ADJACÊNCIAS
        System.out.println("2. Gerando grafo de adjacencias...");
        long inicio = System.currentTimeMillis();
        
        GrafoAdjacencias grafoAdj = new GrafoAdjacencias(propriedades);
        
        long tempo = (System.currentTimeMillis() - inicio) / 1000;
        System.out.println("   Tempo: " + tempo + "s");
        System.out.println("   Vertices: " + grafoAdj.getNumVertices());
        System.out.println("   Arestas: " + grafoAdj.getNumArestas());
        
        Map<Integer, Set<Integer>> grafoMap = new HashMap<>();
        for (Propriedade p : propriedades) {
            Set<Propriedade> vizinhos = grafoAdj.getVizinhos(p);
            Set<Integer> vizinhosIds = new HashSet<>();
            for (Propriedade v : vizinhos) {
                vizinhosIds.add(v.getObjectId());
            }
            grafoMap.put(p.getObjectId(), vizinhosIds);
        }
        
        int limite = Math.min(100, propriedades.size());
        ExportadorAdjacenciasHTML.exportar(
            propriedades.subList(0, limite), 
            grafoMap,
            "grafo_adjacencias.html",
            "adjacencias.js",
            limite
        );
        
        System.out.println("   Exportadas primeiras " + limite + " propriedades para HTML\n");
        
        // 3. EXPORTAR GRAFO DE PROPRIETÁRIOS
        System.out.println("3. Gerando grafo de proprietarios...");
        GrafoProprietarios grafoProps = new GrafoProprietarios(grafoAdj);
        Map<String, Set<String>> grafoOwners = grafoProps.getGrafoCompleto();
        
        int numProprietarios = grafoOwners.size();
        int numConexoes = grafoOwners.values().stream()
            .mapToInt(Set::size)
            .sum() / 2;
        
        System.out.println("   Proprietarios: " + numProprietarios);
        System.out.println("   Conexoes: " + numConexoes);
        
        ExportadorProprietariosHTML.exportar(
            grafoOwners,
            "./",
            "grafo_proprietarios.html",
            "proprietarios.js"
        );
        
        System.out.println("   Grafo exportado\n");
        
        // 4. CRIAR INDEX.HTML
        System.out.println("\n4. Criando pagina principal...");
        criarIndexHTML();
        
        // 5. ABRIR NO BROWSER AUTOMATICAMENTE
        System.out.println("\n=== FICHEIROS CRIADOS ===");
        System.out.println("index.html (pagina principal)");
        System.out.println("grafo_adjacencias.html");
        System.out.println("adjacencias.js");
        System.out.println("grafo_proprietarios.html");
        System.out.println("proprietarios.js");
        
        System.out.println("\n=== ABRINDO NO BROWSER ===");
        abrirNoBrowser("index.html");
    }
    
    public static void criarIndexHTML() {
        try (java.io.FileWriter fw = new java.io.FileWriter("index.html")) {
            fw.write("""
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visualização de Grafos</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container { max-width: 1400px; margin: 0 auto; }
        header {
            text-align: center;
            color: white;
            margin-bottom: 30px;
            padding: 20px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
        }
        h1 { font-size: 2.5em; margin-bottom: 10px; }
        .stats {
            display: flex;
            justify-content: center;
            gap: 40px;
            margin-top: 15px;
        }
        .stat-number { font-size: 2em; font-weight: bold; display: block; }
        .tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            justify-content: center;
        }
        .tab {
            padding: 15px 40px;
            background: rgba(255, 255, 255, 0.2);
            border: none;
            border-radius: 10px;
            color: white;
            font-size: 1.1em;
            cursor: pointer;
            transition: all 0.3s;
        }
        .tab.active { background: white; color: #667eea; }
        .iframe-container {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            display: none;
        }
        .iframe-container.active { display: block; }
        iframe {
            width: 100%;
            height: calc(100vh - 350px);
            border: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>🗺️ Sistema de Gestão Territorial</h1>
            <div class="stats">
                <div><span class="stat-number">35,045</span><span>Propriedades</span></div>
                <div><span class="stat-number">14,988</span><span>Adjacências</span></div>
                <div><span class="stat-number">1,005</span><span>Proprietários</span></div>
            </div>
        </header>
        <div class="tabs">
            <button class="tab active" onclick="showGraph('adjacencias')">📍 Adjacências</button>
            <button class="tab" onclick="showGraph('proprietarios')">👥 Proprietários</button>
        </div>
        <div id="adjacencias-container" class="iframe-container active">
            <iframe src="grafo_adjacencias.html"></iframe>
        </div>
        <div id="proprietarios-container" class="iframe-container">
            <iframe src="grafo_proprietarios.html"></iframe>
        </div>
    </div>
    <script>
        function showGraph(type) {
            document.querySelectorAll('.tab, .iframe-container').forEach(el => el.classList.remove('active'));
            if (type === 'adjacencias') {
                document.querySelectorAll('.tab')[0].classList.add('active');
                document.getElementById('adjacencias-container').classList.add('active');
            } else {
                document.querySelectorAll('.tab')[1].classList.add('active');
                document.getElementById('proprietarios-container').classList.add('active');
            }
        }
    </script>
</body>
</html>
                    """);
            System.out.println("   index.html criado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao criar index.html: " + e.getMessage());
        }
    }
    
    private static void abrirNoBrowser(String ficheiro) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", ficheiro});
                System.out.println("   Abrindo " + ficheiro + " no browser...");
            } else if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", ficheiro});
                System.out.println("   Abrindo " + ficheiro + " no browser...");
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", ficheiro});
                System.out.println("   Abrindo " + ficheiro + " no browser...");
            }
        } catch (Exception e) {
            System.err.println("Nao foi possivel abrir automaticamente.");
            System.out.println("Abre manualmente: open " + ficheiro);
        }
    }
}