package github.mars_jun.leetcode.simple;


import java.util.Arrays;

/**
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 * <p>
 * 示例 1:
 * <p>
 * 输入: 123
 * 输出: 321
 * 示例 2:
 * <p>
 * 输入: -123
 * 输出: -321
 * 示例 3:
 * <p>
 * 输入: 120
 * 输出: 21
 * <p>
 * 注意:
 * <p>
 * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。
 * 请根据这个假设，如果反转后整数溢出那么就返回 0。
 */
public class ReverseSolution {

    public int reverse(int x) {
        String value = String.valueOf(x);
        String prefix = "";
        if (value.startsWith("-")) {
            value = value.substring(1);
            prefix = "-";
        }
        char[] chars = value.toCharArray();
        int length = chars.length;
        for (int i = 0; i < length / 2; i++) {
            char temp = chars[length - 1 - i];
            chars[length - 1 - i] = chars[i];
            chars[i] = temp;
        }
        String ans = String.valueOf(chars);
        if (prefix != "") {
            ans = prefix + ans;
        }
        return Integer.parseInt(ans);
    }
}
