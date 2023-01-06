package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

public class Main {

    public static class TestCase {
        public List<Query> queries = new ArrayList<>();
        public String[] tree;
        public int no_of_children;
    }

    public static class Query {
        public int qn; // query number

        public String name;
        public int uuid;
    }

    public static void main(String[] args) throws Exception {
        List<TestCase> testCases = new Main().readInput("input.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/output.txt"));
        TreeOfSpaces treeOfSpaces = new TreeOfSpaces();
        for (TestCase testCase : testCases) {
            String[] nodes = testCase.tree;
            int no_of_children = testCase.no_of_children;
            Node root = treeOfSpaces.buildTree(nodes, no_of_children);
            for (Query query : testCase.queries) {
                Node node = TreeOfSpaces.map.get(query.name);
                boolean result = false;

                if (query.qn == 1) {
                    result = node.lock(node, query.uuid);
                } else if (query.qn == 2) {
                    result = node.unlock(node, query.uuid);
                } else {
                    result = node.upgrade(node, query.uuid);
                }
                bw.write(result + "\n");
            }

            bw.write("\n");
        }
        bw.close();

    }

    private List<TestCase> readInput(String inputFile) throws Exception {

        List<TestCase> testCases = new ArrayList<>();

        String filePath = getClass().getClassLoader().getResource(inputFile).getPath();
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);

        int numTestCases = Integer.parseInt(br.readLine());
        for (int t = 0; t < numTestCases; t++) {
            TestCase testCase = new TestCase();

            // read nodes

            int numNodes = Integer.parseInt(br.readLine());
            testCase.tree = new String[numNodes];

            for (int i = 0; i < numNodes; i++) {
                testCase.tree[i] = br.readLine();
            }

            // read no of children

            testCase.no_of_children = Integer.parseInt(br.readLine());

            // read queries

            int numQueries = Integer.parseInt(br.readLine());

            for (int i = 0; i < numQueries; i++) {
                Query query = new Query();
                String[] line = br.readLine().split(" ");
                query.qn = Integer.parseInt(line[0]);
                query.name = line[1];
                query.uuid = Integer.parseInt(line[2]);
                testCase.queries.add(query);
            }
            testCases.add(testCase);
        }
        br.close();
        return testCases;

    }
}
