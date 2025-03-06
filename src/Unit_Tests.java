import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.json.*;

public class Unit_Tests {
    public CPU cpu;
    public Opcodes opcodes;

    public Unit_Tests(){
        cpu = new CPU();
        opcodes = new Opcodes();
    }

    public int run(){
        int cases_passed = 0;
        int case_count = 0;
        for (int i=0; i<0x100; i++){
            try{
                if (opcodes.byteToInstruction(i) != null){
                    case_count += 1;
                    int test_result = test_json("test_jsons/" + Util.hexByte(i) + ".json", i);
                    if (test_result == 0){
                        cases_passed += 1;
                        System.out.println("Testing passed on: " + Util.hexByte(i));
                    }
                    else {
                        System.out.println("Testing failed on: " + Util.hexByte(i) + " (error code: " + Integer.toString(test_result) + ")");
                    }
                }
            }
            catch(Exception e){
                System.out.println("Testing failed on: " + Util.hexByte(i));
                e.printStackTrace();
            }
        }
        System.out.println("\nCases passed: " + Integer.toString(cases_passed) + " / " + Integer.toString(case_count));
        return cases_passed;
    }

    public static JSONArray readJsonArrayFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int test_json(String path, int opcode){
        JSONArray jsonArray = readJsonArrayFile(path);
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject testCase = jsonArray.getJSONObject(i);
            JSONObject initial = testCase.getJSONObject("initial");
            JSONArray ram = initial.getJSONArray("ram");
            for (int j=0; j<ram.length(); j++){
                JSONArray ramElement = ram.getJSONArray(j);
                int x = ramElement.getInt(0);
                int y = ramElement.getInt(1);
                cpu.memory[x] = y;
            }

            cpu.registers.pc = initial.getInt("pc");
            cpu.registers.sp = initial.getInt("sp");
            cpu.registers.a = initial.getInt("a");
            cpu.registers.b = initial.getInt("b");
            cpu.registers.c = initial.getInt("c");
            cpu.registers.d = initial.getInt("d");
            cpu.registers.e = initial.getInt("e");
            cpu.registers.f = initial.getInt("f");
            cpu.registers.h = initial.getInt("h");
            cpu.registers.l = initial.getInt("l");
            cpu.execute(opcodes.byteToInstruction(opcode));

            JSONObject end = testCase.getJSONObject("final");
            JSONArray endRam = end.getJSONArray("ram");

            for (int j=0; j<endRam.length(); j++){
                JSONArray ramElement = endRam.getJSONArray(j);
                int x = ramElement.getInt(0);
                int y = ramElement.getInt(1);
                if (cpu.memory[x] != y){
                    System.out.println("At position: " + Integer.toString(x) + " Expected: " + Integer.toString(y) + " Found: " + Integer.toString(cpu.memory[x]));
                }
            }

            if (cpu.registers.pc != end.getInt("pc")){
                System.out.println("Expected: " + Integer.toString(end.getInt("pc")) + " Found: " + Integer.toString(cpu.registers.pc));
                return 2;}
            if (cpu.registers.sp != end.getInt("sp")){
                System.out.println("Expected: " + Integer.toString(end.getInt("sp")) + " Found: " + Integer.toString(cpu.registers.sp));
                return 3;}
            if (cpu.registers.a != end.getInt("a")){
                System.out.println("Expected: " + Integer.toString(end.getInt("a")) + " Found: " + Integer.toString(cpu.registers.a));
                return 4;}
            if (cpu.registers.b != end.getInt("b")){
                System.out.println("Expected: " + Integer.toString(end.getInt("b")) + " Found: " + Integer.toString(cpu.registers.b));
                return 5;}
            if (cpu.registers.c != end.getInt("c")){
                System.out.println("Expected: " + Integer.toString(end.getInt("c")) + " Found: " + Integer.toString(cpu.registers.c));
                return 6;}
            if (cpu.registers.d != end.getInt("d")){
                System.out.println("Expected: " + Integer.toString(end.getInt("d")) + " Found: " + Integer.toString(cpu.registers.d));
                return 7;}
            if (cpu.registers.e != end.getInt("e")){
                System.out.println("Expected: " + Integer.toString(end.getInt("e")) + " Found: " + Integer.toString(cpu.registers.e));
                return 8;}
            if (cpu.registers.f != end.getInt("f")){
                System.out.println("Expected: " + Integer.toString(end.getInt("f")) + " Found: " + Integer.toString(cpu.registers.f));
                return 9;}
            if (cpu.registers.h != end.getInt("h")){
                System.out.println("Expected: " + Integer.toString(end.getInt("h")) + " Found: " + Integer.toString(cpu.registers.h));
                return 10;}
            if (cpu.registers.l != end.getInt("l")){
                System.out.println("Expected: " + Integer.toString(end.getInt("l")) + " Found: " + Integer.toString(cpu.registers.l));
                return 11;}
        }
        return 0;
    }
}
