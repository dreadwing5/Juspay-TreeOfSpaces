import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class NodeLock {
    private final Map<Node, Boolean> locks;
    private final Queue<Thread> waitingQueue;
    private static NodeLock instance;

    private NodeLock() {
        locks = new HashMap<>();
        waitingQueue = new LinkedList<>();
    }

    public static NodeLock getInstance() {
        if (instance == null) {
            instance = new NodeLock();
        }
        return instance;
    }

    public void acquire(Node node) throws InterruptedException {
        synchronized (node) {
            while (locks.containsKey(node) && locks.get(node)) {
                waitingQueue.add(Thread.currentThread());
                node.wait();
            }

            locks.put(node, true);
        }

    }

    public void release(Node node) {
        synchronized (node) {
            locks.put(node, false);
            if (!waitingQueue.isEmpty()) {
                waitingQueue.poll().notify();
            }
        }
    }

}
