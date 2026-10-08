package com.example.ez;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.ez.backend.Backend;
import com.example.ez.backend.Logger;
import com.example.ez.domain.Materia;

import java.util.ArrayList;
import java.util.Arrays;

public class VistaHorariosFragment extends Fragment {

    // container
    private LinearLayout containerHorarios;
    private TextView[] celdasHorario;
    private Button btnMenu;
    private Button btnBack;

    private static final String[] DIAS = {"L", "M", "X", "J", "V", "S"};
    private static final int colorMateriaRepetida = Color.parseColor("#F6B6A6"); // Coral pastel
    private static final int[] coloresPastel = {
            Color.parseColor("#F4B8C5"), // Rosa pastel
            //Color.parseColor("#F6B6A6"), // Coral pastel
            Color.parseColor("#F8C9A4"), // Durazno
            Color.parseColor("#F5E3A1"), // Amarillo pastel
            Color.parseColor("#DDEB9A"), // Limón suave
            Color.parseColor("#B8D8B0"), // Verde pastel
            Color.parseColor("#A8DED0"), // Menta
            Color.parseColor("#9DD9D2"), // Turquesa pastel
            Color.parseColor("#A9D6E5"), // Celeste
            Color.parseColor("#AFC8E9"), // Azul pastel
            Color.parseColor("#B8BDEB"), // Azul lavanda
            Color.parseColor("#C8B6E8"), // Lavanda
            Color.parseColor("#D5B8E8"), // Lila
            //Color.parseColor("#E3B8D7"), // Rosa lavanda
            Color.parseColor("#E6C9A8"), // Beige
            Color.parseColor("#BCC9D6")  // Gris azulado
    };
    private static final int MODULOS = 25;
    private static final int CELL_WIDTH = 95;
    private static final int CELL_HEIGHT = 70;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_horarios, container, false);
        // back
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        // options
        btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> { accionBotonOpciones();});
        // container ppal
        this.containerHorarios = view.findViewById(R.id.containerHorarios);
        containerHorarios.setOrientation(LinearLayout.HORIZONTAL);

        generarTablaHorarios();
        Logger.logHorario(" * OK1");
        actualizarHorario();
        Logger.logHorario(" * OK2");
        return view;
    }

    private void generarTablaHorarios(){
        // Crear fila de encabezados (días)
        LinearLayout columnaModulos = nuevoLinear();
        // Celda vacía para la esquina
        TextView celdaEsquina = crearCelda("", Color.LTGRAY, true);
        columnaModulos.addView(celdaEsquina);
        // Agregar días
        int numeroModulo = 0;
        for (int i = 0; i < MODULOS; i++) {
            TextView celdaDia = crearCelda("" + numeroModulo, Color.LTGRAY, true);
            columnaModulos.addView(celdaDia);
        }
        containerHorarios.addView(columnaModulos);

        // crear matriz de solo celdas
        celdasHorario = new TextView[150];
        int indiceTabla = 0;
        for (int dia = 0; dia < DIAS.length; dia++) {
            LinearLayout columna = nuevoLinear();
            // primer celda - encabezado
            columna.addView(crearCelda(DIAS[dia], Color.LTGRAY, true));
            for (int modulo = 0; modulo < 25; modulo++) {
                // Celdas de cada MODULO de cada DIA, todas en blanco
                TextView celda = crearCelda("", Color.WHITE, false);
                // matriz de solo celdas
                celdasHorario[indiceTabla] = celda;
                indiceTabla++;
                // añadir
                columna.addView(celda);
            }
            containerHorarios.addView(columna);
        }
    }

    private void actualizarHorario(){
        // UseCase ArmarHorarios
        Materia[] inscriptas = Backend.obtenerSoloInscritas();
        Logger.logHorario(" - inscriptas: " + inscriptas.length);
        ArrayList<int[]> horario = Backend.armarHorarios(inscriptas);
        Logger.logHorario(" - materiasHorario: " + horario.size());
        // por cada inscripcion de "horario"
        for (int i = 0; i < horario.size(); i++) {
            Logger.logHorario(" - horariosMateria: " + Arrays.toString(horario.get(i)));
            for (int j = 0; j < horario.get(i).length; j++) {
                // toma los indices de horario ocupados
                Logger.logHorario(" - i.j: " + horario.get(i)[j]);
                setCelda(celdasHorario[horario.get(i)[j]],i,inscriptas[i].getOrden());
            }
        }
    }

    private void setCelda(TextView celda, int lugarInscripcion, int ordenMateria){
        // si la celda esta vacia
        if (celda.getText() == "") {
            celda.setBackgroundColor(coloresPastel[lugarInscripcion]);
            celda.setText("" + ordenMateria);
        }
        // si ya habia materia, lo pinta de rojo
        else{
            celda.setBackgroundColor(colorMateriaRepetida);
            celda.append("/" + ordenMateria);
        }
    }

    private void accionBotonOpciones(){

    }

    private LinearLayout nuevoLinear(){
        LinearLayout liear = new LinearLayout(getContext());
        liear.setOrientation(LinearLayout.VERTICAL);
        liear.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return liear;
    }

    private TextView crearCelda(String texto, int color, boolean esEncabezado) {
        TextView celda = new TextView(getContext());
        celda.setText(texto);
        celda.setBackgroundColor(color);
        celda.setGravity(Gravity.CENTER);
        celda.setLayoutParams(new LinearLayout.LayoutParams(CELL_WIDTH, CELL_HEIGHT));

        // Agregar borde
        celda.setPadding(2, 2, 2, 2);
        android.graphics.drawable.GradientDrawable border = new android.graphics.drawable.GradientDrawable();
        border.setColor(color);
        border.setStroke(1, Color.GRAY);
        celda.setBackground(border);

        if (esEncabezado) {
            celda.setTypeface(null, android.graphics.Typeface.BOLD);
            celda.setTextColor(Color.BLACK);
        }

        return celda;
    }

}