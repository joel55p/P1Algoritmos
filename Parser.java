import java.util.*; /*Como tal esta clase es que de la clase Lexer convierte  los tokens en una estructura de datos, como se pide en las intrucciones "Arbol de sintaxis abstracta 
que es una estructura de datos que representa el code, que esta compusto por valores regulares de Lisp" */

public class Parser {   /*Convierte la lista de tokens en una estructura de datos en Java (listas anidadas). */
    public static Object parse(List<String> tokens) {
        if (tokens.isEmpty()) throw new RuntimeException("Unexpected EOF");
        String token = tokens.remove(0);
        if ("(".equals(token)) {
            List<Object> list = new ArrayList<>();
            while (!tokens.get(0).equals(")")) {
                list.add(parse(tokens));
            }
            tokens.remove(0); // Elimina ")"
           

            return list;
        } else {
            
            return token.matches("-?\\d+") ? Integer.parseInt(token) : token;
        }
    }
}

/*un claro ejemplo de esto es que la  Entrada: "(+ 2 3)" y 
Salida: List<Object> ["+", 2, 3] */
