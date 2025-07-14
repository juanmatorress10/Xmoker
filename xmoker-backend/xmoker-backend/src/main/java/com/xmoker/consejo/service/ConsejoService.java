package com.xmoker.consejo.service;

import com.xmoker.consejo.entity.ConsejoDiario;
import com.xmoker.consejo.repository.ConsejoDiarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ConsejoService {

    private final ConsejoDiarioRepository consejoRepo;
    private final Random random = new Random();

    public ConsejoService(ConsejoDiarioRepository consejoRepo) {
        this.consejoRepo = consejoRepo;
    }

    public ConsejoDiario obtenerPorDia(int dia) {
        return consejoRepo.findByDiaSugerido(dia)
                .orElseGet(this::obtenerAleatorio);
    }

    public ConsejoDiario obtenerAleatorio() {
        List<ConsejoDiario> todos = consejoRepo.findAll();
        return todos.isEmpty() ? null : todos.get(random.nextInt(todos.size()));
    }

    public List<ConsejoDiario> obtenerPorCategoria(String categoria) {
        return consejoRepo.findByCategoria(categoria.toUpperCase());
    }
}
