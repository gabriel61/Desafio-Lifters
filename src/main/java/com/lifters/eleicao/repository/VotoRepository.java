package com.lifters.eleicao.repository;

import com.lifters.eleicao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    List<Voto> findBySessaoId(Long sessaoId);
    long countBySessaoId(Long sessaoId);
    void deleteBySessaoId(Long sessaoId);
    boolean existsByCandidatoId(Long candidatoId);
    boolean existsByEleitorId(Long eleitorId);

    boolean existsByEleitorIdAndSessaoId(Long eleitorId, Long sessaoId);
}