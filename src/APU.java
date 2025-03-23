import javax.sound.sampled.*;

public class APU {
    public int[] dutyCycles = {0b00000001, 0b00000011, 0b00001111, 0b11111100};
    public Thread writer;
    public int frequency;
    public Memory memory;
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 1024;
    private SourceDataLine line;
    private byte[] audioBuffer = new byte[BUFFER_SIZE];
    private int samplesThisWavelength = 0;
    private int samplesPerWavelength = 0;

    public APU(Memory memory) {
        this.memory = memory;
        frequency = 441;
        try {
            initAudio();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void initAudio() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format, BUFFER_SIZE);
        line.start();
    }


    public void makeSound() {
        writer = new Thread(() -> {
            while (true) {
                int buffered = 0;
                for (int i = 0; i < BUFFER_SIZE; i++) {
                    int dutyIndex = (memory.getNR21() >> 6) & 0b11;
                    double dutyCutoff = switch (dutyIndex) { // Which sample to start setting amplitude to 1 for?
                        case 0 -> 1.0 / 8;
                        case 1 -> 2.0 / 8;
                        case 2 -> 4.0 / 8;
                        case 3 -> 6.0 / 8;
                        default -> 0.5;
                    };
                    byte squareWave = (byte) ((samplesThisWavelength < samplesPerWavelength * dutyCutoff) ? 127 : -128);
                    audioBuffer[i] = squareWave;
                    samplesThisWavelength++;
                    if (samplesPerWavelength != 0){
                        samplesThisWavelength %= samplesPerWavelength;
                    }
                }
                line.write(audioBuffer, 0, BUFFER_SIZE);
            }
        });
        writer.start();
    }

    public void tick() {
        frequency = memory.getFrequencyC2();
        samplesPerWavelength = SAMPLE_RATE / frequency;
    }
}

// hi