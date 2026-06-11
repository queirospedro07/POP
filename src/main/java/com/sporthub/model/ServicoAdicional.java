package com.sporthub.model;

import java.io.Serializable;

/**
 * Servico adicional que pode ser incluido numa reserva.
 * Pode ter preco unitario (cobrado uma vez) ou preco por hora.
 */
public class ServicoAdicional implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private TipoServico tipoServico;

    public ServicoAdicional() {
    }

    public ServicoAdicional(int id, String nome, String descricao, double preco, TipoServico tipoServico) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tipoServico = tipoServico;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public TipoServico getTipoServico() { return tipoServico; }
    public void setTipoServico(TipoServico tipoServico) { this.tipoServico = tipoServico; }

    /**
     * Calcula o custo deste servico para uma determinada duracao.
     *
     * @param duracaoHoras duracao da reserva em horas
     * @return custo total do servico
     */
    public double calcularCusto(int duracaoHoras) {
        if (tipoServico == TipoServico.POR_HORA) {
            return preco * duracaoHoras;
        }
        return preco;
    }

    @Override
    public String toString() {
        return nome + " - " + String.format("%.2f EUR", preco) + " (" + tipoServico.getDescricao() + ")";
    }
}
