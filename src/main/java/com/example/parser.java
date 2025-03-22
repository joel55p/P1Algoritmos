/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Implementación alternativa del analizador sintáctico para expresiones Lisp.
 * Este archivo contiene tanto la definición de las clases básicas de expresiones Lisp
 * como el analizador para convertir texto en estructuras de datos evaluables.
 */
package com.example;
import java.util.*;

/**
 * Clase base abstracta para todas las expresiones Lisp.
 * Define el tipo común para todos los diferentes tipos de expresiones.
 */
abstract class Expr {}

/**
 * Representa un símbolo en Lisp, como nombres de variables o funciones.
 */
class Symbol extends Expr {
    /** Nombre del símbolo */
    String name;
    
    /**
     * Constructor para crear un nuevo símbolo.
     * 
     * @param name El nombre del símbolo
     */
    Symbol(String name) { this.name = name; }
    
    /**
     * Convierte el símbolo a una representación de cadena.
     * 
     * @return El nombre del símbolo
     */
    public String toString() { return name; }
}

/**
 * Representa un valor numérico en Lisp.
 */
class NumberExpr extends Expr {
    /** Valor numérico almacenado */
    double value;
    
    /**
     * Constructor para crear una nueva expresión numérica.
     * 
     * @param value El valor numérico a almacenar
     */
    NumberExpr(double value) { this.value = value; }
    
    /**
     * Convierte el valor numérico a una representación de cadena.
     * 
     * @return Una cadena que representa el valor numérico
     */
    public String toString() { return String.valueOf(value); }
}

/**
 * Representa una lista de expresiones en Lisp.
 */
class ListExpr extends Expr {
    /** Lista de elementos que contiene esta expresión */
    List<Expr> elements;
    
    /**
     * Constructor para crear una nueva expresión de lista.
     * 
     * @param elements Lista de expresiones que conformarán esta lista
     */
    ListExpr(List<Expr> elements) { this.elements = elements; }
    
    /**
     * Convierte la lista de expresiones a una representación de cadena.
     * 
     * @return Una cadena que representa la lista, con elementos separados por espacios
     *         y encerrados entre corchetes
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i < elements.size() - 1) sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}

/**
 * Analizador sintáctico para expresiones Lisp.
 * Convierte texto en formato Lisp a estructuras de datos evaluables.
 */
class LispParser {
    /**
     * Analiza y convierte una cadena de entrada en una expresión Lisp evaluable.
     * 
     * @param input La cadena de entrada que contiene código Lisp
     * @return La expresión Lisp parseada como un objeto Expr
     * @throws RuntimeException Si hay errores de sintaxis como paréntesis desbalanceados
     */
    public static Expr parse(String input) {
        List<String> tokens = tokenize(input);
        if (!isValidParentheses(tokens)) {
            throw new RuntimeException("Error: Paréntesis desbalanceados en la expresión.");
        }
        return readFromTokens(tokens);
    }

    /**
     * Tokeniza una cadena de entrada, separando paréntesis y elementos.
     * 
     * @param input La cadena de entrada a tokenizar
     * @return Una lista de tokens
     */
    private static List<String> tokenize(String input) {
        input = input.replace("(", " ( ").replace(")", " ) ");
        return Arrays.asList(input.trim().split("\\s+"));
    }

    /**
     * Verifica si los paréntesis en una lista de tokens están balanceados.
     * 
     * @param tokens Lista de tokens a verificar
     * @return true si los paréntesis están balanceados, false de lo contrario
     */
    private static boolean isValidParentheses(List<String> tokens) {
        int balance = 0;
        for (String token : tokens) {
            if ("(".equals(token)) balance++;
            else if (")".equals(token)) balance--;
            if (balance < 0) return false; // Más cierres que aperturas
        }
        return balance == 0; // Debe quedar balanceado
    }

    /**
     * Convierte una lista de tokens en una estructura de expresiones Lisp.
     * 
     * @param tokens Lista de tokens a convertir
     * @return Estructura de expresión Lisp
     * @throws RuntimeException Si hay errores de sintaxis en la estructura
     */
    private static Expr readFromTokens(List<String> tokens) {
        Stack<List<Expr>> stack = new Stack<>();
        List<Expr> currentList = new ArrayList<>();
        
        for (String token : tokens) {
            if ("(".equals(token)) {
                stack.push(currentList);
                currentList = new ArrayList<>();
            } else if (")".equals(token)) {
                if (stack.isEmpty()) throw new RuntimeException("Error: Paréntesis de cierre inesperado.");
                ListExpr finishedList = new ListExpr(currentList);
                currentList = stack.pop();
                currentList.add(finishedList);
            } else {
                currentList.add(parseAtom(token));
            }
        }
        
        if (!stack.isEmpty()) throw new RuntimeException("Error: Falta paréntesis de cierre.");
        return new ListExpr(currentList);
    }

    /**
     * Parsea un token individual como un átomo (número o símbolo).
     * 
     * @param token El token a parsear
     * @return Una expresión de tipo NumberExpr o Symbol
     */
    private static Expr parseAtom(String token) {
        try {
            return new NumberExpr(Double.parseDouble(token));
        } catch (NumberFormatException e) {
            return new Symbol(token); // Symbol (variable or function name)
        }
    }

    /**
     * Método principal que proporciona una interfaz básica para probar el parser.
     * Solicita al usuario una expresión Lisp, la analiza y muestra el resultado.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una expresión LISP: ");
        String code = scanner.nextLine();
        scanner.close();
        
        try {
            Expr parsedExpression = parse(code);
            System.out.println("Expresión parseada: " + parsedExpression);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}