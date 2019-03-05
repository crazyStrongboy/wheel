package github.com.mars_jun.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HashGen {
    public static Long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 305441741;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = -4132994306676758123L;
        int r = 47;

        long h = seed ^ buf.remaining() * m;

        while (buf.remaining() >= 8) {
            long k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);

            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return Long.valueOf(h);
    }
}
