import java.util.*;
import java.util.function.Function;


public class LispEvaluator {
    private Map<String, Expr> variables = new HashMap<>();
    private Map<String, Function<List<Expr>, Expr>> builtins = new HashMap<>();

    public LispEvaluator() {
        // Operaciones matemáticas básicas que se ejecutarán en el lisp
        builtins.put("+", args -> new NumberExpr(((NumberExpr) args.get(0)).value + ((NumberExpr) args.get(1)).value));
        builtins.put("-", args -> new NumberExpr(((NumberExpr) args.get(0)).value - ((NumberExpr) args.get(1)).value));
        builtins.put("*", args -> new NumberExpr(((NumberExpr) args.get(0)).value * ((NumberExpr) args.get(1)).value));
        builtins.put("/", args -> new NumberExpr(((NumberExpr) args.get(0)).value / ((NumberExpr) args.get(1)).value));
    }

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

            Expr first = eval(elements.get(0));
            if (first instanceof Symbol) {
                String funcName = ((Symbol) first).toString();
                if (builtins.containsKey(funcName)) {
                    List<Expr> args = new ArrayList<>();
                    for (int i = 1; i < elements.size(); i++) {
                        args.add(eval(elements.get(i))); // Evaluar argumentos
                    }
                    return builtins.get(funcName).apply(args);
                } else if ("SETQ".equals(funcName)) {
                    if (elements.size() != 3 || !(elements.get(1) instanceof Symbol)) {
                        throw new RuntimeException("Error en SETQ: Uso incorrecto");
                    }
                    String varName = ((Symbol) elements.get(1)).toString();
                    Expr value = eval(elements.get(2));
                    variables.put(varName, value);
                    return value;
                }
            }
            throw new RuntimeException("Operador no válido: " + first);
        }
        throw new RuntimeException("Expresión no reconocida");
    }
}