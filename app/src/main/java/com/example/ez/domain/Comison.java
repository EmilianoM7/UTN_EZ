package com.example.ez.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comison {
    private int numero;
    private char variante;
    private int[][] horarios;
    private Nivel nivel;
    private Especialiad especialiad;
    private Turno turno;

    public String armarNombre(){
        return (nivel.getNumero() + "." + especialiad.getLetra() + "." + numero + "." + variante);
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

    public int[][] getHorarioMateria(int orden) {
        int[][] horarioMateria = new int[this.horarios.length][this.horarios[0].length];
        for (int i = 0; i < horarioMateria.length; i++) {
            for (int j = 0; j < horarioMateria[i].length ; j++) {
                //TODO revisar
                if(this.horarios[i][j] == orden){
                    horarioMateria[i][j]++;
                }
            }
        }
        return horarioMateria;
    }
}
