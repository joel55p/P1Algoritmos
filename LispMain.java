import java.util.Scanner;
import java.util.List;


public class LispMain {
    
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LispLexer lexer = new LispLexer();
        LispParser parser = new LispParser();
        LispEvaluator evaluator = new LispEvaluator();

        System.out.println("Ingrese una expresión LISP o 'exit' para salir:");
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // Tokenización
                List<String> tokens = lexer.tokenize(input);
                
                // Parsing
                Expr expression = parser.parse(String.join(" ", tokens));

                
                // Evaluación
                Expr result = evaluator.eval(expression);
                
                // Mostrar resultado
                System.out.println("Resultado: " + result);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
