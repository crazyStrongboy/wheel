package github.com.crazyStrongboy.netty.datagram.server;

import java.net.InetSocketAddress;

/**
 * @author mars_jun
 * @version 2019/1/23 22:32
 */
public class LogEventDatagram {
    public static final byte separate = (byte) '-';
    private final String fileName;
    private final String msg;
    private final long received;
    private final InetSocketAddress address;

    public LogEventDatagram(String fileName, String msg) {
        this(fileName, msg, -1, null);
    }

    public LogEventDatagram(String fileName, String msg, long received, InetSocketAddress address) {
        this.fileName = fileName;
        this.msg = msg;
        this.received = received;
        this.address = address;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceived() {
        return received;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}
