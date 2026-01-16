package com.rogerio.cadastro.repository;

import com.rogerio.cadastro.model.Jogador;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JogadorRepository {
    private final JdbcClient jdbcClient;

    public JogadorRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Jogador save(Jogador jogador) {
        jdbcClient.sql("""
                        INSERT INTO JOGADOR (nome, email, telefone, codinome, grupo_codinome) 
                        VALUES (:nome, :email, :telefone, :codinome, :grupoCodinome)
                        """)
                .param("nome", jogador.nome())
                .param("email", jogador.email())
                .param("telefone", jogador.telefone())
                .param("codinome", jogador.codinome())
                .param("grupoCodinome", jogador.grupoCodinome())
                .update();
        return jogador;
    }
}
