import regex.SimpleRegex;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ManyTreadTests implements Runnable {

//    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() - 2;
    private static final int NUM_THREADS = 2;

    private static final int TESTS_TO_RUN_million = 1_000;

    private final int threadNum;

    private final CountDownLatch latch;

    // Needs to be thread safe.
    private static final AtomicInteger ranTests_million = new AtomicInteger(0);



    public ManyTreadTests(CountDownLatch latch, int threadNum) {
        this.latch = latch;
        this.threadNum = threadNum;
    }



    public void run() {
        while (ranTests_million.incrementAndGet() <= TESTS_TO_RUN_million) {
            for (int i = 0; i < 1_000_000; i++) {
                randomPatternAndInput();
            }
            System.out.println("Passed " + (ranTests_million.get() - NUM_THREADS + 1) + " million random tests.");
        }
        System.out.println("Thread " + threadNum + " finished.");
        latch.countDown();
    }

    private void randomPatternAndInput() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        StringBuilder stringBuilder;

        // Make the input.
        int sLen = localRandom.nextInt(10) + 1;
        stringBuilder = new StringBuilder();
        for (int i = 0; i < sLen; i++) {
            stringBuilder.append((char) (localRandom.nextInt(25) + 97));
        }
        String input = stringBuilder.toString();

        // Make the pattern.
        int pLen = localRandom.nextInt(8) + 1;
        double chanceForStar = Math.abs(localRandom.nextInt()) / (double) Integer.MAX_VALUE;
        stringBuilder = new StringBuilder();
        for (int i = 0; i < pLen; i++) {
            int num = localRandom.nextInt(26);
            if (num == 0) {
                stringBuilder.append(".");
            } else {
                stringBuilder.append((char) num + 96);
            }
            if (Math.random() > chanceForStar) {
                stringBuilder.append('*');
            }
        }
        String pattern = stringBuilder.toString();

        SimpleRegex.test(pattern, input);
    }



    public static void manyThreadTest() {
        System.out.println("Spawning " + NUM_THREADS + " treads.");

        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(new ManyTreadTests(latch, i));
        }
        executor.shutdown();

        try {
            latch.await();
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("Many thread test finished.");
    }

}
