package github.com.crazyStrongboy.nio.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author mars_jun
 * @version 2019/1/5 18:31
 */
public class ChannelHandler implements Runnable {

    private SelectionKey readKey;

    public ChannelHandler(SelectionKey readKey) {
        this.readKey = readKey;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        SocketChannel clientSocketChannel = (SocketChannel) this.readKey.channel();
        try {
//            buffer.clear();
//            int readCount = clientSocketChannel.read(buffer);
//            if (readCount <= 0) {
//                clientSocketChannel.close();
//                this.readKey.cancel();
//                return;
//            }
//            buffer.flip();
//            String readMsg = new String(buffer.array(), 0, readCount).trim();
//            String writeMsg = "NIO: " + readMsg;
//            if ("byebye".equalsIgnoreCase(readMsg)) {
//                writeMsg = "byebye!";
//            }
//            buffer.clear();
//            buffer.put(writeMsg.getBytes());
//            buffer.flip();
//            clientSocketChannel.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                clientSocketChannel.close();
                this.readKey.cancel();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
