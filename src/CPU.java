package src;

public class CPU {
    Registers registers;
    int[] memory;

    public CPU(){  
        registers = new Registers();
        memory = new int[0xFFFF]; // 65536 bytes
    }

    public void execute(Instruction instruction){
        switch (instruction.type){
            case Instruction.Type.ADD:
                int n = registers.readValFromEnum(instruction.target);
                int a = registers.a;
                int result = n + a;
                // TODO: set carry flags and handle overflow
                registers.a = result;
                break;
            case Instruction.Type.SUB:
                System.out.println("SUBTRACTING");
                break;
        }
    }
}
