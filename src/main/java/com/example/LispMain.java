/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Clase principal del intérprete Lisp. Proporciona una interfaz de línea de comandos
 * para evaluar expresiones Lisp, con capacidad para entrada multilínea y carga de archivos.
 * Incluye manejo de errores para paréntesis desbalanceados y otros problemas de sintaxis.
 */
package com.example;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LispMain {
    
    // Códigos de color ANSI para la consola
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    
    /**
     * Método principal que ejecuta el intérprete Lisp interactivo.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LispParser parser = new LispParser();
        LispEvaluator evaluator = new LispEvaluator();

        System.out.println("Intérprete Lisp");
        System.out.println("Para ejecutar un archivo: (load \"ruta/al/archivo.lisp\")");
        System.out.println("Para salir: exit");
        
        StringBuilder inputBuffer = new StringBuilder();
        boolean waitingForCompletion = false;
        
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
            
            // Verificar si la línea por sí sola tiene paréntesis desbalanceados
            if (!waitingForCompletion && !LispInputPreprocessor.hasBalancedParentheses(line)) {
                System.out.println(YELLOW + "Advertencia: Paréntesis desbalanceados en la línea actual." + RESET);
                
                // Verificar si hay más paréntesis de cierre que de apertura (esto es un error irrecuperable)
                int openCount = 0, closeCount = 0;
                for (char c : line.toCharArray()) {
                    if (c == '(') openCount++;
                    else if (c == ')') closeCount++;
                }
                
                if (closeCount > openCount) {
                    System.out.println(RED + "Error: Hay más paréntesis de cierre ')' que de apertura '('." + RESET);
                    // Descartar esta línea y empezar de nuevo
                    inputBuffer.setLength(0);
                    waitingForCompletion = false;
                    continue;
                }
                
                waitingForCompletion = true;
            }

            // Añadir la línea al buffer con un espacio
            if (inputBuffer.length() > 0) {
                inputBuffer.append(" ");
            }
            inputBuffer.append(line);
            
            // Normalizar la entrada para verificar los paréntesis
            String normalizedInput = LispInputPreprocessor.normalize(inputBuffer.toString());
            
            // Comprobar si ahora está balanceado
            boolean isBalanced = LispInputPreprocessor.hasBalancedParentheses(normalizedInput);
            
            // Si los paréntesis están balanceados, procesar la entrada
            if (isBalanced) {
                try {
                    // Parsing y evaluación
                    Expr expression = parser.parse(normalizedInput);
                    Expr result = evaluator.eval(expression);
                    
                    // Mostrar resultado
                    System.out.println("Resultado: " + result);
                    
                    // Limpiar el buffer para la siguiente entrada
                    inputBuffer.setLength(0);
                    waitingForCompletion = false;
                } catch (Exception e) {
                    System.err.println(RED + "Error: " + e.getMessage() + RESET);
                    // Limpiar el buffer después de un error
                    inputBuffer.setLength(0);
                    waitingForCompletion = false;
                }
            } else if (!waitingForCompletion) {
                // Si no está balanceado y no estábamos esperando completarlo,
                // es un error irrecuperable
                System.out.println(RED + "Error: Paréntesis desbalanceados en la expresión. Se requiere más entrada." + RESET);
                waitingForCompletion = true;
            }
            // Si los paréntesis no están balanceados y estábamos esperando completarlo,
            // continuamos leyendo más líneas
        }

        scanner.close();
    }
    
    /**
     * Carga y ejecuta un archivo Lisp línea por línea.
     * 
     * @param filename Ruta del archivo Lisp a cargar y ejecutar
     * @param parser Instancia del parser Lisp para analizar las expresiones
     * @param evaluator Instancia del evaluador Lisp para evaluar las expresiones
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