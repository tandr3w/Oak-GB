import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class APU {
    public int[] dutyCycles = {0b00000001, 0b00000011, 0b00001111, 0b11111100};
    // public int[] dutyCycles = {0b11110000, 0b11110000, 0b11110000, 0b11110000};

    public int sequencePointer;
    public int frequencyTimer;
    public Thread writer;
    public int frequency;
    public int amplitude;
    public Memory memory;

    public APU(Memory memory) {
        this.memory = memory;
        sequencePointer = 0;
        frequency = 441;
        frequencyTimer = (2048 - frequency) * 4;
        amplitude = 0;
        try {
            makeSound();
        } catch (LineUnavailableException e) {
        }
    }

    public void makeSound() throws LineUnavailableException {
        System.out.println("Make sound");
        byte[] buf = new byte[2];
        AudioFormat af = new AudioFormat((float) frequency, 16, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();

        class AudioThread extends Thread {
            @Override
            public void run(){
                while (true){
                    int a = 0;
                    if (amplitude == 1){
                        a = 32767;
                    }
                    else if (amplitude == 0){
                        a = -32767;
                    }
                    buf[0] = (byte) (a & 0xFF); //write 8bits ________WWWWWWWW out of 16
                    buf[1] = (byte) (a >> 8); //write 8bits WWWWWWWW________ out of 16
                    sdl.write(buf, 0, 2);
                }
            }
        }

        writer = new AudioThread();
        writer.start();
    }
    public void tick(){
        frequencyTimer -= 1;
        frequency = memory.getFrequencyC2();
        if (frequencyTimer <= 0){
            frequencyTimer = (2048 - frequency) * 4;
            sequencePointer += 1;
            sequencePointer %= 8;
        }
        amplitude = Util.getIthBit(dutyCycles[memory.getNR21() >> 6], sequencePointer);
        // System.out.println(frequency);
    }
}


