package com.lifters.eleicao.model;

import jakarta.persistence.*;
        import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessoes")
@NoArgsConstructor
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    @Column
    private LocalDateTime dataFechamento;

    @Column(nullable = false)
    private boolean aberta;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    public Sessao(LocalDateTime dataAbertura, LocalDateTime dataFechamento, boolean aberta, Cargo cargo) {
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.aberta = aberta;
        this.cargo = cargo;
    }
}