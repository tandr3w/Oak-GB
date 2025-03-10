public class Main {
    public static void main(String[] args){

        Opcodes opcodes = new Opcodes();
        Memory memory = new Memory();
        CPU cpu = new CPU(opcodes, memory);
        // PPU ppu = new PPU(memory);
        Unit_Tests tests = new Unit_Tests(cpu, opcodes);
        tests.run();
        System.out.println("\nTesting Prefixed Instructions:\n");
        tests.runPrefixed();

        // TODO: Main loop
        int MAXCYCLES = 69905; // processor runs 4194304 cycles/second, but screen is updating at 60FPS
        int t_cyclesThisUpdate = 0;
        // while (cyclesThisUpdate < MAXCYCLES){
            
        // }
    }
}
