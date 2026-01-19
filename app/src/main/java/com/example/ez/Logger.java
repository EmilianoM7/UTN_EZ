package com.example.ez;


import android.util.Log;

public class Logger {

    static String tag = "EE";

    public static void logMateriaCSV(String msj){
        Log.d(tag,"MateriaCSV: " + msj);
    }

    public static void logInscripcionCSV(String msj){
        Log.d(tag,"InscripcionCSV: " + msj);
    }

    public static void logCSVReader(String msj){
        Log.d(tag,"CSVReader: " + msj);
    }

    public static void logBackend(String msj){
        Log.d(tag,"Backend: " + msj);
    }

    public static void logMateria(String msj ) {
        Log.d(tag,"Materia: " + msj);
    }

    public static void log(String msj) {
        Log.d(tag,msj);
    }
}

