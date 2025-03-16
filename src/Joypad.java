import java.awt.event.KeyEvent;

public class Joypad {
    Memory memory;
    int keys;

    public int getNumFromKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                return 0;
            case KeyEvent.VK_LEFT:
                return 1;
            case KeyEvent.VK_UP:
                return 2;
            case KeyEvent.VK_DOWN:
                return 3;
            case KeyEvent.VK_Z: // A Key
                return 4;
            case KeyEvent.VK_X: // B Key
                return 5;
            case KeyEvent.VK_SPACE: // SELECT
                return 6;
            case KeyEvent.VK_ENTER: // START
                return 7;
            default:
                return -1;
        }
    }

    public Joypad(Memory memory) {
        this.memory = memory;
    }

    public void updateJoypadPressed(KeyEvent e) {
        int keyNum = getNumFromKey(e);

        if (keyNum == -1) {
            return;
        }
        
        boolean previouslyPressed;
        if (Util.getIthBit(memory.joypadState, keyNum) == 0) {
            previouslyPressed = true;
        } else {
            previouslyPressed = false;
        }
        
        memory.joypadState = Util.setBit(memory.joypadState, keyNum, false);

        if (previouslyPressed) {
            return;
        }

        boolean buttonPressed = keyNum > 3 ? true : false;
        int JOYP = memory.getJOYP();

        if (buttonPressed && Util.getIthBit(JOYP, 5) == 0) {
            memory.requestInterrupt(4);
            return;
        }
        if (!buttonPressed && Util.getIthBit(JOYP, 4) == 0) {
            memory.requestInterrupt(4);
        }
    }

    public void updateJoypadReleased(KeyEvent e) {
        int keyNum = getNumFromKey(e);
        if (keyNum == -1) {
            return;
        }
        memory.joypadState = Util.setBit(memory.joypadState, keyNum, true);
    }
}
