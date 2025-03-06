public class Opcodes {
    public Instruction[] opcodesArray;
    public Opcodes() {
        // TODO: figure out how to handle prefixed/unprefixed opcodes
        // nvm its simple; switch to prefixed when CB instruction is executed
        // TODO: Sort opcodes
        opcodesArray = new Instruction[0x100]; // 0xFF represents 255 in binary, but array needs to hold 256 values

        // ROW 0
        opcodesArray[0x01] = new Instruction(Operation.LD16, Operand.BC, Operand.n16, 3);
        opcodesArray[0x02] = new Instruction(Operation.LD, Operand.MemBC, Operand.A, 1);
        opcodesArray[0x03] = new Instruction(Operation.INC16, Operand.BC, Operand.NONE, 1);
        opcodesArray[0x04] = new Instruction(Operation.INC, Operand.B, Operand.NONE, 1);
        opcodesArray[0x05] = new Instruction(Operation.DEC, Operand.B, Operand.NONE, 1);
        opcodesArray[0x06] = new Instruction(Operation.LD, Operand.B, Operand.n8, 2);
        opcodesArray[0x08] = new Instruction(Operation.LD16, Operand.a16, Operand.SP, 3);
        opcodesArray[0x09] = new Instruction(Operation.ADD16, Operand.HL, Operand.BC, 1);
        opcodesArray[0x0A] = new Instruction(Operation.LD, Operand.A, Operand.MemBC, 1);
        opcodesArray[0x0B] = new Instruction(Operation.DEC16, Operand.BC, Operand.NONE, 1);
        opcodesArray[0x0C] = new Instruction(Operation.INC, Operand.C, Operand.NONE, 1);
        opcodesArray[0x0D] = new Instruction(Operation.DEC, Operand.C, Operand.NONE, 1);
        opcodesArray[0x0E] = new Instruction(Operation.LD, Operand.C, Operand.n8, 2);

        // ROW 1
        opcodesArray[0x11] = new Instruction(Operation.LD16, Operand.DE, Operand.n16, 3);
        opcodesArray[0x12] = new Instruction(Operation.LD, Operand.MemDE, Operand.A, 1);
        opcodesArray[0x13] = new Instruction(Operation.INC16, Operand.DE, Operand.NONE, 1);
        opcodesArray[0x14] = new Instruction(Operation.INC, Operand.D, Operand.NONE, 1);
        opcodesArray[0x15] = new Instruction(Operation.DEC, Operand.D, Operand.NONE, 1);
        opcodesArray[0x16] = new Instruction(Operation.LD, Operand.D, Operand.n8, 2);
        opcodesArray[0x19] = new Instruction(Operation.ADD16, Operand.HL, Operand.DE, 1);
        opcodesArray[0x1A] = new Instruction(Operation.LD, Operand.A, Operand.MemDE, 1);
        opcodesArray[0x1B] = new Instruction(Operation.DEC16, Operand.DE, Operand.NONE, 1);
        opcodesArray[0x1C] = new Instruction(Operation.INC, Operand.E, Operand.NONE, 1);
        opcodesArray[0x1D] = new Instruction(Operation.DEC, Operand.E, Operand.NONE, 1);
        opcodesArray[0x1E] = new Instruction(Operation.LD, Operand.E, Operand.n8, 2);

        // ROW 2
        opcodesArray[0x21] = new Instruction(Operation.LD16, Operand.HL, Operand.n16, 3);
        opcodesArray[0x22] = new Instruction(Operation.LDI, Operand.MemHL, Operand.A, 1);
        opcodesArray[0x23] = new Instruction(Operation.INC16, Operand.HL, Operand.NONE, 1);
        opcodesArray[0x24] = new Instruction(Operation.INC, Operand.H, Operand.NONE, 1);
        opcodesArray[0x25] = new Instruction(Operation.DEC, Operand.H, Operand.NONE, 1);
        opcodesArray[0x26] = new Instruction(Operation.LD, Operand.H, Operand.n8, 2);
        opcodesArray[0x29] = new Instruction(Operation.ADD16, Operand.HL, Operand.HL, 1);
        opcodesArray[0x2A] = new Instruction(Operation.LDI, Operand.A, Operand.MemHL, 1);
        opcodesArray[0x2B] = new Instruction(Operation.DEC16, Operand.HL, Operand.NONE, 1);
        opcodesArray[0x2C] = new Instruction(Operation.INC, Operand.L, Operand.NONE, 1);
        opcodesArray[0x2D] = new Instruction(Operation.DEC, Operand.L, Operand.NONE, 1);
        opcodesArray[0x2E] = new Instruction(Operation.LD, Operand.L, Operand.n8, 2);
        opcodesArray[0x31] = new Instruction(Operation.LD16, Operand.SP, Operand.n16, 3);
        opcodesArray[0x32] = new Instruction(Operation.LDD, Operand.MemHL, Operand.A, 1);
        opcodesArray[0x33] = new Instruction(Operation.INC16, Operand.SP, Operand.NONE, 1);
        opcodesArray[0x34] = new Instruction(Operation.INC, Operand.MemHL, Operand.NONE, 1);
        opcodesArray[0x35] = new Instruction(Operation.DEC, Operand.MemHL, Operand.NONE, 1);
        opcodesArray[0x36] = new Instruction(Operation.LD, Operand.MemHL, Operand.n8, 2);
        opcodesArray[0x39] = new Instruction(Operation.ADD16, Operand.HL, Operand.SP, 1);
        opcodesArray[0x3A] = new Instruction(Operation.LDD, Operand.A, Operand.MemHL, 1);
        opcodesArray[0x3B] = new Instruction(Operation.DEC16, Operand.SP, Operand.NONE, 1);
        opcodesArray[0x3C] = new Instruction(Operation.INC, Operand.A, Operand.NONE, 1);
        opcodesArray[0x3D] = new Instruction(Operation.DEC, Operand.A, Operand.NONE, 1);
        opcodesArray[0x3E] = new Instruction(Operation.LD, Operand.A, Operand.n8, 2);

        // ROW 4
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

        // ROW 5
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
        opcodesArray[0x88] = new Instruction(Operation.ADC, Operand.B, 1);
        opcodesArray[0x89] = new Instruction(Operation.ADC, Operand.C, 1);
        opcodesArray[0x8A] = new Instruction(Operation.ADC, Operand.D, 1);
        opcodesArray[0x8B] = new Instruction(Operation.ADC, Operand.E, 1);
        opcodesArray[0x8C] = new Instruction(Operation.ADC, Operand.H, 1);
        opcodesArray[0x8D] = new Instruction(Operation.ADC, Operand.L, 1);
        opcodesArray[0x8E] = new Instruction(Operation.ADC, Operand.MemHL, 1);
        opcodesArray[0x8F] = new Instruction(Operation.ADC, Operand.A, 1); 
        opcodesArray[0x90] = new Instruction(Operation.SUB, Operand.B, 1);
        opcodesArray[0x91] = new Instruction(Operation.SUB, Operand.C, 1);
        opcodesArray[0x92] = new Instruction(Operation.SUB, Operand.D, 1);
        opcodesArray[0x93] = new Instruction(Operation.SUB, Operand.E, 1);
        opcodesArray[0x94] = new Instruction(Operation.SUB, Operand.H, 1);
        opcodesArray[0x95] = new Instruction(Operation.SUB, Operand.L, 1);
        opcodesArray[0x96] = new Instruction(Operation.SUB, Operand.MemHL, 1);
        opcodesArray[0x97] = new Instruction(Operation.SUB, Operand.A, 1); 
        opcodesArray[0x98] = new Instruction(Operation.SBC, Operand.B, 1);
        opcodesArray[0x99] = new Instruction(Operation.SBC, Operand.C, 1);
        opcodesArray[0x9A] = new Instruction(Operation.SBC, Operand.D, 1);
        opcodesArray[0x9B] = new Instruction(Operation.SBC, Operand.E, 1);
        opcodesArray[0x9C] = new Instruction(Operation.SBC, Operand.H, 1);
        opcodesArray[0x9D] = new Instruction(Operation.SBC, Operand.L, 1);
        opcodesArray[0x9E] = new Instruction(Operation.SBC, Operand.MemHL, 1);
        opcodesArray[0x9F] = new Instruction(Operation.SBC, Operand.A, 1); 
        opcodesArray[0xA0] = new Instruction(Operation.AND, Operand.B, 1);
        opcodesArray[0xA1] = new Instruction(Operation.AND, Operand.C, 1);
        opcodesArray[0xA2] = new Instruction(Operation.AND, Operand.D, 1);
        opcodesArray[0xA3] = new Instruction(Operation.AND, Operand.E, 1);
        opcodesArray[0xA4] = new Instruction(Operation.AND, Operand.H, 1);
        opcodesArray[0xA5] = new Instruction(Operation.AND, Operand.L, 1);
        opcodesArray[0xA6] = new Instruction(Operation.AND, Operand.MemHL, 1);
        opcodesArray[0xA7] = new Instruction(Operation.AND, Operand.A, 1); 
        opcodesArray[0xA8] = new Instruction(Operation.XOR, Operand.B, 1);
        opcodesArray[0xA9] = new Instruction(Operation.XOR, Operand.C, 1);
        opcodesArray[0xAA] = new Instruction(Operation.XOR, Operand.D, 1);
        opcodesArray[0xAB] = new Instruction(Operation.XOR, Operand.E, 1);
        opcodesArray[0xAC] = new Instruction(Operation.XOR, Operand.H, 1);
        opcodesArray[0xAD] = new Instruction(Operation.XOR, Operand.L, 1);
        opcodesArray[0xAE] = new Instruction(Operation.XOR, Operand.MemHL, 1);
        opcodesArray[0xAF] = new Instruction(Operation.XOR, Operand.A, 1); 
        opcodesArray[0xB0] = new Instruction(Operation.OR, Operand.B, 1);
        opcodesArray[0xB1] = new Instruction(Operation.OR, Operand.C, 1);
        opcodesArray[0xB2] = new Instruction(Operation.OR, Operand.D, 1);
        opcodesArray[0xB3] = new Instruction(Operation.OR, Operand.E, 1);
        opcodesArray[0xB4] = new Instruction(Operation.OR, Operand.H, 1);
        opcodesArray[0xB5] = new Instruction(Operation.OR, Operand.L, 1);
        opcodesArray[0xB6] = new Instruction(Operation.OR, Operand.MemHL, 1);
        opcodesArray[0xB7] = new Instruction(Operation.OR, Operand.A, 1); 
        opcodesArray[0xB8] = new Instruction(Operation.CP, Operand.B, 1);
        opcodesArray[0xB9] = new Instruction(Operation.CP, Operand.C, 1);
        opcodesArray[0xBA] = new Instruction(Operation.CP, Operand.D, 1);
        opcodesArray[0xBB] = new Instruction(Operation.CP, Operand.E, 1);
        opcodesArray[0xBC] = new Instruction(Operation.CP, Operand.H, 1);
        opcodesArray[0xBD] = new Instruction(Operation.CP, Operand.L, 1);
        opcodesArray[0xBE] = new Instruction(Operation.CP, Operand.MemHL, 1);
        opcodesArray[0xBF] = new Instruction(Operation.CP, Operand.A, 1); 
        opcodesArray[0xC6] = new Instruction(Operation.ADD, Operand.n8, 2);  
        opcodesArray[0xCE] = new Instruction(Operation.ADC, Operand.n8, 2);  
        opcodesArray[0xD6] = new Instruction(Operation.SUB, Operand.n8, 2);  
        opcodesArray[0xDE] = new Instruction(Operation.SBC, Operand.n8, 2);  
        opcodesArray[0xE6] = new Instruction(Operation.AND, Operand.n8, 2);  
        opcodesArray[0xEE] = new Instruction(Operation.XOR, Operand.n8, 2);  
        opcodesArray[0xE8] = new Instruction(Operation.ADD, Operand.SP, Operand.e8, 2);  
        opcodesArray[0xFE] = new Instruction(Operation.CP, Operand.n8, 2);  
        opcodesArray[0xF6] = new Instruction(Operation.OR, Operand.n8, 2);
        opcodesArray[0xF8] = new Instruction(Operation.LD16, Operand.HL, Operand.SPe8, 2);
        opcodesArray[0xF9] = new Instruction(Operation.LD16, Operand.SP, Operand.HL, 1);

    }

    public Instruction byteToInstruction(int readByte) {
        if (opcodesArray[readByte] == null){
            // System.out.println("Opcode " + Util.hexByte(readByte) + " is not implemented yet.");
            return null;
        }
        return opcodesArray[readByte];
    }

}