package github.com.crazyStrongboy.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author mars_jun
 * @version 2019/1/5 18:31
 */
public class ChannelHandler implements Runnable {

    private SocketChannel channel;
    private boolean flag = true;

    public ChannelHandler(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        try {
            while (this.flag) {
                buffer.clear();
                int readCount = this.channel.read(buffer);
                String readMsg = new String(buffer.array(), 0, readCount).trim();
                String writeMsg = "NIO: " + readMsg;
                if ("byebye".equalsIgnoreCase(readMsg)) {
                    writeMsg = "byebye!";
                    flag = false;
                }
                buffer.clear();
                buffer.put(writeMsg.getBytes());
                buffer.flip();
                this.channel.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
