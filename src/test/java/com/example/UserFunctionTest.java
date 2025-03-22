package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Pruebas unitarias para la clase UserFunction
 */
public class UserFunctionTest {
    
    @Test
    public void testUserFunctionCreation() {
        // Crear parámetros
        List<Symbol> parameters = new ArrayList<>();
        parameters.add(new Symbol("X"));
        parameters.add(new Symbol("Y"));
        
        // Crear cuerpo
        List<Expr> body = new ArrayList<>();
        List<Expr> addElements = new ArrayList<>();
        addElements.add(new Symbol("+"));
        addElements.add(new Symbol("X"));
        addElements.add(new Symbol("Y"));
        body.add(new ListExpr(addElements));
        
        // Crear función
        UserFunction function = new UserFunction(parameters, body);
        
        // Verificar que los parámetros y el cuerpo se almacenaron correctamente
        assertEquals(2, function.parameters.size());
        assertEquals("X", function.parameters.get(0).name);
        assertEquals("Y", function.parameters.get(1).name);
        
        assertEquals(1, function.body.size());
        assertTrue(function.body.get(0) instanceof ListExpr);
    }
    
    @Test
    public void testUserFunctionInvoke() {
        // Crear una función simple que sume dos números
        List<Symbol> parameters = new ArrayList<>();
        parameters.add(new Symbol("X"));
        parameters.add(new Symbol("Y"));
        
        List<Expr> body = new ArrayList<>();
        List<Expr> addElements = new ArrayList<>();
        addElements.add(new Symbol("+"));
        addElements.add(new Symbol("X"));
        addElements.add(new Symbol("Y"));
        body.add(new ListExpr(addElements));
        
        UserFunction function = new UserFunction(parameters, body);
        
        // Crear argumentos
        List<Expr> args = new ArrayList<>();
        args.add(new NumberExpr(10));
        args.add(new NumberExpr(20));
        
        // Invocar la función
        LispEvaluator evaluator = new LispEvaluator();
        Expr result = function.invoke(args, evaluator);
        
        // Verificar resultado
        assertTrue(result instanceof NumberExpr);
        assertEquals(30.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test(expected = RuntimeException.class)
    public void testUserFunctionInvokeWithWrongNumberOfArguments() {
        // Crear una función con dos parámetros
        List<Symbol> parameters = new ArrayList<>();
        parameters.add(new Symbol("X"));
        parameters.add(new Symbol("Y"));
        
        List<Expr> body = new ArrayList<>();
        body.add(new Symbol("X"));  // Solo devuelve X
        
        UserFunction function = new UserFunction(parameters, body);
        
        // Crear argumentos (solo uno, debería fallar)
        List<Expr> args = new ArrayList<>();
        args.add(new NumberExpr(10));
        
        // Invocar la función - debería lanzar una excepción
        LispEvaluator evaluator = new LispEvaluator();
        function.invoke(args, evaluator);
    }
    
    @Test
    public void testUserFunctionWithVariablesRespected() {
        // Crear una función que use una variable global y un parámetro
        List<Symbol> parameters = new ArrayList<>();
        parameters.add(new Symbol("X"));
        
        List<Expr> body = new ArrayList<>();
        List<Expr> addElements = new ArrayList<>();
        addElements.add(new Symbol("+"));
        addElements.add(new Symbol("X"));
        addElements.add(new Symbol("GLOBAL"));
        body.add(new ListExpr(addElements));
        
        UserFunction function = new UserFunction(parameters, body);
        
        // Configurar el evaluador con una variable global
        LispEvaluator evaluator = new LispEvaluator();
        evaluator.variables.put("GLOBAL", new NumberExpr(100));
        
        // Invocar la función
        List<Expr> args = new ArrayList<>();
        args.add(new NumberExpr(50));
        
        Expr result = function.invoke(args, evaluator);
        
        // Verificar resultado: 50 + 100 = 150
        assertTrue(result instanceof NumberExpr);
        assertEquals(150.0, ((NumberExpr) result).value, 0.001);
    }
    
    @Test
    public void testUserFunctionVariableScopeIsolation() {
        // Crear una función que establezca un valor para su parámetro
        List<Symbol> parameters = new ArrayList<>();
        parameters.add(new Symbol("X"));
        
        List<Expr> body = new ArrayList<>();
        // Cuerpo: (SETQ X (* X 2)) - duplica X
        List<Expr> setqElements = new ArrayList<>();
        setqElements.add(new Symbol("SETQ"));
        setqElements.add(new Symbol("X"));
        
        List<Expr> multElements = new ArrayList<>();
        multElements.add(new Symbol("*"));
        multElements.add(new Symbol("X"));
        multElements.add(new NumberExpr(2));
        
        setqElements.add(new ListExpr(multElements));
        body.add(new ListExpr(setqElements));
        
        UserFunction function = new UserFunction(parameters, body);
        
        // Configurar el evaluador con una variable X que no debe cambiar
        LispEvaluator evaluator = new LispEvaluator();
        evaluator.variables.put("X", new NumberExpr(1000));
        
        // Invocar la función
        List<Expr> args = new ArrayList<>();
        args.add(new NumberExpr(25));
        
        Expr result = function.invoke(args, evaluator);
        
        // Verificar resultado: 25 * 2 = 50
        assertTrue(result instanceof NumberExpr);
        assertEquals(50.0, ((NumberExpr) result).value, 0.001);
        
        // Verificar que la variable global X no cambió
        assertTrue(evaluator.variables.containsKey("X"));
        assertEquals(1000.0, ((NumberExpr) evaluator.variables.get("X")).value, 0.001);
    }
}