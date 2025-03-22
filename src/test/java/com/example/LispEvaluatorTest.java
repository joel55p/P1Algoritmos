package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;

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
    
    @Test
    public void testEvalNumberExpr() {
        Expr expr = new NumberExpr(42);
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(42.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalSymbol() {
        // Definir una variable
        evaluator.variables.put("X", new NumberExpr(42));
        
        Expr expr = new Symbol("X");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(42.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalUndefinedSymbol() {
        Expr expr = new Symbol("UNDEFINED");
        evaluator.eval(expr);  // Debería lanzar una excepción
    }
    
    @Test
    public void testEvalAddition() {
        Expr expr = parser.parse("(+ 2 3)");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(5.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalSubtraction() {
        Expr expr = parser.parse("(- 5 3)");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(2.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalMultiplication() {
        Expr expr = parser.parse("(* 2 3)");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(6.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalDivision() {
        Expr expr = parser.parse("(/ 6 3)");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(2.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalEquals() {
        Expr exprTrue = parser.parse("(= 2 2)");
        Expr resultTrue = evaluator.eval(exprTrue);
        
        assertTrue(resultTrue instanceof BooleanExpr);
        assertTrue(((BooleanExpr) resultTrue).value);
        
        Expr exprFalse = parser.parse("(= 2 3)");
        Expr resultFalse = evaluator.eval(exprFalse);
        
        assertTrue(resultFalse instanceof BooleanExpr);
        assertFalse(((BooleanExpr) resultFalse).value);
    }
    
    @Test
    public void testEvalLessThan() {
        Expr exprTrue = parser.parse("(< 2 3)");
        Expr resultTrue = evaluator.eval(exprTrue);
        
        assertTrue(resultTrue instanceof BooleanExpr);
        assertTrue(((BooleanExpr) resultTrue).value);
        
        Expr exprFalse = parser.parse("(< 3 2)");
        Expr resultFalse = evaluator.eval(exprFalse);
        
        assertTrue(resultFalse instanceof BooleanExpr);
        assertFalse(((BooleanExpr) resultFalse).value);
    }
    
    @Test
    public void testEvalGreaterThan() {
        Expr exprTrue = parser.parse("(> 3 2)");
        Expr resultTrue = evaluator.eval(exprTrue);
        
        assertTrue(resultTrue instanceof BooleanExpr);
        assertTrue(((BooleanExpr) resultTrue).value);
        
        Expr exprFalse = parser.parse("(> 2 3)");
        Expr resultFalse = evaluator.eval(exprFalse);
        
        assertTrue(resultFalse instanceof BooleanExpr);
        assertFalse(((BooleanExpr) resultFalse).value);
    }
    
    @Test
    public void testEvalSetq() {
        Expr expr = parser.parse("(SETQ X 42)");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(42.0, ((NumberExpr) result).value, 0.001);
        
        // Verificar que la variable se haya definido correctamente
        assertTrue(evaluator.variables.containsKey("X"));
        assertEquals(42.0, ((NumberExpr) evaluator.variables.get("X")).value, 0.001);
    }
    
    @Test
    public void testEvalQuote() {
        Expr expr = parser.parse("(QUOTE (1 2 3))");
        Expr result = evaluator.eval(expr);
        
        assertTrue(result instanceof ListExpr);
        ListExpr listExpr = (ListExpr) result;
        assertEquals(3, listExpr.elements.size());
        assertEquals(1.0, ((NumberExpr) listExpr.elements.get(0)).value, 0.001);
        assertEquals(2.0, ((NumberExpr) listExpr.elements.get(1)).value, 0.001);
        assertEquals(3.0, ((NumberExpr) listExpr.elements.get(2)).value, 0.001);
    }
    
    @Test
    public void testEvalCond() {
        // Definir una variable para la prueba
        evaluator.variables.put("X", new NumberExpr(1));
        
        // Caso donde la primera condición es verdadera
        Expr exprFirstTrue = parser.parse("(COND ((= X 1) 10) ((= X 2) 20))");
        Expr resultFirstTrue = evaluator.eval(exprFirstTrue);
        
        assertTrue(resultFirstTrue instanceof NumberExpr);
        assertEquals(10.0, ((NumberExpr) resultFirstTrue).value, 0.001);
        
        // Caso donde la segunda condición es verdadera
        evaluator.variables.put("X", new NumberExpr(2));
        Expr exprSecondTrue = parser.parse("(COND ((= X 1) 10) ((= X 2) 20))");
        Expr resultSecondTrue = evaluator.eval(exprSecondTrue);
        
        assertTrue(resultSecondTrue instanceof NumberExpr);
        assertEquals(20.0, ((NumberExpr) resultSecondTrue).value, 0.001);
        
        // Caso donde ninguna condición es verdadera pero hay una condición por defecto
        evaluator.variables.put("X", new NumberExpr(3));
        Expr exprDefaultCase = parser.parse("(COND ((= X 1) 10) ((= X 2) 20) (T 30))");
        Expr resultDefaultCase = evaluator.eval(exprDefaultCase);
        
        assertTrue(resultDefaultCase instanceof NumberExpr);
        assertEquals(30.0, ((NumberExpr) resultDefaultCase).value, 0.001);
    }
    
    @Test
    public void testEvalDefunAndCall() {
        // Definir una función simple
        Expr defunExpr = parser.parse("(DEFUN SQUARE (N) (* N N))");
        evaluator.eval(defunExpr);
        
        // Verificar que la función se definió correctamente
        // Necesitas un método getter en LispEvaluator o hacer userFunctions públicos
        // Usamos aquí la sintaxis como si hubiera un método getUserFunctions()
        assertTrue(evaluator.getUserFunctions().containsKey("SQUARE"));
        
        // Llamar a la función
        Expr callExpr = parser.parse("(SQUARE 5)");
        Expr result = evaluator.eval(callExpr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(25.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalFactorial() {
        // Definir la función factorial
        Expr defunExpr = parser.parse("(DEFUN FACTORIAL (N) (COND ((= N 0) 1) (T (* N (FACTORIAL (- N 1))))))");
        evaluator.eval(defunExpr);
        
        // Calcular factorial de 5
        Expr callExpr = parser.parse("(FACTORIAL 5)");
        Expr result = evaluator.eval(callExpr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(120.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalFibonacci() {
        // Definir la función Fibonacci
        Expr defunExpr = parser.parse(
            "(DEFUN FIBONACCI (N) (COND ((= N 0) 1) ((= N 1) 1) (T (+ (FIBONACCI (- N 1)) (FIBONACCI (- N 2))))))");
        evaluator.eval(defunExpr);
        
        // Calcular Fibonacci de 5
        Expr callExpr = parser.parse("(FIBONACCI 5)");
        Expr result = evaluator.eval(callExpr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(8.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testEvalCar() {
        // Crear una lista
        List<Expr> elements = new ArrayList<>();
        elements.add(new NumberExpr(1));
        elements.add(new NumberExpr(2));
        elements.add(new NumberExpr(3));
        ListExpr listExpr = new ListExpr(elements);
        
        // Definir la lista como una variable
        evaluator.variables.put("MY-LIST", listExpr);
        
        // Usar CAR para obtener el primer elemento
        Expr carExpr = parser.parse("(CAR MY-LIST)");
        Expr result = evaluator.eval(carExpr);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(1.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test(expected = RuntimeException.class)
    public void testEvalCarWithNonList() {
        // Intentar usar CAR con un número (no una lista)
        Expr expr = parser.parse("(CAR 42)");
        evaluator.eval(expr);  // Debería lanzar una excepción
    }
    
    @Test
    public void testEvalAtom() {
        // Probar ATOM con un número (debería ser verdadero)
        Expr exprNumber = parser.parse("(ATOM 42)");
        Expr resultNumber = evaluator.eval(exprNumber);
        
        assertTrue(resultNumber instanceof BooleanExpr);
        assertTrue(((BooleanExpr) resultNumber).value);
        
        // Probar ATOM con una lista (debería ser falso)
        evaluator.variables.put("MY-LIST", parser.parse("(1 2 3)"));
        Expr exprList = parser.parse("(ATOM MY-LIST)");
        Expr resultList = evaluator.eval(exprList);
        
        assertTrue(resultList instanceof BooleanExpr);
        assertFalse(((BooleanExpr) resultList).value);
    }
    
    @Test
    public void testEvalList() {
        // Probar LIST con un número (debería ser falso)
        Expr exprNumber = parser.parse("(LIST 42)");
        Expr resultNumber = evaluator.eval(exprNumber);
        
        assertTrue(resultNumber instanceof BooleanExpr);
        assertFalse(((BooleanExpr) resultNumber).value);
        
        // Probar LIST con una lista (debería ser verdadero)
        evaluator.variables.put("MY-LIST", parser.parse("(1 2 3)"));
        Expr exprList = parser.parse("(LIST MY-LIST)");
        Expr resultList = evaluator.eval(exprList);
        
        assertTrue(resultList instanceof BooleanExpr);
        assertTrue(((BooleanExpr) resultList).value);
    }
}