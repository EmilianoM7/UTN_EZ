package com.example.ez;

import com.example.ez.repo.MateriaCSV;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static String[][] cargarCSV(String ruta){

        try {
            InputStream is = CSVReader.class.getClassLoader().getResourceAsStream(ruta);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // crear lista
            List<String[]> filas = new ArrayList<>();

            // fila
            String fila;

            // BUCLE
            // leer cada fila y crear cada fila
            while ((fila = reader.readLine()) != null) {
                filas.add(dividirLinea(fila));
            }

            reader.close();

            return vectorizar(filas);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String primeraLinea(String ruta, int filas) {
        String linea = "noLinea";

        try {
            InputStream is = MateriaCSV.class.getClassLoader()
                    .getResourceAsStream(ruta);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            linea = reader.readLine();
            for (int i = 0; i < filas; i++) {
                linea += "\n" + reader.readLine();
            }

            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "CSV: " + linea;
    }

    private static String[] dividirLinea(String linea){
        return linea != null ? linea.split(";") : null;
    }

    private static String[][] vectorizar(List<String[]> lista){
        String[][] vector = new String[lista.size()][lista.get(0).length];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = lista.get(i);
        }
        return vector;
    }
}