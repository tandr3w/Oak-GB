package src;

public class CPU {
    Registers registers;
    int[] memory;

    public CPU(){  
        registers = new Registers();
        memory = new int[0xFFFF]; // 65536 bytes
    }

    public int execute(Instruction instruction){
        registers.pc += 1;
        switch (instruction.operation){
            case Instruction.Operation.ADD:
                if (instruction.operand == Operand.n8){
                    addToA(memory[registers.pc]);
                    registers.pc += 1;
                    break;
                }
                addToA(registers.readValFromEnum(instruction.operand));
                break;
            case Instruction.Operation.SUB:
                System.out.println("SUBTRACTING");
                break;
        }
        return registers.pc;
    }

    public void addToA(int val){
        int a = registers.a;
        int result = val + a;
        boolean did_overflow = false;
        if (result > 0xFF){
           did_overflow = true;
           result = result & 0xFF; 
        }  
        // TODO: set carry flags and handle overflow
        registers.a = result;
    }
}
