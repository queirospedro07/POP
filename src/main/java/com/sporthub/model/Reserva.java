package com.sporthub.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma reserva de um espaco desportivo feita por um cliente.
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int clienteId;
    private int espacoId;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private double precoBase;
    private List<ServicoAdicional> servicosAdicionais;
    private EstadoReserva estado;
    private boolean paga;

    public Reserva() {
        this.servicosAdicionais = new ArrayList<>();
        this.estado = EstadoReserva.PENDENTE;
        this.paga = false;
    }

    public Reserva(int id, int clienteId, int espacoId, LocalDate data,
                   LocalTime horaInicio, LocalTime horaFim, double precoBase) {
        this.id = id;
        this.clienteId = clienteId;
        this.espacoId = espacoId;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.precoBase = precoBase;
        this.servicosAdicionais = new ArrayList<>();
        this.estado = EstadoReserva.PENDENTE;
        this.paga = false;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getEspacoId() { return espacoId; }
    public void setEspacoId(int espacoId) { this.espacoId = espacoId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }

    public double getPrecoBase() { return precoBase; }
    public void setPrecoBase(double precoBase) { this.precoBase = precoBase; }

    public List<ServicoAdicional> getServicosAdicionais() { return servicosAdicionais; }
    public void setServicosAdicionais(List<ServicoAdicional> servicosAdicionais) { this.servicosAdicionais = servicosAdicionais; }

    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public boolean isPaga() { return paga; }
    public void setPaga(boolean paga) { this.paga = paga; }

    public void adicionarServico(ServicoAdicional servico) {
        this.servicosAdicionais.add(servico);
    }

    /**
     * @return duracao da reserva em horas completas
     */
    public int getDuracaoHoras() {
        return horaFim.getHour() - horaInicio.getHour();
    }

    /**
     * @return custo total dos servicos adicionais
     */
    public double getCustoServicos() {
        int duracao = getDuracaoHoras();
        return servicosAdicionais.stream()
                .mapToDouble(s -> s.calcularCusto(duracao))
                .sum();
    }

    /**
     * @return preco total da reserva (base + servicos)
     */
    public double getPrecoTotal() {
        return precoBase + getCustoServicos();
    }

    @Override
    public String toString() {
        return String.format("Reserva #%d - %s %s-%s [%s] %s",
                id, data, horaInicio, horaFim, estado.getDescricao(),
                paga ? "(Paga)" : "(Nao Paga)");
    }
}
