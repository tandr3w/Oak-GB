public class Registers {
    int a; // 8-bit integers
    int b;
    int c;
    int d;
    int e;
    int f;
    int h;
    int l;
    // sp & pc are both registers
    int sp;
    int pc;
    CPU cpu;

    public Registers(CPU cpu){
        a = 0x01;
        f = 0xB0;
        b = 0x00;
        c = 0x13;
        d = 0x00;
        e = 0xD8;
        h = 0x01;
        l = 0x4D;
        sp = 0xFFFE;
        pc = 0x0100;
        this.cpu = cpu;
    }

    public int readValFromEnum(Operand operand){
        switch (operand){
            case Operand.A:
                return a;
            case Operand.B:
                return b;
            case Operand.C:
                return c;
            case Operand.D:
                return d;
            case Operand.E:
                return e;
            case Operand.H:
                return h;
            case Operand.L:
                return l;
            case Operand.BC:
                return get_bc();
            case Operand.DE:
                return get_de();
            case Operand.HL:
                return get_hl();
            case Operand.AF:
                return get_af();
            case Operand.MemBC:
                cpu.additionalCycles += 4;
                return cpu.memory.getMemory(get_bc());
            case Operand.MemDE:
                cpu.additionalCycles += 4;
                return cpu.memory.getMemory(get_de());
            case Operand.MemHL:
                cpu.additionalCycles += 4;
                return cpu.memory.getMemory(get_hl());
            case Operand.SP:
                return sp;
            default:
                throw new Error("Attempted Read Of Invalid Register Operand: " + operand.name());
        }
    }

    public void setValToEnum(Operand operand, int val){
        switch (operand){
            case Operand.A:
                a = val;
                break;
            case Operand.B:
                b = val;
                break;
            case Operand.C:
                c = val;
                break;
            case Operand.D:
                d = val;
                break;
            case Operand.E:
                e = val;
                break;
            case Operand.H:
                h = val;
                break;
            case Operand.L:
                l = val;
                break;
            case Operand.BC:
                set_bc(val);
                break;
            case Operand.DE:
                set_de(val);
                break;
            case Operand.HL:
                set_hl(val);
                break;
            case Operand.AF:
                set_af(val);
                break;
            case Operand.SP:
                sp = val;
                break;
            case Operand.MemBC:
                cpu.memory.setMemory(get_bc(), val);
                cpu.additionalCycles += 4;
                break;
            case Operand.MemDE:
                cpu.memory.setMemory(get_de(), val);
                cpu.additionalCycles += 4;
                break;
            case Operand.MemHL:
                cpu.memory.setMemory(get_hl(), val);
                cpu.additionalCycles += 4;
                break;
            default:
                throw new Error("Attempted Read Of Invalid Register Operand: " + operand.name());
        }
    }

    public int get_af(){
        return (a << 0x8) | f;
    }
    public void set_af(int val){
        a = ((val & 0xFF00) >> 0x8); // Set a to the first byte of val
        f = (val & 0xFF); // Set f to the second byte of val
    }
    public int get_bc(){
        return (b << 0x8) | c;
    }
    public void set_bc(int val){
        b = ((val & 0xFF00) >> 8); // Set b to the first byte of val
        c = (val & 0xFF); // Set c to the second byte of val
    }
    public int get_de(){
        return (d << 8) | e;
    }
    public void set_de(int val){
        d = ((val & 0xFF00) >> 8); // Set d to the first bytes of val
        e = (val & 0xFF); // Set e to the second byte of val
    }
    public int get_hl(){
        return (h << 8) | l;
    }
    public void set_hl(int val){
        h = ((val & 0xFF00) >> 8); // Set h to the first bytes of val
        l = (val & 0xFF); // Set l to the second byte of val
    }


    public void set_f_zero(boolean val){
        if (val){
            f = f | 0b10000000;
        }
        else {
            f = f & 0b01111111;
        }    }

    public int get_f_zero(){
        return (f >> 7) & 1;
    }
    public int get_f_subtract(){
        return (f >> 6) & 1;
    }
    public int get_f_halfcarry(){
        return (f >> 5) & 1;
    }
    public int get_f_carry(){
        return (f >> 4) & 1;
    }

    public void set_f_subtract(boolean val){
        if (val){
            f = f | 0b01000000;
        }
        else {
            f = f & 0b10111111;
        }    }
    public void set_f_carry(boolean val){
        if (val){
            f = f | 0b00010000;
        }
        else {
            f = f & 0b11101111;
        }    }
    public void set_f_halfcarry(boolean val){
        if (val){
            f = f | 0b00100000;
        }
        else {
            f = f & 0b11011111;
        }
    }

    public void printRegisters(){
        System.out.print("A: " + Util.hexString(a) + ", ");
        System.out.print("B: " + Util.hexString(b) + ", ");
        System.out.print("C: " + Util.hexString(c) + ", ");
        System.out.print("D: " + Util.hexString(d) + ", ");
        System.out.print("E: " + Util.hexString(e) + ", ");
        System.out.print("F: " + Util.hexString(f) + ", ");
        System.out.print("H: " + Util.hexString(h) + ", ");
        System.out.print("L: " + Util.hexString(l) + ", ");
        System.out.print("SP: " + Util.hexString(sp) + ", ");
        System.out.println("PC: " + Util.hexString(pc));

    }
}
