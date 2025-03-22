/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Programa principal para probar el analizador sintáctico de Lisp.
 * Permite al usuario ingresar expresiones Lisp y muestra el resultado del análisis
 * sintáctico sin realizar la evaluación.
 */
package com.example;
import java.util.Scanner;

public class LispParserMain {
    /**
     * Método principal que ejecuta una versión simple del intérprete que solo
     * realiza el análisis sintáctico de una expresión.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una expresión LISP: ");
        String code = scanner.nextLine(); //Se le solicita al usuario que ingrese una expresión LISP
        scanner.close();

        LispParser parser = new LispParser();
        try {
            Expr parsedExpression = parser.parse(code);
            System.out.println("Expresión parseada: " + parsedExpression);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}