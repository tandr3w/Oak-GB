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
    public Instruction[] opcodes; // Converts opcodes to instructions
    public Instruction(Type type, ArithmeticTarget target){
        this.type = type;
        this.target = target;
        opcodes = new Instruction[0xFFFF];
        opcodes[0x80] = new Instruction(Type.ADD, ArithmeticTarget.B);
        
    }
    public Instruction byte_to_instruction(int readByte){
        return opcodes[readByte];
    }
}
