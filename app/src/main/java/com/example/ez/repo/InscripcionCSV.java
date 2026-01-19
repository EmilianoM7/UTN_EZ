package com.example.ez.repo;

import android.content.Context;

import com.example.ez.Backend;
import com.example.ez.CSVReader;
import com.example.ez.Logger;
import com.example.ez.domain.Condicion;
import com.example.ez.domain.Especialiad;
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
            indiceCarrera,
            indiceAlumno,
            indiceOrden,
            indiceCondicion,
            indiceNota,
            indiceComision,
            indiceFecha;


    public static void crearInscripcionAlumno(Context context,char letraCarrera){
        int idAlumno = 0;
        String[][] fila = {{"ORDEN","CONDICION","NOTA","COMISON","FECHA"}};
        while (CSVReader.existeArchivo(nombreArchivo(letraCarrera,idAlumno))){
            idAlumno++;
        }
        Logger.logInscripcionCSV("crearInscripcionAlumno: " + nombreArchivo(letraCarrera,idAlumno));
        CSVReader.guardarCSVUsuario(context,nombreArchivo(letraCarrera,idAlumno), fila);
    }

    public static boolean guardarInscripcion(Context context, Inscripcion inscripcion, char letraCarrera, int idAlumno){
        Inscripcion[] inscripcions = cargarInscripciones(context,letraCarrera,idAlumno);
        Logger.logInscripcionCSV("guardarInscripcion: ins: " + inscripcions.length);
        // comprobar si existe
        // si existe, modificarla
        if (inscripcions.length > 0){
            for (int i = 0; i < inscripcions.length; i++) {
                if (inscripcions[i].getOrdenMateria() == inscripcion.getOrdenMateria()){
                    inscripcions[i] = inscripcion;
                    return guardarInscripciones(context,inscripcions,letraCarrera,idAlumno);
                }
            }
        }
        // sino exite, crearla y agregarla
        return guardarInscripciones(context,argegarInscripcion(inscripcions,inscripcion),letraCarrera,idAlumno);
    }

    public static boolean eliminarInscripcion(Context context, Inscripcion iEliminar, char letraCarrera, int idAlumno){
        Inscripcion[] inscripcions = cargarInscripciones(context,letraCarrera,idAlumno);
        Inscripcion[] iNuevas = new Inscripcion[inscripcions.length - 1];

        Logger.logInscripcionCSV("eliminarInscripcion: ins: " + inscripcions.length);
        Logger.logInscripcionCSV("eliminarInscripcion: iNuevas: " + iNuevas.length);

        int offset = 0;
        for (int i = 0; i < iNuevas.length; i++) {
            Logger.logInscripcionCSV(inscripcions[i].getOrdenMateria() + ":" + iEliminar.getOrdenMateria());
            if (inscripcions[i].getOrdenMateria() == iEliminar.getOrdenMateria()){
                offset = 1;
            }
            Logger.logInscripcionCSV("i+offset: " + (i+offset));
            iNuevas[i] = inscripcions[i+offset];
        }
        return guardarInscripciones(context,iNuevas,letraCarrera,idAlumno);
    }


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

    private static boolean guardarInscripciones(Context context, Inscripcion[] inscripcions, char letraCarrera, int idAlumno){
        String rutaI = nombreArchivo(letraCarrera,idAlumno);
        String[][] cargado = CSVReader.cargarCSVUsuario(context,rutaI);
        String[] cavecera = cargado[0];

        String[][] filas = new String[inscripcions.length + 1][cavecera.length];
        filas[0] = cavecera;
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
        Logger.logInscripcionCSV("filas: " + cargado.length + " > " + filas.length);
        return CSVReader.guardarCSVUsuario(context,rutaI,filas);
    }

    public static Inscripcion[] cargarInscripciones(Context context, char letraCarrera, int idAlumno){

        String nombre = nombreArchivo(letraCarrera,idAlumno);
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
        String[] archivos = CSVReader.listarArchivosUsuario(context);
        List<InfoInscripcion> infos = new ArrayList<>();

        Logger.logInscripcionCSV("archivos: " + archivos.length);


        // armar el set de letras par ausar el contains()
        Set<Character> letras = new HashSet<>();
        for (char letra : Backend.getLetrarCarreras()) {
            letras.add(letra);
        }
        // recorrer el vector de archivos y armar el vector de InfoInscripcion
        for (int i = 0; i < archivos.length; i++) {
            if(archivos[i].length() == 6){
                char letraArcuivo = archivos[i].charAt(0);
                char numeroArchivo = archivos[i].charAt(1);
                if(letras.contains(letraArcuivo) && Character.isDigit(numeroArchivo)){
                    infos.add(new InfoInscripcion(
                            letraArcuivo,Integer.parseInt("" + numeroArchivo),
                            0,0,0));
                }
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

    public static String nombreArchivo(char letraCarrera, int idAlumno){
        return "" + letraCarrera + idAlumno + ".csv";
    }

    private static void setIndces(String[] cabecera){
        for (int i = 0; i < cabecera.length; i++) {
            switch (cabecera[i]){
                case "CARRERA": indiceCarrera = i; break;
                case "ALUMNO": indiceAlumno = i; break;
                case "ORDEN": indiceOrden = i; break;
                case "CONDICION": indiceCondicion = i; break;
                case "NOTA": indiceNota = i; break;
                case "COMISON": indiceComision = i; break;
                case "FECHA": indiceFecha = i; break;
            }
        }
    }

}
