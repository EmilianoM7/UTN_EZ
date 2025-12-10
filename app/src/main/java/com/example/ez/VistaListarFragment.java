package com.example.ez;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class VistaListarFragment extends Fragment {

    private int opcionSeleccionada;

    private LinearLayout containerLista;
    private TextView txtInfoMateria;
    private View barraInfoEdicion;
    private Button ultimoBotonSeleccionado;

    int grisClaro = Color.parseColor("#E5E5E5");
    int grisMedio = Color.parseColor("#E0E0E0");
    int grisOscuro = Color.parseColor("#9E9E9E");
    int azul = Color.parseColor("#AFCBFF");
    int verde = Color.parseColor("#57FF97");
    int amarillo = Color.parseColor("#FFEB97");
    int naranja = Color.parseColor("#FFCBC1");
    int blanco = Color.parseColor("#FFFFFF");
    int negro = Color.parseColor("#000000");
    int grisLetra = Color.parseColor("#666666");

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

        // GET ID LISTA
        containerLista = view.findViewById(R.id.containerLista);

        // get
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
        // Limpiar contenedor
        containerLista.removeAllViews();

        // buscar las materias (o inscripciones) al back, segun corresponda
        if (opcionSeleccionada == 0) {
            MainActivity.buscarInscripciones();
        } else {
            MainActivity.buscarMaterias();
        }

        // Crear losniveles y añadirlos al containerLista
        for (int i = 1; i <= MainActivity.NIVELES_NOMBRE.length; i++) {
            LinearLayout nuevoNivel = crearGrupoNivel(i);
            containerLista.addView(nuevoNivel);
        }
    }

    private LinearLayout crearGrupoNivel(int nivel) {

        // Contenedor principal del grupo
        LinearLayout grupoLayout = new LinearLayout(requireContext());
        grupoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        grupoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams marginParams = (LinearLayout.LayoutParams) grupoLayout.getLayoutParams();
        marginParams.setMargins(0, 0, 0, dpToPx(8));
        grupoLayout.setLayoutParams(marginParams);

        // buscar datos filtrados
        List<String[]> filtrado = MainActivity.filtrarNivel(nivel);

        // crear primero contenido y luego header
        LinearLayout nuevoContenido;
        LinearLayout nuevoHeader;

        // si al filtrar mateias, hay materias en ese nivel, crea HEADER y CONTENIDO
        if (!filtrado.isEmpty()){
            nuevoContenido = crearContenido(filtrado, true);
            nuevoHeader = crearHeader(nivel, filtrado.size(), nuevoContenido, true);
        }
        // sino, crea solo HEADER
        else{
            List<String[]> noContenido = new ArrayList<>();
            // [nivel, orden, nombreMateria, sigla, condicion, nota]
            noContenido.add(new String[]{"","","","","",""});
            nuevoContenido = crearContenido(noContenido, false);
            nuevoHeader = crearHeader(nivel, 0, nuevoContenido, false);
        }

        // Agregar primero header y y luego contenido al grupo
        grupoLayout.addView(nuevoHeader);
        grupoLayout.addView(nuevoContenido);

        // Agregar grupo al container principal
        return grupoLayout;
    }

    public LinearLayout crearHeader(int nivel, int cantidadMaterias, LinearLayout contenidoMostrar, boolean hayMaterias ){
        LinearLayout header = new LinearLayout(requireContext());
        header.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
        header.setBackgroundColor(grisMedio);
        header.setClickable(true);
        header.setFocusable(true);

        // TextView del nivel
        TextView tvNivel = new TextView(requireContext());
        LinearLayout.LayoutParams nivelParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        tvNivel.setLayoutParams(nivelParams);

        String nombreHeader = MainActivity.NIVELES_NOMBRE[nivel-1] + " (" + cantidadMaterias + ")";

        tvNivel.setText(nombreHeader);
        tvNivel.setTextSize(16);
        tvNivel.setTypeface(null, Typeface.BOLD);
        tvNivel.setTextColor(negro);
        // Color.parseColor("#333333")

        // TextView del indicador (flecha)
        TextView tvIndicador = new TextView(requireContext());
        tvIndicador.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        tvIndicador.setText("▼");
        tvIndicador.setTextSize(14);
        tvIndicador.setTextColor(grisLetra);

        header.addView(tvNivel);
        header.addView(tvIndicador);

        // Click listener para colapsar/expandir
        header.setOnClickListener(v -> {
            if (contenidoMostrar.getVisibility() == View.VISIBLE) {
                contenidoMostrar.setVisibility(View.GONE);
                tvIndicador.setText("▼");
            } else {
                contenidoMostrar.setVisibility(View.VISIBLE);
                tvIndicador.setText("▲");
            }
        });

        // expandir, si hay materias
        if (hayMaterias){
            header.performClick();
        }

        return header;
    }

    public LinearLayout crearContenido(List<String[]> datos, boolean hayMaterias){

        //layout principal
        LinearLayout contenido = new LinearLayout(requireContext());
        contenido.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setPadding(dpToPx(0), dpToPx(8), dpToPx(0), dpToPx(8));
        contenido.setBackgroundColor(Color.parseColor("#F5F5F5"));
        contenido.setVisibility(View.GONE);

        // Agregar cada dato al contenido
        for (String[] dato : datos) {

            // layout por item
            LinearLayout itemDato = new LinearLayout(requireContext());
            itemDato.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            itemDato.setOrientation(LinearLayout.HORIZONTAL);
            itemDato.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

            // TextView clave
            TextView tvClave = new TextView(requireContext());
            LinearLayout.LayoutParams claveParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            tvClave.setLayoutParams(claveParams);
            tvClave.setTextSize(14);

            // TextView valor
            TextView tvValor = new TextView(requireContext());
            tvValor.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tvValor.setTextSize(14);
            tvValor.setTypeface(null, Typeface.BOLD);
            tvValor.setTextColor(Color.parseColor("#000000"));

            // si hay materias setea datos de materia
            if (hayMaterias){
                tvClave.setText(dato[1] + ". " + dato[2]);
                tvClave.setTextColor(negro);
                itemDato.setBackgroundColor(colorCondicion(dato[4]));
                tvValor.setText(dato[5]);
            }
            else {
                tvClave.setText("sin_materias");
                tvClave.setTextColor(blanco);
                itemDato.setBackgroundColor(grisOscuro);
            }

            // insetrat clave y valor
            itemDato.addView(tvClave);
            itemDato.addView(tvValor);

            //insertar materia
            contenido.addView(itemDato);
        }
        return contenido;
    }

    int colorCondicion(String condicion){
        int c = grisClaro; // NoDisponible
        switch (condicion) {
            case "R": c = verde; break; // Regular
            case "A": c = azul; break; // Aprobado
            case "I": c = amarillo; break; // Inscripto
            case "D": c = blanco; break; // Disponible
        }
        return c;
    }

    // Función auxiliar para convertir dp a pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void seleccionarMateria(int indice, String nombre, Button btn, String[] materia) {
        MainActivity.setMateriaIndiceActual(indice);

        txtInfoMateria.setText(" - " + nombre);
        barraInfoEdicion.setVisibility(View.VISIBLE);

        if (ultimoBotonSeleccionado != null) {
            ultimoBotonSeleccionado.setPadding(0, 0, 0, 0);
        }

        btn.setPadding(8, 8, 8, 8);
        ultimoBotonSeleccionado = btn;
    }

    private void mostrarInfoMateria() {

    }

    private void editarMateria() {

    }

    private String truncarCadena(String cadena, int limite){
        if (cadena.length() > limite) {
            return cadena.substring(0, limite - 4) + "...";
        }
        return cadena;
    }
}