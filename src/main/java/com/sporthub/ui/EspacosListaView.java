package com.sporthub.ui;

import com.sporthub.MainApp;
import com.sporthub.model.EspacoDesportivo;
import com.sporthub.model.Precario;
import com.sporthub.model.ServicoAdicional;
import com.sporthub.service.EspacoService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Lista de espacos desportivos disponiveis (vista do cliente).
 */
public class EspacosListaView {

    private final VBox root;
    private final EspacoService espacoService;

    public EspacosListaView() {
        this.espacoService = MainApp.getEspacoService();
        this.root = new VBox(16);
        root.setPadding(new Insets(0));
        construirVista();
    }

    @SuppressWarnings("unchecked")
    private void construirVista() {
        // Header
        VBox header = new VBox(4);
        Label titulo = new Label("Espacos Desportivos");
        titulo.getStyleClass().add("content-title");
        Label subtitulo = new Label("Consulte os espacos disponiveis e respetiva informacao");
        subtitulo.getStyleClass().add("content-subtitle");
        header.getChildren().addAll(titulo, subtitulo);

        // Tabela dentro de card
        VBox tabelaCard = new VBox(0);
        tabelaCard.getStyleClass().add("card");
        tabelaCard.setPadding(new Insets(0));

        TableView<EspacoDesportivo> tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Nenhum espaco disponivel de momento."));
        tabela.setPrefHeight(250);

        TableColumn<EspacoDesportivo, String> colDesignacao = new TableColumn<>("Designacao");
        colDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        colDesignacao.setPrefWidth(200);

        TableColumn<EspacoDesportivo, String> colMorada = new TableColumn<>("Morada");
        colMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        colMorada.setPrefWidth(200);

        TableColumn<EspacoDesportivo, String> colLocalidade = new TableColumn<>("Localidade");
        colLocalidade.setCellValueFactory(new PropertyValueFactory<>("localidade"));
        colLocalidade.setPrefWidth(140);

        TableColumn<EspacoDesportivo, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEspaco"));
        colTipo.setPrefWidth(110);

        tabela.getColumns().addAll(colDesignacao, colMorada, colLocalidade, colTipo);

        List<EspacoDesportivo> espacos = espacoService.getTodosEspacos();
        tabela.setItems(FXCollections.observableArrayList(espacos));
        tabelaCard.getChildren().add(tabela);

        // Detalhes
        VBox detalhes = new VBox(10);
        detalhes.getStyleClass().add("card");

        Label detalheTitulo = new Label("Detalhes do Espaco");
        detalheTitulo.getStyleClass().add("card-header");

        TextArea detalheTexto = new TextArea("Selecione um espaco na tabela acima para ver detalhes.");
        detalheTexto.setEditable(false);
        detalheTexto.setPrefHeight(140);
        detalheTexto.setWrapText(true);

        detalhes.getChildren().addAll(detalheTitulo, detalheTexto);

        // Listener de selecao
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) {
                detalheTitulo.setText(novo.getDesignacao());
                StringBuilder sb = new StringBuilder();
                sb.append("Morada: ").append(novo.getMorada()).append("\n");
                sb.append("Localidade: ").append(novo.getLocalidade()).append("\n");
                sb.append("Tipo: ").append(novo.getTipoEspaco().getDescricao()).append("\n\n");

                sb.append("PRECARIO:\n");
                if (novo.getPrecarios().isEmpty()) {
                    sb.append("  Sem precario definido.\n");
                } else {
                    for (Precario p : novo.getPrecarios()) {
                        sb.append("  ").append(p.toString()).append("\n");
                    }
                }

                sb.append("\nSERVICOS ADICIONAIS:\n");
                if (novo.getServicosDisponiveis().isEmpty()) {
                    sb.append("  Sem servicos adicionais.\n");
                } else {
                    for (ServicoAdicional s : novo.getServicosDisponiveis()) {
                        sb.append("  ").append(s.toString()).append("\n");
                    }
                }
                detalheTexto.setText(sb.toString());
            }
        });

        VBox.setVgrow(tabelaCard, Priority.ALWAYS);
        root.getChildren().addAll(header, tabelaCard, detalhes);
    }

    public VBox getView() {
        return root;
    }
}
