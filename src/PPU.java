// Code for this file is heavily insipred by http://www.codeslinger.co.uk/pages/projects/gameboy/graphics.html

import javax.swing.*;
import java.awt.*;

public class PPU extends JPanel {
    Memory memory;
    int[] spriteBuffer;
    Queue backgroundFIFO; // 16 pixels max
    Queue spriteFIFO;

    // the GB stores colours as 2 bit numbers that we have to translate in to RGB colours
    int[][] colourPaletteTranslator;
    int[][][] screenData;

    int remainingCycles; // Number from 0 - 456; represents T-cycles


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
        
        screenData = new int[144][160][3];
        setPreferredSize(new Dimension(160, 144));
    }



    public void drawScanlineBG(){
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
                xPos = i + memory.getSCX();
            }
            else {
                xPos = i - (memory.getWX() - 7);
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
            // 7-xPos converts bit number from the left to bit number3 from the right
            int bit1 = Util.getIthBit(byte1, 7-xPos);
            int bit2 = Util.getIthBit(byte2, 7-xPos);
            int colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.BGP_address);
            screenData[memory.getLY()][i] = colourPaletteTranslator[colorID];
        }
    }

    public void drawScanlineSprite(){
        boolean using8x16SpriteSize = (memory.getOBJSize() == 1);
        int SPRITEADDRESS = 0xFE00;

        for (int spriteNum = 0; spriteNum < 40; spriteNum++){
            int indexStart = spriteNum * 4; // Sprites are 4 bytes each
            // Get sprite attributes
            int yPos = memory.memoryArray[SPRITEADDRESS + indexStart]-16;
            int xPos = memory.memoryArray[SPRITEADDRESS + indexStart + 1]-8;
            int spriteLocation = memory.memoryArray[SPRITEADDRESS + indexStart + 2]; // Location in memory where the sprite starts
            // Bit 7: priority (0 = rendered on top, 1 = rendered below unless window/bg is white)
            // Bit 6, 5: y and x flip (mirrored)
            // Bit 4: palette number, 0 = from 0xFF48, 1 = from 0xFF49
            // // Bit 0-3: unused
            int attributes = memory.memoryArray[SPRITEADDRESS + indexStart + 3];
            boolean bgPriority = Util.getIthBit(attributes, 7) == 1;
            boolean yFlip = Util.getIthBit(attributes, 6) == 1;
            boolean xFlip = Util.getIthBit(attributes, 5) == 1;
            boolean palette = Util.getIthBit(attributes, 4) == 1;

            int currentScanline = memory.getLY();

            int ySize = 8;
            if (using8x16SpriteSize){
                ySize = 16;
            }

            if (currentScanline >= yPos && currentScanline < (yPos + ySize)){
                int spriteRow;
                // Get the index of the row of the pixels on the sprite we are printing
                // e.g. if scan line is on 10 and our sprite is on 9, print the 2nd (index 1) row of pixels
                if (yFlip){
                    spriteRow = yPos + ySize - currentScanline; // count from end of sprite
                }
                else {
                    spriteRow = currentScanline - yPos;
                }
                spriteRow *= 2; // Since each row is 2 bytes
                int dataAddress = 0x8000 + 16*spriteLocation + spriteRow;
                int byte1 = memory.memoryArray[dataAddress];
                int byte2 = memory.memoryArray[dataAddress + 1];

                for (int spriteCol=0; spriteCol<8; spriteCol++){
                    int whichBit = spriteCol;
                    if (!xFlip){
                        whichBit = 7-whichBit;
                    }
                    int bit1 = Util.getIthBit(byte1, whichBit);
                    int bit2 = Util.getIthBit(byte2, whichBit);
                    int colorID;
                    if (palette){
                        colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.BGP_address+2);
                    }
                    else {
                        colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.BGP_address+1);
                    }
                    if (colorID == 0){
                        continue; // Don't render white pixels
                    }
                    if (bgPriority & screenData[memory.getLY()][xPos+spriteCol] != colourPaletteTranslator[0]) {
                        continue; // Don't render if the background takes priority AND if the background is not white
                    }
                    
                    screenData[memory.getLY()][xPos+spriteCol] = colourPaletteTranslator[colorID];
                }
            }
        }
    }

    // called using .repaint()
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderScreen(g);
    }

    public void renderScreen(Graphics g) {
        for (int y = 0; y < 144; y++) {
            for (int x = 0; x < 160; x++) {
                int[] rgb = screenData[y][x];
                Color pixelColor = new Color(rgb[0], rgb[1], rgb[2]);
                g.setColor(pixelColor);
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    public void updateLCDStatus() {
        int status = memory.getLCDStatus();

        // if LCD is not enabled
        if (memory.getLCDEnable() == 0) {
            memory.setLY(456);
            status &= 0b11111100;
            status = Util.setBit(status, 0, true);
            return;
        }

        int LY = memory.getLY();
        int prevMode = status & 0b11;
        int mode;
        boolean reqInt = false;

        
        if (LY < 144) {
            status = Util.setBit(status, 1, true);
            status = Util.setBit(status, 0, false);

            if (remainingCycles >= 456-80) {
                mode = 2;
                reqInt = Util.getIthBit(status, 5) == 1;
            }
            else if (remainingCycles >= 456-80-172) {
                mode = 3;
            }
            else {
                mode = 0;
            }
        }

        else {
            // MODE 1 - VBLANK
            mode = 1;
            status = Util.setBit(status, 0, true);
            status = Util.setBit(status, 1, false);
            reqInt = Util.getIthBit(status, 4) == 1;
        }

        if ((mode != prevMode) & reqInt) {
            memory.requestInterrupt(1);
        }

        if (LY == memory.getLYC()) {
            status = Util.setBit(status, 2, true);
            if (Util.getIthBit(status, 6) == 1) {
                memory.requestInterrupt(1);
            }
        }
        else {
            status = Util.setBit(status, 2, false);
        }

        memory.setLCDStatus(status);
    }

    public void updateGraphics(int t_cycles) {
        updateLCDStatus();
        if (memory.getLCDEnable() == 0) {
            return;
        }
        
        remainingCycles -= t_cycles;
        int LY = memory.getLY();
        
        if (remainingCycles <= 0) {
            LY++;
            memory.setLY(LY);
            remainingCycles = 456;

            if (LY == 144) {
                memory.requestInterrupt(1);
                return;
            }
            if (LY > 153) {
                memory.setLY(0);
                return;
            }
            drawScanlineBG();
            drawScanlineSprite();
        }
    }

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

    // "MODE 0"
    public void HBank() {
        // PPU doesn't do anything in between scanlines
    }

    // gets data from the map
    // private int getTileData() {
    // }
}
