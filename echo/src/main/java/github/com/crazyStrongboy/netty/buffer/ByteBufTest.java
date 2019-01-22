package github.com.crazyStrongboy.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

public class ByteBufTest {
    public static void main(String[] args) {
//        testByteBuf();
//        testByteBuffer();
//        testMergeByteBuffer();
//        testCompositeByteBuf();
//        testReferenceCount();
//        testCreateBuf();
        testReadWrite();
//        testDerived();
    }

    private static void testDerived() {
        ByteBuf heapBuf = Unpooled.buffer(5);
        heapBuf.writeByte(-1);
        heapBuf.writeByte(-2);
        heapBuf.writeByte(-3);
        heapBuf.writeByte(-4);
        // 直接拷贝整个buffer
        ByteBuf duplicate = heapBuf.duplicate();
        duplicate.setByte(0, 2);
        System.err.println("duplicate: " + duplicate.getByte(0) + "====heapBuf: " + heapBuf.getByte(0));//duplicate: 2====heapBuf: 2
        // 拷贝buffer中已经写了的数据
        ByteBuf slice = heapBuf.slice();
        System.err.println("slice capacity: " + slice.capacity());//slice capacity: 4
        slice.setByte(2, 5);
        ByteBuf slice1 = heapBuf.slice(0, 3);
        System.err.println("slice1 capacity: " + slice1.capacity());//slice1 capacity: 3
        System.err.println("duplicate: " + duplicate.getByte(2) + "====heapBuf: " + heapBuf.getByte(2));//duplicate: 5====heapBuf: 5
    }

    private static void testReadWrite() {
        ByteBuf heapBuf = Unpooled.buffer(5);
        heapBuf.writeByte(1);
        System.err.println("writeIndex : " + heapBuf.writerIndex());//writeIndex : 1
        heapBuf.readByte();
        System.err.println("readIndex : " + heapBuf.readerIndex());//readIndex : 1
        heapBuf.setByte(2, 2);
        System.err.println("writeIndex : " + heapBuf.writerIndex());//writeIndex : 1
        heapBuf.getByte(2);
        System.err.println("readIndex : " + heapBuf.readerIndex());//readIndex : 1
    }

    private static void testCreateBuf() {
        // 创建一个heapBuffer，是在堆内分配的
        ByteBuf heapBuf = Unpooled.buffer(5);
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            handleArray(array, offset, length);
        }
        // 创建一个directBuffer,是分配的堆外内存
        ByteBuf directBuf = Unpooled.directBuffer();
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            handleArray(array, 0, length);
        }
    }

    private static void handleArray(byte[] array, int offset, int length) {
    }

    public static void testReferenceCount() {
        ByteBuf buffer = Unpooled.buffer(1);
        int i = buffer.refCnt();
        System.err.println("refCnt : " + i);    //refCnt : 1
        buffer.retain();
        buffer.retain();
        buffer.retain();
        buffer.retain();
        i = buffer.refCnt();
        System.err.println("refCnt : " + i);      //refCnt : 5
        boolean release = buffer.release();
        i = buffer.refCnt();
        System.err.println("refCnt : " + i + " ===== " + release);      //refCnt : 4 ===== false
        release = buffer.release(4);
        i = buffer.refCnt();
        System.err.println("refCnt : " + i + " ===== " + release);      //refCnt : 0 ===== true
        buffer.writeByte(1);
    }

    public static void testCompositeByteBuf() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.buffer(1);
        headerBuf.writeByte('a');
        ByteBuf bodyBuf = Unpooled.buffer(1);
        bodyBuf.writeByte('b');
        messageBuf.addComponents(headerBuf, bodyBuf);
        for (ByteBuf buf : messageBuf) {
            System.out.println((char) buf.readByte());
            System.out.println(buf.toString());
        }
    }

    public static void testMergeByteBuffer() {
        ByteBuffer header = ByteBuffer.allocate(1);
        header.put("a".getBytes());
        header.flip();
        ByteBuffer body = ByteBuffer.allocate(1);
        body.put("b".getBytes());
        body.flip();
        ByteBuffer message = ByteBuffer.allocate(header.remaining() + body.remaining());
        message.put(header);
        message.put(body);
        message.flip();
        while (message.hasRemaining()) {
            System.err.println((char) message.get());
        }
    }

    public static void testByteBuf() {
        ByteBuf heapBuffer = Unpooled.buffer(8);
        int startWriterIndex = heapBuffer.writerIndex();
        System.err.println("startWriterIndex: " + startWriterIndex);
        int startReadIndex = heapBuffer.readerIndex();
        System.err.println("startReadIndex: " + startReadIndex);
        System.err.println("capacity: " + heapBuffer.capacity());
        System.err.println("========================");
        for (int i = 0; i < 3; i++) {
            heapBuffer.writeByte(i);
        }
        int writerIndex = heapBuffer.writerIndex();
        System.err.println("writerIndex: " + writerIndex);
        heapBuffer.readBytes(2);
        int readerIndex = heapBuffer.readerIndex();
        System.err.println("readerIndex: " + readerIndex);
        System.err.println("capacity: " + heapBuffer.capacity());
    }

    public static void testByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        System.err.println("startPosition: " + byteBuffer.position() + ",limit: " + byteBuffer.limit() + ",capacity: " + byteBuffer.capacity());
        byteBuffer.put("abc".getBytes());
        System.err.println("writePosition: " + byteBuffer.position() + ",limit: " + byteBuffer.limit() + ",capacity: " + byteBuffer.capacity());
        byteBuffer.flip();
        System.err.println("readPosition: " + byteBuffer.position() + ",limit: " + byteBuffer.limit() + ",capacity: " + byteBuffer.capacity());
    }
}
