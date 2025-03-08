import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

public class CPU {
    Registers registers;
    int[] memory;
    Opcodes opcodes;
        // TODO store flags in memory
    boolean lowPowerMode;
    boolean halted;
    boolean interrupts;

    public CPU(Opcodes opcodes, Memory memory){
        this.memory = memory.memoryArray; // 65536 bytes
        registers = new Registers(this);
        this.opcodes = opcodes;
        lowPowerMode = false;
        halted = false;
        interrupts = false;

        loadROM("ROMs/snake.gb"); // FIXME
    }

    public void setNextBytes(Instruction instruction){
        if (instruction.num_bytes > 1){
            instruction.next_bytes = new int[instruction.num_bytes - 1];
            for (int i=0; i<instruction.num_bytes-1; i++){
                instruction.next_bytes[i] = memory[(registers.pc + i + 1) & 0xFFFF]; // &0xFFFF to account for overflow
            }
        }
    }

    public int executePrefixed(Instruction instruction){
        setNextBytes(instruction);
        registers.pc += instruction.num_bytes - 1;
        registers.pc &= 0xFFFF; // overflow
        
        switch (instruction.operation){
            case Operation.SWAP:
                swap(instruction.operand);
                break;
            case Operation.BIT:
                int val = registers.readValFromEnum(instruction.operand);
                if (instruction.operandToSet == Operand.BIT0){
                    registers.set_f_zero((val & 0b00000001) == 0);
                }
                if (instruction.operandToSet == Operand.BIT1){
                    registers.set_f_zero((val & 0b00000010) == 0);
                }
                if (instruction.operandToSet == Operand.BIT2){
                    registers.set_f_zero((val & 0b00000100) == 0);
                }
                if (instruction.operandToSet == Operand.BIT3){
                    registers.set_f_zero((val & 0b00001000) == 0);
                }
                if (instruction.operandToSet == Operand.BIT4){
                    registers.set_f_zero((val & 0b00010000) == 0);
                }
                if (instruction.operandToSet == Operand.BIT5){
                    registers.set_f_zero((val & 0b00100000) == 0);
                }
                if (instruction.operandToSet == Operand.BIT6){
                    registers.set_f_zero((val & 0b01000000) == 0);
                }
                if (instruction.operandToSet == Operand.BIT7){
                    registers.set_f_zero((val & 0b10000000) == 0);
                }
                registers.set_f_subtract(false);
                registers.set_f_halfcarry(true);
                break;
            case Operation.SET:
                int valToSet = registers.readValFromEnum(instruction.operand);
                if (instruction.operandToSet == Operand.BIT0){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b00000001));
                }
                if (instruction.operandToSet == Operand.BIT1){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b00000010));
                }
                if (instruction.operandToSet == Operand.BIT2){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b00000100));
                }
                if (instruction.operandToSet == Operand.BIT3){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b00001000));
                }
                if (instruction.operandToSet == Operand.BIT4){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b00010000));
                }
                if (instruction.operandToSet == Operand.BIT5){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b00100000));
                }
                if (instruction.operandToSet == Operand.BIT6){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b01000000));
                }
                if (instruction.operandToSet == Operand.BIT7){
                    registers.setValToEnum(instruction.operand, (valToSet | 0b10000000));
                }
                break;
            case Operation.RES:
                int valToReset = registers.readValFromEnum(instruction.operand);
                if (instruction.operandToSet == Operand.BIT0){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b11111110));
                }
                if (instruction.operandToSet == Operand.BIT1){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b11111101));
                }
                if (instruction.operandToSet == Operand.BIT2){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b11111011));
                }
                if (instruction.operandToSet == Operand.BIT3){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b11110111));
                }
                if (instruction.operandToSet == Operand.BIT4){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b11101111));
                }
                if (instruction.operandToSet == Operand.BIT5){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b11011111));
                }
                if (instruction.operandToSet == Operand.BIT6){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b10111111));
                }
                if (instruction.operandToSet == Operand.BIT7){
                    registers.setValToEnum(instruction.operand, (valToReset & 0b01111111));
                }
                break;
            case Operation.RLC:
                RLC(instruction.operand);
                break;
            case Operation.RL:
                RL(instruction.operand);
                break;
            case Operation.RRC:
                RRC(instruction.operand);
                break;
            case Operation.RR:
                RR(instruction.operand);
                break;
            case Operation.SLA:
                SLA(instruction.operand);
                break;
            case Operation.SRA:
                SRA(instruction.operand);
                break;
            case Operation.SRL:
                SRL(instruction.operand);
                break;

            default:
                System.out.println("Attempted run of operation that has not been implemented: " + instruction.operation.name());
                break;
        }

        return registers.pc;
    }

    public int execute(Instruction instruction){
        // Load correct number of bytes
        setNextBytes(instruction);
        registers.pc += instruction.num_bytes;
        registers.pc &= 0xFFFF; // overflow
        
        switch (instruction.operation){
            case Operation.PREFIX:
                int nextByte = instruction.next_bytes[0];
                executePrefixed(opcodes.prefixOpcodesArray[nextByte]);
                break;
            case Operation.ADD:
                if (instruction.operand == Operand.n8){
                    addToA(instruction.next_bytes[0]);
                    break;
                }
                if (instruction.operand == Operand.e8){
                    addSignedTo16(Operand.SP, (byte) instruction.next_bytes[0]);
                    break;
                }
                // otherwise, add from another register
                addToA(registers.readValFromEnum(instruction.operand));
                break;
                
            case Operation.ADD16:
                if (instruction.operandToSet == Operand.SP && instruction.operand == Operand.e8){
                    add16(Operand.SP, (byte) instruction.next_bytes[0]);
                    break;
                }
                // Add to HL
                add16(Operand.HL, registers.readValFromEnum(instruction.operand));
                break;

            case Operation.ADC:
                if (instruction.operand == Operand.n8){
                    adcToA(instruction.next_bytes[0]);
                    break;   
                }
                adcToA(registers.readValFromEnum(instruction.operand));
                break;


            case Operation.SUB:
                if (instruction.operand == Operand.n8){
                    subFromA(instruction.next_bytes[0]);
                    break;
                }
                subFromA(registers.readValFromEnum(instruction.operand));
                break;

            case Operation.SBC:
                if (instruction.operand == Operand.n8){
                    sbcFromA(instruction.next_bytes[0]);
                    break;
                }
                sbcFromA(registers.readValFromEnum(instruction.operand));
                break;
            
            case Operation.AND:
                if (instruction.operand == Operand.n8){
                    andA(instruction.next_bytes[0]);
                    break;   
                }
                andA(registers.readValFromEnum(instruction.operand));
                break;

            case Operation.CP:
                if (instruction.operand == Operand.n8){
                    cpToA(instruction.next_bytes[0]);
                    break;   
                }
                cpToA(registers.readValFromEnum(instruction.operand));
                break;      
            
            case Operation.OR:
                if (instruction.operand == Operand.n8){
                    orA(instruction.next_bytes[0]);
                    break;   
                }
                orA(registers.readValFromEnum(instruction.operand));
                break;
            
            case Operation.XOR:
                if (instruction.operand == Operand.n8){
                    xorA(instruction.next_bytes[0]);
                    break;   
                }
                xorA(registers.readValFromEnum(instruction.operand));
                break;    
            
            case Operation.INC:
                inc(instruction.operandToSet);
                break;     
            
            case Operation.DEC:
                dec(instruction.operandToSet);
                break;

            case Operation.INC16:
                inc16(instruction.operandToSet);
                break;

            case Operation.DEC16:
                dec16(instruction.operandToSet);
                break;

            case Operation.CPL:
                registers.a ^= 0xFF; // Flip all bits
                registers.set_f_subtract(true);
                registers.set_f_halfcarry(true);
                break;
            
            case Operation.CCF:
                registers.set_f_carry(registers.get_f_carry() == 0);
                registers.set_f_subtract(false);
                registers.set_f_halfcarry(false);
                break;

            case Operation.SCF:
                registers.set_f_carry(true);
                registers.set_f_subtract(false);
                registers.set_f_halfcarry(false);
                break;  
            
            case Operation.DAA:
                int adjustment = 0;
                if (registers.get_f_subtract() == 1){
                    adjustment += registers.get_f_halfcarry() * 0x6;
                    adjustment += registers.get_f_carry() * 0x60;
                    registers.a -= adjustment;
                    registers.a &= 0xFF;
                }
                else {
                    if (registers.get_f_halfcarry() == 1 || ((registers.a & 0xF) > 0x9)){
                        adjustment += 0x6;
                    }
                    if (registers.get_f_carry() == 1 || (registers.a > 0x99)){
                        adjustment += 0x60;
                        registers.set_f_carry(true);
                    }
                    registers.a += adjustment;
                    registers.a &= 0xFF;
                }
                registers.set_f_zero(registers.a == 0);
                registers.set_f_halfcarry(false);
                break;

            case Operation.LD: // FOR 8-BIT LOAD OPERATIONS
                // instruction.operand is the value that will be loaded
                // get the value that needs to be loaded first
                int valToLoad;
                if (instruction.operand == Operand.a16){
                    valToLoad = memory[((instruction.next_bytes[1] & 0xFF) << 8) | (instruction.next_bytes[0] & 0xFF)];
                }
                
                else if (instruction.operand == Operand.a8) {
                    valToLoad = memory[instruction.next_bytes[0] + 0xFF00];
                }
                
                else if (instruction.operand == Operand.n8) {
                    valToLoad = instruction.next_bytes[0];
                }

                // otherwise, the operant is a register
                else {;
                    valToLoad = registers.readValFromEnum(instruction.operand);
                }
                
                if (instruction.operandToSet == Operand.a16) {
                    // TODO: FIGURE OUT HOW ENDIANNESS WORKS...
                    int address = ((instruction.next_bytes[1] & 0xFF) << 8) | (instruction.next_bytes[0] & 0xFF);
                    memory[address] = valToLoad;
                    break;
                }

                if (instruction.operandToSet == Operand.a8) {
                    int address = instruction.next_bytes[0] + 0xFF00;
                    memory[address] = valToLoad;
                    break;
                }

                // OTHERWISE, operandToSet should be some register
                registers.setValToEnum(instruction.operandToSet, valToLoad);

                break;
            // TODO: check if all cominations of operands are accounted for
            case Operation.LD16:
                int twoByteVal;
                // There are no LD16 operations that have an n8 operand
                if (instruction.operand == Operand.n16) {
                    // LITTLE ENDIAN SMILEY FACE HAHAHAHAHAHAHAHAH
                    twoByteVal = ((instruction.next_bytes[1] & 0xFF) << 8) | (instruction.next_bytes[0] & 0xFF);
                }
                
                else if (instruction.operand == Operand.SPe8) {
                    twoByteVal = returnAddSignedTo16(Operand.SP, (byte) instruction.next_bytes[0]);
                }
                
                // Otherwise, the operand is a 16-bit register
                else {;
                    twoByteVal = registers.readValFromEnum(instruction.operand);
                }
                
                if (instruction.operandToSet == Operand.a16) {
                    // LITTLE ENDIAN
                    int address = ((instruction.next_bytes[1] & 0xFF) << 8) | (instruction.next_bytes[0] & 0xFF);
                    memory[address] = (twoByteVal & 0xFF);
                    memory[(address+1) & 0xFFFF] = (twoByteVal & 0xFF00) >> 8; 

                    break;
                }

                // operandToSet should be a register
                registers.setValToEnum(instruction.operandToSet, twoByteVal);
                break;
                
        
            // 8-bit
            case Operation.LDI:
                if (instruction.operand == Operand.MemHL) {
                    int val = memory[registers.get_hl()];
                    inc16(Operand.HL);

                    // operandToSet is always A
                    registers.setValToEnum(instruction.operandToSet, val);
                    break;
                }
                if (instruction.operandToSet == Operand.MemHL) {
                    // instruction.operand should always be A
                    memory[registers.get_hl()] = registers.readValFromEnum(instruction.operand);
                    inc16(Operand.HL);

                    break;
                }
                break;
                
            case Operation.LDD:
                if (instruction.operand == Operand.MemHL) {
                    int val = memory[registers.get_hl()];
                    dec16(Operand.HL);

                    // operandToSet is always A
                    registers.setValToEnum(instruction.operandToSet, val);
                    break;
                }
                if (instruction.operandToSet == Operand.MemHL) {
                    // instruction.operand should always be A
                    memory[registers.get_hl()] = registers.readValFromEnum(instruction.operand);
                    dec16(Operand.HL);

                    break;
                }

            case Operation.LDH:
                if (instruction.operand == Operand.A && instruction.operandToSet == Operand.C) {
                    int address = registers.c + 0xFF00;
                    memory[address] = registers.a;
                    break;
                }
                if (instruction.operand == Operand.C && instruction.operandToSet == Operand.A) {
                    int val = memory[registers.c + 0xFF00];
                    registers.a = val;
                    break;
                }
                if (instruction.operand == Operand.A && instruction.operandToSet == Operand.a8) { 
                    int address = instruction.next_bytes[0] + 0xFF00;
                    memory[address] = registers.a;
                    break;
                }
                if (instruction.operand == Operand.a8 && instruction.operandToSet == Operand.A) {
                    int val = memory[instruction.next_bytes[0] + 0xFF00];
                    registers.a = val;
                    break;
                }
                break;
            case Operation.RLCA:
                int firstCircularBit = (registers.a & 0b10000000) >> 7;
                registers.a = (registers.a << 1) & 0xFF;
                registers.a |= firstCircularBit;
                registers.set_f_zero(false);
                registers.set_f_halfcarry(false);
                registers.set_f_subtract(false);
                registers.set_f_carry(firstCircularBit == 1);
                break;

            case Operation.RLA:
                int firstBit = (registers.a & 0b10000000) >> 7;
                registers.a = (registers.a << 1) & 0xFF;
                
                registers.a = registers.a | registers.get_f_carry();

                registers.set_f_zero(false);
                registers.set_f_halfcarry(false);
                registers.set_f_subtract(false);
                registers.set_f_carry(firstBit == 1);
                break;

            case Operation.RRCA:
                int lastCircularBit = registers.a & 1;
                registers.a = (registers.a >> 1) & 0xFF;
                registers.a |= lastCircularBit << 7;
                registers.set_f_zero(false);
                registers.set_f_halfcarry(false);
                registers.set_f_subtract(false);
                registers.set_f_carry(lastCircularBit == 1);
                break;
            case Operation.RCA:
                int lastBit = registers.a & 1;
                registers.a = (registers.a >> 1) & 0xFF;
                registers.a |= (registers.get_f_carry() << 7);
                registers.set_f_zero(false);
                registers.set_f_halfcarry(false);
                registers.set_f_subtract(false);
                registers.set_f_carry(lastBit == 1);
                break;

            case Operation.NOP:
                break;

            case Operation.JP:
                if (instruction.operand == Operand.a16){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    jumpTo(addr);
                }
                if (instruction.operand == Operand.JumpNZ){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_zero() == 0){
                        jumpTo(addr);
                    }
                }
                if (instruction.operand == Operand.JumpNC){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_carry() == 0){
                        jumpTo(addr);
                    }
                }
                if (instruction.operand == Operand.JumpC){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_carry() == 1){
                        jumpTo(addr);
                    }
                }
                if (instruction.operand == Operand.JumpZ){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_zero() == 1){
                        jumpTo(addr);
                    }
                }
                if (instruction.operand == Operand.MemHL){
                    jumpTo(registers.get_hl());
                }
                break;
            
            case Operation.JR:
                if (instruction.operand == Operand.e8 && instruction.operandToSet == null){
                    int toAdd = (byte) (instruction.next_bytes[0] & 0xFF);
                    jumpTo((registers.pc + toAdd) & 0xFFFF);
                }
                if (instruction.operandToSet == Operand.JumpNZ){
                    int toAdd = (byte) (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_zero() == 0){
                        jumpTo((registers.pc + toAdd) & 0xFFFF);
                    }
                }
                if (instruction.operandToSet == Operand.JumpNC){
                    int toAdd = (byte) (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_carry() == 0){
                        jumpTo((registers.pc + toAdd) & 0xFFFF);
                    }
                }
                if (instruction.operandToSet == Operand.JumpC){
                    int toAdd = (byte) (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_carry() == 1){
                        jumpTo((registers.pc + toAdd) & 0xFFFF);
                    }
                }
                if (instruction.operandToSet == Operand.JumpZ){
                    int toAdd = (byte) (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_zero() == 1){
                        jumpTo((registers.pc + toAdd) & 0xFFFF);
                    }
                }
                break;

            case Operation.CALL:
                if (instruction.operand == Operand.a16 && instruction.operandToSet == null){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    add16ToStack(registers.pc);
                    jumpTo(addr);
                }
                if (instruction.operandToSet == Operand.JumpNZ){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_zero() == 0){
                        add16ToStack(registers.pc);
                        jumpTo(addr);
                    }
                }
                if (instruction.operandToSet == Operand.JumpNC){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_carry() == 0){
                        add16ToStack(registers.pc);
                        jumpTo(addr);
                    }
                }
                if (instruction.operandToSet == Operand.JumpC){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_carry() == 1){
                        add16ToStack(registers.pc);
                        jumpTo(addr);
                    }
                }
                if (instruction.operandToSet == Operand.JumpZ){
                    int addr = (instruction.next_bytes[1] & 0xFF) << 8 | (instruction.next_bytes[0] & 0xFF);
                    if (registers.get_f_zero() == 1){
                        add16ToStack(registers.pc);
                        jumpTo(addr);
                    }
                }
                break;
            
            case Operation.RST:
                add16ToStack(registers.pc);
                if (instruction.operand == Operand.RST00){jumpTo(0x00);}
                if (instruction.operand == Operand.RST08){jumpTo(0x08);}
                if (instruction.operand == Operand.RST10){jumpTo(0x10);}
                if (instruction.operand == Operand.RST18){jumpTo(0x18);}
                if (instruction.operand == Operand.RST20){jumpTo(0x20);}
                if (instruction.operand == Operand.RST28){jumpTo(0x28);}
                if (instruction.operand == Operand.RST30){jumpTo(0x30);}
                if (instruction.operand == Operand.RST38){jumpTo(0x38);}
                break;
            
            case Operation.RET:
                if (instruction.operand == null){
                    jumpTo(pop16FromStack());
                }
                if (instruction.operand == Operand.JumpNZ){
                    if (registers.get_f_zero() == 0){
                        jumpTo(pop16FromStack());
                    }
                }
                if (instruction.operand == Operand.JumpNC){
                    if (registers.get_f_carry() == 0){
                        jumpTo(pop16FromStack());
                    }
                }
                if (instruction.operand == Operand.JumpC){
                    if (registers.get_f_carry() == 1){
                        jumpTo(pop16FromStack());
                    }
                }
                if (instruction.operand == Operand.JumpZ){
                    if (registers.get_f_zero() == 1){
                        jumpTo(pop16FromStack());
                    }
                }
                break;

            case Operation.PUSH:
                add16ToStack(registers.readValFromEnum(instruction.operand));
                break;

            case Operation.POP:
                if (instruction.operand == Operand.AF){
                    int val = pop16FromStack();
                    registers.a = (val & 0xFF00) >> 8;
                    int lowerByte = val & 0xFF;
                    registers.set_f_zero((lowerByte & 0b10000000) != 0);
                    registers.set_f_subtract((lowerByte & 0b01000000) != 0);
                    registers.set_f_halfcarry((lowerByte & 0b00100000) != 0);
                    registers.set_f_carry((lowerByte & 0b00010000) != 0);
                }
                else {
                    registers.setValToEnum(instruction.operand, pop16FromStack());
                }
                break;

            default:
                System.out.println("Attempted run of operation that has not been implemented: " + instruction.operation.name());
                break;

        }
        return registers.pc;
    }

    public void jumpTo(int val){
        registers.pc = val;
    }
    // if target is memory --> pass in memory address ;-;what

    public void add16ToStack(int val){
        registers.sp--;
        registers.sp &= 0xFFFF;
        memory[registers.sp] = (val & 0xFF00) >> 8;
        registers.sp--;
        registers.sp &= 0xFFFF;
        memory[registers.sp] = val & 0xFF;
    }

    public int pop16FromStack(){
        int val = memory[registers.sp];
        registers.sp++;
        registers.sp &= 0xFFFF;
        val |= memory[registers.sp] << 8;
        registers.sp++;
        registers.sp &= 0xFFFF;
        return val;
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
        registers.a = result;
    }

    public void sbcFromA(int val) {
        int a = registers.a;
        int result = a - val;
        int prevCarryBit = (registers.f & 0b00010000) >> 4;
        result -= prevCarryBit;
        boolean didUnderflow = false;
        if (result < 0x00) {
            didUnderflow = true;
            // java automatically converts negative ints to binary
            result = result & 0xFF;
        }
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(true);
        registers.set_f_carry(didUnderflow);
        registers.set_f_halfcarry(((a & 0xF) - (val & 0xF) - (prevCarryBit & 0xF) & 0x10) != 0);
        registers.a = result;
    }

    // Same as subtraction but only set the flag bits
    public void cpToA(int val) {
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

    public void inc(Operand target){
        int targetVal = registers.readValFromEnum(target);
        int result = targetVal + 1;
        if (result > 0xFF){
            result = result & 0xFF;
        }
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry((targetVal & 0xF) + (1 & 0xF) > 0xF);
        registers.setValToEnum(target, result);
    }

    public void dec(Operand target){
        int targetVal = registers.readValFromEnum(target);
        int result = targetVal - 1;
        if (result < 0x00){
            result = result & 0xFF;
        }
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(true);
        registers.set_f_halfcarry(((targetVal & 0xF) - (1 & 0xF) & 0x10) != 0);
        registers.setValToEnum(target, result);
    }
    
    public void inc16(Operand target){
        int targetVal = registers.readValFromEnum(target);
        int result = targetVal + 1;
        if (result > 0xFFFF){
            result = result & 0xFFFF;
        }
        registers.setValToEnum(target, result);
    }
    public void dec16(Operand target){
        int targetVal = registers.readValFromEnum(target);
        int result = targetVal - 1;
        if (result < 0x0000){
            result = result & 0xFFFF;
        }
        registers.setValToEnum(target, result);
    }

    public void addSignedTo16(Operand target, byte val){
        int targetVal = registers.readValFromEnum(target);
        int result = targetVal + val;

        if (result < 0x00) {
            result = result & 0xFFFF;
        }

        if (result > 0xFFFF){
            result = result & 0xFFFF;
        }

        registers.set_f_zero(false);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry((targetVal & 0xF) + (val & 0xF) > 0xF);
        registers.set_f_carry((targetVal & 0xFF) + (val & 0xFF) > 0xFF);
        registers.setValToEnum(target, result);
    }


    public int returnAddSignedTo16(Operand target, byte val){
        int targetVal = registers.readValFromEnum(target);
        int result = targetVal + val;

        if (result < 0x00) {
            result = result & 0xFFFF;
        }

        if (result > 0xFFFF){
            result = result & 0xFFFF;
        }

        registers.set_f_zero(false);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry((targetVal & 0xF) + (val & 0xF) > 0xF);
        registers.set_f_carry((targetVal & 0xFF) + (val & 0xFF) > 0xFF);
        return result;
    }
    // val is unsigned 8-bit
    // target is either HL or SP
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
        registers.set_f_halfcarry((targetVal & 0x0FFF) + (val & 0x0FFF) > 0x0FFF);
        registers.setValToEnum(target, result);
    }

    public void adcToA(int val){
        int a = registers.a;
        int result = val + a;
        int prevCarryBit = (registers.f & 0b00010000) >> 4;
        result += prevCarryBit;
        boolean didOverflow = false;
        if (result > 0xFF){
            didOverflow = true;
            result = result & 0xFF; 
        }
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry((a & 0xF) + (val & 0xF) + (prevCarryBit & 0xF) > 0xF);
        registers.set_f_carry(didOverflow);
        registers.a = result;
    }

    public void andA(int val){
        int result = registers.a & val;
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry(true);
        registers.set_f_carry(false);
        registers.a = result;
    }

    public void orA(int val){
        int result = registers.a | val;
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry(false);
        registers.set_f_carry(false);
        registers.a = result;
    }

    public void xorA(int val) {
        int result = registers.a ^ val;
        registers.set_f_zero(result == 0);
        registers.set_f_subtract(false);
        registers.set_f_halfcarry(false);
        registers.set_f_carry(false);
        registers.a = result;
    }



    
    // TODO: NEED TO UPDATE ZERO FLAG
    public void RLC(Operand target) {

        int firstCircularBit = (registers.readValFromEnum(target) & 0b10000000) >> 7;
        int shifted = (registers.readValFromEnum(target) << 1) & 0xFF;
        registers.setValToEnum(target, shifted | firstCircularBit);
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstCircularBit == 1);
    }

    public void RL(Operand target) {
        int firstBit = (registers.readValFromEnum(target) & 0b10000000) >> 7;
        int shifted = (registers.readValFromEnum(target) << 1) & 0xFF;
        registers.setValToEnum(target, shifted | registers.get_f_carry());
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstBit == 1);
    }

    public void RRC(Operand target) {
        int firstCircularBit = (registers.readValFromEnum(target) & 0b00000001);
        int shifted = (registers.readValFromEnum(target) >> 1);
        registers.setValToEnum(target, shifted | (firstCircularBit << 7));
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstCircularBit == 1);
    }

    public void RR(Operand target) {
        int firstBit = (registers.readValFromEnum(target) & 0b00000001);
        int shifted = (registers.readValFromEnum(target) >> 1);
        registers.setValToEnum(target, shifted | (registers.get_f_carry() << 7));
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstBit == 1);
    }

    public void SLA(Operand target) {
        int firstBit = (registers.readValFromEnum(target) & 0b10000000) >> 7;
        int shifted = (registers.readValFromEnum(target) << 1);
        registers.setValToEnum(target, shifted & 0xFF); // handle overflow
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstBit == 1);
    }

    public void SRA(Operand target) {
        int firstBit = (registers.readValFromEnum(target) & 0b00000001);
        int bitSeven = (registers.readValFromEnum(target) & 0b10000000);
        int shifted = (registers.readValFromEnum(target) >> 1);
        registers.setValToEnum(target, shifted | bitSeven);
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstBit == 1);
    }

    public void SRL(Operand target) {
        int firstBit = (registers.readValFromEnum(target) & 0b00000001);
        int shifted = (registers.readValFromEnum(target) >> 1);
        registers.setValToEnum(target, shifted);
        registers.set_f_zero(registers.readValFromEnum(target) == 0);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
        registers.set_f_carry(firstBit == 1);
    }

    public void swap(Operand target){
        int val = registers.readValFromEnum(target);
        int lowerNibble = val & 0b00001111;
        int upperNibble = val & 0b11110000;
        int result = (lowerNibble << 4) | (upperNibble >> 4);
        registers.setValToEnum(target, result);
        registers.set_f_zero(result == 0);
        registers.set_f_carry(false);
        registers.set_f_halfcarry(false);
        registers.set_f_subtract(false);
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

    private void setHardwareRegisters(){

    }
}
