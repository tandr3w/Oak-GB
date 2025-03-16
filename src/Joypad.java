import java.awt.event.KeyEvent;

public class Joypad {
    Memory memory;
    int keys;
    int joypadState;

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
        joypadState = 0b11111111;
    }

    public void updateJoypadPressed(KeyEvent e) {
        int keyNum = getNumFromKey(e);

        if (keyNum == -1) {
            return;
        }
        
        boolean previouslyPressed;
        if (Util.getIthBit(joypadState, keyNum) == 0) {
            previouslyPressed = true;
        } else {
            previouslyPressed = false;
        }
        
        joypadState = Util.setBit(joypadState, keyNum, false);

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
        joypadState = Util.setBit(joypadState, keyNum, true);
    }

    // TODO: call get joypad state everytime the cpu writes to FF00 (JOYP register)
    public int getJoypadState() {
        int state = memory.getJOYP();
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
}
