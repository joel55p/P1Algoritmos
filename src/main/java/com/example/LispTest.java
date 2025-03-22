/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Clase de prueba para el intérprete Lisp.
 * Contiene pruebas de funcionalidad para evaluar el correcto funcionamiento
 * de las funciones definidas por el usuario, como Fibonacci y Malan.
 */
package com.example;

public class LispTest {
    /**
     * Método principal que ejecuta pruebas para el intérprete Lisp.
     * Prueba la definición y evaluación de funciones recursivas en formato
     * multilínea y de una sola línea.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        // Las funciones que quieres probar, en formato multilínea
        String fibonacciMultiline = 
            "(DEFUN FIBONACCI (N)\n" +
            " (COND ((= N 0) 1)\n" +
            "       ((= N 1) 1)\n" +
            "       (T (+ (FIBONACCI (- N 1))\n" +
            "             (FIBONACCI (- N 2))))))";
            
        String malanMultiline = 
            "(DEFUN MALAN (M N)\n" +
            "  (COND ( (= N 0) 1)\n" +
            "        (T (* M (MALAN M (- N 1)))\n" +
            "        )\n" +
            "  ))";
            
        // Las mismas funciones en una sola línea
        String fibonacciOneline = "(DEFUN FIBONACCI (N) (COND ((= N 0) 1) ((= N 1) 1) (T (+ (FIBONACCI (- N 1)) (FIBONACCI (- N 2))))))";
        String malanOneline = "(DEFUN MALAN (M N) (COND ((= N 0) 1) (T (* M (MALAN M (- N 1))))))";
        
        // Procesar ambas versiones
        System.out.println("Procesando versión multilínea de FIBONACCI:");
        processExpression(fibonacciMultiline);
        
        System.out.println("\nProcesando versión una línea de FIBONACCI:");
        processExpression(fibonacciOneline);
        
        System.out.println("\nProcesando versión multilínea de MALAN:");
        processExpression(malanMultiline);
        
        System.out.println("\nProcesando versión una línea de MALAN:");
        processExpression(malanOneline);
        
        // Crear un evaluador para probar las funciones
        LispParser parser = new LispParser();
        LispEvaluator evaluator = new LispEvaluator();
        
        try {
            // Definir las funciones
            Expr fibonacciDef = parser.parse(LispInputPreprocessor.normalize(fibonacciMultiline));
            evaluator.eval(fibonacciDef);
            
            Expr malanDef = parser.parse(LispInputPreprocessor.normalize(malanMultiline));
            evaluator.eval(malanDef);
            
            // Probar FIBONACCI
            System.out.println("\nProbando FIBONACCI(5):");
            Expr fibResult = evaluator.eval(parser.parse("(FIBONACCI 5)"));
            System.out.println("Resultado: " + fibResult);
            
            // Probar MALAN
            System.out.println("\nProbando MALAN(2 3):");
            Expr malanResult = evaluator.eval(parser.parse("(MALAN 2 3)"));
            System.out.println("Resultado: " + malanResult);
            
        } catch (Exception e) {
            System.err.println("Error en prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Procesa una expresión Lisp, la normaliza y comprueba el balance de paréntesis.
     * 
     * @param expression La expresión Lisp a procesar
     */
    private static void processExpression(String expression) {
        System.out.println("Expresión original:");
        System.out.println(expression);
        
        String normalized = LispInputPreprocessor.normalize(expression);
        System.out.println("Expresión normalizada:");
        System.out.println(normalized);
        
        System.out.println("Paréntesis balanceados: " + LispInputPreprocessor.hasBalancedParentheses(normalized));
    }
}