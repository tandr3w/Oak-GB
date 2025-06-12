package src;
enum Operand {
    A, B, C, D, E, H, L, BC, DE, HL, AF,
    MemHL, MemBC, MemDE,
    SP, PC,
    
    n3, n8, e8, n16,
    
    a8, // a8 is unsigned 8-bit that is added to FF00 to make a 16-bit address
    a16,
    NONE,
    JumpNZ, JumpNC, JumpZ, JumpC, // For jumps
    SPe8, // special operand for opcode 0xF8
    RST00, RST08, RST10, RST18, RST20, RST28, RST30, RST38,
    BIT0, BIT1, BIT2, BIT3, BIT4, BIT5, BIT6, BIT7,
}

enum Operation {
    NOP,
    ADD,
    ADD16,
    SUB,
    LD,
    LD16,
    LDI,
    LDD,
    LDH,
    AND,
    OR,
    XOR,
    ADC,
    SBC,
    CP,
    INC,
    DEC,
    INC16,
    DEC16,
    RLCA,
    RLA,
    RRCA,
    RCA,
    CPL,
    CCF,
    SCF,
    DAA,
    JP, JR,
    CALL,
    RET,
    RST,
    POP,
    PUSH,
    PREFIX,
    STOP, HALT, RETI,
    DI, EI,

    // PREFIX INSTRUCTIONS
    SWAP,
    RLC,
    RRC,
    RL,
    RR,
    SLA,
    SRA,
    SRL,
    BIT,
    SET,
    RES,

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
    public Instruction(Operation operation, int num_bytes) {
        this.operation = operation;
        this.num_bytes = num_bytes;
    }
}
