package com.mars_jun.alg;

import java.util.*;

public class StringTest {
    public static void main(String[] args) {
        String s = "au";
//        char[] chars = s.toCharArray();
//        List<String> list = new LinkedList<>();
//        int max = 0;
//        for (char c : chars) {
//            if (list.contains(String.valueOf(c))) {
//                list = list.subList(list.indexOf(String.valueOf(c)) + 1, list.size());
//            }
//            list.add(String.valueOf(c));
//            max = Math.max(list.size(), max);
//        }

//        int n = s.length(), ans = 0;
//        int[] index = new int[128]; // current index of character
//        // try to extend the range [i, j]
//        for (int j = 0, i = 0; j < n; j++) {
//            i = Math.max(index[s.charAt(j)], i);
//            ans = Math.max(ans, j - i + 1);
//            index[s.charAt(j)] = j + 1;
//        }
        int n = s.length();
        int ans = 0, i = 0, j = 0;
        Set<Character> set = new HashSet<>();
        while (i < n && j < n) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        System.out.println(ans);
    }
}
