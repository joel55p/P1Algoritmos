# Intérprete de Lisp en Java

Un intérprete de Lisp implementado en Java que permite ejecutar expresiones en el lenguaje Lisp, tanto en una línea como en múltiples líneas.

## Autores

- Denil José Parada Cabrera - 24761
- Arodi Chávez - 241112
- Joel Nerio - 24253

## Descripción

Este proyecto implementa un intérprete básico para un subconjunto del lenguaje Lisp. El intérprete permite analizar, parsear y evaluar expresiones Lisp, incluyendo funciones definidas por el usuario y estructuras de control condicionales.

## Características

- **Análisis léxico y sintáctico** de expresiones Lisp
- **Evaluación** de expresiones matemáticas y lógicas
- **Soporte para funciones recursivas** definidas por el usuario mediante `DEFUN`
- **Estructuras de control** como `COND`
- **Operaciones básicas**: +, -, *, /, =, <, >, etc.
- **Funciones Lisp**: ATOM, LIST, EQUAL, CAR
- **Manejo de múltiples líneas** para escribir expresiones complejas
- **Detección de errores** como paréntesis desbalanceados

## Estructura del Proyecto

- `Expr.java`: Clase base abstracta para todas las expresiones Lisp
- `BooleanExpr.java`: Representa valores booleanos (T/NIL)
- `NumberExpr.java`: Representa valores numéricos
- `Symbol.java`: Representa símbolos (variables, nombres de funciones)
- `ListExpr.java`: Representa listas de expresiones
- `LispLexer.java`: Tokeniza la entrada en lexemas
- `LispParser.java`: Construye un árbol de sintaxis a partir de los tokens
- `LispEvaluator.java`: Evalúa las expresiones según la semántica Lisp
- `LispMain.java`: Punto de entrada, interfaz de línea de comandos
- `LispInputPreprocessor.java`: Preprocesa la entrada para manejar múltiples líneas
- `UserFunction.java`: Representa funciones definidas por el usuario

## Funcionalidades Implementadas

1. **Operaciones matemáticas básicas**
   - Suma, resta, multiplicación y división
   - Comparaciones (igual, mayor que, menor que)

2. **Definición de funciones**
   - Creación de funciones recursivas
   - Evaluación con ámbito local de variables

3. **Estructuras de control**
   - Condicionales con COND
   - Expresiones booleanas (verdadero/falso)

4. **Interfaz de usuario interactiva**
   - Entrada multilínea
   - Detección de paréntesis desbalanceados
   - Mensajes de error descriptivos

## Uso

### Ejecución

Para ejecutar el intérprete:

```
java -cp "ruta/a/las/clases" com.example.LispMain
```

### Comandos Básicos

- Para salir: `exit`
- Para cargar un archivo: `(load "ruta/al/archivo.lisp")`

### Ejemplos de Expresiones

#### Función Fibonacci
```lisp
(DEFUN FIBONACCI (N)
 (COND ((= N 0) 1)                   
       ((= N 1) 1)                   
       (T (+ (FIBONACCI (- N 1))     
             (FIBONACCI (- N 2))))))
```

#### Función Factorial
```lisp
(DEFUN FACTORIAL (N)
  (COND ((= N 0) 1)
        (T (* N (FACTORIAL (- N 1))))))
```

### Evaluación de Expresiones

Para evaluar una expresión después de definir una función:
```
> (FIBONACCI 5)
Resultado: 8

> (FACTORIAL 5)
Resultado: 120
```

## Limitaciones

- No implementa todas las funciones estándar de Lisp
- Manejo limitado de tipos de datos (sin soporte para strings, arrays, etc.)
- Sin optimizaciones como evaluación perezosa o tail-call optimization

## Desarrollo Futuro

Posibles mejoras para el proyecto:
- Implementar más funciones estándar de Lisp
- Añadir soporte para macros
- Mejorar el manejo de errores y depuración
- Implementar un recolector de basura
- Añadir soporte para evaluación paralela

## Referencias

- McCarthy, J. (1960). Recursive functions of symbolic expressions and their computation by machine, Part I. Communications of the ACM.
- Abelson, H., & Sussman, G. J. (1996). Structure and Interpretation of Computer Programs. MIT Press.
