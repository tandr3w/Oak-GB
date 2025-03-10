import javax.swing.*;
import java.awt.*;

public class PPU extends JFrame {
    Memory memory;
    int[] spriteBuffer;
    Queue backgroundFIFO; // 16 pixels max
    Queue spriteFIFO;

    // the GB stores colours as 2 bit numbers that we have to translate in to RGB colours
    int[][] colourPaletteTranslator;
    int[][][] screenData;

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
        
        int[][][] screenData = new int[144][160][3];

        setTitle("Gameboy Emulator");
        setSize(160, 144); // TODO: add support for upscaling
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // opens the display in the middle of the screen
        setFocusable(true);

        display = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // TODO: drawPixels(g);
            }
        };
        add(display);
        setVisible(true);
        requestFocus();
    }

    public void drawScanline(){
        // Draws tiles for one scanline
        boolean windowEnabled = false;
        int tileDataLocation; // location of the start of the tile data
        int displayDataLocation; // location of the start of the window or background data
        boolean signed = false;
        if (memory.getWindowEnable() == 1){
            if (memory.getWY() <= memory.getLY()){ // Check if current line is inside window region
                windowEnabled = true;
            }
        }
        if (memory.getAddressingMode() == 1){
            tileDataLocation = 0x8000;
        }
        else {
            tileDataLocation = 0x8800;
            signed = true; // Signed bytes are used as identifiers
        }

        if (windowEnabled){
            if (memory.getWindowTileMapArea() == 1){ // We are rendering window instead of BG at this pixel
                displayDataLocation = 0x9C00;
            }
            else {
                displayDataLocation = 0x9800;
            }
        }
        else {
            if (memory.getBGTileMapArea() == 1){
                displayDataLocation = 0x9C00;
            }
            else {
                displayDataLocation = 0x9800;
            }  
        }

        // memory[displayDataLocation --> displayDataLocation + 1023] holds a 32x32 grid of tile identification numbers
        // giving the tiles from left to right and then top down
        // Find which 
        int yPos = 0; // Get the Y position on the 256x256 display that we are currently drawing at
        if (!windowEnabled){
            yPos = memory.getSCY() + memory.getLY();
        }  
        else {
            yPos = memory.getLY() - memory.getWY();
        }

        int verticalTileIndex = yPos / 8;

        for (int i = 0; i<160; i++){
            int xPos; // Get x position on the 256x256 display we are currently drawing on
            if (!windowEnabled){
                xPos = i + memory.getSCY();
            }  
            else {
                xPos = i - memory.getWY();
            }
            int horizontalTileIndex = xPos / 8;
            int dataPos = displayDataLocation + horizontalTileIndex + verticalTileIndex*32; // Get index of the tile identifier in memory
            int tileIdentifier;
            if (signed){
                tileIdentifier = (int) ((byte) memory.memoryArray[dataPos]);
            }
            else {
                tileIdentifier = memory.memoryArray[dataPos];
            }

            int tileMemLocation = tileDataLocation; // Location of the start of the tile in memory
            
            if (signed){
                tileMemLocation += (tileIdentifier + 128) * 16; // Multiply by 16 because tiles are 16 bytes each
            }
            else {
                tileMemLocation += tileIdentifier * 16;
            }

            int byteInTile = (yPos % 8) * 2; // Each 2 bytes corresponds to a row in the tile
            int byte1 = memory.memoryArray[tileMemLocation + byteInTile];
            int byte2 = memory.memoryArray[tileMemLocation + byteInTile + 1];
            int xIndex = xPos % 8;
            // we need the xIndex'th byte from the left of both of the bytes
            // -(xPos-7) converts bit number from the left to bit number from the right
            int bit1 = Util.getIthBit(byte1, -(xPos-7));
            int bit2 = Util.getIthBit(byte2, -(xPos-7));
            int colorID = memory.getPaletteColor((bit2 << 1) | bit1);
            screenData[memory.getLY()][i] = colourPaletteTranslator[colorID];
        }
    }
    // public void drawPixels(Graphics g) {
    //     fetchPixels();
    //     g.setColor(Color.BLACK); // FIXME temporary
    //     g.fillRect(80, 72, 1, 1);
    // }

    // public void render() {
    //     memory.setLY(0);
    //     while (memory.getLY() <= 143) {
    //         if (memory.getLY() == memory.getLYC()) {
    //             // some sort of interupt thingy happens idk lol
    //             // FIXME
    //         }
    //         OAMScan();
    //         display.repaint(); // triggers the display object's paintComponent automatically
    //         HBank();
    //         memory.setLY(memory.getLY() + 1); // Don't have to account for overflow
    //     }
    //     while (memory.getLY() <= 153) {
    //         VBank();
    //         memory.setLY(memory.getLY() + 1);
    //     }
    // }

    // // "MODE 2"
    // public void OAMScan() {
    //     // 80 T cycles - 40 OAM entries checked
    //     spriteBuffer = new int[10];
    //     int numObjects = 0; // can't exceed 10
    //     int LY = memory.getLY();

    //     for (int addr = 0xFE00; addr <= 0xFE9F; addr += 4){
    //         // add 2 T cycles / dots
    //         // I think we need to update timings here because the LCDC.2 flag that controls object sizes is updated mid scan
    //         int y = memory.memoryArray[addr];
    //         // int x = memory.memoryArray[addr+1];
    //         int height = 8 + 8 * memory.getOBJSize();
    //         // GBEDG says to check if (x > 0), but pandocs says that PPU doesn't check x-coord for OAM scan
    //         if ((LY + 16 >= y) & (LY + 16 < y + height) & (numObjects < 10)){
    //             numObjects++;
    //             spriteBuffer[numObjects+1] = addr; // stores address of the OAM for now
    //         }
    //     }
    // }

    // // "MODE 3"
    // public void drawPixels(Graphics g) {
    //     fetchPixels();
    //     g.setColor(Color.BLACK); // FIXME temporary
    //     g.fillRect(80, 72, 1, 1);
    // }

    // public void fetchPixels() {
    //     // scroll registers are re-read on each new fetch
    //     // low 3 bits of SCX are NOT re-read (only at the beginning of scanline)


    //     int fetcherX = 0; // internal x-pos counter
    //     int fetcherY = 0;
    //     int windowLineCounter = 0; // incremented when scanline has window pixels; reset in VBlank (i'm just gonna reset it here for convenience)
    //     int tileMapAddr;
    //     // fetch tile number
    //     if ((memory.getWindowEnable() == 1) & (memory.getBGTileMapArea() == 1) & (memory.getLY() < memory.getWY())) { // scanline is outside of window
    //         tileMapAddr = 0x9C00;
    //     }
    //     else {
    //         tileMapAddr = 0x9800; // default value
    //     }

    //     // fetch tile data (low)

    //     // fetch tile data (high)

    //     // sleep (until backgroundFIFO is NOT empty)
    //     while (backgroundFIFO.length() != 0){
    //     }

    //     // push to FIFO --> only happens if backgroundFIFO is empty
    //     if (backgroundFIFO.length() == 0) {
            
    //         // push LSB then MSB if tile is flipped horizontally
    //         // otherwise, push normally
    //     }

        
    // }

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
