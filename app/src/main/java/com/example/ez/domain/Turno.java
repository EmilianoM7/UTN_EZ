package com.example.ez.domain;

public enum Turno {
    Mañana('M'),
    Tarde('T'),
    Noche('N');

    private final char letra;

    Turno(char letra){
        this.letra = letra;
    }

    public char getLetra(){
        return this.letra;
    }
}
