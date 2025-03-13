import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private Opcodes opcodes;
    private Memory memory;
    private CPU cpu;
    private PPU ppu;
    private Timer gameLoopTimer;

    public Main() {
        opcodes = new Opcodes();
        memory = new Memory();
        cpu = new CPU(opcodes, memory);
        ppu = new PPU(memory);

        Unit_Tests tests = new Unit_Tests(cpu, opcodes);
        tests.run();
        System.out.println("\nTesting Prefixed Instructions:\n");
        tests.runPrefixed();
        cpu.loadROM("ROMs/snake.gb");

        setTitle("Gameboy Emulator");
        setSize(160, 144);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(ppu);
        pack();
        ppu.requestFocus();
        setVisible(true);

        int delay = 16; // 1000 / 60 --> 16.6667
        gameLoopTimer = new Timer(delay, e -> runFrame());
        gameLoopTimer.start();
    }

    private void runFrame() {
        int MAXCYCLES = 69905;
        int t_cyclesThisFrame = 0;
        while (t_cyclesThisFrame < MAXCYCLES) {
            int cycles = cpu.execute(opcodes.byteToInstruction(memory.memoryArray[cpu.registers.pc]));
            t_cyclesThisFrame += cycles;
            cpu.doInterrupts();
            // TODO: Add CPU Timer update function
            // TODO: Add PPU timer update function
        }
        ppu.repaint();
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
}