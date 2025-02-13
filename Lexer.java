import java.util.*;   /*esta clase su funcionalidad como tal es que convierte code Lisp en una lista de tokens, osea tokeniza la entrada
osea un claro Ejemplo es : Entrada: "(+ 2 3)"
Salida: ["(", "+", "2", "3", ")"] */

public class Lexer {
    public static List<String> tokenize(String input) {
        return Arrays.asList(input.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+"));
    }
}
