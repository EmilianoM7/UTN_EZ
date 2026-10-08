package com.example.ez.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comison {
    private int numero;
    private char variante = '?';
    private Nivel nivel;
    private Especialiad especialiad;
    private Turno turno;

    public String armarNombre(){
        return (nivel.getNumero() + "." + especialiad.getLetra() + "." + numero
                + (variante != '?' ? "." + variante : ""));
    }

    public boolean esNumeroComision(int num){
        return (this.numero == num);
    }

    public boolean esEspecialidadComision(char esp){
        return (this.especialiad.getLetra() == esp);
    }

    public boolean esnivelComision(int numero){
        return (this.nivel.getNumero() == numero);
    }

}
