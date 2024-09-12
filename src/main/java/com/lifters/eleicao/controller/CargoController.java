package com.lifters.eleicao.controller;

import com.lifters.eleicao.model.Cargo;
import com.lifters.eleicao.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<Cargo>> listarTodos() {
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cargoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cargo> criar(@RequestBody Cargo cargo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.salvar(cargo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> atualizar(@PathVariable Long id, @RequestBody Cargo cargo) {
        return ResponseEntity.ok(cargoService.atualizar(id, cargo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cargoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}