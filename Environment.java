import java.util.*; /*En esta clase se almacena variables (SETQ) y funciones (DEFUN) */

public class Environment {
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, FunctionDef> functions = new HashMap<>();

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object getVariable(String name) {
        return variables.getOrDefault(name, null);
    }

    public void defineFunction(String name, FunctionDef function) {
        functions.put(name, function);
    }

    public FunctionDef getFunction(String name) {
        return functions.get(name);
    }

    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }

    //  Nuevo método para acceder a las variables
    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }
}


/*osea como tal esta calse es la que maneja variables y funciones */