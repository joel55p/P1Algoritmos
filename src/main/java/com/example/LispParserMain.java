package com.example;
import java.util.Scanner;

public class LispParserMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una expresión LISP: ");
        String code = scanner.nextLine(); //Se le solicita al usuario que ingrese una expresión LISP
        scanner.close();

        LispParser parser = new LispParser();
        try {
            Expr parsedExpression = parser.parse(code);
            System.out.println("Expresión parseada: " + parsedExpression);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}