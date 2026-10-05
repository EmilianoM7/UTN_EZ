package com.example.ez;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ez.domain.Especialiad;

public class VistaMenuFragment extends Fragment {

    LinearLayout containerMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_menu, container, false);

        containerMenu = view.findViewById(R.id.containerMenu);

        // titulo segun carrera
        TextView txtTitulo = view.findViewById(R.id.txtMenuTitle);
        String titulo = "EZ - " + Especialiad.fromLetra(MainActivity.getCarreraActual()).name();
        txtTitulo.setText(titulo);

        containerMenu.addView(nuevoBoton(
                "ListarMaterias",16, 0, false,
                v -> {
                    ((MainActivity) getActivity()).showFragmentWithBackStack(
                            VistaListarFragment.newInstance(true));
                }));
        containerMenu.addView(nuevoBoton("InfoCarrera",16, 0, false,
                v -> {
                    String[] datos = Backend.infoCarrera(MainActivity.getCarreraActual());
                    ((MainActivity) getActivity()).showFragmentWithBackStack(
                            VistaInfoResumenFragment.newInstance("InfoCarrera", datos));
                }));
        containerMenu.addView(nuevoBoton("ResumenCursada",16, 0, false,
                v -> {
                    String[] datos = Backend.resumenCursada();
                    ((MainActivity) getActivity()).showFragmentWithBackStack(
                            VistaInfoResumenFragment.newInstance("ResumenCursada", datos));
                }));
        containerMenu.addView(nuevoBoton("Horarios",16, 0, false,
                v -> {
                    ((MainActivity) getActivity()).showFragmentWithBackStack(
                            new VistaHorariosFragment());
                }));
        containerMenu.addView(nuevoBoton("Salir",16, 0, false,
                v -> {
                    getActivity().finish();
                }));

        return view;
    }

    Button nuevoBoton (String text, int tamanoTexto, int colorFondo, boolean bold, View.OnClickListener l){
        Button btn = new Button(requireContext());
        btn.setTextSize(tamanoTexto);
        btn.setText(text);
        if (bold){btn.setTypeface(null, Typeface.BOLD);}
        else {btn.setTypeface(null, Typeface.NORMAL);}

        // borde y fondo
        btn.setPadding(4,4,4,4);
        if(colorFondo != 0){btn.setBackgroundColor(colorFondo);}

        //action
        btn.setOnClickListener(l);
        return btn;
    }

}