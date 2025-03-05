package src;

enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL,
    
    n8, e8, // for when we are using a given number instead of a register value
    SP, PC,
    Memory, // for load
}

public class Instruction {
    public static enum Operation {
        ADD,
        ADD16,
        SUB,
        LD,
    }
    public Operation operation;
    public Operand operand; // 2nd operand
    public Operand operandToSet; // 1st operand

    public Instruction(Operation operation, Operand operand){
        this.operation = operation;
        this.operand = operand;
    }
    public Instruction(Operation operation, Operand operand, Operand operandToSet){
        this.operation = operation;
        this.operand = operand;
        this.operandToSet = operandToSet;
    }
    // TODO: add more constructors for other operations
}
