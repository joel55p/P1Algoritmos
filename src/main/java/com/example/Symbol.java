/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Implementación de símbolos para el intérprete Lisp.
 * Representa identificadores como variables, nombres de funciones y operadores,
 * que son elementos fundamentales en el lenguaje Lisp.
 */
package com.example;

public class Symbol extends Expr {
    /** Nombre del símbolo */
    public String name;
    
    /**
     * Constructor para crear un nuevo símbolo.
     * 
     * @param name El nombre del símbolo
     */
    public Symbol(String name) { 
        this.name = name; 
    }
    
    /**
     * Convierte el símbolo a una representación de cadena.
     * 
     * @return El nombre del símbolo
     */
    public String toString() { 
        return name; 
    }
}