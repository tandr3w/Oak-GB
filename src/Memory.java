import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    int BGP_address;
    int OBP_address;

    // Timer control addresses
    int TIMA_address;
    int TMA_address;
    int TMC_address;
    int DIV_address;

    int IF_address;
    int IE_address;

    int[] memoryArray;
    public Memory() {
        memoryArray = new int[0xFFFF + 1];
        LCDC_address = 0xFF40; // Settings for display
        STAT_address = 0xFF41;
        SCY_address = 0xFF42; // y position of the background to start drawing from (scroll)
        SCX_address = 0xFF43; // x position of the background to start drawing from (scroll)
        WY_address = 0xFF4A; // y position of the window to start drawing from
        WX_address = 0xFF4B; // x position - 7 of the window to start drawing from
        LY_address = 0xFF44; // current position we are drawing at (offset from start)
        LYC_address = 0xFF45;

        BGP_address = 0xFF47; // Color palette
        OBP_address = 0xFF48; // OBP1 is 0xFF49; OBP is typically left uninitialized

        TIMA_address = 0xFF05; // Also called TIMA
        TMA_address = 0xFF06;
        TMC_address = 0xFF07; // Also called TAC
        DIV_address = 0xFF04; // Divider register

        IF_address = 0xFF0F;
        IE_address = 0xFFFF;


        // Default values
        memoryArray[LCDC_address] = 0x91;
        memoryArray[STAT_address] = 0x85;
        memoryArray[SCY_address] = 0x00;
        memoryArray[SCX_address] = 0x00;
        memoryArray[WY_address] = 0x00;
        memoryArray[WX_address] = 0x00;
        memoryArray[LY_address] = 0x00;
        memoryArray[LYC_address] = 0x00;
        memoryArray[BGP_address] = 0xFC; // 0b1111100 - initially maps to black, black, black, white

        memoryArray[TIMA_address] = 0x00;
        memoryArray[TMA_address] = 0x00;
        memoryArray[TMC_address] = 0xF8;
        memoryArray[DIV_address] = 0xAB; // 171

        memoryArray[IF_address] = 0xE1;
        memoryArray[IE_address] = 0x00;
    }

    public void setMemory(int address, int data){ // TODO: dont use this for unit tests
        if (address < 0x8000){
            return;
        }
        if (address >= 0xFEA0 && address < 0xFEFF){
            return;
        }
        if (address >= 0xE000 && address < 0xFE00){
            memoryArray[address] = data;
            setMemory(address-0x2000, data);
        }
        else if (address == 0xFF44) // Reset LY if attempted write to it
        {
           memoryArray[address] = 0 ;
        }
        else {
            memoryArray[address] = data;
        }
    }

    public int getMemory(int address){
        return memoryArray[address];
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

    public int getSCX(){
        return memoryArray[SCX_address];
    }
    public int getSCY(){
        return memoryArray[SCY_address];
    }
    // LY and LYC
    public int getLY() {
        return memoryArray[LY_address];
    }
    public int getLYC() {
        return memoryArray[LYC_address];
    }

    public void setLY(int val) {
        memoryArray[LY_address] = val;
    }
    
    public void setLYC(int val) {
        memoryArray[LYC_address] = val;
    }

    public int getWY() {
        return memoryArray[WY_address];
    }

    public int getWX() {
        return memoryArray[WX_address];
    }

    public void setWY(int val) {
        memoryArray[WY_address] = val;
    }

    public void setWX(int val) {
        memoryArray[WX_address] = val;
    }

    public int getPaletteColor(int id, int address){
        if (id == 0b00){
            return memoryArray[address] & 0b11;
        }
        else if (id == 0b01){
            return (memoryArray[address] & 0b1100) >> 2;
        }
        else if (id == 0b10){
            return (memoryArray[address] & 0b110000) >> 4;
        }
        else if (id == 0b11){
            return (memoryArray[address] & 0b11000000) >> 6;
        }
        else {
            System.out.println("Err: attempted access of invalid palette color ID");
            return 0;
        }
    }

    public int getLCDStatus() {
        return memoryArray[STAT_address];
    }

    public void setLCDStatus(int val) {
        memoryArray[STAT_address] = val;
    }

    public boolean isClockEnabled() {
        return Util.getIthBit(memoryArray[TMC_address], 2) == 1;
    }

    public int getTMC() {
        return memoryArray[TMC_address];
    }

    public int getTMA() {
        return memoryArray[TMA_address];
    }

    public int getTIMA() {
        return memoryArray[TIMA_address];
    }

    public int getDIV() {
        return memoryArray[DIV_address];
    }

    public void setDIV(int val) {
        memoryArray[DIV_address] = val;
    }

    public void setTIMA(int val) {
        memoryArray[TIMA_address] = val;
    }

    

    public void loadROM(String ROMName) {
        try {
            File ROMFile = new File(ROMName);
            FileInputStream in = new FileInputStream(ROMFile);
            long size = ROMFile.length();
            byte[] contents = new byte[(int) size];
            in.read(contents);
            for (int i=0; i<size; i++){
                memoryArray[i] = (contents[i] & 0xFF);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public void requestInterrupt(int id){
        memoryArray[0xFF0F] = Util.setBit(memoryArray[0xFF0F], id, true);
    }
    
}
