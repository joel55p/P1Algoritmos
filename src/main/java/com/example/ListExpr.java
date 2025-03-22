/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Implementación de listas de expresiones para el intérprete Lisp.
 * Representa una colección de expresiones en forma de lista, fundamental para
 * la estructura de datos y sintaxis de Lisp.
 */
package com.example;
import java.util.List;

public class ListExpr extends Expr {
    /** Lista de elementos que contiene esta expresión */
    public List<Expr> elements;
    
    /**
     * Constructor para crear una nueva expresión de lista.
     * 
     * @param elements Lista de expresiones que conformarán esta lista
     */
    public ListExpr(List<Expr> elements) { 
        this.elements = elements; 
    }
    
    /**
     * Convierte la lista de expresiones a una representación de cadena.
     * 
     * @return Una cadena que representa la lista, con elementos separados por espacios
     *         y encerrados entre corchetes
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i < elements.size() - 1) sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Obtiene los elementos contenidos en esta lista.
     * 
     * @return La lista de expresiones contenidas
     */
    public List<Expr> getElements() {
        return elements;
    }
}