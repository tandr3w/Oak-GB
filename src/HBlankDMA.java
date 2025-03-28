public class HBlankDMA {
    int startPosition;
    int endPosition;
    int length;
    int currentPosition;
    Memory memory;

    public HBlankDMA(Memory memory, int startPosition, int endPosition, int length){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.length = length;
        this.currentPosition = 0;
        this.memory = memory;
    }

    public void tick(){
        if (currentPosition >= length){
            memory.hBlankDMA = null;
            memory.memoryArray[0xFF55] = 0xFF;
        }
        for (int i=0; i<0x10; i++){
            memory.setMemory(startPosition + currentPosition, endPosition + currentPosition);
            currentPosition += 1;
        }
        memory.memoryArray[0xFF55] = length - currentPosition;
        memory.cpu.additionalCycles += 4;
    }
}
