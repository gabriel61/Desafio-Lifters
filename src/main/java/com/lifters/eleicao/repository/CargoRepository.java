package com.lifters.eleicao.repository;

import com.lifters.eleicao.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    boolean existsByNome(String nome);
}