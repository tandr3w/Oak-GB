import java.time.Clock;

import javax.sound.sampled.*;

public class APU {
    public int[] dutyCycles = {0b00000001, 0b00000011, 0b00001111, 0b11111100};
    public volatile Thread writer;
    public volatile int frequency;
    public volatile Memory memory;
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 4096;
    private volatile SourceDataLine line;
    private volatile byte[] audioBuffer = new byte[BUFFER_SIZE];
    private volatile int samplesThisWavelength = 0;
    private volatile int samplesPerWavelength = 0;
    private volatile int cycleCount = 0;
    private volatile int clockSpeed;
    private volatile int buffered;

    public APU(Memory memory, int CLOCKSPEED) {
        this.memory = memory;
        clockSpeed = CLOCKSPEED;
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


    public synchronized void makeSound() {
        writer = new Thread(() -> {
            int cyclesPerSample = clockSpeed/SAMPLE_RATE;
            while (true) {
                while (cycleCount >= cyclesPerSample) {
                    frequency = memory.getFrequencyC2();
                    cycleCount -= cyclesPerSample;

                    int dutyIndex = (memory.getNR21() >> 6) & 0b11;
                    double dutyCutoff = switch (dutyIndex) { // Which sample to start setting amplitude to 1 for?
                        case 0 -> 1.0 / 8;
                        case 1 -> 2.0 / 8;
                        case 2 -> 4.0 / 8;
                        case 3 -> 6.0 / 8;
                        default -> 0.5;
                    };
                    byte squareWave = (byte) ((samplesThisWavelength < samplesPerWavelength * dutyCutoff) ? 127 : -128);
                    audioBuffer[buffered] = squareWave;
                    samplesThisWavelength++;
                    if (samplesPerWavelength != 0){
                        samplesThisWavelength %= samplesPerWavelength;
                    }
                    buffered += 1;
            
                    if (buffered >= BUFFER_SIZE){
                        line.write(audioBuffer, 0, BUFFER_SIZE);
                        buffered = 0;
                    }
            }}
        });
        writer.start();
    }

    public void tick(int cycles) {
        cycleCount += cycles;
        samplesPerWavelength = SAMPLE_RATE / frequency;
    }
}

// hi