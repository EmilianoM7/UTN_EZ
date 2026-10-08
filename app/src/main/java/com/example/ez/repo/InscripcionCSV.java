package com.example.ez.repo;

import android.content.Context;

import com.example.ez.backend.Backend;
import com.example.ez.backend.Logger;
import com.example.ez.domain.Condicion;
import com.example.ez.domain.InfoInscripcion;
import com.example.ez.domain.Inscripcion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InscripcionCSV {
// esta clase convierte de String[] - Materia

    private static String rutaCarpetaRaw = "res/raw/";

    private static final Log log = LogFactory.getLog(InscripcionCSV.class);
    public static int
            indiceOrden,
            indiceCondicion,
            indiceNota,
            indiceComision,
            indiceFecha;
    private static String[] cabecera = {"ORDEN","CONDICION","NOTA","COMISON","FECHA"};

    public static boolean crearPerfilAlumno(Context context, char letraCarrera, String nombreAlumno){
        // verificar si existe el archivo
        if (CSVReader.existeArchivo(context, nombreArchivo(letraCarrera,nombreAlumno))){
            return false;
        }
        // crear la cabecera
        String[][] fila = {cabecera};
        Logger.logInscripcionCSV("crearInscripcionAlumno: " + nombreArchivo(letraCarrera,nombreAlumno));
        CSVReader.guardarCSVUsuario(context,nombreArchivo(letraCarrera,nombreAlumno), fila);
        return true;
    }

    //eliminar inscripcion de un alumno
    public static boolean eliminarPerfilAlumno(Context context, char letraCarrera, String nombreAlumno){
        return CSVReader.eliminarCSVAlumno(context,nombreArchivo(letraCarrera,nombreAlumno));
    }

    public static boolean guardarInscripcion(Context context, Inscripcion inscripcion, char letraCarrera, String nombreAlumno){
        Inscripcion[] inscripcions = cargarPerfilAlumno(context,letraCarrera,nombreAlumno);
        Logger.logInscripcionCSV("guardarInscripcion: " + inscripcions.length);
        // comprobar si existe la inscripcion a esa materia
        // si existe, modificarla
        if (inscripcions.length > 0){
            for (int i = 0; i < inscripcions.length; i++) {
                if (inscripcions[i].getOrdenMateria() == inscripcion.getOrdenMateria()){
                    Logger.logInscripcionCSV("> modificar: " + inscripcion.getOrdenMateria() );
                    inscripcions[i] = inscripcion;
                    return guardarInscripciones(context,inscripcions,letraCarrera,nombreAlumno);
                }
            }
        }
        // si no exite (o si hay 0 inscripciones), crearla y agregarla
        Logger.logInscripcionCSV("> agregar: " + inscripcion.getOrdenMateria() );
        return guardarInscripciones(context,argegarInscripcion(inscripcions,inscripcion),letraCarrera,nombreAlumno);
    }

    public static boolean eliminarInscripcionMateria(Context context, Inscripcion iEliminar, char letraCarrera, String nombreAlumno){
        Inscripcion[] inscripcions = cargarPerfilAlumno(context,letraCarrera,nombreAlumno);
        Inscripcion[] iNuevas = new Inscripcion[inscripcions.length - 1];

        int offset = 0;
        for (int i = 0; i < iNuevas.length; i++) {
            Logger.logInscripcionCSV(inscripcions[i].getOrdenMateria() + ":" + iEliminar.getOrdenMateria());
            if (inscripcions[i].getOrdenMateria() == iEliminar.getOrdenMateria()){
                offset = 1;
            }
            iNuevas[i] = inscripcions[i+offset];
        }
        return guardarInscripciones(context,iNuevas,letraCarrera,nombreAlumno);
    }

    // apendear una inscripcion a un vector
    private static Inscripcion[] argegarInscripcion(Inscripcion[] iActuales,Inscripcion inscripcion){
        Inscripcion[] iNuevas = new Inscripcion[iActuales.length + 1];

        Logger.logInscripcionCSV("iActuales: " + iActuales.length);
        Logger.logInscripcionCSV("iNuevas: " + iNuevas.length);

        // si hay solo una, retorna esa sola
        if (iNuevas.length == 1){
            iNuevas[0] = inscripcion;
            return iNuevas;
        }
        // si hay mas de una, insertar ordenada
        int offset = 0;
        boolean guardado = false;
        for (int i = 0; i < iActuales.length; i++) {
            // isercion ordenada
            iNuevas[i] = iActuales[i];
        }
        iNuevas[iActuales.length] = inscripcion;
        return iNuevas;
    }

    // guardar inscripicones en un perfil de alumno
    private static boolean guardarInscripciones(Context context, Inscripcion[] inscripcions, char letraCarrera, String nombreAlumno){
        // convertir Insc[] a String[][]
        String[][] filas = new String[inscripcions.length + 1][cabecera.length];
        filas[0] = cabecera;
        for (int i = 0; i < inscripcions.length; i++) {
            // ORDEN;CONDICION;NOTA;COMISON;FECHA
            filas[i+1] = new String[]{
                    inscripcions[i].getOrdenMateria() + "",
                    inscripcions[i].getLetraCondicion() + "",
                    inscripcions[i].getNota() + "",
                    inscripcions[i].getNombreComison(),
                    inscripcions[i].getAnoInscripcion() + ""
            };
        }
        // cargar inscripciones anteriores de archivo
        String rutaI = nombreArchivo(letraCarrera,nombreAlumno);
        Logger.logInscripcionCSV("filas: " + CSVReader.cargarCSVUsuario(context,rutaI).length + " > " + filas.length);
        return CSVReader.guardarCSVUsuario(context,rutaI,filas);
    }

    public static Inscripcion[] cargarPerfilAlumno(Context context, char letraCarrera, String nombreAlumno){

        String nombre = nombreArchivo(letraCarrera,nombreAlumno);
        String[][] filas = CSVReader.cargarCSVUsuario(context,nombre);

        Logger.logInscripcionCSV("filas: " + filas.length + "x" + filas[0].length);

        // si tiene solo la cabecera, devolver vacio
        if (filas.length == 1){
            return new Inscripcion[0];
        }

        // iniciar materias y descontar fila cabecera
        Inscripcion[] inscripcions = new Inscripcion[filas.length - 1];

        // acomodar indices de columnas (en 3ra fila)
        setIndces(filas[0]);

        // contar desde 1, hasta la ultima fila
        for (int i = 1; i < filas.length; i++) {
            //crear cada Inscripcion
            Inscripcion ins = new Inscripcion(
                    Integer.parseInt(filas[i][indiceOrden]),
                    Integer.parseInt(filas[i][indiceFecha]),
                    Integer.parseInt(filas[i][indiceNota]),
                    filas[i][indiceComision],
                    Condicion.fromLetra(filas[i][indiceCondicion].charAt(0))
            );

            // asignar Inscripcion a cada indice
            inscripcions[i-1] = ins;
        }

        //Logger.logInscripcionCSV("inscripcions: " + inscripcions.length);

        //retornar
        return inscripcions;
    }

    public static InfoInscripcion[] listarArchivosInscripcion(Context context){
        String[] nombresArchivo = CSVReader.listarArchivosUsuario(context);
        List<InfoInscripcion> infos = new ArrayList<>();

        Logger.logInscripcionCSV("archivos: " + nombresArchivo.length);

        // armar el set de letras par ausar el contains()
        Set<Character> letras = new HashSet<>();
        for (char letra : Backend.getLetrarCarreras()) {
            letras.add(letra);
        }
        // recorrer el vector de archivos y armar el vector de InfoInscripcion
        for (int i = 0; i < nombresArchivo.length; i++) {
            Logger.logInscripcionCSV(i + ": " + nombresArchivo[i]);
            Logger.logInscripcionCSV("check 1");
            char letraArcuivo = nombresArchivo[i].charAt(0);
            if(letras.contains(letraArcuivo)){
                Logger.logInscripcionCSV("check 2");

                String nombreAlumnoArchivo = nombresArchivo[i].split("\\.")[1];
                Logger.logInscripcionCSV("check 3");

                Inscripcion[] mats = cargarPerfilAlumno(context,letraArcuivo,nombreAlumnoArchivo);
                int regulares = 0, aprobadas = 0, inscriptas = 0;
                for (Inscripcion ins : mats) {
                    if (ins.getLetraCondicion() == 'R'){regulares++;}
                    else if (ins.getLetraCondicion() == 'A'){aprobadas++;}
                    else if (ins.getLetraCondicion() == 'I'){inscriptas++;}
                }
                infos.add(new InfoInscripcion(
                        letraArcuivo,nombreAlumnoArchivo,
                        regulares,aprobadas,inscriptas));
            }

        }
        // devolver en forma de vector
        InfoInscripcion[] vectorInfos = new InfoInscripcion[infos.size()];
        for (int i = 0; i < infos.size(); i++) {
            vectorInfos[i] = infos.get(i);
        }
        Logger.logInscripcionCSV("InfosCorrectas: " + vectorInfos.length);
        return vectorInfos;
    }

    public static String nombreArchivo(char letraCarrera, String nombreAlumno){
        return "" + letraCarrera + "." + nombreAlumno + ".csv";
    }

    private static void setIndces(String[] primeraLinea){
        for (int i = 0; i < primeraLinea.length; i++) {
            switch (primeraLinea[i]){
                case "ORDEN": indiceOrden = i; break;
                case "CONDICION": indiceCondicion = i; break;
                case "NOTA": indiceNota = i; break;
                case "COMISON": indiceComision = i; break;
                case "FECHA": indiceFecha = i; break;
            }
        }
    }

}
