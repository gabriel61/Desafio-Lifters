package com.lifters.eleicao.controller;

import com.lifters.eleicao.model.Voto;
import com.lifters.eleicao.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eleitores/{eleitorId}/votar")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping
    public ResponseEntity<Voto> votar(
            @PathVariable Long eleitorId,
            @RequestParam Long candidatoId,
            @RequestParam Long sessaoId) {
        Voto voto = votoService.votar(eleitorId, candidatoId, sessaoId);
        return ResponseEntity.ok(voto);
    }
}