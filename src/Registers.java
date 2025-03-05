package src;
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

    public Registers(){
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
            default:
                throw new Error("Attempted Read Of Invalid Register Operand");
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
}
