package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispLexer (analizador léxico)
 * Ampliadas para conseguir cobertura completa del código
 */
public class LispLexerTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    public void testIsBalancedWithBalancedParentheses() {
        assertTrue(LispLexer.isBalanced("(+ 2 3)"));
        assertTrue(LispLexer.isBalanced("(+ (* 2 3) (- 5 1))"));
        assertTrue(LispLexer.isBalanced("(DEFUN FIBONACCI (N) (COND ((= N 0) 1) ((= N 1) 1) (T (+ (FIBONACCI (- N 1)) (FIBONACCI (- N 2))))))"));
    }

    @Test
    public void testIsBalancedWithUnbalancedParentheses() {
        assertFalse(LispLexer.isBalanced("(+ 2 3"));
        assertFalse(LispLexer.isBalanced("(+ 2 3))"));
        assertFalse(LispLexer.isBalanced(")(+ 2 3)"));
    }

    @Test
    public void testTokenizeSimpleExpression() {
        List<String> tokens = LispLexer.tokenize("(+ 2 3)");
        
        assertEquals(5, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("2", tokens.get(2));
        assertEquals("3", tokens.get(3));
        assertEquals(")", tokens.get(4));
    }

    @Test
    public void testTokenizeNestedExpression() {
        List<String> tokens = LispLexer.tokenize("(+ (* 2 3) (- 5 1))");
        
        assertEquals(13, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("(", tokens.get(2));
        assertEquals("*", tokens.get(3));
        assertEquals("2", tokens.get(4));
        assertEquals("3", tokens.get(5));
        assertEquals(")", tokens.get(6));
        assertEquals("(", tokens.get(7));
        assertEquals("-", tokens.get(8));
        assertEquals("5", tokens.get(9));
        assertEquals("1", tokens.get(10));
        assertEquals(")", tokens.get(11));
        assertEquals(")", tokens.get(12));
    }

    @Test
    public void testTokenizeWithExtraWhitespace() {
        List<String> tokens = LispLexer.tokenize("  (+    2   3  )  ");
        
        assertEquals(5, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("2", tokens.get(2));
        assertEquals("3", tokens.get(3));
        assertEquals(")", tokens.get(4));
    }
    
    @Test
    public void testTokenizeWithEmptyInput() {
        List<String> tokens = LispLexer.tokenize("");
        assertEquals(0, tokens.size());
    }
    
    @Test
    public void testTokenizeWithOnlyParentheses() {
        List<String> tokens = LispLexer.tokenize("()");
        assertEquals(2, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals(")", tokens.get(1));
    }

    @Test
    public void testTokenizeWithSpecialCharacters() {
        List<String> tokens = LispLexer.tokenize("(= x 42)");
        
        assertEquals(5, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("=", tokens.get(1));
        assertEquals("x", tokens.get(2));
        assertEquals("42", tokens.get(3));
        assertEquals(")", tokens.get(4));
    }

    @Test
    public void testTokenizeWithTrailingToken() {
        List<String> tokens = LispLexer.tokenize("(+ 2 3) 4");
        
        assertEquals(6, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("2", tokens.get(2));
        assertEquals("3", tokens.get(3));
        assertEquals(")", tokens.get(4));
        assertEquals("4", tokens.get(5));
    }
    
    @Test
    public void testTokenizeWithMultipleConsecutiveParentheses() {
        List<String> tokens = LispLexer.tokenize("((()))");
        
        assertEquals(6, tokens.size());
        for (int i = 0; i < 3; i++) {
            assertEquals("(", tokens.get(i));
        }
        for (int i = 3; i < 6; i++) {
            assertEquals(")", tokens.get(i));
        }
    }
    
    @Test
    public void testTokenizeWithSymbolsContainingSpecialChars() {
        List<String> tokens = LispLexer.tokenize("(* x_2 y-3)");
        
        assertEquals(5, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("*", tokens.get(1));
        assertEquals("x_2", tokens.get(2));
        assertEquals("y-3", tokens.get(3));
        assertEquals(")", tokens.get(4));
    }
    
    @Test
    public void testTokenizeWithMultipleContinuousTokens() {
        List<String> tokens = LispLexer.tokenize("(abc123)(xyz)");
        
        assertEquals(6, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("abc123", tokens.get(1));
        assertEquals(")", tokens.get(2));
        assertEquals("(", tokens.get(3));
        assertEquals("xyz", tokens.get(4));
        assertEquals(")", tokens.get(5));
    }
    
    @Test
    public void testMainWithBalancedExpression() {
        // Simular entrada del usuario
        String input = "(+ 2 3)\nsalir\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispLexer.main(new String[]{});
        
        // Verificar salida
        String output = outContent.toString();
        assertTrue("La salida debe indicar expresión correcta", 
                  output.contains("Expresión correcta") || output.contains("paréntesis están balanceados"));
        assertTrue("La salida debe mostrar los tokens", 
                  output.contains("[") && output.contains("+") && output.contains("2") && 
                  output.contains("3") && output.contains("]"));
    }

    @Test
    public void testMainWithUnbalancedExpression() {
        // Simular entrada del usuario
        String input = "(+ 2 3\nsalir\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispLexer.main(new String[]{});
        
        // Verificar salida
        String output = outContent.toString();
        assertTrue("La salida debe indicar expresión incorrecta", 
                  output.contains("Expresión incorrecta") || output.contains("paréntesis no están balanceados"));
    }
    
    @Test
    public void testMainWithMultipleInputs() {
        // Simular entrada del usuario con múltiples expresiones
        String input = "(+ 2 3)\n(* 4 5)\nsalir\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispLexer.main(new String[]{});
        
        // Verificar salida
        String output = outContent.toString();
        // Verificar que se procesan ambas expresiones
        assertTrue("Debe procesar la primera expresión correctamente", 
                 (output.indexOf("Expresión correcta") >= 0 && 
                  output.indexOf("Tokens: [") >= 0 && 
                  output.indexOf("+") >= 0));
                  
        assertTrue("Debe procesar la segunda expresión correctamente", 
                 (output.lastIndexOf("Expresión correcta") > output.indexOf("Expresión correcta") && 
                  output.lastIndexOf("Tokens: [") > output.indexOf("Tokens: [") && 
                  output.indexOf("*") >= 0));
    }
    
    @Test
    public void testMainWithEmptyInput() {
        // Simular entrada del usuario con una línea vacía
        String input = "\nsalir\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispLexer.main(new String[]{});
        
        // Verificar salida - no debería haber errores
        String output = outContent.toString();
        String error = errContent.toString();
        
        // Comprobar que no hay errores de ejecución
        assertTrue("No debería haber errores en la salida estándar para entrada vacía", 
                  !output.contains("Exception") && !output.contains("Error"));
        assertEquals("No debería haber errores en la salida de error para entrada vacía", "", error);
    }
    
    @Test
    public void testTokenizeComplexExpression() {
        // Probar con una expresión más compleja que mezcla varios tipos de tokens
        List<String> tokens = LispLexer.tokenize("(DEFUN FACTORIAL (N) (COND ((= N 0) 1) (T (* N (FACTORIAL (- N 1))))))");
        
        assertTrue(tokens.size() > 20);  // Debería tener muchos tokens
        assertEquals("(", tokens.get(0));
        assertEquals("DEFUN", tokens.get(1));
        assertEquals("FACTORIAL", tokens.get(2));
        assertEquals("(", tokens.get(3));
        assertEquals("N", tokens.get(4));
        assertEquals(")", tokens.get(5));
    }
    
    @Test
    public void testMainWithDirectExit() {
        // Probar cuando el usuario escribe "salir" inmediatamente
        String input = "salir\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        LispLexer.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue("Debe salir correctamente", output.contains("Saliendo"));
    }
}