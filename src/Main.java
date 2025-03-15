import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements KeyListener {
    private Opcodes opcodes;
    private Memory memory;
    private CPU cpu;
    private PPU ppu;

    private Tilemap tilemap;
    private JFrame tilemapFrame;


    private Timer gameLoop;
    private int[] TMAFrequencies;
    private final int CLOCKSPEED;
    private int timerCounter;
    private int dividerCounter;

    private int pressesToTrigger = 0;

    public Main() {
        addKeyListener(this);
        opcodes = new Opcodes();
        memory = new Memory();
        cpu = new CPU(opcodes, memory);
        ppu = new PPU(memory);
        tilemap = new Tilemap(memory, ppu);
        
        TMAFrequencies = new int[] {
            4096,
            262144,
            65536,
            16384,
        };
        CLOCKSPEED = 4194304;
        updateTimerCounter(); // Initializes the value for timerCounter

        // Divider register is similar to timer
        // counts from 0 - 255
        // an interrupt is NOT called upon overflow (unlike the timer)
        // cannot be paused
        dividerCounter = 0;

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
        ppu.addKeyListener(this);
        pack();
        ppu.requestFocus();
        setVisible(true);

        // Create and display the tile map window

        tilemap = new Tilemap(memory, ppu);
        tilemapFrame = new JFrame("Tile Map");
        tilemapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tilemapFrame.add(tilemap);
        tilemapFrame.setSize(512, 256);
        tilemapFrame.setLocation(69, 69);
        tilemapFrame.setResizable(false);
        tilemapFrame.setVisible(true);

        int delay = 16; // 1000 / 60 --> 16.6667
        gameLoop = new Timer(delay, e -> runFrame());
        gameLoop.start();
    }

    public void keyPressed(KeyEvent e){
        System.out.println("KEY PRESSED");
        pressesToTrigger += 1;
    }

    public void keyReleased(KeyEvent e){
        System.out.println("KEY Released");

    }

    public void keyTyped(KeyEvent e){

    }

    private void runFrame() {
        if (pressesToTrigger > 0){
            int MAXCYCLES = 69905;
            int cyclesThisFrame = 0;
            while (cyclesThisFrame < MAXCYCLES) {
                int cycles = cpu.execute(opcodes.byteToInstruction(memory.memoryArray[cpu.registers.pc]));
                cyclesThisFrame += cycles;
                updateTimer(cycles);
                ppu.updateGraphics(cycles);
                cpu.doInterrupts();
                tilemap.repaint();
            }
            ppu.repaint();
            pressesToTrigger -= 1;
        }

    }


    // TODO: Add changes to CPU so that the DIV register resets everytime it is written to / when STOP is called
    public void doDividerRegisters(int cycles) {
        dividerCounter += cycles;
        // At a freq of 16384, the divider register increments once every 256 cycles
        if (dividerCounter >= 255) {
            dividerCounter = 0;
            memory.setDIV((memory.getDIV() + 1) & 0xFF); // handle overflow
        }
    }

    public void updateTimerCounter() {
        int freqID = memory.getTMC() & 0b11;
        timerCounter = CLOCKSPEED / TMAFrequencies[freqID];
    }

    // TODO: implement timer update function
    public void updateTimer(int cycles) {
        doDividerRegisters(cycles);

        if (memory.isClockEnabled()) {
            timerCounter -= cycles;
            if (timerCounter <= 0) {
                updateTimerCounter();

                // when TIMA overflows, an interrupt is requested, and TIMA is set to TMA
                if (memory.getTIMA() == 255) {
                    memory.setTIMA(memory.getTMA());
                    memory.requestInterrupt(2);
                }
                return;
            }
            memory.setTIMA((memory.getTIMA() + 1) & 0xFF);
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