public class Queue {
    private int[] data;
    private int head;
    private int tail;
    private int count;
    private int size;

    public Queue(int size) {
        this.size = size;
        data = new int[this.size];
        head = 0;
        tail = 0;
        count = 0;
    }

    public void enqueue(int val) {
        data[tail] = val;
        count++;
        tail = (tail + 1) % data.length;
    }

    public int dequeue() {
        int result = data[head];
        head = (head + 1) % data.length;
        count--;
        return result;
    }
    
    public int peek() {
        return data[head];
    }

    public int length() {
        return count;
    }
    
    public void clear() {
        data = new int[size];
        head = 0;
        tail = 0;
        count = 0;
    }
}