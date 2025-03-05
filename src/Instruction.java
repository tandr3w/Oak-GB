package src;

enum ArithmeticTarget {
    A, B, C, D, E, H, L,
}

public class Instruction {
    public enum Type {
        ADD,
        SUBTRACT
    }
    public Type type;
    public ArithmeticTarget target;
    public Instruction(Type type, ArithmeticTarget target){
        this.type = type;
        this.target = target;
    }
}
