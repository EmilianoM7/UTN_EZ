package com.example.ez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class VistaEditarMateriaFragment extends Fragment {

    private int orden;
    private char condicionOriginal;
    private int notaOriginal;
    private String comisionOriginal;
    private String nombreMateria;
    private Runnable onConfirmListener;

    public static VistaEditarMateriaFragment newInstance(int orden, char condicion,
                                                         int nota, String comision,
                                                         String nombre) {
        VistaEditarMateriaFragment fragment = new VistaEditarMateriaFragment();
        Bundle args = new Bundle();
        args.putInt("orden", orden);
        args.putChar("condicion", condicion);
        args.putInt("nota", nota);
        args.putString("comision", comision);
        args.putString("nombre", nombre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_editar_materia, container, false);

        if (getArguments() != null) {
            orden = getArguments().getInt("orden");
            condicionOriginal = getArguments().getChar("condicion");
            notaOriginal = getArguments().getInt("nota");
            comisionOriginal = getArguments().getString("comision");
            nombreMateria = getArguments().getString("nombre");
        }

        TextView txtTituloMateria = view.findViewById(R.id.txtTituloMateria);
        txtTituloMateria.setText(nombreMateria);

        Spinner spinnerCondicion = view.findViewById(R.id.spinnerCondicion);
        Spinner spinnerNota = view.findViewById(R.id.spinnerNota);
        Spinner spinnerComision = view.findViewById(R.id.spinnerComision);

        // Configurar spinners
        String[] condiciones = Backend.getCondiciones();
        ArrayAdapter<String> adapterCondiciones = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, condiciones);
        adapterCondiciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCondicion.setAdapter(adapterCondiciones);

        Integer[] notas = Backend.getNotas();
        ArrayAdapter<Integer> adapterNotas = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, notas);
        adapterNotas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNota.setAdapter(adapterNotas);

        String[] comisiones = Backend.getComisiones(MainActivity.getSiglaMateriaActual());
        ArrayAdapter<String> adapterComisiones = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, comisiones);
        adapterComisiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComision.setAdapter(adapterComisiones);

        // Seleccionar valores originales
        int posCondicion = getCondicionIndex(condicionOriginal);
        spinnerCondicion.setSelection(posCondicion);
        spinnerNota.setSelection(notaOriginal);

        for (int i = 0; i < comisiones.length; i++) {
            if (comisiones[i].equals(comisionOriginal)) {
                spinnerComision.setSelection(i);
                break;
            }
        }

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> cerrar());

        Button btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(v -> confirmar(spinnerCondicion, spinnerNota, spinnerComision));

        View panelFondo = view.findViewById(R.id.panelFondo);
        panelFondo.setOnClickListener(v -> cerrar());

        return view;
    }

    private int getCondicionIndex(char condicion) {
        switch (condicion) {
            case 'R': return 0;
            case 'A': return 1;
            case 'I': return 2;
            case 'D': return 3;
            case 'N': return 4;
            default: return 0;
        }
    }

    private char getCondicionChar(int index) {
        switch (index) {
            case 0: return 'R';
            case 1: return 'A';
            case 2: return 'I';
            case 3: return 'D';
            case 4: return 'N';
            default: return 'N';
        }
    }

    private void confirmar(Spinner spinnerCondicion, Spinner spinnerNota, Spinner spinnerComision) {
        char condicionSeleccionada = getCondicionChar(spinnerCondicion.getSelectedItemPosition());
        int notaSeleccionada = (Integer) spinnerNota.getSelectedItem();
        String comisionSeleccionada = (String) spinnerComision.getSelectedItem();

        if (condicionSeleccionada != condicionOriginal ||
                notaSeleccionada != notaOriginal ||
                !comisionSeleccionada.equals(comisionOriginal)) {

            Backend.registrarInscripcion(orden, condicionSeleccionada,
                    notaSeleccionada, comisionSeleccionada);

            if (onConfirmListener != null) {
                onConfirmListener.run();
            }
        }

        cerrar();
    }

    private void cerrar() {
        getParentFragmentManager().popBackStack();
    }

    public void setOnConfirmListener(Runnable listener) {
        this.onConfirmListener = listener;
    }
}