package com.lifters.eleicao.repository;

import com.lifters.eleicao.model.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
    boolean existsByCpf(String cpf);
}