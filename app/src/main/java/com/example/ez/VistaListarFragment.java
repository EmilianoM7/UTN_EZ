package com.example.ez;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.ez.domain.Materia;

import java.util.ArrayList;
import java.util.List;

public class VistaListarFragment extends Fragment {

    private int opcionSeleccionada;

    private LinearLayout containerLista;
    private TextView txtInfoMateria;
    private View barraInfoEdicion;

    int grisClaro = Color.parseColor("#E5E5E5");
    int grisMedio = Color.parseColor("#E0E0E0");
    int grisOscuro = Color.parseColor("#9E9E9E");
    int azul = Color.parseColor("#AFCBFF");
    int azulLetra = Color.parseColor("#3465a4");
    int verde = Color.parseColor("#77bc65");
    int verdeLetra = Color.parseColor("#0c4d16");
    int amarillo = Color.parseColor("#ffd428");
    int naranja = Color.parseColor("#ffb66c");
    int grisLetra = Color.parseColor("#666666");

    // ENV
    Materia materiaActual;

    public static VistaListarFragment newInstance(int opcion) {
        VistaListarFragment fragment = new VistaListarFragment();
        Bundle args = new Bundle();
        args.putInt("opcion", opcion);
        fragment.setArguments(args);
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

        Button btnInfoMateria = view.findViewById(R.id.btnInfo);
        btnInfoMateria.setOnClickListener(v -> accionMostrarInfoMateria());

        Button btnEditarMateria = view.findViewById(R.id.btnEditar);
        btnEditarMateria.setOnClickListener(v -> accionEditarMateria());

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
        // ocultar barra de infoMateria
        barraInfoEdicion.setVisibility(View.GONE);
    }

    private LinearLayout crearGrupoNivel(int nivel) {

        // Contenedor principal del grupo
        LinearLayout grupoLayout = nuevoLinar(true, Color.WHITE, 0, 0);
        setMargins(grupoLayout, 0, 0, 0, 8);

        // buscar datos filtrados
        List<Materia> filtrado = MainActivity.filtrarNivel(nivel);

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
            // crear contenido vacio
            List<Materia> vacio = new ArrayList<>();
            vacio.add(new Materia());

            nuevoContenido = crearContenido(vacio, false);
            nuevoHeader = crearHeader(nivel, 0, nuevoContenido, false);
        }

        // Agregar primero header y y luego contenido al grupo
        grupoLayout.addView(nuevoHeader);
        grupoLayout.addView(nuevoContenido);

        // Agregar grupo al container principal
        return grupoLayout;
    }

    public LinearLayout crearHeader(int nivel, int cantidadMaterias, LinearLayout contenidoMostrar, boolean hayMaterias ){

        LinearLayout header = nuevoLinar(false, grisMedio, 12, 12);

        // TextView del nivel
        String nombreHeader = MainActivity.NIVELES_NOMBRE[nivel-1] + " (" + cantidadMaterias + ")";
        TextView tvNivel = nuevoTexto(nombreHeader, 16, Color.BLACK, true, false, true);

        // TextView del indicador (flecha)
        TextView tvIndicador = nuevoTexto("▼", 14, grisLetra, false, false, false);

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

        // expandir si contiene materias
        if (hayMaterias){
            header.performClick();
        }
        return header;
    }

    public LinearLayout crearContenido(List<Materia> materias, boolean hayMaterias){

        //layout principal
        LinearLayout layoutContenido = nuevoLinar(true,0,0,8);

        // colapsar layoutContenido por defecto
        layoutContenido.setVisibility(View.GONE);

        // Creat y agregar cada dato al layoutContenido
        for (Materia mat : materias) {

            // layout por item
            LinearLayout layoutItem = nuevoLinar(false, 0, 8, 8);

            // TextView clave
            TextView tvNombreMateria = nuevoTexto("", 14, Color.BLACK, false, false, true);

            // TextView valor
            TextView tvNota = nuevoTexto("", 14, Color.BLACK, true, false, false);

            // si hay materias setea materias de materia
            if (hayMaterias){
                // orden - nombre
                tvNombreMateria.setText(ordenElectiva(mat.getOrden()) + mat.getNombre());
                tvNombreMateria.setTextColor(Color.BLACK);
                layoutItem.setBackgroundColor(colorCondicion(mat.getLetraCodicion()));
                // nota
                String nota = mat.getNotaInscripcion() == 0 ? "" : "" + mat.getNotaInscripcion();
                tvNota.setText(nota);
                // accion seleccionar materia
                layoutItem.setOnClickListener(v -> {
                    accionSeleccionarMateria(mat);
                });
            }
            else {
                tvNombreMateria.setText("sin_materias");
                tvNombreMateria.setTextColor(Color.WHITE);
                layoutItem.setBackgroundColor(grisOscuro);
            }

            // insetrat clave y valor
            layoutItem.addView(tvNombreMateria);
            layoutItem.addView(tvNota);

            //insertar materia
            layoutContenido.addView(layoutItem);
        }
        return layoutContenido;
    }

    int colorCondicion(char condicion){
        int c = Color.WHITE; // NoDisponible
        switch (condicion) {
            case 'A': c = azul; break; // Aprobado
            case 'R': c = verde; break; // Regular
            case 'D': c = amarillo; break; // Disponible
            case 'I': c = naranja; break; // Inscripto
        }
        return c;
    }

    // ACCION

    private void accionSeleccionarMateria(Materia mat) {
        materiaActual = mat;
        String infoMateria = (ordenElectiva(mat.getOrden()) + mat.getNombre());
        txtInfoMateria.setText(infoMateria);
        txtInfoMateria.setTextColor(Color.BLACK);
        txtInfoMateria.setTypeface(null, Typeface.BOLD);
        barraInfoEdicion.setVisibility(View.VISIBLE);
    }

    private void accionMostrarInfoMateria() {

        AlertDialog dialog1 = nuevoDialogo("");

        // Layout info
        LinearLayout layoutDialogo = nuevoLinar(true,0,8,4);

        layoutDialogo.addView(nuevoLayoutTituloMateria(
                ordenElectiva(materiaActual.getOrden()) + materiaActual.getNombre(),
                colorCondicion(materiaActual.getLetraCodicion())
        ));

        layoutDialogo.addView(nuevoLayoutInfoMateria(
                materiaActual.getNumeroNivel(),
                materiaActual.getNombreEspecialidad(),
                materiaActual.getPuntos()
        ));

        Materia[] regulares = MainActivity.getMateriasOrden(materiaActual.getCorrelativasReg());
        if (regulares != null){
            layoutDialogo.addView(nuevoLayoutCorrelativas(regulares, false));
        }

        Materia[] aprobadas = MainActivity.getMateriasOrden(materiaActual.getCorrelativasAp());
        if (aprobadas != null){
            layoutDialogo.addView(nuevoLayoutCorrelativas(aprobadas, true));
        }

        dialog1.setView(layoutDialogo);

        dialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", (dialog, which) -> {});

        dialog1.show();
    }

    private void accionEditarMateria() {

    }

    // GRAFICO GENERICO

    LinearLayout nuevoLayoutTituloMateria(String titulo, int colorFondo){
        LinearLayout layoutTitulo = nuevoLinar(true,0,4,8);
        layoutTitulo.addView(nuevoTexto(
                titulo,
                20, Color.BLACK, true, true, false
        ));
        layoutTitulo.setBackground(nuevoFondo(colorFondo,2,Color.BLACK,8));
        setMargins(layoutTitulo, 0, 8, 0, 8);
        return layoutTitulo;
    }

    LinearLayout nuevoLayoutInfoMateria(int nivel, String especialidad, int puntos){
        LinearLayout layoutInfo = nuevoLinar(true,0,4,8);
        layoutInfo.addView(nuevoTexto(
                "Nivel: " + nivel,
                16, Color.BLACK, false, false, false
        ));
        layoutInfo.addView(nuevoTexto(
                "Especialidad: " + especialidad,
                16, Color.BLACK, false, false, false
        ));
        if (puntos != 0) {
            layoutInfo.addView(nuevoTexto(
                    "Puntos (electiva): " + puntos,
                    16, Color.BLACK, false, false, false
            ));
        }
        layoutInfo.setBackground(nuevoFondo(0,2,grisOscuro,8));
        setMargins(layoutInfo, 0, 0, 0, 8);
        return layoutInfo;
    }

    LinearLayout nuevoLayoutCorrelativas(Materia[] materias, boolean aprobadas){
        LinearLayout layoutCorrelativas = nuevoLinar(true,0,4,8);
        String titulo =  "Correlativas ";
        titulo += aprobadas ? "Aprobadas (A):" : "Regulares (R):";
        TextView txtReg = nuevoTexto(titulo, 16, Color.BLACK, true, true, false);
        layoutCorrelativas.addView(txtReg);

        for (Materia mat : materias) {
            LinearLayout linearMateria = nuevoLinar(false, 0, 0, 0);
            linearMateria.addView(nuevoTexto(
                    ordenElectiva(mat.getOrden()) + mat.getNombre(),
                    14, Color.BLACK, false, false, true
            ));
            linearMateria.addView(nuevoTexto(
                    "" + mat.getLetraCodicion(),
                    14, Color.BLACK, true, false, false
            ));
            linearMateria.setBackgroundColor(colorCondicion(mat.getLetraCodicion()));
            layoutCorrelativas.addView(linearMateria);
        }

        layoutCorrelativas.setBackground(nuevoFondo(0,2,grisOscuro,8));
        setMargins(layoutCorrelativas, 0, 0, 0, 8);

        return layoutCorrelativas;
    }

    LinearLayout nuevoLinar (boolean vertical, int colorFondo, int paddingH, int paddingV) {
        LinearLayout linear = new LinearLayout(requireContext());

        // Asignar LayoutParams al LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linear.setLayoutParams(params);

        if (vertical) {linear.setOrientation(LinearLayout.VERTICAL);}
        else {linear.setOrientation(LinearLayout.HORIZONTAL);}

        if (colorFondo != 0){
            linear.setBackgroundColor(colorFondo);
        }
        linear.setGravity(Gravity.CENTER);
        linear.setPadding(dpToPx(paddingH), dpToPx(paddingV), dpToPx(paddingH), dpToPx(paddingV));

        return linear;
    }

    TextView nuevoTexto(String texto, int tamano, int colorTexto, boolean bold, boolean centrado, boolean ajustarAncho){
        TextView txt = new TextView(requireContext());

        // atrib
        txt.setText(texto);
        txt.setTextSize(tamano == 0 ? 16 : tamano);
        txt.setTextColor(Color.BLACK);
        if (bold) {
            txt.setTypeface(null, Typeface.BOLD);
        }
        if (centrado){
            txt.setGravity(Gravity.CENTER);
        }
        txt.setTextColor(colorTexto);
        txt.setPadding(12,4,12,4);

        if (ajustarAncho){
            LinearLayout.LayoutParams nivelParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            txt.setLayoutParams(nivelParams);
        }

        //return
        return txt;
    }

    AlertDialog nuevoDialogo(String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(titulo);
        AlertDialog dialog = builder.create();

        // estilo
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(nuevoFondo(grisClaro,1,Color.BLACK,20));
        }

        return dialog;
    }

    GradientDrawable nuevoFondo (int colorFondo, int borde, int colorBorde, int radio){
        GradientDrawable drawable = new GradientDrawable();
        if (colorFondo != 0){
            drawable.setColor(colorFondo);
        }
        drawable.setCornerRadius(radio);
        drawable.setStroke(borde, colorBorde);
        return drawable;
    }

    // AUX

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private String truncarCadena(String cadena, int limite){
        if (cadena.length() > limite) {
            return cadena.substring(0, limite - 4) + "...";
        }
        return cadena;
    }

    public void setMargins(LinearLayout linearLayout, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        linearLayout.setLayoutParams(params);
    }

    private String ordenElectiva(int orden){
        return orden <= 99 ? orden + ". " : "(E). ";
    }

}