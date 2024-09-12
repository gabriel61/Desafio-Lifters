package com.lifters.eleicao.service;

import com.lifters.eleicao.model.Eleitor;
import com.lifters.eleicao.repository.EleitorRepository;
import com.lifters.eleicao.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EleitorService {

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private VotoRepository votoRepository;

    public List<Eleitor> listarTodos() {
        return eleitorRepository.findAll();
    }

    public Eleitor buscarPorId(Long id) {
        return eleitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Eleitor não encontrado"));
    }

    public Eleitor salvar(Eleitor eleitor) {
        if (eleitorRepository.existsByCpf(eleitor.getCpf())) {
            throw new IllegalArgumentException("Já existe um eleitor com este CPF");
        }
        return eleitorRepository.save(eleitor);
    }

    public Eleitor atualizar(Long id, Eleitor eleitor) {
        Eleitor eleitorExistente = buscarPorId(id);
        eleitorExistente.setNome(eleitor.getNome());
        eleitorExistente.setCpf(eleitor.getCpf());
        return eleitorRepository.save(eleitorExistente);
    }

    public void deletar(Long id) {
        Eleitor eleitor = buscarPorId(id);
        if (votoRepository.existsByEleitorId(id)) {
            throw new IllegalStateException("Não é possível deletar um eleitor que já votou");
        }
        eleitorRepository.delete(eleitor);
    }
}