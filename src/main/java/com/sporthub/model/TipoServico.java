package com.sporthub.model;

/**
 * Tipo de cobranca de um servico adicional.
 */
public enum TipoServico {
    UNITARIO("Preco Unitario"),
    POR_HORA("Preco por Hora");

    private final String descricao;

    TipoServico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
