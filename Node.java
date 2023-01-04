import java.util.*;

public class Node {
    String val;
    ArrayList<Node> locked_nodes; // this is track all the locked descendant nodes of a node
    Node parent;
    boolean isLocked;
    int uuid;
    boolean resourceInUse = false;

    Node(String val) {
        this.val = val;
        locked_nodes = new ArrayList<>();
        isLocked = false;
        uuid = -1;
        parent = null;
    }

    public boolean lock(Node node, int uuid) throws InterruptedException {
        Stack<Node> ancestors = new Stack<>();
        try {
            NodeLock.getInstance().acquire(node);
            synchronized (node) {
                if (node.isLocked == true || locked_nodes.size() >= 1) {
                    return false;
                }
            }
            Node curr = node.parent;
            while (curr != null) {
                NodeLock.getInstance().acquire(curr);
                ancestors.push(curr);
                if (curr.isLocked == true) {
                    return false;
                }
                curr = curr.parent;

            }

            // lock the node
            node.isLocked = true;
            node.uuid = uuid;

            curr = node.parent;
            while (curr != null) {
                Thread.sleep(1000); // force context switch
                synchronized (curr) {
                    curr.locked_nodes.add(node);
                    curr = curr.parent;
                }
            }
            return true;
        } finally {
            while (!ancestors.isEmpty()) {
                Node curr = ancestors.pop();
                NodeLock.getInstance().release(curr);
            }
            NodeLock.getInstance().release(this);
        }

    }

    public boolean unlock(Node node, int uuid) {
        if (node.isLocked == false || node.uuid != uuid) {
            return false;
        }

        // unlock the node
        node.isLocked = false;
        node.uuid = -1;
        Node curr = node.parent;
        while (curr != null) {
            int idx = curr.locked_nodes.lastIndexOf(node);
            curr.locked_nodes.remove(idx);
            curr = curr.parent;
        }
        return true;
    }

    public boolean upgrade(Node node, int uuid) throws InterruptedException {
        if (node.isLocked == true || locked_nodes.size() == 0) {
            return false;
        }

        for (Node curr : locked_nodes) {
            if (curr.isLocked == true && curr.uuid != uuid) {
                return false;
            }
        }

        int i = locked_nodes.size() - 1;
        while (node.locked_nodes.size() != 0) {
            unlock(locked_nodes.get(i), uuid);
            i--;
        }
        return lock(node, uuid);
    }

}
