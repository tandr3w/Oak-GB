package src;
import java.io.File;
import java.io.FileNotFoundException;
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
            case Instruction.Operation.SUB:
                System.out.println("SUBTRACTING");
                break;
        }
        return registers.pc;
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
