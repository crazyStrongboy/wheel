package github.com.crazyStrongboy;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import github.com.crazyStrongboy.event.LongEvent;
import github.com.crazyStrongboy.factory.LongEventFactory;
import github.com.crazyStrongboy.handler.LongEventHandler;
import github.com.crazyStrongboy.handler.LongEventHandler1;
import github.com.crazyStrongboy.producer.LongEventProducer;

import java.nio.ByteBuffer;

public class LongEventMain {

    public static void main(String[] args) throws InterruptedException {
        // The factory for the event
        LongEventFactory factory = new LongEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 8;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE);

        // Connect the handler
        disruptor.handleEventsWith(new LongEventHandler(),new LongEventHandler1());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            Thread.sleep(1000);
        }
    }

}
