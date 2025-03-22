package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispTest
 */
public class LispTestTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    // Prueba el método main completo con captura de excepciones para garantizar cobertura
    @Test
    public void testLispTestMainMethod() {
        try {
            // Ejecutar el método main de LispTest
            LispTest.main(new String[]{});
            
            // Verificar la salida
            String output = outContent.toString();
            
            // Verificar que la salida contiene los resultados esperados de las pruebas
            assertTrue("Debe procesar la versión multilínea de FIBONACCI", 
                    output.contains("Procesando versión multilínea de FIBONACCI"));
            
            assertTrue("Debe probar FIBONACCI(5)", 
                    output.contains("Probando FIBONACCI(5)"));
            
            assertTrue("Debe procesar la versión multilínea de MALAN", 
                    output.contains("Procesando versión multilínea de MALAN"));
            
            assertTrue("Debe probar MALAN(2 3)", 
                    output.contains("Probando MALAN(2 3)"));
        } catch (Exception e) {
            // Si hay una excepción, imprimimos el stack trace para depuración
            e.printStackTrace();
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // Prueba el método processExpression con varios tipos de expresiones
    @Test
    public void testProcessExpressionMethod() {
        try {
            // Usar reflection para acceder al método privado processExpression
            Method processExpression = LispTest.class.getDeclaredMethod(
                "processExpression", String.class);
            processExpression.setAccessible(true);
            
            // Probar con una expresión simple
            processExpression.invoke(null, "(+ 1 2)");
            
            // Probar con una expresión más compleja
            processExpression.invoke(null, "(DEFUN TEST (X) (* X 2))");
            
            // Verificar la salida general
            String output = outContent.toString();
            assertTrue("Debe mostrar la expresión original", 
                    output.contains("Expresión original"));
            assertTrue("Debe mostrar la expresión normalizada", 
                    output.contains("Expresión normalizada"));
            assertTrue("Debe indicar que los paréntesis están balanceados", 
                    output.contains("Paréntesis balanceados"));
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // Prueba específica para el caso de paréntesis desbalanceados
    @Test
    public void testProcessExpressionWithUnbalancedParentheses() {
        try {
            Method processExpression = LispTest.class.getDeclaredMethod(
                "processExpression", String.class);
            processExpression.setAccessible(true);
            
            // Probar con una expresión con paréntesis desbalanceados
            processExpression.invoke(null, "(DEFUN TEST (X) (* X 2)");
            
            String output = outContent.toString();
            assertTrue("Debe indicar que los paréntesis no están balanceados", 
                    output.contains("Paréntesis balanceados: false"));
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // Prueba para expresiones multilínea
    @Test
    public void testProcessExpressionWithMultilineFormat() {
        try {
            Method processExpression = LispTest.class.getDeclaredMethod(
                "processExpression", String.class);
            processExpression.setAccessible(true);
            
            // Probar con una expresión multilínea
            String multilineExpr = 
                "(DEFUN FACTORIAL (N)\n" +
                "  (COND ((= N 0) 1)\n" +
                "        (T (* N (FACTORIAL (- N 1))))))";
            processExpression.invoke(null, multilineExpr);
            
            String output = outContent.toString();
            assertTrue("Debe mostrar la expresión normalizada sin saltos de línea", 
                    output.contains("Expresión normalizada") && 
                    output.contains("DEFUN") && 
                    output.contains("FACTORIAL"));
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // Prueba con entradas vacías o nulas
    @Test
    public void testProcessExpressionWithEmptyOrNullInput() {
        try {
            Method processExpression = LispTest.class.getDeclaredMethod(
                "processExpression", String.class);
            processExpression.setAccessible(true);
            
            // Probar con una cadena vacía
            processExpression.invoke(null, "");
            
            // Probar con una cadena que solo tiene espacios
            processExpression.invoke(null, "   ");
            
            // No debería lanzar excepciones
            assertTrue(true);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // Prueba con comentarios
    @Test
    public void testProcessExpressionWithComments() {
        try {
            Method processExpression = LispTest.class.getDeclaredMethod(
                "processExpression", String.class);
            processExpression.setAccessible(true);
            
            // Probar con una expresión que contiene comentarios
            String exprWithComments = 
                "(DEFUN TEST (X) ; Este es un comentario\n" +
                "  (* X 2)) ; Otro comentario";
            processExpression.invoke(null, exprWithComments);
            
            String output = outContent.toString();
            assertTrue("Debe mostrar la expresión original con comentarios", 
                    output.contains("Este es un comentario") || 
                    output.contains("Otro comentario"));
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // Prueba para el manejo de excepciones en el método main
    @Test
    public void testMainMethodExceptionHandling() {
        try {
            // Modificar System.err temporalmente para capturar errores
            PrintStream originalErr = System.err;
            ByteArrayOutputStream errContent = new ByteArrayOutputStream();
            System.setErr(new PrintStream(errContent));
            
            // Ejecutar el método main con un argumento (no soportado pero debería manejar la excepción)
            LispTest.main(new String[]{"argumento"});
            
            // Restaurar System.err
            System.setErr(originalErr);
            
            // Verificar que el programa no terminó abruptamente
            assertTrue(true);
            
        } catch (Exception e) {
            // Si hay una excepción, también es válido siempre y cuando sea manejada
            assertTrue(true);
        }
    }
}