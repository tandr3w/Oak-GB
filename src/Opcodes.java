package src;
public class Opcodes {
    private Instruction[] opcodesArray;
    public Opcodes() {
        // TODO: figure out how to handle prefixed/unprefixed opcodes
        opcodesArray = new Instruction[0x100]; // 0xFF represents 255 in binary, but array needs to hold 256 values
        opcodesArray[0x80] = new Instruction(Instruction.Operation.ADD, Operand.B);
        opcodesArray[0x81] = new Instruction(Instruction.Operation.ADD, Operand.C);
        opcodesArray[0x82] = new Instruction(Instruction.Operation.ADD, Operand.D);
        opcodesArray[0x83] = new Instruction(Instruction.Operation.ADD, Operand.E);
        opcodesArray[0x84] = new Instruction(Instruction.Operation.ADD, Operand.H);
        opcodesArray[0x85] = new Instruction(Instruction.Operation.ADD, Operand.L);
        opcodesArray[0x86] = new Instruction(Instruction.Operation.ADD, Operand.HL);
        opcodesArray[0x87] = new Instruction(Instruction.Operation.ADD, Operand.A);  
    }

    public Instruction byteToInstruction(int readByte) {
        return opcodesArray[readByte];
    }
}