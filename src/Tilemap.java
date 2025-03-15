import javax.swing.*;
import java.awt.*;

public class Tilemap extends JPanel {
    PPU ppu;
    Memory memory;

    public Tilemap(Memory memory, PPU ppu) {
        this.memory = memory;
        this.ppu = ppu;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(512, 256);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileDataLocation;
        boolean signed = false;

        if (memory.getAddressingMode() == 1) {
            tileDataLocation = 0x8000;
        } else {
            tileDataLocation = 0x8800;
            signed = true;
        }

        drawTilemap(g, 0x9800, 0, 0, tileDataLocation, signed);
        drawTilemap(g, 0x9C00, 256, 0, tileDataLocation, signed);

        drawViewportBox(g);
    }

    public void drawTilemap(Graphics g, int displayDataLocation, int offsetX, int offsetY, int tileDataLocation, boolean signed) {
        for (int tileY = 0; tileY < 32; tileY++) {
            for (int tileX = 0; tileX < 32; tileX++) {
                int dataPos = displayDataLocation + tileX + tileY * 32;
                short tileIdentifier;
                if (signed) {
                    tileIdentifier = (short) ((byte) memory.memoryArray[dataPos]);
                } else {
                    tileIdentifier = (short) memory.memoryArray[dataPos];
                }

                int tileMemLocation = tileDataLocation;
                if (signed) {
                    tileMemLocation += (tileIdentifier + 128) * 16; // Each tile is 16 bytes
                } else {
                    tileMemLocation += tileIdentifier * 16;
                }

                for (int pixelY = 0; pixelY < 8; pixelY++) {
                    int byteInTile = pixelY * 2;
                    int byte1 = memory.memoryArray[tileMemLocation + byteInTile];
                    int byte2 = memory.memoryArray[tileMemLocation + byteInTile + 1];

                    for (int pixelX = 0; pixelX < 8; pixelX++) {
                        int bit1 = Util.getIthBit(byte1, 7 - pixelX);
                        int bit2 = Util.getIthBit(byte2, 7 - pixelX);
                        int colorID = memory.getPaletteColor((bit2 << 1) | bit1, memory.BGP_address);
                        int[] rgb = ppu.colourPaletteTranslator[colorID];

                        int screenX = offsetX + tileX * 8 + pixelX;
                        int screenY = offsetY + tileY * 8 + pixelY;

                        g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                        g.fillRect(screenX, screenY, 1, 1);
                    }
                }
            }
        }
    }

    public void drawViewportBox(Graphics g) {
        int SCX = memory.getSCX();
        int SCY = memory.getSCY();

        int viewportX = SCX % 256;
        int viewportY = SCY % 256;

        g.setColor(Color.RED);
        g.drawRect(viewportX, viewportY, 160, 144);
    }
}
