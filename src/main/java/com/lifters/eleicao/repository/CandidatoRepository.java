package com.lifters.eleicao.repository;

import com.lifters.eleicao.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    boolean existsByNome(String nome);
}