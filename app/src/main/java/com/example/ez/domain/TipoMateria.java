package com.example.ez.domain;

public enum TipoMateria {
    Principal('P'),
    Electiva('E'),
    NoTipo('X');

    public final char letra;

    TipoMateria(char letra){
        this.letra = letra;
    }

    public char getLetra(){
        return this.letra;
    }
    
    public static TipoMateria fromLetra(char letraBuscado) {
        for (TipoMateria e : values()) {
            if (e.letra == Character.toUpperCase(letraBuscado)) {
                return e;
            }
        }
        return NoTipo; // equivalente NULL
    }
}
