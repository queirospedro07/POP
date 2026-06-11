package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.model.TipoUtilizador;
import com.sporthub.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Vista de login e registo com design moderno.
 */
public class LoginView {

    private final StackPane root;
    private final AuthService authService;

    public LoginView() {
        this.authService = MainApp.getAuthService();
        this.root = new StackPane();
        root.getStyleClass().add("login-container");
        root.setAlignment(Pos.CENTER);
        mostrarLogin();
    }

    private void mostrarLogin() {
        VBox loginBox = new VBox(18);
        loginBox.getStyleClass().add("login-box");
        loginBox.setAlignment(Pos.CENTER);

        // Icone / Avatar
        StackPane iconPane = new StackPane();
        Circle circle = new Circle(32);
        circle.setFill(Color.web("#0061f2"));
        Text iconText = new Text("S");
        iconText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        iconText.setFill(Color.WHITE);
        iconPane.getChildren().addAll(circle, iconText);

        // Titulo
        Text titulo = new Text("SportHub");
        titulo.getStyleClass().add("login-title");

        Text subtitulo = new Text("Inicie sessao para continuar");
        subtitulo.getStyleClass().add("login-subtitle");

        Region spacer1 = new Region();
        spacer1.setPrefHeight(8);

        // Campos
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);
        emailField.setPrefHeight(40);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setPrefHeight(40);

        Label erroLabel = new Label();
        erroLabel.getStyleClass().add("error-label");
        erroLabel.setMaxWidth(300);
        erroLabel.setWrapText(true);

        // Botao Login
        Button loginBtn = new Button("Entrar");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(300);
        loginBtn.setPrefHeight(42);

        // Separador visual
        HBox separadorBox = new HBox(10);
        separadorBox.setAlignment(Pos.CENTER);
        separadorBox.setMaxWidth(300);
        Separator sepLeft = new Separator();
        sepLeft.setPrefWidth(100);
        HBox.setHgrow(sepLeft, Priority.ALWAYS);
        Label ouLabel = new Label("ou");
        ouLabel.setStyle("-fx-text-fill: #9e9e9e; -fx-font-size: 11px;");
        Separator sepRight = new Separator();
        sepRight.setPrefWidth(100);
        HBox.setHgrow(sepRight, Priority.ALWAYS);
        separadorBox.getChildren().addAll(sepLeft, ouLabel, sepRight);

        // Botao Registar
        Button registarBtn = new Button("Criar nova conta");
        registarBtn.getStyleClass().add("btn-secondary");
        registarBtn.setMaxWidth(300);
        registarBtn.setPrefHeight(42);

        // Acoes
        loginBtn.setOnAction(e -> {
            erroLabel.setText("");
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                erroLabel.setText("Preencha todos os campos.");
                return;
            }
            if (authService.login(email, password)) {
                navegarParaDashboard();
            } else {
                erroLabel.setText("Email ou password incorretos.");
            }
        });

        passwordField.setOnAction(e -> loginBtn.fire());
        registarBtn.setOnAction(e -> mostrarRegisto());

        loginBox.getChildren().addAll(
                iconPane, titulo, subtitulo, spacer1,
                emailField, passwordField, erroLabel,
                loginBtn, separadorBox, registarBtn
        );

        root.getChildren().setAll(loginBox);
    }

    private void mostrarRegisto() {
        VBox registoBox = new VBox(12);
        registoBox.getStyleClass().add("login-box");
        registoBox.setAlignment(Pos.CENTER);
        registoBox.setMaxWidth(440);

        Text titulo = new Text("Criar Conta");
        titulo.getStyleClass().add("login-title");

        Text subtitulo = new Text("Preencha os dados para se registar");
        subtitulo.getStyleClass().add("login-subtitle");

        Region spacer = new Region();
        spacer.setPrefHeight(4);

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome completo");
        nomeField.setMaxWidth(340);
        nomeField.setPrefHeight(38);

        TextField nifField = new TextField();
        nifField.setPromptText("NIF (9 digitos)");
        nifField.setMaxWidth(340);
        nifField.setPrefHeight(38);

        DatePicker dataNascPicker = new DatePicker();
        dataNascPicker.setPromptText("Data de Nascimento");
        dataNascPicker.setMaxWidth(340);

        TextField telefoneField = new TextField();
        telefoneField.setPromptText("Telefone");
        telefoneField.setMaxWidth(340);
        telefoneField.setPrefHeight(38);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(340);
        emailField.setPrefHeight(38);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (min. 4 caracteres)");
        passwordField.setMaxWidth(340);
        passwordField.setPrefHeight(38);

        ComboBox<String> tipoCombo = new ComboBox<>();
        tipoCombo.getItems().addAll("Cliente", "Gestor de Espaco");
        tipoCombo.setValue("Cliente");
        tipoCombo.setMaxWidth(340);

        Label erroLabel = new Label();
        erroLabel.getStyleClass().add("error-label");
        erroLabel.setWrapText(true);
        erroLabel.setMaxWidth(340);

        Button registarBtn = new Button("Registar");
        registarBtn.getStyleClass().add("btn-primary");
        registarBtn.setMaxWidth(340);
        registarBtn.setPrefHeight(42);

        Button voltarBtn = new Button("Voltar ao Login");
        voltarBtn.getStyleClass().add("btn-secondary");
        voltarBtn.setMaxWidth(340);
        voltarBtn.setPrefHeight(42);

        registarBtn.setOnAction(e -> {
            TipoUtilizador tipo = tipoCombo.getValue().equals("Cliente")
                    ? TipoUtilizador.CLIENTE : TipoUtilizador.GESTOR;

            String erro = authService.registar(
                    nomeField.getText().trim(),
                    nifField.getText().trim(),
                    dataNascPicker.getValue(),
                    telefoneField.getText().trim(),
                    emailField.getText().trim(),
                    passwordField.getText(),
                    tipo
            );

            if (erro != null) {
                erroLabel.setText(erro);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Conta criada com sucesso! Faca login para continuar.");
                alert.showAndWait();
                mostrarLogin();
            }
        });

        voltarBtn.setOnAction(e -> mostrarLogin());

        registoBox.getChildren().addAll(
                titulo, subtitulo, spacer,
                nomeField, nifField, dataNascPicker,
                telefoneField, emailField, passwordField, tipoCombo,
                erroLabel, registarBtn, voltarBtn
        );

        root.getChildren().setAll(registoBox);
    }

    private void navegarParaDashboard() {
        DashboardView dashboard = new DashboardView();
        Scene scene = new Scene(dashboard.getView(), 1050, 720);
        scene.getStylesheets().add(MainApp.class.getResource("/styles.css").toExternalForm());
        MainApp.getPrimaryStage().setScene(scene);
    }

    public StackPane getView() {
        return root;
    }
}
