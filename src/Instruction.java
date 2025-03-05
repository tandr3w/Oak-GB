package src;

enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL
}

public class Instruction {
    public static enum Type {
        ADD,
        SUB,
    }
    public Type type;
    public Operand operand;
    public Instruction(Type type, Operand operand){
        this.type = type;
        this.operand = operand;
    }

    // TODO: add more constructors for other operations
}
