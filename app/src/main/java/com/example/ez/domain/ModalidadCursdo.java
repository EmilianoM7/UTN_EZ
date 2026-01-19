package com.example.ez.domain;

public enum ModalidadCursdo {
    Anual('A'),
    C1('1'),
    C2('2'),
    C1_C2('C');

    private final char letra;

    ModalidadCursdo(char letra) {
        this.letra = letra;
    }

    public char getLetra() {
        return letra;
    }
    public static ModalidadCursdo fromLetra(char let) {
        for (ModalidadCursdo e : values()) {
            if (e.letra == Character.toUpperCase(let)) {
                return e;
            }
        }
        return Anual; // equivalente NULL
    }
}
