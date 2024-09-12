package com.lifters.eleicao.controller;

import com.lifters.eleicao.service.BoletimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boletim-urna")
public class BoletimController {

    @Autowired
    private BoletimService boletimService;

    @GetMapping("/{idSessao}")
    public String gerarBoletim(@PathVariable Long idSessao) {
        return boletimService.gerarBoletim(idSessao);
    }
}
