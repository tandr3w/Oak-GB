package src;
public class Opcodes {

    // TODO: figure out what should be static and what shouldn't be...
    private static Instruction[] opcodes = new Instruction[0xFF];

    static {
        opcodes[0x80] = new Instruction(Instruction.Type.ADD, Operand.B);
        opcodes[0x81] = new Instruction(Instruction.Type.ADD, Operand.C);
        opcodes[0x82] = new Instruction(Instruction.Type.ADD, Operand.D);
        opcodes[0x83] = new Instruction(Instruction.Type.ADD, Operand.E);
        opcodes[0x84] = new Instruction(Instruction.Type.ADD, Operand.H);
        opcodes[0x85] = new Instruction(Instruction.Type.ADD, Operand.L);
        opcodes[0x86] = new Instruction(Instruction.Type.ADD, Operand.HL);
        opcodes[0x87] = new Instruction(Instruction.Type.ADD, Operand.A); 
    }   

    public static Instruction byteToInstruction(int readByte) {
        return opcodes[readByte];
    }
}