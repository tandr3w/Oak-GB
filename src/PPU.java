import javax.swing.*;
import java.awt.*;

public class PPU {
    int[] memory;
    boolean VRAMInUse;
    // TODO: Implement two queues for pixel FIFO

    public PPU(Memory memory) {
        VRAMInUse = false;
        this.memory = memory.memoryArray;
    }

    // "MODE 2"
    public void OAMScan() {

    }

    // "MODE 3"
    public void drawPixels() {

    }

    // "MODE 0"
    public void HBank() {
        
    }

    // "MODE 1"
    public void VBank() {

    }

    public boolean isVRAMAccessible(){
        return VRAMInUse;
    }
}
