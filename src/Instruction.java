package src;

enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL,
    SP, PC,
    
    n8, e8, n16, // for when we are using a given number instead of a register value
    
    a8, // a8 is unsigned 8-bit that is added to FF00 to make a 16-bit address  
    a16,
}

enum Operation {
    ADD,
    ADD16,
    SUB,
    LD,
    LD16,
}

public class Instruction {

    public Operation operation;
    public Operand operand; // 2nd operand
    public Operand operandToSet; // 1st operand

    public Instruction(Operation operation, Operand operand){
        this.operation = operation;
        this.operand = operand;
        this.operandToSet = null;
    }
    public Instruction(Operation operation, Operand operandToSet, Operand operand){
        this.operation = operation;
        this.operand = operand;
        this.operandToSet = operandToSet;
    }
    // TODO: add more constructors for other operations
}
