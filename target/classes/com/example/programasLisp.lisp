;;; =====================================================================
;;; Universidad del Valle de Guatemala
;;; Autor: Denil José Parada Cabrera - 24761, Joel Nerio, Arodi chávez 241112
;;; Fecha: 13 de Febrero de 2025
;;; Descripción: Programa en Lisp que contiene tres funciones:
;;;             - Conversión de grados Celsius a Fahrenheit.
;;;             - Cálculo de la serie de Fibonacci hasta el número n.
;;;             - Cálculo del factorial de un número n.
;;; =====================================================================

;;; Función para convertir grados Celsius a Fahrenheit
(defun celsius-a-fahrenheit (celsius)
  "Convierte una temperatura de grados Celsius a Fahrenheit."
  (+ (* celsius 1.8) 32))

;;; Prueba de la función Celsius a Fahrenheit
(format t "~%30 grados Celsius en Fahrenheit: ~a~%" (celsius-a-fahrenheit 30))



;;; =====================================================================
;;; Función para calcular la serie de Fibonacci hasta el número n
(defun fibonacci (n)
  "Calcula el enésimo número de la serie de Fibonacci."
  (cond
    ((<= n 1) n)
    (t (+ (fibonacci (- n 1)) (fibonacci (- n 2))))))

;;; Prueba de la función Fibonacci
(format t "~%Fibonacci de 10: ~a~%" (fibonacci 10))



;;; =====================================================================
;;; Función para calcular el factorial de un número n
(defun factorial (n)
  "Calcula el factorial de un número dado n."
  (cond
    ((<= n 1) 1)
    (t (* n (factorial (- n 1))))))

;;; Prueba de la función Factorial
(format t "~%Factorial de 5: ~a~%" (factorial 5))