package com.example.ez.backend;

import android.content.Context;

import com.example.ez.MainActivity;
import com.example.ez.domain.Condicion;
import com.example.ez.domain.Especialiad;
import com.example.ez.domain.InfoInscripcion;
import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.repo.InscripcionCSV;
import com.example.ez.repo.MateriaCSV;

import java.util.ArrayList;
import java.util.List;

public class Backend {

    // * Realiza LLAMADOS a UseCase
    // * Resuelve ABMC directos
    // * Resuelve CALCULOS contextuales
    // * Resuelve LISTAS definidas y valores ENUM

    // LLAMADOS a UseCase

    public static String[] resumenCursada() {
        return ResumenCursada.execute();
    }

    public static ArrayList<int[]> armarHorarios(Materia[] inscriptas) {
        return ArmarHorarios.excecute(inscriptas);
    }

    // TODO mokeado
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

    // ABMC

    public static boolean crearInscripcionAlumno(Context context, char letraCarrera,String nombreAlumno) {
        return InscripcionCSV.crearPerfilAlumno(context,letraCarrera,nombreAlumno);
    }

    public static boolean guardarInscripcion(Context context, Inscripcion inscripcion, char letraCarrera, String nombreAlumno) {
        return InscripcionCSV.guardarInscripcion(context,inscripcion,letraCarrera,nombreAlumno);
    }

    public static boolean eliminarInscripcion(Context context, Materia mEliminar) {
        return InscripcionCSV.eliminarInscripcionMateria(context,
                mEliminar.getInscripcion(),
                MainActivity.getCarreraActual(),
                MainActivity.getNombreAlumno());
    }

    public static InfoInscripcion[] listarArchivosInscripcion(Context context){
        return InscripcionCSV.listarArchivosInscripcion(context);
    }

    public static boolean eliminarAlumno(Context context, char letraCarrera, String nombreAlumno){
        return InscripcionCSV.eliminarPerfilAlumno(context,letraCarrera,nombreAlumno);
    }

    public static Materia[] listarMateriasCSV(char letraCarrera){
        return MateriaCSV.cargarMaterias(letraCarrera);
    }

    // CALCULOS contextuales

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

    public static Materia[] obtenerSoloInscritas(){
        ArrayList<Materia> arrayInscritas = new ArrayList<>();
        Materia[] materias = MainActivity.getMateriasDatos();
        for (Materia m : materias) {
            if (m.esCondicionInscripta()){
                arrayInscritas.add(m);
            }
        }
        Materia[] vectorInscritas = new Materia[arrayInscritas.size()];
        for (int i = 0; i < vectorInscritas.length; i++) {
            vectorInscritas[i] = arrayInscritas.get(i);
        }
        return vectorInscritas;
    }

    // LISTAS definidas y valores ENUM

    public static String[][] getNombreLetraCarreras() {
        Especialiad[] especialiads = Especialiad.values();
        String[][] valores = new String[especialiads.length-2][2];
        for (int i = 0; i < valores.length; i++) {
            valores[i][0] = especialiads[i].getLetra() + "";
            valores[i][1] = especialiads[i].name();
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

    public static String[] getNombresCondiciones(boolean soloAcademicas) {
        Condicion[] condiciones = Condicion.values();
        // si pide solo academicas, va a omitir Disponible y NoDisponible
        String[] valores = new String[soloAcademicas ? (condiciones.length -2) : (condiciones.length)];

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