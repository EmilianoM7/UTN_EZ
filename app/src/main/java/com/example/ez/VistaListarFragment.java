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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ez.domain.Condicion;
import com.example.ez.domain.Inscripcion;
import com.example.ez.domain.Materia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VistaListarFragment extends Fragment {

    private boolean todas;

    private LinearLayout containerLista;

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
    String separadorInfo = "   •   ";

    // ENV
    MainActivity mainActivity;
    Materia materiaActual;

    public static VistaListarFragment newInstance(boolean todas) {
        VistaListarFragment fragment = new VistaListarFragment();
        Bundle args = new Bundle();
        args.putBoolean("todas", todas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_listar, container, false);

        if (getArguments() != null) {
            todas = getArguments().getBoolean("todas");
        }
        // titulo
        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        txtTitulo.setText("Lista de " + ( todas ? "Materias" : "Inscripciones" ) );
        // boton back
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        //boton opciones
        Button btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> { accionBotonOpciones();});

        // get ID container principal de la lista
        containerLista = view.findViewById(R.id.containerLista);

        actualizarLista();
        return view;
    }

    public void actualizarLista() {
        // Limpiar contenedor
        containerLista.removeAllViews();

        // Crear los niveles y añadirlos al containerLista
        for (int i = 1; i <= Backend.getNombresNiveles().length; i++) {
            LinearLayout nuevoNivel = crearGrupoNivel(i);
            containerLista.addView(nuevoNivel);
        }
    }

    private LinearLayout crearGrupoNivel(int nivel) {

        // Contenedor principal del grupo
        LinearLayout grupoLayout = nuevoLinar(true, Color.WHITE, 0, 0);
        setMargins(grupoLayout, 0, 0, 0, 8);

        // buscar datos filtrados
        List<Materia> filtrado = MainActivity.getMateriasPorNivel(nivel, todas);

        // crear primero contenido y luego header
        LinearLayout nuevoContenido;
        LinearLayout nuevoHeader;

        // si al filtrar mateias, hay materias en ese nivel, crea HEADER y CONTENIDO
        if (!filtrado.isEmpty()){
            nuevoContenido = crearContenido(filtrado, true);
            nuevoHeader = crearHeader(nivel, filtrado.size(), nuevoContenido, true);
        }
        // sino, crea solo HEADER y "no_materias"
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

        LinearLayout header = nuevoLinar(false, grisMedio, 8, 8);

        // TextView del nivel
        String nombreHeader = Backend.getNombresNiveles()[nivel-1] + " (" + cantidadMaterias + ")";
        TextView tvNivel = nuevoTexto(nombreHeader, 16, Color.BLACK,0, true, false, true);

        // TextView del indicador (flecha)
        TextView tvIndicador = nuevoTexto("▼", 14, grisLetra, 0, false, false, false);

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
        LinearLayout layoutContenido = nuevoLinar(true,0,8,8);

        // colapsar layoutContenido por defecto
        layoutContenido.setVisibility(View.GONE);

        // Creat y agregar cada dato al layoutContenido
        for (Materia mat : materias) {

            // layout por item
            LinearLayout layoutItem = nuevoLinar(false, 0, 4, 6);

            // TextView clave
            TextView tvNombreMateria = nuevoTexto("", 14, Color.BLACK, 0, false, false, true);

            // TextView valor
            TextView tvNota = nuevoTexto("", 16, Color.BLACK, 0, true, false, false);

            // si hay materias setea materias de materia
            int color;
            if (hayMaterias){
                // orden - nombre
                tvNombreMateria.setText(ordenElectiva(mat.getOrden()) + mat.getNombre());
                tvNombreMateria.setTextColor(Color.BLACK);
                color = colorCondicion(mat.getLetraCodicion());
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
                color = grisOscuro;
            }

            // insetrat clave y valor
            layoutItem.addView(tvNombreMateria);
            layoutItem.addView(tvNota);

            //estilo
            layoutItem.setBackground(nuevoFondo(color,1,Color.GRAY,8));
            setMargins(layoutItem, 0, 0, 0, 4);

            //insertar materia
            layoutContenido.addView(layoutItem);
        }
        return layoutContenido;
    }

    // ACCION

    private void accionBotonOpciones() {
        AlertDialog dialog1 = nuevoDialogo("Menu opciones");
        // todo realizar accion
        dialog1.show();
    }

    private void accionSeleccionarMateria(Materia mat) {
        AlertDialog dialog1 = nuevoDialogo("");
        // Layout ppal inf
        LinearLayout layoutDialogo = nuevoLinar(true,0,8,4);
        // cuadro titulo materia
        layoutDialogo.addView(nuevoCuadroTituloMateria(mat));
        // cuadro info materia
        if (mat.getInscripcion() != null){
            layoutDialogo.addView(nuevoCuadroDetallesInscripcion(mat));
        }
        // cuadro correlativas reg
        Materia[] regulares = MainActivity.getMateriasPorOrden(mat.getCorrelativasReg());
        if (regulares != null){
            layoutDialogo.addView(nuevoLayoutCorrelativas(regulares, "<< Correlativas Regulares (R-A)", verde, verdeLetra));
        }
        // cuadro correlativas apr
        Materia[] aprobadas = MainActivity.getMateriasPorOrden(mat.getCorrelativasAp());
        if (aprobadas != null){
            layoutDialogo.addView(nuevoLayoutCorrelativas(aprobadas, "<< Correlativas Aprobadas (A)", azul, azulLetra));
        }
        // cuadro materias que libera Reg
        Materia[] liberadasReg = Backend.materiasQueLibera(mat.getOrden(),'R');
        if (liberadasReg.length > 0){
            layoutDialogo.addView(nuevoLayoutCorrelativas(liberadasReg, "Estando Regular, libera >>", 0, grisLetra));
        }
        // cuadro materias que libera Apr
        Materia[] liberadasApr = Backend.materiasQueLibera(mat.getOrden(),'A');
        if (liberadasApr.length > 0) {
            layoutDialogo.addView(nuevoLayoutCorrelativas(liberadasApr, "Estando Aprobada, libera >>", 0, grisLetra));
        }

        //
        ScrollView scroll = new ScrollView(requireContext());
        scroll.addView(layoutDialogo);
        dialog1.setView(scroll);
        dialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "Editar", (dialog, which) -> accionEditarMateria(mat));
        dialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", (dialog, which) -> {});
        dialog1.show();
    }

    private void accionEditarMateria(Materia mat) {
        AlertDialog dialog1 = nuevoDialogo("EDITAR");
        // Layout info
        LinearLayout layoutDialogo = nuevoLinar(true,0,8,4);
        layoutDialogo.addView(nuevoCuadroTituloMateria(mat));
        // - layoutEditar
        LinearLayout layoutEditar = nuevoLinar(true,0,4,8);
        // editar condicion
        layoutEditar.addView(nuevoTexto(
                "Condicion:",
                16, Color.BLACK, grisMedio, true, false, false
        ));
        Condicion[] condiciones = Condicion.values();
        int[] coloresCondicion = new int[condiciones.length];
        for (int i = 0; i < coloresCondicion.length; i++) {
            coloresCondicion[i] = colorCondicion(condiciones[i].getLetra());
        }
        Spinner spn_condicion = nuevoSpiner(
                Backend.getNombresCondiciones(),
                coloresCondicion,
                mat.getOrdenCondicionActual()
        );
        layoutEditar.addView(spn_condicion);
        // editar nota
        layoutEditar.addView(nuevoTexto(
                "Nota:",
                16, Color.BLACK, grisMedio, true, false, false
        ));
        Spinner spn_nota = nuevoSpiner(
                Backend.getNotasString(),
                null,
                mat.getNotaInscripcion()
        );
        layoutEditar.addView(spn_nota);
        layoutEditar.setBackground(nuevoFondo(0,2,grisOscuro,8));
        // -
        layoutDialogo.addView(layoutEditar);
        dialog1.setView(layoutDialogo);
        if (mat.getInscripcion() != null){
            dialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "Borrar", (dialog, which) ->
                    accionBorrarInscripcion(mat)
            );
        }
        dialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", (dialog, which) -> {});
        dialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", (dialog, which) ->
            accionAceptarEdicion(
                mat,
                spn_condicion.getSelectedItemPosition(),
                Integer.parseInt(spn_nota.getSelectedItem().toString())
            )
        );

        dialog1.show();
    }

    private void accionAceptarEdicion(Materia mat, int indiceCondicion, int nota){
        // si la materia tiene inscripcion
        boolean guardado;
        boolean actualizar;
        if (mat.getInscripcion() != null){
            // si hay cambios
            if(mat.getOrdenCondicionActual() != indiceCondicion || mat.getNotaInscripcion() != nota){
                // guardar Inscripcion
                Logger.log("aceptarEdicion-cambios: "
                        + mat.getOrdenCondicionActual() + " > " + indiceCondicion
                        + " - " + mat.getNotaInscripcion() + " > " + nota);
                guardado = guardarInscripcion(mat, indiceCondicion, nota);
                actualizar = true;
            }
            // si no hay cambios
            else {
                guardado = true;
                actualizar = false;
            }
        }
        // si no tiene inscripcion
        else{
            // guardar inscripcion
            guardado = guardarInscripcion(mat, indiceCondicion, nota);
            actualizar = true;
        }
        mostrarMensajeConfirmacionEdicion("guardar",guardado, actualizar);
    }

    private void accionBorrarInscripcion(Materia mat){
        new AlertDialog.Builder(getContext())
                .setTitle("Borrar:")
                .setMessage("Inscripcion a " + mat.getNombre())
                .setPositiveButton("Sí", (dialog, which) -> {
                    eliminarInscripcion(mat);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminarInscripcion(Materia mat){
        boolean borrado = Backend.eliminarInscripcion(
                this.getContext(),
                mat.getInscripcion(),
                MainActivity.getCarreraActual(),
                MainActivity.getIdAlumno()
        );
        mostrarMensajeConfirmacionEdicion("borrar", borrado, true);
    }

    private boolean guardarInscripcion(Materia mat, int indiceCondicion, int nota){
        Logger.log("guardarInscripcion: " + mat.getOrden() + " - " + indiceCondicion + " - " + nota);
        return Backend.guardarInscripcion(
                this.getContext(),
                new Inscripcion(
                        mat.getOrden(),
                        mat.getAnoInscripcion(),
                        nota,
                        mat.getNombreComision(),
                        Condicion.values()[indiceCondicion]
                ),
                MainActivity.getCarreraActual(),
                MainActivity.getIdAlumno()
        );
    }

    private void mostrarMensajeConfirmacionEdicion(String accion, boolean guardado, boolean actualizar){
        String msj = (guardado) ? "Exito al " : "Error al ";
        msj += accion;
        AlertDialog dialogGuardado = nuevoDialogo(msj);

        dialogGuardado.show();

        if (actualizar){
            MainActivity.actualizarInscripicones(this.getContext());
            actualizarLista();
        }
    }

    // GRAFICO GENERICO

    LinearLayout nuevoCuadroTituloMateria(Materia mat){
        LinearLayout layoutTitulo = nuevoLinar(false,0,8,8);
        layoutTitulo.addView(nuevoTexto(
                ordenElectiva(mat.getOrden()) + mat.getNombre(),
                20, Color.BLACK, 0, false, false, true
        ));
        if(mat.getInscripcion() != null){
            layoutTitulo.addView(nuevoTexto(
                    mat.getNotaInscripcion() + "",
                    16, Color.BLACK, 0, true, false, false
            ));
        }
        layoutTitulo.setBackground(nuevoFondo(
                colorCondicion(mat.getLetraCodicion()),
                2,Color.BLACK,8));
        setMargins(layoutTitulo, 0, 8, 0, 8);
        return layoutTitulo;
    }

    LinearLayout nuevoCuadroDetallesInscripcion(Materia mat){
        LinearLayout layoutDetalles = nuevoLinar(true,0,0,8);

        String detalles =
                mat.getNombreCondicion() + separadorInfo +
                mat.getNotaInscripcion() + separadorInfo +
                mat.getNombreComision() + separadorInfo +
                mat.getAnoInscripcion();

        layoutDetalles.addView(nuevoTexto(detalles, 16, Color.BLACK, 0, false, true, false));

        layoutDetalles.setBackground(nuevoFondo(0,2,Color.BLACK,8));
        setMargins(layoutDetalles, 0, 0, 0, 8);
        return layoutDetalles;
    }

    LinearLayout nuevoLayoutCorrelativas(Materia[] materias, String titulo, int colorFondo, int colorBorde){
        LinearLayout layoutCorrelativas = nuevoLinar(true,0,4,8);
        TextView txtReg = nuevoTexto(titulo, 16, Color.BLACK, 0, true, true, false);
        layoutCorrelativas.addView(txtReg);

        for (Materia mat : materias) {
            LinearLayout linearMateria = nuevoLinar(false, 0, 0, 0);
            linearMateria.addView(nuevoTexto(
                    ordenElectiva(mat.getOrden()) + mat.getNombre(),
                    14, Color.BLACK, 0, false, false, true
            ));
            linearMateria.addView(nuevoTexto(
                    "" + mat.getLetraCodicion(),
                    14, Color.BLACK, 0, true, false, false
            ));
            linearMateria.setBackgroundColor(colorCondicion(mat.getLetraCodicion()));
            layoutCorrelativas.addView(linearMateria);
        }

        layoutCorrelativas.setBackground(nuevoFondo(colorFondo,2,colorBorde,8));
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

    Button nuevoBoton (String text, int tamanoTexto, int colorFondo,  boolean bold){
        Button btn = new Button(requireContext());
        btn.setTextSize(tamanoTexto);
        btn.setText(text);
        if (bold){btn.setTypeface(null, Typeface.BOLD);}
        else {btn.setTypeface(null, Typeface.NORMAL);}

        // borde y fondo
        btn.setPadding(4,4,4,4);
        btn.setBackground(nuevoFondo(colorFondo,1,Color.LTGRAY,8));
        //agregarMargenBoton(btn,4);
        return btn;
    }
    
    TextView nuevoTexto(String texto, int tamano, int colorTexto, int colorFondo,
                        boolean bold, boolean centrado, boolean ajustarAncho){
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
        if (colorFondo != 0){
            txt.setBackgroundColor(colorFondo);
        }
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

    private Spinner nuevoSpiner(String[] valores, int[] colores, int seleccionDefault){
        // spinner
        Spinner spinner = new Spinner(getContext());
        spinner.setId(View.generateViewId());
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        spinner.setLayoutParams(spinnerParams);
        //spinner.setBackgroundColor(Color.WHITE);
        //spinner.setPadding(4,4,4,4);
        // opciones

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),android.R.layout.simple_spinner_item, valores)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                textView.setTextColor(Color.BLACK);
                textView.setBackgroundColor(colores == null ? Color.WHITE : colores[position]);
                textView.setPadding(16, 16, 16, 16);

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                textView.setTextColor(Color.BLACK);
                textView.setBackgroundColor(colores == null ? Color.WHITE : colores[position]);
                textView.setPadding(16, 16, 16, 16);

                return view;
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(seleccionDefault);
        // accion
        return spinner;
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

    private void agregarMargenBoton(Button btn, int margen){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
        params.setMargins(margen,margen,margen,margen);
        btn.setLayoutParams(params);
    }

    private String ordenElectiva(int orden){
        return orden <= 99 ? orden + ". " : "(E). ";
    }

}