package com.example.ez;

import com.example.ez.domain.Condicion;
import com.example.ez.domain.Especialiad;
import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.domain.Nivel;
import com.example.ez.useCase.ListarMaterias;
import com.example.ez.useCase.ResumenCursada;

import java.util.ArrayList;
import java.util.List;

public class Backend {

    private static int[] regulares;
    private static int[] aprobadas;
    Materia[] materias;
    Inscripcion[] inscripciones;

    // si hay usuario retorna sesion, sino retorna null
    // todo dataInicial
    public static String[] dataInicial() {
        // [nombreUsuario, carreraElegida, añoIngreso]
        String[] info = new String[]{
                "EMI",
                "0",
                "2023"
        };
        return info;
    }

    // todo infoCarrera
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

    public static String[] resumenCursada(char carrera, int alumno) {
        return ResumenCursada.execute(carrera,alumno);
    }

    // todo infoMateria
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

    // todo simularHorario
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

    public static Materia[] listarMaterias(char letraCarrera, int alumno) {
        return ListarMaterias.execute(letraCarrera,alumno,true);
    }

    public static Materia[] listarInscripciones(char letraCarrera,int alumno) {
        return ListarMaterias.execute(letraCarrera,alumno,false);
    }

    // todo registrarInscripcion
    public static void registrarInscripcion(int orden, char condicion, int nota, String comision) {
        // Simula el registro
        Logger.logBackend("Registrado: orden=" + orden + " condicion=" + condicion +
                " nota=" + nota + " comision=" + comision);
    }

    public static String[][] getCarreras() {
        Especialiad[] especialiads = Especialiad.values();
        String[][] valores = new String[especialiads.length-2][2];

        for (int i = 0; i < valores.length; i++) {
            valores[i][0] = "" + especialiads[i].getLetra();
            valores[i][1] = especialiads[i].name();
        }
        return valores;
    }

    public static String[] getNiveles(char ltraCarrera) {
        Nivel[] nivels = Nivel.values();
        String[] valores = new String[nivels.length-1];

        for (int i = 0; i < valores.length; i++) {
            valores[i] = nivels[i].name();
        }
        return valores;
    }

    public static String[] getCondiciones() {
        Condicion[] condiciones = Condicion.values();
        String[] valores = new String[condiciones.length];

        for (int i = 0; i < valores.length; i++) {
            valores[i] = condiciones[i].name();
        }
        return valores;
    }

    public static Integer[] getNotas() {
        Integer[] notas = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return notas;
    }

    //todo getComisiones
    public static String[] getComisiones(String sigla) {
        String[] coms = {"1K1", "2K1", "3K1", "4K1", "5K1", "1K2", "2K2", "3K2"};
        return coms;
    }

}