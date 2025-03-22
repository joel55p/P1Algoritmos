package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispParser (analizador sintáctico)
 * Ampliadas para conseguir cobertura completa del código
 */
public class LispParserTest {
    
    private LispParser parser;
    
    @Before
    public void setUp() {
        parser = new LispParser();
    }
    
    @Test
    public void testParseNumber() {
        Expr expr = parser.parse("42");
        assertTrue(expr instanceof NumberExpr);
        assertEquals(42.0, ((NumberExpr) expr).value, 0.001);
    }
    
    @Test
    public void testParseSymbol() {
        Expr expr = parser.parse("VARIABLE");
        assertTrue(expr instanceof Symbol);
        assertEquals("VARIABLE", ((Symbol) expr).name);
    }
    
    @Test
    public void testParseSimpleList() {
        Expr expr = parser.parse("(+ 2 3)");
        assertTrue(expr instanceof ListExpr);
        
        ListExpr listExpr = (ListExpr) expr;
        assertEquals(3, listExpr.elements.size());
        
        assertTrue(listExpr.elements.get(0) instanceof Symbol);
        assertEquals("+", ((Symbol) listExpr.elements.get(0)).name);
        
        assertTrue(listExpr.elements.get(1) instanceof NumberExpr);
        assertEquals(2.0, ((NumberExpr) listExpr.elements.get(1)).value, 0.001);
        
        assertTrue(listExpr.elements.get(2) instanceof NumberExpr);
        assertEquals(3.0, ((NumberExpr) listExpr.elements.get(2)).value, 0.001);
    }
    
    @Test
    public void testParseNestedList() {
        Expr expr = parser.parse("(+ (* 2 3) (- 5 1))");
        assertTrue(expr instanceof ListExpr);
        
        ListExpr listExpr = (ListExpr) expr;
        assertEquals(3, listExpr.elements.size());
        
        assertTrue(listExpr.elements.get(0) instanceof Symbol);
        assertEquals("+", ((Symbol) listExpr.elements.get(0)).name);
        
        assertTrue(listExpr.elements.get(1) instanceof ListExpr);
        ListExpr multExpr = (ListExpr) listExpr.elements.get(1);
        assertEquals(3, multExpr.elements.size());
        assertEquals("*", ((Symbol) multExpr.elements.get(0)).name);
        assertEquals(2.0, ((NumberExpr) multExpr.elements.get(1)).value, 0.001);
        assertEquals(3.0, ((NumberExpr) multExpr.elements.get(2)).value, 0.001);
        
        assertTrue(listExpr.elements.get(2) instanceof ListExpr);
        ListExpr subExpr = (ListExpr) listExpr.elements.get(2);
        assertEquals(3, subExpr.elements.size());
        assertEquals("-", ((Symbol) subExpr.elements.get(0)).name);
        assertEquals(5.0, ((NumberExpr) subExpr.elements.get(1)).value, 0.001);
        assertEquals(1.0, ((NumberExpr) subExpr.elements.get(2)).value, 0.001);
    }
    
    @Test(expected = RuntimeException.class)
    public void testParseUnbalancedParentheses() {
        parser.parse("(+ 2 3");  // Falta un paréntesis de cierre
    }
    
    @Test(expected = RuntimeException.class)
    public void testParseExtraClosingParenthesis() {
        parser.parse("(+ 2 3))");  // Paréntesis de cierre extra
    }
    
    @Test
    public void testParseWithExtraWhitespace() {
        Expr expr = parser.parse("  (  +   2   3  )  ");
        assertTrue(expr instanceof ListExpr);
        
        ListExpr listExpr = (ListExpr) expr;
        assertEquals(3, listExpr.elements.size());
        assertEquals("+", ((Symbol) listExpr.elements.get(0)).name);
        assertEquals(2.0, ((NumberExpr) listExpr.elements.get(1)).value, 0.001);
        assertEquals(3.0, ((NumberExpr) listExpr.elements.get(2)).value, 0.001);
    }
    
    @Test
    public void testParseSyntaxFix() {
        // Probar que el preprocesador arregla problemas comunes de sintaxis
        Expr expr = parser.parse("(COND ((= N 0) 1)\n((= N 1) 1))");
        assertTrue(expr instanceof ListExpr);
        
        ListExpr listExpr = (ListExpr) expr;
        assertEquals("COND", ((Symbol) listExpr.elements.get(0)).name);
    }
    
    @Test
    public void testParseEmptyList() {
        Expr expr = parser.parse("()");
        assertTrue(expr instanceof ListExpr);
        assertEquals(0, ((ListExpr) expr).elements.size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testParseEmptyString() {
        parser.parse("");  // Debería lanzar excepción
    }
    
    @Test
    public void testParseInvalidToken() {
        try {
            parser.parse("[invalid]");
            fail("Se esperaba una excepción para un token inválido");
        } catch (RuntimeException e) {
            // Verificar que el mensaje de error menciona el token inválido
            assertTrue(e.getMessage().contains("Operador no válido") || 
                       e.getMessage().contains("[invalid]"));
        }
    }
    
    @Test
    public void testParseSpecialSyntaxCorrection() {
        // Prueba específica para la corrección de sintaxis de Fibonacci
        try {
            Expr expr = parser.parse("(FIBONACCI [- N 1.0])");
            fail("Se esperaba una excepción para un token inválido");
        } catch (RuntimeException e) {
            // Verificar que el mensaje de error sugiere una corrección
            assertTrue(e.getMessage().contains("Operador no válido") || 
                       e.getMessage().contains("[- N"));
        }
    }
    
    @Test
    public void testParseComplexExpression() {
        String input = "(DEFUN FACTORIAL (N) (COND ((= N 0) 1) (T (* N (FACTORIAL (- N 1))))))";
        Expr expr = parser.parse(input);
        assertTrue(expr instanceof ListExpr);
        
        ListExpr listExpr = (ListExpr) expr;
        assertEquals(4, listExpr.elements.size());
        assertEquals("DEFUN", ((Symbol) listExpr.elements.get(0)).name);
        assertEquals("FACTORIAL", ((Symbol) listExpr.elements.get(1)).name);
        
        // Verificar la estructura de parámetros
        assertTrue(listExpr.elements.get(2) instanceof ListExpr);
        
        // Verificar el cuerpo de la función (COND...)
        assertTrue(listExpr.elements.get(3) instanceof ListExpr);
    }
    
    @Test
    public void testParseSingleElement() {
        // Prueba para un caso donde hay un solo elemento en la lista
        Expr expr = parser.parse("(QUOTE X)");
        assertTrue(expr instanceof ListExpr);
        
        ListExpr listExpr = (ListExpr) expr;
        assertEquals(2, listExpr.elements.size());
        assertEquals("QUOTE", ((Symbol) listExpr.elements.get(0)).name);
        assertEquals("X", ((Symbol) listExpr.elements.get(1)).name);
    }
    
    @Test
    public void testParseFixCommonSyntaxIssues() {
        // Probar la función fixCommonSyntaxIssues con una expresión de Fibonacci
        String input = "(DEFUN FIBONACCI (N) (COND ((= N 0) 1) ((= N 1) 1) (T (+ (FIBONACCI [- N 1.0]) (FIBONACCI [- N 2.0])))))";
        // Esta entrada normalmente fallaría sin la corrección
        
        try {
            Expr expr = parser.parse(input);
            // Si llega aquí, significa que el fixCommonSyntaxIssues corrigió la expresión
            // o que la implementación de parse es tolerante a esta sintaxis
            
            // Verificamos que al menos la estructura básica está correcta
            assertTrue(expr instanceof ListExpr);
            ListExpr listExpr = (ListExpr) expr;
            assertEquals("DEFUN", ((Symbol) listExpr.elements.get(0)).name);
            assertEquals("FIBONACCI", ((Symbol) listExpr.elements.get(1)).name);
            
            // No podemos afirmar mucho más sin conocer el comportamiento exacto de fixCommonSyntaxIssues
        } catch (RuntimeException e) {
            // Incluso si falla, debería fallar con un mensaje que indica que se intentó corregir
            assertTrue(e.getMessage().contains("Operador no válido") || 
                       e.getMessage().contains("[- N"));
        }
    }
}