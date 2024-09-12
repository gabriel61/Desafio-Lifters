package com.lifters.eleicao.controller;

import com.lifters.eleicao.model.Eleitor;
import com.lifters.eleicao.service.EleitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eleitores")
public class EleitorController {

    @Autowired
    private EleitorService eleitorService;

    @GetMapping
    public ResponseEntity<List<Eleitor>> listarTodos() {
        return ResponseEntity.ok(eleitorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Eleitor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(eleitorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Eleitor> criar(@RequestBody Eleitor eleitor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eleitorService.salvar(eleitor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Eleitor> atualizar(@PathVariable Long id, @RequestBody Eleitor eleitor) {
        return ResponseEntity.ok(eleitorService.atualizar(id, eleitor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eleitorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}