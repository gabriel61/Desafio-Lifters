package com.lifters.eleicao.controller;

import com.lifters.eleicao.model.Candidato;
import com.lifters.eleicao.service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @GetMapping
    public ResponseEntity<List<Candidato>> listarTodos() {
        return ResponseEntity.ok(candidatoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidato> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(candidatoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Candidato> criar(@RequestBody Candidato candidato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatoService.salvar(candidato));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidato> atualizar(@PathVariable Long id, @RequestBody Candidato candidato) {
        return ResponseEntity.ok(candidatoService.atualizar(id, candidato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        candidatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}