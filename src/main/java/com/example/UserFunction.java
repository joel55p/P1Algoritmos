/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Implementación de funciones definidas por el usuario para el intérprete Lisp.
 * Almacena los parámetros y el cuerpo de una función definida con DEFUN y
 * permite su invocación durante la evaluación.
 */
package com.example;
import java.util.*;

public class UserFunction {
    /** Lista de símbolos que representan los parámetros de la función */
    List<Symbol> parameters;
    
    /** Lista de expresiones que componen el cuerpo de la función */
    List<Expr> body;

    /**
     * Constructor para crear una nueva función definida por el usuario.
     * 
     * @param parameters Lista de símbolos que representan los parámetros de la función
     * @param body Lista de expresiones que componen el cuerpo de la función
     */
    public UserFunction(List<Symbol> parameters, List<Expr> body) {
        this.parameters = parameters;
        this.body = body;
    }

    /**
     * Invoca la función con los argumentos proporcionados.
     * Crea un ámbito local para las variables de la función y ejecuta el cuerpo.
     * 
     * @param args Lista de expresiones que son los valores de los argumentos
     * @param evaluator El evaluador Lisp para evaluar las expresiones
     * @return El resultado de evaluar el cuerpo de la función
     * @throws RuntimeException Si se proporciona un número incorrecto de argumentos
     */
    public Expr invoke(List<Expr> args, LispEvaluator evaluator) {
        if (args.size() != parameters.size()) {
            throw new RuntimeException("Error: Número incorrecto de argumentos.");
        }

        // Guardar variables originales
        Map<String, Expr> backup = new HashMap<>(evaluator.variables);

        try {
            // Asignar los valores de los argumentos a las variables locales
            for (int i = 0; i < parameters.size(); i++) {
                evaluator.variables.put(parameters.get(i).name, args.get(i));
            }

            // Evaluar cada expresión en el cuerpo y devolver el último resultado
            Expr result = null;
            for (Expr expr : body) {
                result = evaluator.eval(expr);
            }
            return result;
        } finally {
            // Restaurar las variables originales
            evaluator.variables = backup;
        }
    }
}