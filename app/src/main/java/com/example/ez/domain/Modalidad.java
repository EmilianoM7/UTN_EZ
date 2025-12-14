package com.example.ez.domain;

public enum Modalidad {
    Anual('A'),
    C1('1'),
    C2('2'),
    Intensivo('I');

    public final char letra;

    Modalidad(char letra){
        this.letra = letra;
    }

    public char getLetra(){
        return this.letra;
    }

}
