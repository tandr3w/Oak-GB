public class Memory {
    // Hardware register addresses
    int LCDC_address;
    int STAT_address;
    int SCY_address;
    int SCX_address;
    int WY_address;
    int WX_address;
    int LY_address;
    int LYC_address;

    int[] memoryArray;
    public Memory() {
        memoryArray = new int[0xFFFF + 1];
        LCDC_address = 0xFF40;
        STAT_address = 0xFF41;
        SCY_address = 0xFF42;
        SCX_address = 0xFF43;
        LY_address = 0xFF44;
        LYC_address = 0xFF45;

        // Default values
        memoryArray[LCDC_address] = 0x91;
        memoryArray[STAT_address] = 0x85;
        memoryArray[SCY_address] = 0x00;
        memoryArray[SCX_address] = 0x00;
        memoryArray[LY_address] = 0x00;
        memoryArray[LYC_address] = 0x00;
    }

    // LCDC commands
    private int getBitFromLCDC(int bitNum) {
        return ((memoryArray[LCDC_address]) >> bitNum) & 1;
    }
    public int getLCDEnable() {
        return getBitFromLCDC(7);
    }
    public int getWindowTileMapArea() {
        return getBitFromLCDC(6);
    }
    public int getWindowEnable() {
        return getBitFromLCDC(5);
    }
    public int getAddressingMode() {    // Doesn't affect sprites
        return getBitFromLCDC(4);
    }
    public int getBGTileMapArea() {
        return getBitFromLCDC(3);
    }
    public int getOBJSize() {
        return getBitFromLCDC(2);
    }
    public int getOBJEnable() {
        return getBitFromLCDC(1);
    }
    public int getBGWindowEnable() {
        return getBitFromLCDC(0);
    }
    private void setBitInLCDC(int bitNum, boolean val) {
        int mask = 1 << bitNum;
        memoryArray[LCDC_address] = mask | memoryArray[LCDC_address];
    }
    public void setLCDEnable(boolean val) {
        setBitInLCDC(7, val);
    }
    public void setWindowTileMapArea(boolean val) {
        setBitInLCDC(6, val);
    }
    public void setWindowEnable(boolean val) {
        setBitInLCDC(5, val);
    }
    public void setAddressingMode(boolean val) {
        setBitInLCDC(4, val);
    }
    public void setBGTileMapArea(boolean val) {
        setBitInLCDC(3, val);
    }
    public void setOBJSize(boolean val) {
        setBitInLCDC(2, val);
    }
    public void setOBJEnable(boolean val) {
        setBitInLCDC(1, val);
    }
    public void setBGWindowEnable(boolean val) {
        setBitInLCDC(0, val);
    }
}
