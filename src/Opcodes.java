package src;
public class Opcodes {
    private Instruction[] opcodesArray;
    public Opcodes() {
        // TODO: figure out how to handle prefixed/unprefixed opcodes
        // nvm its simple; switch to prefixed when CB instruction is executed
        opcodesArray = new Instruction[0x100]; // 0xFF represents 255 in binary, but array needs to hold 256 values
        opcodesArray[0x02] = new Instruction(Operation.LD, Operand.MemBC, Operand.A, 1);
        opcodesArray[0x09] = new Instruction(Operation.ADD16, Operand.HL, Operand.BC, 1);
        opcodesArray[0x19] = new Instruction(Operation.ADD16, Operand.HL, Operand.DE, 1);
        opcodesArray[0x29] = new Instruction(Operation.ADD16, Operand.HL, Operand.HL, 1);
        opcodesArray[0x39] = new Instruction(Operation.ADD16, Operand.HL, Operand.SP, 1);
        opcodesArray[0x80] = new Instruction(Operation.ADD, Operand.B, 1);
        opcodesArray[0x80] = new Instruction(Operation.ADD, Operand.C, 1);
        opcodesArray[0x82] = new Instruction(Operation.ADD, Operand.D, 1);
        opcodesArray[0x83] = new Instruction(Operation.ADD, Operand.E, 1);
        opcodesArray[0x84] = new Instruction(Operation.ADD, Operand.H, 1);
        opcodesArray[0x85] = new Instruction(Operation.ADD, Operand.L, 1);
        opcodesArray[0x86] = new Instruction(Operation.ADD, Operand.MemHL, 1);
        opcodesArray[0x87] = new Instruction(Operation.ADD, Operand.A, 1); 
        // TODO: implement subtraction properly
        opcodesArray[0x90] = new Instruction(Operation.SUB, Operand.B, 1); 
        opcodesArray[0x91] = new Instruction(Operation.SUB, Operand.C, 1); 
        opcodesArray[0x92] = new Instruction(Operation.SUB, Operand.D, 1); 
        opcodesArray[0x93] = new Instruction(Operation.SUB, Operand.E, 1); 
        opcodesArray[0x94] = new Instruction(Operation.SUB, Operand.H, 1); 
        opcodesArray[0x95] = new Instruction(Operation.SUB, Operand.L, 1); 
        opcodesArray[0x96] = new Instruction(Operation.SUB, Operand.MemHL, 1); 
        opcodesArray[0x97] = new Instruction(Operation.SUB, Operand.A, 1); 
        opcodesArray[0xC6] = new Instruction(Operation.ADD, Operand.n8, 2);  
        opcodesArray[0xE8] = new Instruction(Operation.ADD16, Operand.SP, Operand.e8, 2);  
    }

    public Instruction byteToInstruction(int readByte) {
        return opcodesArray[readByte];
    }

}