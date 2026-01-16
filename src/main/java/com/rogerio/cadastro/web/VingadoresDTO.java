package com.rogerio.cadastro.web;

import java.util.List;

public record VingadoresDTO(List<Codinome> vingadores) implements CodinomeDTO{
    @Override
    public List<String> getCodinomes() {
        return vingadores.stream().map(Codinome::codinome).toList();
    }
}

record Codinome(String codinome) {
}
