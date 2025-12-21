package com.example.ez;


import android.util.Log;

public class Logger {

    public static void logMateriaCSV(String msj){
        Log.d("EE","MateriaCSV: " + msj);
    }

      public static void logBackend(String msj){
        Log.d("EE","Backend: " + msj);
    }


    public static void logMateria(String msj ) {
        Log.d("EE","Materia: " + msj);
    }
}

