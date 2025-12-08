package com.example.ez;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaListarFragment extends Fragment {

    private int opcionSeleccionada;
    private int materiaSeleccionada = -1;
    private String[] datosMateria;
    private LinearLayout containerLista;
    private TextView txtInfoMateria;
    private View barraInfoEdicion;
    private Map<Integer, LinearLayout> gruposNivel = new HashMap<>();
    private Button ultimoBotonSeleccionado;

    public static VistaListarFragment newInstance(int opcion) {
        VistaListarFragment fragment = new VistaListarFragment();
        Bundle args = new Bundle();
        args.putInt("opcion", opcion);
        fragment.setArguments(args);
        Logger.tracer("VistaListarFragment");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_listar, container, false);

        if (getArguments() != null) {
            opcionSeleccionada = getArguments().getInt("opcion");
        }

        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        txtTitulo.setText("Lista de " + MainActivity.OPCION_LISTAR[opcionSeleccionada]);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());

        containerLista = view.findViewById(R.id.containerLista);
        txtInfoMateria = view.findViewById(R.id.txtInfoMateria);
        barraInfoEdicion = view.findViewById(R.id.barraInfoEdicion);

        Button btnInfo = view.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(v -> mostrarInfoMateria());

        Button btnEditar = view.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(v -> editarMateria());

        actualizarLista();

        return view;
    }

    public void actualizarLista() {
        containerLista.removeAllViews();
        gruposNivel.clear();

        String[][] materias;
        if (opcionSeleccionada == 0) {
            materias = Backend.listarInscripciones(MainActivity.getCarreraActual());
        } else {
            materias = Backend.listarMaterias(MainActivity.getCarreraActual());
        }

        // Agrupar por nivel
        // ES BUENA LA DEL MAP
        Map<Integer, List<String[]>> materiasPorNivel = new HashMap<>();
        String[] nombresNivel = {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto"};

        for (String[] materia : materias) {
            int nivel = Integer.parseInt(materia[0]);
            // va inicializando los niveles (si no lo estan)
            if (!materiasPorNivel.containsKey(nivel)) {
                materiasPorNivel.put(nivel, new ArrayList<>());
            }
            // mientras les inserta materias
            materiasPorNivel.get(nivel).add(materia);
        }

        for (int i = 0; i <= nombresNivel.length; i++) {

            List<String[]> materiasNivel = materiasPorNivel.get(i);

            // rear el boton colapsable del nivel
            Button btnNivel = new Button(getContext());
            btnNivel.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //crea el grupo de materias correspondiente
            LinearLayout grupoMaterias = new LinearLayout(getContext());
            grupoMaterias.setOrientation(LinearLayout.VERTICAL);
            grupoMaterias.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //si el nivel esta vacio
            if (materiasNivel == null || materiasNivel.isEmpty()) {
                btnNivel.setText(nombresNivel[i] + " (vacío)");
                btnNivel.setEnabled(false);
            }
            // si el nivel itene materias
            else {
                btnNivel.setText(nombresNivel[i] + " -");
                grupoMaterias.setVisibility(View.VISIBLE);

                for (String[] materia : materiasNivel) {
                    Button btnMateria = crearBotonMateria(materia);
                    grupoMaterias.addView(btnMateria);
                }

                gruposNivel.put(i, grupoMaterias);

                // accion de expandir y colapsar
                btnNivel.setOnClickListener(v -> {
                    if (grupoMaterias.getVisibility() == View.VISIBLE) {
                        grupoMaterias.setVisibility(View.GONE);
                        btnNivel.setText(btnNivel.getText().toString().replace("-", "+"));
                    } else {
                        grupoMaterias.setVisibility(View.VISIBLE);
                        btnNivel.setText(btnNivel.getText().toString().replace("+", "-"));
                    }
                });
            }

            containerLista.addView(btnNivel);
            containerLista.addView(grupoMaterias);
        }
    }

    private Button crearBotonMateria(String[] materia) {
        Button btn = new Button(getContext());
        String orden = materia[1];
        String nombre = materia[2];
        String nota = materia[5];
        String condicion = materia[4];

        btn.setText(orden + " - " + truncarCadena(nombre, 15) + " (" + nota + ")");
        btn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
        params.setMargins(32, 4, 4, 4);
        btn.setLayoutParams(params);

        // Colorear según condición
        String colorCondicion = "#9E9E9E"; // Gris
        switch (condicion) {
            case "R": colorCondicion = "#4CAF50"; break; // Verde
            case "A": colorCondicion = "#2196F3"; break; // Azul
            case "I": colorCondicion = "#FF9800"; break; // Naranja
            case "D": colorCondicion = "#FFEB3B"; break; // Amarillo
        }
        btn.setBackgroundColor(Color.parseColor(colorCondicion));

        //set listenre
        btn.setOnClickListener(v -> seleccionarMateria(Integer.parseInt(orden), nombre, btn, materia));

        return btn;
    }

    private void seleccionarMateria(int orden, String nombre, Button btn, String[] materia) {
        materiaSeleccionada = orden;
        datosMateria = materia;
        txtInfoMateria.setText(orden + " - " + nombre);
        barraInfoEdicion.setVisibility(View.VISIBLE);

        if (ultimoBotonSeleccionado != null) {
            ultimoBotonSeleccionado.setPadding(0, 0, 0, 0);
        }

        btn.setPadding(8, 8, 8, 8);
        ultimoBotonSeleccionado = btn;
    }

    private void mostrarInfoMateria() {
        if (materiaSeleccionada == -1) return;
        /*
        String[] info = Backend.infoMateria(materiaSeleccionada);
        ((MainActivity) getActivity()).showFragmentWithBackStack(
                VistaInfoResumenFragment.newInstance("InfoMateria", info));

         */
    }

    private void editarMateria() {
        if (materiaSeleccionada == -1 || datosMateria == null) return;
        /*
        VistaEditarMateriaFragment fragment = VistaEditarMateriaFragment.newInstance(
                Integer.parseInt(datosMateria[1]),
                datosMateria[4].charAt(0),
                Integer.parseInt(datosMateria[5]),
                "4K1",
                datosMateria[2]
        );

        fragment.setOnConfirmListener(() -> actualizarLista());

        getChildFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();

         */
    }

    private String truncarCadena(String cadena, int limite){
        if (cadena.length() > limite) {
            return cadena.substring(0, limite - 4) + "...";
        }
        return cadena;
    }
}