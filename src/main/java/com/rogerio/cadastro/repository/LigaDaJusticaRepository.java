package com.rogerio.cadastro.repository;

import com.rogerio.cadastro.model.GrupoCodinome;
import com.rogerio.cadastro.web.VingadoresDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Repository
public class LigaDaJusticaRepository implements CodinomeRepository {

    @Override
    public List<String> buscarCodinomes() {
        var codinomes = RestClient
                .builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE)
                .baseUrl(GrupoCodinome.LIGA_DA_JUSTICA.getUri())
                .build()
                .get()
                .retrieve()
                .body(String.class);

        var objectMapper = new ObjectMapper();
        var vingadores = objectMapper.readValue(codinomes, VingadoresDTO.class);
        return vingadores.getCodinomes();
    }
}
