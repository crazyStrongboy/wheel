package github.com.crazyStrongboy.bio.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author mars_jun
 * @version 2019/1/5 18:31
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Scanner scanner;
    private PrintStream out;
    private boolean flag = true;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.scanner = new Scanner(this.clientSocket.getInputStream());
            this.scanner.useDelimiter("\n");
            this.out = new PrintStream(this.clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (flag) {
                if (this.scanner.hasNext()) {
                    String val = this.scanner.next().trim();
                    if ("byebye".equalsIgnoreCase(val)) {
                        this.out.println("BYE BYE");
                        flag = false;
                    }else {
                        this.out.println("EchoBIO: " + val);
                    }
                }
            }
        } finally {
            try {
                clientSocket.close();
                scanner.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
