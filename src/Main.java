package src;

public class Main {
    public static void main(String[] args){
        Unit_Tests tests = new Unit_Tests();
        tests.run();        

    }
    public static void printHex(int val){
        String hex = Integer.toHexString(val);
        System.out.println(hex);
    }
}
