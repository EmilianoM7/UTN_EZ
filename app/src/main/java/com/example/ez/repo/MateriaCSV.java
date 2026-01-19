package com.example.ez.repo;

import android.content.Context;

import com.example.ez.CSVReader;
import com.example.ez.domain.Especialiad;
import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.domain.ModalidadCursdo;
import com.example.ez.domain.Nivel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MateriaCSV {

    // esta clase convierte de String[] - Materia

    private static final Log log = LogFactory.getLog(MateriaCSV.class);
    public static int
            indiceNivel,
            indiceOrden,
            indiceSigla,
            indiceModalidad,
            indiceNombre,
            indiceEspecialidad,
            indiceRegulares,
            indiceAprobadas,
            indicePuntos
    ;

    public static Materia[] cargarMaterias(char carrera){

        String[][] filas = CSVReader.cargarCSV(rutaArchivo(carrera));

        // iniciar materias y descontar fila cabecera
        Materia[] materias = new Materia[filas.length - 1];

        // acomodar indices de columnas
        setIndces(filas[0]);

        // contar desde 1, hasta la ultima fila
        for (int i = 1; i < filas.length; i++) {
            //crear cada Materia
            Materia mat = new Materia(
                    Integer.parseInt(filas[i][indiceOrden]),
                    filas[i][indiceNombre],
                    filas[i][indiceSigla],
                    filas[i][indiceModalidad],
                    false,
                    false,
                    tomarCorrelativas(filas[i][indiceRegulares]),
                    tomarCorrelativas(filas[i][indiceAprobadas]),
                    Integer.parseInt(filas[i][indicePuntos]),
                    null,
                    Nivel.fromNumero(Integer.parseInt(filas[i][indiceNivel])),
                    Especialiad.fromLetra(filas[i][indiceEspecialidad].charAt(0))
            );
            mat.setElectiva(mat.getOrden() >= 100);

            // asignar Materia a cada indice
            materias[i-1] = mat;
        }
        //retornar
        return materias;
    }

    public static String indices(){
        return
                indiceNivel + "." +
                indiceOrden + "." +
                indiceSigla + "." +
                indiceNombre + "." +
                indiceEspecialidad + "." +
                indiceRegulares + "." +
                indiceAprobadas;
    }

    private static void setIndces(String[] cabecera){
        for (int i = 0; i < cabecera.length; i++) {
            switch (cabecera[i]){
                case "NIVEL": indiceNivel = i; break;
                case "ORDEN": indiceOrden = i; break;
                case "SIGLA": indiceSigla = i; break;
                case "MODALIDAD": indiceModalidad = i; break;
                case "NOMBRE": indiceNombre = i; break;
                case "ESPEC": indiceEspecialidad = i; break;
                case "REG": indiceRegulares = i; break;
                case "APR": indiceAprobadas = i; break;
                case "PUNTOS": indicePuntos = i; break;
            }
        }
    }

    private static int[] tomarCorrelativas(String texto){
        if (texto != null && !texto.isEmpty()) {
            String[] splited = texto.split("_");
            int[] numeros = new int[splited.length];
            for (int i = 0; i < numeros.length; i++) {
                numeros[i] = Integer.parseInt(splited[i]);
            }
            return numeros;
        }
        return new int[]{0};
    }

    private static String rutaArchivo(char carrera){
        // materiasK
        return "res/raw/materias" + Character.toLowerCase(carrera) + ".csv";
    }

    private static Inscripcion buscarInscripcion (Inscripcion[] lista, int ordenBuscado){
        for (Inscripcion inscripcion : lista) {
            if (inscripcion.getOrdenMateria() == ordenBuscado) {
                return inscripcion;
            }
        }
        return null;
    }

}
