/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Implementación de valores numéricos para el intérprete Lisp.
 * Representa un número de punto flotante que puede ser utilizado en 
 * operaciones aritméticas y comparaciones.
 */
package com.example;

public class NumberExpr extends Expr {
    /** Valor numérico almacenado */
    public double value;
    
    /**
     * Constructor para crear una nueva expresión numérica.
     * 
     * @param value El valor numérico a almacenar
     */
    public NumberExpr(double value) { 
        this.value = value; 
    }
    
    /**
     * Convierte el valor numérico a una representación de cadena.
     * 
     * @return Una cadena que representa el valor numérico
     */
    public String toString() { 
        return String.valueOf(value); 
    }
}