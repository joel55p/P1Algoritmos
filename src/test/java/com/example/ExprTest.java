package com.example;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para las clases de expresiones (Expr, NumberExpr, BooleanExpr, Symbol, ListExpr)
 */
public class ExprTest {

    @Test
    public void testNumberExpr() {
        // Verificar la creación y representación de string para NumberExpr
        NumberExpr num = new NumberExpr(42.5);
        assertEquals(42.5, num.value, 0.001);
        assertEquals("42.5", num.toString());
    }

    @Test
    public void testBooleanExpr() {
        // Verificar la creación y representación de string para BooleanExpr
        BooleanExpr trueExpr = new BooleanExpr(true);
        BooleanExpr falseExpr = new BooleanExpr(false);
        
        assertTrue(trueExpr.value);
        assertFalse(falseExpr.value);
        
        assertEquals("T", trueExpr.toString());
        assertEquals("NIL", falseExpr.toString());
    }

    @Test
    public void testSymbol() {
        // Verificar la creación y representación de string para Symbol
        Symbol sym = new Symbol("VARIABLE");
        assertEquals("VARIABLE", sym.name);
        assertEquals("VARIABLE", sym.toString());
    }

    @Test
    public void testListExpr() {
        // Crear una lista con varios tipos de expresiones
        java.util.List<Expr> elements = new java.util.ArrayList<>();
        elements.add(new Symbol("PLUS"));
        elements.add(new NumberExpr(10));
        elements.add(new NumberExpr(20));
        
        ListExpr list = new ListExpr(elements);
        
        // Verificar que la lista contiene los elementos correctos
        assertEquals(3, list.elements.size());
        assertEquals("PLUS", ((Symbol)list.elements.get(0)).name);
        assertEquals(10.0, ((NumberExpr)list.elements.get(1)).value, 0.001);
        assertEquals(20.0, ((NumberExpr)list.elements.get(2)).value, 0.001);
        
        // Verificar la representación de string
        assertEquals("[PLUS 10.0 20.0]", list.toString());
        
        // Verificar el getter
        assertEquals(elements, list.getElements());
    }
}