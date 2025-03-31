package src;
public class Util {
    public static String hexString(int val){
        String hex = Integer.toHexString(val);
        return hex;
    }
    public static String bitString(int val){
        String bi = Integer.toBinaryString(val);
        return bi;
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

    public static byte getByteFromLong(long longVal, int byteNum) {
        return (byte) ((longVal >> (byteNum * 8)) & 0xFF);
    }

    public static long getLongFromBytes(byte byte0, byte byte1, byte byte2, byte byte3, byte byte4, byte byte5, byte byte6, byte byte7) {
        return ((long) byte0 & 0xFF) |
        (((long) byte1 & 0xFF) << 8) |
        (((long) byte2 & 0xFF) << 8*2) |
        (((long) byte3 & 0xFF) << 8*3) |
        (((long) byte4 & 0xFF) << 8*4) |
        (((long) byte5 & 0xFF) << 8*5) |
        (((long) byte6 & 0xFF) << 8*6) |
        (((long) byte7 & 0xFF) << 8*7);
    }
}
