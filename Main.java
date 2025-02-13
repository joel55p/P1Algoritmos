import java.util.*; // Ejecuta el intérprete con ejemplos de Fibonacci y Factorial

public class Main {
    public static void main(String[] args) {
        Environment env = new Environment();

        // Convertir tokens a lista mutable antes de parsear
        List<String> tokensFactorial = new ArrayList<>(Lexer.tokenize(
                "(DEFUN factorial (n) (COND ((= n 0) 1) (T (* n (factorial (- n 1))))))"
        ));
        Evaluator.eval(Parser.parse(tokensFactorial), env);

        List<String> tokensEvalFactorial = new ArrayList<>(Lexer.tokenize("(factorial 5)"));
        System.out.println(Evaluator.eval(Parser.parse(tokensEvalFactorial), env));
        // Salida esperada: 120

        // Definir Fibonacci
        List<String> tokensFibonacci = new ArrayList<>(Lexer.tokenize(
                "(DEFUN fibonacci (n) (COND ((= n 0) 0) ((= n 1) 1) (T (+ (fibonacci (- n 1)) (fibonacci (- n 2))))))"
        ));
        Evaluator.eval(Parser.parse(tokensFibonacci), env);

        List<String> tokensEvalFibonacci = new ArrayList<>(Lexer.tokenize("(fibonacci 6)"));
        System.out.println(Evaluator.eval(Parser.parse(tokensEvalFibonacci), env));
        // Salida esperada: 8
    }
}
