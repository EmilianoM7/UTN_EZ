package com.example.ez.useCase;

import static androidx.core.content.ContentProviderCompat.requireContext;

import com.example.ez.domain.Materia;
import com.example.ez.repo.MateriaCSV;

public class ListarMaterias {

    public static String[][] execute(char carrera){
        // leer el CSV

        Materia[] materias = MateriaCSV.cargarMaterias(carrera);

        // si esta NULL, hay error del back
        if (materias != null){
            // crear la respuesta
            String[][] respuesta = new String[materias.length][6];
            // llenar la respuesta
            for (int i = 0; i < respuesta.length; i++) {
                // nivel, orden, nombreMateria, sigla, condicion, nota
                respuesta[i][0] = "" + materias[i].getNivel().getNumero();
                respuesta[i][1] = "" + materias[i].getOrden();
                respuesta[i][2] = materias[i].getNombre();
                respuesta[i][3] = materias[i].getSigla();
                respuesta[i][4] = "D";
                respuesta[i][5] = "7";
            }
            return respuesta;
        }

        return new String[][]{
                // nivel, orden, nombreMateria, sigla, condicion, nota
                {"1", "0", "DB: materias no Encontradas", "NO", "D", "0"},
        };
    }
}
