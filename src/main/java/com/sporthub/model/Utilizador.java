package com.sporthub.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa um utilizador do sistema (Cliente ou Gestor de Espaco).
 */
public class Utilizador implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String nif;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String password;
    private TipoUtilizador tipo;

    public Utilizador() {
    }

    public Utilizador(int id, String nome, String nif, LocalDate dataNascimento,
                      String telefone, String email, String password, TipoUtilizador tipo) {
        this.id = id;
        this.nome = nome;
        this.nif = nif;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public TipoUtilizador getTipo() { return tipo; }
    public void setTipo(TipoUtilizador tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return nome + " (" + email + ")";
    }
}
