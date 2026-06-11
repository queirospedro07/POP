package com.sporthub.ui;

import com.sporthub.MainApp;
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
 * Vista do histórico de reservas do cliente.
 */
public class MinhasReservasView {
    private final VBox root;
    private final ReservaService reservaService;
    private final EspacoService espacoService;
    private final Utilizador cliente;

    public MinhasReservasView() {
        this.reservaService = MainApp.getReservaService();
        this.espacoService = MainApp.getEspacoService();
        this.cliente = MainApp.getAuthService().getUtilizadorAtual();
        this.root = new VBox(15);
        root.setPadding(new Insets(10));
        construirVista();
    }

    @SuppressWarnings("unchecked")
    private void construirVista() {
        // Header
        VBox header = new VBox(4);
        Label titulo = new Label("As Minhas Reservas");
        titulo.getStyleClass().add("content-title");
        Label subtitulo = new Label("Historico de todas as suas reservas");
        subtitulo.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtitulo);

        TableView<Reserva> tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Não possui reservas."));

        TableColumn<Reserva, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colId.setPrefWidth(40);

        TableColumn<Reserva, String> colEspaco = new TableColumn<>("Espaço");
        colEspaco.setCellValueFactory(c -> {
            EspacoDesportivo esp = espacoService.getEspacoPorId(c.getValue().getEspacoId());
            return new SimpleStringProperty(esp != null ? esp.getDesignacao() : "N/A");
        });
        colEspaco.setPrefWidth(150);

        TableColumn<Reserva, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getData().toString()));
        colData.setPrefWidth(100);

        TableColumn<Reserva, String> colHorario = new TableColumn<>("Horário");
        colHorario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getHoraInicio() + " - " + c.getValue().getHoraFim()));
        colHorario.setPrefWidth(110);

        TableColumn<Reserva, String> colPreco = new TableColumn<>("Preço Total");
        colPreco.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.2f€", c.getValue().getPrecoTotal())));
        colPreco.setPrefWidth(90);

        TableColumn<Reserva, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().getDescricao()));
        colEstado.setPrefWidth(80);

        TableColumn<Reserva, String> colPaga = new TableColumn<>("Pagamento");
        colPaga.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isPaga() ? "Paga" : "Não Paga"));
        colPaga.setPrefWidth(90);

        tabela.getColumns().addAll(colId, colEspaco, colData, colHorario, colPreco, colEstado, colPaga);

        List<Reserva> reservas = reservaService.getReservasPorCliente(cliente.getId());
        tabela.setItems(FXCollections.observableArrayList(reservas));

        // Detalhes e ações
        VBox detalhesBox = new VBox(10);
        detalhesBox.getStyleClass().add("card");
        detalhesBox.setPadding(new Insets(15));

        Label detalheLabel = new Label("Selecione uma reserva para ver detalhes");
        TextArea detalheTexto = new TextArea();
        detalheTexto.setEditable(false);
        detalheTexto.setPrefHeight(100);
        detalheTexto.setWrapText(true);

        Button cancelarBtn = new Button("Cancelar Reserva");
        cancelarBtn.getStyleClass().add("btn-danger");
        cancelarBtn.setDisable(true);

        cancelarBtn.setOnAction(e -> {
            Reserva sel = tabela.getSelectionModel().getSelectedItem();
            if (sel != null && sel.getEstado() != EstadoReserva.CANCELADA) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar Cancelamento");
                confirm.setHeaderText(null);
                confirm.setContentText("Tem a certeza que deseja cancelar esta reserva?");
                confirm.showAndWait().ifPresent(resp -> {
                    if (resp == ButtonType.OK) {
                        reservaService.cancelarReserva(sel.getId());
                        tabela.setItems(FXCollections.observableArrayList(
                                reservaService.getReservasPorCliente(cliente.getId())));
                        tabela.refresh();
                    }
                });
            }
        });

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) {
                EspacoDesportivo esp = espacoService.getEspacoPorId(novo.getEspacoId());
                StringBuilder sb = new StringBuilder();
                sb.append("Espaço: ").append(esp != null ? esp.getDesignacao() : "N/A").append("\n");
                sb.append("Data: ").append(novo.getData()).append("\n");
                sb.append("Horário: ").append(novo.getHoraInicio()).append(" - ").append(novo.getHoraFim()).append("\n");
                sb.append("Preço Base: ").append(String.format("%.2f€", novo.getPrecoBase())).append("\n");

                if (!novo.getServicosAdicionais().isEmpty()) {
                    sb.append("Serviços: ");
                    for (ServicoAdicional s : novo.getServicosAdicionais()) {
                        sb.append(s.getNome()).append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                    sb.append("\n");
                    sb.append("Custo Serviços: ").append(String.format("%.2f€", novo.getCustoServicos())).append("\n");
                }

                sb.append("Total: ").append(String.format("%.2f€", novo.getPrecoTotal()));
                detalheTexto.setText(sb.toString());

                cancelarBtn.setDisable(novo.getEstado() == EstadoReserva.CANCELADA);
            }
        });

        detalhesBox.getChildren().addAll(detalheLabel, detalheTexto, cancelarBtn);

        VBox.setVgrow(tabela, Priority.ALWAYS);
        root.getChildren().addAll(header, tabela, detalhesBox);
    }

    public VBox getView() {
        return root;
    }
}
