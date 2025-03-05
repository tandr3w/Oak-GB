package src;

public class CPU {
    Registers registers;
    int[] memory;

    public CPU(){  
        registers = new Registers();
        memory = new int[0xFFFF]; // 65536 bytes
    }

    public void execute(Instruction instruction){
        switch (instruction.operation){
            case Instruction.Operation.ADD: // will need 
                int n = registers.readValFromEnum(instruction.operand);
                addToA(n);
                break;
            case Instruction.Operation.SUB:
                System.out.println("SUBTRACTING");
                break;
        }
    }

    public void addToA(int val){
        int a = registers.a;
        int result = val + a;
        // TODO: set carry flags and handle overflow
        registers.a = result;
    }
}
