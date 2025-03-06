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
            try{
                if (opcodes.byteToInstruction(i) != null){
                    cpu.execute(opcodes.byteToInstruction(i));
                    System.out.println("Testing passed on: " + Util.hexByte(i));
                }
            }
            catch(Exception e){
                System.out.println("Testing failed on: " + Util.hexByte(i));
                e.printStackTrace();
            }
        }
        return 1;
    }
}
