package com.example.ez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;
import com.example.ez.repo.InscripcionCSV;
import com.example.ez.repo.MateriaCSV;


public class VistaMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_menu, container, false);

        // titulo segun carrera
        TextView txtTitulo = view.findViewById(R.id.txtMenuTitle);
        String titulo = "EZ - " + MainActivity.getNombreLetraCarreraActual();
        txtTitulo.setText(titulo);

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

            ((MainActivity) getActivity()).showFragmentWithBackStack(VistaListarFragment.newInstance(1));
        });

        btnInfoCarrera.setOnClickListener(v -> {
            String[] datos = Backend.infoCarrera(MainActivity.getLetraCarreraActual());
            ((MainActivity) getActivity()).showFragmentWithBackStack(
                    VistaInfoResumenFragment.newInstance("InfoCarrera", datos));
        });

        btnResumenCursada.setOnClickListener(v -> {
            String[] datos = Backend.resumenCursada(MainActivity.getLetraCarreraActual(),1);
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