package com.rogerio.cadastro.model;

public record Jogador(
        String nome, String email, String telefone, String codinome, GrupoCodinome grupoCodinome) {
}
