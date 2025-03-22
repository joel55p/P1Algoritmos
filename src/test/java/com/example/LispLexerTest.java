package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispLexer (analizador léxico)
 */
public class LispLexerTest {

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
}