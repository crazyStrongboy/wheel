package github.com.mars_jun.utils;

import java.io.UnsupportedEncodingException;

public class UrlUtils {
    public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    public static String encodeURIComponent(String input) {
        if ((input == null) || ("".equals(input))) {
            return input;
        }

        int l = input.length();
        StringBuilder o = new StringBuilder(l * 3);
        try {
            for (int i = 0; i < l; i++) {
                String e = input.substring(i, i + 1);
                if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()".indexOf(e) == -1) {
                    byte[] b = e.getBytes("utf-8");
                    o.append(getHex(b));
                } else {
                    o.append(e);
                }
            }
            return o.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }

    private static String getHex(byte[] buf) {
        StringBuilder o = new StringBuilder(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            int n = buf[i] & 0xFF;
            o.append("%");
            if (n < 16) {
                o.append("0");
            }
            o.append(Long.toString(n, 16).toUpperCase());
        }
        return o.toString();
    }

    public static String decodeURIComponent(String encodedURI) {
        StringBuffer buffer = new StringBuffer();
        int sumb = 0;
        int i = 0;
        for (int more = -1; i < encodedURI.length(); i++) {
            char actualChar = encodedURI.charAt(i);
            int bytePattern;
            switch (actualChar) {
                case '%':
                    i++;
                    actualChar = encodedURI.charAt(i);
                    int hb = (Character.isDigit(actualChar) ? actualChar - '0' :
                            '\n' + Character.toLowerCase(actualChar) - 97) & 0xF;
                    i++;
                    actualChar = encodedURI.charAt(i);
                    int lb = (Character.isDigit(actualChar) ? actualChar - '0' :
                            '\n' + Character.toLowerCase(actualChar) - 97) & 0xF;
                    bytePattern = hb << 4 | lb;
                    break;
                case '+':
                    bytePattern = 32;
                    break;
                default:
                    bytePattern = actualChar;
            }

            if ((bytePattern & 0xC0) == 128) {
                sumb = sumb << 6 | bytePattern & 0x3F;
                more--;
                if (more == 0)
                    buffer.append((char) sumb);
            } else if ((bytePattern & 0x80) == 0) {
                buffer.append((char) bytePattern);
            } else if ((bytePattern & 0xE0) == 192) {
                sumb = bytePattern & 0x1F;
                more = 1;
            } else if ((bytePattern & 0xF0) == 224) {
                sumb = bytePattern & 0xF;
                more = 2;
            } else if ((bytePattern & 0xF8) == 240) {
                sumb = bytePattern & 0x7;
                more = 3;
            } else if ((bytePattern & 0xFC) == 248) {
                sumb = bytePattern & 0x3;
                more = 4;
            } else {
                sumb = bytePattern & 0x1;
                more = 5;
            }
        }
        return buffer.toString();
    }
}