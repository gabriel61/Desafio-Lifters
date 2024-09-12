package com.lifters.eleicao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "votos")
@NoArgsConstructor
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleitor_id", nullable = false)
    private Eleitor eleitor;

    @ManyToOne
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    public Voto(Eleitor eleitor, Candidato candidato, Sessao sessao) {
        this.eleitor = eleitor;
        this.candidato = candidato;
        this.sessao = sessao;
    }
}