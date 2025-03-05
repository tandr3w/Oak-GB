package src;

enum ArithmeticTarget {
    A, B, C, D, E, H, L, BC, DE, HL
}

public class Instruction {
    public static enum Type {
        ADD,
        SUB,
    }
    public Type type;
    public ArithmeticTarget target;
    public Instruction(Type type, ArithmeticTarget target){
        this.type = type;
        this.target = target;
    }

    // TODO: add more constructors for other operations
}
