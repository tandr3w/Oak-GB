package src;
public class HBlankDMA {
    int source;
    int destination;
    int length;
    int currentPosition;
    Memory memory;

    public HBlankDMA(Memory memory, int source, int destination, int length){
        this.source = source;
        this.destination = destination;
        this.length = length;
        this.currentPosition = 0;
        this.memory = memory;
    }

    public void tick(){
        if (memory.cpu.halted){
            return;
        }
        if (currentPosition >= length){
            memory.hBlankDMA = null;
            memory.memoryArray[0xFF55] = 0xFF;
        }
        for (int i=0; i<0x10; i++){
            memory.setMemory(destination + 0x8000 + currentPosition, memory.getMemory(source + currentPosition));
            currentPosition += 1;
        }

        memory.memoryArray[0xFF55] = length/0x10 - currentPosition/0x10 - 1;
        memory.cpu.additionalCycles += 4 * 0x10;
    }
}
