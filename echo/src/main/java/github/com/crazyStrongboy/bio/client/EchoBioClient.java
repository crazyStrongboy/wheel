package github.com.crazyStrongboy.bio.client;

import github.com.crazyStrongboy.utils.Global;
import github.com.crazyStrongboy.utils.ScanLine;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author mars_jun
 * @version 2019/1/5 18:27
 */
public class EchoBioClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Global.CONNECT_HOST, Global.PORT));
        Scanner scanner = new Scanner(socket.getInputStream());
        scanner.useDelimiter("\n");
        PrintStream out = new PrintStream(socket.getOutputStream());
        boolean flag = true;
        try {
            while (flag) {
                String send = ScanLine.getString("请输入要发送的内容：").trim();
                out.println(send);
                if (scanner.hasNext()) {
                    String trim = scanner.next().trim();
                    System.out.println("receive:" + trim);
                }
                if ("byebye".equalsIgnoreCase(send)) {
                    flag = false;
                }
            }
        } finally {
            socket.close();
            scanner.close();
            out.close();
        }
    }
}
