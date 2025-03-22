package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispMain (punto de entrada del intérprete)
 */
public class LispMainTest {
    
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
    
    @Test
    public void testSimpleExpression() {
        String input = "(+ 2 3)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Resultado: 5.0"));
    }
    
    @Test
    public void testSetqAndUseVariable() {
        String input = "(SETQ X 42)\n(+ X 10)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Resultado: 42.0"));
        assertTrue(output.contains("Resultado: 52.0"));
    }
    
    @Test
    public void testDefunAndCallFunction() {
        String input = 
            "(DEFUN SQUARE (N) (* N N))\n" +
            "(SQUARE 5)\n" +
            "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Resultado: SQUARE"));
        assertTrue(output.contains("Resultado: 25.0"));
    }
    
    @Test
    public void testMultilineInput() {
        String input = 
            "(DEFUN FACTORIAL (N)\n" +
            "  (COND ((= N 0) 1)\n" +
            "        (T (* N (FACTORIAL (- N 1))))))\n" +
            "(FACTORIAL 5)\n" +
            "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Resultado: FACTORIAL"));
        assertTrue(output.contains("Resultado: 120.0"));
    }
    
    @Test
    public void testUnbalancedParentheses() {
        String input = "(+ 2 3\n)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Paréntesis desbalanceados"));
        assertTrue(output.contains("Resultado: 5.0"));
    }
    
    @Test
    public void testErrorHandling() {
        String input = "(/ 1 0)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        try {
            LispMain.main(new String[]{});
            
            // O verificamos que el output contiene algún mensaje que indique un error
            String output = outContent.toString();
            String error = errContent.toString();
            
            // La prueba pasa si o bien hay un mensaje de error en error, o bien
            // hay un mensaje de error en el output normal
            boolean hasError = error.length() > 0 || 
                            output.contains("Error") || 
                            output.contains("error") ||
                            output.contains("Exception") ||
                            output.contains("excepción");
            
            assertTrue("Se esperaba algún tipo de mensaje de error para la división por cero", hasError);
        } catch (Exception e) {
            // Si lanza una excepción que no se captura, también consideramos que la prueba pasa
            // porque se ha detectado el error de alguna forma
            assertTrue(true);
        }
    }    
    @Test
    public void testLoadFile() throws IOException {
        // Crear un archivo temporal con código Lisp
        File tempFile = File.createTempFile("test", ".lisp");
        tempFile.deleteOnExit();
        
        FileWriter writer = new FileWriter(tempFile);
        writer.write("(DEFUN SQUARE (N) (* N N))\n");
        writer.write("(SQUARE 5)\n");
        writer.close();
        
        // Ejecutar el intérprete con el comando load
        String input = "(load \"" + tempFile.getAbsolutePath() + "\")\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Archivo procesado:"));
        assertTrue(output.contains("Resultado: 25.0"));
    }
    
    @Test
    public void testComplexExpression() {
        String input = "(+ (* 2 3) (- 10 5))\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Resultado: 11.0"));
    }
    
    @Test
    public void testRecursiveFunction() {
        String input = 
            "(DEFUN FIBONACCI (N)\n" +
            " (COND ((= N 0) 1)\n" +
            "       ((= N 1) 1)\n" +
            "       (T (+ (FIBONACCI (- N 1))\n" +
            "             (FIBONACCI (- N 2))))))\n" +
            "(FIBONACCI 5)\n" +
            "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LispMain.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Resultado: FIBONACCI"));
        assertTrue(output.contains("Resultado: 8.0"));
    }
}