
/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Clase que implementa el evaluador de expresiones para el intérprete Lisp.
 * Se encarga de evaluar expresiones Lisp según las reglas semánticas del lenguaje,
 * incluyendo operaciones matemáticas, funciones integradas, construcciones especiales como
 * SETQ, QUOTE, DEFUN y COND, así como la evaluación de funciones definidas por el usuario.
 */
package com.example;

import java.util.*;
import java.util.function.Function;

public class LispEvaluator {
    /** Mapa que almacena las variables definidas en el entorno */
    public Map<String, Expr> variables = new HashMap<>();
    
    /** Mapa que almacena las funciones integradas del lenguaje */
    private Map<String, Function<List<Expr>, Expr>> builtins = new HashMap<>();
    
    /** Mapa que almacena las funciones definidas por el usuario */
    private Map<String, UserFunction> userFunctions = new HashMap<>();

    /**
     * Constructor del evaluador Lisp.
     * Inicializa todas las funciones integradas (builtins) y las constantes por defecto.
     */
    public LispEvaluator() {
        // Operaciones matemáticas básicas que se ejecutarán en el lisp
        builtins.put("+", args -> new NumberExpr(((NumberExpr) args.get(0)).value + ((NumberExpr) args.get(1)).value));
        builtins.put("-", args -> new NumberExpr(((NumberExpr) args.get(0)).value - ((NumberExpr) args.get(1)).value));
        builtins.put("*", args -> new NumberExpr(((NumberExpr) args.get(0)).value * ((NumberExpr) args.get(1)).value));
        builtins.put("/", args -> new NumberExpr(((NumberExpr) args.get(0)).value / ((NumberExpr) args.get(1)).value));
        builtins.put("=", args -> new BooleanExpr(((NumberExpr) args.get(0)).value == ((NumberExpr) args.get(1)).value));
        builtins.put("ATOM", args -> new BooleanExpr(!(args.get(0) instanceof ListExpr)));
        builtins.put("LIST", args -> new BooleanExpr(args.get(0) instanceof ListExpr));
        builtins.put("EQUAL", args -> new BooleanExpr(args.get(0).equals(args.get(1))));
        builtins.put("<", args -> new BooleanExpr(((NumberExpr) args.get(0)).value < ((NumberExpr) args.get(1)).value));
        builtins.put(">", args -> new BooleanExpr(((NumberExpr) args.get(0)).value > ((NumberExpr) args.get(1)).value));
        variables.put("T", new BooleanExpr(true));  // Definir T como verdadero
        variables.put("NIL", new BooleanExpr(false));  // Definir NIL como falso
        builtins.put("CAR", args -> {
            if (!(args.get(0) instanceof ListExpr)) {
                throw new RuntimeException("Error en CAR: argumento no es una lista.");
            }
            List<Expr> elements = ((ListExpr) args.get(0)).elements;
            if (elements.isEmpty()) {
                throw new RuntimeException("Error en CAR: lista vacía.");
            }
            return elements.get(0);
        });
    }

    /**
     * Evalúa una expresión Lisp según las reglas del lenguaje.
     * 
     * @param expr La expresión a evaluar
     * @return El resultado de la evaluación
     * @throws RuntimeException Si ocurre un error durante la evaluación
     */
    public Expr eval(Expr expr) {
        if (expr instanceof NumberExpr) {
            return expr;
        } else if (expr instanceof Symbol) {
            String name = ((Symbol) expr).toString();
            if (variables.containsKey(name)) {
                return variables.get(name);
            }
            throw new RuntimeException("Variable no definida: " + name);
        } else if (expr instanceof ListExpr) {
            List<Expr> elements = ((ListExpr) expr).elements;
            if (elements.isEmpty()) return expr;
    
            // No evalúes el operador antes de comprobar si es un built-in
            Expr first = elements.get(0);
            if (first instanceof Symbol) {
                String funcName = ((Symbol) first).name;  // Accede al nombre del símbolo
                if (builtins.containsKey(funcName)) {
                    List<Expr> args = new ArrayList<>();
                    for (int i = 1; i < elements.size(); i++) {
                        args.add(eval(elements.get(i))); // Evaluar argumentos
                    }
                    return builtins.get(funcName).apply(args);
                }
                // SETQ y QUOTE siguen igual
                else if ("SETQ".equals(funcName)) {
                    if (elements.size() != 3 || !(elements.get(1) instanceof Symbol)) {
                        throw new RuntimeException("Error en SETQ: Uso incorrecto");
                    }
                    String varName = ((Symbol) elements.get(1)).name;
                    Expr value = eval(elements.get(2));
                    variables.put(varName, value);
                    return value;
                }
                else if ("QUOTE".equals(funcName)) {
                    if (elements.size() != 2) {
                        throw new RuntimeException("Error en QUOTE: Uso incorrecto");
                    }
                    return elements.get(1);
                }
                else if ("DEFUN".equals(funcName)) {
                    if (elements.size() < 3 || !(elements.get(1) instanceof Symbol) || !(elements.get(2) instanceof ListExpr)) {
                        throw new RuntimeException("Error en DEFUN: Uso incorrecto");
                    }
                    String functionName = ((Symbol) elements.get(1)).name;
                    ListExpr paramList = (ListExpr) elements.get(2);
                    List<Symbol> parameters = new ArrayList<>();
                
                    for (Expr param : paramList.elements) {
                        if (!(param instanceof Symbol)) {
                            throw new RuntimeException("Error en DEFUN: Parámetros deben ser símbolos.");
                        }
                        parameters.add((Symbol) param);
                    }
                
                    List<Expr> body = elements.subList(3, elements.size());
                    userFunctions.put(functionName, new UserFunction(parameters, body));
                
                    return new Symbol(functionName);
                }
                else if (userFunctions.containsKey(funcName)) {
                    UserFunction userFunction = userFunctions.get(funcName);
                    List<Expr> args = new ArrayList<>();
                    for (int i = 1; i < elements.size(); i++) {
                        args.add(eval(elements.get(i)));
                    }
                    return userFunction.invoke(args, this);
                }
                else if ("COND".equals(funcName)) {
                    for (int i = 1; i < elements.size(); i++) {
                        if (!(elements.get(i) instanceof ListExpr)) {
                            throw new RuntimeException("Error en COND: cada condición debe ser una lista.");
                        }
                        List<Expr> conditionPair = ((ListExpr) elements.get(i)).elements;
                        
                        if (conditionPair.size() != 2) {
                            throw new RuntimeException("Error en COND: cada cláusula debe tener exactamente dos elementos (condición y resultado).");
                        }
                
                        Expr condition = eval(conditionPair.get(0));  // Evalúa la condición
                        if (condition instanceof BooleanExpr && ((BooleanExpr) condition).value) {
                            return eval(conditionPair.get(1));  // Si es verdadero, evalúa y retorna el resultado
                        }
                    }
                    throw new RuntimeException("Error en COND: ninguna condición fue verdadera.");
                }
            }
    
            throw new RuntimeException("Operador no válido: " + first);
        }
        throw new RuntimeException("Expresión no reconocida");
    }
}