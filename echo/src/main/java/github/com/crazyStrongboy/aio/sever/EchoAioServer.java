package github.com.crazyStrongboy.aio.sever;

import github.com.crazyStrongboy.utils.Global;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author mars_jun
 * @version 2019/1/6 15:56
 */
public class EchoAioServer {
    public static void main(String[] args) throws IOException {
        Thread t = new Thread(new AIOServerThread());
        t.start();
    }
}

class AIOServerThread implements Runnable {

    private AsynchronousServerSocketChannel serverSocketChannel;
    private CountDownLatch latch;

    public AIOServerThread() throws IOException {
        this.serverSocketChannel = AsynchronousServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(Global.PORT));
        System.err.println("AIO SERVER STARTED AT PORT: " + Global.PORT);
        this.latch = new CountDownLatch(1);
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
    public void run() {
        this.serverSocketChannel.accept(this, new AcceptHandler());
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServerThread> {

    @Override
    public void completed(AsynchronousSocketChannel channel, AIOServerThread aioThread) {
        aioThread.getServerSocketChannel().accept(aioThread, this);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        channel.read(buffer, buffer, new EchoHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AIOServerThread aioThread) {
        System.err.println("客户端创建连接失败。。。");
        aioThread.getLatch().countDown();
    }
}

class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private boolean flag = false;

    public EchoHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        System.out.println("1111111111111111111");
        buffer.flip();
        String message = new String(buffer.array(), 0, buffer.remaining()).trim();
        System.out.println("message : " + message);
        String writeMsg = "AIO :" + message;
        if ("byebye".equalsIgnoreCase(message)) {
            writeMsg = "byebye!";
            this.flag = true;
        }
        this.echoWrite(writeMsg);
    }

    private void echoWrite(String writeMsg) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.clear();
        buffer.put(writeMsg.getBytes());
        buffer.flip();
        this.channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buf) {
                // 用来处理第一次没发送完，然后继续发送的。也就是循环处理数据。
                System.out.println(buf.hasRemaining() + "=====" + buf.limit());
                if (buf.hasRemaining()) {
                    System.err.println("222222222222222");
                    EchoHandler.this.channel.write(buf, buf, this);
                } else {
                    if (EchoHandler.this.flag == false) {
                        ByteBuffer b = ByteBuffer.allocate(100);
                        EchoHandler.this.channel.read(b, b, new EchoHandler(EchoHandler.this.channel));
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}