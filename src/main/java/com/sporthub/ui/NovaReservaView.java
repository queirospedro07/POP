package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.model.*;
import com.sporthub.service.EspacoService;
import com.sporthub.service.ReservaService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista para criar uma nova reserva (cliente).
 */
public class NovaReservaView {
    private final VBox root;
    private final EspacoService espacoService;
    private final ReservaService reservaService;
    private final Utilizador cliente;

    public NovaReservaView() {
        this.espacoService = MainApp.getEspacoService();
        this.reservaService = MainApp.getReservaService();
        this.cliente = MainApp.getAuthService().getUtilizadorAtual();
        this.root = new VBox(15);
        root.setPadding(new Insets(10));
        construirVista();
    }

    private void construirVista() {
        // Header
        VBox header = new VBox(4);
        Label titulo = new Label("Nova Reserva");
        titulo.getStyleClass().add("content-title");
        Label subtituloLabel = new Label("Selecione um espaco, data e horario para reservar");
        subtituloLabel.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtituloLabel);

        // Aviso de reservas não pagas
        Label avisoLabel = new Label();
        avisoLabel.getStyleClass().add("error-label");
        if (reservaService.clienteTemReservasNaoPagas(cliente.getId())) {
            avisoLabel.setText("⚠ Tem reservas não pagas. Não poderá fazer nova reserva até regularizar o pagamento.");
        }

        // Formulário
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);
        form.getStyleClass().add("card");
        form.setPadding(new Insets(20));

        // Espaço
        Label espacoLabel = new Label("Espaço:");
        espacoLabel.getStyleClass().add("form-label");
        ComboBox<EspacoDesportivo> espacoCombo = new ComboBox<>();
        espacoCombo.setItems(FXCollections.observableArrayList(espacoService.getTodosEspacos()));
        espacoCombo.setPromptText("Selecione o espaço");
        espacoCombo.setPrefWidth(300);

        // Data
        Label dataLabel = new Label("Data:");
        dataLabel.getStyleClass().add("form-label");
        DatePicker dataPicker = new DatePicker();
        dataPicker.setValue(LocalDate.now().plusDays(1));

        // Hora início
        Label horaInicioLabel = new Label("Hora Início:");
        horaInicioLabel.getStyleClass().add("form-label");
        ComboBox<String> horaInicioCombo = new ComboBox<>();
        for (int h = 6; h <= 23; h++) {
            horaInicioCombo.getItems().add(String.format("%02d:00", h));
        }
        horaInicioCombo.setValue("09:00");

        // Hora fim
        Label horaFimLabel = new Label("Hora Fim:");
        horaFimLabel.getStyleClass().add("form-label");
        ComboBox<String> horaFimCombo = new ComboBox<>();
        for (int h = 7; h <= 24; h++) {
            horaFimCombo.getItems().add(String.format("%02d:00", h));
        }
        horaFimCombo.setValue("10:00");

        form.add(espacoLabel, 0, 0);
        form.add(espacoCombo, 1, 0);
        form.add(dataLabel, 0, 1);
        form.add(dataPicker, 1, 1);
        form.add(horaInicioLabel, 0, 2);
        form.add(horaInicioCombo, 1, 2);
        form.add(horaFimLabel, 0, 3);
        form.add(horaFimCombo, 1, 3);

        // Serviços adicionais
        Label servicosLabel = new Label("Serviços Adicionais:");
        servicosLabel.getStyleClass().add("form-label");
        servicosLabel.setStyle("-fx-font-size: 14px;");

        VBox servicosBox = new VBox(5);
        servicosBox.setPadding(new Insets(10));
        List<CheckBox> servicoChecks = new ArrayList<>();

        espacoCombo.setOnAction(e -> {
            servicosBox.getChildren().clear();
            servicoChecks.clear();
            EspacoDesportivo espaco = espacoCombo.getValue();
            if (espaco != null && !espaco.getServicosDisponiveis().isEmpty()) {
                for (ServicoAdicional s : espaco.getServicosDisponiveis()) {
                    CheckBox cb = new CheckBox(s.toString());
                    cb.setUserData(s);
                    servicoChecks.add(cb);
                    servicosBox.getChildren().add(cb);
                }
            } else {
                servicosBox.getChildren().add(new Label("Sem serviços adicionais disponíveis."));
            }
        });

        // Preço estimado
        Label precoLabel = new Label();
        precoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button calcularBtn = new Button("Calcular Preço");
        calcularBtn.getStyleClass().add("btn-secondary");
        calcularBtn.setOnAction(e -> {
            EspacoDesportivo espaco = espacoCombo.getValue();
            if (espaco == null) {
                precoLabel.setText("Selecione um espaço.");
                return;
            }
            try {
                LocalTime hi = LocalTime.of(Integer.parseInt(horaInicioCombo.getValue().substring(0, 2)), 0);
                LocalTime hf = LocalTime.of(Integer.parseInt(horaFimCombo.getValue().substring(0, 2)), 0);
                double precoBase = reservaService.calcularPrecoBase(espaco, hi, hf);
                if (precoBase < 0) {
                    precoLabel.setText("Horário não coberto pelo preçário.");
                    precoLabel.getStyleClass().add("error-label");
                } else {
                    // Somar serviços
                    double custoServicos = 0;
                    int duracao = hf.getHour() - hi.getHour();
                    for (CheckBox cb : servicoChecks) {
                        if (cb.isSelected()) {
                            ServicoAdicional s = (ServicoAdicional) cb.getUserData();
                            custoServicos += s.calcularCusto(duracao);
                        }
                    }
                    precoLabel.setText(String.format("Preço estimado: %.2f€ (base) + %.2f€ (serviços) = %.2f€",
                            precoBase, custoServicos, precoBase + custoServicos));
                    precoLabel.getStyleClass().remove("error-label");
                }
            } catch (Exception ex) {
                precoLabel.setText("Verifique os horários.");
            }
        });

        // Botão reservar
        Label resultLabel = new Label();

        Button reservarBtn = new Button("Reservar");
        reservarBtn.getStyleClass().add("btn-primary");
        reservarBtn.setOnAction(e -> {
            EspacoDesportivo espaco = espacoCombo.getValue();
            if (espaco == null) {
                resultLabel.setText("Selecione um espaço.");
                resultLabel.getStyleClass().setAll("error-label");
                return;
            }

            try {
                LocalDate data = dataPicker.getValue();
                LocalTime hi = LocalTime.of(Integer.parseInt(horaInicioCombo.getValue().substring(0, 2)), 0);
                LocalTime hf = LocalTime.of(Integer.parseInt(horaFimCombo.getValue().substring(0, 2)), 0);

                List<ServicoAdicional> servicosSelecionados = new ArrayList<>();
                for (CheckBox cb : servicoChecks) {
                    if (cb.isSelected()) {
                        servicosSelecionados.add((ServicoAdicional) cb.getUserData());
                    }
                }

                String erro = reservaService.criarReserva(
                        cliente.getId(), espaco.getId(), data, hi, hf, servicosSelecionados
                );

                if (erro != null) {
                    resultLabel.setText(erro);
                    resultLabel.getStyleClass().setAll("error-label");
                } else {
                    resultLabel.setText("Reserva criada com sucesso! Aguarde aprovação do gestor.");
                    resultLabel.getStyleClass().setAll("success-label");
                }
            } catch (Exception ex) {
                resultLabel.setText("Erro: verifique os dados introduzidos.");
                resultLabel.getStyleClass().setAll("error-label");
            }
        });

        HBox botoesBox = new HBox(10, calcularBtn, reservarBtn);

        root.getChildren().addAll(header, avisoLabel, form, servicosLabel, servicosBox,
                precoLabel, botoesBox, resultLabel);
    }

    public VBox getView() {
        return root;
    }
}
