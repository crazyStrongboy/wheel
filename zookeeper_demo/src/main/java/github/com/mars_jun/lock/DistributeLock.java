package github.com.mars_jun.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author mars_jun
 * @version 2019/3/3 14:35
 */
public class DistributeLock implements Lock, Watcher {
    private ZooKeeper zk;
    private String current;
    private String last;
    private static final String LOCK_PATH = "/locks";
    private CountDownLatch latch;

    public DistributeLock() {
        try {
            this.zk = new ZooKeeper("134.175.35.208:2181", 30000, this);
            Stat stat = zk.exists(LOCK_PATH, false);
            if (stat == null) {
                zk.create(LOCK_PATH, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void lock() {
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "-获取锁：" + current);
            return;
        }
        waitLock(last);
    }

    private boolean waitLock(String last) {
        try {
            System.out.println(Thread.currentThread().getName() + "-等待前面释放锁:" + last);
            Stat stat = zk.exists(last, true);
            if (stat == null) {
                // TODO 这里可能会有问题,监听没成功也直接返回true了
                return true;
            }
            latch = new CountDownLatch(1);
            latch.await();
            System.out.println(Thread.currentThread().getName() + "-获取锁:"+current);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        try {
            current = zk.create(LOCK_PATH + "/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zk.getChildren(LOCK_PATH, null);
            SortedSet<String> sortNodes = new TreeSet<String>();
            children.forEach(
                    child -> sortNodes.add(LOCK_PATH + "/" + child)
            );
            if (current.equals(sortNodes.first())) {
                return true;
            }
            SortedSet<String> headSet = sortNodes.headSet(current);

            if (!headSet.isEmpty()) {
                last = headSet.last();
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        System.out.println(Thread.currentThread().getName() + "->释放锁" + current);
        try {
            zk.delete(current, -1);
            current = null;
            last = null;
            latch = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public Condition newCondition() {
        return null;
    }

    public void process(WatchedEvent watchedEvent) {
        if (this.latch != null) {
            this.latch.countDown();
        }


    }
}
