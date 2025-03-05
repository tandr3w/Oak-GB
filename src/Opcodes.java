package src;
public class Opcodes {
    private Instruction[] opcodesArray;
    public Opcodes() {
        // TODO: figure out how to handle prefixed/unprefixed opcodes
        // nvm its simple; switch to prefixed when CB instruction is executed
        opcodesArray = new Instruction[0x100]; // 0xFF represents 255 in binary, but array needs to hold 256 values
        opcodesArray[0x02] = new Instruction(Operation.LD, Operand.MemBC, Operand.A, 1);
        opcodesArray[0x09] = new Instruction(Operation.ADD16, Operand.HL, Operand.BC, 1);
        opcodesArray[0x12] = new Instruction(Operation.LD, Operand.MemDE, Operand.A, 1);
        opcodesArray[0x19] = new Instruction(Operation.ADD16, Operand.HL, Operand.DE, 1);
        opcodesArray[0x29] = new Instruction(Operation.ADD16, Operand.HL, Operand.HL, 1);
        opcodesArray[0x39] = new Instruction(Operation.ADD16, Operand.HL, Operand.SP, 1);
        opcodesArray[0x40] = new Instruction(Operation.LD, Operand.B, Operand.B, 1);
        opcodesArray[0x41] = new Instruction(Operation.LD, Operand.B, Operand.C, 1);
        opcodesArray[0x42] = new Instruction(Operation.LD, Operand.B, Operand.D, 1);
        opcodesArray[0x43] = new Instruction(Operation.LD, Operand.B, Operand.E, 1);
        opcodesArray[0x44] = new Instruction(Operation.LD, Operand.B, Operand.H, 1);
        opcodesArray[0x45] = new Instruction(Operation.LD, Operand.B, Operand.L, 1);
        opcodesArray[0x46] = new Instruction(Operation.LD, Operand.B, Operand.MemHL, 1);
        opcodesArray[0x47] = new Instruction(Operation.LD, Operand.B, Operand.A, 1);
        opcodesArray[0x48] = new Instruction(Operation.LD, Operand.C, Operand.B, 1);
        opcodesArray[0x49] = new Instruction(Operation.LD, Operand.C, Operand.C, 1);
        opcodesArray[0x4A] = new Instruction(Operation.LD, Operand.C, Operand.D, 1);
        opcodesArray[0x4B] = new Instruction(Operation.LD, Operand.C, Operand.E, 1);
        opcodesArray[0x4C] = new Instruction(Operation.LD, Operand.C, Operand.H, 1);
        opcodesArray[0x4D] = new Instruction(Operation.LD, Operand.C, Operand.L, 1);
        opcodesArray[0x4E] = new Instruction(Operation.LD, Operand.C, Operand.MemHL, 1);
        opcodesArray[0x4F] = new Instruction(Operation.LD, Operand.C, Operand.A, 1);
        opcodesArray[0x50] = new Instruction(Operation.LD, Operand.D, Operand.B, 1);
        opcodesArray[0x51] = new Instruction(Operation.LD, Operand.D, Operand.C, 1);
        opcodesArray[0x52] = new Instruction(Operation.LD, Operand.D, Operand.D, 1);
        opcodesArray[0x53] = new Instruction(Operation.LD, Operand.D, Operand.E, 1);
        opcodesArray[0x54] = new Instruction(Operation.LD, Operand.D, Operand.H, 1);
        opcodesArray[0x55] = new Instruction(Operation.LD, Operand.D, Operand.L, 1);
        opcodesArray[0x56] = new Instruction(Operation.LD, Operand.D, Operand.MemHL, 1);
        opcodesArray[0x57] = new Instruction(Operation.LD, Operand.D, Operand.A, 1);
        opcodesArray[0x58] = new Instruction(Operation.LD, Operand.E, Operand.B, 1);
        opcodesArray[0x59] = new Instruction(Operation.LD, Operand.E, Operand.C, 1);
        opcodesArray[0x5A] = new Instruction(Operation.LD, Operand.E, Operand.D, 1);
        opcodesArray[0x5B] = new Instruction(Operation.LD, Operand.E, Operand.E, 1);
        opcodesArray[0x5C] = new Instruction(Operation.LD, Operand.E, Operand.H, 1);
        opcodesArray[0x5D] = new Instruction(Operation.LD, Operand.E, Operand.L, 1);
        opcodesArray[0x5E] = new Instruction(Operation.LD, Operand.E, Operand.MemHL, 1);
        opcodesArray[0x5F] = new Instruction(Operation.LD, Operand.E, Operand.A, 1);
        opcodesArray[0x60] = new Instruction(Operation.LD, Operand.H, Operand.B, 1);
        opcodesArray[0x61] = new Instruction(Operation.LD, Operand.H, Operand.C, 1);
        opcodesArray[0x62] = new Instruction(Operation.LD, Operand.H, Operand.D, 1);
        opcodesArray[0x63] = new Instruction(Operation.LD, Operand.H, Operand.E, 1);
        opcodesArray[0x64] = new Instruction(Operation.LD, Operand.H, Operand.H, 1);
        opcodesArray[0x65] = new Instruction(Operation.LD, Operand.H, Operand.L, 1);
        opcodesArray[0x66] = new Instruction(Operation.LD, Operand.H, Operand.MemHL, 1);
        opcodesArray[0x67] = new Instruction(Operation.LD, Operand.H, Operand.A, 1);
        opcodesArray[0x68] = new Instruction(Operation.LD, Operand.L, Operand.B, 1);
        opcodesArray[0x69] = new Instruction(Operation.LD, Operand.L, Operand.C, 1);
        opcodesArray[0x6A] = new Instruction(Operation.LD, Operand.L, Operand.D, 1);
        opcodesArray[0x6B] = new Instruction(Operation.LD, Operand.L, Operand.E, 1);
        opcodesArray[0x6C] = new Instruction(Operation.LD, Operand.L, Operand.H, 1);
        opcodesArray[0x6D] = new Instruction(Operation.LD, Operand.L, Operand.L, 1);
        opcodesArray[0x6E] = new Instruction(Operation.LD, Operand.L, Operand.MemHL, 1);
        opcodesArray[0x6F] = new Instruction(Operation.LD, Operand.L, Operand.A, 1);
        opcodesArray[0x70] = new Instruction(Operation.LD, Operand.MemHL, Operand.B, 1);
        opcodesArray[0x71] = new Instruction(Operation.LD, Operand.MemHL, Operand.C, 1);
        opcodesArray[0x72] = new Instruction(Operation.LD, Operand.MemHL, Operand.D, 1);
        opcodesArray[0x73] = new Instruction(Operation.LD, Operand.MemHL, Operand.E, 1);
        opcodesArray[0x74] = new Instruction(Operation.LD, Operand.MemHL, Operand.H, 1);
        opcodesArray[0x75] = new Instruction(Operation.LD, Operand.MemHL, Operand.L, 1);
        opcodesArray[0x77] = new Instruction(Operation.LD, Operand.MemHL, Operand.A, 1);
        opcodesArray[0x78] = new Instruction(Operation.LD, Operand.A, Operand.B, 1);
        opcodesArray[0x79] = new Instruction(Operation.LD, Operand.A, Operand.C, 1);
        opcodesArray[0x7A] = new Instruction(Operation.LD, Operand.A, Operand.D, 1);
        opcodesArray[0x7B] = new Instruction(Operation.LD, Operand.A, Operand.E, 1);
        opcodesArray[0x7C] = new Instruction(Operation.LD, Operand.A, Operand.H, 1);
        opcodesArray[0x7D] = new Instruction(Operation.LD, Operand.A, Operand.L, 1);
        opcodesArray[0x7E] = new Instruction(Operation.LD, Operand.A, Operand.MemHL, 1);
        opcodesArray[0x7F] = new Instruction(Operation.LD, Operand.A, Operand.A, 1);
        opcodesArray[0x80] = new Instruction(Operation.ADD, Operand.B, 1);
        opcodesArray[0x81] = new Instruction(Operation.ADD, Operand.C, 1);
        opcodesArray[0x82] = new Instruction(Operation.ADD, Operand.D, 1);
        opcodesArray[0x83] = new Instruction(Operation.ADD, Operand.E, 1);
        opcodesArray[0x84] = new Instruction(Operation.ADD, Operand.H, 1);
        opcodesArray[0x85] = new Instruction(Operation.ADD, Operand.L, 1);
        opcodesArray[0x86] = new Instruction(Operation.ADD, Operand.MemHL, 1);
        opcodesArray[0x87] = new Instruction(Operation.ADD, Operand.A, 1); 
        opcodesArray[0xC6] = new Instruction(Operation.ADD, Operand.n8, 2);  
        opcodesArray[0xE8] = new Instruction(Operation.ADD16, Operand.SP, Operand.e8, 2);  
    }

    public Instruction byteToInstruction(int readByte) {
        return opcodesArray[readByte];
    }

}