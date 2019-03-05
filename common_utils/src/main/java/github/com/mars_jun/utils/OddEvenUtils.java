package github.com.mars_jun.utils;

public class OddEvenUtils {
    /**
     * 判断是否是偶数
     * @param num
     * @return
     */
    public static boolean isOdd(Integer num) {
        return (num & 1) == 0;
    }

    /**
     * 判断是否是奇数
     * @param num
     * @return
     */
    public static boolean isEven(Integer num) {
        return (num & 1) == 1;
    }
}
