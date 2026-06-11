package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.data.DataStore;
import com.sporthub.model.*;
import com.sporthub.service.EspacoService;
import com.sporthub.service.ReservaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Vista de gestão de reservas (gestor).
 * Permite aceitar, cancelar e marcar como pagas.
 */
public class GestorReservasView {
    private final VBox root;
    private final ReservaService reservaService;
    private final EspacoService espacoService;
    private final Utilizador gestor;
    private TableView<Reserva> tabela;

    public GestorReservasView() {
        this.reservaService = MainApp.getReservaService();
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
        Label titulo = new Label("Gestao de Reservas");
        titulo.getStyleClass().add("content-title");
        Label subtitulo = new Label("Aceitar, cancelar e gerir pagamentos de reservas");
        subtitulo.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtitulo);

        // Filtros
        HBox filtros = new HBox(10);
        filtros.setPadding(new Insets(5));

        Label filtroLabel = new Label("Filtrar por espaço:");
        ComboBox<String> filtroCombo = new ComboBox<>();
        filtroCombo.getItems().add("Todos");
        for (EspacoDesportivo e : espacoService.getEspacosPorGestor(gestor.getId())) {
            filtroCombo.getItems().add(e.getDesignacao());
        }
        filtroCombo.setValue("Todos");

        Label filtroEstadoLabel = new Label("Estado:");
        ComboBox<String> filtroEstadoCombo = new ComboBox<>();
        filtroEstadoCombo.getItems().addAll("Todos", "Pendente", "Aceite", "Cancelada");
        filtroEstadoCombo.setValue("Todos");

        filtros.getChildren().addAll(filtroLabel, filtroCombo, filtroEstadoLabel, filtroEstadoCombo);

        // Tabela
        tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Sem reservas."));

        TableColumn<Reserva, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colId.setPrefWidth(40);

        TableColumn<Reserva, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(c -> {
            Utilizador cl = DataStore.getInstance().getUtilizadorPorId(c.getValue().getClienteId());
            return new SimpleStringProperty(cl != null ? cl.getNome() : "N/A");
        });
        colCliente.setPrefWidth(130);

        TableColumn<Reserva, String> colEspaco = new TableColumn<>("Espaço");
        colEspaco.setCellValueFactory(c -> {
            EspacoDesportivo esp = espacoService.getEspacoPorId(c.getValue().getEspacoId());
            return new SimpleStringProperty(esp != null ? esp.getDesignacao() : "N/A");
        });
        colEspaco.setPrefWidth(130);

        TableColumn<Reserva, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getData().toString()));
        colData.setPrefWidth(90);

        TableColumn<Reserva, String> colHorario = new TableColumn<>("Horário");
        colHorario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getHoraInicio() + "-" + c.getValue().getHoraFim()));
        colHorario.setPrefWidth(90);

        TableColumn<Reserva, String> colPreco = new TableColumn<>("Total");
        colPreco.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.2f€", c.getValue().getPrecoTotal())));
        colPreco.setPrefWidth(70);

        TableColumn<Reserva, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().getDescricao()));
        colEstado.setPrefWidth(70);

        TableColumn<Reserva, String> colPaga = new TableColumn<>("Paga");
        colPaga.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isPaga() ? "Sim" : "Não"));
        colPaga.setPrefWidth(50);

        tabela.getColumns().addAll(colId, colCliente, colEspaco, colData, colHorario, colPreco, colEstado, colPaga);

        // Ações
        HBox acoes = new HBox(10);
        acoes.setPadding(new Insets(5));

        Button aceitarBtn = new Button("Aceitar");
        aceitarBtn.getStyleClass().add("btn-success");
        aceitarBtn.setDisable(true);

        Button cancelarBtn = new Button("Cancelar");
        cancelarBtn.getStyleClass().add("btn-danger");
        cancelarBtn.setDisable(true);

        Button pagaBtn = new Button("Marcar Paga");
        pagaBtn.getStyleClass().add("btn-primary");
        pagaBtn.setDisable(true);

        Button naoPagaBtn = new Button("Marcar Não Paga");
        naoPagaBtn.getStyleClass().add("btn-secondary");
        naoPagaBtn.setDisable(true);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            boolean habilitado = novo != null;
            aceitarBtn.setDisable(!habilitado || novo.getEstado() != EstadoReserva.PENDENTE);
            cancelarBtn.setDisable(!habilitado || novo.getEstado() == EstadoReserva.CANCELADA);
            pagaBtn.setDisable(!habilitado || novo.isPaga());
            naoPagaBtn.setDisable(!habilitado || !novo.isPaga());
        });

        aceitarBtn.setOnAction(e -> {
            Reserva sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) {
                reservaService.aceitarReserva(sel.getId());
                atualizarTabela(filtroCombo.getValue(), filtroEstadoCombo.getValue());
            }
        });

        cancelarBtn.setOnAction(e -> {
            Reserva sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) {
                reservaService.cancelarReserva(sel.getId());
                atualizarTabela(filtroCombo.getValue(), filtroEstadoCombo.getValue());
            }
        });

        pagaBtn.setOnAction(e -> {
            Reserva sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) {
                reservaService.marcarPagamento(sel.getId(), true);
                atualizarTabela(filtroCombo.getValue(), filtroEstadoCombo.getValue());
            }
        });

        naoPagaBtn.setOnAction(e -> {
            Reserva sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null) {
                reservaService.marcarPagamento(sel.getId(), false);
                atualizarTabela(filtroCombo.getValue(), filtroEstadoCombo.getValue());
            }
        });

        acoes.getChildren().addAll(aceitarBtn, cancelarBtn, pagaBtn, naoPagaBtn);

        // Listeners dos filtros
        filtroCombo.setOnAction(e -> atualizarTabela(filtroCombo.getValue(), filtroEstadoCombo.getValue()));
        filtroEstadoCombo.setOnAction(e -> atualizarTabela(filtroCombo.getValue(), filtroEstadoCombo.getValue()));

        // Carregar dados iniciais
        atualizarTabela("Todos", "Todos");

        VBox.setVgrow(tabela, Priority.ALWAYS);
        root.getChildren().addAll(header, filtros, tabela, acoes);
    }

    private void atualizarTabela(String filtroEspaco, String filtroEstado) {
        List<Reserva> reservas = reservaService.getReservasPorGestor(gestor.getId());

        // Filtrar por espaço
        if (filtroEspaco != null && !filtroEspaco.equals("Todos")) {
            reservas = reservas.stream()
                    .filter(r -> {
                        EspacoDesportivo esp = espacoService.getEspacoPorId(r.getEspacoId());
                        return esp != null && esp.getDesignacao().equals(filtroEspaco);
                    }).toList();
        }

        // Filtrar por estado
        if (filtroEstado != null && !filtroEstado.equals("Todos")) {
            EstadoReserva estado = switch (filtroEstado) {
                case "Pendente" -> EstadoReserva.PENDENTE;
                case "Aceite" -> EstadoReserva.ACEITE;
                case "Cancelada" -> EstadoReserva.CANCELADA;
                default -> null;
            };
            if (estado != null) {
                EstadoReserva finalEstado = estado;
                reservas = reservas.stream()
                        .filter(r -> r.getEstado() == finalEstado).toList();
            }
        }

        tabela.setItems(FXCollections.observableArrayList(reservas));
    }

    public VBox getView() {
        return root;
    }
}
