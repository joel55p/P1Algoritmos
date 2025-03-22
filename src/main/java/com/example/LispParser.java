package com.example;
import java.util.*;

public class LispParser {
    
    public Expr parse(String input) {
        // Preprocesar la entrada
        input = LispInputPreprocessor.fixCommonSyntaxIssues(input);
        
        List<String> tokens = tokenize(input);
        if (!isValidParentheses(tokens)) {
            throw new RuntimeException("Error: Paréntesis desbalanceados en la expresión.");
        }
        return readFromTokens(tokens);
    }

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

    private boolean isValidParentheses(List<String> tokens) {
        int balance = 0;
        for (String token : tokens) {
            if ("(".equals(token)) balance++;
            else if (")".equals(token)) balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

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