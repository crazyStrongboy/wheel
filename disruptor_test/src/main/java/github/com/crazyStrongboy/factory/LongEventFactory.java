package github.com.crazyStrongboy.factory;

import com.lmax.disruptor.EventFactory;
import github.com.crazyStrongboy.event.LongEvent;

public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
