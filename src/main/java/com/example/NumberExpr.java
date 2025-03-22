package com.example;

public class NumberExpr extends Expr {
    public  double value;
    public NumberExpr(double value) { this.value = value; }
    public String toString() { return String.valueOf(value); }
}
