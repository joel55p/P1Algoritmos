/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Analizador sintáctico para el intérprete Lisp.
 * Construye un árbol de sintaxis abstracta a partir de los tokens obtenidos
 * en la fase del análisis léxico, traduciendo el código fuente a una estructura
 * que puede ser evaluada por el intérprete.
 */
package com.example;
import java.util.*;

public class LispParser {
    
    /**
     * Analiza y convierte una cadena de entrada en una expresión Lisp evaluable.
     * 
     * @param input La cadena de entrada que contiene código Lisp
     * @return La expresión Lisp parseada como un objeto Expr
     * @throws RuntimeException Si hay errores de sintaxis como paréntesis desbalanceados
     */
    public Expr parse(String input) {
        // Preprocesar la entrada
        input = LispInputPreprocessor.fixCommonSyntaxIssues(input);
        
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
    private List<String> tokenize(String input) {
        // Asegurar que hay espacio alrededor de los paréntesis
        input = input.replace("(", " ( ").replace(")", " ) ");
        
        // Dividir en tokens, filtrando cadenas vacías
        List<String> tokens = new ArrayList<>();
        for (String token : input.trim().split("\\s+")) {
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * Verifica si los paréntesis en una lista de tokens están balanceados.
     * 
     * @param tokens Lista de tokens a verificar
     * @return true si los paréntesis están balanceados, false de lo contrario
     */
    private boolean isValidParentheses(List<String> tokens) {
        int balance = 0;
        for (String token : tokens) {
            if ("(".equals(token)) balance++;
            else if (")".equals(token)) balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    /**
     * Convierte una lista de tokens en una estructura de expresiones Lisp.
     * 
     * @param tokens Lista de tokens a convertir
     * @return Estructura de expresión Lisp
     * @throws RuntimeException Si hay errores de sintaxis en la estructura
     */
    private Expr readFromTokens(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("Error: Expresión vacía.");
        }
        
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
                // Verificar token inválido como '[-' o '[N'
                if (token.startsWith("[") || token.endsWith("]")) {
                    String correctedToken = token.replace("[", "(").replace("]", ")");
                    throw new RuntimeException("Error: Operador no válido: [" + token + "]. ¿Quizás quisiste usar '" + correctedToken + "'?");
                }
                currentList.add(parseAtom(token));
            }
        }
    
        if (!stack.isEmpty()) throw new RuntimeException("Error: Falta paréntesis de cierre.");
    
        // Si la expresión es un solo elemento (número o símbolo), devuélvelo sin ListExpr
        if (currentList.size() == 1) {
            return currentList.get(0);
        }
    
        return new ListExpr(currentList);
    }
    
    /**
     * Parsea un token individual como un átomo (número o símbolo).
     * 
     * @param token El token a parsear
     * @return Una expresión de tipo NumberExpr o Symbol
     */
    private Expr parseAtom(String token) {
        // Verificar si es un número
        try {
            return new NumberExpr(Double.parseDouble(token));
        } catch (NumberFormatException e) {
            // Si no es un número, debe ser un símbolo
            return new Symbol(token);
        }
    }
}