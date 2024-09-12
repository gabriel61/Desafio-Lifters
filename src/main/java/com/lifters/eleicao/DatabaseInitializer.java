package com.lifters.eleicao;

import com.lifters.eleicao.model.Candidato;
import com.lifters.eleicao.model.Cargo;
import com.lifters.eleicao.model.Eleitor;
import com.lifters.eleicao.model.Sessao;
import com.lifters.eleicao.repository.VotoRepository;
import com.lifters.eleicao.repository.CandidatoRepository;
import com.lifters.eleicao.repository.CargoRepository;
import com.lifters.eleicao.repository.EleitorRepository;
import com.lifters.eleicao.repository.SessaoRepository;
import com.lifters.eleicao.service.VotoService;
import com.lifters.eleicao.service.SessaoService;
import com.lifters.eleicao.service.BoletimService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.Random;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private VotoService votoService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private BoletimService boletimService;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Override
    public void run(String... args) throws Exception {

        logger.info("Iniciando a configuração do banco de dados...");

        votoRepository.deleteAll();
        candidatoRepository.deleteAll();
        eleitorRepository.deleteAll();
        sessaoRepository.deleteAll();
        cargoRepository.deleteAll();

        // Verifica o maior ID atual de eleitores
        Long maxEleitorId = eleitorRepository.findAll()
                .stream()
                .map(Eleitor::getId)
                .max(Long::compare)
                .orElse(0L);

        // Adiciona novos eleitores a partir do maior ID atual
        for (int i = 1; i <= 81; i++) {
            Eleitor eleitor = new Eleitor("Eleitor " + (maxEleitorId + i), "cpf" + (maxEleitorId + i));
            eleitorRepository.save(eleitor);
            logger.info("Eleitor salvo: ID = {}", eleitor.getId());
        }

        // Obtém o ID do primeiro eleitor salvo
        Long primeiroEleitorId = eleitorRepository.findAll().stream()
                .min(Comparator.comparing(Eleitor::getId))
                .map(Eleitor::getId)
                .orElseThrow(() -> new RuntimeException("Nenhum eleitor encontrado."));

        // Criar cargos
        Cargo presidente = new Cargo("PRESIDENTE");
        cargoRepository.save(presidente);

        // Criar candidatos
        for (int i = 1; i <= 8; i++) {
            Candidato candidato = new Candidato("XPTO " + i, presidente);
            candidatoRepository.save(candidato);
            logger.info("Candidato salvo: " + candidato.getNome() + " com ID: " + candidato.getId());
        }

        // Criar e abrir sessão
        Sessao sessao = new Sessao(LocalDateTime.now(), null, true, presidente);
        sessaoRepository.save(sessao);
        logger.info("Sessão criada com ID: {}", sessao.getId());

        // Verifiquando se a sessão foi salva corretamente
        Optional<Sessao> sessaoOptional = sessaoRepository.findById(sessao.getId());
        if (sessaoOptional.isEmpty()) {
            logger.error("Erro: Sessão não encontrada no banco de dados após ser salva.");
            throw new RuntimeException("Sessão não encontrada.");
        }
        logger.info("Sessão verificada com sucesso: {}", sessaoOptional.get());

        // Buscar todos os candidatos do bd
        List<Candidato> candidatos = candidatoRepository.findAll();

        if (candidatos.isEmpty()) {
            logger.error("Erro: Nenhum candidato encontrado no banco de dados.");
            throw new RuntimeException("Nenhum candidato disponível para registrar votos.");
        }
        logger.info("Total de candidatos encontrados: {}", candidatos.size());

        // Registra votos aleatórios
        for (int i = 0; i <= 81; i++) {
            Long eleitorId = primeiroEleitorId + (long) i;
            Long candidatoId = candidatos.get(new Random().nextInt(candidatos.size())).getId();

            // Verifica se o eleitor existe
            Optional<Eleitor> eleitorOptional = eleitorRepository.findById(eleitorId);
            if (eleitorOptional.isEmpty()) {
                logger.error("Erro: Eleitor com ID {} não encontrado.", eleitorId);
                continue;
            }

            // Tenta registrar o voto
            try {
                logger.info("Registrando voto para Eleitor ID: {} para Candidato ID: {}", eleitorId, candidatoId);
                votoService.votar(eleitorId, candidatoId, sessao.getId());
            } catch (Exception e) {
                logger.error("Erro ao registrar o voto para Eleitor ID: {} para Candidato ID: {}: {}", eleitorId, candidatoId, e.getMessage());
            }
        }

        // Fecha a sessão
        sessaoService.fecharSessao(sessao.getId());

        // Gera o boletim
        String boletim = boletimService.gerarBoletim(sessao.getId());
        System.out.println(boletim);
        System.out.println(sessao.getId());
    }
}
