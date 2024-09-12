package com.lifters.eleicao;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.lifters.eleicao.repository.*;
import com.lifters.eleicao.model.*;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VotacaoSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @BeforeEach
    public void setup() {
        // Limpar o banco de dados antes de cada teste
        votoRepository.deleteAll();
        sessaoRepository.deleteAll();
        candidatoRepository.deleteAll();
        eleitorRepository.deleteAll();
        cargoRepository.deleteAll();
    }

    @Test
    public void testCRUDOperations() throws Exception {
        // Teste para Cargo
        ResultActions resultCargo = mockMvc.perform(post("/cargos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Presidente\"}"))
                .andExpect(status().isCreated());

        String cargoId = String.valueOf(JsonPath.read(resultCargo.andReturn().getResponse().getContentAsString(), "$.id"));

        // Teste para Candidato
        ResultActions resultCandidato = mockMvc.perform(post("/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Candidato 1\",\"cargoId\":%s}".formatted(cargoId)))
                .andExpect(status().isCreated());

        String candidatoId = String.valueOf(JsonPath.read(resultCandidato.andReturn().getResponse().getContentAsString(), "$.id"));

        // Teste para Eleitor
        ResultActions resultEleitor = mockMvc.perform(post("/eleitores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Eleitor 1\",\"cpf\":\"12345678901\"}"))
                .andExpect(status().isCreated());

        String eleitorId = String.valueOf(JsonPath.read(resultEleitor.andReturn().getResponse().getContentAsString(), "$.id"));

        // Testar leitura
        mockMvc.perform(get("/cargos/" + cargoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Presidente"));

        // Testar atualização
        mockMvc.perform(put("/candidatos/" + candidatoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Candidato 1 Atualizado\",\"cargoId\":%s}".formatted(cargoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Candidato 1 Atualizado"));

        // Testar deleção
        mockMvc.perform(delete("/eleitores/" + eleitorId))
                .andExpect(status().isNoContent());

        // Testar criação de duplicata (deve falhar)
        mockMvc.perform(post("/cargos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Presidente\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testVotacao() throws Exception {
        // Criar cargo, candidato, eleitor e sessão
        Cargo cargo = cargoRepository.save(new Cargo("Presidente"));
        Candidato candidato = candidatoRepository.save(new Candidato("Candidato 1", cargo));
        Eleitor eleitor = eleitorRepository.save(new Eleitor("Eleitor 1", "12345678901"));
        Sessao sessao = sessaoRepository.save(new Sessao(LocalDateTime.now(), null, true, cargo));

        // Testar votação válida
        mockMvc.perform(post("/eleitores/" + eleitor.getId() + "/votar")
                        .param("candidatoId", candidato.getId().toString())
                        .param("sessaoId", sessao.getId().toString()))
                .andExpect(status().isOk());

        // Testar votação duplicada (deve falhar)
        mockMvc.perform(post("/eleitores/" + eleitor.getId() + "/votar")
                        .param("candidatoId", String.valueOf(candidato.getId()))
                        .param("sessaoId", String.valueOf(sessao.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalStateException))
                .andExpect(result -> assertEquals("O eleitor já votou nesta sessão.", result.getResolvedException().getMessage()));

        // Fechar a sessão
        mockMvc.perform(patch("/sessoes/" + sessao.getId() + "/fechar"))
                .andExpect(status().isOk());

        // Testar votação em sessão fechada (deve falhar)
        Eleitor eleitor2 = eleitorRepository.save(new Eleitor("Eleitor 2", "98765432109"));
        mockMvc.perform(post("/eleitores/" + eleitor2.getId() + "/votar")
                        .param("candidatoId", candidato.getId().toString())
                        .param("sessaoId", sessao.getId().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalStateException))
                .andExpect(result -> assertEquals("A sessão está fechada.", result.getResolvedException().getMessage()));
    }

    @Test
    public void testBoletimUrna() throws Exception {
        // Criar cargo, candidatos, eleitores e sessão
        Cargo cargo = cargoRepository.save(new Cargo("Presidente"));
        Candidato candidato1 = candidatoRepository.save(new Candidato("Candidato 1", cargo));
        Candidato candidato2 = candidatoRepository.save(new Candidato("Candidato 2", cargo));
        Eleitor eleitor1 = eleitorRepository.save(new Eleitor("Eleitor 1", "12345678901"));
        Eleitor eleitor2 = eleitorRepository.save(new Eleitor("Eleitor 2", "98765432109"));
        Sessao sessao = sessaoRepository.save(new Sessao(LocalDateTime.now(), null, true, cargo));

        // Realizar votação
        mockMvc.perform(post("/eleitores/" + eleitor1.getId() + "/votar")
                        .param("candidatoId", candidato1.getId().toString())
                        .param("sessaoId", sessao.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/eleitores/" + eleitor2.getId() + "/votar")
                        .param("candidatoId", candidato2.getId().toString())
                        .param("sessaoId", sessao.getId().toString()))
                .andExpect(status().isOk());

        // Fechar a sessão
        mockMvc.perform(patch("/sessoes/" + sessao.getId() + "/fechar"))
                .andExpect(status().isOk());

        // Gerar boletim de urna
        ResultActions result = mockMvc.perform(get("/boletim-urna/" + sessao.getId()))
                .andExpect(status().isOk());

        String boletim = result.andReturn().getResponse().getContentAsString();
        assertTrue(boletim.contains("Candidato 1"));
        assertTrue(boletim.contains("Candidato 2"));
        assertTrue(boletim.contains("Total de votos 2"));
    }
}