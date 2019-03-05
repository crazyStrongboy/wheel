package github.com.mars_jun.utils;

import java.io.PrintStream;

public class PrintHelper {
    public static final short FOREGROUND_BLACK = 0;
    public static final short FOREGROUND_BLUE = 1;
    public static final short FOREGROUND_GREEN = 2;
    public static final short FOREGROUND_RED = 4;
    public static final short FOREGROUND_WHITE = 7;
    public static final short FOREGROUND_INTENSITY = 8;
    public static final short BACKGROUND_BLUE = 16;
    public static final short BACKGROUND_GREEN = 32;
    public static final short BACKGROUND_RED = 64;
    public static final short BACKGROUND_INTENSITY = 128;
    public static final String LOG_SET = "[SET]    ";
    public static final String LOG_READ = "[READ]   ";
    public static final String LOG_EMPTY = "         ";
    public static final String LOG_OWNED = "        \032";
    public static final String LOG_THREAD = "[THREAD] ";

    public static void cls() {
    }

    public static void print(String str) {
        System.out.print(str);
    }

    public static void println(String str) {
        System.out.println(str);
    }

    public static void println(String Str, short ForeColor, short BackColor) {
        System.out.println(Str);
    }

    public static void println(String Str, short ForeColor, short BackColor, short x, short y) {
        System.out.println(Str);
    }

    public static void println(String Str, short[] xy) {
        System.out.println(Str);
    }

    public static void print(String Str, short ForeColor, short BackColor) {
        System.out.print(Str);
    }

    public static void print(String Str, short ForeColor, short BackColor, short x, short y) {
        System.out.print(Str);
    }

    public static void print(String Str, short[] xy) {
        System.out.print(Str);
    }

    public static void error(String Str) {
        System.out.println(Str);
    }

    public static void log(String type, String Str) {
        System.out.print(type);
        System.out.println(" " + Str);
    }
}
