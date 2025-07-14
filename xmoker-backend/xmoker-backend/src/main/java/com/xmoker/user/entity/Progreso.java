package com.xmoker.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@Entity

public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(mappedBy = "progreso")
    @JsonBackReference
    private Usuario usuario;

    private int diasSinFumar;
    private int cantidadCigarrillosEvitados;
    private float dineroAhorrado;
    private String beneficiosFisicos;
    private int rachaActual;
    private int rachaMaxima;
    private int horasVidaRecuperadas;
    private String reduccionRiesgoEnfermedades;
    private String capacidadPulmonarRecuperada;
    private String frecuenciaCardiacaMejorada;
    private String saturacionOxigenoMejorada;
    private String mejoraSistemaInmune;
    private String CO2NoEmitido;
    private String reduccionColillasContaminantes;
    private String reduccionConsumoPlastico;
    private String reduccionExposicionFumadoresPasivos;
    private String proteccionMenoresMascotas;
    private String nivelesEstresReducidos;
    private String mejoraCalidadSueño;
    private String estadoAnimicoUsuario;
    private String usoChatbotApoyo;
    private String participacionForo;
    private String usoProfesionalesSalud;
    private String metodosAlternativosUtilizados;
    private String reflexionesPersonalesProgreso;
    private String posiblesGatillosRecaidaEstrategias;
    private String registroRecaidas;
    private int retosAnsiedadCompletados;

    @OneToMany(mappedBy = "progreso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference
    private List<Recaida> recaidas;
}
