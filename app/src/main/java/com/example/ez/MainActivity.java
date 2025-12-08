package com.example.ez;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    // enum
    static char[] LETRAS_CARRERAS = {'K', 'C', 'M'};
    public static String[] OPCION_LISTAR = {
            "Inscripciones",
            "Materias"
    };

    private static String alumnoActual = "alumno?";
    private static char carreraActual = '?';
    private static int anoIngreso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //primera llamada a Backend
        String[] sesion = Backend.dataInicial();

        if (sesion != null) {
            // Hay usuario, mostrar VistaMenu
            alumnoActual = sesion[0];
            carreraActual = sesion[1].charAt(0);
            anoIngreso = Integer.parseInt(sesion[2]);
            showFragment(new VistaMenuFragment());
        } else {
            // No hay usuario, mostrar VistaCarreras
            showFragment(VistaCarrerasFragment.newInstance(Backend.getCarreras()));
        }
        Logger.tracer("MainActivity.onCreate()");

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

    public static void setCarreraActual(char letra) {
        carreraActual = letra;
    }

    public static char getCarreraActual() {
        return carreraActual;
    }
}