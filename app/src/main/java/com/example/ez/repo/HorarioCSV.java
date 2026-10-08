package com.example.ez.repo;

import com.example.ez.backend.Logger;

import java.util.ArrayList;

public class HorarioCSV {

    public static int[] cargarHorarioComision(String nombreComision, boolean primerCuatri){
        // horarios del nivel completo
        String[][] filas = CSVReader.cargarCSV(rutaArchivo(nombreComision));
        // horarios de la comision especifica
        String[][] horariosComision = new String[25][12];
        // recorre el nivel completo hasta encontrar la comision
        for (int i = 1; i < filas.length; i++) {
            if (nombreComision.equals(filas[i][0])){
                // toma solo las 25 filas de la comision
                for (int j = 0; j < horariosComision.length; j++) {
                     eliminarPrimerCasilla(filas[j+i], horariosComision[j]);
                }
                break;
            }
        }
        // crear el array
        int columnas = horariosComision[0].length / 2;
        int[] horarios = new int[horariosComision.length * columnas];
        // recorrer el filas, pero verticalmente (columnas)
        int indice = 0;
        int primeraColumna = primerCuatri ? 0 : 6;
        for (int j = 0; j < columnas; j++) {
            for (int i = 0; i < horariosComision.length; i++) {
                horarios[indice] = parsarNumero(horariosComision[i][j + primeraColumna]);
                indice++;
            }
        }
        return horarios;
    }

    public static String[] getNombresComision(int nivel, char letraCarrera){
        ArrayList<String> arrayNombres = new ArrayList<>();
        // horarios del nivel completo
        String[][] filas = CSVReader.cargarCSV(rutaArchivo("" + nivel + letraCarrera));
        Logger.logHorarioCSV("filas[1][0]:" + filas[1][0]);
        arrayNombres.add(filas[1][0]);
        for (int i = 1; i < filas.length; i++) {
            if (!arrayNombres.contains(filas[i][0])){
                arrayNombres.add(filas[1][0]);
            }
        }
        Logger.logHorarioCSV("arrayNombres.size(): " + arrayNombres.size());
        String[] vectorNombres = new String[arrayNombres.size()];
        for (int i = 0; i < vectorNombres.length; i++) {
            vectorNombres[i] = arrayNombres.get(i);
        }
        return vectorNombres;
    }

    static void eliminarPrimerCasilla(String[] fila, String[] destino){
        //Logger.logHorarioCSV(" --- OKn " + fila.length + "->" + destino.length);
        for (int i = 0; i < destino.length; i++) {
            if (i+1 < fila.length){
                destino[i] = fila[i+1];
            }
        }
    }

    static int parsarNumero (String texto){
        int res = 0;
        if (texto != null){
            if (!texto.isEmpty()){
                res = Integer.parseInt(texto);
            }
        }
        return res;
    }

    private static String rutaArchivo(String nombreComision){
        // horrios1k.csv
        int nivel = Integer.parseInt("" + nombreComision.toCharArray()[0]);
        char letraCarrera = Character.toLowerCase(nombreComision.toCharArray()[1]);
        return "res/raw/horarios" + nivel + letraCarrera + ".csv";
    }

}
