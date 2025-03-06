public class Util {
    public static String hexString(int val){
        String hex = Integer.toHexString(val);
        return hex;
    }
    public static String hexByte(int val){
        String hex = Integer.toHexString(val);
        if (hex.length() == 1){
            hex = "0" + hex;
        }
        return hex;
    }
}
