package com.example.ez.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoInscripcion {
    private char letraCarrera;
    private String nomberAlumno;
    private int regulares;
    private int aprobadas;
    private int inscriptas;
}
