package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiCoreTest {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {

        TreeOfSpaces treeOfSpaces = new TreeOfSpaces();
        String[] nodes = { "World", "Asia", "China", "India", "SouthAfrica", "Egypt" };
        int no_of_children = 2;
        Node root = treeOfSpaces.buildTree(nodes, no_of_children);
        Node india = TreeOfSpaces.map.get("India");
        Node asia = TreeOfSpaces.map.get("Asia");
        Future<Boolean> lockResult2 = executor.submit(() -> asia.lock(asia, 2));
        Future<Boolean> lockResult1 = executor.submit(() -> india.lock(india, 2));
        System.out.println("Lock result 1: " + lockResult1.get());
        System.out.println("Lock result 2: " + lockResult2.get());

        executor.shutdown();
    }

}
