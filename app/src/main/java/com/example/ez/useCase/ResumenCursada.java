package com.example.ez.useCase;

import com.example.ez.domain.Condicion;
import com.example.ez.domain.Materia;

public class ResumenCursada {

    public static String[] execute(char carrera, int alumno) {
        Materia[] materias = ListarMaterias.execute(carrera,alumno,true);

        int aprobado = 0;
        int aprobadoElect = 0;
        int regular = 0;
        int inscriptos = 0;
        int disponible = 0;
        int restantes = 0;
        int acumPromedio = 0;
        int acumPromedioElect = 0;
        int n = materias.length;
        int electivas = 0;
        int pts = 0;
        int ptsTotales = 20;

        for (Materia m : materias) {
            if (m.getInscripcion() != null){
                if (m.isElectiva()) {
                    if (m.getInscripcion().getCondicion() == Condicion.Aprobado){
                        aprobadoElect++;
                        acumPromedioElect += m.getInscripcion().getNota();
                        pts += m.getPuntos();
                    }
                }
                else {
                    switch (m.getInscripcion().getCondicion()){
                        case Aprobado: aprobado++; break;
                        case Regular: regular++; break;
                        case Inscripto: inscriptos++; break;
                    }
                    acumPromedio += m.getInscripcion().getNota();
                }
            }
            else if (m.isCursable()) {disponible++;}
            else {restantes++;}

            if (m.isElectiva()) {electivas++;}
        }
        disponible += inscriptos;

        // sin electivas
        int matsTotales = n - electivas;
        // con electivas
        int aprbadoTotal = aprobado + aprobadoElect;
        int acumPromedioTotal = acumPromedioElect + acumPromedio;

        return new String[]{
                "" + matsTotales,
                "" + aprobado,
                "" + regular,
                "" + inscriptos,
                "" + disponible,
                "" + restantes,
                "" + electivas,
                //
                "" + porcentaje(aprobado, matsTotales),
                "" + porcentaje(aprbadoTotal, n),
                //
                "" + promedio(acumPromedio, aprobado),
                "" + promedio(acumPromedioTotal, aprbadoTotal),
                //
                "" + pts,
                "" + ptsTotales,
                //
                "" + n
        };
    }

    static Double promedio (int acum, int n){
        if (n == 0) return 0.0;
        return (double) Math.round((double) acum / n * 100) / 100;
    }

    static int porcentaje (int a, int b){
        if (b == 0) return 0;
        return Math.round( (float) a / b * 100);
    }

}
