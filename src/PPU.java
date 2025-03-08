import java.swing.*;
import java.awt.*;

public class PPU {
    int[] = memory
    boolean VRAMInUse;
    // TODO: Implement two queues for pixel FIFO

    public PPU(Memory memory) {
        VRAMInUse = false;
        this.memory = Memory.memoryArray;
    }

    // "MODE 2"
    public OAMScan() {

    }

    // "MODE 3"
    public drawPixels() {

    }

    // "MODE 0"
    public HBank() {
        
    }

    // "MODE 1"
    public VBank() {

    }

    public boolean isVRAMAccessible(){
        return VRAMInUse;
    }
}
