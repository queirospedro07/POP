package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.model.TipoUtilizador;
import com.sporthub.model.Utilizador;
import com.sporthub.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Painel principal com sidebar e area de conteudo.
 */
public class DashboardView {

    private final BorderPane root;
    private final AuthService authService;
    private final StackPane contentArea;
    private Button btnAtivo;

    public DashboardView() {
        this.authService = MainApp.getAuthService();
        this.root = new BorderPane();

        root.setLeft(criarSidebar());

        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
        root.setCenter(contentArea);

        // Vista inicial
        if (authService.isCliente()) {
            mostrarConteudo(new EspacosListaView().getView());
        } else {
            mostrarConteudo(new GestorEspacosView().getView());
        }
    }

    private VBox criarSidebar() {
        VBox sb = new VBox(4);
        sb.getStyleClass().add("sidebar");
        sb.setPrefWidth(240);

        Utilizador user = authService.getUtilizadorAtual();

        // Logo
        HBox logoBox = new HBox(6);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(0, 0, 4, 4));
        Label logoS = new Label("Sport");
        logoS.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label logoH = new Label("Hub");
        logoH.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0061f2;");
        logoBox.getChildren().addAll(logoS, logoH);

        // Perfil do utilizador
        HBox perfilBox = new HBox(12);
        perfilBox.setAlignment(Pos.CENTER_LEFT);
        perfilBox.setPadding(new Insets(16, 8, 16, 8));

        // Avatar circular com inicial
        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(20);
        avatarCircle.setFill(Color.web("#0061f2"));
        Text avatarText = new Text(user.getNome().substring(0, 1).toUpperCase());
        avatarText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        avatarText.setFill(Color.WHITE);
        avatar.getChildren().addAll(avatarCircle, avatarText);

        VBox perfilInfo = new VBox(2);
        Label nomeLabel = new Label(user.getNome());
        nomeLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #e9ecef;");
        Label badgeLabel = new Label(user.getTipo() == TipoUtilizador.CLIENTE ? "Cliente" : "Gestor");
        badgeLabel.getStyleClass().add("sidebar-badge");
        perfilInfo.getChildren().addAll(nomeLabel, badgeLabel);

        perfilBox.getChildren().addAll(avatar, perfilInfo);

        // Separador
        Separator sep1 = new Separator();
        sep1.getStyleClass().add("sidebar-separator");

        sb.getChildren().addAll(logoBox, perfilBox, sep1);

        // Seccao do menu
        Label menuSection = new Label("MENU");
        menuSection.getStyleClass().add("sidebar-section");
        sb.getChildren().add(menuSection);

        if (authService.isCliente()) {
            adicionarBotaoSidebar(sb, "\u2302  Espacos Disponiveis", () -> mostrarConteudo(new EspacosListaView().getView()));
            adicionarBotaoSidebar(sb, "+  Nova Reserva", () -> mostrarConteudo(new NovaReservaView().getView()));
            adicionarBotaoSidebar(sb, "\u2630  As Minhas Reservas", () -> mostrarConteudo(new MinhasReservasView().getView()));
        } else {
            adicionarBotaoSidebar(sb, "\u2302  Os Meus Espacos", () -> mostrarConteudo(new GestorEspacosView().getView()));
            adicionarBotaoSidebar(sb, "+  Novo Espaco", () -> mostrarConteudo(new NovoEspacoView().getView()));
            adicionarBotaoSidebar(sb, "\u2630  Reservas", () -> mostrarConteudo(new GestorReservasView().getView()));
        }

        // Seccao conta
        Separator sep2 = new Separator();
        sep2.getStyleClass().add("sidebar-separator");
        Label contaSection = new Label("CONTA");
        contaSection.getStyleClass().add("sidebar-section");
        sb.getChildren().addAll(sep2, contaSection);

        adicionarBotaoSidebar(sb, "\u2699  O Meu Perfil", () -> mostrarConteudo(new PerfilView().getView()));

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sb.getChildren().add(spacer);

        // Logout
        Button logoutBtn = new Button("\u2190  Terminar Sessao");
        logoutBtn.getStyleClass().add("sidebar-btn");
        logoutBtn.setStyle("-fx-text-fill: #ef5350;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> MainApp.mostrarLogin());
        sb.getChildren().add(logoutBtn);

        return sb;
    }

    private void adicionarBotaoSidebar(VBox sb, String texto, Runnable acao) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("sidebar-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            if (btnAtivo != null) {
                btnAtivo.getStyleClass().remove("sidebar-btn-active");
            }
            btn.getStyleClass().add("sidebar-btn-active");
            btnAtivo = btn;
            acao.run();
        });
        sb.getChildren().add(btn);

        if (btnAtivo == null) {
            btn.getStyleClass().add("sidebar-btn-active");
            btnAtivo = btn;
        }
    }

    private void mostrarConteudo(Node conteudo) {
        contentArea.getChildren().setAll(conteudo);
    }

    public BorderPane getView() {
        return root;
    }
}
