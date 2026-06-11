package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.data.DataStore;
import com.sporthub.model.Utilizador;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Vista de perfil do utilizador com edicao de dados pessoais.
 */
public class PerfilView {

    private final VBox root;
    private final Utilizador utilizador;

    public PerfilView() {
        this.utilizador = MainApp.getAuthService().getUtilizadorAtual();
        this.root = new VBox(16);
        root.setPadding(new Insets(0));
        construirVista();
    }

    private void construirVista() {
        // Header
        VBox header = new VBox(4);
        Label titulo = new Label("O Meu Perfil");
        titulo.getStyleClass().add("content-title");
        Label subtitulo = new Label("Utilizador #" + utilizador.getId() + " | " + utilizador.getEmail());
        subtitulo.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtitulo);

        // Formulario
        VBox formCard = new VBox(16);
        formCard.getStyleClass().add("card");

        GridPane form = new GridPane();
        form.setHgap(16);
        form.setVgap(14);

        TextField nomeField = new TextField(utilizador.getNome());
        nomeField.setPrefWidth(320);
        nomeField.setPrefHeight(38);

        TextField nifField = new TextField(utilizador.getNif());
        nifField.setPrefWidth(320);
        nifField.setPrefHeight(38);

        DatePicker dataPicker = new DatePicker(utilizador.getDataNascimento());
        dataPicker.setPrefWidth(320);

        TextField telefoneField = new TextField(utilizador.getTelefone());
        telefoneField.setPrefWidth(320);
        telefoneField.setPrefHeight(38);

        TextField emailField = new TextField(utilizador.getEmail());
        emailField.setPrefWidth(320);
        emailField.setPrefHeight(38);
        emailField.setEditable(false);
        emailField.setStyle("-fx-opacity: 0.6;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nova password (deixe vazio para manter)");
        passwordField.setPrefWidth(320);
        passwordField.setPrefHeight(38);

        form.add(criarLabel("Nome:"), 0, 0);
        form.add(nomeField, 1, 0);
        form.add(criarLabel("NIF:"), 0, 1);
        form.add(nifField, 1, 1);
        form.add(criarLabel("Data Nascimento:"), 0, 2);
        form.add(dataPicker, 1, 2);
        form.add(criarLabel("Telefone:"), 0, 3);
        form.add(telefoneField, 1, 3);
        form.add(criarLabel("Email:"), 0, 4);
        form.add(emailField, 1, 4);
        form.add(criarLabel("Password:"), 0, 5);
        form.add(passwordField, 1, 5);

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);

        Button guardarBtn = new Button("Guardar Alteracoes");
        guardarBtn.getStyleClass().add("btn-primary");
        guardarBtn.setOnAction(e -> {
            String nome = nomeField.getText().trim();
            String nif = nifField.getText().trim();
            String telefone = telefoneField.getText().trim();

            if (nome.isEmpty() || nif.isEmpty() || telefone.isEmpty()) {
                resultLabel.setText("Preencha todos os campos obrigatorios.");
                resultLabel.getStyleClass().setAll("error-label");
                return;
            }
            if (nif.length() != 9) {
                resultLabel.setText("NIF deve ter 9 digitos.");
                resultLabel.getStyleClass().setAll("error-label");
                return;
            }

            utilizador.setNome(nome);
            utilizador.setNif(nif);
            utilizador.setDataNascimento(dataPicker.getValue());
            utilizador.setTelefone(telefone);

            if (!passwordField.getText().isEmpty()) {
                if (passwordField.getText().length() < 4) {
                    resultLabel.setText("Password deve ter pelo menos 4 caracteres.");
                    resultLabel.getStyleClass().setAll("error-label");
                    return;
                }
                utilizador.setPassword(passwordField.getText());
            }

            DataStore.getInstance().atualizarUtilizador(utilizador);
            resultLabel.setText("Perfil atualizado com sucesso!");
            resultLabel.getStyleClass().setAll("success-label");
            passwordField.clear();
        });

        formCard.getChildren().addAll(form, guardarBtn, resultLabel);
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
