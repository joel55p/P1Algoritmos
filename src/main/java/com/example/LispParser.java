package com.example;
import java.util.*;

public class LispParser {
    
    public Expr parse(String input) {
        List<String> tokens = tokenize(input);
        if (!isValidParentheses(tokens)) {
            throw new RuntimeException("Error: Paréntesis desbalanceados en la expresión.");
        }
        return readFromTokens(tokens);
    }

    private List<String> tokenize(String input) {
        input = input.replace("(", " ( ").replace(")", " ) ");
        return Arrays.asList(input.trim().split("\\s+"));
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

    private Expr parseAtom(String token) {
        try {
            return new NumberExpr(Double.parseDouble(token));
        } catch (NumberFormatException e) {
            return new Symbol(token);
        }
    }
}
