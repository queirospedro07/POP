package com.sporthub.model;

import java.io.Serializable;

/**
 * Define o preco por hora para um intervalo horario de um espaco desportivo.
 */
public class Precario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int horaInicio;
    private int horaFim;
    private double precoPorHora;

    public Precario() {
    }

    public Precario(int horaInicio, int horaFim, double precoPorHora) {
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.precoPorHora = precoPorHora;
    }

    // Getters e Setters

    public int getHoraInicio() { return horaInicio; }
    public void setHoraInicio(int horaInicio) { this.horaInicio = horaInicio; }

    public int getHoraFim() { return horaFim; }
    public void setHoraFim(int horaFim) { this.horaFim = horaFim; }

    public double getPrecoPorHora() { return precoPorHora; }
    public void setPrecoPorHora(double precoPorHora) { this.precoPorHora = precoPorHora; }

    /**
     * Verifica se uma determinada hora esta dentro deste intervalo.
     *
     * @param hora hora a verificar (0-23)
     * @return true se a hora esta coberta por este precario
     */
    public boolean contemHora(int hora) {
        if (horaInicio < horaFim) {
            return hora >= horaInicio && hora < horaFim;
        }
        // Caso o intervalo atravesse a meia-noite
        return hora >= horaInicio || hora < horaFim;
    }

    @Override
    public String toString() {
        return String.format("%02d:00 - %02d:00 | %.2f EUR/hora", horaInicio, horaFim, precoPorHora);
    }
}
