package com.example.ez;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

public class VistaCarrerasFragment extends Fragment {

    public static VistaCarrerasFragment newInstance() {
        VistaCarrerasFragment fragment = new VistaCarrerasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_carreras, container, false);

        LinearLayout containerCarreras = view.findViewById(R.id.containerCarreras);

        // Crear botones para cada carrera

        String [] nombresBoton = MainActivity.getNombresLetrasCarreras();

        for (int i = 0; i < nombresBoton.length; i++) {
            Button btnCarrera = new Button(getContext());

            btnCarrera.setText(nombresBoton[i]);
            //btnCarrera.setText(Backend.getCarreras().length);
            //btnCarrera.setText("" + Backend.getCarreras().length);

            btnCarrera.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnCarrera.getLayoutParams();
            params.setMargins(0, 0, 0, 16);
            btnCarrera.setLayoutParams(params);

            int finalI = i;

            btnCarrera.setOnClickListener(v -> mostrarConfirmacion(finalI, nombresBoton[finalI]));
/*
            btnCarrera.setOnClickListener(v ->{
                MainActivity.elegirCarrerra(finalI);
                btnCarrera.setText("nivel0 " + MainActivity.NIVELES_NOMBRE[0]);

            });

 */



            containerCarreras.addView(btnCarrera);
        }

        return view;
    }

    private void mostrarConfirmacion(int finalI, String nombre) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Carrera")
                .setMessage("¿Desea seleccionar la carrera?\n" + nombre)
                .setPositiveButton("Sí", (dialog, which) -> {
                    MainActivity.elegirCarrerra(finalI);
                    ((MainActivity) getActivity()).showFragment(new VistaMenuFragment());
                })
                .setNegativeButton("No", null)
                .show();
    }
}