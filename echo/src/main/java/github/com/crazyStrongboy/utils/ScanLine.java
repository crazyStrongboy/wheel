package github.com.crazyStrongboy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author mars_jun
 * @version 2019/1/5 18:39
 */
public class ScanLine {

    private static final BufferedReader BUFFERED_READER = new BufferedReader(new InputStreamReader(System.in));

    private ScanLine() {

    }

    public static String getString(String line) {
        String str = "";
        boolean flag = true;
        System.out.print(line);
        while (flag) {
            try {
                str = BUFFERED_READER.readLine();
                if (str == null || "".equals(str.trim())) {
                    System.out.print("请输入要发送的内容：");
                } else {
                    flag = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}
