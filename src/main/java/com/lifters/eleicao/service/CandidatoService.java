package com.lifters.eleicao.service;

import com.lifters.eleicao.model.Candidato;
import com.lifters.eleicao.repository.CandidatoRepository;
import com.lifters.eleicao.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VotoRepository votoRepository;

    public List<Candidato> listarTodos() {
        return candidatoRepository.findAll();
    }

    public Candidato buscarPorId(Long id) {
        return candidatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
    }

    public Candidato salvar(Candidato candidato) {
        if (candidatoRepository.existsByNome(candidato.getNome())) {
            throw new IllegalArgumentException("Já existe um candidato com este nome");
        }
        return candidatoRepository.save(candidato);
    }

    public Candidato atualizar(Long id, Candidato candidato) {
        Candidato candidatoExistente = buscarPorId(id);
        candidatoExistente.setNome(candidato.getNome());
        candidatoExistente.setCargo(candidato.getCargo());
        return candidatoRepository.save(candidatoExistente);
    }

    public void deletar(Long id) {
        Candidato candidato = buscarPorId(id);
        if (votoRepository.existsByCandidatoId(id)) {
            throw new IllegalStateException("Não é possível deletar um candidato que já possui votos");
        }
        candidatoRepository.delete(candidato);
    }
}