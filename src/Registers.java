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
    int sp;
    int pc;
    public Registers(){
        int a = 0x01;
        int f = 0xB0;
        int b = 0x00;
        int c = 0x13;
        int d = 0x00;
        int e = 0xD8;
        int h = 0x01;
        int l = 0x4D;
        int sp = 0xFFFE;
        int pc = 0x0100;
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
