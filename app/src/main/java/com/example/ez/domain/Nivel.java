package com.example.ez.domain;

public enum Nivel {
    Primero(1),
    Segundo(2),
    Tercero(3),
    Cuarto(4),
    Quinto(5),
    NoNivel(0);

    private final int numero;

    Nivel(int numero){
        this.numero = numero;
    }

    public int getNumero(){
        return this.numero;
    }

    public static Nivel fromNumero(int num) {
        for (Nivel n : values()) {
            if (n.numero == num) {
                return n;
            }
        }
        return NoNivel; // equivalente NULL
    }
}
