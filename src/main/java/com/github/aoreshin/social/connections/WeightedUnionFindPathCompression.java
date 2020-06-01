package com.github.aoreshin.social.connections;

public class WeightedUnionFindPathCompression {
    private final int[] network;
    private final int[] treeSize;
    private boolean allConnected = false;

    public WeightedUnionFindPathCompression(int networkSize) {
        this.network = new int[networkSize];
        this.treeSize = new int[networkSize];

        for (int i = 0; i < networkSize; i++) {
            network[i] = i;
            treeSize[i] = 1;
        }
    }

    private int root(int i) {
        while (i != network[i]) {
            network[i] = network[network[i]];
            i = network[i];
        }

        return i;
    }

    public boolean connected(int a, int b) {
        return root(a) == root(b);
    }

    public void union(int a, int b) {
        int rootA = root(a);
        int rootB = root(b);

        if (rootA == rootB) {
            return;
        }

        if (treeSize[rootA] < treeSize[rootB]) {
            performUpdate(rootB, rootA);
        } else {
            performUpdate(rootA, rootB);
        }
    }

    private void performUpdate(int rootA, int rootB) {
        network[rootB] = rootA;
        treeSize[rootA] += treeSize[rootB];

        if (treeSize[rootA] == network.length) {
            allConnected = true;
        }
    }

    public boolean allConnected() {
        return allConnected;
    }
}
