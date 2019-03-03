package github.com.mars_jun;

import github.com.mars_jun.lock.DistributeLock;

import java.util.concurrent.CountDownLatch;

/**
 * @author mars_jun
 * @version 2019/3/3 14:36
 */
public class App {
    public static void main(String[] args) {
        int count  = 3;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Thread t = new Thread(
                    () -> {
                        DistributeLock lock = new DistributeLock();
                        try {
                            countDownLatch.await();
                            lock.lock();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
//                            lock.unlock();
                        }

                    }
                    , "thread -- " + i);
            t.start();
            countDownLatch.countDown();
        }
    }
}
