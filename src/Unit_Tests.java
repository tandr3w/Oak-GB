package src;

public class Unit_Tests {
    public CPU cpu;
    public Opcodes opcodes;

    public Unit_Tests(){
        cpu = new CPU();
        opcodes = new Opcodes();
    }

    public int run(){
        // Test add operation
        // Test all opcodes do not error
        for (int i=0; i<0x100; i++){
            System.out.println("Testing: " + Integer.toString(i));
            cpu.execute(opcodes.byteToInstruction(i));
        }
        return 1;
    }
}
