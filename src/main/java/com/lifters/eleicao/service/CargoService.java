package com.lifters.eleicao.service;

import com.lifters.eleicao.model.Cargo;
import com.lifters.eleicao.repository.CargoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> listarTodos() {
        return cargoRepository.findAll();
    }

    public Cargo buscarPorId(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado"));
    }

    public Cargo salvar(Cargo cargo) {
        if (cargoRepository.existsByNome(cargo.getNome())) {
            throw new IllegalArgumentException("Já existe um cargo com este nome");
        }
        return cargoRepository.save(cargo);
    }

    public Cargo atualizar(Long id, Cargo cargo) {
        Cargo cargoExistente = buscarPorId(id);
        cargoExistente.setNome(cargo.getNome());
        return cargoRepository.save(cargoExistente);
    }

    public void deletar(Long id) {
        Cargo cargo = buscarPorId(id);
        cargoRepository.delete(cargo);
    }
}