import java.time.Clock;

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
    private int cycleCount = 0;
    private int clockSpeed;
    private int buffered;

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
        // writer = new Thread(() -> {
        //     int cyclesPerSample = clockSpeed/SAMPLE_RATE;
        //     while (true) {
        //         int buffered = 0;
        //         while (buffered < BUFFER_SIZE){
        //             if (true){
        //                 int dutyIndex = (memory.getNR21() >> 6) & 0b11;
        //                 double dutyCutoff = switch (dutyIndex) { // Which sample to start setting amplitude to 1 for?
        //                     case 0 -> 1.0 / 8;
        //                     case 1 -> 2.0 / 8;
        //                     case 2 -> 4.0 / 8;
        //                     case 3 -> 6.0 / 8;
        //                     default -> 0.5;
        //                 };
        //                 byte squareWave = (byte) ((samplesThisWavelength < samplesPerWavelength * dutyCutoff) ? 127 : -128);
        //                 audioBuffer[buffered] = squareWave;
        //                 samplesThisWavelength++;
        //                 if (samplesPerWavelength != 0){
        //                     samplesThisWavelength %= samplesPerWavelength;
        //                 }
        //                 buffered += 1;
        //             }
        //         }
        //         line.write(audioBuffer, 0, BUFFER_SIZE);
        //         buffered = 0;
        //     }
        // });
        // writer.start();
    }

    public void tick(int cycles) {
        cycleCount += cycles;
        frequency = memory.getFrequencyC2();
        samplesPerWavelength = SAMPLE_RATE / frequency;
        int cyclesPerSample = clockSpeed/SAMPLE_RATE;
        while (cycleCount >= cyclesPerSample) {
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
        }
    }
}

// hi