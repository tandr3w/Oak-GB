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

    public int readValFromEnum(ArithmeticTarget target){
        switch (target){
            case ArithmeticTarget.A:
                return a;
            case ArithmeticTarget.B:
                return b;
            case ArithmeticTarget.C:
                return c;
            case ArithmeticTarget.D:
                return d;
            case ArithmeticTarget.E:
                return e;
            case ArithmeticTarget.H:
                return h;
            case ArithmeticTarget.L:
                return l;
            case ArithmeticTarget.BC:
                return get_bc();
            case ArithmeticTarget.DE:
                return get_de();
            case ArithmeticTarget.HL:
                return get_hl();
            default:
                return 0; // Should be unreachable
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
}
