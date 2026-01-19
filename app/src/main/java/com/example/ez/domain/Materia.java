package com.example.ez.domain;

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
    private String sigla;
    private String modalidad;
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

    public int getNumeroNivel(){
        return nivel.getNumero();
    }

    public char getLetraCodicion(){
        if (inscripcion != null){
            return inscripcion.getLetraCondicion();
        }
        return cursable ? 'D' : 'N';
    }

    public String getNombreCondicion(){
        return Condicion.fromLetra(getLetraCodicion()).name();
    }

    public int getOrdenCondicionActual (){
        if (inscripcion != null){
            return inscripcion.getOrdenCondicion(getLetraCodicion());
        }
        return cursable ? Condicion.Disponible.ordinal() : Condicion.NoDisponible.ordinal();
    }

    public String getNombreComision(){
        if (inscripcion != null){
            return inscripcion.getNombreComison();
        }
        return "noComision";
    }

    public int getAnoInscripcion(){
        if (inscripcion != null){
            return inscripcion.getAnoInscripcion();
        }
        return 0;
    }

    public int getNotaInscripcion() {
        if (inscripcion != null){
            return inscripcion.getNota();
        }
        return 0;
    }

    public String getNombreEspecialidad(){
        return especialiad.name();
    }

    public char contieneCorrelativa(int orden){
        if (this.correlativasReg[0] != 0){
            Set<Integer> setRegulares = new HashSet<>();
            for (int valor : this.correlativasReg) {
                setRegulares.add(valor);
            }
            if (setRegulares.contains(orden)){
                return 'R';
            }
        }
        if (this.correlativasAp[0] != 0){
            Set<Integer> setAprobadas = new HashSet<>();
            for (int valor : this.correlativasAp) {
                setAprobadas.add(valor);
            }
            if (setAprobadas.contains(orden)){
                return 'A';
            }
        }
        return 'N';
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
