package src;
public class Opcodes {
    private Instruction[] opcodesArray;
    public Opcodes() {
        // TODO: figure out how to handle prefixed/unprefixed opcodes
        // nvm its simple; switch to prefixed when CB instruction is executed
        opcodesArray = new Instruction[0x100]; // 0xFF represents 255 in binary, but array needs to hold 256 values
        opcodesArray[0x80] = new Instruction(Operation.ADD, Operand.C, 1);
        opcodesArray[0x82] = new Instruction(Operation.ADD, Operand.D, 1);
        opcodesArray[0x83] = new Instruction(Operation.ADD, Operand.E, 1);
        opcodesArray[0x84] = new Instruction(Operation.ADD, Operand.H, 1);
        opcodesArray[0x85] = new Instruction(Operation.ADD, Operand.L, 1);
        opcodesArray[0x86] = new Instruction(Operation.ADD, Operand.HL, 1);
        opcodesArray[0x87] = new Instruction(Operation.ADD, Operand.A, 1); 
        opcodesArray[0xC6] = new Instruction(Operation.ADD, Operand.n8, 1);  

        // 8-bit loading
        opcodesArray[0x02] = new Instruction(Operation.LD, Operand.BC, Operand.A, 2);
        

    }

    public Instruction byteToInstruction(int readByte) {
        return opcodesArray[readByte];
    }

}