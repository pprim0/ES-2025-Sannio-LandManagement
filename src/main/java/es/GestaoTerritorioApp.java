package es;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JavaFX Application for visual management of rural properties.
 * Allows loading property data, building adjacency graphs,
 * calculating average areas and generating exchange suggestions between owners.
 * 
 * FULL VERSION:
 * - Visualization of ALL 35k properties
 * - Advanced area calculation with detailed statistics
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
        Label titleLabel = new Label("🗺️ Land Management System");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        statusLabel = new Label("Loading properties...");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-padding: 10px;");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(50, 50);

        // Load properties
        new Thread(() -> {
            try {
                propriedades = CSVLoader.carregarPropriedades("data/Madeira-Moodle-1.1.csv");
                Platform.runLater(() -> {
                    statusLabel.setText("✅ " + propriedades.size() + " properties loaded");
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #90EE90; -fx-font-weight: bold;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("❌ Error loading properties: " + e.getMessage());
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF6B6B; -fx-font-weight: bold;");
                });
            }
        }).start();

        // Buttons
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMaxWidth(400);

        Button btnGrafoAdj = createStyledButton("🔗 Build Adjacency Graph (ALL)", "#2196F3");
        btnGrafoAdj.setOnAction(e -> construirGrafoAdjacencias());

        Button btnGrafoProp = createStyledButton("👥 Build Owner Graph", "#FF9800");
        btnGrafoProp.setOnAction(e -> construirGrafoProprietarios());

        Button btnAreaSimples = createStyledButton("📐 Calculate Average Area (Simple)", "#9C27B0");
        btnAreaSimples.setOnAction(e -> calcularAreaMedia(false));

        Button btnAreaAvancada = createStyledButton("📏 Calculate Average Area (Advanced)", "#E91E63");
        btnAreaAvancada.setOnAction(e -> calcularAreaMedia(true));

        Button btnSugestoesSimples = createStyledButton("💡 Exchange Suggestions (Simple)", "#00BCD4");
        btnSugestoesSimples.setOnAction(e -> gerarSugestoes(false));

        Button btnSugestoesAvancadas = createStyledButton("⭐ Exchange Suggestions (Advanced)", "#FFC107");
        btnSugestoesAvancadas.setOnAction(e -> gerarSugestoes(true));

        buttonBox.getChildren().addAll(
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
        stage.setTitle("Land Management System - Group Project");
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

    /**
     * ⚠️ FULL VERSION: Exports ALL 35k properties
     * WARNING: May take 1-2 minutes and consume high RAM in browser!
     */
    private void construirGrafoAdjacencias() {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Warning", "Please wait for properties to load!");
            return;
        }

        // ⚠️ USER WARNING
        Alert warning = new Alert(Alert.AlertType.WARNING);
        warning.setTitle("⚠️ IMPORTANT WARNING");
        warning.setHeaderText("Visualization of ALL properties");
        warning.setContentText(
            "You are about to visualize " + propriedades.size() + " properties!\n\n" +
            "⚠️ THIS MAY:\n" +
            "• Take 1-2 minutes to process\n" +
            "• Consume 4-8 GB of RAM in browser\n" +
            "• Make the browser slow or freeze\n" +
            "• Result in very dense visualization\n\n" +
            "💡 RECOMMENDATION:\n" +
            "For better experience, consider using a smaller sample.\n\n" +
            "Continue anyway?"
        );
        warning.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        
        if (warning.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        showProgress(true, "⏳ Building graph with " + propriedades.size() + " properties... (may take time)");
        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                
                // Build graph
                grafoAdjacencias = new GrafoAdjacencias(propriedades);
                
                Platform.runLater(() -> {
                    statusLabel.setText("✅ Graph built! Exporting HTML...");
                });
                
                // Export to HTML with ALL properties
                Map<Integer, Set<Integer>> grafoMap = buildGrafoMap();
                
                ExportadorAdjacenciasHTML.exportar(
                    propriedades, // ✅ ALL properties
                    grafoMap,
                    "grafo_adjacencias.html",
                    "adjacencias.js",
                    propriedades.size() // ✅ No limit
                );
                
                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime) / 1000;
                
                Platform.runLater(() -> {
                    showProgress(false, null);
                    
                    // Show statistics
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("✅ Graph Built");
                    alert.setHeaderText("Adjacency Graph generated successfully!");
                    alert.setContentText(String.format(
                        "📊 STATISTICS:\n\n" +
                        "Properties visualized: %,d\n" +
                        "Adjacencies (edges): %,d\n" +
                        "Processing time: %d seconds\n\n" +
                        "⚠️ ATTENTION:\n" +
                        "Browser may take 30-60 seconds to render.\n" +
                        "Please be patient!\n\n" +
                        "Opening visualization...",
                        propriedades.size(),
                        grafoAdjacencias.getNumArestas(),
                        duration
                    ));
                    
                    alert.showAndWait();
                    
                    // Open in browser
                    abrirHTML("grafo_adjacencias.html");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Error", "Failed to build graph: " + ex.getMessage());
                    ex.printStackTrace();
                });
            }
        }).start();
    }

    /**
     * Build owner graph (this one stays normal)
     */
    private void construirGrafoProprietarios() {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Warning", "Please wait for properties to load!");
            return;
        }

        showProgress(true, "Building and exporting owner graph...");
        new Thread(() -> {
            try {
                // Build adjacency graph if needed
                if (grafoAdjacencias == null) {
                    grafoAdjacencias = new GrafoAdjacencias(propriedades);
                }
                
                // Build owner graph
                grafoProprietarios = new GrafoProprietarios(grafoAdjacencias);
                
                // Export to HTML
                ExportadorProprietariosHTML.exportar(
                    grafoProprietarios.getGrafoCompleto(),
                    "./",
                    "grafo_proprietarios.html",
                    "proprietarios.js"
                );
                
                Platform.runLater(() -> {
                    showProgress(false, null);
                    
                    // Show statistics AND open visualization
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Graph Built");
                    alert.setHeaderText("Owner Graph built successfully!");
                    alert.setContentText(String.format(
                        "Owners: %d\n\n" +
                        "Visualization will be opened in browser...",
                        grafoProprietarios.getNumProprietarios()
                    ));
                    
                    alert.showAndWait();
                    
                    // Open in browser
                    abrirHTML("grafo_proprietarios.html");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Error", "Failed to build graph: " + ex.getMessage());
                });
            }
        }).start();
    }

    /**
     * Calculate average area (simple or advanced)
     */
    private void calcularAreaMedia(boolean avancada) {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Warning", "Please wait for properties to load!");
            return;
        }

        String tipo = escolherOpcao("Division type:", Arrays.asList("freguesia", "municipio", "ilha"));
        if (tipo == null) {
            return;
        }

        List<String> nomes = obterNomesPorTipo(tipo);
        if (nomes.isEmpty()) {
            showAlert("Error", "No names found for the selected type.");
            return;
        }

        String nome = escolherOpcao("Choose " + tipo + ":", nomes);
        if (nome == null) {
            return;
        }

        showProgress(true, avancada ? "Calculating corrected area (connected components)..." : "Calculating average area...");
        new Thread(() -> {
            try {
                if (avancada) {
                    // ADVANCED CALCULATION - Considers connected components (adjacent groups)
                    // Build graph if needed
                    if (grafoAdjacencias == null) {
                        grafoAdjacencias = new GrafoAdjacencias(propriedades);
                    }
                    
                    // Convert graph to Map<Integer, Set<Integer>>
                    Map<Integer, Set<Integer>> grafoMap = buildGrafoMap();
                    
                    double mediaCorrigida = AreaAvancada.calcularAreaMediaCorrigida(
                        propriedades, tipo, nome, grafoMap
                    );
                    
                    Platform.runLater(() -> {
                        showProgress(false, null);
                        showAlert("Advanced Result", 
                            String.format("Corrected Average Area (Advanced)\n" +
                                        "%s = %s\n\n" +
                                        "%.2f m²\n\n" +
                                        "ℹ️ Calculation considers adjacent properties\n" +
                                        "from the same owner as a single group.",
                                tipo, nome, mediaCorrigida));
                    });
                } else {
                    // SIMPLE CALCULATION - Just arithmetic average
                    double media = AreaPropriedades.calcularAreaMediaPorNivel(propriedades, tipo, nome);
                    
                    Platform.runLater(() -> {
                        showProgress(false, null);
                        showAlert("Result", 
                            String.format("Average area (Simple)\n%s = %s\n\n%.2f m²",
                                tipo, nome, media));
                    });
                }
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Error", "Failed to calculate: " + ex.getMessage());
                    ex.printStackTrace();
                });
            }
        }).start();
    }

    private void gerarSugestoes(boolean avancada) {
        if (propriedades == null || propriedades.isEmpty()) {
            showAlert("Warning", "Please wait for properties to load!");
            return;
        }

        List<String> donos = propriedades.stream()
            .map(Propriedade::getOwner)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        String dono = escolherOpcao("Choose an owner:", donos);
        if (dono == null) {
            return;
        }

        showProgress(true, "Generating suggestions...");
        new Thread(() -> {
            try {
                List<?> sugestoes = avancada
                    ? SugestaoTrocaAvancada.gerar(propriedades, dono)
                    : SugestaoTroca.gerar(propriedades, dono);

                Platform.runLater(() -> {
                    showProgress(false, null);
                    mostrarSugestoes(
                        avancada ? "Advanced Suggestions" : "Simple Suggestions",
                        sugestoes
                    );
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showProgress(false, null);
                    showAlert("Error", "Failed to generate suggestions: " + ex.getMessage());
                });
            }
        }).start();
    }

    private void mostrarSugestoes(String titulo, List<?> sugestoes) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Top 50 Suggestions (sorted by score)");

        VBox conteudo = new VBox(8);
        conteudo.setPadding(new Insets(15));

        if (sugestoes.isEmpty()) {
            conteudo.getChildren().add(new Label("No suggestions found."));
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
                showAlert("Error", "File not found: " + path);
            }
        } catch (IOException e) {
            showAlert("Error", "Error opening HTML: " + e.getMessage());
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
        if (opcoes.isEmpty()) {
            return null;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(opcoes.get(0), opcoes);
        dialog.setTitle("Choose");
        dialog.setHeaderText(null);
        dialog.setContentText(titulo);
        return dialog.showAndWait().orElse(null);
    }

    public static void main(String[] args) {
        launch();
    }
}