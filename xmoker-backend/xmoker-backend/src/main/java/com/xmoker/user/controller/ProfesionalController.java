package com.xmoker.user.controller;

import com.xmoker.user.entity.ProfesionalDatos;
import com.xmoker.user.repository.ProfesionalDatosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profesionales")
@CrossOrigin(origins = "*")
public class ProfesionalController {

    @Autowired
    private ProfesionalDatosRepository profesionalDatosRepository;

    @PostMapping
    public ProfesionalDatos guardarDatosProfesional(@RequestBody ProfesionalDatos datos) {
        return profesionalDatosRepository.save(datos);
    }
}
