package com.sporthub.model;

/**
 * Estados possiveis de uma reserva.
 */
public enum EstadoReserva {
    PENDENTE("Pendente"),
    ACEITE("Aceite"),
    CANCELADA("Cancelada");

    private final String descricao;

    EstadoReserva(String descricao) {
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
