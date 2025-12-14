package com.example.ez.domain;

public enum Especialiad {
    Civil('C'),
    Electrica('Q'),
    Electronica('R'),
    Industrial('D'),
    Mecanica('S'),
    Metalurgica('T'),
    Quimica('V'),
    Sistemas('K'),
    Basica('B'),
    NoEspecialidad('X');

    private final char letra;

    Especialiad(char letra){
        this.letra = letra;
    }

    public char getLetra(){
        return this.letra;
    }

    public static Especialiad fromLetra(char let) {
        for (Especialiad e : values()) {
            if (e.letra == let) {
                return e;
            }
        }
        return NoEspecialidad; // equivalente NULL
    }

}
