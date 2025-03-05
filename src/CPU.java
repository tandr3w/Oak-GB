package src;
public class CPU {
    Registers registers;
    int[] memory;
    int pc;
    int sp;
    public CPU(){  
        registers = new Registers();
        memory = new int[0xFFFF]; // 65536 bytes
    }
}
