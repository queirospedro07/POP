package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.model.*;
import com.sporthub.service.EspacoService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Vista de gestão dos espaços do gestor.
 */
public class GestorEspacosView {
    private final VBox root;
    private final EspacoService espacoService;
    private final Utilizador gestor;
    private TableView<EspacoDesportivo> tabela;

    public GestorEspacosView() {
        this.espacoService = MainApp.getEspacoService();
        this.gestor = MainApp.getAuthService().getUtilizadorAtual();
        this.root = new VBox(15);
        root.setPadding(new Insets(10));
        construirVista();
    }

    @SuppressWarnings("unchecked")
    private void construirVista() {
        // Header
        VBox header = new VBox(4);
        Label titulo = new Label("Os Meus Espacos Desportivos");
        titulo.getStyleClass().add("content-title");
        Label subtitulo = new Label("Gerir espacos, precarios e servicos");
        subtitulo.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtitulo);

        tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Não possui espaços registados."));

        TableColumn<EspacoDesportivo, String> colDesignacao = new TableColumn<>("Designação");
        colDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        colDesignacao.setPrefWidth(180);

        TableColumn<EspacoDesportivo, String> colMorada = new TableColumn<>("Morada");
        colMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        colMorada.setPrefWidth(180);

        TableColumn<EspacoDesportivo, String> colLocalidade = new TableColumn<>("Localidade");
        colLocalidade.setCellValueFactory(new PropertyValueFactory<>("localidade"));
        colLocalidade.setPrefWidth(120);

        TableColumn<EspacoDesportivo, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEspaco"));
        colTipo.setPrefWidth(100);

        tabela.getColumns().addAll(colDesignacao, colMorada, colLocalidade, colTipo);
        atualizarTabela();

        // Área de detalhes e edição
        VBox detalhesBox = new VBox(10);
        detalhesBox.getStyleClass().add("card");
        detalhesBox.setPadding(new Insets(15));

        Label detalheLabel = new Label("Selecione um espaço para gerir");
        detalheLabel.setStyle("-fx-font-weight: bold;");

        TextArea detalheTexto = new TextArea();
        detalheTexto.setEditable(false);
        detalheTexto.setPrefHeight(120);
        detalheTexto.setWrapText(true);

        // Botões de ação
        Button editarBtn = new Button("Editar Espaço");
        editarBtn.getStyleClass().add("btn-primary");
        editarBtn.setDisable(true);

        Button precarioBtn = new Button("Gerir Preçário");
        precarioBtn.getStyleClass().add("btn-secondary");
        precarioBtn.setDisable(true);

        Button servicoBtn = new Button("Gerir Serviços");
        servicoBtn.getStyleClass().add("btn-secondary");
        servicoBtn.setDisable(true);

        Button eliminarBtn = new Button("Eliminar Espaço");
        eliminarBtn.getStyleClass().add("btn-danger");
        eliminarBtn.setDisable(true);

        HBox botoesBox = new HBox(10, editarBtn, precarioBtn, servicoBtn, eliminarBtn);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            boolean habilitado = novo != null;
            editarBtn.setDisable(!habilitado);
            precarioBtn.setDisable(!habilitado);
            servicoBtn.setDisable(!habilitado);
            eliminarBtn.setDisable(!habilitado);

            if (novo != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Designação: ").append(novo.getDesignacao()).append("\n");
                sb.append("Morada: ").append(novo.getMorada()).append("\n");
                sb.append("Localidade: ").append(novo.getLocalidade()).append("\n");
                sb.append("Tipo: ").append(novo.getTipoEspaco().getDescricao()).append("\n\n");

                sb.append("Preçários: ").append(novo.getPrecarios().size()).append("\n");
                for (Precario p : novo.getPrecarios()) {
                    sb.append("  • ").append(p.toString()).append("\n");
                }

                sb.append("\nServiços: ").append(novo.getServicosDisponiveis().size()).append("\n");
                for (ServicoAdicional s : novo.getServicosDisponiveis()) {
                    sb.append("  • ").append(s.toString()).append("\n");
                }
                detalheTexto.setText(sb.toString());
            }
        });

        editarBtn.setOnAction(e -> {
            EspacoDesportivo sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) mostrarDialogoEditar(sel);
        });

        precarioBtn.setOnAction(e -> {
            EspacoDesportivo sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) mostrarDialogoPrecario(sel);
        });

        servicoBtn.setOnAction(e -> {
            EspacoDesportivo sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) mostrarDialogoServico(sel);
        });

        eliminarBtn.setOnAction(e -> {
            EspacoDesportivo sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar Eliminação");
                confirm.setHeaderText(null);
                confirm.setContentText("Tem a certeza que deseja eliminar o espaço '" + sel.getDesignacao() + "'?");
                confirm.showAndWait().ifPresent(resp -> {
                    if (resp == ButtonType.OK) {
                        espacoService.removerEspaco(sel.getId());
                        atualizarTabela();
                    }
                });
            }
        });

        detalhesBox.getChildren().addAll(detalheLabel, detalheTexto, botoesBox);

        VBox.setVgrow(tabela, Priority.ALWAYS);
        root.getChildren().addAll(header, tabela, detalhesBox);
    }

    private void atualizarTabela() {
        List<EspacoDesportivo> espacos = espacoService.getEspacosPorGestor(gestor.getId());
        tabela.setItems(FXCollections.observableArrayList(espacos));
    }

    private void mostrarDialogoEditar(EspacoDesportivo espaco) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Espaço");
        dialog.setHeaderText("Editar informações do espaço");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField designacaoField = new TextField(espaco.getDesignacao());
        TextField moradaField = new TextField(espaco.getMorada());
        TextField localidadeField = new TextField(espaco.getLocalidade());
        ComboBox<TipoEspaco> tipoCombo = new ComboBox<>(FXCollections.observableArrayList(TipoEspaco.values()));
        tipoCombo.setValue(espaco.getTipoEspaco());

        grid.add(new Label("Designação:"), 0, 0);
        grid.add(designacaoField, 1, 0);
        grid.add(new Label("Morada:"), 0, 1);
        grid.add(moradaField, 1, 1);
        grid.add(new Label("Localidade:"), 0, 2);
        grid.add(localidadeField, 1, 2);
        grid.add(new Label("Tipo:"), 0, 3);
        grid.add(tipoCombo, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                String erro = espacoService.atualizarEspaco(
                        espaco.getId(),
                        designacaoField.getText().trim(),
                        moradaField.getText().trim(),
                        localidadeField.getText().trim(),
                        tipoCombo.getValue()
                );
                if (erro != null) {
                    new Alert(Alert.AlertType.ERROR, erro).showAndWait();
                } else {
                    atualizarTabela();
                }
            }
        });
    }

    private void mostrarDialogoPrecario(EspacoDesportivo espaco) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Gerir Preçário");
        dialog.setHeaderText("Preçário de " + espaco.getDesignacao());

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        // Lista preçários existentes
        ListView<String> lista = new ListView<>();
        for (int i = 0; i < espaco.getPrecarios().size(); i++) {
            lista.getItems().add(espaco.getPrecarios().get(i).toString());
        }
        lista.setPrefHeight(120);

        Button removerBtn = new Button("Remover Selecionado");
        removerBtn.getStyleClass().add("btn-danger");
        removerBtn.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                espacoService.removerPrecario(espaco.getId(), idx);
                lista.getItems().remove(idx);
            }
        });

        // Adicionar novo
        Label addLabel = new Label("Adicionar Preçário:");
        addLabel.setStyle("-fx-font-weight: bold;");

        HBox addBox = new HBox(10);
        Spinner<Integer> horaInicioSpin = new Spinner<>(0, 23, 8);
        horaInicioSpin.setPrefWidth(70);
        Spinner<Integer> horaFimSpin = new Spinner<>(1, 24, 18);
        horaFimSpin.setPrefWidth(70);
        TextField precoField = new TextField();
        precoField.setPromptText("Preço/h");
        precoField.setPrefWidth(80);

        Button addBtn = new Button("Adicionar");
        addBtn.getStyleClass().add("btn-success");
        addBtn.setOnAction(e -> {
            try {
                double preco = Double.parseDouble(precoField.getText().replace(",", "."));
                String erro = espacoService.adicionarPrecario(
                        espaco.getId(), horaInicioSpin.getValue(), horaFimSpin.getValue(), preco);
                if (erro != null) {
                    new Alert(Alert.AlertType.ERROR, erro).showAndWait();
                } else {
                    EspacoDesportivo atualizado = espacoService.getEspacoPorId(espaco.getId());
                    lista.getItems().clear();
                    for (Precario p : atualizado.getPrecarios()) {
                        lista.getItems().add(p.toString());
                    }
                    precoField.clear();
                }
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Preço inválido.").showAndWait();
            }
        });

        addBox.getChildren().addAll(
                new Label("De:"), horaInicioSpin,
                new Label("Até:"), horaFimSpin,
                new Label("€/h:"), precoField, addBtn
        );

        content.getChildren().addAll(lista, removerBtn, new Separator(), addLabel, addBox);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
        atualizarTabela();
    }

    private void mostrarDialogoServico(EspacoDesportivo espaco) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Gerir Serviços Adicionais");
        dialog.setHeaderText("Serviços de " + espaco.getDesignacao());

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        // Lista serviços existentes
        ListView<String> lista = new ListView<>();
        for (ServicoAdicional s : espaco.getServicosDisponiveis()) {
            lista.getItems().add(s.toString());
        }
        lista.setPrefHeight(120);

        Button removerBtn = new Button("Remover Selecionado");
        removerBtn.getStyleClass().add("btn-danger");
        removerBtn.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                ServicoAdicional s = espaco.getServicosDisponiveis().get(idx);
                espacoService.removerServico(espaco.getId(), s.getId());
                lista.getItems().remove(idx);
            }
        });

        // Adicionar novo
        Label addLabel = new Label("Adicionar Serviço:");
        addLabel.setStyle("-fx-font-weight: bold;");

        GridPane addGrid = new GridPane();
        addGrid.setHgap(10);
        addGrid.setVgap(8);

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome");
        TextField descField = new TextField();
        descField.setPromptText("Descrição");
        TextField precoField = new TextField();
        precoField.setPromptText("Preço");
        ComboBox<TipoServico> tipoCombo = new ComboBox<>(FXCollections.observableArrayList(TipoServico.values()));
        tipoCombo.setValue(TipoServico.UNITARIO);

        addGrid.add(new Label("Nome:"), 0, 0);
        addGrid.add(nomeField, 1, 0);
        addGrid.add(new Label("Descrição:"), 0, 1);
        addGrid.add(descField, 1, 1);
        addGrid.add(new Label("Preço:"), 0, 2);
        addGrid.add(precoField, 1, 2);
        addGrid.add(new Label("Tipo:"), 0, 3);
        addGrid.add(tipoCombo, 1, 3);

        Button addBtn = new Button("Adicionar");
        addBtn.getStyleClass().add("btn-success");
        addBtn.setOnAction(e -> {
            try {
                double preco = Double.parseDouble(precoField.getText().replace(",", "."));
                String erro = espacoService.adicionarServico(
                        espaco.getId(), nomeField.getText().trim(),
                        descField.getText().trim(), preco, tipoCombo.getValue());
                if (erro != null) {
                    new Alert(Alert.AlertType.ERROR, erro).showAndWait();
                } else {
                    EspacoDesportivo atualizado = espacoService.getEspacoPorId(espaco.getId());
                    lista.getItems().clear();
                    for (ServicoAdicional s : atualizado.getServicosDisponiveis()) {
                        lista.getItems().add(s.toString());
                    }
                    nomeField.clear();
                    descField.clear();
                    precoField.clear();
                }
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Preço inválido.").showAndWait();
            }
        });

        content.getChildren().addAll(lista, removerBtn, new Separator(), addLabel, addGrid, addBtn);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
        atualizarTabela();
    }

    public VBox getView() {
        return root;
    }
}
