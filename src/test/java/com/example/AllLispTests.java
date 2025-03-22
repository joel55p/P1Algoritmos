package com.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Universidad del Valle de Guatemala
 * Departamento de Ciencia de la Computación
 * Autores: Denil José Parada Cabrera - 24761, Arodi Chávez - 241112, Joel Nerio - 24253
 * Fecha: 21/03/2025
 * Descripción: Suite que agrupa todas las pruebas unitarias del intérprete Lisp
 */
@RunWith(Suite.class)
@SuiteClasses({
    ExprTest.class,
    LispLexerTest.class,
    LispParserTest.class, 
    LispEvaluatorTest.class,
    LispInputPreprocessorTest.class,
    UserFunctionTest.class,
    LispMainTest.class,
    LispIntegrationTest.class
})
public class AllLispTests {
    // Esta clase sirve únicamente como contenedor para la anotación @SuiteClasses
}
