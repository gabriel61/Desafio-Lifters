package com.lifters.eleicao.service;

import com.lifters.eleicao.model.Sessao;
import com.lifters.eleicao.model.Voto;
import com.lifters.eleicao.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoletimService {

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private VotoRepository votoRepository;

    public String gerarBoletim(Long sessaoId) {
        Sessao sessao = sessaoService.buscarPorId(sessaoId);

        if (sessao.isAberta()) {
            throw new IllegalStateException("A sessão ainda está aberta.");
        }

        List<Voto> votos = votoRepository.findBySessaoId(sessaoId);
        if (votos.isEmpty()) {
            return formatBoletim(sessao, Map.of(), 0, null);
        }

        Map<String, Long> resultado = votos.stream()
                .collect(Collectors.groupingBy(voto -> voto.getCandidato().getNome(), Collectors.counting()));

        long totalVotos = resultado.values().stream().mapToLong(Long::longValue).sum();

        String vencedor = resultado.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return formatBoletim(sessao, resultado, totalVotos, vencedor);
    }

    private String formatBoletim(Sessao sessao, Map<String, Long> resultado, long totalVotos, String vencedor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        sb.append("----------------------------------------\n");
        sb.append("Data relatório: ").append(LocalDateTime.now().format(formatter)).append("\n");
        sb.append("Cargo: ").append(sessao.getCargo()).append("\n");
        sb.append("Candidatos Votos\n");

        resultado.forEach((nome, votos) -> sb.append(nome).append(" ").append(votos).append("\n"));

        sb.append("Total de votos ").append(totalVotos).append("\n");

        if (totalVotos > 1) {
            sb.append("Vencedor: ").append(vencedor).append("\n");
        }

        sb.append("----------------------------------------\n");

        return sb.toString();
    }
}
