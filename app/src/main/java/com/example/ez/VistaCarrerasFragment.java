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

    private String[] carreras;


    public static VistaCarrerasFragment newInstance(String[] carreras) {
        VistaCarrerasFragment fragment = new VistaCarrerasFragment();
        Bundle args = new Bundle();
        args.putStringArray("carreras", carreras);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_carreras, container, false);

        if (getArguments() != null) {
            carreras = getArguments().getStringArray("carreras");
        }

        LinearLayout containerCarreras = view.findViewById(R.id.containerCarreras);

        // Crear botones para cada carrera
        for (int i = 0; i < carreras.length; i++) {
            Button btnCarrera = new Button(getContext());
            btnCarrera.setText(carreras[i]);
            btnCarrera.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnCarrera.getLayoutParams();
            params.setMargins(0, 0, 0, 16);
            btnCarrera.setLayoutParams(params);

            final int index = i;
            btnCarrera.setOnClickListener(v -> mostrarConfirmacion(index));

            containerCarreras.addView(btnCarrera);
        }

        return view;
    }

    private void mostrarConfirmacion(int index) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Carrera")
                .setMessage("¿Desea seleccionar " + carreras[index] + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    MainActivity.setCarreraActual(MainActivity.LETRAS_CARRERAS[index]);
                    ((MainActivity) getActivity()).showFragment(new VistaMenuFragment());
                })
                .setNegativeButton("No", null)
                .show();
    }
}