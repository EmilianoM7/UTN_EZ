package com.example.ez.repo;

import com.example.ez.CSVReader;
import com.example.ez.domain.Especialiad;
import com.example.ez.domain.Materia;
import com.example.ez.domain.Nivel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MateriaCSV {

    public static int
            indiceNivel,
            indiceOrden,
            indiceSigla,
            indiceNombre,
            indiceEspecialidad,
            indiceRegulares,
            indiceAprobadas
    ;

    public static Materia[] cargarMaterias(char carrera){

        String ruta = rutaArchivo(carrera);
        try {
            InputStream is = MateriaCSV.class.getClassLoader().getResourceAsStream(ruta);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));


            // fila se usara para cabecera y para el bucle
            String[] fila;

            // leer la cabecera y asignar indices de columna
            fila = dividirLinea(reader.readLine());
            setIndces(fila);

            // crear lista de materias
            List<Materia> materias = new ArrayList<>();

            // BUCLE
            // leer cada fila y crear cada materia
            while ((fila = dividirLinea(reader.readLine())) != null) {
                Materia m = new Materia();
                m.setOrden(Integer.parseInt(fila[indiceOrden]));
                m.setNombre(fila[indiceNombre]);
                m.setDescripcion("NoHay");
                m.setPrograma("NoHay");
                m.setSigla(fila[indiceSigla]);
                m.setElectiva (m.getOrden() >= 100);
                m.setCorrelativasReg(tomarCorrelativas(fila[indiceRegulares]));
                m.setCorrelativasAp(tomarCorrelativas(fila[indiceAprobadas]));
                m.setNivel(Nivel.fromNumero(Integer.parseInt(fila[indiceNivel])));
                m.setEspecialiad(Especialiad.fromLetra(fila[indiceEspecialidad].charAt(0)));
                materias.add(m);
            }

            reader.close();

            //retornar
            return vectorizar(materias);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
                case "NOMBRE": indiceNombre = i; break;
                case "ESPEC": indiceEspecialidad = i; break;
                case "REG": indiceRegulares = i; break;
                case "APR": indiceAprobadas = i; break;
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

    private static Materia[] vectorizar(List<Materia> listaMats){
        Materia[] vectorMats = new Materia[listaMats.size()];
        for (int i = 0; i < vectorMats.length; i++) {
            vectorMats[i] = listaMats.get(i);
        }
        return vectorMats;
    }

    private static String[] dividirLinea(String linea){
        return linea != null ? linea.split(";") : null;
    }

    private static String rutaArchivo(char carrera){
        // materiasK
        return "res/raw/materias" + Character.toLowerCase(carrera) + ".csv";
    }
/*
    public static Materia[] cargarMaterias(char carrera){

        String[][] filas = CSVReader.cargarCSV(rutaArchivo(carrera));

        // iniciar materias y descontar fila cabecera
        Materia[] materias = new Materia[filas.length - 1];

        // acomodar indices de columnas
        setIndces(filas[0]);

        // contar desde 1, hasta la ultima fila
        for (int i = 1; i < filas.length; i++) {

            materias[i-1] = new Materia(
                    Integer.parseInt(filas[i][indiceOrden]),
                    filas[i][indiceNombre],
                    "NoHay",
                    "NoHay",
                    filas[i][indiceSigla],
                    false,
                    tomarCorrelativas(filas[i][indiceRegulares]),
                    tomarCorrelativas(filas[i][indiceAprobadas]),
                    Nivel.fromNumero(Integer.parseInt(filas[i][indiceNivel])),
                    Especialiad.fromLetra(filas[i][indiceEspecialidad].charAt(0))
            );
            materias[i].setElectiva(materias[i].getOrden() >= 100);

        }

        //retornar
        return materias;

    }*/

}
