package com.example;
import java.util.*;

public class UserFunction {
    List<Symbol> parameters;
    List<Expr> body;

    public UserFunction(List<Symbol> parameters, List<Expr> body) {
        this.parameters = parameters;
        this.body = body;
    }

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
