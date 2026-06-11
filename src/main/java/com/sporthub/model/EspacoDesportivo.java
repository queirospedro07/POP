package com.sporthub.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um espaco desportivo disponivel para reserva.
 * Contem informacao sobre localizacao, precario e servicos adicionais.
 */
public class EspacoDesportivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String designacao;
    private String morada;
    private String localidade;
    private TipoEspaco tipoEspaco;
    private int gestorId;
    private List<Precario> precarios;
    private List<ServicoAdicional> servicosDisponiveis;

    public EspacoDesportivo() {
        this.precarios = new ArrayList<>();
        this.servicosDisponiveis = new ArrayList<>();
    }

    public EspacoDesportivo(int id, String designacao, String morada, String localidade,
                            TipoEspaco tipoEspaco, int gestorId) {
        this.id = id;
        this.designacao = designacao;
        this.morada = morada;
        this.localidade = localidade;
        this.tipoEspaco = tipoEspaco;
        this.gestorId = gestorId;
        this.precarios = new ArrayList<>();
        this.servicosDisponiveis = new ArrayList<>();
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDesignacao() { return designacao; }
    public void setDesignacao(String designacao) { this.designacao = designacao; }

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }

    public String getLocalidade() { return localidade; }
    public void setLocalidade(String localidade) { this.localidade = localidade; }

    public TipoEspaco getTipoEspaco() { return tipoEspaco; }
    public void setTipoEspaco(TipoEspaco tipoEspaco) { this.tipoEspaco = tipoEspaco; }

    public int getGestorId() { return gestorId; }
    public void setGestorId(int gestorId) { this.gestorId = gestorId; }

    public List<Precario> getPrecarios() { return precarios; }
    public void setPrecarios(List<Precario> precarios) { this.precarios = precarios; }

    public List<ServicoAdicional> getServicosDisponiveis() { return servicosDisponiveis; }
    public void setServicosDisponiveis(List<ServicoAdicional> servicosDisponiveis) { this.servicosDisponiveis = servicosDisponiveis; }

    public void adicionarPrecario(Precario precario) {
        this.precarios.add(precario);
    }

    public void adicionarServico(ServicoAdicional servico) {
        this.servicosDisponiveis.add(servico);
    }

    @Override
    public String toString() {
        return designacao + " - " + localidade + " (" + tipoEspaco.getDescricao() + ")";
    }
}
