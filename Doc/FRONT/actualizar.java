public void actualizarLista() {
        containerLista.removeAllViews();
        gruposNivel.clear();

        String[][] materias;
        if (opcionSeleccionada == 0) {
            materias = Backend.listarInscripciones(MainActivity.getCarreraActual());
        } else {
            materias = Backend.listarMaterias(MainActivity.getCarreraActual());
        }

        // Agrupar por nivel
        // ES BUENA LA DEL MAP
        Map<Integer, List<String[]>> materiasPorNivel = new HashMap<>();
        String[] nombresNivel = {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto"};

        for (String[] materia : materias) {
            int nivel = Integer.parseInt(materia[0]);
            // va inicializando los niveles (si no lo estan)
            if (!materiasPorNivel.containsKey(nivel)) {
                materiasPorNivel.put(nivel, new ArrayList<>());
            }
            // mientras les inserta materias
            materiasPorNivel.get(nivel).add(materia);
        }

        for (int i = 0; i <= nombresNivel.length; i++) {

            List<String[]> materiasNivel = materiasPorNivel.get(i);

            // rear el boton colapsable del nivel
            Button btnNivel = new Button(getContext());
            btnNivel.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //crea el grupo de materias correspondiente
            LinearLayout grupoMaterias = new LinearLayout(getContext());
            grupoMaterias.setOrientation(LinearLayout.VERTICAL);
            grupoMaterias.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //si el nivel esta vacio
            if (materiasNivel == null || materiasNivel.isEmpty()) {
                btnNivel.setText(nombresNivel[i] + " (vacío)");
                btnNivel.setEnabled(false);
            }
            // si el nivel itene materias
            else {
                btnNivel.setText(nombresNivel[i] + " -");
                grupoMaterias.setVisibility(View.VISIBLE);

                for (String[] materia : materiasNivel) {
                    Button btnMateria = crearBotonMateria(materia);
                    grupoMaterias.addView(btnMateria);
                }

                gruposNivel.put(i, grupoMaterias);

                // accion de expandir y colapsar
                btnNivel.setOnClickListener(v -> {
                    if (grupoMaterias.getVisibility() == View.VISIBLE) {
                        grupoMaterias.setVisibility(View.GONE);
                        btnNivel.setText(btnNivel.getText().toString().replace("-", "+"));
                    } else {
                        grupoMaterias.setVisibility(View.VISIBLE);
                        btnNivel.setText(btnNivel.getText().toString().replace("+", "-"));
                    }
                });
            }

            containerLista.addView(btnNivel);
            containerLista.addView(grupoMaterias);
        }
    }
