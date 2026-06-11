package com.sporthub.model;

/**
 * Tipos de espaco desportivo disponiveis.
 */
public enum TipoEspaco {
    FUTEBOL("Futebol"),
    TENIS("Tenis"),
    PISCINA("Piscina"),
    GINASIO("Ginasio"),
    PADEL("Padel"),
    BASQUETEBOL("Basquetebol"),
    VOLEIBOL("Voleibol"),
    OUTRO("Outro");

    private final String descricao;

    TipoEspaco(String descricao) {
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
