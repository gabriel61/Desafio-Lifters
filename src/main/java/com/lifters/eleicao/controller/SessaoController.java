package com.lifters.eleicao.controller;

import com.lifters.eleicao.model.Sessao;
import com.lifters.eleicao.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @PostMapping("/abrir")
    public ResponseEntity<Sessao> abrirSessao(@RequestBody Sessao sessao) {
        Sessao sessaoAberta = sessaoService.abrirSessao(sessao);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoAberta);
    }

    @PatchMapping("/{id}/fechar")
    public ResponseEntity<Sessao> fecharSessao(@PathVariable Long id) {
        Sessao sessaoFechada = sessaoService.fecharSessao(id);
        return ResponseEntity.ok(sessaoFechada);
    }
}
