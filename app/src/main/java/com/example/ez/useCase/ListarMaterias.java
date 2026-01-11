package com.example.ez.useCase;

import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.repo.InscripcionCSV;
import com.example.ez.repo.MateriaCSV;

import java.util.ArrayList;
import java.util.List;

public class ListarMaterias {

    private static int[] regulares;
    private static int[] aprobadas;

    public static Materia[] execute(char letraCarrera, int alumno, boolean todas) {
        Materia[] materias = MateriaCSV.cargarMaterias(letraCarrera,alumno);
        Inscripcion[] inscripcions = InscripcionCSV.cargarInscripciones(alumno);

        // esto es para calcular las corelativas (cursables)
        if (todas){
            if (inscripcions != null){

                // obtener ordenes de regulares y aprobadas
                obtenerOrdenes(inscripcions);

                // asignar si es cursable o no
                for (Materia m : materias) {
                    m.setCursable(m.esCursable(regulares,aprobadas));
                }
            }
        }
        // ni no, solo se filtra solo inscripciones
        else {
            List<Materia> mats = new ArrayList<>();
            for (Materia m : materias) {
                if (m.getInscripcion() != null){
                    mats.add(m);
                }
            }
            materias = new Materia[mats.size()];
            for (int i = 0; i < materias.length; i++) {
                materias[i] = mats.get(i);
            }
        }
        return materias;
    }

    private static void obtenerOrdenes(Inscripcion[] inscripcions){
        List<Integer> regs = new ArrayList<>();
        List<Integer> aps = new ArrayList<>();
        for (Inscripcion ins : inscripcions) {
            if (ins.esRegular()){
                regs.add(ins.getOrdenMateria());
            } else if (ins.esAprobado()) {
                aps.add(ins.getOrdenMateria());
            }
        }
        // vectorizar
        regulares = new int[regs.size()];
        for (int i = 0; i < regulares.length; i++) {
            regulares[i] = regs.get(i);
        }
        aprobadas = new int[aps.size()];
        for (int i = 0; i < aprobadas.length; i++) {
            aprobadas[i] = aps.get(i);
        }
    }
}
