import javax.swing.*;
import java.awt.*;

public class PPU extends JFrame {
    Memory memory;
    int[] spriteBuffer;
    Queue backgroundFIFO; // 16 pixels max
    Queue spriteFIFO;

    // the GB stores colours as 2 bit numbers that we have to translate in to RGB colours
    int[][] colourPaletteTranslator;
    private JPanel display;

    public PPU(Memory memory) {

        this.memory = memory;
        backgroundFIFO = new Queue(16);
        spriteFIFO = new Queue(16);
        colourPaletteTranslator = new int[][] {
            {255, 255, 255}, // 0b00
            {192, 192, 192}, // 0b01
            {96, 96, 96}, // 0b10
            {0, 0, 0} // 0b11
        };
        
        setTitle("Gameboy Emulator");
        setSize(160, 144); // TODO: add support for upscaling
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // opens the display in the middle of the screen
        setFocusable(true);

        display = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPixels(g);
            }
        };
        add(display);
        setVisible(true);
        requestFocus();
    }

    public void render() {
        memory.setLY(0);
        while (memory.getLY() <= 143) {
            if (memory.getLY() == memory.getLYC()) {
                // some sort of interupt thingy happens idk lol
                // FIXME
            }
            OAMScan();
            display.repaint(); // triggers the display object's paintComponent automatically
            HBank();
            memory.setLY(memory.getLY() + 1); // Don't have to account for overflow
        }
        while (memory.getLY() <= 153) {
            VBank();
            memory.setLY(memory.getLY() + 1);
        }
    }

    // "MODE 2"
    public void OAMScan() {
        // 80 T cycles - 40 OAM entries checked
        spriteBuffer = new int[10];
        int numObjects = 0; // can't exceed 10
        int LY = memory.getLY();

        for (int addr = 0xFE00; addr <= 0xFE9F; addr += 4){
            // add 2 T cycles / dots
            // I think we need to update timings here because the LCDC.2 flag that controls object sizes is updated mid scan
            int y = memory.memoryArray[addr];
            // int x = memory.memoryArray[addr+1];
            int height = 8 + 8 * memory.getOBJSize();
            // GBEDG says to check if (x > 0), but pandocs says that PPU doesn't check x-coord for OAM scan
            if ((LY + 16 >= y) & (LY + 16 < y + height) & (numObjects < 10)){
                numObjects++;
                spriteBuffer[numObjects+1] = addr; // stores address of the OAM for now
            }
        }
    }

    // "MODE 3"
    public void drawPixels(Graphics g) {
        fetchPixels();
        g.setColor(Color.BLACK); // FIXME temporary
        g.fillRect(80, 72, 1, 1);
    }

    public void fetchPixels() {
        // scroll registers are re-read on each new fetch
        // low 3 bits of SCX are NOT re-read (only at the beginning of scanline)


        int fetcherX = 0; // internal x-pos counter
        int fetcherY = 0;
        int windowLineCounter = 0; // incremented when scanline has window pixels; reset in VBlank (i'm just gonna reset it here for convenience)
        int tileMapAddr;
        // fetch tile number
        if ((memory.getWindowEnable() == 1) & (memory.getBGTileMapArea() == 1) & (memory.getLY() < memory.getWY())) { // scanline is outside of window
            tileMapAddr = 0x9C00;
        }
        else {
            tileMapAddr = 0x9800; // default value
        }

        // fetch tile data (low)

        // fetch tile data (high)

        // sleep (until backgroundFIFO is NOT empty)
        while (backgroundFIFO.length() != 0){
        }

        // push to FIFO --> only happens if backgroundFIFO is empty
        if (backgroundFIFO.length() == 0) {
            
            // push LSB then MSB if tile is flipped horizontally
            // otherwise, push normally
        }

        
    }

    // "MODE 0"
    public void HBank() {
        // PPU doesn't do anything in between scanlines
    }

    // "MODE 1"
    public void VBank() {
        // PPU doesn't do anything in between screens
    }

    // gets data from the map
    // private int getTileData() {
    // }
}
