package com.example.ez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class VistaMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_menu, container, false);

        Button btnListarInscripciones = view.findViewById(R.id.btnListarInscripciones);
        Button btnListarMaterias = view.findViewById(R.id.btnListarMaterias);
        Button btnInfoCarrera = view.findViewById(R.id.btnInfoCarrera);
        Button btnResumenCursada = view.findViewById(R.id.btnResumenCursada);
        Button btnHorarios = view.findViewById(R.id.btnHorarios);
        Button btnSalir = view.findViewById(R.id.btnSalir);

        btnListarInscripciones.setOnClickListener(v -> {
            ((MainActivity) getActivity()).showFragmentWithBackStack(
                    VistaListarFragment.newInstance(0));
        });

        btnListarMaterias.setOnClickListener(v -> {
            ((MainActivity) getActivity()).showFragmentWithBackStack(
                    VistaListarFragment.newInstance(1));
        });

        btnInfoCarrera.setOnClickListener(v -> {
            String[] datos = Backend.infoCarrera(MainActivity.getCarreraActual());
            ((MainActivity) getActivity()).showFragmentWithBackStack(
                    VistaInfoResumenFragment.newInstance("InfoCarrera", datos));
        });

        btnResumenCursada.setOnClickListener(v -> {
            String[] datos = Backend.resumenCursada();
            ((MainActivity) getActivity()).showFragmentWithBackStack(
                    VistaInfoResumenFragment.newInstance("ResumenCursada", datos));
        });

        btnHorarios.setOnClickListener(v -> {
            ((MainActivity) getActivity()).showFragmentWithBackStack(
                    new VistaHorariosFragment());
        });

        btnSalir.setOnClickListener(v -> {
            getActivity().finish();
        });

        return view;
    }
}