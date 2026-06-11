package com.sporthub.service;

import com.sporthub.data.DataStore;
import com.sporthub.model.*;

import java.util.List;

/**
 * Servico de gestao de espacos desportivos.
 */
public class EspacoService {

    private final DataStore dataStore;

    public EspacoService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Cria um novo espaco desportivo.
     *
     * @return mensagem de erro ou null se criado com sucesso
     */
    public String criarEspaco(String designacao, String morada, String localidade,
                              TipoEspaco tipoEspaco, int gestorId) {
        if (designacao == null || designacao.isBlank()) return "Designacao e obrigatoria.";
        if (morada == null || morada.isBlank()) return "Morada e obrigatoria.";
        if (localidade == null || localidade.isBlank()) return "Localidade e obrigatoria.";
        if (tipoEspaco == null) return "Tipo de espaco e obrigatorio.";

        EspacoDesportivo espaco = new EspacoDesportivo(
                dataStore.proximoIdEspaco(), designacao, morada, localidade, tipoEspaco, gestorId
        );
        dataStore.adicionarEspaco(espaco);
        return null;
    }

    /**
     * Atualiza os dados de um espaco existente.
     *
     * @return mensagem de erro ou null se atualizado com sucesso
     */
    public String atualizarEspaco(int id, String designacao, String morada,
                                  String localidade, TipoEspaco tipoEspaco) {
        if (designacao == null || designacao.isBlank()) return "Designacao e obrigatoria.";
        if (morada == null || morada.isBlank()) return "Morada e obrigatoria.";
        if (localidade == null || localidade.isBlank()) return "Localidade e obrigatoria.";

        EspacoDesportivo espaco = dataStore.getEspacoPorId(id);
        if (espaco == null) return "Espaco nao encontrado.";

        espaco.setDesignacao(designacao);
        espaco.setMorada(morada);
        espaco.setLocalidade(localidade);
        espaco.setTipoEspaco(tipoEspaco);
        dataStore.atualizarEspaco(espaco);
        return null;
    }

    /**
     * Adiciona um precario (faixa horaria com preco) a um espaco.
     */
    public String adicionarPrecario(int espacoId, int horaInicio, int horaFim, double precoPorHora) {
        if (horaInicio < 0 || horaInicio > 23) return "Hora de inicio invalida.";
        if (horaFim < 1 || horaFim > 24) return "Hora de fim invalida.";
        if (horaInicio >= horaFim) return "Hora de inicio deve ser menor que hora de fim.";
        if (precoPorHora <= 0) return "Preco deve ser positivo.";

        EspacoDesportivo espaco = dataStore.getEspacoPorId(espacoId);
        if (espaco == null) return "Espaco nao encontrado.";

        espaco.adicionarPrecario(new Precario(horaInicio, horaFim, precoPorHora));
        dataStore.atualizarEspaco(espaco);
        return null;
    }

    /**
     * Adiciona um servico adicional a um espaco.
     */
    public String adicionarServico(int espacoId, String nome, String descricao,
                                   double preco, TipoServico tipoServico) {
        if (nome == null || nome.isBlank()) return "Nome do servico e obrigatorio.";
        if (preco <= 0) return "Preco deve ser positivo.";

        EspacoDesportivo espaco = dataStore.getEspacoPorId(espacoId);
        if (espaco == null) return "Espaco nao encontrado.";

        int novoId = espaco.getServicosDisponiveis().stream()
                .mapToInt(ServicoAdicional::getId).max().orElse(0) + 1;

        espaco.adicionarServico(new ServicoAdicional(novoId, nome, descricao, preco, tipoServico));
        dataStore.atualizarEspaco(espaco);
        return null;
    }

    public void removerPrecario(int espacoId, int index) {
        EspacoDesportivo espaco = dataStore.getEspacoPorId(espacoId);
        if (espaco != null && index >= 0 && index < espaco.getPrecarios().size()) {
            espaco.getPrecarios().remove(index);
            dataStore.atualizarEspaco(espaco);
        }
    }

    public void removerServico(int espacoId, int servicoId) {
        EspacoDesportivo espaco = dataStore.getEspacoPorId(espacoId);
        if (espaco != null) {
            espaco.getServicosDisponiveis().removeIf(s -> s.getId() == servicoId);
            dataStore.atualizarEspaco(espaco);
        }
    }

    public void removerEspaco(int id) {
        dataStore.removerEspaco(id);
    }

    public List<EspacoDesportivo> getTodosEspacos() { return dataStore.getEspacos(); }

    public List<EspacoDesportivo> getEspacosPorGestor(int gestorId) { return dataStore.getEspacosPorGestor(gestorId); }

    public EspacoDesportivo getEspacoPorId(int id) { return dataStore.getEspacoPorId(id); }
}
