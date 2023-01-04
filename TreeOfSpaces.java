import java.util.*;

public class TreeOfSpaces {

    static HashMap<String, Node> map = new HashMap<>();

    public Node buildTree(String[] A, int no_of_children) {
        int curr = 0, n = A.length;
        Queue<Node> q = new LinkedList<>();
        Node root = new Node(A[curr]);
        map.put(A[curr], root);
        curr++;
        q.add(root);
        while (!q.isEmpty()) {
            Node front = q.poll();
            int i = 0;
            while (curr < n && i < no_of_children) {
                Node newNode = new Node(A[curr]);
                newNode.parent = front;
                q.add(newNode);
                map.put(A[curr], newNode);
                curr++;
                i++;
            }
        }
        return root;
    }

}
