package github.com.crazyStrongboy.nio.client;

import github.com.crazyStrongboy.utils.Global;
import github.com.crazyStrongboy.utils.ScanLine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author mars_jun
 * @version 2019/1/5 18:27
 */
public class EchoNioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(Global.CONNECT_HOST, Global.PORT));
        ByteBuffer buffer = ByteBuffer.allocate(50);
        boolean flag = true;
        while (flag) {
            buffer.clear();
            String msg = ScanLine.getString("请输入要发送的内容：").trim();
            buffer.put(msg.getBytes());
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
            int readCount = channel.read(buffer);
            buffer.flip();
            String resp = new String(buffer.array(), 0, readCount);
            if ("byebye!".equalsIgnoreCase(resp)) {
                flag = false;
            }
            System.out.println(resp);
        }

    }
}
