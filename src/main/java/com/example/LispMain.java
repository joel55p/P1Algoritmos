package com.example;
import java.util.Scanner;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class LispMain {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LispParser parser = new LispParser();
        LispEvaluator evaluator = new LispEvaluator();

        System.out.println("Intérprete Lisp");
        System.out.println("Para ejecutar un archivo: (load \"ruta/al/archivo.lisp\")");
        System.out.println("Para salir: exit");
        
        StringBuilder inputBuffer = new StringBuilder();
        
        while (true) {
            // El prompt cambia según si estamos en medio de una entrada multi-línea
            if (inputBuffer.length() == 0) {
                System.out.print("> ");
            } else {
                System.out.print("... ");
            }
            
            String line = scanner.nextLine();

            if (inputBuffer.length() == 0 && line.equalsIgnoreCase("exit")) {
                break;
            }
            
            // Si estamos comenzando una nueva entrada y es un comando para cargar un archivo
            if (inputBuffer.length() == 0 && line.trim().startsWith("(load ")) {
                try {
                    String filename = line.substring(6, line.length() - 1).trim();
                    // Eliminar comillas si están presentes
                    if (filename.startsWith("\"") && filename.endsWith("\"")) {
                        filename = filename.substring(1, filename.length() - 1);
                    }
                    loadAndExecuteFile(filename, parser, evaluator);
                } catch (Exception e) {
                    System.err.println("Error al cargar el archivo: " + e.getMessage());
                }
                continue;
            }

            // Añadir la línea al buffer con un espacio
            if (inputBuffer.length() > 0) {
                inputBuffer.append(" ");
            }
            inputBuffer.append(line);
            
            // Normalizar la entrada para verificar los paréntesis
            String normalizedInput = LispInputPreprocessor.normalize(inputBuffer.toString());
            
            // Si los paréntesis están balanceados, procesar la entrada
            if (LispInputPreprocessor.hasBalancedParentheses(normalizedInput)) {
                try {
                    // Parsing y evaluación
                    Expr expression = parser.parse(normalizedInput);
                    Expr result = evaluator.eval(expression);
                    
                    // Mostrar resultado
                    System.out.println("Resultado: " + result);
                    
                    // Limpiar el buffer para la siguiente entrada
                    inputBuffer.setLength(0);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    // Limpiar el buffer después de un error
                    inputBuffer.setLength(0);
                }
            }
            // Si los paréntesis no están balanceados, continuamos leyendo más líneas
        }

        scanner.close();
    }
    
    /**
     * Carga y ejecuta un archivo Lisp línea por línea.
     */
    private static void loadAndExecuteFile(String filename, LispParser parser, LispEvaluator evaluator) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            
            // Normalizar todo el contenido del archivo
            String normalizedContent = LispInputPreprocessor.normalize(fileContent.toString());
            
            // Dividir en expresiones individuales
            int start = 0;
            int openParens = 0;
            
            for (int i = 0; i < normalizedContent.length(); i++) {
                char c = normalizedContent.charAt(i);
                
                if (c == '(') {
                    if (openParens == 0) {
                        start = i;
                    }
                    openParens++;
                } 
                else if (c == ')') {
                    openParens--;
                    
                    if (openParens == 0) {
                        // Tenemos una expresión completa
                        String expr = normalizedContent.substring(start, i + 1);
                        try {
                            Expr expression = parser.parse(expr);
                            Expr result = evaluator.eval(expression);
                            System.out.println("Expresión: " + expr);
                            System.out.println("Resultado: " + result);
                        } catch (Exception e) {
                            System.err.println("Error al evaluar expresión: " + expr);
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                }
            }
            
            System.out.println("Archivo procesado: " + filename);
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}