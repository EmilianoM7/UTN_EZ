package com.example.ez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class VistaInfoResumenFragment extends Fragment {

    private String tipo;
    private String[] datos;

    public static VistaInfoResumenFragment newInstance(String tipo, String[] datos) {
        VistaInfoResumenFragment fragment = new VistaInfoResumenFragment();
        Bundle args = new Bundle();
        args.putString("tipo", tipo);
        args.putStringArray("datos", datos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_info_resumen, container, false);

        if (getArguments() != null) {
            tipo = getArguments().getString("tipo");
            datos = getArguments().getStringArray("datos");
        }

        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        txtTitulo.setText(tipo);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());

        LinearLayout containerInfo = view.findViewById(R.id.containerInfo);

        if (tipo.equals("InfoCarrera")) {
            mostrarInfoCarrera(containerInfo);
        } else if (tipo.equals("ResumenCursada")) {
            mostrarResumenCursada(containerInfo);
        } else if (tipo.equals("InfoMateria")) {
            mostrarInfoMateria(containerInfo);
        }

        return view;
    }

    private void mostrarInfoCarrera(LinearLayout container) {
        // [letraCarrera, nombreCarrera, titulo, tituloMedio, horasCarrera, materiasCarrera, descripcionCarrera]
        agregarTexto(container, datos[1]  + " - " + datos[0], true);
        agregarTexto(container, "", false);
        agregarTexto(container, "Título: ", true);
        agregarTexto(container, datos[2], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Título Intermedio: ", true);
        agregarTexto(container, datos[3], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Horas totales: ", true);
        agregarTexto(container, datos[4], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Cantidad de materias: ", true);
        agregarTexto(container, datos[5], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Descripción:", true);
        agregarTexto(container, datos[6], false);
    }

    private void mostrarResumenCursada(LinearLayout container) {
        // [A, R, I, D, N, porcentaje, promedio, puntos, puntosNecesarios]
        agregarTexto(container, "Resumen de Cursada", true);
        agregarTexto(container, "", false);
        agregarTexto(container, "Materias", true);
        agregarTexto(container, "Aprobadas: " + datos[0], false);
        agregarTexto(container, "Regulares: " + datos[1], false);
        agregarTexto(container, "Inscriptas: " + datos[2], false);
        agregarTexto(container, "Disponibles: " + datos[3], false);
        agregarTexto(container, "No Disponibles: " + datos[4], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Porcentaje aprobado: ", true);
        agregarTexto(container, datos[5] + "%" + "\n(no estima las materias electivas)", false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Promedio general: ", true);
        agregarTexto(container, datos[6], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Puntos de electiva obtenidos: ", true);
        agregarTexto(container, datos[7] + "/" + datos[8], false);
    }

    private void mostrarInfoMateria(LinearLayout container) {
        // [orden, sigla, nombreMateria, descripcionMateria, programaMateria]
        agregarTexto(container, datos[0] + " - " + datos[1] + ":", true);
        agregarTexto(container, datos[2], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Descripción:", true);
        agregarTexto(container, datos[3], false);
        agregarTexto(container, "", false);
        agregarTexto(container, "Programa:", true);
        agregarTexto(container, datos[4], false);
    }

    private void agregarTexto(LinearLayout container, String texto, boolean bold) {
        TextView txt = new TextView(getContext());
        txt.setText(texto);
        txt.setTextSize(16);
        if (bold) {
            txt.setTypeface(null, android.graphics.Typeface.BOLD);
        }
        txt.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txt.getLayoutParams();
        params.setMargins(0, 8, 0, 8);
        txt.setLayoutParams(params);
        container.addView(txt);
    }
}