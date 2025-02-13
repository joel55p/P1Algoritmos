import java.util.List;   /*Como tal para representar funciones definidas por el usuario en Lisp */

public class FunctionDef {
    List<String> params;
    Object body;

    public FunctionDef(List<String> params, Object body) {
        this.params = params;
        this.body = body;
    }
}
