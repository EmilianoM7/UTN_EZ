package com.example.ez;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ez.domain.Especialiad;
import com.example.ez.domain.InfoInscripcion;
import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.repo.InscripcionCSV;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // ENV
    private static char CARRERRA_ACTUAL = Especialiad.NoEspecialidad.getLetra();
    private static int ID_ALUMNO;
    private static InfoInscripcion[] INFOS_INSCRIPCION;
    private static Inscripcion[] INSCRIPCIONES;
    private static Materia[] MATERIAS_DATOS;

    private static int[] regulares;
    private static int[] aprobadas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // primer llamada a los DATOS DE USUARIO
        primerLLamado();
    }

    private void primerLLamado(){

        //
        INFOS_INSCRIPCION = Backend.listarArchivosInscripcion(this);
        // si no hay INFOS
        if (INFOS_INSCRIPCION.length == 0){
            // mostrar lista de carreras
            showFragment(VistaCarrerasFragment.newInstance(true));
        }
        // si hay mas de una INFO
        else {
            // mostrar lista de infos
            showFragment(VistaCarrerasFragment.newInstance(false));
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

    // ENV

    public void seleccionarCarrera(char letraCarrera){
        Backend.crearInscripcionAlumno(this,letraCarrera);
        CARRERRA_ACTUAL = letraCarrera;
        MATERIAS_DATOS = Backend.listarMateriasCSV(letraCarrera);
        actualizarInscripicones(this);
        Logger.log("MainActivity.elegirCarrerra - CARRERA_ACTUAL: " + getCarreraActual());
        showFragment(new VistaMenuFragment());
    }

    public void seleccionarAlumno(char letraCarrera, int idAlumno){
        CARRERRA_ACTUAL = letraCarrera;
        ID_ALUMNO = idAlumno;
        MATERIAS_DATOS = Backend.listarMateriasCSV(letraCarrera);
        actualizarInscripicones(this);
        Logger.log("MainActivity.elegirCarrerra - CARRERA_ACTUAL: " + getCarreraActual());
        showFragment(new VistaMenuFragment());
    }

    public static void actualizarInscripicones(Context context){
        Inscripcion[] inscripcions = InscripcionCSV.cargarInscripciones(context,CARRERRA_ACTUAL,ID_ALUMNO);
        // limpiar inscripciones anteriores
        for (Materia mat : MATERIAS_DATOS) {
            mat.setInscripcion(null);
        }
        // recargar inscripciones
        if (inscripcions != null){
            for (Inscripcion ins : inscripcions) {
                getMateriaPorOrden(ins.getOrdenMateria()).setInscripcion(ins);
            }
            obtenerOrdenes(inscripcions);
            for (Materia mat : MATERIAS_DATOS) {
                mat.setCursable(mat.esCursable(regulares,aprobadas));
            }
        }
    }


    public static Materia[] getMateriasDatos(){
        return MATERIAS_DATOS;
    }

    public static char getCarreraActual(){
        return CARRERRA_ACTUAL;
    }

    public static int getIdAlumno(){
        return ID_ALUMNO;
    }

    public static InfoInscripcion[] getInfosInscripcion(){
        return INFOS_INSCRIPCION;
    }

    private static Materia getMateriaPorOrden(int orden){
        for (Materia mat : MATERIAS_DATOS){
            if (mat.getOrden() == orden){
                return mat;
            }
        }
        return null;
    }


    public static Materia[] getMateriasPorOrden(int[] ordenes){

        if (ordenes[0] != 0){
            List<Materia> filtrado = new ArrayList<>();

            Set<Integer> conjunto = new HashSet<>();
            for (int valor : ordenes) {
                conjunto.add(valor);
            }
            for (Materia mat : getMateriasDatos()) {
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

    public static List<Materia> getMateriasPorNivel(int nivel, boolean todas){
        List<Materia> filtro = new ArrayList<>();
        Materia[] materias = todas ? getMateriasDatos() : getMateriasConInscripcion();
        for (Materia mat : materias){
            if (mat.getNumeroNivel() == nivel){
                filtro.add(mat);
            }
        }
        return filtro;
    }

    public static Materia[] getMateriasConInscripcion(){
        List<Materia> filtro = new ArrayList<>();
        for (Materia mat : MATERIAS_DATOS){
            if (mat.getInscripcion() != null){
                filtro.add(mat);
            }
        }
        Materia[] vector = new Materia[filtro.size()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = filtro.get(i);
        }
        return vector;
    }

    //AUX

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