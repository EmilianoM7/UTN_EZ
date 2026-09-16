# EZ - Informacion de Materias y correlativas facil y rapido

## Estructura del Proyecto

```
app/
├── src/main/
│   ├── java/com/example/ez/
│   │   ├── MainActivity.java
│   │   ├── Backend.java
│   │   ├── VistaCarrerasFragment.java
│   │   ├── VistaMenuFragment.java
│   │   ├── VistaListarFragment.java
│   │   ├── VistaEditarMateriaFragment.java
│   │   ├── VistaInfoResumenFragment.java
│   │   └── VistaHorariosFragment.java
│   │
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── fragment_vista_carreras.xml
│   │   │   ├── fragment_vista_menu.xml
│   │   │   ├── fragment_vista_listar.xml
│   │   │   ├── fragment_vista_editar_materia.xml
│   │   │   ├── fragment_vista_info_resumen.xml
│   │   │   └── fragment_vista_horarios.xml
```

## Funcionalidades 

```
✅ Listar materias/inscripciones agrupadas por nivel(año)
✅ Vista de horarios de materias inscriptas
✅ Resumen de cursada (progreso y promedios)
```

## Notas Técnicas

- Usa XML básico sin Jetpack Compose
- No requiere dependencias pesadas
- Compatible con API 21+
- Usa Fragment para la navegación
