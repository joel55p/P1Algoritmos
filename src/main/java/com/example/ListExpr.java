package com.example;
import java.util.List;

public class ListExpr extends Expr {
    public List<Expr> elements;
    public ListExpr(List<Expr> elements) { this.elements = elements; }
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i < elements.size() - 1) sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public List<Expr> getElements() {
        return elements;
    }
    
}