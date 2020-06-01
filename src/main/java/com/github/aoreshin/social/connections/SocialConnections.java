package com.github.aoreshin.social.connections;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class SocialConnections {
    public static void main(String[] args) {
        int networkSize = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);

        long start = System.currentTimeMillis();
        double averageConnections = getAverageConnections(networkSize, iterations);
        long duration = System.currentTimeMillis() - start;

        System.out.println("Calculation took " + duration +  " ms");
        System.out.println("Average connections to connect all network users: " + averageConnections);
        System.out.println("Average friend list size " + averageConnections / networkSize);
    }

    private static double getAverageConnections(int networkSize, int iterations) {
        ForkJoinPool pool = new ForkJoinPool(10);
        AtomicLong connectionsCount = new AtomicLong();

        pool.submit(()-> IntStream.range(0, iterations).parallel().forEach(i ->
                connectionsCount.addAndGet(getConnectionsCount(networkSize))));
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 1.0 * connectionsCount.get() / iterations;
    }

    private static int getConnectionsCount(int networkSize) {
        WeightedUnionFindPathCompression union = new WeightedUnionFindPathCompression(networkSize);

        int connections = 0;

        while (!union.allConnected()) {
            int a = ThreadLocalRandom.current().nextInt(networkSize);
            int b = ThreadLocalRandom.current().nextInt(networkSize);

            /*
              Does not filter duplicates, equivalent connections and connections to self on purpose,
              because it doesn't have any effect on average values when networkSize and iterations are significant (~1000).
              When network size is small (~10) lack of filtering adds about 25% on average values
              Keeping track of integer pairs adds 5x time overhead (in my case I tried storing objects with integer pairs
              in the Set)
             */
            union.union(a, b);
            connections++;
        }

        return connections;
    }
}
