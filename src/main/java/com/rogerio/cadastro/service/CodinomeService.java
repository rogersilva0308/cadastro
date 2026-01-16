package com.rogerio.cadastro.service;

import com.rogerio.cadastro.model.GrupoCodinome;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodinomeService {

    private final CodinomesRepositoryFactory codinomesRepositoryFactory;

    public CodinomeService(CodinomesRepositoryFactory codinomesRepositoryFactory) {
        this.codinomesRepositoryFactory = codinomesRepositoryFactory;
    }

    public String gerarCodinome(GrupoCodinome grupoCodinome, List<String> codinomesEmUso) {

        var codinomesDisponiveis = listarCodinomesDisponiveis(grupoCodinome, codinomesEmUso);
        if (codinomesDisponiveis.isEmpty()) {
            throw new RuntimeException("Não há codinomes disponíveis para o grupo " + grupoCodinome.getNome());
        }

        var codinomeSorteado = sortearCodinome(codinomesDisponiveis);
        return codinomeSorteado;
    }

    private List<String> listarCodinomesDisponiveis(GrupoCodinome grupoCodinome, List<String> codinomesEmUso) {
        var codinomes = buscarCodinomes(grupoCodinome);

        var codinomesDisponiveis = codinomes
                .stream()
                .filter(codinome -> !codinomesEmUso.contains(codinome))
                .toList();

        return codinomesDisponiveis;
    }

    private List<String> buscarCodinomes(GrupoCodinome grupoCodinome) {
        var codinomeRepository = codinomesRepositoryFactory.create(grupoCodinome);
        return codinomeRepository.buscarCodinomes();
    }


    private String sortearCodinome(List<String> codinomesDisponiveis) {
        return codinomesDisponiveis
                .get((int) (Math.random() * codinomesDisponiveis.size()));
    }

}
