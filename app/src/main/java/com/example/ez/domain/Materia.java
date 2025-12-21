package com.example.ez.domain;

import com.example.ez.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Materia {

    private int orden;
    private String nombre;
    private String descripcion;
    private String programa;
    private String sigla;
    private boolean electiva;
    private boolean cursable;
    private int[] correlativasReg;
    private int[] correlativasAp;
    private int puntos;
    private Inscripcion inscripcion;
    private Nivel nivel;
    private Especialiad especialiad;


    public boolean esOrdenMteria(int ord){
        return this.orden == ord;
    }

    public boolean esCursable(int[] reg, int[] apr){
        // juntar reg y apr
        int[] alMenosRegular = juntarVectores(reg,apr);

        // comprobar
        return contieneTodos(alMenosRegular, this.correlativasReg)
                && contieneTodos(apr, this.correlativasAp);
    }

    private boolean contieneTodos(int[] actuales, int[] necesarias) {

        if (necesarias[0] != 0){
            Set<Integer> conjunto = new HashSet<>();
            for (int valor : actuales) {
                conjunto.add(valor);
            }

            for (int elemento : necesarias) {
                if (!conjunto.contains(elemento)) {
                    return false;
                }
            }
        }

        return true;
    }

    int[] juntarVectores(int[] reg, int[] apr){
        int[] juntado = new int[reg.length + apr.length];
        for (int i = 0; i < reg.length; i++) {
            juntado[i] = reg[i];
        }
        for (int i = 0; i < apr.length; i++) {
            juntado[i + reg.length] = apr[i];
        }
        return juntado;
    }

    public String str(){
        return nivel.getNumero() + "." + orden + "." + especialiad.getLetra() + "." + sigla;
    }
}
