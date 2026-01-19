package com.example.ez.domain;

public enum Condicion {
    Regular('R'),
    Aprobado('A'),
    Inscripto('I'),
    Disponible('D'),
    NoDisponible('N');

    private final char letra;

    Condicion(char letra){
        this.letra = letra;
    }

    public char getLetra(){
        return this.letra;
    }

    public static Condicion fromLetra(char let) {
        for (Condicion e : values()) {
            if (e.letra == Character.toUpperCase(let)) {
                return e;
            }
        }
        return NoDisponible; // equivalente NULL
    }

    public static int getOrden (char letra){
        return fromLetra(letra).ordinal();
    }

}
