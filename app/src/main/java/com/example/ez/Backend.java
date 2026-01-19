package com.example.ez;

import android.content.Context;

import com.example.ez.domain.Comison;
import com.example.ez.domain.Condicion;
import com.example.ez.domain.Especialiad;
import com.example.ez.domain.InfoInscripcion;
import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.domain.Nivel;
import com.example.ez.repo.InscripcionCSV;
import com.example.ez.repo.MateriaCSV;
import com.example.ez.useCase.ListarMaterias;
import com.example.ez.useCase.ResumenCursada;

import java.util.ArrayList;
import java.util.List;

public class Backend {

    // ENV

    public static String[] resumenCursada() {
        return ResumenCursada.execute();
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

    public static Materia[] materiasQueLibera(int ordenMateria, char Condicion){
        Materia[] materiasCarrera = MainActivity.getMateriasDatos();
        List<Materia> matLiberadas = new ArrayList<>();
        for (Materia m : materiasCarrera){
            if (m.contieneCorrelativa(ordenMateria) == Condicion){
                matLiberadas.add(m);
            }
        }
        return matLiberadas.toArray(new Materia[0]);
    }

    // CSV

    public static String[] infoCarrera(char letraCarrera) {
        // [letraCarrera, nombreCarrera, titulo, tituloMedio, horasCarrera, materiasCarrera, descripcionCarrera]
        String[] info = {
                "" + letraCarrera,
                "Ingeniería en Sistemas de Información",
                "Ingeniero en Sistemas de Información",
                "Analista Desarrollador Universitario de Sistemas de Información",
                "3992",
                "56",
                "Forma ingenieros.\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\n...\nFIN"
        };
        return info;
    }

    public static void crearInscripcionAlumno(Context context, char letraCarrera) {
        InscripcionCSV.crearInscripcionAlumno(context,letraCarrera);
    }

    public static boolean guardarInscripcion(Context context, Inscripcion inscripcion, char letraCarrera, int idAlumno) {
        return InscripcionCSV.guardarInscripcion(context,inscripcion,letraCarrera,idAlumno);
    }

    public static boolean eliminarInscripcion(Context context, Inscripcion iEliminar, char letraCarrera, int idAlumno) {
        return InscripcionCSV.eliminarInscripcion(context, iEliminar, letraCarrera, idAlumno);
    }

    public static InfoInscripcion[] listarArchivosInscripcion(Context context){
        return InscripcionCSV.listarArchivosInscripcion(context);
    }

    public static Materia[] listarMateriasCSV(char letraCarrera){
        return MateriaCSV.cargarMaterias(letraCarrera);
    }

    public static String[] buscarComisionesMateria(Materia mat){
        int cantidad = 5;
        int niv = mat.getNumeroNivel();
        String[] coms = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            coms[i] = mat.getNumeroNivel() + mat.getEspecialiad().getLetra() + (i+1) + "";
        }
        return coms;
    }

    // HARDCODEADO

    public static String[][] getNombreLetraCarreras() {
        Especialiad[] especialiads = Especialiad.values();
        String[][] valores = new String[especialiads.length-2][2];
        for (int i = 0; i < valores.length; i++) {
            valores[i][0] = especialiads[i].name();
            valores[i][1] = especialiads[i].getLetra() + "";
        }
        return valores;
    }

    public static char[] getLetrarCarreras() {
        Especialiad[] especialiads = Especialiad.values();
        char[] valores = new char[especialiads.length-2];
        for (int i = 0; i < valores.length; i++) {
            valores[i] = especialiads[i].getLetra();
        }
        return valores;
    }

    public static String[] getNombresNiveles() {
        Nivel[] nivels = Nivel.values();
        String[] valores = new String[nivels.length-1];

        for (int i = 0; i < valores.length; i++) {
            valores[i] = nivels[i].name();
        }
        return valores;
    }

    public static String[] getNombresCondiciones() {
        Condicion[] condiciones = Condicion.values();
        String[] valores = new String[condiciones.length];

        for (int i = 0; i < valores.length; i++) {
            valores[i] = condiciones[i].name();
        }
        return valores;
    }

    public static Integer[] getNotas() {
        return new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }

    public static String[] getNotasString(){
        Integer[] notas = getNotas();
        String[] notasString = new String[notas.length];
        for (int i = 0; i < notasString.length; i++) {
            notasString[i] = notas[i].toString();
        }
        return notasString;
    }
}