package github.com.crazyStrongboy.handler;

import com.lmax.disruptor.EventHandler;
import github.com.crazyStrongboy.event.LongEvent;

public class LongEventHandler1 implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long l, boolean b) throws Exception {
        System.out.println("Event: " + event);
    }
}
