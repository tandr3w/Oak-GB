import java.util.HexFormat;

public class Main {
    public static void main(String[] args){
        printHex(255);
    }
    public static void printHex(int val){
        String hex = Integer.toHexString(val);
        System.out.println(hex);
    }
}
