package es;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aplicação JavaFX para a gestão visual de propriedades rústicas.
 * Permite carregar dados de propriedades, construir grafos de adjacência,
 * calcular áreas médias e gerar sugestões de troca entre proprietários.
 * 
 * VERSÃO MELHORADA:
 * - Botões de grafo agora exportam e visualizam automaticamente
 * - Área avançada implementada com estatísticas detalhadas
 */
public class GestaoTerritorioApp extends Application {

    private List<Propriedade> propriedades;
    private GrafoAdjacencias grafoAdjacencias;
    private GrafoProprietarios grafoProprietarios;
    private Label statusLabel;
    private ProgressIndicator progressIndicator;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");

        // Header
        Label titleLabel = new Label("🗺️ Sistema de Gestão Territorial");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        statusLabel = new Label("Carregando propriedades...");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-padding: 10px;");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(50, 50);

        // Carregar propriedades
        new Thread(() -> {
            try {
                propriedades = CSVLoader.carregarPropriedades("data/Madeira-Moodle-1.1.csv");
                Platform.runLater(() -> {
                    statusLabel.setText("✅ " + propriedades.size() + " propriedades carregadas");
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #90EE90; -fx-font-weight: bold;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("❌ Erro ao carregar propriedades: " + e.getMessage());
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF6B6B; -fx-font-weight: bold;");
                });
            }
        }).start();

        // Botões
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMaxWidth(400);

        Button btnVisualizacao = createStyledButton("📊 Abrir Visualização Web", "#4CAF50");
        btnVisualizacao.setOnAction(e -> abrirVisualizacao());

        Button btnGrafoAdj = createStyledButton("🔗 Construir Grafo de Adjacências", "#2196F3");
        btnGrafoAdj.setOnAction(e -> construirGrafoAdjacencias());

        Button btnGrafoProp = createStyledButton("👥 Construir Grafo de Proprietários", "#FF9800");
        btnGrafoProp.setOnAction(e -> construirGrafoProprietarios());

        Button btnAreaSimples = createStyledButton("📐 Calcular Área Média (Simples)", "#9C27B0");
        btnAreaSimples.setOnAction(e -> calcularAreaMedia(false));

        Button btnAreaAvancada = createStyledButton("📏 Calcular Área Média (Avançada)", "#E91E63");
        btnAreaAvancada.setOnAction(e -> calcularAreaMedia(true));

        Button btnSugestoesSimples = createStyledButton("💡 Sugestões de Troca (Simples)", "#00BCD4");
        btnSugestoesSimples.setOnAction(e -> gerarSugestoes(false));

        Button btnSugestoesAvancadas = createStyledButton("⭐ Sugestões de Troca (Avançadas)", "#FFC107");
        btnSugestoesAvancadas.setOnAction(e -> gerarSugestoes(true));

        buttonBox.getChildren().addAll(
            btnVisualizacao,
            new Separator(),
            btnGrafoAdj,
            btnGrafoProp,
            new Separator(),
            btnAreaSimples,
            btnAreaAvancada,
            new Separator(),
            btnSugestoesSimples,
            btnSugestoesAvancadas
        );

        root.getChildren().addAll(titleLabel, statusLabel, progressIndicator, buttonBox);

        Scene scene = new Scene(root, 900, 700);
        stage.setTitle("Sistema de Gestão Territorial - Grupo");
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 12px 24px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;",
            color
        ));
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-opacity: 0.9;"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-opacity: 0.9;", "")));
        return btn;
    }

    private void abrirVisualizacao() {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Aviso", "Aguarde o carregamento das propriedades!");
            return;
        }

        showProgress(true, "Gerando visualização...");
        new Thread(() -> {
            try {
                // Se ainda não existe, constrói os grafos
                if (grafoAdjacencias == null) {
                    grafoAdjacencias = new GrafoAdjacencias(propriedades);
                }
                if (grafoProprietarios == null) {
                    grafoProprietarios = new GrafoProprietarios(grafoAdjacencias);
                }

                // Exporta HTML
                Map<Integer, Set<Integer>> grafoMap = buildGrafoMap();
                
                ExportadorAdjacenciasHTML.exportar(
                    propriedades.subList(0, Math.min(100, propriedades.size())),
                    grafoMap,
                    "grafo_adjacencias.html",
                    "adjacencias.js",
                    100
                );

                ExportadorProprietariosHTML.exportar(
                    grafoProprietarios.getGrafoCompleto(),
                    "./",
                    "grafo_proprietarios.html",
                    "proprietarios.js"
                );

                // Cria index.html
                TestarExportadores.criarIndexHTML();

                Platform.runLater(() -> {
                    showProgress(false, null);
                    abrirHTML("index.html");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Erro", "Falha ao gerar visualização: " + ex.getMessage());
                });
            }
        }).start();
    }

    /**
     * MELHORADO: Agora constrói o grafo E exporta para HTML automaticamente
     */
    private void construirGrafoAdjacencias() {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Aviso", "Aguarde o carregamento das propriedades!");
            return;
        }

        showProgress(true, "Construindo e exportando grafo de adjacências...");
        new Thread(() -> {
            try {
                // Construir grafo
                grafoAdjacencias = new GrafoAdjacencias(propriedades);
                
                // Exportar para HTML
                Map<Integer, Set<Integer>> grafoMap = buildGrafoMap();
                ExportadorAdjacenciasHTML.exportar(
                    propriedades.subList(0, Math.min(100, propriedades.size())),
                    grafoMap,
                    "grafo_adjacencias.html",
                    "adjacencias.js",
                    100
                );
                
                Platform.runLater(() -> {
                    showProgress(false, null);
                    
                    // Mostrar estatísticas E abrir visualização
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Grafo Construído");
                    alert.setHeaderText("Grafo de Adjacências construído com sucesso!");
                    alert.setContentText(String.format(
                        "Vértices (Propriedades): %d\n" +
                        "Arestas (Adjacências): %d\n\n" +
                        "Visualização será aberta no browser...",
                        grafoAdjacencias.getNumVertices(),
                        grafoAdjacencias.getNumArestas()
                    ));
                    
                    alert.showAndWait();
                    
                    // Abrir no browser
                    abrirHTML("grafo_adjacencias.html");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Erro", "Falha ao construir grafo: " + ex.getMessage());
                });
            }
        }).start();
    }

    /**
     * MELHORADO: Agora constrói o grafo E exporta para HTML automaticamente
     */
    private void construirGrafoProprietarios() {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Aviso", "Aguarde o carregamento das propriedades!");
            return;
        }

        showProgress(true, "Construindo e exportando grafo de proprietários...");
        new Thread(() -> {
            try {
                // Construir grafo de adjacências se necessário
                if (grafoAdjacencias == null) {
                    grafoAdjacencias = new GrafoAdjacencias(propriedades);
                }
                
                // Construir grafo de proprietários
                grafoProprietarios = new GrafoProprietarios(grafoAdjacencias);
                
                // Exportar para HTML
                ExportadorProprietariosHTML.exportar(
                    grafoProprietarios.getGrafoCompleto(),
                    "./",
                    "grafo_proprietarios.html",
                    "proprietarios.js"
                );
                
                Platform.runLater(() -> {
                    showProgress(false, null);
                    
                    // Mostrar estatísticas E abrir visualização
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Grafo Construído");
                    alert.setHeaderText("Grafo de Proprietários construído com sucesso!");
                    alert.setContentText(String.format(
                        "Proprietários: %d\n\n" +
                        "Visualização será aberta no browser...",
                        grafoProprietarios.getNumProprietarios()
                    ));
                    
                    alert.showAndWait();
                    
                    // Abrir no browser
                    abrirHTML("grafo_proprietarios.html");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Erro", "Falha ao construir grafo: " + ex.getMessage());
                });
            }
        }).start();
    }

    /**
     * MELHORADO: Agora suporta cálculo avançado com componentes conexas
     */
    private void calcularAreaMedia(boolean avancada) {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Aviso", "Aguarde o carregamento das propriedades!");
            return;
        }

        String tipo = escolherOpcao("Tipo de divisão:", Arrays.asList("freguesia", "municipio", "ilha"));
        if (tipo == null) return;

        List<String> nomes = obterNomesPorTipo(tipo);
        if (nomes.isEmpty()) {
            showAlert("Erro", "Não foram encontrados nomes para o tipo selecionado.");
            return;
        }

        String nome = escolherOpcao("Escolha " + tipo + ":", nomes);
        if (nome == null) return;

        showProgress(true, avancada ? "Calculando área corrigida (componentes conexas)..." : "Calculando área média...");
        new Thread(() -> {
            try {
                if (avancada) {
                    // CÁLCULO AVANÇADO - Considera componentes conexas (grupos adjacentes)
                    // Construir grafo se necessário
                    if (grafoAdjacencias == null) {
                        grafoAdjacencias = new GrafoAdjacencias(propriedades);
                    }
                    
                    // Converter grafo para Map<Integer, Set<Integer>>
                    Map<Integer, Set<Integer>> grafoMap = buildGrafoMap();
                    
                    double mediaCorrigida = AreaAvancada.calcularAreaMediaCorrigida(
                        propriedades, tipo, nome, grafoMap
                    );
                    
                    Platform.runLater(() -> {
                        showProgress(false, null);
                        showAlert("Resultado Avançado", 
                            String.format("Área Média Corrigida (Avançada)\n" +
                                        "%s = %s\n\n" +
                                        "%.2f m²\n\n" +
                                        "ℹ️ Cálculo considera propriedades adjacentes\n" +
                                        "do mesmo dono como um único grupo.",
                                tipo, nome, mediaCorrigida));
                    });
                } else {
                    // CÁLCULO SIMPLES - Apenas média aritmética
                    double media = AreaPropriedades.calcularAreaMediaPorNivel(propriedades, tipo, nome);
                    
                    Platform.runLater(() -> {
                        showProgress(false, null);
                        showAlert("Resultado", 
                            String.format("Área média (Simples)\n%s = %s\n\n%.2f m²",
                                tipo, nome, media));
                    });
                }
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Erro", "Falha ao calcular: " + ex.getMessage());
                    ex.printStackTrace();
                });
            }
        }).start();
    }

    private void gerarSugestoes(boolean avancada) {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Aviso", "Aguarde o carregamento das propriedades!");
            return;
        }

        List<String> donos = propriedades.stream()
            .map(Propriedade::getOwner)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        String dono = escolherOpcao("Escolha um proprietário:", donos);
        if (dono == null) return;

        showProgress(true, "Gerando sugestões...");
        new Thread(() -> {
            try {
                List<?> sugestoes = avancada
                    ? SugestaoTrocaAvancada.gerar(propriedades, dono)
                    : SugestaoTroca.gerar(propriedades, dono);

                Platform.runLater(() -> {
                    showProgress(false, null);
                    mostrarSugestoes(
                        avancada ? "Sugestões Avançadas" : "Sugestões Simples",
                        sugestoes
                    );
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Erro", "Falha ao gerar sugestões: " + ex.getMessage());
                });
            }
        }).start();
    }

    private void mostrarSugestoes(String titulo, List<?> sugestoes) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Top 50 Sugestões (ordenadas por score)");

        VBox conteudo = new VBox(8);
        conteudo.setPadding(new Insets(15));

        if (sugestoes.isEmpty()) {
            conteudo.getChildren().add(new Label("Nenhuma sugestão encontrada."));
        } else {
            int limite = Math.min(50, sugestoes.size());
            for (int i = 0; i < limite; i++) {
                Label linha = new Label((i + 1) + ". " + sugestoes.get(i).toString());
                linha.setStyle("-fx-font-size: 12px;");
                linha.setWrapText(true);
                conteudo.getChildren().add(linha);
                if (i < limite - 1) {
                    conteudo.getChildren().add(new Separator());
                }
            }
        }

        ScrollPane scroll = new ScrollPane(conteudo);
        scroll.setFitToWidth(true);
        scroll.setPrefSize(700, 500);

        dialog.getDialogPane().setContent(scroll);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private Map<Integer, Set<Integer>> buildGrafoMap() {
        Map<Integer, Set<Integer>> grafoMap = new HashMap<>();
        for (Propriedade p : propriedades) {
            Set<Propriedade> vizinhos = grafoAdjacencias.getVizinhos(p);
            Set<Integer> vizinhosIds = vizinhos.stream()
                .map(Propriedade::getObjectId)
                .collect(Collectors.toSet());
            grafoMap.put(p.getObjectId(), vizinhosIds);
        }
        return grafoMap;
    }

    private List<String> obterNomesPorTipo(String tipo) {
        return propriedades.stream()
            .map(p -> switch (tipo) {
                case "freguesia" -> p.getFreguesia();
                case "municipio" -> p.getMunicipio();
                case "ilha" -> p.getIlha();
                default -> null;
            })
            .filter(s -> s != null && !s.isBlank())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    private void abrirHTML(String path) {
        try {
            File htmlFile = new File(path);
            if (htmlFile.exists()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                showAlert("Erro", "Ficheiro não encontrado: " + path);
            }
        } catch (IOException e) {
            showAlert("Erro", "Erro ao abrir HTML: " + e.getMessage());
        }
    }

    private void showProgress(boolean visible, String message) {
        progressIndicator.setVisible(visible);
        if (message != null) {
            statusLabel.setText(message);
            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        }
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private String escolherOpcao(String titulo, List<String> opcoes) {
        if (opcoes.isEmpty()) return null;
        ChoiceDialog<String> dialog = new ChoiceDialog<>(opcoes.get(0), opcoes);
        dialog.setTitle("Escolha");
        dialog.setHeaderText(null);
        dialog.setContentText(titulo);
        return dialog.showAndWait().orElse(null);
    }

    public static void main(String[] args) {
        launch();
    }
}