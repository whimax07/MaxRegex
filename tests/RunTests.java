import java.util.concurrent.TimeUnit;

public class RunTests {

    public static void main(String[] args) {
        manyTreadTest();
//        singleTreadTest();
    }



    private static void manyTreadTest() {
        long time = System.nanoTime();

        ManyTreadTests.manyThreadTest();

        long timeTaken = System.nanoTime() - time;
        System.out.println(
                "Many treads took " + TimeUnit.SECONDS.convert(timeTaken, TimeUnit.NANOSECONDS) + "."
                        + TimeUnit.MILLISECONDS.convert(timeTaken, TimeUnit.MILLISECONDS) % 100 + "s."
        );
    }

    private static void singleTreadTest() {
        long time = System.nanoTime();

        SingleTreadTest.runSingleTreadTests();

        long timeTaken = System.nanoTime() - time;
        System.out.println(
                "Single tread took " + TimeUnit.SECONDS.convert(timeTaken, TimeUnit.NANOSECONDS) + "."
                        + TimeUnit.MILLISECONDS.convert(timeTaken, TimeUnit.MILLISECONDS) % 100 + "s."
        );
    }

}
