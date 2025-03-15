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

    public static int getIthBit(int byteVal, int bitNum){
        return (byteVal >> bitNum) & 1;
    }

    public static int setBit(int byteVal, int bitNum, boolean val){
        if (val){
            return byteVal | (1 << bitNum);
        }
        else {
            return byteVal & ~(1 << bitNum);
        }
    }
}
