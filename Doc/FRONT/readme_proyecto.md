# Proyecto EZ - Aplicación Android de Gestión de Materias

## Estructura del Proyecto

El proyecto está organizado de la siguiente manera:

```
app/
├── src/main/
│   ├── java/com/example/ez/
│   │   ├── * MainActivity.java
│   │   ├── * Backend.java
│   │   ├── * VistaCarrerasFragment.java
│   │   ├── * VistaMenuFragment.java
│   │   ├── * VistaListarFragment.java
│   │   ├── VistaEditarMateriaFragment.java
│   │   ├── * VistaInfoResumenFragment.java
│   │   └── * VistaHorariosFragment.java
│   │
│   ├── res/
│   │   ├── layout/
│   │   │   ├── * activity_main.xml
│   │   │   ├── * fragment_vista_carreras.xml
│   │   │   ├── * fragment_vista_menu.xml
│   │   │   ├── x fragment_vista_listar.xml
│   │   │   ├── * fragment_vista_editar_materia.xml
│   │   │   ├── * fragment_vista_info_resumen.xml
│   │   │   └── * fragment_vista_horarios.xml
│   │   │
│   │   └── values/
│   │       └── * strings.xml
│   │
│   └── * AndroidManifest.xml
│
└── * build.gradle
```

## Descripción de Componentes

### Backend.java
Clase que simula el backend con datos hardcodeados. Incluye todos los métodos especificados:
- `dataInicial()`: Retorna lista de carreras o null
- `infoCarrera()`: Información detallada de la carrera
- `resumenCursada()`: Estadísticas de la cursada
- `infoMateria()`: Información de una materia específica
- `simularHorario()`: Matriz de horarios
- `listarMaterias()`: Lista todas las materias
- `listarInscripciones()`: Lista solo las inscripciones
- `registrarInscripcion()`: Guarda cambios en inscripciones
- `getCondiciones()`, `getNotas()`, `getComisiones()`: Datos para los spinners

### MainActivity.java
Actividad principal que maneja la navegación entre fragmentos. Controla el flujo inicial basado en `dataInicial()`.

### VistaCarrerasFragment
Muestra la lista de carreras disponibles con confirmación al seleccionar.

### VistaMenuFragment
Menú principal con 6 opciones:
1. Listar Inscripciones
2. Listar Materias
3. Info Carrera
4. Resumen Cursada
5. Horarios
6. Salir

### VistaListarFragment
Vista reutilizable para listar materias o inscripciones:
- Botones de nivel colapsables (Primero-Quinto)
- Materias coloreadas según condición
- Barra de información al seleccionar materia
- Botones de info y edición

### VistaEditarMateriaFragment
Dialog para editar información de materia:
- Spinners para condición, nota y comisión
- Compara valores antes de guardar
- Actualiza la lista al confirmar cambios

### VistaInfoResumenFragment
Vista reutilizable para mostrar información:
- Info de Carrera
- Resumen de Cursada
- Info de Materia

### VistaHorariosFragment
Muestra una grilla de horarios:
- 6 columnas (días L-S)
- 17 filas (módulos)
- Colores: blanco (0), azul (1), rojo (>1)

## Colores según Condición

- **R (Regular)**: Verde (#4CAF50)
- **A (Aprobado)**: Azul (#2196F3)
- **I (Inscripto)**: Naranja (#FF9800)
- **D (Disponible)**: Amarillo (#FFEB3B)
- **N (No Disponible)**: Gris (#9E9E9E)

## Funcionalidades Implementadas

✅ Navegación entre vistas con fragmentos
✅ Backend con datos de ejemplo hardcodeados
✅ Selección de carrera con confirmación
✅ Menú principal con 6 opciones
✅ Lista de materias/inscripciones con agrupación por nivel
✅ Niveles colapsables
✅ Selección de materia con barra de info
✅ Edición de materia con spinners
✅ Vista de información reutilizable
✅ Vista de horarios con grilla coloreable
✅ Botón back para navegar hacia atrás
✅ Persistencia del estado durante la sesión

## Cómo Usar

1. Al iniciar, el app llama a `Backend.dataInicial()`
2. Si retorna lista de carreras, muestra VistaCarreras
3. Si retorna null, muestra VistaMenu directamente
4. Desde el menú se puede acceder a todas las funcionalidades
5. La navegación usa el back stack para volver atrás

## Modificar Datos Hardcodeados

Para cambiar los datos de ejemplo, edita los métodos en `Backend.java`:
- Modifica los arrays de retorno en cada método
- Ajusta las condiciones, notas, nombres según necesites
- Los datos se refrescan al volver a cargar la vista

## Notas Técnicas

- Usa XML básico sin Jetpack Compose
- No requiere dependencias pesadas
- Compatible con API 21+
- Usa Fragment para la navegación
- Spinners nativos de Android para los combos
- LinearLayout para las listas scrolleables
