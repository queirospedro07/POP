package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.model.TipoEspaco;
import com.sporthub.model.Utilizador;
import com.sporthub.service.EspacoService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Formulario para registar um novo espaco desportivo (gestor).
 */
public class NovoEspacoView {

    private final VBox root;
    private final EspacoService espacoService;
    private final Utilizador gestor;

    public NovoEspacoView() {
        this.espacoService = MainApp.getEspacoService();
        this.gestor = MainApp.getAuthService().getUtilizadorAtual();
        this.root = new VBox(16);
        root.setPadding(new Insets(0));
        construirVista();
    }

    private void construirVista() {
        // Header
        VBox header = new VBox(4);
        Label titulo = new Label("Novo Espaco Desportivo");
        titulo.getStyleClass().add("content-title");
        Label subtitulo = new Label("Preencha os dados do espaco que pretende disponibilizar");
        subtitulo.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtitulo);

        // Formulario
        VBox formCard = new VBox(16);
        formCard.getStyleClass().add("card");

        GridPane form = new GridPane();
        form.setHgap(16);
        form.setVgap(14);

        TextField designacaoField = new TextField();
        designacaoField.setPromptText("Ex: Campo de Futebol Norte");
        designacaoField.setPrefWidth(320);
        designacaoField.setPrefHeight(38);

        TextField moradaField = new TextField();
        moradaField.setPromptText("Ex: Rua do Desporto, 123");
        moradaField.setPrefWidth(320);
        moradaField.setPrefHeight(38);

        TextField localidadeField = new TextField();
        localidadeField.setPromptText("Ex: Viana do Castelo");
        localidadeField.setPrefWidth(320);
        localidadeField.setPrefHeight(38);

        ComboBox<TipoEspaco> tipoCombo = new ComboBox<>(FXCollections.observableArrayList(TipoEspaco.values()));
        tipoCombo.setPromptText("Selecione o tipo");
        tipoCombo.setPrefWidth(320);

        form.add(criarLabel("Designacao:"), 0, 0);
        form.add(designacaoField, 1, 0);
        form.add(criarLabel("Morada:"), 0, 1);
        form.add(moradaField, 1, 1);
        form.add(criarLabel("Localidade:"), 0, 2);
        form.add(localidadeField, 1, 2);
        form.add(criarLabel("Tipo de Espaco:"), 0, 3);
        form.add(tipoCombo, 1, 3);

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);

        Button criarBtn = new Button("Registar Espaco");
        criarBtn.getStyleClass().add("btn-primary");
        criarBtn.setOnAction(e -> {
            String erro = espacoService.criarEspaco(
                    designacaoField.getText().trim(),
                    moradaField.getText().trim(),
                    localidadeField.getText().trim(),
                    tipoCombo.getValue(),
                    gestor.getId()
            );

            if (erro != null) {
                resultLabel.setText(erro);
                resultLabel.getStyleClass().setAll("error-label");
            } else {
                resultLabel.setText("Espaco criado com sucesso! Configure o precario e servicos em 'Os Meus Espacos'.");
                resultLabel.getStyleClass().setAll("success-label");
                designacaoField.clear();
                moradaField.clear();
                localidadeField.clear();
                tipoCombo.setValue(null);
            }
        });

        formCard.getChildren().addAll(form, criarBtn, resultLabel);
        root.getChildren().addAll(header, formCard);
    }

    private Label criarLabel(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("form-label");
        return label;
    }

    public VBox getView() {
        return root;
    }
}
