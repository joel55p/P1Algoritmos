package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas de integración para el intérprete Lisp
 * Verifica la interacción correcta entre los diferentes componentes
 */
public class LispIntegrationTest {
    
    private LispParser parser;
    private LispEvaluator evaluator;
    
    @Before
    public void setUp() {
        parser = new LispParser();
        evaluator = new LispEvaluator();
    }
    
    @Test
    public void testFullPipelineSimpleExpression() {
        // Probar una expresión simple en todo el pipeline
        String input = "(+ 2 3)";
        String normalized = LispInputPreprocessor.normalize(input);
        Expr parsed = parser.parse(normalized);
        Expr result = evaluator.eval(parsed);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(5.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithNestedExpressions() {
        // Probar una expresión anidada en todo el pipeline
        String input = "(+ (* 2 3) (- 10 4))";
        String normalized = LispInputPreprocessor.normalize(input);
        Expr parsed = parser.parse(normalized);
        Expr result = evaluator.eval(parsed);
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(12.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithVariables() {
        // Definir y usar variables
        evaluator.eval(parser.parse("(SETQ X 5)"));
        evaluator.eval(parser.parse("(SETQ Y 10)"));
        
        Expr result = evaluator.eval(parser.parse("(+ X Y)"));
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(15.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithUserDefinedFunction() {
        // Definir y llamar a una función
        evaluator.eval(parser.parse("(DEFUN DOUBLE (X) (* X 2))"));
        
        Expr result = evaluator.eval(parser.parse("(DOUBLE 7)"));
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(14.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithRecursiveFunction() {
        // Definir y llamar a una función recursiva (factorial)
        String factorialDef = "(DEFUN FACTORIAL (N) (COND ((= N 0) 1) (T (* N (FACTORIAL (- N 1))))))";
        evaluator.eval(parser.parse(factorialDef));
        
        Expr result = evaluator.eval(parser.parse("(FACTORIAL 5)"));
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(120.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithConditionals() {
        // Probar condicionales
        evaluator.eval(parser.parse("(SETQ X 10)"));
        
        // Caso verdadero
        Expr resultTrue = evaluator.eval(parser.parse("(COND ((> X 5) 1) (T 0))"));
        assertTrue(resultTrue instanceof NumberExpr);
        assertEquals(1.0, ((NumberExpr) resultTrue).value, 0.001);
        
        // Caso falso
        evaluator.eval(parser.parse("(SETQ X 3)"));
        Expr resultFalse = evaluator.eval(parser.parse("(COND ((> X 5) 1) (T 0))"));
        assertTrue(resultFalse instanceof NumberExpr);
        assertEquals(0.0, ((NumberExpr) resultFalse).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithMultipleStatements() {
        // Ejecutar una secuencia de sentencias
        evaluator.eval(parser.parse("(SETQ X 1)"));
        evaluator.eval(parser.parse("(SETQ Y 2)"));
        evaluator.eval(parser.parse("(SETQ Z (+ X Y))"));
        
        Expr result = evaluator.eval(parser.parse("(* Z 3)"));
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(9.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithComplexFunction() {
        // Definir y llamar a una función más compleja
        String fibDef = 
            "(DEFUN FIBONACCI (N) " +
            "   (COND ((= N 0) 1) " +
            "         ((= N 1) 1) " +
            "         (T (+ (FIBONACCI (- N 1)) " +
            "               (FIBONACCI (- N 2))))))";
        
        // Normalizar, parsear y evaluar la definición
        String normalizedDef = LispInputPreprocessor.normalize(fibDef);
        evaluator.eval(parser.parse(normalizedDef));
        
        // Llamar a la función
        Expr result = evaluator.eval(parser.parse("(FIBONACCI 6)"));
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(13.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testFullPipelineWithNestedFunctions() {
        // Definir múltiples funciones e invocarlas anidadamente
        evaluator.eval(parser.parse("(DEFUN SQUARE (X) (* X X))"));
        evaluator.eval(parser.parse("(DEFUN CUBE (X) (* X (SQUARE X)))"));
        
        Expr result = evaluator.eval(parser.parse("(CUBE 2)"));
        
        assertTrue(result instanceof NumberExpr);
        assertEquals(8.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testMultilineInputProcessing() {
        // Entrada multilínea con comentarios y espacios extra
        String input = 
            "(DEFUN FACTORIAL (N)  ; Función factorial\n" +
            "   (COND ((= N 0) 1)  ;; Caso base\n" +
            "         (T (* N      ; Recursión\n" +
            "               (FACTORIAL (- N 1))))))\n";
        
        // Normalizar la entrada
        String normalized = LispInputPreprocessor.normalize(input);
        
        // Verificar que los comentarios se eliminaron y los espacios se normalizaron
        assertFalse(normalized.contains(";"));
        assertFalse(normalized.contains("\n"));
        
        // Parsear y evaluar
        Expr parsed = parser.parse(normalized);
        Expr result = evaluator.eval(parsed);
        
        // Verificar que la función se definió correctamente
        assertTrue(result instanceof Symbol);
        assertEquals("FACTORIAL", ((Symbol) result).name);
        
        // Probar la función
        Expr callResult = evaluator.eval(parser.parse("(FACTORIAL 4)"));
        assertTrue(callResult instanceof NumberExpr);
        assertEquals(24.0, ((NumberExpr) callResult).value, 0.001);
    }
}