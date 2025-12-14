package com.example.ez.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Carrera {
    private String nombre;
    private String descripcion;
    private int horas;
    private Materia[] materias;

    public int getCantidadMaterias(){
        return this.materias.length;
    }
}
