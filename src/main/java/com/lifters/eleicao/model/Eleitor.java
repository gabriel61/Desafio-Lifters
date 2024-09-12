package com.lifters.eleicao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "eleitores")
@NoArgsConstructor
public class Eleitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    public Eleitor(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
}