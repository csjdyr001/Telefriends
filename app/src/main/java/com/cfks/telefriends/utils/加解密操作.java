package com.cfks.telefriends.utils;

import android.app.Activity;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class 加解密操作 {
    private static char[] base64EncodeChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static byte[] base64DecodeChars = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    public static String MD5加密(String str) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] bytes = str.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            byte[] digest = messageDigest.digest();
            int length = digest.length;
            char[] cArr2 = new char[length * 2];
            int i = 0;
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                cArr2[i3] = cArr[b & 15];
                i++;
                i2 = i3 + 1;
            }
            return new String(cArr2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String SHA加密(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(str.getBytes());
            return new String(messageDigest.digest());
        } catch (Exception e) {
            return null;
        }
    }

    public static String Base64编码(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

    public static String Base64解码(String str) {
        return new String(Base64.getDecoder().decode(str));
    }

    public static String Base64编码(String str, String str2) {
        return Base64Utils.encode(str, str2);
    }

    public static String Base64解码(String str, String str2) {
        return Base64Utils.decode(str, str2);
    }

    public static String Base64编码(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        try {
            String binary = binary(str.getBytes(str2), 2);
            int i = 0;
            while (binary.length() % 24 != 0) {
                binary = binary + "0";
                i++;
            }
            for (int i2 = 0; i2 <= binary.length() - 6; i2 += 6) {
                int parseInt = Integer.parseInt(binary.substring(i2, i2 + 6), 2);
                if (parseInt == 0 && i2 >= binary.length() - i) {
                    sb.append("=");
                } else {
                    sb.append(str3.charAt(parseInt));
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String Base64解码(String str, String str2, String str3) {
        String str4 = "";
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt != '=') {
                String binaryString = Integer.toBinaryString(str3.indexOf(charAt));
                while (binaryString.length() != 6) {
                    binaryString = "0" + binaryString;
                }
                str4 = str4 + binaryString;
            }
        }
        String substring = str4.substring(0, str4.length() - (str4.length() % 8));
        byte[] bArr = new byte[substring.length() / 8];
        for (int i2 = 0; i2 < substring.length() / 8; i2++) {
            bArr[i2] = (byte) Integer.parseInt(substring.substring(i2 * 8, (i2 * 8) + 8), 2);
        }
        try {
            return new String(bArr, str2);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String BASE64编码(byte[] bArr) {
        return encode(bArr);
    }

    public static byte[] BASE64解码(String str) {
        try {
            return decode(str);
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    public static String DES加密(String str, String str2) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("GBK"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            return BASE64编码(cipher.doFinal(str.getBytes("GBK")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DES解密(String str, String str2) {
        try {
            byte[] bArr = BASE64解码(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("GBK"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(bArr), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String RC4加密(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        try {
            byte[] RC4Base = RC4Base(str.getBytes("GBK"), str2);
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            int length = RC4Base.length;
            char[] cArr2 = new char[length * 2];
            int i = 0;
            int i2 = 0;
            while (i < length) {
                byte b = RC4Base[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                cArr2[i3] = cArr[b & 15];
                i++;
                i2 = i3 + 1;
            }
            return new String(cArr2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String RC4解密(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        try {
            return new String(RC4Base(HexString2Bytes(str), str2), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Authcode加密(String str, String str2) {
        return Encode(str, str2);
    }

    public static String Authcode解密(String str, String str2) {
        return Decode(str, str2);
    }

    private static String binary(byte[] bArr, int i) {
        String bigInteger = new BigInteger(1, bArr).toString(i);
        while (bigInteger.length() % 8 != 0) {
            bigInteger = "0" + bigInteger;
        }
        return bigInteger;
    }

    private static String Encode(String str, String str2) {
        if (str == null || str2 == null) {
            return "";
        }
        try {
            String MD52 = MD52(str2);
            String MD522 = MD52(CutString(MD52, 0, 16));
            String MD523 = MD52(CutString(MD52, 16, 16));
            String RandomString = RandomString(4);
            return RandomString + encode(RC4(("0000000000" + CutString(MD52(str + MD523), 0, 16) + str).getBytes("UTF-8"), MD522 + MD52(MD522 + RandomString)));
        } catch (Exception e) {
            return "";
        }
    }

    private static String Decode(String str, String str2) {
        String str3;
        if (str == null || str2 == null) {
            return "";
        }
        try {
            String MD52 = MD52(str2);
            String MD522 = MD52(CutString(MD52, 0, 16));
            String MD523 = MD52(CutString(MD52, 16, 16));
            String str4 = MD522 + MD52(MD522 + CutString(str, 0, 4));
            String str5 = new String(RC4(decode(CutString(str, 4)), str4));
            if (CutString(str5, 10, 16).equals(CutString(MD52(CutString(str5, 26) + MD523), 0, 16))) {
                str3 = CutString(str5, 26);
            } else {
                String str6 = new String(RC4(decode(CutString(str + "=", 4)), str4));
                if (CutString(str6, 10, 16).equals(CutString(MD52(CutString(str6, 26) + MD523), 0, 16))) {
                    str3 = CutString(str6, 26);
                } else {
                    String str7 = new String(RC4(decode(CutString(str + "==", 4)), str4));
                    if (CutString(str7, 10, 16).equals(CutString(MD52(CutString(str7, 26) + MD523), 0, 16))) {
                        str3 = CutString(str7, 26);
                    } else {
                        str3 = "2";
                    }
                }
            }
            return str3;
        } catch (Exception e) {
            return "";
        }
    }

    private static String encode(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = bArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            int i2 = i + 1;
            int i3 = bArr[i] & 255;
            if (i2 == length) {
                stringBuffer.append(base64EncodeChars[i3 >>> 2]);
                stringBuffer.append(base64EncodeChars[(i3 & 3) << 4]);
                stringBuffer.append("==");
                break;
            }
            int i4 = i2 + 1;
            int i5 = bArr[i2] & 255;
            if (i4 == length) {
                stringBuffer.append(base64EncodeChars[i3 >>> 2]);
                stringBuffer.append(base64EncodeChars[((i3 & 3) << 4) | ((i5 & 240) >>> 4)]);
                stringBuffer.append(base64EncodeChars[(i5 & 15) << 2]);
                stringBuffer.append("=");
                break;
            }
            i = i4 + 1;
            int i6 = bArr[i4] & 255;
            stringBuffer.append(base64EncodeChars[i3 >>> 2]);
            stringBuffer.append(base64EncodeChars[((i3 & 3) << 4) | ((i5 & 240) >>> 4)]);
            stringBuffer.append(base64EncodeChars[((i5 & 15) << 2) | ((i6 & 192) >>> 6)]);
            stringBuffer.append(base64EncodeChars[i6 & 63]);
        }
        return stringBuffer.toString();
    }

    private static byte[] decode(String str) throws UnsupportedEncodingException {
        int i;
        byte b;
        int i2;
        byte b2;
        int i3;
        byte b3;
        byte b4;
        int length = str.length() % 4;
        if (length == 2) {
            str = str + "==";
        } else if (length == 3) {
            str = str + "=";
        }
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bytes = str.getBytes("US-ASCII");
        int length2 = bytes.length;
        int i4 = 0;
        while (i4 < length2) {
            while (true) {
                i = i4 + 1;
                b = base64DecodeChars[bytes[i4]];
                if (i >= length2 || b != -1) {
                    break;
                }
                i4 = i;
            }
            if (b != -1) {
                while (true) {
                    i2 = i + 1;
                    b2 = base64DecodeChars[bytes[i]];
                    if (i2 >= length2 || b2 != -1) {
                        break;
                    }
                    i = i2;
                }
                if (b2 == -1) {
                    break;
                }
                stringBuffer.append((char) ((b << 2) | ((b2 & 48) >>> 4)));
                while (true) {
                    i3 = i2 + 1;
                    byte b5 = bytes[i2];
                    if (b5 == 61) {
                        return stringBuffer.toString().getBytes("iso8859-1");
                    }
                    b3 = base64DecodeChars[b5];
                    if (i3 >= length2 || b3 != -1) {
                        break;
                    }
                    i2 = i3;
                }
                if (b3 == -1) {
                    break;
                }
                stringBuffer.append((char) (((b2 & 15) << 4) | ((b3 & 60) >>> 2)));
                while (true) {
                    i4 = i3 + 1;
                    byte b6 = bytes[i3];
                    if (b6 == 61) {
                        return stringBuffer.toString().getBytes("iso8859-1");
                    }
                    b4 = base64DecodeChars[b6];
                    if (i4 >= length2 || b4 != -1) {
                        break;
                    }
                    i3 = i4;
                }
                if (b4 == -1) {
                    break;
                }
                stringBuffer.append((char) (b4 | ((b3 & 3) << 6)));
            } else {
                break;
            }
        }
        return stringBuffer.toString().getBytes("iso8859-1");
    }

    private static String CutString(String str, int i, int i2) {
        int i3 = 0;
        if (i >= 0) {
            if (i2 < 0) {
                i2 *= -1;
                if (i - i2 < 0) {
                    i2 = i;
                } else {
                    i3 = i - i2;
                }
            } else {
                i3 = i;
            }
            if (i3 > str.length()) {
                return "";
            }
        } else if (i2 >= 0 && i2 + i > 0) {
            i2 += i;
        } else {
            return "";
        }
        if (str.length() - i3 < i2) {
            i2 = str.length() - i3;
        }
        return str.substring(i3, i3 + i2);
    }

    private static byte[] HexString2Bytes(String str) {
        try {
            int length = str.length();
            byte[] bArr = new byte[length / 2];
            byte[] bytes = str.getBytes("GBK");
            for (int i = 0; i < length / 2; i++) {
                bArr[i] = uniteBytes(bytes[i * 2], bytes[(i * 2) + 1]);
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte uniteBytes(byte b, byte b2) {
        return (byte) (((char) (((char) Byte.decode("0x" + new String(new byte[]{b})).byteValue()) << 4)) ^ ((char) Byte.decode("0x" + new String(new byte[]{b2})).byteValue()));
    }

    private static byte[] RC4Base(byte[] bArr, String str) {
        byte[] initKey = initKey(str);
        byte[] bArr2 = new byte[bArr.length];
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < bArr.length) {
            i3 = (i3 + 1) & 255;
            int i4 = ((initKey[i3] & 255) + i2) & 255;
            byte b = initKey[i3];
            initKey[i3] = initKey[i4];
            initKey[i4] = b;
            bArr2[i] = (byte) (initKey[((initKey[i3] & 255) + (initKey[i4] & 255)) & 255] ^ bArr[i]);
            i++;
            i2 = i4;
        }
        return bArr2;
    }

    private static byte[] initKey(String str) {
        try {
            byte[] bytes = str.getBytes("GBK");
            byte[] bArr = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr[i] = (byte) i;
            }
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            while (i2 < 256) {
                int i5 = ((bytes[i4] & 255) + (bArr[i2] & 255) + i3) & 255;
                byte b = bArr[i2];
                bArr[i2] = bArr[i5];
                bArr[i5] = b;
                i4 = (i4 + 1) % bytes.length;
                i2++;
                i3 = i5;
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String CutString(String str, int i) {
        return CutString(str, i, str.length());
    }

    private static byte[] GetKey(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr2[i2] = (byte) i2;
        }
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            int i5 = ((((bArr2[i3] + 256) % 256) + i4) + bArr[i3 % bArr.length]) % i;
            byte b = bArr2[i3];
            bArr2[i3] = bArr2[i5];
            bArr2[i5] = b;
            i3++;
            i4 = i5;
        }
        return bArr2;
    }

    private static String RandomString(int i) {
        char[] cArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int length = cArr.length;
        String str = "";
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            str = str + cArr[Math.abs(random.nextInt(length))];
        }
        return str;
    }

    private static byte[] RC4(byte[] bArr, String str) {
        if (bArr == null || str == null) {
            return null;
        }
        byte[] bArr2 = new byte[bArr.length];
        byte[] GetKey = GetKey(str.getBytes(), 256);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < bArr.length) {
            i3 = (i3 + 1) % GetKey.length;
            int length = (((GetKey[i3] + 256) % 256) + i2) % GetKey.length;
            byte b = GetKey[i3];
            GetKey[i3] = GetKey[length];
            GetKey[length] = b;
            bArr2[i] = (byte) (bArr[i] ^ toInt(GetKey[(toInt(GetKey[i3]) + toInt(GetKey[length])) % GetKey.length]));
            i++;
            i2 = length;
        }
        return bArr2;
    }

    private static String MD52(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (byte b : MessageDigest.getInstance("MD5").digest(str.getBytes())) {
                String hexString = Integer.toHexString(b & 255);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private static int toInt(byte b) {
        return (b + 256) % 256;
    }

    private static class Base64Utils {
    private static String base64Table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static String add = "=";

    public static String encode(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        try {
            String binary = binary(str.getBytes(str2), 2);
            int i = 0;
            while (binary.length() % 24 != 0) {
                binary = binary + "0";
                i++;
            }
            for (int i2 = 0; i2 <= binary.length() - 6; i2 += 6) {
                int parseInt = Integer.parseInt(binary.substring(i2, i2 + 6), 2);
                if (parseInt == 0 && i2 >= binary.length() - i) {
                    sb.append(add);
                } else {
                    sb.append(base64Table.charAt(parseInt));
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String str, String str2) {
        String str3 = "";
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt != '=') {
                String binaryString = Integer.toBinaryString(base64Table.indexOf(charAt));
                while (binaryString.length() != 6) {
                    binaryString = "0" + binaryString;
                }
                str3 = str3 + binaryString;
            }
        }
        String substring = str3.substring(0, str3.length() - (str3.length() % 8));
        byte[] bArr = new byte[substring.length() / 8];
        for (int i2 = 0; i2 < substring.length() / 8; i2++) {
            bArr[i2] = (byte) Integer.parseInt(substring.substring(i2 * 8, (i2 * 8) + 8), 2);
        }
        try {
            return new String(bArr, str2);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String binary(byte[] bArr, int i) {
        String bigInteger = new BigInteger(1, bArr).toString(i);
        while (bigInteger.length() % 8 != 0) {
            bigInteger = "0" + bigInteger;
        }
        return bigInteger;
    }
}
}