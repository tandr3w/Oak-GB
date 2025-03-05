package src;
public class Main {
    public static void main(String[] args){
        Registers registers = new Registers();
        // CPU cpu = new CPU();
        registers.b = 0xFF;
        registers.c = 0xAF;
        printHex(registers.get_bc());

    }
    public static void printHex(int val){
        String hex = Integer.toHexString(val);
        System.out.println(hex);
    }
}
