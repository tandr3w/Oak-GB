import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class APU {
    public int[] dutyCycles = {0b00000001, 0b00000011, 0b00001111, 0b11111100};
    public int sequencePointer;
    public Thread writer;

    public APU(Memory memory) {
        try {
            makeSound();
        } catch (LineUnavailableException e) {
        }
    }

    public void makeSound() throws LineUnavailableException {
        System.out.println("Make sound");
        byte[] buf = new byte[2];
        int frequency = 44100; //44100 sample points per 1 second
        AudioFormat af = new AudioFormat((float) frequency, 16, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();

        class AudioThread extends Thread {
            @Override
            public void run(){
                int numberOfTimesFullSinFuncPerSec = 441; //number of times in 1sec sin function repeats
                int sampleRate = 44100;
                int counter = 0;
                while (true){
                    float numberOfSamplesToRepresentFullSin= (float) frequency / numberOfTimesFullSinFuncPerSec;
                    double angle = (counter) / (numberOfSamplesToRepresentFullSin/ 2.0) * Math.PI;  // /divide with 2 since sin goes 0PI to 2PI
                    short a = (short) (Math.sin(angle) * 32767);  //32767 - max value for sample to take (-32767 to 32767)
                    buf[0] = (byte) (a & 0xFF); //write 8bits ________WWWWWWWW out of 16
                    buf[1] = (byte) (a >> 8); //write 8bits WWWWWWWW________ out of 16
                    sdl.write(buf, 0, 2);
                    counter += 1;
                }   
            }
        }

        writer = new AudioThread();
        writer.start();
        
    }
}


