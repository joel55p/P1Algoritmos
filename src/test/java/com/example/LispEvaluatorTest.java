package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Universidad del Valle de Guatemala

 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase LispEvaluator (evaluador de expresiones)
 */
public class LispEvaluatorTest {
    
    private LispEvaluator evaluator;
    private LispParser parser;
    
    @Before
    public void setUp() {
        evaluator = new LispEvaluator();
        parser = new LispParser();
    }
    
    // Pruebas existentes...
    
    // Pruebas adicionales para aumentar cobertura
    
    @Test(expected = RuntimeException.class)
    public void testEvalEmptyList() {
        List<Expr> emptyList = new ArrayList<>();
        ListExpr listExpr = new ListExpr(emptyList);
        
        // Al evaluar una lista vacía debería devolver la misma lista
        Expr result = evaluator.eval(listExpr);
        assertEquals(listExpr, result);
        
        // Pero al evaluar una lista con un elemento no reconocido debería fallar
        List<Expr> invalidList = new ArrayList<>();
        invalidList.add(new NumberExpr(1)); // Un número no puede ser un operador
        ListExpr invalidListExpr = new ListExpr(invalidList);
        
        evaluator.eval(invalidListExpr); // Esto debería lanzar excepción
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalInvalidExpression() {
        // Intentar evaluar una expresión no reconocida debería fallar
        // Esto debería entrar en el último `throw new RuntimeException("Expresión no reconocida")`
        Expr customExpr = new Expr() {}; // Una subclase anónima de Expr
        evaluator.eval(customExpr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalSetqWithInvalidArguments() {
        // SETQ con menos argumentos
        Expr expr = parser.parse("(SETQ X)");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalSetqWithNonSymbol() {
        // SETQ con un no-símbolo como primer argumento
        Expr expr = parser.parse("(SETQ 123 456)");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalQuoteWithInvalidArguments() {
        // QUOTE sin argumentos
        Expr expr = parser.parse("(QUOTE)");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalDefunWithInvalidArguments() {
        // DEFUN con menos argumentos
        Expr expr = parser.parse("(DEFUN)");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalDefunWithNonSymbolName() {
        // DEFUN con un no-símbolo como nombre
        Expr expr = parser.parse("(DEFUN 123 (X) (+ X 1))");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalDefunWithNonListParams() {
        // DEFUN con parámetros no-lista
        Expr expr = parser.parse("(DEFUN TEST 123 (+ X 1))");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalDefunWithNonSymbolParams() {
        // DEFUN con parámetros que no son símbolos
        Expr expr = parser.parse("(DEFUN TEST (123) (+ 123 1))");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalCondWithNonListCondition() {
        // COND con una condición que no es lista
        Expr expr = parser.parse("(COND 123)");
        evaluator.eval(expr);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalCondWithInvalidConditionPair() {
        // COND con un par condición-resultado inválido (más o menos de 2 elementos)
        Expr expr = parser.parse("(COND ((= 1 1)))");
        evaluator.eval(expr);
    }
    
    @Test
    public void testEvalBuiltinWithInvalidArguments() {
        try {
            // Intentar una operación con argumentos no numéricos
            Expr expr = parser.parse("(+ \"a\" 2)");
            evaluator.eval(expr);
            fail("Debería haber lanzado una excepción");
        } catch (Exception e) {
            // Espera cualquier excepción
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetUserFunctions() {
        // Verificar que inicialmente no hay funciones definidas
        Map<String, UserFunction> functions = evaluator.getUserFunctions();
        assertNotNull(functions);
        assertEquals(0, functions.size());
        
        // Definir una función y verificar que se agregó correctamente
        evaluator.eval(parser.parse("(DEFUN DOUBLE (X) (* X 2))"));
        assertEquals(1, functions.size());
        assertTrue(functions.containsKey("DOUBLE"));
    }
    
    @Test
    public void testEvalBooleanConstants() {
        // Probar las constantes T y NIL
        Expr trueExpr = parser.parse("T");
        Expr resultTrue = evaluator.eval(trueExpr);
        assertTrue(resultTrue instanceof BooleanExpr);
        assertTrue(((BooleanExpr) resultTrue).value);
        
        Expr falseExpr = parser.parse("NIL");
        Expr resultFalse = evaluator.eval(falseExpr);
        assertTrue(resultFalse instanceof BooleanExpr);
        assertFalse(((BooleanExpr) resultFalse).value);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalCarWithEmptyList() {
        // Crear una lista vacía
        List<Expr> emptyElements = new ArrayList<>();
        ListExpr emptyListExpr = new ListExpr(emptyElements);
        
        // Definir la lista vacía como variable
        evaluator.variables.put("EMPTY-LIST", emptyListExpr);
        
        // Intentar usar CAR con una lista vacía debe fallar
        evaluator.eval(parser.parse("(CAR EMPTY-LIST)"));
    }
    
    @Test
    public void testEvalListWithNonSymbolFirst() {
        try {
            // Crear una lista cuyo primer elemento no es un símbolo
            Expr expr = parser.parse("((1 2) 3 4)");
            evaluator.eval(expr);
            fail("Se esperaba una excepción");
        } catch (RuntimeException e) {
            // Se espera que falle con un mensaje sobre operador no válido
            assertTrue(e.getMessage().contains("Operador no válido"));
        }
    }
}