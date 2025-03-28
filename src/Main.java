import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.LongToIntFunction;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;;



public class Main extends JFrame implements KeyListener {
    private Opcodes opcodes;
    private Memory memory;
    private CPU cpu;
    private PPU ppu;
    private Joypad joypad;
    private APU apu;

    // private Tilemap tilemap;
    // private JFrame tilemapFrame;

    private Timer gameLoop;
    private int[] TMAFrequencies;
    private int CLOCKSPEED;
    private int timerCounter;
    private int dividerCounter;

    // private boolean initLoad = true;
    private int MAXCYCLES = 69905;
    private long CYCLESPERSECOND = MAXCYCLES * 60;
    private long cyclesThisSecond = 0;
    private int cycleNum = 0;
    // private boolean DEBUG = false;
    // private int MAXCYCLES = 456;
    // private int pressesToTrigger = (int) (float)(30f / (float)((float)MAXCYCLES / 19900f)); // Skip first 120 frames

    public Main() {
        CLOCKSPEED = 4194304;
        addKeyListener(this);
        opcodes = new Opcodes();
        memory = new Memory();
        joypad = new Joypad(memory);
        cpu = new CPU(opcodes, memory);
        ppu = new PPU(memory);
        // apu = new APU(memory, CLOCKSPEED);
        memory.cpu = cpu;
        // tilemap = new Tilemap(memory, ppu);
        
        TMAFrequencies = new int[] {
            4096,
            262144,
            65536,
            16384,
        };
        updateTimerCounter(); // Initializes the value for timerCounter
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                memory.saveOnClose();
            }
        });

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
        memory.loadROM("ROMs/PokemonCrystal.gbc");
        
        memory.loadSave();
        // memory.loadROM("ROMs/mooneye-wario-suite/acceptance/bits/unused_hwio-GS.gb"); // Failed
        // memory.loadROM("ROMs/blargg-test-roms/cpu_instrs/cpu_instrs.gb"); // Passed
        // memory.loadROM("ROMs/mooneye-test-suite/emulator-only/mbc1/bits_mode.gb");
        // memory.loadROM("ROMs/blargg-test-roms/interrupt_time/interrupt_time.gb");

        if (memory.CGBMode == true){
            // CLOCKSPEED *= 2;
            // MAXCYCLES *= 2;
            // CYCLESPERSECOND = MAXCYCLES * 60;
            cpu.registers.a = 0x11;   
        }

        setTitle("Gameboy Emulator");
        ImageIcon gameboyIcon = new ImageIcon("icons/gameboy.png");
        setIconImage(gameboyIcon.getImage());
        setLocationRelativeTo(null);
        setLocation(400,150);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(ppu);
        ppu.addKeyListener(this);
        pack();
        ppu.requestFocus();
        setVisible(true);

        // tilemap = new Tilemap(memory, ppu);
        // tilemapFrame = new JFrame("Tile Map");
        // tilemapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // tilemapFrame.add(tilemap);
        // tilemapFrame.setSize(512, 256);
        // tilemapFrame.setLocation(69, 69);
        // tilemapFrame.setResizable(false);
        // tilemapFrame.setVisible(true);
        int delay = 16; // 1000 / 60 --> 16.6667
        gameLoop = new Timer(delay, e -> runFrame());
        gameLoop.start();
        // apu.makeSound();

    }

    public void keyPressed(KeyEvent e){
        joypad.updateJoypadPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_EQUALS){
            MAXCYCLES += 69905/2;
            System.out.println("current speed: " + MAXCYCLES);
        }
        if (e.getKeyCode() == KeyEvent.VK_MINUS){
            if (MAXCYCLES - 69905/2 > 1) { // compare with 1 because of integer rounding
                MAXCYCLES -= 69905/2;
            }
            System.out.println("current speed: " + MAXCYCLES);
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH){ // RESET
            MAXCYCLES = 69905;
            System.out.println("current speed: " + MAXCYCLES);
        }
        // pressesToTrigger += 1;
    }

    public void keyReleased(KeyEvent e){
        // System.out.println("KEY Released");
        joypad.updateJoypadReleased(e);
    }

    public void keyTyped(KeyEvent e){
    }

    public void printInfo(){
        System.out.println("IME: " + cpu.interrupts + " FF40: " + Util.hexString(memory.getMemory(0xFF40)) + " FF41: " + Util.hexString(memory.getMemory(0xFF41)) + " FF0F: " + Util.hexString(memory.getMemory(0xFF0F)) + " FFFF: " + Util.hexString(memory.getMemory(0xFFFF)));
    }

    private void runFrame() {
        cycleNum += 1;
        long startTime = System.nanoTime();
        // System.out.println("LCDC: " + Util.bitString(memory.getMemory(memory.LCDC_address)));
        // System.out.println("SCX: " + Util.bitString(memory.getMemory(memory.SCX_address)));
        int cyclesThisFrame = 0;
        while (cyclesThisFrame < MAXCYCLES) { // FIXME double speed probably doesnt work properly
            Instruction instruction = opcodes.byteToInstruction(memory.getMemory(cpu.registers.pc));
            int cycles = cpu.execute(instruction);
            cyclesThisFrame += cycles;
            updateTimer(cycles);
            ppu.updateGraphics(cycles);
            cpu.doInterrupts();
        }
        if ((!cpu.doubleSpeed) || (cycleNum % 2 == 1)){ // Skip every other cycle during double speed mode
            // apu.tick(MAXCYCLES);
            ppu.repaint(); 
        }
        // tilemap.repaint();
        long endTime = System.nanoTime();
        long frameDuration = endTime - startTime;
        int newDelay = 15 - ((int) frameDuration/1000000);
        if (cpu.doubleSpeed){
            newDelay /= 2;
        }
        if (newDelay > 0){
            gameLoop.setDelay(newDelay);
        }
        else {
            gameLoop.setDelay(0);
        }


        cyclesThisSecond += MAXCYCLES;
        if (cyclesThisSecond >= CYCLESPERSECOND) {
            cyclesThisSecond = 0;
            memory.updateRTC(1);
        }
    }


    // TODO: Add changes to CPU so that the DIV register resets everytime it is written to / when STOP is called
    public void doDividerRegisters(int cycles) {
        dividerCounter += cycles;
        // At a freq of 16384, the divider register increments once every 256 cycles
        if (dividerCounter >= 256) {
            dividerCounter -= 256;
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
                if (memory.getTIMA() > 255) {
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
}