package com.example.ez;

import android.content.Context;

import com.example.ez.repo.MateriaCSV;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private static String rutaRaw = "res/raw/";
    private static String directorioUsuario = "datosUsuario";


    public static boolean existeArchivo(String ruta){
        File archivo = new File(ruta);
        return archivo.exists();
    }

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

            return matrizar(filas);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // USUARIO

    public static String[] listarArchivosUsuario(Context context) {
        File directorio = new File(context.getFilesDir(), directorioUsuario);

        // Verificar si existe
        if (!directorio.exists()) {
            return new String[0]; // Array vacío
        }

        // Obtener lista de nombres
        String[] nombres = directorio.list();

        Logger.logCSVReader("listarArchivosUsuario: " + nombres.length);
        for (int i = 0; i < nombres.length; i++) {
            Logger.logCSVReader(nombres[i]);
        }

        // Si es null, devolver array vacío
        return nombres != null ? nombres : new String[0];
    }

    public static boolean guardarCSVUsuario(Context context, String nombreArchivo, String[][] datos) {
        String guardar = "";
        for (int i = 0; i < datos.length; i++) {
            guardar += unirLinea(datos[i], ';') + "\n";
        }
        Logger.logCSVReader("guardarCSVUsuario");
        Logger.log(guardar);
        guardarTexto(context, nombreArchivo, guardar);
        return true;
    }

    public static String[][] cargarCSVUsuario(Context context, String nombreArchivo){
        String leido = leerTexto(context, nombreArchivo);
        Logger.logCSVReader("cargarCSVUsuario");
        Logger.log(leido);
        if (leido != null){
            String[] lineas = leido.split("\n");
            String[][] matriz = new String[lineas.length][dividirLinea(lineas[0]).length];

            for (int i = 0; i < matriz.length; i++) {
                matriz[i] = dividirLinea(lineas[i]);
            }
            return matriz;
        }
        return null;
    }

    // LECTURA-CARGA

    public static void guardarTexto(Context context, String nombreArchivo, String contenido) {
        // Crear subdirectorio

        Logger.logCSVReader("guardarTexto(" + nombreArchivo + ")");

        File directorio = new File(context.getFilesDir(), directorioUsuario);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        File archivo = new File(directorio, nombreArchivo);

        try {
            FileWriter fw = new FileWriter(archivo);
            fw.write(contenido);
            fw.close();

            Logger.logCSVReader("Guardado en: " + archivo.getAbsolutePath());
            // Imprime: /data/data/tu.paquete/files/mis_datos/archivo.txt

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String leerTexto(Context context, String nombreArchivo) {
        // Acceder al subdirectorio
        File directorio = new File(context.getFilesDir(), directorioUsuario);
        File archivo = new File(directorio, nombreArchivo);

        // Verificar si el archivo existe
        if (!archivo.exists()) {
            Logger.logCSVReader("El archivo no existe");
            return "NO_EXISTE";
        }

        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }

            br.close();
            fr.close();

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "IO_ERROR";
        }
    }

    // AUX

    private static String obtenerNombreSinExtension(String nombreArchivo) {
        int indicePunto = nombreArchivo.lastIndexOf('.');
        if (indicePunto > 0) {
            return nombreArchivo.substring(0, indicePunto);
        }
        return nombreArchivo;
    }

    private static String unirLinea(String[] elementos, char separador) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < elementos.length; i++) {
            sb.append(elementos[i]);

            // Añadir coma entre elementos (excepto el último)
            if (i < elementos.length - 1) {
                sb.append(separador);
            }
        }

        return sb.toString();
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

    private static String[] vectorizar(List<String> lista){
        String[] vector = new String[lista.size()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = lista.get(i);
        }
        return vector;
    }

    private static String[][] matrizar(List<String[]> lista){
        String[][] vector = new String[lista.size()][lista.get(0).length];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = lista.get(i);
        }
        return vector;
    }
}