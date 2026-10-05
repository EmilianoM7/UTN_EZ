package com.example.ez;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    // 0) comprueba si hay alumnos guardados
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

    // 1) si no hay inscripicones, muestra las carreras a elegir
    private void generarListaCarreras(){
        // limpiar
        containerCarreras.removeAllViews();
        // Crear botones para cada carrera
        String [][] nombresCarrera = Backend.getNombreLetraCarreras();

        for (int i = 0; i < nombresCarrera.length; i++) {
            Button btnCarrera = nuevoBoton(
                    nombresCarrera[i][0] + " - " + nombresCarrera[i][1],
                    16, 0, false
            );
            int finalI = i;
            btnCarrera.setOnClickListener(v -> mostrarConfirmacionCarrera(
                    nombresCarrera[finalI][0].charAt(0),
                    nombresCarrera[finalI][1]
            ));
            containerCarreras.addView(btnCarrera);
        }
    }

    // 2) si hay inscripicones, muestra las infos de los alumnos
    private void generarListaInfos(){
        // limpiar
        containerCarreras.removeAllViews();
        // Crear botones para cada carrera
        InfoInscripcion[] infos = MainActivity.getInfosInscripcion();

        for (int i = 0; i < infos.length; i++) {
            Button btnInfo = nuevoBoton(
                    infos[i].getLetraCarrera() + "." + infos[i].getNomberAlumno(),
                    16, 0, false
            );
            int finalI = i;

            btnInfo.setOnClickListener(v -> mostrarOpcionesInscripcionAlumno(infos[finalI]));

            containerCarreras.addView(btnInfo);
        }
        // boton crear nueva info
        Button btnNuevaInfo = nuevoBoton("(+) Crear",16, 0, false);
        btnNuevaInfo.setOnClickListener(v -> mostrarConfirmacionCrearInscripcionAlumno());
        containerCarreras.addView(btnNuevaInfo);
    }

    // 1.1) confirmacion carrera, para crear la inscripcion del alumno
    private void mostrarConfirmacionCarrera(char letraCarrera, String nombreCarrera) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Carrera")
                .setMessage(nombreCarrera)
                .setPositiveButton("Sí", (dialog, which) -> {
                    tomarNombreAlumno(letraCarrera);
                })
                .setNegativeButton("No", null)
                .show();

    }

    // 1.2) ingresar nombre de alumno
    // recursiva
    private void tomarNombreAlumno(char letraCarrera) {
        // crear cuadro de texto
        EditText editText = new EditText(requireContext());
        editText.setHint("Nombre...");
        // mostrar cuadro de dialogo
        new AlertDialog.Builder(getContext())
                .setTitle("Ingresar nombre (solo letras):")
                .setView(editText)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String nombreIngresado = editText.getText().toString();
                    comprobarPuntos(letraCarrera,nombreIngresado);
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }

    private void comprobarPuntos(char letraCarrera, String nombreIngresado){
        if(nombreIngresado.contains(".")){
            new AlertDialog.Builder(getContext())
                .setTitle("solo letras")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // recursividad
                    tomarNombreAlumno(letraCarrera);
                })
                .show();
        }
        else {
            ((MainActivity) getActivity()).seleccionarCarrera(letraCarrera,nombreIngresado);
        }
    }

    // 2.1) Cargar o borrar alumno elegido, para gestionar sus materias inscriptas
    private void mostrarOpcionesInscripcionAlumno(InfoInscripcion info) {
        String msj = "Carrera: " + Especialiad.fromLetra(info.getLetraCarrera())
            + "\n" + "Regulares: " + info.getRegulares()
            + "\n" + "Aprobadas: " + info.getAprobadas()
            + "\n" + "Inscriptas: " + info.getInscriptas();

        new AlertDialog.Builder(getContext())
            .setTitle("Inscripcion:")
            .setMessage(msj)
            .setPositiveButton("Abrir", (dialog, which) -> {
                ((MainActivity) getActivity()).seleccionarAlumno(
                        info.getLetraCarrera(),info.getNomberAlumno());
            })
            .setNegativeButton("Cancelar",null)
            .setNeutralButton("Eliminar", (dialog, which) -> {
                mostrarConfirmacionEliminarAlumno(info);
            })
            .show();
    }

    // 2.1.1) Confirmar eliminar alumno elegido
    private void mostrarConfirmacionEliminarAlumno(InfoInscripcion info){
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Alumno?")
                .setMessage(info.getLetraCarrera() + "." + info.getNomberAlumno())
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    ((MainActivity) getActivity()).borrarAlumno(
                            info.getLetraCarrera(),info.getNomberAlumno());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // 2.2) Confirmar crear nueva inscripcion alumno
    private void mostrarConfirmacionCrearInscripcionAlumno() {
        new AlertDialog.Builder(getContext())
                .setTitle("Crear nueva?")
                .setMessage("")
                .setPositiveButton("Sí", (dialog, which) -> {
                    generarListaCarreras();
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