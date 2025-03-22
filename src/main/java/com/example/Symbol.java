package com.example;
public class Symbol extends Expr {
    public String name;
    public Symbol(String name) { this.name = name; }
    public String toString() { return name; }
}