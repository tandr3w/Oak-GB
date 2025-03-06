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
        // Test add operation
        // Test all opcodes do not error
        for (int i=0; i<0x100; i++){
            try{
                if (opcodes.byteToInstruction(i) != null){
                    int test_result = test_json("test_jsons/" + Util.hexByte(i) + ".json");
                    if (test_result == 0){
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
        return 1;
    }

    public static JSONArray readJsonArrayFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int test_json(String path){
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

            JSONObject end = testCase.getJSONObject("final");
            JSONArray endRam = end.getJSONArray("ram");
            for (int j=0; j<endRam.length(); j++){
                JSONArray ramElement = endRam.getJSONArray(j);
                int x = ramElement.getInt(0);
                int y = ramElement.getInt(1);
                if (cpu.memory[x] != y){
                    return 1;
                }
            }

            if (cpu.registers.pc != end.getInt("pc")){return 2;}
            if (cpu.registers.sp != end.getInt("sp")){return 3;}
            if (cpu.registers.a != end.getInt("a")){return 4;}
            if (cpu.registers.b != end.getInt("b")){return 5;}
            if (cpu.registers.c != end.getInt("c")){return 6;}
            if (cpu.registers.d != end.getInt("d")){return 7;}
            if (cpu.registers.e != end.getInt("e")){return 8;}
            if (cpu.registers.f != end.getInt("f")){return 9;}
            if (cpu.registers.h != end.getInt("h")){return 10;}
            if (cpu.registers.l != end.getInt("l")){return 11;}
        }
        return 0;
    }
}
