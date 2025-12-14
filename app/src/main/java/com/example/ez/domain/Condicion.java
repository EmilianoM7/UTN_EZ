package com.example.ez.domain;

public enum Condicion {
    Regular('R'),
    Aprobado('A'),
    Inscripto('I'),
    Disponible('D'),
    NoDisponible('N');

    public final char letra;

    Condicion(char letra){
        this.letra = letra;
    }

    public char getLetra(){
        return this.letra;
    }
}
