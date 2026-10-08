# UTN_EZ
Seguimiento de carrera simplificado

## Funcionalidades 

```
✅ Listar materias inscritas y disponibles  para cursar agrupadas por nivel(año)
✅ Mostrar info de cada materia 
✅ Vista de horarios de materias inscriptas
✅ Resumen de cursada (progreso y promedios)
```

## Estructura del Proyecto

```
app/src/main/java/com/example/ez/
├── backend/                            //usaCases y logica
├── domain/                             //clases de dominio
├── repo/                               //clasesORM
├── MainActivity.java
├── VistaCarrerasFragment.java
├── VistaMenuFragment.java
├── VistaListarFragment.java
├── VistaEditarMateriaFragment.java
├── VistaInfoResumenFragment.java
└── VistaHorariosFragment.java

```

## Notas Técnicas

```
- Usa XML básico sin Jetpack Compose (dependencias pesadas)
- Compatible con API 21+
- Navegación mediante Fragments
```
