package com.example.ez.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inscripcion {
    private Date fecha;
    private int nota;
    private Materia materia;
    private Comison comison;
    private Condicion condicion;

    public boolean esOrdenMateria(int orden){
        return this.materia.esOrdenMteria(orden);
    }

    public int getOrdenMateria(){
        return this.materia.getOrden();
    }

    public boolean esRegular(){
        return this.condicion == Condicion.Regular;
    }

    public boolean esAprobado(){
        return this.condicion == Condicion.Aprobado;
    }

}
