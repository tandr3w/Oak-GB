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
    public Instruction(Type type, ArithmeticTarget target){
        this.type = type;
        this.target = target;
    }
    public Instruction byte_to_instruction(int readByte){
        switch(readByte){
            // TODO: Convert bytes to instructions
            case 0x80:
                return new Instruction(Type.ADD, ArithmeticTarget.B); // All ADD instructions add the value of target into A
            default: // will remove later
                return new Instruction(Type.ADD, ArithmeticTarget.B);
        }
    }
}
