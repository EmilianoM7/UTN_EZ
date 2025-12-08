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

public class VistaHorariosFragment extends Fragment {

    private static final String[] DIAS = {"L", "M", "X", "J", "V", "S"};
    private static final int CELL_WIDTH = 95;
    private static final int CELL_HEIGHT = 70;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_horarios, container, false);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());

        LinearLayout containerHorarios = view.findViewById(R.id.containerHorarios);

        int[][] horario = Backend.simularHorario();

        // Crear fila de encabezados (días)
        LinearLayout filaEncabezado = new LinearLayout(getContext());
        filaEncabezado.setOrientation(LinearLayout.HORIZONTAL);
        filaEncabezado.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Celda vacía para la esquina
        TextView celdaEsquina = crearCelda("", Color.LTGRAY, true);
        filaEncabezado.addView(celdaEsquina);

        // Agregar días
        for (String dia : DIAS) {
            TextView celdaDia = crearCelda(dia, Color.LTGRAY, true);
            filaEncabezado.addView(celdaDia);
        }
        containerHorarios.addView(filaEncabezado);

        // Crear filas para cada módulo (1-17)
        for (int modulo = 0; modulo < horario[0].length; modulo++) {
            LinearLayout fila = new LinearLayout(getContext());
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Número de módulo
            TextView celdaModulo = crearCelda(String.valueOf(modulo + 1), Color.LTGRAY, true);
            fila.addView(celdaModulo);

            // Celdas de cada día
            for (int dia = 0; dia < horario.length; dia++) {
                int valor = horario[dia][modulo];
                int color;

                if (valor == 0) {
                    color = Color.WHITE;
                } else if (valor == 1) {
                    color = Color.parseColor("#2196F3"); // Azul
                } else {
                    color = Color.parseColor("#F44336"); // Rojo
                }

                TextView celda = crearCelda("", color, false);
                fila.addView(celda);
            }

            containerHorarios.addView(fila);
        }

        return view;
    }

    private int getColorSegunValor(int valor) {
        if (valor == 0) {
            return Color.WHITE;
        } else if (valor == 1) {
            return Color.parseColor("#2196F3"); // Azul
        } else {
            return Color.parseColor("#F44336"); // Rojo
        }
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