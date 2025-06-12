import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import src.*;

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
    private int steps = 0;
    private boolean broken = false;
    private int breakpoint = 7657577;

    // private boolean initLoad = true;
    private int MAXCYCLES = 69905;
    private long CYCLESPERSECOND = MAXCYCLES * 60;
    private long cyclesThisSecond = 0;
    private int cycleNum = 0;
    // private boolean DEBUG = false;
    // private int MAXCYCLES = 456;
    // private int pressesToTrigger = (int) (float)(30f / (float)((float)MAXCYCLES / 19900f)); // Skip first 120 frames

    public Main() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        CLOCKSPEED = 4194304;
        addKeyListener(this);
        opcodes = new Opcodes();
        memory = new Memory();
        joypad = new Joypad(memory);
        cpu = new CPU(opcodes, memory);
        ppu = new PPU(memory);
        apu = new APU(memory, CLOCKSPEED);
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

        setTitle("Oak GB");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JMenuBar menuBar = new JMenuBar();
        JMenu speedBar= new JMenu("Speed");
        JMenu sizeBar = new JMenu("Size");
        JMenu audioBar = new JMenu("Audio");


        JMenuItem speed1 = new JCheckBoxMenuItem("0.5x");
        JMenuItem speed2 = new JCheckBoxMenuItem("1x", true);
        JMenuItem speed3 = new JCheckBoxMenuItem("1.5x");
        JMenuItem speed4 = new JCheckBoxMenuItem("2x");
        JMenuItem speed5 = new JCheckBoxMenuItem("3x");

        JMenuItem size1 = new JCheckBoxMenuItem("160x144");
        JMenuItem size2 = new JCheckBoxMenuItem("320x288");
        JMenuItem size3 = new JCheckBoxMenuItem("480x432", true);
        JMenuItem size4 = new JCheckBoxMenuItem("640x576");

        JMenuItem muted = new JCheckBoxMenuItem("Muted", Settings.muted);

        muted.addActionListener(e -> {
            Settings.muted = !Settings.muted;
        });

        size1.addActionListener(e -> {
            size2.setSelected(false);
            size3.setSelected(false);
            size4.setSelected(false);
            Settings.screenSize = 1;
            ppu.updateDisplay();
            pack();
        });
        size2.addActionListener(e -> {
            size1.setSelected(false);
            size3.setSelected(false);
            size4.setSelected(false);
            Settings.screenSize = 2;
            ppu.updateDisplay();
            pack();
        });
        size3.addActionListener(e -> {
            size1.setSelected(false);
            size2.setSelected(false);
            size4.setSelected(false);
            Settings.screenSize = 3;
            ppu.updateDisplay();
            pack();
        });
        size4.addActionListener(e -> {
            size1.setSelected(false);
            size2.setSelected(false);
            size3.setSelected(false);
            Settings.screenSize = 4;
            ppu.updateDisplay();
            pack();
        });


        speed1.addActionListener(e -> {
            speed2.setSelected(false);
            speed3.setSelected(false);
            speed4.setSelected(false);
            speed5.setSelected(false);
            Settings.gameSpeed = 0.5;
        });
        speed2.addActionListener(e -> {
            speed1.setSelected(false);
            speed3.setSelected(false);
            speed4.setSelected(false);
            speed5.setSelected(false);
            Settings.gameSpeed = 1;
        });
        speed3.addActionListener(e -> {
            speed1.setSelected(false);
            speed2.setSelected(false);
            speed4.setSelected(false);
            speed5.setSelected(false);
            Settings.gameSpeed = 1.5;
        });
        speed4.addActionListener(e -> {
            speed1.setSelected(false);
            speed2.setSelected(false);
            speed3.setSelected(false);
            speed5.setSelected(false);
            Settings.gameSpeed = 2;
        });
        speed5.addActionListener(e -> {
            speed1.setSelected(false);
            speed2.setSelected(false);
            speed3.setSelected(false);
            speed4.setSelected(false);
            Settings.gameSpeed = 3;
        });
        speedBar.add(speed1);
        speedBar.add(speed2);
        speedBar.add(speed3);
        speedBar.add(speed4);
        speedBar.add(speed5);
        sizeBar.add(size1);
        sizeBar.add(size2);
        sizeBar.add(size3);
        sizeBar.add(size4);
        audioBar.add(muted);

        menuBar.add(speedBar);
        menuBar.add(sizeBar);
        menuBar.add(audioBar);

        setJMenuBar(menuBar);

        ImageIcon gameboyIcon = new ImageIcon("icons/gameboy.png");
        setIconImage(gameboyIcon.getImage());
        setLocationRelativeTo(null);
        setLocation(400,150);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String fileName = "";
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "ROMs (.gb and .gbc files)", "gb", "gbc");
        chooser.setFileFilter(filter);
        // chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setCurrentDirectory(new File("ROMs"));
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getPath();
        }   
        else {
            System.exit(0);
        }


        memory.loadROM(fileName);
        memory.loadSave();
        memory.setCGBMode();

        // tilemap = new Tilemap(memory, ppu);
        // tilemapFrame = new JFrame("Tile Map");
        // tilemapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // tilemapFrame.add(tilemap);
        // tilemapFrame.setSize(512, 256);
        // tilemapFrame.setLocation(69, 69);
        // tilemapFrame.setResizable(false);
        // tilemapFrame.setVisible(true);

        int delay = 0; // 1000 / 60 --> 16.6667
        gameLoop = new Timer(delay, e -> runFrame());
        gameLoop.start();
        apu.makeSound();
        
        add(ppu);
        ppu.addKeyListener(this);
        pack();
        ppu.requestFocus();
        setVisible(true);

    }

    public void printPalettes(){
        for (int i=0; i<8; i++){

            int[] colorData = memory.getCGBPaletteColorHex(8*i, false);
            System.out.print(i + ": " + Util.hexString(colorData[0]) + " ");
            System.out.print(Util.hexString(colorData[1]) + " ");
            System.out.print(Util.hexString(colorData[2])+ " ");
            System.out.print(Util.hexString(colorData[3]));
            System.out.println();
        }
    }

    public void keyPressed(KeyEvent e){ 
        joypad.updateJoypadPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_BACK_QUOTE){
            try {
                int count = 0;
                while (true){
                    File file = new File("resources/screenshot" + count + ".png");
                    if (!file.isFile()){
                        System.out.println("Screenshot taken successfully: " + file);
                        ImageIO.write(ppu.bi.getSubimage(0, 0, 160*Settings.screenSize, 144*Settings.screenSize), "png", file);
                        break;
                    }
                    count += 1;
                }
            }
            catch (IOException error){
                error.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
            System.out.println(Util.bitString(memory.getLCDStatus()));
            System.out.println(Util.bitString(memory.getLY()));
            printInfo();
            cpu.registers.printRegisters();
            System.out.println("\n\n");
        }
        if (e.getKeyCode() == KeyEvent.VK_0) {
            steps += 1;
        }
        // if (e.getKeyCode() == KeyEvent.VK_EQUALS){
        //     MAXCYCLES += 69905/2;
        //     System.out.println("current speed: " + MAXCYCLES);
        // }
        // if (e.getKeyCode() == KeyEvent.VK_MINUS){
        //     if (MAXCYCLES - 69905/4 > 1) { // compare with 1 because of integer rounding
        //         MAXCYCLES -= 69905/4;
        //     }
        //     System.out.println("current speed: " + MAXCYCLES);
        // }
        // if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH){ // RESET
        //     MAXCYCLES = 69905;
        //     System.out.println("current speed: " + MAXCYCLES);
        // }
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
        // Print palettes
        int cyclesThisFrame = 0;
        while (cyclesThisFrame < (int) (MAXCYCLES * Settings.gameSpeed)) { // FIXME double speed probably doesnt work properly
            Instruction instruction = opcodes.byteToInstruction(memory.getMemory(cpu.registers.pc));
            if (broken && steps > 0){
                steps -= 1;
                System.out.println(Util.bitString(memory.getLCDStatus()));
                System.out.println(Util.bitString(memory.getLY()));
                printInfo();
                cpu.registers.printRegisters();
                System.out.println("\n\n");  
            }
            else if (broken && steps <= 0){
                return;
            }
            else {
                if (cpu.registers.pc == breakpoint){
                    System.out.println(Util.bitString(memory.getLCDStatus()));
                    System.out.println(Util.bitString(memory.getLY()));
                    printInfo();
                    cpu.registers.printRegisters();
                    System.out.println("\n\n");  
                    broken = true;
                    return;          
                }
            }



            int cycles = cpu.execute(instruction);
            cyclesThisFrame += cycles;
            updateTimer(cycles);
            ppu.updateGraphics(cycles);
            cpu.doInterrupts();
        }
        if ((!cpu.doubleSpeed) || (cycleNum % 2 == 1)){ // Skip every other cycle during double speed mode
            if (!Settings.muted){
                apu.tick((int) (MAXCYCLES * Settings.gameSpeed));
            }            
            ppu.repaint(); 
        }
        // tilemap.repaint();
        long endTime = System.nanoTime();
        long frameDuration = endTime - startTime;
        int newDelay = 16 - ((int) frameDuration/1000000);

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
