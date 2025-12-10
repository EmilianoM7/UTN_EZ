package com.example.ez;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static String filePath = "/logeos.txt";
    private static DateTimeFormatter formatter;


    public static void tracer(String msj){
        log(">> " + msj + " - OK");
    }

    public static void log(String mensaje) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            String linea = String.format("[%s] %s", timestamp, mensaje);
            writer.println(linea);
        } catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
        }
    }

}

