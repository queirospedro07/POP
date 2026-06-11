package com.sporthub.service;

import com.sporthub.data.DataStore;
import com.sporthub.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servico de gestao de reservas.
 * Implementa as regras de negocio relativas a reservas.
 */
public class ReservaService {

    private final DataStore dataStore;

    public ReservaService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Verifica se o cliente tem reservas aceites nao pagas.
     * Um cliente com dividas nao pode fazer novas reservas.
     */
    public boolean clienteTemReservasNaoPagas(int clienteId) {
        return dataStore.getReservasPorCliente(clienteId).stream()
                .anyMatch(r -> !r.isPaga() && r.getEstado() == EstadoReserva.ACEITE);
    }

    /**
     * Verifica se o espaco esta disponivel na data/hora indicadas.
     */
    public boolean espacoDisponivel(int espacoId, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        return dataStore.getReservasPorEspaco(espacoId).stream()
                .filter(r -> r.getData().equals(data))
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .noneMatch(r -> horasConflitam(r.getHoraInicio(), r.getHoraFim(), horaInicio, horaFim));
    }

    /**
     * Calcula o preco base conforme o precario do espaco.
     *
     * @return preco total ou -1 se o horario nao esta coberto pelo precario
     */
    public double calcularPrecoBase(EspacoDesportivo espaco, LocalTime horaInicio, LocalTime horaFim) {
        double total = 0;
        for (int h = horaInicio.getHour(); h < horaFim.getHour(); h++) {
            boolean encontrou = false;
            for (Precario p : espaco.getPrecarios()) {
                if (p.contemHora(h)) {
                    total += p.getPrecoPorHora();
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                return -1;
            }
        }
        return total;
    }

    /**
     * Cria uma nova reserva com validacao de regras de negocio.
     *
     * @return mensagem de erro ou null se criada com sucesso
     */
    public String criarReserva(int clienteId, int espacoId, LocalDate data,
                               LocalTime horaInicio, LocalTime horaFim,
                               List<ServicoAdicional> servicos) {
        if (data == null) return "Data e obrigatoria.";
        if (data.isBefore(LocalDate.now())) return "Nao e possivel reservar no passado.";
        if (horaInicio == null || horaFim == null) return "Hora de inicio e fim sao obrigatorias.";
        if (!horaFim.isAfter(horaInicio)) return "Hora de fim deve ser posterior a hora de inicio.";

        if (clienteTemReservasNaoPagas(clienteId)) {
            return "Nao pode reservar enquanto tiver reservas nao pagas.";
        }

        if (!espacoDisponivel(espacoId, data, horaInicio, horaFim)) {
            return "O espaco nao esta disponivel neste horario.";
        }

        EspacoDesportivo espaco = dataStore.getEspacoPorId(espacoId);
        if (espaco == null) return "Espaco nao encontrado.";

        double precoBase = calcularPrecoBase(espaco, horaInicio, horaFim);
        if (precoBase < 0) {
            return "O horario selecionado nao esta coberto pelo precario do espaco.";
        }

        Reserva reserva = new Reserva(
                dataStore.proximoIdReserva(), clienteId, espacoId, data, horaInicio, horaFim, precoBase
        );
        if (servicos != null) {
            servicos.forEach(reserva::adicionarServico);
        }

        dataStore.adicionarReserva(reserva);
        return null;
    }

    /**
     * Aceita uma reserva pendente (acao do gestor).
     */
    public void aceitarReserva(int reservaId) {
        Reserva r = dataStore.getReservaPorId(reservaId);
        if (r != null && r.getEstado() == EstadoReserva.PENDENTE) {
            r.setEstado(EstadoReserva.ACEITE);
            dataStore.atualizarReserva(r);
        }
    }

    /**
     * Cancela uma reserva que nao esteja ja cancelada.
     */
    public void cancelarReserva(int reservaId) {
        Reserva r = dataStore.getReservaPorId(reservaId);
        if (r != null && r.getEstado() != EstadoReserva.CANCELADA) {
            r.setEstado(EstadoReserva.CANCELADA);
            dataStore.atualizarReserva(r);
        }
    }

    /**
     * Altera o estado de pagamento de uma reserva.
     */
    public void marcarPagamento(int reservaId, boolean paga) {
        Reserva r = dataStore.getReservaPorId(reservaId);
        if (r != null) {
            r.setPaga(paga);
            dataStore.atualizarReserva(r);
        }
    }

    public List<Reserva> getReservasPorCliente(int clienteId) { return dataStore.getReservasPorCliente(clienteId); }

    public List<Reserva> getReservasPorEspaco(int espacoId) { return dataStore.getReservasPorEspaco(espacoId); }

    public List<Reserva> getReservasPorGestor(int gestorId) { return dataStore.getReservasPorGestor(gestorId); }

    // --- Metodo auxiliar ---

    private boolean horasConflitam(LocalTime inicio1, LocalTime fim1, LocalTime inicio2, LocalTime fim2) {
        return inicio1.isBefore(fim2) && inicio2.isBefore(fim1);
    }
}
