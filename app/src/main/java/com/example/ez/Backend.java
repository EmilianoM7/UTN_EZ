package com.example.ez;

import android.content.Context;

import com.example.ez.useCase.ListarMaterias;

import java.util.ArrayList;
import java.util.List;

public class Backend {

    String[][] listaMaterias = {
            // nivel, orden, nombreMateria, sigla, condicion, nota
            {"1", "1", "Análisis Matemático I", "AM1", "A", "8"},
            {"1", "6", "Comunicación Multimedial en el Desarrollo de Sistemas de Informacion", "CMD", "R", ""},

            {"2", "7", "Análisis Matemático II", "AM2", "A", "9"},
            {"2", "8", "Sistemas Operativos", "SOP", "I", ""},

            {"3", "12", "Base de Datos", "BDD", "D", ""},
            {"3", "15", "Diseño de Sistemas", "DIS", "D", ""},

            {"4", "16", "Gestión de Datos", "GDD", "N", ""},
            {"4", "18", "Simulación", "SIM", "N", ""},

            {"5", "19", "Proyecto Final", "PFI", "N", ""}
    };


    // si hay usuario retorna sesion, sino retorna null
    public static String[] dataInicial() {
        // [nombreUsuario, carreraElegida, añoIngreso]
        String[] info = new String[]{
                "EMI",
                "0",
                "2023"
        };
        return info;
    }

    public static String[] infoCarrera(char letraCarrera) {
        // [letraCarrera, nombreCarrera, titulo, tituloMedio, horasCarrera, materiasCarrera, descripcionCarrera]
        String[] info = {
            String.valueOf(letraCarrera),
                    "Ingeniería en Sistemas de Información",
                    "Ingeniero en Sistemas de Información",
                    "Analista Desarrollador Universitario de Sistemas de Información",
                    "3992",
                    "56",
                    "Forma ingenieros.\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\nFIN"
        };
        return info;
    }

    public static String[] resumenCursada() {
        // [A, R, I, D, N, porcentaje, promedio, puntos, puntosNecesarios]
        String[] resumen = {"15", "8", "3", "12", "18", "26.8", "7.8", "10", "20"};
        return resumen;
    }

    public static String[] infoMateria(int orden) {
        // [orden, sigla, nombreMateria, descripcionMateria, programaMateria]
        String[] info = {
            "2",
            "AGA",
            "Algebra",
            "Esta es ladescripcion de la materia AGA",
            "Programa: \nUnidad 1... \nUnidad 2... \nUnidad 3... \nUnidad 4... \nUnidad 5... \nUnidad 6... "
        };
        return info;
    }

    public static int[][] simularHorario() {
        int[][] horario = new int[6][17];
        // Simular algunas materias en distintos horarios
        horario[0][2] = 1; horario[0][3] = 1; // Lunes modulos 3-4
        horario[1][5] = 1; horario[1][6] = 1; // Martes modulos 6-7
        horario[2][8] = 2; horario[2][9] = 2; // Miércoles modulos 9-10 (2 materias)
        horario[3][1] = 1; horario[3][2] = 1; // Jueves modulos 2-3
        horario[4][10] = 1; // Viernes modulo 11
        return horario;
    }

    public static String[][] listarMaterias(char letraCarrera) {
        //return lista;
        return ListarMaterias.execute(letraCarrera);
    }

    public static String[][] listarInscripciones(char letraCarrera) {
        // [[nivel, orden, nombreMateria, sigla, condicion, nota],[],...]

        String[][] mat = listarMaterias(letraCarrera);
        List<String[]> insc = new ArrayList<>();
        for (String[] m : mat){
            if( m[4].equals("A") | m[4].equals("R") | m[4].equals("I") ){
                insc.add(m);
            }
        }

        String[][] ret = new String[insc.size()][6];
        for(int i = 0; i < ret.length; i++){
            ret[i] = insc.get(i);
        }
        return ret;
    }

    public static void registrarInscripcion(int orden, char condicion, int nota, String comision) {
        // Simula el registro
        System.out.println("Registrado: orden=" + orden + " condicion=" + condicion +
                " nota=" + nota + " comision=" + comision);
    }

    public static String[][] getCarreras() {
        String[][] carreras ={
                {"K","Sistemas"},
                {"C","Civil"},
                {"V", "Quimica"},
                {"D","Industrial"}
        };
        return carreras;
    }

    public static String[] getNiveles(char ltraCarrera) {
        String[] niv = {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto"};
        return niv;
    }

    public static String[] getCondiciones() {
        String[] cond = {"Regular", "Aprobado", "Inscripto", "Disponible", "No Disponible"};
        return cond;
    }

    public static Integer[] getNotas() {
        Integer[] notas = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return notas;
    }

    public static String[] getComisiones(String sigla) {
        String[] coms = {"1K1", "2K1", "3K1", "4K1", "5K1", "1K2", "2K2", "3K2"};
        return coms;
    }
}