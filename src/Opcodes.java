public class Opcodes {
    public Instruction[] opcodesArray;
    public Instruction[] prefixOpcodesArray;
    public Opcodes() {
        // TODO: figure out how to handle prefixed/unprefixed opcodes
        // nvm its simple; switch to prefixed when CB instruction is executed
        opcodesArray = new Instruction[0xFF + 1];
        prefixOpcodesArray = new Instruction[0xFF + 1];
        
        prefixOpcodesArray[0x00] = new Instruction(Operation.RLC, Operand.B, 1);
        prefixOpcodesArray[0x01] = new Instruction(Operation.RLC, Operand.C, 1);
        prefixOpcodesArray[0x02] = new Instruction(Operation.RLC, Operand.D, 1);
        prefixOpcodesArray[0x03] = new Instruction(Operation.RLC, Operand.E, 1);
        prefixOpcodesArray[0x04] = new Instruction(Operation.RLC, Operand.H, 1);
        prefixOpcodesArray[0x05] = new Instruction(Operation.RLC, Operand.L, 1);
        prefixOpcodesArray[0x06] = new Instruction(Operation.RLC, Operand.MemHL, 1);
        prefixOpcodesArray[0x07] = new Instruction(Operation.RLC, Operand.A, 1);
        prefixOpcodesArray[0x08] = new Instruction(Operation.RRC, Operand.B, 1);
        prefixOpcodesArray[0x09] = new Instruction(Operation.RRC, Operand.C, 1);
        prefixOpcodesArray[0x0A] = new Instruction(Operation.RRC, Operand.D, 1);
        prefixOpcodesArray[0x0B] = new Instruction(Operation.RRC, Operand.E, 1);
        prefixOpcodesArray[0x0C] = new Instruction(Operation.RRC, Operand.H, 1);
        prefixOpcodesArray[0x0D] = new Instruction(Operation.RRC, Operand.L, 1);
        prefixOpcodesArray[0x0E] = new Instruction(Operation.RRC, Operand.MemHL, 1);
        prefixOpcodesArray[0x0F] = new Instruction(Operation.RRC, Operand.A, 1);

        prefixOpcodesArray[0x10] = new Instruction(Operation.RL, Operand.B, 1);
        prefixOpcodesArray[0x11] = new Instruction(Operation.RL, Operand.C, 1);
        prefixOpcodesArray[0x12] = new Instruction(Operation.RL, Operand.D, 1);
        prefixOpcodesArray[0x13] = new Instruction(Operation.RL, Operand.E, 1);
        prefixOpcodesArray[0x14] = new Instruction(Operation.RL, Operand.H, 1);
        prefixOpcodesArray[0x15] = new Instruction(Operation.RL, Operand.L, 1);
        prefixOpcodesArray[0x16] = new Instruction(Operation.RL, Operand.MemHL, 1);
        prefixOpcodesArray[0x17] = new Instruction(Operation.RL, Operand.A, 1);
        prefixOpcodesArray[0x18] = new Instruction(Operation.RR, Operand.B, 1);
        prefixOpcodesArray[0x19] = new Instruction(Operation.RR, Operand.C, 1);
        prefixOpcodesArray[0x1A] = new Instruction(Operation.RR, Operand.D, 1);
        prefixOpcodesArray[0x1B] = new Instruction(Operation.RR, Operand.E, 1);
        prefixOpcodesArray[0x1C] = new Instruction(Operation.RR, Operand.H, 1);
        prefixOpcodesArray[0x1D] = new Instruction(Operation.RR, Operand.L, 1);
        prefixOpcodesArray[0x1E] = new Instruction(Operation.RR, Operand.MemHL, 1);
        prefixOpcodesArray[0x1F] = new Instruction(Operation.RR, Operand.A, 1);

        prefixOpcodesArray[0x20] = new Instruction(Operation.SLA, Operand.B, 1);
        prefixOpcodesArray[0x21] = new Instruction(Operation.SLA, Operand.C, 1);
        prefixOpcodesArray[0x22] = new Instruction(Operation.SLA, Operand.D, 1);
        prefixOpcodesArray[0x23] = new Instruction(Operation.SLA, Operand.E, 1);
        prefixOpcodesArray[0x24] = new Instruction(Operation.SLA, Operand.H, 1);
        prefixOpcodesArray[0x25] = new Instruction(Operation.SLA, Operand.L, 1);
        prefixOpcodesArray[0x26] = new Instruction(Operation.SLA, Operand.MemHL, 1);
        prefixOpcodesArray[0x27] = new Instruction(Operation.SLA, Operand.A, 1);
        prefixOpcodesArray[0x28] = new Instruction(Operation.SRA, Operand.B, 1);
        prefixOpcodesArray[0x29] = new Instruction(Operation.SRA, Operand.C, 1);
        prefixOpcodesArray[0x2A] = new Instruction(Operation.SRA, Operand.D, 1);
        prefixOpcodesArray[0x2B] = new Instruction(Operation.SRA, Operand.E, 1);
        prefixOpcodesArray[0x2C] = new Instruction(Operation.SRA, Operand.H, 1);
        prefixOpcodesArray[0x2D] = new Instruction(Operation.SRA, Operand.L, 1);
        prefixOpcodesArray[0x2E] = new Instruction(Operation.SRA, Operand.MemHL, 1);
        prefixOpcodesArray[0x2F] = new Instruction(Operation.SRA, Operand.A, 1);
        
        prefixOpcodesArray[0x30] = new Instruction(Operation.SWAP, Operand.B, 1);
        prefixOpcodesArray[0x31] = new Instruction(Operation.SWAP, Operand.C, 1);
        prefixOpcodesArray[0x32] = new Instruction(Operation.SWAP, Operand.D, 1);
        prefixOpcodesArray[0x33] = new Instruction(Operation.SWAP, Operand.E, 1);
        prefixOpcodesArray[0x34] = new Instruction(Operation.SWAP, Operand.H, 1);
        prefixOpcodesArray[0x35] = new Instruction(Operation.SWAP, Operand.L, 1);
        prefixOpcodesArray[0x36] = new Instruction(Operation.SWAP, Operand.MemHL, 1);
        prefixOpcodesArray[0x37] = new Instruction(Operation.SWAP, Operand.A, 1);
        prefixOpcodesArray[0x38] = new Instruction(Operation.SRL, Operand.B, 1);
        prefixOpcodesArray[0x39] = new Instruction(Operation.SRL, Operand.C, 1);
        prefixOpcodesArray[0x3A] = new Instruction(Operation.SRL, Operand.D, 1);
        prefixOpcodesArray[0x3B] = new Instruction(Operation.SRL, Operand.E, 1);
        prefixOpcodesArray[0x3C] = new Instruction(Operation.SRL, Operand.H, 1);
        prefixOpcodesArray[0x3D] = new Instruction(Operation.SRL, Operand.L, 1);
        prefixOpcodesArray[0x3E] = new Instruction(Operation.SRL, Operand.MemHL, 1);
        prefixOpcodesArray[0x3F] = new Instruction(Operation.SRL, Operand.A, 1);


        
        prefixOpcodesArray[0x40] = new Instruction(Operation.BIT, Operand.BIT0, Operand.B, 1);
        prefixOpcodesArray[0x41] = new Instruction(Operation.BIT, Operand.BIT0, Operand.C, 1);
        prefixOpcodesArray[0x42] = new Instruction(Operation.BIT, Operand.BIT0, Operand.D, 1);
        prefixOpcodesArray[0x43] = new Instruction(Operation.BIT, Operand.BIT0, Operand.E, 1);
        prefixOpcodesArray[0x44] = new Instruction(Operation.BIT, Operand.BIT0, Operand.H, 1);
        prefixOpcodesArray[0x45] = new Instruction(Operation.BIT, Operand.BIT0, Operand.L, 1);
        prefixOpcodesArray[0x46] = new Instruction(Operation.BIT, Operand.BIT0, Operand.MemHL, 1);
        prefixOpcodesArray[0x47] = new Instruction(Operation.BIT, Operand.BIT0, Operand.A, 1);
        prefixOpcodesArray[0x48] = new Instruction(Operation.BIT, Operand.BIT1, Operand.B, 1);
        prefixOpcodesArray[0x49] = new Instruction(Operation.BIT, Operand.BIT1, Operand.C, 1);
        prefixOpcodesArray[0x4A] = new Instruction(Operation.BIT, Operand.BIT1, Operand.D, 1);
        prefixOpcodesArray[0x4B] = new Instruction(Operation.BIT, Operand.BIT1, Operand.E, 1);
        prefixOpcodesArray[0x4C] = new Instruction(Operation.BIT, Operand.BIT1, Operand.H, 1);
        prefixOpcodesArray[0x4D] = new Instruction(Operation.BIT, Operand.BIT1, Operand.L, 1);
        prefixOpcodesArray[0x4E] = new Instruction(Operation.BIT, Operand.BIT1, Operand.MemHL, 1);
        prefixOpcodesArray[0x4F] = new Instruction(Operation.BIT, Operand.BIT1, Operand.A, 1);

        prefixOpcodesArray[0x50] = new Instruction(Operation.BIT, Operand.BIT2, Operand.B, 1);
        prefixOpcodesArray[0x51] = new Instruction(Operation.BIT, Operand.BIT2, Operand.C, 1);
        prefixOpcodesArray[0x52] = new Instruction(Operation.BIT, Operand.BIT2, Operand.D, 1);
        prefixOpcodesArray[0x53] = new Instruction(Operation.BIT, Operand.BIT2, Operand.E, 1);
        prefixOpcodesArray[0x54] = new Instruction(Operation.BIT, Operand.BIT2, Operand.H, 1);
        prefixOpcodesArray[0x55] = new Instruction(Operation.BIT, Operand.BIT2, Operand.L, 1);
        prefixOpcodesArray[0x56] = new Instruction(Operation.BIT, Operand.BIT2, Operand.MemHL, 1);
        prefixOpcodesArray[0x57] = new Instruction(Operation.BIT, Operand.BIT2, Operand.A, 1);
        prefixOpcodesArray[0x58] = new Instruction(Operation.BIT, Operand.BIT3, Operand.B, 1);
        prefixOpcodesArray[0x59] = new Instruction(Operation.BIT, Operand.BIT3, Operand.C, 1);
        prefixOpcodesArray[0x5A] = new Instruction(Operation.BIT, Operand.BIT3, Operand.D, 1);
        prefixOpcodesArray[0x5B] = new Instruction(Operation.BIT, Operand.BIT3, Operand.E, 1);
        prefixOpcodesArray[0x5C] = new Instruction(Operation.BIT, Operand.BIT3, Operand.H, 1);
        prefixOpcodesArray[0x5D] = new Instruction(Operation.BIT, Operand.BIT3, Operand.L, 1);
        prefixOpcodesArray[0x5E] = new Instruction(Operation.BIT, Operand.BIT3, Operand.MemHL, 1);
        prefixOpcodesArray[0x5F] = new Instruction(Operation.BIT, Operand.BIT3, Operand.A, 1);

        prefixOpcodesArray[0x60] = new Instruction(Operation.BIT, Operand.BIT4, Operand.B, 1);
        prefixOpcodesArray[0x61] = new Instruction(Operation.BIT, Operand.BIT4, Operand.C, 1);
        prefixOpcodesArray[0x62] = new Instruction(Operation.BIT, Operand.BIT4, Operand.D, 1);
        prefixOpcodesArray[0x63] = new Instruction(Operation.BIT, Operand.BIT4, Operand.E, 1);
        prefixOpcodesArray[0x64] = new Instruction(Operation.BIT, Operand.BIT4, Operand.H, 1);
        prefixOpcodesArray[0x65] = new Instruction(Operation.BIT, Operand.BIT4, Operand.L, 1);
        prefixOpcodesArray[0x66] = new Instruction(Operation.BIT, Operand.BIT4, Operand.MemHL, 1);
        prefixOpcodesArray[0x67] = new Instruction(Operation.BIT, Operand.BIT4, Operand.A, 1);
        prefixOpcodesArray[0x68] = new Instruction(Operation.BIT, Operand.BIT5, Operand.B, 1);
        prefixOpcodesArray[0x69] = new Instruction(Operation.BIT, Operand.BIT5, Operand.C, 1);
        prefixOpcodesArray[0x6A] = new Instruction(Operation.BIT, Operand.BIT5, Operand.D, 1);
        prefixOpcodesArray[0x6B] = new Instruction(Operation.BIT, Operand.BIT5, Operand.E, 1);
        prefixOpcodesArray[0x6C] = new Instruction(Operation.BIT, Operand.BIT5, Operand.H, 1);
        prefixOpcodesArray[0x6D] = new Instruction(Operation.BIT, Operand.BIT5, Operand.L, 1);
        prefixOpcodesArray[0x6E] = new Instruction(Operation.BIT, Operand.BIT5, Operand.MemHL, 1);
        prefixOpcodesArray[0x6F] = new Instruction(Operation.BIT, Operand.BIT5, Operand.A, 1);

        prefixOpcodesArray[0x70] = new Instruction(Operation.BIT, Operand.BIT6, Operand.B, 1);
        prefixOpcodesArray[0x71] = new Instruction(Operation.BIT, Operand.BIT6, Operand.C, 1);
        prefixOpcodesArray[0x72] = new Instruction(Operation.BIT, Operand.BIT6, Operand.D, 1);
        prefixOpcodesArray[0x73] = new Instruction(Operation.BIT, Operand.BIT6, Operand.E, 1);
        prefixOpcodesArray[0x74] = new Instruction(Operation.BIT, Operand.BIT6, Operand.H, 1);
        prefixOpcodesArray[0x75] = new Instruction(Operation.BIT, Operand.BIT6, Operand.L, 1);
        prefixOpcodesArray[0x76] = new Instruction(Operation.BIT, Operand.BIT6, Operand.MemHL, 1);
        prefixOpcodesArray[0x77] = new Instruction(Operation.BIT, Operand.BIT6, Operand.A, 1);
        prefixOpcodesArray[0x78] = new Instruction(Operation.BIT, Operand.BIT7, Operand.B, 1);
        prefixOpcodesArray[0x79] = new Instruction(Operation.BIT, Operand.BIT7, Operand.C, 1);
        prefixOpcodesArray[0x7A] = new Instruction(Operation.BIT, Operand.BIT7, Operand.D, 1);
        prefixOpcodesArray[0x7B] = new Instruction(Operation.BIT, Operand.BIT7, Operand.E, 1);
        prefixOpcodesArray[0x7C] = new Instruction(Operation.BIT, Operand.BIT7, Operand.H, 1);
        prefixOpcodesArray[0x7D] = new Instruction(Operation.BIT, Operand.BIT7, Operand.L, 1);
        prefixOpcodesArray[0x7E] = new Instruction(Operation.BIT, Operand.BIT7, Operand.MemHL, 1);
        prefixOpcodesArray[0x7F] = new Instruction(Operation.BIT, Operand.BIT7, Operand.A, 1);

        prefixOpcodesArray[0x80] = new Instruction(Operation.RES, Operand.BIT0, Operand.B, 1);
        prefixOpcodesArray[0x81] = new Instruction(Operation.RES, Operand.BIT0, Operand.C, 1);
        prefixOpcodesArray[0x82] = new Instruction(Operation.RES, Operand.BIT0, Operand.D, 1);
        prefixOpcodesArray[0x83] = new Instruction(Operation.RES, Operand.BIT0, Operand.E, 1);
        prefixOpcodesArray[0x84] = new Instruction(Operation.RES, Operand.BIT0, Operand.H, 1);
        prefixOpcodesArray[0x85] = new Instruction(Operation.RES, Operand.BIT0, Operand.L, 1);
        prefixOpcodesArray[0x86] = new Instruction(Operation.RES, Operand.BIT0, Operand.MemHL, 1);
        prefixOpcodesArray[0x87] = new Instruction(Operation.RES, Operand.BIT0, Operand.A, 1);
        prefixOpcodesArray[0x88] = new Instruction(Operation.RES, Operand.BIT1, Operand.B, 1);
        prefixOpcodesArray[0x89] = new Instruction(Operation.RES, Operand.BIT1, Operand.C, 1);
        prefixOpcodesArray[0x8A] = new Instruction(Operation.RES, Operand.BIT1, Operand.D, 1);
        prefixOpcodesArray[0x8B] = new Instruction(Operation.RES, Operand.BIT1, Operand.E, 1);
        prefixOpcodesArray[0x8C] = new Instruction(Operation.RES, Operand.BIT1, Operand.H, 1);
        prefixOpcodesArray[0x8D] = new Instruction(Operation.RES, Operand.BIT1, Operand.L, 1);
        prefixOpcodesArray[0x8E] = new Instruction(Operation.RES, Operand.BIT1, Operand.MemHL, 1);
        prefixOpcodesArray[0x8F] = new Instruction(Operation.RES, Operand.BIT1, Operand.A, 1);

        prefixOpcodesArray[0x90] = new Instruction(Operation.RES, Operand.BIT2, Operand.B, 1);
        prefixOpcodesArray[0x91] = new Instruction(Operation.RES, Operand.BIT2, Operand.C, 1);
        prefixOpcodesArray[0x92] = new Instruction(Operation.RES, Operand.BIT2, Operand.D, 1);
        prefixOpcodesArray[0x93] = new Instruction(Operation.RES, Operand.BIT2, Operand.E, 1);
        prefixOpcodesArray[0x94] = new Instruction(Operation.RES, Operand.BIT2, Operand.H, 1);
        prefixOpcodesArray[0x95] = new Instruction(Operation.RES, Operand.BIT2, Operand.L, 1);
        prefixOpcodesArray[0x96] = new Instruction(Operation.RES, Operand.BIT2, Operand.MemHL, 1);
        prefixOpcodesArray[0x97] = new Instruction(Operation.RES, Operand.BIT2, Operand.A, 1);
        prefixOpcodesArray[0x98] = new Instruction(Operation.RES, Operand.BIT3, Operand.B, 1);
        prefixOpcodesArray[0x99] = new Instruction(Operation.RES, Operand.BIT3, Operand.C, 1);
        prefixOpcodesArray[0x9A] = new Instruction(Operation.RES, Operand.BIT3, Operand.D, 1);
        prefixOpcodesArray[0x9B] = new Instruction(Operation.RES, Operand.BIT3, Operand.E, 1);
        prefixOpcodesArray[0x9C] = new Instruction(Operation.RES, Operand.BIT3, Operand.H, 1);
        prefixOpcodesArray[0x9D] = new Instruction(Operation.RES, Operand.BIT3, Operand.L, 1);
        prefixOpcodesArray[0x9E] = new Instruction(Operation.RES, Operand.BIT3, Operand.MemHL, 1);
        prefixOpcodesArray[0x9F] = new Instruction(Operation.RES, Operand.BIT3, Operand.A, 1);

        prefixOpcodesArray[0xA0] = new Instruction(Operation.RES, Operand.BIT4, Operand.B, 1);
        prefixOpcodesArray[0xA1] = new Instruction(Operation.RES, Operand.BIT4, Operand.C, 1);
        prefixOpcodesArray[0xA2] = new Instruction(Operation.RES, Operand.BIT4, Operand.D, 1);
        prefixOpcodesArray[0xA3] = new Instruction(Operation.RES, Operand.BIT4, Operand.E, 1);
        prefixOpcodesArray[0xA4] = new Instruction(Operation.RES, Operand.BIT4, Operand.H, 1);
        prefixOpcodesArray[0xA5] = new Instruction(Operation.RES, Operand.BIT4, Operand.L, 1);
        prefixOpcodesArray[0xA6] = new Instruction(Operation.RES, Operand.BIT4, Operand.MemHL, 1);
        prefixOpcodesArray[0xA7] = new Instruction(Operation.RES, Operand.BIT4, Operand.A, 1);
        prefixOpcodesArray[0xA8] = new Instruction(Operation.RES, Operand.BIT5, Operand.B, 1);
        prefixOpcodesArray[0xA9] = new Instruction(Operation.RES, Operand.BIT5, Operand.C, 1);
        prefixOpcodesArray[0xAA] = new Instruction(Operation.RES, Operand.BIT5, Operand.D, 1);
        prefixOpcodesArray[0xAB] = new Instruction(Operation.RES, Operand.BIT5, Operand.E, 1);
        prefixOpcodesArray[0xAC] = new Instruction(Operation.RES, Operand.BIT5, Operand.H, 1);
        prefixOpcodesArray[0xAD] = new Instruction(Operation.RES, Operand.BIT5, Operand.L, 1);
        prefixOpcodesArray[0xAE] = new Instruction(Operation.RES, Operand.BIT5, Operand.MemHL, 1);
        prefixOpcodesArray[0xAF] = new Instruction(Operation.RES, Operand.BIT5, Operand.A, 1);

        prefixOpcodesArray[0xB0] = new Instruction(Operation.RES, Operand.BIT6, Operand.B, 1);
        prefixOpcodesArray[0xB1] = new Instruction(Operation.RES, Operand.BIT6, Operand.C, 1);
        prefixOpcodesArray[0xB2] = new Instruction(Operation.RES, Operand.BIT6, Operand.D, 1);
        prefixOpcodesArray[0xB3] = new Instruction(Operation.RES, Operand.BIT6, Operand.E, 1);
        prefixOpcodesArray[0xB4] = new Instruction(Operation.RES, Operand.BIT6, Operand.H, 1);
        prefixOpcodesArray[0xB5] = new Instruction(Operation.RES, Operand.BIT6, Operand.L, 1);
        prefixOpcodesArray[0xB6] = new Instruction(Operation.RES, Operand.BIT6, Operand.MemHL, 1);
        prefixOpcodesArray[0xB7] = new Instruction(Operation.RES, Operand.BIT6, Operand.A, 1);
        prefixOpcodesArray[0xB8] = new Instruction(Operation.RES, Operand.BIT7, Operand.B, 1);
        prefixOpcodesArray[0xB9] = new Instruction(Operation.RES, Operand.BIT7, Operand.C, 1);
        prefixOpcodesArray[0xBA] = new Instruction(Operation.RES, Operand.BIT7, Operand.D, 1);
        prefixOpcodesArray[0xBB] = new Instruction(Operation.RES, Operand.BIT7, Operand.E, 1);
        prefixOpcodesArray[0xBC] = new Instruction(Operation.RES, Operand.BIT7, Operand.H, 1);
        prefixOpcodesArray[0xBD] = new Instruction(Operation.RES, Operand.BIT7, Operand.L, 1);
        prefixOpcodesArray[0xBE] = new Instruction(Operation.RES, Operand.BIT7, Operand.MemHL, 1);
        prefixOpcodesArray[0xBF] = new Instruction(Operation.RES, Operand.BIT7, Operand.A, 1);

        prefixOpcodesArray[0xC0] = new Instruction(Operation.SET, Operand.BIT0, Operand.B, 1);
        prefixOpcodesArray[0xC1] = new Instruction(Operation.SET, Operand.BIT0, Operand.C, 1);
        prefixOpcodesArray[0xC2] = new Instruction(Operation.SET, Operand.BIT0, Operand.D, 1);
        prefixOpcodesArray[0xC3] = new Instruction(Operation.SET, Operand.BIT0, Operand.E, 1);
        prefixOpcodesArray[0xC4] = new Instruction(Operation.SET, Operand.BIT0, Operand.H, 1);
        prefixOpcodesArray[0xC5] = new Instruction(Operation.SET, Operand.BIT0, Operand.L, 1);
        prefixOpcodesArray[0xC6] = new Instruction(Operation.SET, Operand.BIT0, Operand.MemHL, 1);
        prefixOpcodesArray[0xC7] = new Instruction(Operation.SET, Operand.BIT0, Operand.A, 1);
        prefixOpcodesArray[0xC8] = new Instruction(Operation.SET, Operand.BIT1, Operand.B, 1);
        prefixOpcodesArray[0xC9] = new Instruction(Operation.SET, Operand.BIT1, Operand.C, 1);
        prefixOpcodesArray[0xCA] = new Instruction(Operation.SET, Operand.BIT1, Operand.D, 1);
        prefixOpcodesArray[0xCB] = new Instruction(Operation.SET, Operand.BIT1, Operand.E, 1);
        prefixOpcodesArray[0xCC] = new Instruction(Operation.SET, Operand.BIT1, Operand.H, 1);
        prefixOpcodesArray[0xCD] = new Instruction(Operation.SET, Operand.BIT1, Operand.L, 1);
        prefixOpcodesArray[0xCE] = new Instruction(Operation.SET, Operand.BIT1, Operand.MemHL, 1);
        prefixOpcodesArray[0xCF] = new Instruction(Operation.SET, Operand.BIT1, Operand.A, 1);

        prefixOpcodesArray[0xD0] = new Instruction(Operation.SET, Operand.BIT2, Operand.B, 1);
        prefixOpcodesArray[0xD1] = new Instruction(Operation.SET, Operand.BIT2, Operand.C, 1);
        prefixOpcodesArray[0xD2] = new Instruction(Operation.SET, Operand.BIT2, Operand.D, 1);
        prefixOpcodesArray[0xD3] = new Instruction(Operation.SET, Operand.BIT2, Operand.E, 1);
        prefixOpcodesArray[0xD4] = new Instruction(Operation.SET, Operand.BIT2, Operand.H, 1);
        prefixOpcodesArray[0xD5] = new Instruction(Operation.SET, Operand.BIT2, Operand.L, 1);
        prefixOpcodesArray[0xD6] = new Instruction(Operation.SET, Operand.BIT2, Operand.MemHL, 1);
        prefixOpcodesArray[0xD7] = new Instruction(Operation.SET, Operand.BIT2, Operand.A, 1);
        prefixOpcodesArray[0xD8] = new Instruction(Operation.SET, Operand.BIT3, Operand.B, 1);
        prefixOpcodesArray[0xD9] = new Instruction(Operation.SET, Operand.BIT3, Operand.C, 1);
        prefixOpcodesArray[0xDA] = new Instruction(Operation.SET, Operand.BIT3, Operand.D, 1);
        prefixOpcodesArray[0xDB] = new Instruction(Operation.SET, Operand.BIT3, Operand.E, 1);
        prefixOpcodesArray[0xDC] = new Instruction(Operation.SET, Operand.BIT3, Operand.H, 1);
        prefixOpcodesArray[0xDD] = new Instruction(Operation.SET, Operand.BIT3, Operand.L, 1);
        prefixOpcodesArray[0xDE] = new Instruction(Operation.SET, Operand.BIT3, Operand.MemHL, 1);
        prefixOpcodesArray[0xDF] = new Instruction(Operation.SET, Operand.BIT3, Operand.A, 1);

        prefixOpcodesArray[0xE0] = new Instruction(Operation.SET, Operand.BIT4, Operand.B, 1);
        prefixOpcodesArray[0xE1] = new Instruction(Operation.SET, Operand.BIT4, Operand.C, 1);
        prefixOpcodesArray[0xE2] = new Instruction(Operation.SET, Operand.BIT4, Operand.D, 1);
        prefixOpcodesArray[0xE3] = new Instruction(Operation.SET, Operand.BIT4, Operand.E, 1);
        prefixOpcodesArray[0xE4] = new Instruction(Operation.SET, Operand.BIT4, Operand.H, 1);
        prefixOpcodesArray[0xE5] = new Instruction(Operation.SET, Operand.BIT4, Operand.L, 1);
        prefixOpcodesArray[0xE6] = new Instruction(Operation.SET, Operand.BIT4, Operand.MemHL, 1);
        prefixOpcodesArray[0xE7] = new Instruction(Operation.SET, Operand.BIT4, Operand.A, 1);
        prefixOpcodesArray[0xE8] = new Instruction(Operation.SET, Operand.BIT5, Operand.B, 1);
        prefixOpcodesArray[0xE9] = new Instruction(Operation.SET, Operand.BIT5, Operand.C, 1);
        prefixOpcodesArray[0xEA] = new Instruction(Operation.SET, Operand.BIT5, Operand.D, 1);
        prefixOpcodesArray[0xEB] = new Instruction(Operation.SET, Operand.BIT5, Operand.E, 1);
        prefixOpcodesArray[0xEC] = new Instruction(Operation.SET, Operand.BIT5, Operand.H, 1);
        prefixOpcodesArray[0xED] = new Instruction(Operation.SET, Operand.BIT5, Operand.L, 1);
        prefixOpcodesArray[0xEE] = new Instruction(Operation.SET, Operand.BIT5, Operand.MemHL, 1);
        prefixOpcodesArray[0xEF] = new Instruction(Operation.SET, Operand.BIT5, Operand.A, 1);

        prefixOpcodesArray[0xF0] = new Instruction(Operation.SET, Operand.BIT6, Operand.B, 1);
        prefixOpcodesArray[0xF1] = new Instruction(Operation.SET, Operand.BIT6, Operand.C, 1);
        prefixOpcodesArray[0xF2] = new Instruction(Operation.SET, Operand.BIT6, Operand.D, 1);
        prefixOpcodesArray[0xF3] = new Instruction(Operation.SET, Operand.BIT6, Operand.E, 1);
        prefixOpcodesArray[0xF4] = new Instruction(Operation.SET, Operand.BIT6, Operand.H, 1);
        prefixOpcodesArray[0xF5] = new Instruction(Operation.SET, Operand.BIT6, Operand.L, 1);
        prefixOpcodesArray[0xF6] = new Instruction(Operation.SET, Operand.BIT6, Operand.MemHL, 1);
        prefixOpcodesArray[0xF7] = new Instruction(Operation.SET, Operand.BIT6, Operand.A, 1);
        prefixOpcodesArray[0xF8] = new Instruction(Operation.SET, Operand.BIT7, Operand.B, 1);
        prefixOpcodesArray[0xF9] = new Instruction(Operation.SET, Operand.BIT7, Operand.C, 1);
        prefixOpcodesArray[0xFA] = new Instruction(Operation.SET, Operand.BIT7, Operand.D, 1);
        prefixOpcodesArray[0xFB] = new Instruction(Operation.SET, Operand.BIT7, Operand.E, 1);
        prefixOpcodesArray[0xFC] = new Instruction(Operation.SET, Operand.BIT7, Operand.H, 1);
        prefixOpcodesArray[0xFD] = new Instruction(Operation.SET, Operand.BIT7, Operand.L, 1);
        prefixOpcodesArray[0xFE] = new Instruction(Operation.SET, Operand.BIT7, Operand.MemHL, 1);
        prefixOpcodesArray[0xFF] = new Instruction(Operation.SET, Operand.BIT7, Operand.A, 1);
        // ROW 0
        opcodesArray[0x00] = new Instruction(Operation.NOP, Operand.NONE, 1);
        opcodesArray[0x01] = new Instruction(Operation.LD16, Operand.BC, Operand.n16, 3);
        opcodesArray[0x02] = new Instruction(Operation.LD, Operand.MemBC, Operand.A, 1);
        opcodesArray[0x03] = new Instruction(Operation.INC16, Operand.BC, Operand.NONE, 1);
        opcodesArray[0x04] = new Instruction(Operation.INC, Operand.B, Operand.NONE, 1);
        opcodesArray[0x05] = new Instruction(Operation.DEC, Operand.B, Operand.NONE, 1);
        opcodesArray[0x06] = new Instruction(Operation.LD, Operand.B, Operand.n8, 2);
        opcodesArray[0x07] = new Instruction(Operation.RLCA, 1);
        opcodesArray[0x08] = new Instruction(Operation.LD16, Operand.a16, Operand.SP, 3);
        opcodesArray[0x09] = new Instruction(Operation.ADD16, Operand.HL, Operand.BC, 1);
        opcodesArray[0x0A] = new Instruction(Operation.LD, Operand.A, Operand.MemBC, 1);
        opcodesArray[0x0B] = new Instruction(Operation.DEC16, Operand.BC, Operand.NONE, 1);
        opcodesArray[0x0C] = new Instruction(Operation.INC, Operand.C, Operand.NONE, 1);
        opcodesArray[0x0D] = new Instruction(Operation.DEC, Operand.C, Operand.NONE, 1);
        opcodesArray[0x0E] = new Instruction(Operation.LD, Operand.C, Operand.n8, 2);
        opcodesArray[0x0F] = new Instruction(Operation.RRCA, 1);

        // ROW 1
        opcodesArray[0x10] = new Instruction(Operation.STOP, 1);
        opcodesArray[0x11] = new Instruction(Operation.LD16, Operand.DE, Operand.n16, 3);
        opcodesArray[0x12] = new Instruction(Operation.LD, Operand.MemDE, Operand.A, 1);
        opcodesArray[0x13] = new Instruction(Operation.INC16, Operand.DE, Operand.NONE, 1);
        opcodesArray[0x14] = new Instruction(Operation.INC, Operand.D, Operand.NONE, 1);
        opcodesArray[0x15] = new Instruction(Operation.DEC, Operand.D, Operand.NONE, 1);
        opcodesArray[0x16] = new Instruction(Operation.LD, Operand.D, Operand.n8, 2);
        opcodesArray[0x17] = new Instruction(Operation.RLA, 1);
        opcodesArray[0x18] = new Instruction(Operation.JR, Operand.e8, 2);
        opcodesArray[0x19] = new Instruction(Operation.ADD16, Operand.HL, Operand.DE, 1);
        opcodesArray[0x1A] = new Instruction(Operation.LD, Operand.A, Operand.MemDE, 1);
        opcodesArray[0x1B] = new Instruction(Operation.DEC16, Operand.DE, Operand.NONE, 1);
        opcodesArray[0x1C] = new Instruction(Operation.INC, Operand.E, Operand.NONE, 1);
        opcodesArray[0x1D] = new Instruction(Operation.DEC, Operand.E, Operand.NONE, 1);
        opcodesArray[0x1E] = new Instruction(Operation.LD, Operand.E, Operand.n8, 2);
        opcodesArray[0x1F] = new Instruction(Operation.RCA, 1);

        // ROW 2
        opcodesArray[0x20] = new Instruction(Operation.JR, Operand.JumpNZ, Operand.e8, 2);
        opcodesArray[0x21] = new Instruction(Operation.LD16, Operand.HL, Operand.n16, 3);
        opcodesArray[0x22] = new Instruction(Operation.LDI, Operand.MemHL, Operand.A, 1);
        opcodesArray[0x23] = new Instruction(Operation.INC16, Operand.HL, Operand.NONE, 1);
        opcodesArray[0x24] = new Instruction(Operation.INC, Operand.H, Operand.NONE, 1);
        opcodesArray[0x25] = new Instruction(Operation.DEC, Operand.H, Operand.NONE, 1);
        opcodesArray[0x26] = new Instruction(Operation.LD, Operand.H, Operand.n8, 2);
        opcodesArray[0x27] = new Instruction(Operation.DAA, 1);
        opcodesArray[0x28] = new Instruction(Operation.JR, Operand.JumpZ, Operand.e8, 2);
        opcodesArray[0x29] = new Instruction(Operation.ADD16, Operand.HL, Operand.HL, 1);
        opcodesArray[0x2A] = new Instruction(Operation.LDI, Operand.A, Operand.MemHL, 1);
        opcodesArray[0x2B] = new Instruction(Operation.DEC16, Operand.HL, Operand.NONE, 1);
        opcodesArray[0x2C] = new Instruction(Operation.INC, Operand.L, Operand.NONE, 1);
        opcodesArray[0x2D] = new Instruction(Operation.DEC, Operand.L, Operand.NONE, 1);
        opcodesArray[0x2E] = new Instruction(Operation.LD, Operand.L, Operand.n8, 2);
        opcodesArray[0x2F] = new Instruction(Operation.CPL, 1);

        // ROW 3
        opcodesArray[0x30] = new Instruction(Operation.JR, Operand.JumpNC, Operand.e8, 2);
        opcodesArray[0x31] = new Instruction(Operation.LD16, Operand.SP, Operand.n16, 3);
        opcodesArray[0x32] = new Instruction(Operation.LDD, Operand.MemHL, Operand.A, 1);
        opcodesArray[0x33] = new Instruction(Operation.INC16, Operand.SP, Operand.NONE, 1);
        opcodesArray[0x34] = new Instruction(Operation.INC, Operand.MemHL, Operand.NONE, 1);
        opcodesArray[0x35] = new Instruction(Operation.DEC, Operand.MemHL, Operand.NONE, 1);
        opcodesArray[0x36] = new Instruction(Operation.LD, Operand.MemHL, Operand.n8, 2);
        opcodesArray[0x37] = new Instruction(Operation.SCF, 1);
        opcodesArray[0x38] = new Instruction(Operation.JR, Operand.JumpC, Operand.e8, 2);
        opcodesArray[0x39] = new Instruction(Operation.ADD16, Operand.HL, Operand.SP, 1);
        opcodesArray[0x3A] = new Instruction(Operation.LDD, Operand.A, Operand.MemHL, 1);
        opcodesArray[0x3B] = new Instruction(Operation.DEC16, Operand.SP, Operand.NONE, 1);
        opcodesArray[0x3C] = new Instruction(Operation.INC, Operand.A, Operand.NONE, 1);
        opcodesArray[0x3D] = new Instruction(Operation.DEC, Operand.A, Operand.NONE, 1);
        opcodesArray[0x3E] = new Instruction(Operation.LD, Operand.A, Operand.n8, 2);
        opcodesArray[0x3F] = new Instruction(Operation.CCF, 1);

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

        // ROW 7
        opcodesArray[0x70] = new Instruction(Operation.LD, Operand.MemHL, Operand.B, 1);
        opcodesArray[0x71] = new Instruction(Operation.LD, Operand.MemHL, Operand.C, 1);
        opcodesArray[0x72] = new Instruction(Operation.LD, Operand.MemHL, Operand.D, 1);
        opcodesArray[0x73] = new Instruction(Operation.LD, Operand.MemHL, Operand.E, 1);
        opcodesArray[0x74] = new Instruction(Operation.LD, Operand.MemHL, Operand.H, 1);
        opcodesArray[0x75] = new Instruction(Operation.LD, Operand.MemHL, Operand.L, 1);
        opcodesArray[0x76] = new Instruction(Operation.HALT, 1);
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

        // ROW A
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

        // ROW B
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

        // ROW C
        opcodesArray[0xC0] = new Instruction(Operation.RET, Operand.JumpNZ, 1);
        opcodesArray[0xC1] = new Instruction(Operation.POP, Operand.BC, 1);
        opcodesArray[0xC2] = new Instruction(Operation.JP, Operand.JumpNZ, 3);
        opcodesArray[0xC3] = new Instruction(Operation.JP, Operand.a16, 3);
        opcodesArray[0xC4] = new Instruction(Operation.CALL, Operand.JumpNZ, Operand.a16, 3);
        opcodesArray[0xC5] = new Instruction(Operation.PUSH, Operand.BC, 1);
        opcodesArray[0xC6] = new Instruction(Operation.ADD, Operand.n8, 2);
        opcodesArray[0xC7] = new Instruction(Operation.RST, Operand.RST00, 1);
        opcodesArray[0xC8] = new Instruction(Operation.RET, Operand.JumpZ, 1);
        opcodesArray[0xC9] = new Instruction(Operation.RET, 1);
        opcodesArray[0xCA] = new Instruction(Operation.JP, Operand.JumpZ, 3);
        opcodesArray[0xCB] = new Instruction(Operation.PREFIX, 2);
        opcodesArray[0xCC] = new Instruction(Operation.CALL, Operand.JumpZ, Operand.a16, 3);
        opcodesArray[0xCD] = new Instruction(Operation.CALL, Operand.a16, 3);
        opcodesArray[0xCE] = new Instruction(Operation.ADC, Operand.n8, 2);
        opcodesArray[0xCF] = new Instruction(Operation.RST, Operand.RST08, 1);

        // ROW D
        opcodesArray[0xD0] = new Instruction(Operation.RET, Operand.JumpNC, 1);
        opcodesArray[0xD1] = new Instruction(Operation.POP, Operand.DE, 1);
        opcodesArray[0xD2] = new Instruction(Operation.JP, Operand.JumpNC, 3);
        opcodesArray[0xD4] = new Instruction(Operation.CALL, Operand.JumpNC, Operand.a16, 3);
        opcodesArray[0xD5] = new Instruction(Operation.PUSH, Operand.DE, 1);
        opcodesArray[0xD6] = new Instruction(Operation.SUB, Operand.n8, 2);
        opcodesArray[0xD7] = new Instruction(Operation.RST, Operand.RST10, 1);
        opcodesArray[0xD8] = new Instruction(Operation.RET, Operand.JumpC, 1);
        opcodesArray[0xD9] = new Instruction(Operation.RETI, 1);
        opcodesArray[0xDA] = new Instruction(Operation.JP, Operand.JumpC, 3);
        opcodesArray[0xDC] = new Instruction(Operation.CALL, Operand.JumpC, Operand.a16, 3);
        opcodesArray[0xDE] = new Instruction(Operation.SBC, Operand.n8, 2);
        opcodesArray[0xDF] = new Instruction(Operation.RST, Operand.RST18, 1);
        // ROW E
        opcodesArray[0xE0] = new Instruction(Operation.LDH, Operand.a8, Operand.A, 2);
        opcodesArray[0xE1] = new Instruction(Operation.POP, Operand.HL, 1);
        opcodesArray[0xE2] = new Instruction(Operation.LDH, Operand.C, Operand.A, 1);
        opcodesArray[0xE5] = new Instruction(Operation.PUSH, Operand.HL, 1);
        opcodesArray[0xE6] = new Instruction(Operation.AND, Operand.n8, 2);
        opcodesArray[0xE7] = new Instruction(Operation.RST, Operand.RST20, 1);
        opcodesArray[0xE8] = new Instruction(Operation.ADD, Operand.SP, Operand.e8, 2);
        opcodesArray[0xE9] = new Instruction(Operation.JP, Operand.HL, 1);
        opcodesArray[0xEA] = new Instruction(Operation.LD, Operand.a16, Operand.A, 3);
        opcodesArray[0xEE] = new Instruction(Operation.XOR, Operand.n8, 2);
        opcodesArray[0xEF] = new Instruction(Operation.RST, Operand.RST28, 1);
        // ROW F
        opcodesArray[0xF0] = new Instruction(Operation.LDH, Operand.A, Operand.a8, 2);
        opcodesArray[0xF1] = new Instruction(Operation.POP, Operand.AF, 1);
        opcodesArray[0xF2] = new Instruction(Operation.LDH, Operand.A, Operand.C, 1);
        opcodesArray[0xF3] = new Instruction(Operation.DI, 1);
        opcodesArray[0xF5] = new Instruction(Operation.PUSH, Operand.AF, 1);
        opcodesArray[0xF6] = new Instruction(Operation.OR, Operand.n8, 2);
        opcodesArray[0xF7] = new Instruction(Operation.RST, Operand.RST30, 1);
        opcodesArray[0xF8] = new Instruction(Operation.LD16, Operand.HL, Operand.SPe8, 2);
        opcodesArray[0xF9] = new Instruction(Operation.LD16, Operand.SP, Operand.HL, 1);
        opcodesArray[0xFA] = new Instruction(Operation.LD, Operand.A, Operand.a16, 3);
        opcodesArray[0xFB] = new Instruction(Operation.EI, 1);
        opcodesArray[0xFE] = new Instruction(Operation.CP, Operand.n8, 2);
        opcodesArray[0xFF] = new Instruction(Operation.RST, Operand.RST38, 1);
        
        
    }

    public Instruction byteToInstruction(int readByte) {
        if (opcodesArray[readByte] == null){
            // System.out.println("Opcode " + Util.hexByte(readByte) + " is not implemented yet.");
            return null;
        }
        return opcodesArray[readByte];
    }
    
    public void printInstruction(Instruction instruction){
        if (instruction.operandToSet != null){
            System.out.println(instruction.operation.name() + " " + instruction.operandToSet.name() + " " + instruction.operand.name());
        }
        else if (instruction.operand != null){
            System.out.println(instruction.operation.name() + " " + instruction.operand.name());
        }
        else {
            System.out.println(instruction.operation.name());

        }
    }

}