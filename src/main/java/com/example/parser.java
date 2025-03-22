package com.example;
import java.util.*;

abstract class Expr {}
class Symbol extends Expr {
    String name;
    Symbol(String name) { this.name = name; }
    public String toString() { return name; }
}
class NumberExpr extends Expr {
    double value;
    NumberExpr(double value) { this.value = value; }
    public String toString() { return String.valueOf(value); }
}
class ListExpr extends Expr {
    List<Expr> elements;
    ListExpr(List<Expr> elements) { this.elements = elements; }
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

class LispParser {
    public static Expr parse(String input) {
        List<String> tokens = tokenize(input);
        if (!isValidParentheses(tokens)) {
            throw new RuntimeException("Error: Paréntesis desbalanceados en la expresión.");
        }
        return readFromTokens(tokens);
    }

    private static List<String> tokenize(String input) {
        input = input.replace("(", " ( ").replace(")", " ) ");
        return Arrays.asList(input.trim().split("\\s+"));
    }

    private static boolean isValidParentheses(List<String> tokens) {
        int balance = 0;
        for (String token : tokens) {
            if ("(".equals(token)) balance++;
            else if (")".equals(token)) balance--;
            if (balance < 0) return false; // Más cierres que aperturas
        }
        return balance == 0; // Debe quedar balanceado
    }

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

    private static Expr parseAtom(String token) {
        try {
            return new NumberExpr(Double.parseDouble(token));
        } catch (NumberFormatException e) {
            return new Symbol(token); // Symbol (variable or function name)
        }
    }

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

