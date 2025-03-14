import javax.swing.*;

public class Main extends JFrame {
    private Opcodes opcodes;
    private Memory memory;
    private CPU cpu;
    private PPU ppu;
    private Timer gameLoop;

    private int[] TMAFrequencies;

    public Main() {
        opcodes = new Opcodes();
        memory = new Memory();
        cpu = new CPU(opcodes, memory);
        ppu = new PPU(memory);
        
        TMAFrequencies = new int[] {
            4096,
            262144,
            65536,
            16384,
        };

        // Unit_Tests tests = new Unit_Tests(cpu, opcodes);
        // tests.run();
        // System.out.println("\nTesting Prefixed Instructions:\n");
        // tests.runPrefixed();
        
        // https://github.com/mattcurrie/dmg-acid2
        // memory.loadROM("ROMs/dmg-acid2.gb"); // graphics testing ROM
        memory.loadROM("ROMs/snake.gb");

        setTitle("Gameboy Emulator");
        setSize(160, 144);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(ppu);
        pack();
        ppu.requestFocus();
        setVisible(true);

        int delay = 1000; // 1000 / 60 --> 16.6667
        // FIXME: CHANGE DELAY LATER
        gameLoop = new Timer(delay, e -> runFrame());
        gameLoop.start();
    }

    private void runFrame() {
        int MAXCYCLES = 69905;
        int t_cyclesThisFrame = 0;
        while (t_cyclesThisFrame < MAXCYCLES) {
            int cycles = cpu.execute(opcodes.byteToInstruction(memory.memoryArray[cpu.registers.pc]));
            t_cyclesThisFrame += cycles;
            
            // TODO: Add Timer update function
            ppu.updateGraphics(cycles);
            cpu.doInterrupts();
        }
        ppu.repaint();
    }

    // TODO: implement timer update function
    public void updateTimers(int cycles) {
        // doDividerRegisters(cycles);

        if (memory.isClockEnabled()) {
            
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    // same as
    
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(new Runnable() {
    //         @Override
    //         public void run() {
    //             new Main();
    //         }
    //     });
    // }


    /*
    
    move this somewhere later
    TMAFrequencies = new int[] {
        4096,
        262144,
        65536,
        16384,
    };

    */
}