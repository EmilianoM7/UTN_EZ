package com.example.ez;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ez.domain.Materia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // variables estaticas del contexto
    private static char[] CARRERAS_LETRA;
    private static String[] CARRERAS_NOMBRE;

    public static String[] NIVELES_NOMBRE;
    static Materia[] MATERIAS_DATOS;
    public static String[] OPCION_LISTAR = {
            "Inscripciones",
            "Materias"
    };
    static String ALUMNO_ACTUAL = "alumno?";
    static int CARRERRA_ACTUAL = -1; // -1 es el valor nulo
    static int MATERIA_INDICE_ACTUAL = -1; // -1 es el valor nulo
    static int ANO_INGRESO = 0;

    Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // busca las carreras
        buscarCarreras();

        //primera llamada a Backend
        String[] sesion = Backend.dataInicial();

        if (sesion != null) {
            // Hay usuario, mostrar VistaMenu
            ALUMNO_ACTUAL = sesion[0];
            elegirCarrerra(Integer.parseInt(sesion[1]));
            ANO_INGRESO = Integer.parseInt(sesion[2]);
            showFragment(new VistaMenuFragment());
        } else {
            // No hay usuario, mostrar VistaCarreras
            showFragment(VistaCarrerasFragment.newInstance());
        }

    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void showFragmentWithBackStack(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // funciones de contexto

    void buscarCarreras(){
        // llamado
        String[][] carreras = Backend.getCarreras();
        int largo = carreras.length;
        // declarar largo vectores
        CARRERAS_LETRA = new char[largo];
        CARRERAS_NOMBRE = new String[largo];
        // rellenar ambos
        for (int i = 0; i < largo; i++) {
            CARRERAS_LETRA[i] = carreras[i][0].charAt(0);
            CARRERAS_NOMBRE[i] = carreras[i][1];
        }
    }

    public static char getLetraCarreraActual(){
        return 'k';
        //return CARRERAS_LETRA[CARRERRA_ACTUAL];
    }

    public static String getNombreLetraCarrera(int indice){
        return "(" + CARRERAS_LETRA[indice] + ") " + CARRERAS_NOMBRE[indice];
    }

    public static String getNombreLetraCarreraActual(){
        return getNombreLetraCarrera(CARRERRA_ACTUAL);
    }

    public static String[] getNombresLetrasCarreras(){
        String[] nl = new String[CARRERAS_LETRA.length];
        for (int i = 0; i < nl.length; i++) {
            nl[i] = getNombreLetraCarrera(i);
        }
        return nl;
    }

    public static void elegirCarrerra(int indiceCarrera){
        CARRERRA_ACTUAL = indiceCarrera;
        NIVELES_NOMBRE = Backend.getNiveles(getLetraCarreraActual());
    }

    public static void buscarInscripciones(){
        MATERIAS_DATOS = Backend.listarInscripciones(getLetraCarreraActual(),1);
    }

    public static void buscarMaterias(){
        MATERIAS_DATOS = Backend.listarMaterias(getLetraCarreraActual(),1);
    }

    public static Materia[] getMateriasOrden(int[] ordenes){

        if (ordenes[0] != 0){
            List<Materia> filtrado = new ArrayList<>();

            Set<Integer> conjunto = new HashSet<>();
            for (int valor : ordenes) {
                conjunto.add(valor);
            }
            for (Materia mat : MATERIAS_DATOS) {
                if (conjunto.contains(mat.getOrden())) {
                    filtrado.add(mat);
                }
            }
            Materia[] vector = new Materia[filtrado.size()];
            for (int i = 0; i < vector.length; i++) {
                vector[i] = filtrado.get(i);
            }

            return vector;
        }
        return null;
    }

    public static List<Materia> filtrarNivel(int nivel){
        List<Materia> filtro = new ArrayList<>();
        for (Materia mat : MATERIAS_DATOS){
            if (mat.getNumeroNivel() == nivel){
                filtro.add(mat);
            }
        }
        return filtro;
    }


}