package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispParserMain
 */
public class LispParserMainTest {
    
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
    public void testParserMainWithValidInput() {
        // Configurar la entrada simulada
        String input = "(+ 2 3)\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar la salida
        String output = outContent.toString();
        assertTrue("Debe mostrar la expresión parseada correctamente", 
                  output.contains("Expresión parseada:") && 
                  (output.contains("[+ 2.0 3.0]") || output.contains("(+ 2.0 3.0)") || 
                   output.contains("+ 2.0 3.0")));
    }
    
    @Test
    public void testParserMainWithInvalidInput() {
        // Configurar la entrada simulada con una expresión incompleta
        String input = "(+ 2 3\n";  // Falta un paréntesis de cierre
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar la salida
        String output = outContent.toString();
        
        assertTrue("Debe mostrar algún tipo de error por paréntesis desbalanceados", 
                output.contains("Error") || output.contains("paréntesis") || 
                output.contains("desbalanceados"));
    }
    
    @Test
    public void testParserMainWithComplexExpression() {
        // Configurar la entrada simulada con una expresión más compleja
        String input = "(DEFUN FACTORIAL (N) (COND ((= N 0) 1) (T (* N (FACTORIAL (- N 1))))))\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar la salida
        String output = outContent.toString();
        assertTrue("Debe mostrar la expresión parseada correctamente", 
                  output.contains("Expresión parseada:") && 
                  output.contains("DEFUN") && 
                  output.contains("FACTORIAL"));
    }
    
    @Test
    public void testParserMainWithEmptyInput() {
        // Configurar la entrada simulada vacía
        String input = "\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar la salida
        String output = outContent.toString();
        assertTrue("Debe mostrar algún mensaje de error para entrada vacía", 
                output.contains("Error") || output.contains("vacía"));
    }
    
    @Test
    public void testParserMainWithSingleSymbol() {
        // Configurar la entrada simulada con un solo símbolo
        String input = "xyz\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar que se parseó correctamente
        String output = outContent.toString();
        assertTrue("Debe mostrar la expresión parseada correctamente", 
                output.contains("Expresión parseada:") && output.contains("xyz"));
    }
    
    @Test
    public void testParserMainWithNumericInput() {
        // Configurar la entrada simulada con un número
        String input = "42\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar que se parseó correctamente
        String output = outContent.toString();
        assertTrue("Debe mostrar la expresión parseada correctamente", 
                output.contains("Expresión parseada:") && output.contains("42"));
    }
    
    @Test
    public void testParserMainWithInvalidSyntax() {
        // Configurar la entrada simulada con una expresión que causará un error sintáctico
        String input = "[invalid]\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        
        // Ejecutar el método main
        LispParserMain.main(new String[]{});
        
        // Verificar que muestra un mensaje de error
        String output = outContent.toString();
        assertTrue("Debe mostrar un mensaje de error", 
                output.contains("Error") || output.contains("Operador no válido"));
    }
    
    @Test
    public void testParserMainWithNullInput() {
        // Esta prueba simula un caso extremo donde el scanner podría devolver null
        // aunque en realidad esto no ocurriría normalmente
        try {
            // Configurar un InputStream que inmediatamente alcance EOF
            ByteArrayInputStream inContent = new ByteArrayInputStream(new byte[0]);
            System.setIn(inContent);
            
            // Ejecutar el método main
            LispParserMain.main(new String[]{});
            
            // Si llega aquí, la prueba pasa (no debería lanzar NullPointerException)
            assertTrue(true);
        } catch (Exception e) {
            // Si ocurre una excepción, es aceptable siempre que sea manejada apropiadamente
            String output = outContent.toString();
            assertTrue("Debe manejar la entrada null o vacía", 
                    output.contains("Error") || !output.isEmpty());
        }
    }
    
    @Test
    public void testParserMainWithIOException() {
        // Esta prueba simula un caso donde ocurre una IOException
        // Usando una entrada que causará problemas al scanner
        try {
            // Crear un InputStream que lanzará una excepción al leer
            InputStream problematicStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    throw new IOException("Error de lectura simulado");
                }
            };
            System.setIn(problematicStream);
            
            // Ejecutar el método main
            LispParserMain.main(new String[]{});
            
            // Si llega aquí sin lanzar excepciones, la prueba pasa
            assertTrue(true);
        } catch (Exception e) {
            // Si se lanza una excepción, también es válido siempre que sea manejada
            String output = outContent.toString();
            assertTrue("Debe manejar las excepciones de I/O", 
                    output.contains("Error") || !output.isEmpty());
        } finally {
            // Restaurar la entrada estándar
            System.setIn(originalIn);
        }
    }
}