package src;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

public class CPU {
    Registers registers;
    int[] memory;

    public CPU(){
        registers = new Registers();
        memory = new int[0xFFFF]; // 65536 bytes
        loadROM("ROMs/snake.gb");
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
            case Instruction.Operation.ADD16:
                if (instruction.operandToSet == Operand.SP && instruction.operand == Operand.e8){
                    add16(Operand.SP, memory[registers.pc]);
                    registers.pc += 1;
                }
                break;
            case Instruction.Operation.SUB:
                System.out.println("SUBTRACTING");
                break;
            case Instruction.Operation.LD:
                // TODO:code load
                break;
            default:
                System.out.println("Not implemented!");
                break;
        }
        return registers.pc;
    }


    // 16 bit load
    // 8 bit load
    // 16 bit in to A
    public void load(Operand target, int val) {
        registers.setValToEnum(target, val);
    }
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
        for (int i=0x100; i<0x150;i++) {
            System.out.println(memory[i]);
        }
    }
}
