package com.lifters.eleicao.service;

import com.lifters.eleicao.model.Sessao;
import com.lifters.eleicao.repository.SessaoRepository;
import com.lifters.eleicao.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    public Sessao buscarPorId(Long id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
    }

    public Sessao abrirSessao(Sessao sessao) {
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setAberta(true);
        return sessaoRepository.save(sessao);
    }

    public Sessao fecharSessao(Long id) {
        Sessao sessao = buscarPorId(id);

        if (!sessao.isAberta()) {
            throw new IllegalStateException("A sessão já está fechada");
        }

        long quantidadeVotos = votoRepository.countBySessaoId(id);
        if (quantidadeVotos == 1) {
            votoRepository.deleteBySessaoId(id);
            quantidadeVotos = 0;
        }

        if (quantidadeVotos > 0 && quantidadeVotos < 2) {
            throw new IllegalStateException("Não é possível fechar a sessão com apenas um voto");
        }

        sessao.setDataFechamento(LocalDateTime.now());
        sessao.setAberta(false);
        return sessaoRepository.save(sessao);
    }
}