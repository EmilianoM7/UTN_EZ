package com.example.ez.backend;

import com.example.ez.domain.Materia;
import com.example.ez.repo.HorarioCSV;

import java.util.ArrayList;

public class ArmarHorarios {

    // [materia][indicesModulosOcupados]
    // por cada materia, los indices de modulos ocupados por la materia
    public static ArrayList<int[]> excecute(Materia[] inscriptas){
        // si hay mas de una materia de la misma comision, carga la comision una vez
        ArrayList<String> comisionesCargadas = new ArrayList<String>();
        ArrayList<int[]> horariosComision = new ArrayList<int[]>();
        ArrayList<int[]> res = new ArrayList<int[]>();

        // Buscar un horario por cada materia inscripta
        for (int i = 0; i < inscriptas.length; i++) {
            int[] comisionActual = new int[0];
            // cargar horario completo de la comision que corresponde a la materia inscripta
            if (!comisionesCargadas.contains(inscriptas[i].getNombreComision())){
                // si la comision de la inscripcion no esta cargada ya, la carga
                comisionesCargadas.add(inscriptas[i].getNombreComision());
                horariosComision.add(
                        HorarioCSV.cargarHorarioComision(
                                inscriptas[i].getNombreComision(), true));
                comisionActual = horariosComision.get(horariosComision.size() -1);
            }
            else{
                for (int j = 0; j < comisionesCargadas.size(); j++) {
                    if (comisionesCargadas.get(j).equals(inscriptas[i].getNombreComision())){
                        comisionActual = horariosComision.get(j);
                    }
                }
            }
            res.add(obtenerHorarioMateria(comisionActual, inscriptas[i].getOrden()));
        }

        return res;
    }
    // esto obtendra los indices de los modulos ocupados por la materia
    static int[] obtenerHorarioMateria(int[] horarioComision, int ordenMateria){
        ArrayList<Integer> arrayModulos = new ArrayList<Integer>();
        for (int i = 0; i < horarioComision.length; i++) {
            if (horarioComision[i] == ordenMateria){
                arrayModulos.add(i);
            }
        }
        int[] vectorModulos = new int[arrayModulos.size()];
        for (int i = 0; i < vectorModulos.length; i++) {
            vectorModulos[i] = arrayModulos.get(i);
        }
        return vectorModulos;
    }

}
