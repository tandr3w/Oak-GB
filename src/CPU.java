package src;

public class CPU {
    Registers registers;
    int[] memory;
    int pc;
    int sp;
    public CPU(){  
        registers = new Registers();
        memory = new int[0xFFFF]; // 65536 bytes
    }

    public void execute(Instruction instruction){
        switch (instruction.type){
            case Instruction.Type.ADD:
                // TODO: Automate this part by creating a generic ADD function and a GetRegisterValueFromEnum function.
                switch (instruction.target){
                    case ArithmeticTarget.C:
                        // TODO: Implement add instruction on C
                        break;
                    default:
                        break;
                }
                break;
            case Instruction.Type.SUBTRACT:
                System.out.println("SUBTRACTING");
                break;
        }
    }
}
