package com.sporthub;

import com.sporthub.data.DataInitializer;
import com.sporthub.service.AuthService;
import com.sporthub.service.EspacoService;
import com.sporthub.service.ReservaService;
import com.sporthub.ui.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal da aplicacao SportHub.
 * Ponto de entrada do programa e gestao de navegacao entre ecras.
 *
 * @author Pedro Silva
 * @author Ana Costa
 * @version 1.0.0
 */
public class MainApp extends Application {

    private static Stage primaryStage;
    private static AuthService authService;
    private static EspacoService espacoService;
    private static ReservaService reservaService;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // Inicializar servicos
        authService = new AuthService();
        espacoService = new EspacoService();
        reservaService = new ReservaService();

        // Criar dados iniciais na primeira execucao
        DataInitializer.inicializar();

        // Configurar janela principal
        primaryStage.setTitle("SportHub - Gestao de Reservas de Espacos Desportivos");
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(720);

        mostrarLogin();
        primaryStage.show();
    }

    /**
     * Navega para o ecra de login e termina a sessao atual.
     */
    public static void mostrarLogin() {
        authService.logout();
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 1050, 720);
        scene.getStylesheets().add(MainApp.class.getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() { return primaryStage; }

    public static AuthService getAuthService() { return authService; }

    public static EspacoService getEspacoService() { return espacoService; }

    public static ReservaService getReservaService() { return reservaService; }

    public static void main(String[] args) {
        launch(args);
    }
}
