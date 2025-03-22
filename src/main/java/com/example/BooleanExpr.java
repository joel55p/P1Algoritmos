package com.example;

public class BooleanExpr extends Expr {
    public final boolean value;

    public BooleanExpr(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value ? "T" : "NIL"; // En Lisp, `T` es verdadero y `NIL` es falso
    }
}
