package com.rogerio.cadastro.service;

import com.rogerio.cadastro.model.GrupoCodinome;
import com.rogerio.cadastro.repository.CodinomeRepository;
import com.rogerio.cadastro.repository.LigaDaJusticaRepository;
import com.rogerio.cadastro.repository.VingadoresRepository;
import org.springframework.stereotype.Component;

@Component
public class CodinomesRepositoryFactory {

    private final LigaDaJusticaRepository ligaDaJusticaRepository;
    private final VingadoresRepository vingadoresRepository;

    public CodinomesRepositoryFactory(LigaDaJusticaRepository ligaDaJusticaRepository,
                                      VingadoresRepository vingadoresRepository) {
        this.ligaDaJusticaRepository = ligaDaJusticaRepository;
        this.vingadoresRepository = vingadoresRepository;
    }

    public CodinomeRepository create(GrupoCodinome grupoCodinome){
        return switch (grupoCodinome){
            case LIGA_DA_JUSTICA -> ligaDaJusticaRepository;
            case VINGADORES -> vingadoresRepository;
        };
    }
}
