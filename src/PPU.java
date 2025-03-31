package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class PPU extends JPanel {
    Memory memory;
    int[] spriteBuffer;
    // Queue backgroundFIFO; // 16 pixels max
    // Queue spriteFIFO;
    // the GB stores colours as 2 bit numbers that we have to translate in to RGB colours
    int[][] colourPaletteTranslator;
    int[][] greenColourPalette;
    boolean green = false; // chooses which palette to use
    int[][][] screenData;
    int[][] bgPriorities;
    int[][] bgColorIDs;
    int prevLY = -1;
    int prevLYC = -1;
    int internalWindowCounter = 0;

    int remainingCycles; // Number from 0 - 456; represents T-cycles
    int upscalingFactor = 3;
    BufferedImage bi;
    ImageIcon icon;

    public PPU(Memory memory) {
        bi = new BufferedImage(160*upscalingFactor, 144*upscalingFactor, BufferedImage.TYPE_INT_RGB);
        icon = new ImageIcon(bi);
        add(new JLabel(icon));
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.memory = memory;
        remainingCycles = 456;
        colourPaletteTranslator = new int[][] {
            {255, 255, 255}, // 0b00
            {170, 170, 170}, // 0b01
            {85, 85, 85}, // 0b10
            {0, 0, 0} // 0b11
        };
        greenColourPalette = new int[][] {
            {181, 175, 66},
            {145, 155, 58},
            {93, 120, 46},
            {58, 81, 34},
        };
        if (green) {
            colourPaletteTranslator = greenColourPalette;
        }
        screenData = new int[144][160][3];
        bgPriorities = new int[144][160];
        bgColorIDs = new int[144][160];

        setPreferredSize(new Dimension(160*upscalingFactor, 144*upscalingFactor));
    }

    public void drawScanlineBG(){
        // Draws tiles for one scanline
        int windowX = memory.getWX() - 7;
        int tileDataLocation; // location of the start of the tile data
        int displayDataLocation; // location of the start of the window or background data
        boolean signed = false;
        if (memory.getAddressingMode() == 1){
            tileDataLocation = 0x8000;
        }
        else {
            tileDataLocation = 0x8800;
            signed = true; // Signed bytes are used as identifiers
        }

        boolean windowEnabled = false;
        if (memory.getWindowEnable() == 1){
            if (memory.getWY() <= memory.getLY()){ // Check if current line is inside window region
                windowEnabled = true;
            }
        }

        boolean hadWindow = false;
        // memory[displayDataLocation --> displayDataLocation + 1023] holds a 32x32 grid of tile identification numbers
        // giving the tiles from left to right and then top down
        // Find which
        int yPos; // Get the Y position on the 256x256 display that we are currently drawing at
        for (int i = 0; i<160; i++){
            int xPos = (i + memory.getSCX()) % 256; // Get x position on the 256x256 display we are currently drawing on
            if (windowEnabled && i >= windowX){
                hadWindow = true;
                yPos = internalWindowCounter % 256;
                xPos = (i - windowX) % 256;
                if (memory.getWindowTileMapArea() == 1){ // We are rendering window instead of BG at this pixel
                    displayDataLocation = 0x9C00;
                }
                else {
                    displayDataLocation = 0x9800;
                }
            }
            else {
                yPos = (memory.getSCY() + memory.getLY()) % 256;
                if (memory.getBGTileMapArea() == 1){
                    displayDataLocation = 0x9C00;
                }
                else {
                    displayDataLocation = 0x9800;
                }
            }


            int verticalTileIndex = yPos / 8;
            int horizontalTileIndex = xPos / 8;
            int dataPos = displayDataLocation + horizontalTileIndex + verticalTileIndex*32;

            short tileIdentifier;

            if (signed) {
                tileIdentifier = (short) ((byte) memory.getMemory(dataPos));
            } else {
                tileIdentifier = (short) memory.getMemory(dataPos);
            }

            int tileMemLocation = tileDataLocation;
            if (signed) {
                tileMemLocation += (tileIdentifier + 128) * 16; // Each tile is 16 bytes
            } else {
                tileMemLocation += tileIdentifier * 16;
            }

            int byteInTile = (yPos % 8) * 2; // Each 2 bytes corresponds to a row in the tile
            int byte1 = memory.getMemory(tileMemLocation + byteInTile);
            int byte2 = memory.getMemory(tileMemLocation + byteInTile + 1);
            int xIndex = xPos % 8;
            // we need the xIndex'th byte from the left of both of the bytes
            // 7-xPos converts bit number from the left to bit number from the right
            int bit1 = Util.getIthBit(byte1, 7-xIndex);
            int bit2 = Util.getIthBit(byte2, 7-xIndex);

            int colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.BGP_address);
            screenData[memory.getLY()][i] = colourPaletteTranslator[colorID];
        }
        
        if (hadWindow){
            internalWindowCounter++;
        }
    }

    public void CGB_drawScanlineBG(){
        // Draws tiles for one scanline
        int windowX = memory.getWX() - 7;
        int tileDataLocation; // location of the start of the tile data
        int displayDataLocation; // location of the start of the window or background data
        boolean signed = false;
        if (memory.getAddressingMode() == 1){
            tileDataLocation = 0x8000;
        }
        else {
            tileDataLocation = 0x8800;
            signed = true; // Signed bytes are used as identifiers
        }

        boolean windowEnabled = false;
        if (memory.getWindowEnable() == 1){
            if (memory.getWY() <= memory.getLY()){ // Check if current line is inside window region
                windowEnabled = true;
            }
        }

        boolean hadWindow = false;
        // memory[displayDataLocation --> displayDataLocation + 1023] holds a 32x32 grid of tile identification numbers
        // giving the tiles from left to right and then top down
        // Find which
        int yPos; // Get the Y position on the 256x256 display that we are currently drawing at
        for (int i = 0; i<160; i++){
            int xPos = (i + memory.getSCX()) % 256; // Get x position on the 256x256 display we are currently drawing on
            if (windowEnabled && i >= windowX){
                hadWindow = true;
                yPos = internalWindowCounter % 256;
                xPos = (i - windowX) % 256;
                if (memory.getWindowTileMapArea() == 1){ // We are rendering window instead of BG at this pixel
                    displayDataLocation = 0x9C00;
                }
                else {
                    displayDataLocation = 0x9800;
                }
            }
            else {
                yPos = (memory.getSCY() + memory.getLY()) % 256;
                if (memory.getBGTileMapArea() == 1){
                    displayDataLocation = 0x9C00;
                }
                else {
                    displayDataLocation = 0x9800;
                }
            }

            int verticalTileIndex = yPos / 8;
            int horizontalTileIndex = xPos / 8;
            int dataPos = displayDataLocation + horizontalTileIndex + verticalTileIndex*32;

            int attributes = memory.getVRAM(1, dataPos);
            int bank = Util.getIthBit(attributes, 3);
            int yFlip = Util.getIthBit(attributes, 6);
            int xFlip = Util.getIthBit(attributes, 5);
            int priority = Util.getIthBit(attributes, 7);;

            short tileIdentifier;
            
            if (signed) {
                tileIdentifier = (short) ((byte) memory.getVRAM(0, dataPos));
            } else {
                tileIdentifier = (short) memory.getVRAM(0, dataPos);
            }

            int tileMemLocation = tileDataLocation;
            if (signed) {
                tileMemLocation += (tileIdentifier + 128) * 16; // Each tile is 16 bytes
            } else {
                tileMemLocation += tileIdentifier * 16;
            }

            int byteInTile;
            if (yFlip == 0){
                byteInTile = (yPos % 8) * 2; // Each 2 bytes corresponds to a row in the tile
            }
            else {
                byteInTile = (7-(yPos % 8)) * 2; // Each 2 bytes corresponds to a row in the tile
            }
            int byte1 = memory.getVRAM(bank, tileMemLocation + byteInTile);
            int byte2 = memory.getVRAM(bank, tileMemLocation + byteInTile+1);
            int xIndex = xPos % 8;
            if (xFlip == 1){
                xIndex = 7 - xIndex;
            }
            // we need the xIndex'th byte from the left of both of the bytes
            // 7-xPos converts bit number from the left to bit number from the right
            int bit1;
            int bit2;
            bit1 = Util.getIthBit(byte1, 7-xIndex);
            bit2 = Util.getIthBit(byte2, 7-xIndex);
            int paletteID = (bit2 << 1) | bit1;
            // 2 bytes per color, 4 colors per BGP
            int bgpStartAddress = (attributes & 0b111) * 2 * 4;
            int[][] colorData = memory.getCGBPaletteColor(bgpStartAddress, false);

            screenData[memory.getLY()][i] = colorData[paletteID]; 
            bgPriorities[memory.getLY()][i] = priority;
            bgColorIDs[memory.getLY()][i] = paletteID;

        }
        if (hadWindow){
            internalWindowCounter++;
        }
    }

    public void drawScanlineSprite(){
        
        int objectsSoFar = 0;
        int SPRITEADDRESS = 0xFE00;
        int[] minxPosAtPos = new int[160];
        for (int i=0; i<160; i++){
            minxPosAtPos[i] = 1000;
        }

        for (int spriteNum = 0; spriteNum < 40; spriteNum++){
            
            int indexStart = spriteNum * 4; // Sprites are 4 bytes each
            // Get sprite attributes
            int yPos = memory.getMemory(SPRITEADDRESS + indexStart)-16;
            int xPos = memory.getMemory(SPRITEADDRESS + indexStart + 1)-8;
            int spriteLocation = memory.getMemory(SPRITEADDRESS + indexStart + 2); // Location in memory where the sprite starts
            // Bit 7: priority (0 = rendered on top, 1 = rendered below unless window/bg is white)
            // Bit 6, 5: y and x flip (mirrored)
            // Bit 4: palette number, 0 = from 0xFF48, 1 = from 0xFF49
            // // Bit 0-3: unused

            int attributes = memory.getMemory(SPRITEADDRESS + indexStart + 3);
            boolean bgPriority = Util.getIthBit(attributes, 7) == 1;
            boolean yFlip = Util.getIthBit(attributes, 6) == 1;
            boolean xFlip = Util.getIthBit(attributes, 5) == 1;
            boolean palette = Util.getIthBit(attributes, 4) == 1;

            int currentScanline = memory.getLY();
            int ySize = 8 + 8*memory.getOBJSize();

            if ((currentScanline >= yPos) && (currentScanline < (yPos + ySize))){
                objectsSoFar += 1;
                int spriteRow;
                // Get the index of the row of the pixels on the sprite we are printing
                // e.g. if scan line is on 10 and our sprite is on 9, print the 2nd (index 1) row of pixels
                if (yFlip){
                    spriteRow = yPos + ySize - 1 - currentScanline; // count from end of sprite
                }
                else {
                    spriteRow = currentScanline - yPos;
                }
                spriteRow *= 2; // Since each row is 2 bytes
                int dataAddress = 0x8000 + 16*spriteLocation + spriteRow;
                if (ySize == 16){
                    dataAddress = 0x8000 + 16*Util.setBit(spriteLocation, 0, false) + spriteRow;
                }
                int byte1 = memory.getMemory(dataAddress);
                int byte2 = memory.getMemory(dataAddress + 1);
                for (int spriteCol=0; spriteCol<8; spriteCol++){
                    int whichBit = spriteCol;
                    if (!xFlip){
                        whichBit = 7-whichBit;
                    }
                    int bit1 = Util.getIthBit(byte1, whichBit);
                    int bit2 = Util.getIthBit(byte2, whichBit);
                    int colorID;
                    int paletteID;
                    if (palette){
                        paletteID = (bit2 << 1) | bit1;
                        colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.OBP_address+1); // accesses OBP instead of BGP
                    }
                    else {
                        paletteID = (bit2 << 1) | bit1;
                        colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.OBP_address);
                    }

                    if (paletteID == 0){
                        continue; // Don't render white pixels
                    }
                    if (xPos + spriteCol < 0 || xPos + spriteCol >= 160){
                        continue;
                    }
                    if (bgPriority && (screenData[memory.getLY()][xPos+spriteCol] != colourPaletteTranslator[0])) {
                        continue; // Don't render if the background takes priority AND if the background is not white
                    }
                    if (objectsSoFar > 10){
                        continue;
                    }
                    if (xPos < minxPosAtPos[xPos+spriteCol]) {
                        screenData[memory.getLY()][xPos+spriteCol] = colourPaletteTranslator[colorID];
                        minxPosAtPos[xPos+spriteCol] = xPos; // the array should only be updated if the new xpos is lower
                    }
                    // minxPosAtPos[xPos+spriteCol] = xPos;
                }
            }
        }
    }

    public void CGB_drawScanlineSprite(){
        
        int objectsSoFar = 0;

        int SPRITEADDRESS = 0xFE00;
        int[] hasSprite = new int[160];

        for (int spriteNum = 0; spriteNum < 40; spriteNum++){
            
            int indexStart = spriteNum * 4; // Sprites are 4 bytes each
            // Get sprite attributes
            int yPos = memory.getMemory(SPRITEADDRESS + indexStart)-16;
            int xPos = memory.getMemory(SPRITEADDRESS + indexStart + 1)-8;
            int spriteLocation = memory.getMemory(SPRITEADDRESS + indexStart + 2); // Location in memory where the sprite starts
            // Bit 7: priority (0 = rendered on top, 1 = rendered below unless window/bg is white)
            // Bit 6, 5: y and x flip (mirrored)
            // Bit 4: palette number, 0 = from 0xFF48, 1 = from 0xFF49
            // // Bit 0-3: unused

            int attributes = memory.getMemory(SPRITEADDRESS + indexStart + 3);
            boolean bgPriority = Util.getIthBit(attributes, 7) == 1;
            boolean yFlip = Util.getIthBit(attributes, 6) == 1;
            boolean xFlip = Util.getIthBit(attributes, 5) == 1;
            int bank = Util.getIthBit(attributes, 3);

            int currentScanline = memory.getLY();
            int ySize = 8 + 8*memory.getOBJSize();


            if ((currentScanline >= yPos) && (currentScanline < (yPos + ySize))){
                objectsSoFar += 1;
                int spriteRow;
                // Get the index of the row of the pixels on the sprite we are printing
                // e.g. if scan line is on 10 and our sprite is on 9, print the 2nd (index 1) row of pixels
                if (yFlip){
                    spriteRow = yPos + ySize - 1 - currentScanline; // count from end of sprite
                }
                else {
                    spriteRow = currentScanline - yPos;
                }
                spriteRow *= 2; // Since each row is 2 bytes
                int dataAddress = 0x8000 + 16*spriteLocation + spriteRow;
                if (ySize == 16){
                    dataAddress = 0x8000 + 16*Util.setBit(spriteLocation, 0, false) + spriteRow;
                }
                int byte1 = memory.getVRAM(bank, dataAddress);
                int byte2 = memory.getVRAM(bank, dataAddress + 1);
                for (int spriteCol=0; spriteCol<8; spriteCol++){
                    int whichBit = spriteCol;
                    if (!xFlip){
                        whichBit = 7-whichBit;
                    }
                    int bit1 = Util.getIthBit(byte1, whichBit);
                    int bit2 = Util.getIthBit(byte2, whichBit);
                    int paletteID = (bit2 << 1) | bit1;

                    if (paletteID == 0){
                        continue; // Don't render white pixels
                    }

                    if (xPos + spriteCol < 0 || xPos + spriteCol >= 160){
                        continue;
                    }
                    if (hasSprite[xPos+spriteCol] == 1){
                        continue;
                    }
                    if (objectsSoFar > 10){
                        continue;
                    }
                    if (memory.getBGWindowEnable() == 1){
                        if ((bgPriority || (bgPriorities[memory.getLY()][xPos+spriteCol] == 1)) && (bgColorIDs[memory.getLY()][xPos+spriteCol] != 0)) {
                            continue; // Don't render if the background takes priority AND if the background is not white
                        }
                    }
                    // 2 bytes per color, 4 colors per BGP
                    int spritePaletteStartAddress = (attributes & 0b111) * 2 * 4;
                    int[][] colorData = memory.getCGBPaletteColor(spritePaletteStartAddress, true);
                    screenData[memory.getLY()][xPos+spriteCol] = colorData[paletteID];  
                    hasSprite[xPos+spriteCol] = 1;                  
                }
            }
        }
    }

    // called using .repaint()
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderScreen(g);
    }

    public void drawScanline() {
        if (memory.CGBMode){
            CGB_drawScanlineBG();
        }
        else if (memory.getBGWindowEnable() == 1) {
            drawScanlineBG();
        }
        
        if (memory.getOBJEnable() == 1) {
            if (memory.CGBMode){
                CGB_drawScanlineSprite();
            }
            else {
                drawScanlineSprite();

            }
        }
    }

    public void renderScreen(Graphics g) {
        for (int y = 0; y < 144; y++) {
            for (int x = 0; x < 160; x++) {
                int[] rgb = screenData[y][x];
                // int pixelRGB = (0xFF << 24) | (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);
                Color pixelColor = new Color(rgb[0], rgb[1], rgb[2]);
                for (int i=0; i<upscalingFactor; i++){
                    for (int j=0; j<upscalingFactor; j++){
                        bi.setRGB(x*upscalingFactor+i, y*upscalingFactor+j, pixelColor.getRGB());
                    }
                }

                // System.out.println(rgb[0] + " " + rgb[1] + " " + rgb[2] + " " + Util.bitString(pixelColor.getRGB()));
                // g.setColor(pixelColor);
                // g.fillRect(x * upscalingFactor, y * upscalingFactor, upscalingFactor, upscalingFactor);
            }
        }
    }
    

    public void updateLCDStatus() {
        // Todo chekc that lyc=ly is checked after ly is set to 0
        int status = memory.getLCDStatus();

        // if LCD is not enabled
        if (memory.getLCDEnable() == 0) {
            memory.setLY(0);
            internalWindowCounter = 0;
            remainingCycles = 456;
            status &= 0b11111100;
            status = Util.setBit(status, 0, false); // TODO: FIGURE THIS PART OUT
            memory.setLCDStatus(status);
            return;
        }

        int LY = memory.getLY();
        int prevMode = status & 0b11;
        int mode;
        boolean reqInt = false;

        
        if (LY < 144) {
            if (remainingCycles >= 456-80) {
                mode = 2;
                status = Util.setBit(status, 1, true);
                status = Util.setBit(status, 0, false);
                reqInt = Util.getIthBit(status, 5) == 1;
            }
            else if (remainingCycles >= 456-80-172) {
                mode = 3;
                status = Util.setBit(status, 1, true);
                status = Util.setBit(status, 0, true);
            }
            else {
                mode = 0;
                status = Util.setBit(status, 1, false);
                status = Util.setBit(status, 0, false);
                reqInt = Util.getIthBit(status, 3) == 1;
                if (mode != prevMode && memory.hBlankDMA != null){
                    memory.hBlankDMA.tick();
                }
            }
        }

        else {
            // MODE 1 - VBLANK
            mode = 1;
            status = Util.setBit(status, 0, true);
            status = Util.setBit(status, 1, false);
            reqInt = Util.getIthBit(status, 4) == 1;
        }

        if ((mode != prevMode) && reqInt) {
            memory.requestInterrupt(1);
        }
        
        // check if LY can compare to LYC when LY == 0
        if (LY == memory.getLYC() && (LY != prevLY || memory.getLYC() != prevLYC)) {
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

    public void updateGraphics(int cycles) {
        updateLCDStatus();
        prevLY = memory.getLY();
        prevLYC = memory.getLYC();
        if (memory.getLCDEnable() == 0) {
            return;
        }
        
        remainingCycles -= cycles;
        int LY = memory.getLY();
        
        if (remainingCycles <= 0) {

            if (LY < 144) {
                drawScanline();
            }

            LY++;
            memory.setLY(LY);
            remainingCycles = 456;
            
            if (LY == 144) {
                memory.requestInterrupt(0);
                return;
            }
            if (LY > 153) {
                memory.setLY(0);
                internalWindowCounter = 0;
                return;
            }
            
            return;
        }
    }
}