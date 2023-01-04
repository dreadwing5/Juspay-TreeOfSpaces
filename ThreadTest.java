import java.util.Random;

public class ThreadTest extends Thread {
    private Node node;
    private int uuid;
    Random rand = new Random();

    public ThreadTest(Node node, int uuid) {
        this.node = node;
        this.uuid = uuid;

    }

    @Override
    public void run() {

        try {
            Thread.sleep(rand.nextInt(1000));
            if (node.lock(node, uuid)) {
                System.out.println("Thread " + Thread.currentThread().getId() + " successfully acquired lock on node "
                        + node.val);
            } else {
                System.out.println(
                        "Thread " + Thread.currentThread().getId() + " failed to acquire lock on node " + node.val);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        TreeOfSpaces treeOfSpaces = new TreeOfSpaces();
        String[] nodes = { "World", "Asia", "China", "India", "SouthAfrica", "Egypt" };
        int no_of_children = 2;
        Node root = treeOfSpaces.buildTree(nodes, no_of_children);
        ThreadTest t1 = new ThreadTest(TreeOfSpaces.map.get("Asia"), 1);
        ThreadTest t2 = new ThreadTest(TreeOfSpaces.map.get("China"), 2);
        t1.start();
        t2.start();

    }

}
