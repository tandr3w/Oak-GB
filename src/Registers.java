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
    public Registers(){

    }
    public int get_af(){
        return (a << 8) | f;
    }
    public void set_af(int val){
        a = ((val & 0xFF00) >> 8); // Set b to the first bytes of val
        f = (val & 0xFF); // Set c to the second byte of val
    }
    public int get_bc(){
        return (b << 8) | c;
    }
    public void set_bc(int val){
        b = ((val & 0xFF00) >> 8); // Set b to the first bytes of val
        c = (val & 0xFF); // Set c to the second byte of val
    }
    public int get_de(){
        return (d << 8) | e;
    }
    public void set_de(int val){
        d = ((val & 0xFF00) >> 8); // Set b to the first bytes of val
        e = (val & 0xFF); // Set c to the second byte of val
    }
    public int get_hl(){
        return (h << 8) | l;
    }
    public void set_hl(int val){
        h = ((val & 0xFF00) >> 8); // Set b to the first bytes of val
        l = (val & 0xFF); // Set c to the second byte of val
    }
}
