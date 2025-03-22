/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Clase que implementa un analizador léxico para expresiones LISP.
 * Se encarga de tokenizar y verificar el balance de paréntesis en las expresiones.
 */
package com.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que ejecuta el analizador léxico de expresiones LISP.
 */
public class LispLexer {
    // Códigos de color ANSI para la consola
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String ORANGE = "\u001B[38;5;214m";

    /**
     * Método principal que ejecuta el menú del analizador léxico.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Ingrese una expresión LISP o escriba 'salir' para terminar:");
            String input = scanner.nextLine().trim(); /*se guarda la respuesta */
            
            if (input.equalsIgnoreCase("salir")) {  /*ver si quiere salir o no */
                System.out.println("Saliendo del programa...");
                break;
            }
            
            // Verificar balance de paréntesis
            if (isBalanced(input)) {
                System.out.println(GREEN + "Expresión correcta: los paréntesis están balanceados." + RESET);
            } else {
                System.out.println(RED + "Expresión incorrecta: los paréntesis no están balanceados." + RESET);
            }
            
            // Obtener tokens
            List<String> tokens = tokenize(input);
            System.out.print(CYAN + "Tokens: [" + RESET);
            for (int i = 0; i < tokens.size(); i++) {
                System.out.print(ORANGE + tokens.get(i) + RESET);
                if (i < tokens.size() - 1) {
                    System.out.print(CYAN + ", " + RESET);
                }
            }
            System.out.println(CYAN + "]" + RESET);
        }
        scanner.close();
    }

    /**
     * Verifica si los paréntesis de la expresión están balanceados.
     * 
     * @param expression La expresión LISP a analizar.
     * @return true si los paréntesis están balanceados, false de lo contrario.
     */
    public static boolean isBalanced(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) return false; // Se encontró un ')' sin su '('
        }
        return balance == 0;
    }

    /**
     * Tokeniza una expresión LISP, separando operadores, números, variables y paréntesis.
     * 
     * @param expression La expresión LISP a tokenizar.
     * @return Lista de tokens extraídos de la expresión.
     */
    public static List<String> tokenize(String expression) { /*construccion de una parte segun (OpenIA, 2025) */
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        
        for (char c : expression.toCharArray()) {
            if (c == '(' || c == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(c);
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        return tokens;
    }
}

/*Referencias bibliográficas 
OpenAI. (2025). ChatGPT (versión mas reciente) [Modelo de lenguaje de gran 
tamaño]. https://chatgpt.com  

Descripción del uso de IA: 
Fue usado para idea clave que se implementó para tokenizar y separar la entrada, ya luego se implementaron complementos propios adicionales para obtener una resultante del programa.  */