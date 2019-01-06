package github.com.crazyStrongboy.aio.client;

import github.com.crazyStrongboy.utils.Global;
import github.com.crazyStrongboy.utils.ScanLine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author mars_jun
 * @version 2019/1/6 16:55
 */
public class EchoAioClient {
    public static void main(String[] args) throws IOException {
        AIOClientThread clientThread = new AIOClientThread();
        new Thread(clientThread).start();
        clientThread.sendMessage(ScanLine.getString("请输入要发送的内容："));
    }
}

class AIOClientThread implements Runnable {

    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch latch;

    public AIOClientThread() throws IOException {
        this.socketChannel = AsynchronousSocketChannel.open();
        this.socketChannel.connect(new InetSocketAddress(Global.CONNECT_HOST, Global.PORT));
        latch = new CountDownLatch(1);
    }

    @Override
    public void run() {
        try {
            latch.await();
            System.err.println("exit...........");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        this.socketChannel.write(buffer, buffer, new AIOClientWrite(this.socketChannel, this.latch));
        if ("byebye".equalsIgnoreCase(message)) {
            System.err.println("false/////////////////");
            return false;
        }
        return true;
    }
}

class AIOClientWrite implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch latch;

    public AIOClientWrite(AsynchronousSocketChannel socketChannel, CountDownLatch latch) {
        this.socketChannel = socketChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            this.socketChannel.write(buffer, buffer, this);
        } else {
            ByteBuffer readBuffer = ByteBuffer.allocate(100);
            this.socketChannel.read(readBuffer, readBuffer, new AIOClientRead(this.socketChannel, this.latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.latch.countDown();
    }
}

class AIOClientRead implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch latch;

    public AIOClientRead(AsynchronousSocketChannel socketChannel, CountDownLatch latch) {
        this.socketChannel = socketChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        String readMsg = new String(buffer.array(), 0, buffer.remaining()).trim();
        System.out.println(readMsg);
        if ("byebye!".equalsIgnoreCase(readMsg)) {
            System.err.println("bbbbbbbbbbbbbbbbbb");
            this.latch.countDown();
            return;
        }
        sendMessage(ScanLine.getString("请输入要发送的内容："));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.latch.countDown();
    }

    public boolean sendMessage(String message) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        this.socketChannel.write(buffer, buffer, new AIOClientWrite(this.socketChannel, this.latch));
        if ("byebye".equalsIgnoreCase(message)) {
            System.err.println("false/////////////////");
            return false;
        }
        return true;
    }
}