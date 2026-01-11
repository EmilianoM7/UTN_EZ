package com.example.ez.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inscripcion {
    private int ordenMateria;
    private Date fecha;
    private int nota;
    private String nombreComison;
    private Condicion condicion;

    public boolean esOrdenMateria(int orden){
        return this.ordenMateria == orden;
    }

    public boolean esRegular(){
        return this.condicion == Condicion.Regular;
    }

    public boolean esAprobado(){
        return this.condicion == Condicion.Aprobado;
    }

    public char getLetraCondicion(){
        return condicion.getLetra();
    }

    public String str(){
        return ordenMateria + "." + nota + "." + condicion.name();
    }
}
