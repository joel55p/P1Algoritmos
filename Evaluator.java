import java.util.*;/* Esta clase es porque evalúa las expresiones en el entorno dado, osea evalua en Lisp*/

public class Evaluator {
    public static Object eval(Object exp, Environment env) {
        if (exp instanceof Integer) return exp; // Retorna números directamente
        if (exp instanceof String) return env.getVariable((String) exp); // Retorna variables

        List<Object> list = (List<Object>) exp;
        String op = (String) list.get(0);  /*operador */
        System.out.println("Evaluando operación: " + op);

        switch (op) {
            case "+":  /*operaciones aritmeticas */
                return (Integer) eval(list.get(1), env) + (Integer) eval(list.get(2), env);
            case "*":
                return (Integer) eval(list.get(1), env) * (Integer) eval(list.get(2), env);
            case "SETQ":
                if (exp instanceof List && ((List<?>) exp).size() == 3) {
                    List<?> exprList = (List<?>) exp;
                    String varName = (String) exprList.get(1);
                    Object value = eval(exprList.get(2), env);
                    env.setVariable(varName, value);  // CORREGIDO: Usar setVariable()
                    return value;
                } else {
                    throw new RuntimeException("Error en SETQ: Sintaxis incorrecta.");
                }
            
            case "=":
                if (!(exp instanceof List)) {
                    throw new RuntimeException("La operación '=' espera una lista de argumentos.");
                }
            
                List<Object> eqArgs = ((List<Object>) exp).subList(1, ((List<Object>) exp).size());
            
                if (eqArgs.size() != 2) {
                    throw new RuntimeException("= requiere exactamente 2 argumentos");
                }
            
                Object left = eval(eqArgs.get(0), env);
                Object right = eval(eqArgs.get(1), env);
            
                // Debug: Ver qué valores está intentando comparar
                System.out.println("Comparando: " + left + " y " + right);
                System.out.println("Tipos: " + left.getClass().getSimpleName() + " y " + right.getClass().getSimpleName());
            
                if (left instanceof Number && right instanceof Number) {
                    return ((Number) left).doubleValue() == ((Number) right).doubleValue();
                } else {
                    throw new RuntimeException("= solo puede comparar números. Recibidos: " + left + " y " + right);
                }
    
            case "QUOTE":
                return list.get(1);
            case "DEFUN":
                if (exp instanceof List && ((List<?>) exp).size() >= 4) {
                    List<?> exprList = (List<?>) exp;
                    String functionName = (String) exprList.get(1);
                    List<String> params = (List<String>) exprList.get(2);
                    Object body = exprList.subList(3, exprList.size());
                    
                    FunctionDef function = new FunctionDef(params, body);
                    env.defineFunction(functionName, function);
                    return functionName;  // Retorna el nombre de la función
                } else {
                    throw new RuntimeException("Error en DEFUN: Sintaxis incorrecta.");
                }
            
            case "COND":
                for (int i = 1; i < list.size(); i++) {
                    List<Object> clause = (List<Object>) list.get(i);
                    if (Boolean.TRUE.equals(eval(clause.get(0), env))) {
                        return eval(clause.get(1), env);
                    }
                }
                return null;
            default:
                if (env.hasFunction(op)) {
                    FunctionDef fn = env.getFunction(op);
                    Map<String, Object> localEnv = new HashMap<>(env.getVariables()); // 🔹 Usamos getVariables()
                    for (int i = 0; i < fn.params.size(); i++) {
                        localEnv.put(fn.params.get(i), eval(list.get(i + 1), env));
                    }
                    return eval(fn.body, new Environment());
                }
                throw new RuntimeException("Operación desconocida: " + op);
        }
    }
}
