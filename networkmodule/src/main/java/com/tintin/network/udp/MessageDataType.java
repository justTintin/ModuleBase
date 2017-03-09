package com.tintin.network.udp;

/**
 * 数据格式转换
 */
public class MessageDataType
{
    public static byte[] subByte(byte[] bytes, int start, int length)
    {
        return DataType.subByte(bytes, start, length);
    }

    public static byte[] uniteByte(byte[] bytes1, byte[] bytes2)
    {
        return DataType.uniteByte(bytes1, bytes2);
    }

    public static int wordToInt(byte[] bytes)
    {

        return DataType.byteToInt(bytes);
    }

    public static long dwordToInt(byte[] bytes)
    {
        int num = DataType.byteToInt(bytes);
        if (num < 0)
        {
            return 4294967296L + num;
        }
        return num;
    }

    // private static byte[] reversalBytes(byte[] bytes) {
    // byte[] newBytes = new byte[bytes.length];
    // int length = bytes.length;
    // for (int i = 0; i < length; i++) {
    // newBytes[length - i - 1] = bytes[i];
    // }
    // return bytes;
    // }

    public static int indexof(byte[] bytes1, byte[] bytes2)
    {
        return DataType.indexOf(bytes1, bytes2);
    }

    public static byte getCheckCode(byte[] bytes)
    {
        byte b = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            b = (byte) (b ^ bytes[i]);
        }
        return b;
    }

    public static byte[] addCheckCode(byte[] bytes)
    {
        byte b = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            b = (byte) (b ^ bytes[i]);
        }
        byte[] newBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
        newBytes[bytes.length] = b;
        return newBytes;
    }

    public static byte[] intToWord(int num)
    {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (num >> 8);
        bytes[0] = (byte) (num >> 0);
        return bytes;
    }

    public static byte[] intToDword(long num)
    {
        num = num > 2147483647 ? num - 4294967296L : num;
        return DataType.intToByte((int) num);
    }

    public static void copyBytes(byte[] bytes1, byte[] bytes2)
    {
        System.arraycopy(bytes2,
                0,
                bytes1,
                0,
                bytes1.length < bytes2.length ? bytes1.length : bytes2.length);

    }

    public static int byteToInt(byte b)
    {
        int result = b;
        if (b < 0)
        {
            result += 256;
        }
        return result;
    }

    /**
     * float转换byte
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putFloat(byte[] bb, float x, int index) {
        // byte[] b = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Integer(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * double转换byte
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putDouble(byte[] bb, double x, int index) {
        // byte[] b = new byte[8];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Long(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }


}