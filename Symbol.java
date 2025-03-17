public class Symbol extends Expr {
    private String name;
    public Symbol(String name) { this.name = name; }
    public String toString() { return name; }
}