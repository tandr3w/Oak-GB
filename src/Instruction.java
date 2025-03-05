package src;

enum ArithmeticTarget {
    A, B, C, D, E, H, L, BC, DE, HL
}

public class Instruction {
    public enum Type {
        ADD,
        SUB,
    }
    public Type type;
    public ArithmeticTarget target;
    public Instruction[] opcodes; // Converts opcodes to instructions
    public Instruction(Type type, ArithmeticTarget target){
        this.type = type;
        this.target = target;
        opcodes = new Instruction[0xFFFF]; // TODO: set this to an actually appropriate size...
        opcodes[0x80] = new Instruction(Type.ADD, ArithmeticTarget.B);
        opcodes[0x81] = new Instruction(Type.ADD, ArithmeticTarget.C);
        opcodes[0x82] = new Instruction(Type.ADD, ArithmeticTarget.D);
        opcodes[0x83] = new Instruction(Type.ADD, ArithmeticTarget.E);
        opcodes[0x84] = new Instruction(Type.ADD, ArithmeticTarget.H);
        opcodes[0x85] = new Instruction(Type.ADD, ArithmeticTarget.L);
        opcodes[0x86] = new Instruction(Type.ADD, ArithmeticTarget.HL);
        opcodes[0x87] = new Instruction(Type.ADD, ArithmeticTarget.A);
    }
    public Instruction byte_to_instruction(int readByte){
        return opcodes[readByte];
    }
}
