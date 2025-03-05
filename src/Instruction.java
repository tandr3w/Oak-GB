package src;

enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL
}

public class Instruction {
    public static enum Operation {
        ADD,
        SUB,
    }
    public Operation operation;
    public Operand operand;
    public Instruction(Operation operation, Operand operand){
        this.operation = operation;
        this.operand = operand;
    }

    // TODO: add more constructors for other operations
}
