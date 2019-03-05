package github.com.mars_jun.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumberUtils {
    static String[] units = {"", "十", "百", "千", "万", "十", "百", "千", "亿"};

    static String[] nums = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        return b1.add(b2);
    }

    public static double add(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).setScale(scale, 4).doubleValue();
    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        return b1.add(b2).setScale(scale, 4);
    }

    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        return b1.subtract(b2);
    }

    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        return b1.multiply(b2);
    }

    public static double multiply(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale, 4)
                .doubleValue();
    }

    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        return b1.multiply(b2).setScale(scale, 4);
    }

    public static double divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).doubleValue();
    }

    public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
        return b1.divide(b2);
    }

    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, 4).doubleValue();
    }

    public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        return b1.divide(b2, scale, 4);
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, 4).doubleValue();
    }

    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        return v.setScale(scale, 4);
    }

    public static String int2RomanNum(int int2Roman) {
        String lm_result = "";
        int temp = int2Roman;

        for (int i = 1; i <= temp / 1000; i++) {
            lm_result = lm_result + 'M';
            int2Roman -= 1000;
        }
        temp = int2Roman;

        for (int i = 1; i <= temp / 500; i++) {
            lm_result = lm_result + 'D';
            int2Roman -= 500;
        }
        temp = int2Roman;

        for (int i = 1; i <= temp / 100; i++) {
            lm_result = lm_result + 'C';
            int2Roman -= 100;
        }
        temp = int2Roman;

        for (int i = 1; i <= temp / 50; i++) {
            lm_result = lm_result + 'L';
            int2Roman -= 50;
        }
        temp = int2Roman;

        for (int i = 1; i <= temp / 10; i++) {
            lm_result = lm_result + 'X';
            int2Roman -= 10;
        }
        temp = int2Roman;

        for (int i = 1; i <= temp / 5; i++) {
            lm_result = lm_result + 'V';
            int2Roman -= 5;
        }
        temp = int2Roman;

        for (int i = 1; i <= temp / 1; i++) {
            lm_result = lm_result + 'I';
            int2Roman--;
        }
        return lm_result;
    }

    public static int romanNum2Int(String roman2Int) {
        int int_result = 0;

        int i = 0;
        while (i <= roman2Int.length() - 1) {
            switch (roman2Int.charAt(i)) {
                case 'M':
                    int_result += 1000;
                    i++;
                    break;
                case 'D':
                    int_result += 500;
                    i++;
                    break;
                case 'C':
                    int_result += 100;
                    i++;
                    break;
                case 'L':
                    int_result += 50;
                    i++;
                    break;
                case 'X':
                    int_result += 10;
                    i++;
                    break;
                case 'V':
                    int_result += 5;
                    i++;
                    break;
                case 'I':
                    int_result++;
                    i++;
            }
        }

        return int_result;
    }

    public static String int2Ch(int theNum) {
        String result = "";
        if (theNum < 0) {
            result = "负";
            theNum = Math.abs(theNum);
        }
        String t = String.valueOf(theNum);
        for (int i = t.length() - 1; i >= 0; i--) {
            int r = (int) (theNum / Math.pow(10.0D, i));
            if (r % 10 != 0) {
                String s = String.valueOf(r);
                String l = s.substring(s.length() - 1, s.length());
                result = result + nums[(Integer.parseInt(l) - 1)];
                result = result + units[i];
            } else if (!result.endsWith("零")) {
                result = result + "零";
            }
        }

        return result;
    }

    public static String double2Percent(double doubleNum) {
        DecimalFormat df = new DecimalFormat("##.00%");
        if (doubleNum == 0.0D)
            return "0%";
        return df.format(doubleNum);
    }

    public static String numberFormart(String reg, double number) {
        DecimalFormat bf = new DecimalFormat(reg);
        return bf.format(number);
    }

    public static String getRemain(double size) {
        if (size == 0.0D) return "0 Bytes";
        String[] sizeNames = {" B", " KB", " MB", " GB", " TB", " PB", " EB", " ZB", " YB"};
        int i = (int) Math.floor(Math.log(size) / Math.log(1024.0D));
        return new Double(divide(size, Math.pow(1024.0D, Math.floor(i)), 2)).toString() + sizeNames[i];
    }

    public static List<Integer> getNumbers(int min, int max) {
        max++;
        Integer temp = null;
        List list = new ArrayList(max - min);
        Set s = new HashSet();
        while ((max > min) && (s.size() < max - min)) {
            temp = Integer.valueOf(new Integer((int) (Math.random() * max)).intValue() + min);
            s.add(temp);
            if (list.size() >= s.size()) continue;
            list.add(temp);
        }
        return list;
    }
}
