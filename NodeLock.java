public class NodeLock {
    private static NodeLock instance;

    private NodeLock() {
    }

    public static NodeLock getInstance() {
        if (instance == null) {
            instance = new NodeLock();
        }
        return instance;
    }

    public void acquire(Node node) throws InterruptedException {
        synchronized (node) {
            while (node.resourceInUse) {
                node.wait();
            }
            node.resourceInUse = true;
        }

    }

    public void release(Node node) {
        synchronized (node) {
            node.resourceInUse = false;
            node.notify();
        }
    }

}
