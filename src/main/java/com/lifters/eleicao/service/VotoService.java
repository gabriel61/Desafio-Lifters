package com.lifters.eleicao.service;

import com.lifters.eleicao.model.Candidato;
import com.lifters.eleicao.model.Eleitor;
import com.lifters.eleicao.model.Sessao;
import com.lifters.eleicao.model.Voto;
import com.lifters.eleicao.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private EleitorService eleitorService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private SessaoService sessaoService;

    public Voto votar(Long eleitorId, Long candidatoId, Long sessaoId) {
        Eleitor eleitor = eleitorService.buscarPorId(eleitorId);
        Candidato candidato = candidatoService.buscarPorId(candidatoId);
        Sessao sessao = sessaoService.buscarPorId(sessaoId);

        if (!sessao.isAberta()) {
            throw new IllegalStateException("A sessão de votação está fechada.");
        }

        if (votoRepository.existsByEleitorIdAndSessaoId(eleitorId, sessaoId)) {
            throw new IllegalStateException("O eleitor já votou nesta sessão.");
        }

        if (!candidato.getCargo().equals(sessao.getCargo())) {
            throw new IllegalArgumentException("O candidato não pertence ao cargo desta sessão de votação.");
        }

        Voto voto = new Voto();
        voto.setEleitor(eleitor);
        voto.setCandidato(candidato);
        voto.setSessao(sessao);

        return votoRepository.save(voto);
    }
}