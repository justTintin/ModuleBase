package com.tintin.network.udp;

public class DataType {

	public static final String TAG = DataType.class.getSimpleName();
    
    public static byte[] charToByte(char c) {
        return new byte[] { (byte) c };
    }
    
    public static char byteToChar(byte[] bytes) {
        return (char) bytes[0];
    }
    
    public static byte[] booleanToByte(boolean b) {
        return intToByte(b ? 1 : 0);
    }
    
    public static boolean byteToBoolean(byte[] bytes) {
        return byteToInt(bytes) != 0;
    }
    
    public static byte[] stringToByte(String string, int length) {
        
        byte[] b = string.getBytes();
        byte[] bytes = new byte[length - b.length];
        return uniteByte(b, bytes);
    }
    
    public static String byteToString(byte[] bytes) {
        return new String(bytes).trim();
    }
    
    public static byte[] hexStringToByte(String string, int length) {
        byte[] b = new byte[string.length() / 2];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2),
                                           16);
        }
        byte[] bytes = new byte[length - b.length];
        return uniteByte(b, bytes);
    }
    
    public static String byteToHexString(String[] array, int start, int size) {
        StringBuffer buffer = new StringBuffer();
        for (int i = start; i < start + size; i++) {
            buffer.append(array[i]);
        }
        return buffer.toString();
    }
    
    public static byte[] arrayToByte(String[] array, int start, int length) {
        byte[] bytes = new byte[length];
        for (int i = start; i < start + length; i++) {
            bytes[i - start] = (byte) Integer.parseInt(array[i], 16);
        }
        return bytes;
    }
    
    public static byte[] uniteByte(byte[] byte1, byte[] byte2) {
        byte[] bytes = new byte[byte1.length + byte2.length];
        for (int i = 0; i < byte1.length && byte1 != null; i++) {
            bytes[i] = byte1[i];
        }
        for (int i = 0; i < byte2.length && byte2 != null; i++) {
            bytes[i + byte1.length] = byte2[i];
        }
        return bytes;
    }
    
    public static byte[] subByte(byte[] bs, int start, int length) {
        length = bs.length - start >= length ? length : bs.length - start;
        if (length < 0) {
            length = 0;
        }
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = bs[i + start];
        }
        return bytes;
    }
    
    public static String[] subHexArray(String[] array, int start, int length) {
        String[] arrays = new String[length];
        for (int i = 0; i < length && i < array.length; i++) {
            arrays[i] = array[i + start];
        }
        return arrays;
    }
    
    public static String[] uniteHexArray(String[] array1, String[] array2) {
		String[] arrays = new String[array1.length + array2.length];
		for (int i = 0; i < array1.length; i++) {
			arrays[i] = array1[i];
		}
		for (int i = 0; i < array2.length; i++) {
			arrays[i + array1.length] = array2[i];
		}
		return arrays;
	}

	public static byte[] intToByte(int x) {
		byte[] bb = new byte[4];
		bb[3] = (byte) (x >> 24);
		bb[2] = (byte) (x >> 16);
		bb[1] = (byte) (x >> 8);
		bb[0] = (byte) (x >> 0);
		return bb;
	}

	public static int byteToInt(byte[] bb) {
		if (bb.length == 1) {
			return (int) bb[0];
		} else if (bb.length == 2) {
			return ((bb[1] & 0xff) << 8) | ((bb[0] & 0xff) << 0);
		} else if (bb.length == 4) {
			return (((bb[3] & 0xff) << 24) | ((bb[2] & 0xff) << 16)
					| ((bb[1] & 0xff) << 8) | ((bb[0] & 0xff) << 0));
		}
		return -1;
	}

	// public static int byteToInt(byte[] bb) {
	// if (bb.length == 1) {
	// return byteToInt(bb[0]);
	// } else if (bb.length == 2) {
	// return byteToInt(bb[1])*256+byteToInt(bb[0]);
	// } else if (bb.length == 4) {
	// return
	// byteToInt(bb[3])*256*256*256+byteToInt(bb[2])*256*256+byteToInt(bb[1])*256+byteToInt(bb[0]);
	// }
	// return -1;
	// }

	public static byte[] longToByte(long x) {
		byte[] bb = new byte[8];
		bb[7] = (byte) (x >> 56);
		bb[6] = (byte) (x >> 48);
		bb[5] = (byte) (x >> 40);
		bb[4] = (byte) (x >> 32);
		bb[3] = (byte) (x >> 24);
		bb[2] = (byte) (x >> 16);
		bb[1] = (byte) (x >> 8);
		bb[0] = (byte) (x >> 0);
		return bb;
	}

	public static long byteToLong(byte[] bb) {
		return ((((long) bb[7] & 0xff) << 56) | (((long) bb[6] & 0xff) << 48)
				| (((long) bb[5] & 0xff) << 40) | (((long) bb[4] & 0xff) << 32)
				| (((long) bb[3] & 0xff) << 24) | (((long) bb[2] & 0xff) << 16)
				| (((long) bb[1] & 0xff) << 8) | (((long) bb[0] & 0xff) << 0));
	}

	public static int indexOf(byte[] bytes1, byte[] bytes2) {
		return indexOf(0, bytes1, bytes2);
	}

	public static int indexOf(int startIndex, byte[] bytes1, byte[] bytes2) {
		if (bytes1 == null || bytes1.length == 0 || bytes2 == null
				|| bytes2.length == 0 || bytes1.length < bytes2.length) {
			return -1;
		}
		for (int i = startIndex; i <= bytes1.length - bytes2.length; i++) {
			boolean isSame = true;
			for (int j = 0; j < bytes2.length; j++) {
				if (bytes1[i + j] != bytes2[j]) {
					isSame = false;
					break;
				}
			}
			if (isSame) {
				return i;
			}
		}
		return -1;
	}

	public static int byteToInt(byte b) {
		int result = b;
		if (b < 0) {
			result += 256;
		}
		return result;
	}

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	public static byte BitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit¥¶¿Ì
			if (byteStr.charAt(0) == '0') {// ’˝ ˝
				re = Integer.parseInt(byteStr, 2);
			} else {// ∏∫ ˝
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit¥¶¿Ì
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}
}
