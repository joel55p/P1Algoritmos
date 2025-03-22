/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Clase que representa una expresión booleana en Lisp.
 * Extiende de la clase Expr y maneja los valores true/false, 
 * representados en Lisp como T/NIL respectivamente.
 */

package com.example;
public class BooleanExpr extends Expr {
    /** Valor booleano que contiene esta expresión */
    public final boolean value;

    /**
     * Constructor que inicializa una expresión booleana con el valor especificado.
     * 
     * @param value El valor booleano (true o false) a almacenar
     */
    public BooleanExpr(boolean value) {
        this.value = value;
    }

    /**
     * Convierte esta expresión booleana a su representación en String.
     * En Lisp, true se representa como "T" y false como "NIL".
     * 
     * @return "T" si el valor es true, "NIL" si es false
     */
    @Override
    public String toString() {
        return value ? "T" : "NIL"; // En Lisp, `T` es verdadero y `NIL` es falso
    }
}