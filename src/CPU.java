package src;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

public class CPU {
    Registers registers;
    int[] memory;

    public CPU(){
        memory = new int[0xFFFF]; // 65536 bytes
        registers = new Registers(this);
        loadROM("ROMs/snake.gb"); // FIXME
    }

    public int execute(Instruction instruction){
        // Load correct number of bytes
        if (instruction.num_bytes > 1){
            instruction.next_bytes = new int[instruction.num_bytes - 1];
            for (int i=0; i<instruction.num_bytes-1; i++){
                instruction.next_bytes[i] = memory[registers.pc + i + 1];
            }
        }
        registers.pc += instruction.num_bytes;
        
        switch (instruction.operation){

            case Operation.ADD:
                if (instruction.operand == Operand.n8){
                    addToA(instruction.next_bytes[0]);
                    break;
                }
                // otherwise, add from another register
                addToA(registers.readValFromEnum(instruction.operand));
                break;
            case Operation.ADD16:
                if (instruction.operandToSet == Operand.SP && instruction.operand == Operand.e8){
                    add16(Operand.SP, instruction.next_bytes[0]);
                    break;
                }
                // Add to HL
                add16(Operand.HL, registers.readValFromEnum(instruction.operand));
                break;


            case Operation.SUB:
                subFromA(registers.readValFromEnum(instruction.operand));
                break;


            case Operation.LD: // FOR 8-BIT LOAD OPERATIONS
                // instruction.operand is the value that will be loaded

                // get the value that needs to be loaded first
                int valToLoad;
                if (instruction.operand == Operand.a16){
                    // TODO: FIGURE OUT HOW ENDIANNESS WORKS...
                    valToLoad = memory[((instruction.next_bytes[0] & 0xFF) << 8) | (instruction.next_bytes[1] & 0xFF)];
                } 
                
                else if (instruction.operand == Operand.a8) {
                    valToLoad = memory[instruction.next_bytes[0] + 0xFF00];
                }
                
                // otherwise, the operant is a register
                else {;
                    valToLoad = registers.readValFromEnum(instruction.operand);
                }
                
                
                if (instruction.operandToSet == Operand.a16) {
                    // TODO: FIGURE OUT HOW ENDIANNESS WORKS...
                    int address = memory[((instruction.next_bytes[0] & 0xFF) << 8) | (instruction.next_bytes[1] & 0xFF)];
                    memory[address] = valToLoad;
                    break;
                }

                if (instruction.operand == Operand.a8) {
                    int address = instruction.next_bytes[0] + 0xFF00;
                    memory[address] = valToLoad;
                    break;
                }

                // OTHERWISE, operandToSet should be some register
                registers.setValToEnum(instruction.operandToSet, valToLoad);

                break;
            default:
                System.out.println("Not implemented!");
                break;
        }
        return registers.pc;
    }

    // if target is memory --> pass in memory address ;-;what

    public void addToA(int val){
        int a = registers.a;
        int result = val + a;
        boolean didOverflow = false;
        if (result > 0xFF){
            didOverflow = true;
            result = result & 0xFF; 
        }  
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(false);
        registers.set_f_carry(didOverflow);
        registers.set_f_halfcarry((a & 0xF) + (val & 0xF) > 0xF);
        registers.a = result;
    }

    public void subFromA(int val) {
        int a = registers.a;
        int result = a - val;
        boolean didUnderflow = false;
        if (result < 0x00) {
            didUnderflow = true;
            // java automatically converts negative ints to binary
            result = result & 0xFF;
        }
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(true);
        registers.set_f_carry(didUnderflow);
        registers.set_f_halfcarry(((a & 0xF) - (val & 0xF) & 0x10) != 0);
    }

    // val is unsigned 8
    // operant is either HL or SP
    public void add16(Operand target, int val) {
        int targetVal = registers.readValFromEnum(target);
        int result = val + targetVal;
        boolean didOverflow = false;
        if (result > 0xFFFF){
            didOverflow = true;
            result = result & 0xFFFF; 
        }

        if (target == Operand.SP){
            // left untouched for HL
            registers.set_f_zero(false); // always false for adding to sp
        }
        registers.set_f_subtract(false);
        registers.set_f_carry(didOverflow);
        registers.set_f_halfcarry((targetVal & 0xFF) + (val & 0xFF) > 0xFF);
        registers.setValToEnum(target, result);
    }

    public void loadROM(String ROMName) {
        try {
            File ROMFile = new File(ROMName);
            FileInputStream in = new FileInputStream(ROMFile);
            long size = ROMFile.length();
            byte[] contents = new byte[(int) size];
            in.read(contents);
            for (int i=0; i<size; i++){
                memory[i] = (contents[i] & 0xFF);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        } 
    }
}
