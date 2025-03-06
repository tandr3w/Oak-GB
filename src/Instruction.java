enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL,
    MemHL, MemBC, MemDE,
    SP, PC,
    
    n8, e8, n16, // for when we are using a given number instead of a register value
    
    a8, // a8 is unsigned 8-bit that is added to FF00 to make a 16-bit address
    a16,
    SPe8 // special operand for opcode 0xF8
}

enum Operation {
    ADD,
    ADD16,
    SUB,
    LD,
    LD16,
    AND,
    OR,
    XOR,
    ADC,
}

public class Instruction {

    public Operation operation;
    public Operand operand; // 2nd operand
    public Operand operandToSet; // 1st operand
    public int num_bytes;
    public int[] next_bytes;


    public Instruction(Operation operation, Operand operand, int num_bytes){
        this.operation = operation;
        this.operand = operand;
        this.operandToSet = null;
        this.num_bytes = num_bytes;
    }
    public Instruction(Operation operation, Operand operandToSet, Operand operand, int num_bytes){
        this.operation = operation;
        this.operand = operand;
        this.operandToSet = operandToSet;
        this.num_bytes = num_bytes;
    }

    // TODO: add more constructors for other operations
}
