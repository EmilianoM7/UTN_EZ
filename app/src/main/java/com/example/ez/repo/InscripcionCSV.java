package com.example.ez.repo;

import com.example.ez.CSVReader;
import com.example.ez.domain.Condicion;
import com.example.ez.domain.Inscripcion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class InscripcionCSV {
// esta clase convierte de String[] - Materia

    private static final Log log = LogFactory.getLog(InscripcionCSV.class);
    public static int
            indiceOrden,
            indiceCondicion,
            indiceNota,
            indiceComision,
            indiceFecha;

    public static Inscripcion[] cargarInscripciones(int alumno){

        String[][] filas = CSVReader.cargarCSV(rutaArchivo(alumno));

        // iniciar materias y descontar fila cabecera
        Inscripcion[] inscripcions = new Inscripcion[filas.length - 3];

        // acomodar indices de columnas (en 3ra fila)
        setIndces(filas[2]);

        // contar desde 1, hasta la ultima fila
        for (int i = 3; i < filas.length; i++) {
            //crear cada Inscripcion
            Inscripcion ins = new Inscripcion(
                    Integer.parseInt(filas[i][indiceOrden]),
                    new Date(),
                    Integer.parseInt(filas[i][indiceNota]),
                    filas[i][indiceComision],
                    Condicion.fromLetra(filas[i][indiceCondicion].charAt(0))
            );

            // asignar Inscripcion a cada indice
            inscripcions[i-3] = ins;

            //Logger.logInscripcionCSV(materias[i-1].tostring());
        }
        //retornar
        return inscripcions;
    }

    public static String indices(){
        return
                indiceOrden + "." +
                        indiceCondicion + "." +
                        indiceNota + "." +
                        indiceComision + "." +
                        indiceFecha;
    }

    private static void setIndces(String[] cabecera){
        for (int i = 0; i < cabecera.length; i++) {
            switch (cabecera[i]){
                case "ORDEN": indiceOrden = i; break;
                case "CONDICION": indiceCondicion = i; break;
                case "NOTA": indiceNota = i; break;
                case "COMISON": indiceComision = i; break;
                case "FECHA": indiceFecha = i; break;
            }
        }
    }

    private static String rutaArchivo(int alumno){
        // i1
        return "res/raw/i" + alumno + ".csv";
    }
}
