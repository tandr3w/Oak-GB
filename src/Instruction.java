package src;

enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL,
    n8 // for when we are using a given number instead of a register value
}

public class Instruction {
    public static enum Operation {
        ADD,
        SUB,
    }
    public Operation operation;
    public Operand operand;
    public int value;
    public Instruction(Operation operation, Operand operand){
        this.operation = operation;
        this.operand = operand;
    }

    // TODO: add more constructors for other operations
}
