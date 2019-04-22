package com.mars_jun.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mars_jun
 * @version 2019/3/17 0:07
 */
public class ThirtSix2TenUtil {
    //定义36进制数字
    private static final String X36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Integer BASE = 36;

    private static Map<Integer, Character> tenToThirtySix = new HashMap<Integer, Character>();
    private static Map<Integer, String> prefix = new HashMap<Integer, String>();

    static {
        for (int i = 0; i < X36.length(); i++) {
            //0--0,... ..., 35 -- Z的对应存放进去
            tenToThirtySix.put(i, X36.charAt(i));
        }
        for (int i = 1; i < 10; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 1; j <= i; j++) {
                s.append("0");
            }
            prefix.put(i, s.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        ThirtSix2TenUtil thirtSix2TenUtil = new ThirtSix2TenUtil();
        List<String> results = new ArrayList<String>();
        List<String> failed = new ArrayList<String>();
//        String result = thirtSix2TenUtil.ten2ThirtySix(36,null);
//        System.out.println(result);
//        result = thirtSix2TenUtil.ten2ThirtySix(72,null);
//        System.out.println(result);
        for (int i = 0; i < 100000; i++) {
            String result = thirtSix2TenUtil.ten2ThirtySix(i, null);
            if (results.contains(result))
                failed.add(result);
            results.add(result);
            System.out.println(result);
        }
        System.out.printf("failed: %s\n", failed.size());
        System.in.read();
    }

    public String ten2ThirtySix(Integer num, StringBuilder sb) {
        if (sb == null)
            sb = new StringBuilder();
        boolean flag = true;
        while (flag) {
            int key = num / BASE;
            int value = num - key * BASE;
            if (key == 0) {
                sb.append(tenToThirtySix.get(value).toString());
                flag = false;
            } else {
                if (value < BASE) {
                    flag = false;
                }
                ten2ThirtySix(key, sb);
                sb.append(tenToThirtySix.get(value).toString());
            }
        }
        return fill2Ten(sb.toString());
    }

    public String fill2Ten(String num) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix.get(10 - num.length())).append(num);
        return builder.toString();
    }
}
