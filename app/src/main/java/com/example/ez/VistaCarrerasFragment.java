package com.example.ez;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

import com.example.ez.domain.Especialiad;
import com.example.ez.domain.InfoInscripcion;

public class VistaCarrerasFragment extends Fragment {

    LinearLayout containerCarreras;

    public static VistaCarrerasFragment newInstance(boolean carreras) {
        VistaCarrerasFragment fragment = new VistaCarrerasFragment();
        Bundle args = new Bundle();
        args.putBoolean("carreras", carreras);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_carreras, container, false);
        containerCarreras = view.findViewById(R.id.containerCarreras);

        boolean carreras = getArguments().getBoolean("carreras");

        if (carreras){
            generarListaCarreras();
        }
        else{
            generarListaInfos();
        }

        return view;
    }

    private void generarListaCarreras(){
        // Crear botones para cada carrera
        String [][] nombresCarrera = Backend.getNombreLetraCarreras();

        for (int i = 0; i < nombresCarrera.length; i++) {
            Button btnCarrera = nuevoBoton(
                    nombresCarrera[i][0] + " (" + nombresCarrera[i][1] + ")",
                    16, 0, false
            );
            int finalI = i;
            btnCarrera.setOnClickListener(v -> mostrarConfirmacionCarrera(
                    nombresCarrera[finalI][0],
                    nombresCarrera[finalI][1].charAt(0)
            ));
            containerCarreras.addView(btnCarrera);
        }
    }

    private void generarListaInfos(){
        // Crear botones para cada carrera
        InfoInscripcion[] infos = MainActivity.getInfosInscripcion();

        for (int i = 0; i < infos.length; i++) {
            Button btnInfo = nuevoBoton(
                    Especialiad.fromLetra(infos[i].getLetraCarrera()).name() + "." + infos[i].getIdAlumno(),
                    16, 0, false
            );
            int finalI = i;

            btnInfo.setOnClickListener(v -> mostrarConfirmacionInscripcion(infos[finalI]));

            containerCarreras.addView(btnInfo);
        }
    }


    private void mostrarConfirmacionCarrera(String nombreCarrera, char letraCarrera) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Carrera")
                .setMessage(nombreCarrera)
                .setPositiveButton("Sí", (dialog, which) -> {
                    ((MainActivity) getActivity()).seleccionarCarrera(letraCarrera);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void mostrarConfirmacionInscripcion(InfoInscripcion info) {
        String msj = "Carrera: " + Especialiad.fromLetra(info.getLetraCarrera())
            + "\n" + "Regulares: " + info.getRegulares()
            + "\n" + "Aprobadas: " + info.getAprobadas()
            + "\n" + "Inscriptas: " + info.getInscriptas();

        new AlertDialog.Builder(getContext())
            .setTitle("Confirmar Alumno")
            .setMessage(msj)
            .setPositiveButton("Sí", (dialog, which) -> {
                ((MainActivity) getActivity()).seleccionarAlumno(
                        info.getLetraCarrera(),info.getIdAlumno());
            })
            .setNegativeButton("No", null)
            .show();
    }

    // GRAFICO GENERICO

    Button nuevoBoton (String text, int tamanoTexto, int colorFondo,  boolean bold){
        Button btn = new Button(requireContext());
        btn.setTextSize(tamanoTexto);
        btn.setText(text);
        if (bold){btn.setTypeface(null, Typeface.BOLD);}
        else {btn.setTypeface(null, Typeface.NORMAL);}

        // borde y fondo
        btn.setPadding(4,4,4,4);
        if(colorFondo != 0){btn.setBackgroundColor(colorFondo);}
        return btn;
    }
}