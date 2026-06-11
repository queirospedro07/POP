package com.sporthub.service;

import com.sporthub.data.DataStore;
import com.sporthub.model.TipoUtilizador;
import com.sporthub.model.Utilizador;

import java.time.LocalDate;

/**
 * Servico de autenticacao e registo de utilizadores.
 */
public class AuthService {

    private final DataStore dataStore;
    private Utilizador utilizadorAtual;

    public AuthService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Autentica um utilizador por email e password.
     *
     * @return true se as credenciais forem validas
     */
    public boolean login(String email, String password) {
        Utilizador u = dataStore.getUtilizadorPorEmail(email);
        if (u != null && u.getPassword().equals(password)) {
            this.utilizadorAtual = u;
            return true;
        }
        return false;
    }

    /**
     * Regista um novo utilizador no sistema.
     *
     * @return mensagem de erro ou null se registado com sucesso
     */
    public String registar(String nome, String nif, LocalDate dataNascimento,
                           String telefone, String email, String password, TipoUtilizador tipo) {
        if (nome == null || nome.isBlank()) return "Nome e obrigatorio.";
        if (nif == null || nif.length() != 9) return "NIF deve ter 9 digitos.";
        if (dataNascimento == null) return "Data de nascimento e obrigatoria.";
        if (telefone == null || telefone.isBlank()) return "Telefone e obrigatorio.";
        if (email == null || !email.contains("@")) return "Email invalido.";
        if (password == null || password.length() < 4) return "Password deve ter pelo menos 4 caracteres.";
        if (dataStore.getUtilizadorPorEmail(email) != null) return "Ja existe um utilizador com este email.";

        Utilizador novo = new Utilizador(
                dataStore.proximoIdUtilizador(),
                nome, nif, dataNascimento, telefone, email, password, tipo
        );
        dataStore.adicionarUtilizador(novo);
        return null;
    }

    public void logout() {
        this.utilizadorAtual = null;
    }

    public Utilizador getUtilizadorAtual() { return utilizadorAtual; }

    public boolean isLoggedIn() { return utilizadorAtual != null; }

    public boolean isCliente() {
        return utilizadorAtual != null && utilizadorAtual.getTipo() == TipoUtilizador.CLIENTE;
    }

    public boolean isGestor() {
        return utilizadorAtual != null && utilizadorAtual.getTipo() == TipoUtilizador.GESTOR;
    }
}
