import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class Memory {
    // Hardware register addresses
    boolean CGBMode = false; // Gameboy Color Game TODO: detect on startup based on registers rather than file extension
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

    final int NR52_address;
    final int NR51_address;
    final int NR50_address;

    final int NR10_address;
    final int NR11_address;
    final int NR12_address;
    final int NR13_address;
    final int NR14_address;

    final int NR21_address;
    final int NR22_address;
    final int NR23_address;
    final int NR24_address;

    final int NR30_address;
    final int NR31_address;
    final int NR32_address;
    final int NR33_address;
    final int NR34_address;

    // WAVE PATTERN RAM IN FF30-FF3F

    final int NR41_address;
    final int NR42_address;
    final int NR43_address;
    final int NR44_address;

    public int[] memoryArray;
    public int[] cartridge;
    public boolean isMBC1;
    public boolean isMBC2;
    public boolean isMBC3;
    public boolean isMBC5;
    public int currentROMBank;
    public int currentRAMBank;
    public int[] ramBanks;
    public int[] vram;
    public int[] wram;
    public int[] BGPaletteMemory;
    public int[] spritePaletteMemory;
    public boolean ramEnabled;
    public boolean rtcEnabled;
    public boolean romBankingMode;
    public boolean rtcMappingMode; // MBC3
    public int ramSize;
    public CPU cpu;
    public HBlankDMA hBlankDMA;
    
    enum RTCRegister  {
        S,
        M,
        H,
        DL,
        DH,
    }

    public int S;
    public int M;
    public int H;
    public int DL;
    public int DH;

    public int latchedS;
    public int latchedM;
    public int latchedH;
    public int latchedDL;
    public int latchedDH;
    public boolean lastWrittenValueIsZero;

    public RTCRegister selectedRTCRegister;

    public String ROMPath;

    public Memory() {
        memoryArray = new int[0xFFFF + 1];
        isMBC1 = false;
        lastWrittenValueIsZero = false;
        isMBC2 = false;
        cartridge = new int[0x200000];
        ramBanks = new int[0x200000]; // todo check this size
        vram = new int[0x4000]; // 16KB
        wram = new int[0x8000]; // 32KB

        // CGB Palettes
        BGPaletteMemory = new int[0x40];
        spritePaletteMemory = new int[0x40];

        // Init palettes to white
        for (int i=0; i<0x40; i++){
            BGPaletteMemory[i] = 0xFF;
            spritePaletteMemory[i] = 0xFF;
        }

        currentROMBank = 1;
        currentRAMBank = 0;
        ramEnabled = false;
        rtcEnabled = false;
        rtcMappingMode = false;
        romBankingMode = false;
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

        // GLOBAL AUDIO REGISTERS
        NR52_address = 0xFF26; // Audio Master Control
        NR51_address = 0xFF25;
        NR50_address = 0xFF24;

        // CHANNEL 1
        NR10_address = 0xFF10; // Sweep
        NR11_address = 0xFF11; // Length Timer & Duty Cycle
        NR12_address = 0xFF12; // Volume & Envelope
        NR13_address = 0xFF13; // Period Low (write only)
        NR14_address = 0xFF14; // High / Control

        // CHANNEL 2
        NR21_address = 0xFF16; // Length Timer & Duty Cycle
        NR22_address = 0xFF17; // Volume & Envelope
        NR23_address = 0xFF18; // Period Low (write only)
        NR24_address = 0xFF19; // High / Control

        // CHANNEL 3
        NR30_address = 0xFF1A;
        NR31_address = 0xFF1B;
        NR32_address = 0xFF1C;
        NR33_address = 0xFF1D;
        NR34_address = 0xFF1E;

        // CHANNEL 4
        NR41_address = 0xFF20;
        NR42_address = 0xFF21;
        NR43_address = 0xFF22;
        NR44_address = 0xFF23;

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

        memoryArray[NR50_address] = 0x77;
        memoryArray[NR51_address] = 0xF3;
        memoryArray[NR51_address] = 0xF1;

        memoryArray[NR10_address] = 0x80;
        memoryArray[NR11_address] = 0xBF;
        memoryArray[NR12_address] = 0xF3;
        memoryArray[NR13_address] = 0xFF;
        memoryArray[NR14_address] = 0xBF;

        memoryArray[NR21_address] = 0x3F;
        memoryArray[NR22_address] = 0x00;
        memoryArray[NR23_address] = 0xFF;
        memoryArray[NR24_address] = 0xBF;

        memoryArray[NR30_address] = 0x7F;
        memoryArray[NR31_address] = 0xFF;
        memoryArray[NR32_address] = 0x9F;
        memoryArray[NR33_address] = 0xFF;
        memoryArray[NR34_address] = 0xBF;

        memoryArray[NR41_address] = 0xFF;
        memoryArray[NR42_address] = 0x00;
        memoryArray[NR43_address] = 0x00;
        memoryArray[NR44_address] = 0xBF;

        ramSize = 0;
    }


    public int getFrequencyC2(){
        return 131072/(2048-(getPeriodC2()));
    }
    public int getPeriodC2() {
        return ((getMemory(NR24_address)) & 0b00000111) << 8 | getMemory(NR23_address);
    }

    public int getNR21(){
        return getMemory(NR21_address);
    }

    public int getLeftVolume(){
        return (getMemory(NR50_address) & 0b01110000) >> 4;
    }

    public int getVINLeft(){
        return (getMemory(NR50_address) & 0b10000000) >> 7;
    }

    public int getVINRight(){
        return getMemory(NR50_address) & 1;
    }

    public int getAudioOn(){
        return Util.getIthBit(NR52_address, 7);
    }

    public int getCH2On(){
        return Util.getIthBit(NR52_address, 1);
    }


    public int getRightVolume(){
        return getMemory(NR50_address) & 0b111;
    }

    public void setMemory(int address, int data){ // TODO: dont use this for unit tests
        // restricted memory
        // if (address != LY_address && address != LCDC_address && address != STAT_address && address != 0xFF04){
        //     System.out.println(Util.hexString(address));
        // }
        if (address == 0xFF0F){
            memoryArray[0xFF0F] &= 0xE0;
            memoryArray[0xFF0F] |= data & 0b11111;
            return;
        }
        if (address == 0xFF04){
            setDIV(0); // handle overflow
            return;
        }
        if (address < 0x8000){
            // Handle ROM banking
            handleROMBanking(address, data);
        }

        // Write to VRAM bank (CGB only)
        else if (address >= 0x8000 && address <= 0x9FFF && CGBMode){
            int bank = getMemory(0xFF4F) & 1;
            vram[address - 0x8000 + 0x2000*bank] = data;
        }

        else if (address >= 0xA000 && address <= 0xBFFF){
            if (isMBC1 || isMBC2 || isMBC5) {
                if (ramEnabled){
                    ramBanks[address - 0xA000 + (currentRAMBank * 0x2000)] = data;
                }
            }

            if (isMBC3) {
                // either ram bank or register
                if (!rtcMappingMode){
                    if (ramEnabled){
                        ramBanks[address - 0xA000 + (currentRAMBank * 0x2000)] = data;
                    }
                }
                else {
                    if (rtcEnabled){
                        switch (selectedRTCRegister){
                            case RTCRegister.S:
                                S = data;
                                break;
                            case RTCRegister.M:
                                M = data;
                                break;
                            case RTCRegister.H:
                                H = data;
                                break;
                            case RTCRegister.DL:
                                DL = data;
                                break;
                            case RTCRegister.DH:
                                DH = data;
                                break;
                        }
                    }
                }
            }
        }
        // Write to WRAM Bank (CGB only)
        else if (CGBMode && address >= 0xD000 && address <= 0xDFFF){
            int bank = getMemory(0xFF70) & 0b00000111;
            if (bank == 0){ // Use 0xC000 - 0xCFFF for bank 0
                bank = 1;
            }
            wram[address - 0xD000 + bank*0x1000] = data;
        }

        else if (address >= 0xFEA0 && address < 0xFEFF){
            return;
        }
        else if (address >= 0xE000 && address < 0xFE00){
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
        else if (address == 0xFF55 && CGBMode){
            CGBDMATransfer(data);
        }

        else if (address == 0xFF69 && CGBMode){ // Background color palettes register
            int paletteAddress = getMemory(0xFF68) & 0b11111;
            BGPaletteMemory[paletteAddress] = data;
            if (Util.getIthBit(getMemory(0xFF68), 7) == 1){
                setMemory(0xFF68, getMemory(0xFF68) + 1); // Auto Increment Bit
            }
        }
        else if (address == 0xFF6B && CGBMode){ // Sprite color palettes register
            int paletteAddress = getMemory(0xFF6A) & 0b11111;
            spritePaletteMemory[paletteAddress] = data;
            if (Util.getIthBit(getMemory(0xFF6A), 7) == 1){
                setMemory(0xFF6A, getMemory(0xFF6A) + 1); // Auto Increment Bit
            }
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
            return cartridge[address - 0x4000 + 0x4000*currentROMBank];
        }

        // Read from VRAM bank (CGB only)
        if (address >= 0x8000 && address <= 0x9FFF && CGBMode){
            int bank = getMemory(0xFF4F);
            return getVRAM(bank, address);
        }

        if (CGBMode && address == 0xFF4F){
            return (memoryArray[0xFF4F] & 1);
        }

        // Read from RAM bank
        if (address >= 0xA000 && address <= 0xBFFF){
            // TODO: Check if MBC3
            if (!rtcMappingMode){
                if (ramEnabled){
                    return ramBanks[address - 0xA000 + (currentRAMBank*0x2000)];
                }
                else {
                    return 0xFF;
                }
            }
            else {
                if (rtcEnabled){
                    // Return value from register
                    switch (selectedRTCRegister){
                        case RTCRegister.S:
                            return latchedS;
                        case RTCRegister.M:
                            return latchedM;
                        case RTCRegister.H:
                            return latchedH;
                        case RTCRegister.DL:
                            return latchedDL;
                        case RTCRegister.DH:
                            return latchedDH;
                    }
                }
                else {
                    return 0xFF;
                }

            }
        }

        // Read from WRAM Bank (CGB only)
        if (CGBMode && address >= 0xD000 && address <= 0xDFFF){
            int bank = getMemory(0xFF70) & 0b00000111;
            if (bank == 0){ // Read 0xC000 - 0xCFFF for bank 0
                bank = 1;
            }
            return wram[address - 0xD000 + bank*0x1000];
        }

        // Read palette data
        if (address == 0xFF69 && CGBMode){ 
            int paletteAddress = getMemory(0xFF68) & 0b11111;
            return BGPaletteMemory[paletteAddress];
        }
        if (address == 0xFF6B && CGBMode){ 
            int paletteAddress = getMemory(0xFF6A) & 0b11111;
            return spritePaletteMemory[paletteAddress];
        }

        return memoryArray[address];
    }

    public int getVRAM(int bank, int address){
        if (!CGBMode){
            return memoryArray[address];
        }
        return vram[address - 0x8000 + 0x2000*bank];
    }

    public void handleROMBanking(int address, int data){
        address &= 0xFFFF;
        if (isMBC1){
            data = (byte) (data & 0xFF);
        }
        if (address < 0x2000){ // Enable RAM bank writing
            if (isMBC1 || isMBC5){
                if ((data & 0xF) == 0xA){
                    if (ramSize > 0){
                        ramEnabled = true;
                    }
                }
                else if ((data & 0xF) == 0){
                    if (ramSize > 0){
                        ramEnabled = false;
                    }
                }
                else {
                }
            }
            if (isMBC3){
                if ((data & 0xF) == 0xA){
                    if (ramSize > 0){
                        ramEnabled = true;
                    }
                    rtcEnabled = true;
                }
                else if ((data & 0xF) == 0){
                    if (ramSize > 0){
                        ramEnabled = false;
                    }
                    rtcEnabled = false;
                }
                else {
                }
            }
        }
        else if (address < 0x4000){ // ROM bank change
            if (isMBC1){
                // Change lower 5 bits of rom bank
                int lower5 = data & 0b11111;
                currentROMBank &= 0b1100000;
                currentROMBank |= lower5;
                if (currentROMBank == 0 || currentROMBank == 0x20 || currentROMBank == 0x40 || currentROMBank == 0x60){
                    currentROMBank += 1; // ROM bank cannot be 0
                }
            }
            else if (isMBC2){
                currentROMBank = data & 0xF;
                if (currentROMBank == 0){
                    currentROMBank = 1; // ROM bank cannot be 0
                }
            }
            else if (isMBC3){
                currentROMBank = data & 0x7F;
                if (currentROMBank == 0){
                    currentROMBank = 1; // ROM bank cannot be 0
                }
            }
            else if (isMBC5){
                if (address < 0x3000){
                    currentROMBank = data & 0xFF;
                }
                else {
                    currentROMBank |= ((data & 1) << 8);
                }
            }
        }
        else if (address < 0x6000){ // RAM or ROM bank change based on mode
            if (isMBC1){
                if (romBankingMode){
                    currentROMBank &= 0b11111; // Unset first 3 bits
                    currentROMBank |= (data & 0b1100000);
                    if (currentROMBank == 0 || currentROMBank == 0x20 || currentROMBank == 0x40 || currentROMBank == 0x60){
                        currentROMBank += 1; // ROM bank cannot be 0
                    }
                }
                else {
                    currentRAMBank = data & 0b11;
                }
            }
            else if (isMBC3) {
                if (data <= 3){
                    currentRAMBank = data;
                    rtcMappingMode = false;
                }
                else if ((data >= 8) && (data <= 0x0C)){
                    if (rtcEnabled){
                        rtcMappingMode = true;
                        switch (data){
                            case 0x08:
                                selectedRTCRegister = RTCRegister.S;
                                break;
                            case 0x09:
                                selectedRTCRegister = RTCRegister.M;
                                break;
                            case 0x0A:
                                selectedRTCRegister = RTCRegister.H;
                                break;
                            case 0x0B:
                                selectedRTCRegister = RTCRegister.DL;
                                break;
                            case 0x0C:
                                selectedRTCRegister = RTCRegister.DH;
                                break;
                        }
                        currentRAMBank = -1;
                    }
                    // RTC stuff
                }
            }
            else if (isMBC5){
                currentRAMBank = data;
            }
        }
        else if (address < 0x8000){ // Change RAM/ROM bank mode
            if (isMBC1){
                if (ramSize >= 3){
                    romBankingMode = (data & 1) == 0;
                }
                // if (romBankingMode){
                //     currentROMBank = 0;
                // }
            }
            else if (isMBC3){
                if (lastWrittenValueIsZero && data == 1){
                    latchedS = S;
                    latchedM = M;
                    latchedH = H;
                    latchedDL = DL;
                    latchedDH = DH;
                }
                if (data == 0){
                    lastWrittenValueIsZero = true;
                }
                else {
                    lastWrittenValueIsZero = false;
                }
            }
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
        cpu.additionalCycles += 640;
    }

    public void CGBDMATransfer(int data){
        int source = (getMemory(0xFF51) << 8) | (getMemory(0xFF52) & 0b11110000);
        int destination = ((getMemory(0xFF53) & 0b11111) << 8) | (getMemory(0xFF54) & 0b11110000);
        int length = ((data | 0b01111111) + 1) * 0x10;
        if ((data & 0b10000000) == 0){ // General Purpose DMA
            if (hBlankDMA != null){
                System.out.println("Terminated hBlankDMA"); // FIXME;
            }

            for (int i=0; i<length; i++){
                setMemory(source + i, getMemory(destination+i));
            }
            cpu.additionalCycles += 4*length;
            memoryArray[0xFF55] = 0xFF;
        }
        else { // HBlank DMA
            hBlankDMA = new HBlankDMA(this, source, destination, length);
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

    public int[][] getCGBPaletteColor(int address, boolean sprite){
        int[][] colorData = new int[4][3];
        for (int j=0; j<4; j++){
            int colors;
            if (sprite){
                colors = ((spritePaletteMemory[address+2*j+1] & 0xFF) << 8) | (spritePaletteMemory[address+2*j] & 0xFF);
            }
            else {
                colors = ((BGPaletteMemory[address+2*j+1] & 0xFF) << 8) | (BGPaletteMemory[address+2*j] & 0xFF);
            }
            // Red
            colorData[j][0] = colors & 0b11111;
            // Green
            colorData[j][1] = (colors >> 5) & 0b11111;
            // Blue
            colorData[j][2] = (colors >> 10) & 0b11111;

            // Convert from RGB555 to RGB888
            colorData[j][0] *= 8;
            colorData[j][1] *= 8;
            colorData[j][2] *= 8;
        }
        return colorData;
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
        memoryArray[DIV_address] = val;
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

    public void loadROM(String ROMPath) {
        this.ROMPath = ROMPath;
        try {
            File ROMFile = new File(ROMPath);
            FileInputStream in = new FileInputStream(ROMFile);
            long size = ROMFile.length();
            byte[] contents = new byte[(int) size];
            in.read(contents);
            System.out.println("File size: " + Util.hexString((int) size));
            for (int i=0; i<0x8000; i++){
                memoryArray[i] = (contents[i] & 0xFF);
            }
            for (int i=0; i<size; i++){
                cartridge[i] = (contents[i] & 0xFF);
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
                case 0x0F:
                    isMBC3 = true;
                    break;
                case 0x10:
                    isMBC3 = true;
                    break;   
                case 0x11:
                    isMBC3 = true;
                    break;     
                case 0x12:
                    isMBC3 = true;
                    break;       
                case 0x13:
                    isMBC3 = true;
                    break;
                case 0x19:
                    isMBC5 = true;
                    break;
                case 0x1A:
                    isMBC5 = true;
                    break;
                case 0x1B:
                    isMBC5 = true;
                    break;
                case 0x1C:
                    isMBC5 = true;
                    break;
                case 0x1D:
                    isMBC5 = true;
                    break;
                case 0x1E:
                    isMBC5 = true;
                    break;
            }
            System.out.println("Loading " + ROMPath + " with mode " + memoryArray[0x147]);
            if (isMBC1) {
                System.out.println("Using MBC1!");;
            }
            else if (isMBC2) {
                System.out.println("Using MBC2!");
            }
            else if (isMBC3) {
                System.out.println("Using MBC3");
            }
            ramSize = memoryArray[0x149];
            in.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public void requestInterrupt(int id){
        // System.out.println("REQUESTING INTERRUPT");
        // System.out.println("IF IS CURRENTLY: " + getMemory(0xFF0F));
        setMemory(0xFF0F, Util.setBit(getMemory(0xFF0F), id, true));
    }

    public String extractRomName(String ROMPath) {
        String ROMName = Paths.get(ROMPath).getFileName().toString();
        if (ROMName.toLowerCase().endsWith(".gbc")){
            CGBMode = true;
        }
        if (ROMName.toLowerCase().endsWith(".gb") || ROMName.toLowerCase().endsWith(".gbc")) {
            ROMName = ROMName.substring(0, ROMName.lastIndexOf('.'));
        } else {
            System.out.println("Invalid ROM Path: Extension was not .gb or .gbc!");
        }

        return ROMName;
    }

    public int getBatteryRamSize() {
        int batteryRAMSize;
        switch(ramSize) {
            case 1: batteryRAMSize = 2 * 1024; break;
            case 2: batteryRAMSize = 8 * 1024; break; // Commmon for mbc2
            case 3: batteryRAMSize = 32 * 1024; break;
            case 4: batteryRAMSize = 128 * 1024; break;
            case 5: batteryRAMSize = 64 * 1024; break;
            default: batteryRAMSize = 0; break;
        }
        return batteryRAMSize;
    }

    public void saveOnClose() {
        // TODO: check support saving for other MBCs and remove early return
        if (!isMBC3 && !isMBC1 && !isMBC5 && ramSize == 0) {
            return;
        }
        String ROMName = extractRomName(ROMPath);
        String folderName = "Saves";

        File folderDir = new File(folderName);
        if (!folderDir.exists()) {
            folderDir.mkdirs();
        }

        String savePath = folderName + "/" + ROMName + ".sav";
        // System.out.println(savePath);
        File saveFile = new File(savePath);
        System.out.println(saveFile);
        if (!saveFile.exists()) {
            try {
                if (saveFile.createNewFile()) {
                    System.out.println("Created new save file: " + savePath);
                } else {
                    System.out.println("Failed to create new save file: " + savePath);
                }
            } catch (IOException e) {
                System.out.println("Error creating save file: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        
        try {
            FileOutputStream out = new FileOutputStream(savePath);
            int batteryRAMSize = getBatteryRamSize();
            if (batteryRAMSize == 0) {
                System.out.println("Could not save because the batteryRAMSize is 0");
                out.close();
                return;
            }

            byte[] saveData = new byte[batteryRAMSize];

            for (int i = 0; i < batteryRAMSize; i++) {
                saveData[i] = (byte) (ramBanks[i] & 0xFF);
            }
            out.write(saveData);
            
            if (!isMBC3) {
                out.close();
                return;
            }

            out.write((byte) S);
            out.write((byte) M);
            out.write((byte) H);
            out.write((byte) DL);
            out.write((byte) DH);
            
            long savedTime = System.currentTimeMillis();
            byte byte0 = Util.getByteFromLong(savedTime, 0);
            byte byte1 = Util.getByteFromLong(savedTime, 1);
            byte byte2 = Util.getByteFromLong(savedTime, 2);
            byte byte3 = Util.getByteFromLong(savedTime, 3);
            byte byte4 = Util.getByteFromLong(savedTime, 4);
            byte byte5 = Util.getByteFromLong(savedTime, 5);
            byte byte6 = Util.getByteFromLong(savedTime, 6);
            byte byte7 = Util.getByteFromLong(savedTime, 7);

            out.write(byte0);
            out.write(byte1);
            out.write(byte2);
            out.write(byte3);
            out.write(byte4);
            out.write(byte5);
            out.write(byte6);
            out.write(byte7);

            out.close();
        }
        catch (IOException e) {
            System.out.println("Error occured while saving: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadSave() {
        
        String ROMName = extractRomName(ROMPath);
        String folderName = "Saves";
        String savePath = folderName + "/" + ROMName + ".sav";
        File saveFile = new File(savePath);
        
        if (!saveFile.exists()) {
            System.out.println("No save file for " + ROMName);
            return;
        }
        
        try (FileInputStream in = new FileInputStream(saveFile)) {

            int batteryRAMSize = getBatteryRamSize();
            byte[] saveData = new byte[batteryRAMSize];
            int bytesRead = in.read(saveData);
            if (bytesRead < batteryRAMSize) {
                System.out.println("WARNING: Data read from save file was incomplete!");
            }
            for (int i = 0; i < batteryRAMSize; i++) {
                ramBanks[i] = saveData[i] & 0xFF;
            }

            if (!isMBC3) {
                return;
            }

            if (in.available() >= 13) {
                S = in.read();
                M = in.read();
                H = in.read();
                DL = in.read();
                DH = in.read();
                System.out.println(S);
                byte byte0 = (byte) in.read();
                byte byte1 = (byte) in.read();
                byte byte2 = (byte) in.read();
                byte byte3 = (byte) in.read();
                byte byte4 = (byte) in.read();
                byte byte5 = (byte) in.read();
                byte byte6 = (byte) in.read();
                byte byte7 = (byte) in.read();
                long prevTime = Util.getLongFromBytes(byte0, byte1, byte2, byte3, byte4, byte5, byte6, byte7);
                updateRTCOffline(prevTime);


            } else {
                System.out.println("WARNING: RTC registers not read! / TIME NOT UPDATED PROPERLY");
            }
            
        } catch (IOException e) {
            System.out.println("Error occured while ading save: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateRTC(long elapsedSeconds) {
        if (elapsedSeconds <= 0) { // Can happen if system time is changed
            return;
        }

        S += elapsedSeconds;
        if (S >= 60) {
            M += S/60;
            S %= 60;
        }
        if (M >= 60) {
            H += M/60;
            M %= 60;
        }
        if (H >= 24) {
            DL += H/24;
            H %= 24;
        }
        if (DL >= 256) { // DH bit 0 is 9th bit of day counter
            DL = 0;
            boolean bit0 = Util.getIthBit(DH, 0) == 1;
            DH = Util.setBit(DH, 0, !bit0);
        }
        if ((Util.getIthBit(DH, 0) << 8 | DL) >= 512) {
            DL = 0;
            DH = Util.setBit(DH, 0, false);
            DH = Util.setBit(DH, 6, true);  // Set day overflow flag
        }
    }

    public void updateRTCOffline(long prevTime) {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - prevTime) / 1000;
        updateRTC(elapsedSeconds);
    }
}
