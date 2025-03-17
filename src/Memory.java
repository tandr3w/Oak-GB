import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Memory {
    // Hardware register addresses
    final int LCDC_address;
    final int STAT_address;
    final int SCY_address;
    final int SCX_address;
    final int WY_address;
    final int WX_address;
    final int LY_address;
    final int LYC_address;

    final int BGP_address;
    final int OBP_address;

    // Timer control addresses
    final int TIMA_address;
    final int TMA_address;
    final int TMC_address;
    final int DIV_address;

    final int IF_address;
    final int IE_address;

    final int JOYP_address;
    public int joypadState;

    public int[] memoryArray;
    public boolean isMBC1;
    public boolean isMBC2;
    public int currentROMBank;
    public int currentRAMBank;
    public int[] ramBanks;

    public Memory() {
        memoryArray = new int[0xFFFF + 1];
        isMBC1 = false;
        isMBC2 = false;
        ramBanks = new int[0x100000]; // todo check this size
        currentROMBank = 1;
        currentRAMBank = 0;
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

        JOYP_address = 0xFF00; // joypad
        joypadState = 0b11111111;


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

        memoryArray[JOYP_address] = 0xCF;
    }

    public void setMemory(int address, int data){ // TODO: dont use this for unit tests
        if (address < 0x8000){
            // Handle ROM banking
            handleROMBanking(address, data);
        }
        if (address >= 0xFEA0 && address < 0xFEFF){
            return;
        }
        if (address >= 0xE000 && address < 0xFE00){
            memoryArray[address] = data;
            setMemory(address-0x2000, data);
        }
        else if (address == LY_address) // Reset LY if attempted write to it
        {
            memoryArray[address] = 0;
        }
        else if (address == 0xFF46){
            DMATransfer(data);
        }
        else {
            memoryArray[address] = data;
        }
    }
    public int getMemory(int address){
        if (address == JOYP_address) {
            int JOYP = getJoypadState();
            // System.out.println(JOYP);
            return JOYP;
        }
        // Read from ROM bank
        if (address >= 0x4000 && address <= 0x7FFF){
            return memoryArray[address - 0x4000 + 0x4000*currentROMBank];
        }

        // Read from RAM bank
        if (address >= 0xA000 && address <= 0xBFFF){
            return ramBanks[address - 0xA000 + (currentRAMBank*0x2000)];
        }

        return memoryArray[address];
    }

    public void handleROMBanking(int address, int data){
        if (address < 0x2000){ // Enable RAM bank writing

        }
        else if (address < 0x4000){ // ROM bank change

        }
        else if (address < 0x6000){ // RAM or ROM bank change based on mode

        }
        else if (address < 0x8000){ // Change RAM/ROM bank mode
            
        }
        else {
            System.out.println("ERR: Invalid address for ROM banking call");
        }
    }

    public void DMATransfer(int data){
        int address = data << 8;
        for (int i = 0; i < 0xA0; i++)
        {
          setMemory(0xFE00+i, getMemory(address+i));
        }
    }

    public int getJoypadState() {
        int state = memoryArray[JOYP_address]; // access memory array directly to avoid recursion errors
        state ^= 0xFF;
        if (Util.getIthBit(state, 4) == 0) {
            int buttons = (joypadState >> 4);
            buttons |= 0xF0;
            state &= buttons;
        } else if (Util.getIthBit(state, 5) == 0) {
            int d_pad = (joypadState & 0x0F);
            state &= d_pad;
        }
        return state;
    }

    // LCDC commands
    private int getBitFromLCDC(int bitNum) {
        return ((getMemory(LCDC_address)) >> bitNum) & 1;
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
        setMemory(LCDC_address, mask | memoryArray[LCDC_address]);
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
        return getMemory(SCX_address);
    }
    public int getSCY(){
        return getMemory(SCY_address);
    }
    // LY and LYC
    public int getLY() {
        return getMemory(LY_address);
    }
    public int getLYC() {
        return getMemory(LYC_address);
    }

    public void setLY(int val) {
        memoryArray[LY_address] = val; // must access array directly
    }
    
    public void setLYC(int val) {
        setMemory(LYC_address, val);
    }

    public int getWY() {
        return getMemory(WY_address);
    }

    public int getWX() {
        return getMemory(WX_address);
    }

    public void setWY(int val) {
        setMemory(WY_address, val);
    }

    public void setWX(int val) {
        setMemory(WX_address, val);
    }

    public int getPaletteColor(int id, int address){
        if (id == 0b00){
            return getMemory(address) & 0b11;
        }
        else if (id == 0b01){
            return (getMemory(address) & 0b1100) >> 2;
        }
        else if (id == 0b10){
            return (getMemory(address) & 0b110000) >> 4;
        }
        else if (id == 0b11){
            return (getMemory(address) & 0b11000000) >> 6;
        }
        else {
            System.out.println("Err: attempted access of invalid palette color ID");
            return 0;
        }
    }

    public int getLCDStatus() {
        return getMemory(STAT_address);
    }

    public void setLCDStatus(int val) {
        setMemory(STAT_address, val);
    }

    public boolean isClockEnabled() {
        return Util.getIthBit(getMemory(TMC_address), 2) == 1;
    }

    public int getTMC() {
        return getMemory(TMC_address);
    }

    public int getTMA() {
        return getMemory(TMA_address);
    }

    public int getTIMA() {
        return getMemory(TIMA_address);
    }

    public int getDIV() {
        return getMemory(DIV_address);
    }

    public void setDIV(int val) {
        setMemory(DIV_address, val);
    }

    public void setTIMA(int val) {
        setMemory(TIMA_address, val);
    }

    public int getJOYP() {
        return getMemory(JOYP_address);
    }

    public void setJOYP(int val) {
        setMemory(JOYP_address, val);
    }


    public void loadROM(String ROMName) {
        try {
            File ROMFile = new File(ROMName);
            FileInputStream in = new FileInputStream(ROMFile);
            long size = ROMFile.length();
            byte[] contents = new byte[(int) size];
            in.read(contents);
            System.out.println("File size: " + Util.hexString((int) size));
            for (int i=0; i<0x8000; i++){
                memoryArray[i] = (contents[i] & 0xFF);
            }
            switch (memoryArray[0x147]){
                case 1:
                    isMBC1 = true;
                    break;
                case 2:
                    isMBC1 = true;
                    break;
                case 3:
                    isMBC1 = true;
                    break;
                case 5:
                    isMBC2 = true;
                    break;
                case 6:
                    isMBC2 = true;
                    break;
            }
            System.out.println("Loading " + ROMName + " with mode " + memoryArray[0x147]);
            in.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public void requestInterrupt(int id){
        setMemory(0xFF0F, Util.setBit(getMemory(0xFF0F), id, true));
    }

}
