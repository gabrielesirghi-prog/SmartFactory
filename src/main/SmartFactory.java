package main;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The factory: it owns the three limited resources (one semaphore each)
 * and the methods that the products use to go through the production stages.
 */
public class SmartFactory {

    /** One type of resource (assembly machines, testing stations, ...). */
    private static class Stage {
        private final String name;          // e.g. "assembly machine"
        private final String doing;         // e.g. "assembly"
        private final Semaphore permits;    // one permit = one free machine
        private final AtomicInteger inUse = new AtomicInteger(0);
        private final AtomicInteger maxInUse = new AtomicInteger(0);
        private final int minTime;
        private final int maxTime;

        Stage(String name, String doing, int capacity, int minTime, int maxTime) {
            this.name = name;
            this.doing = doing;
            this.permits = new Semaphore(capacity);
            this.minTime = minTime;
            this.maxTime = maxTime;
        }

        void process(int productId) throws InterruptedException {
            System.out.println("Product " + productId + " is waiting for " + article() + " " + name + ".");
            permits.acquire();                       // wait for a free machine
            try {
                int now = inUse.incrementAndGet();
                maxInUse.accumulateAndGet(now, Math::max);
                System.out.println("Product " + productId + " started " + doing + ".");
                int time = ThreadLocalRandom.current().nextInt(minTime, maxTime + 1);
                Thread.sleep(time);                  // simulate processing time
                System.out.println("Product " + productId + " completed " + doing + ".");
            } finally {
                inUse.decrementAndGet();
                permits.release();                   // always give the machine back
            }
        }

        private String article() {
            return name.startsWith("a") ? "an" : "a";
        }
    }

    private final Stage assembly;
    private final Stage testing;
    private final Stage packaging;
    private int finishedProducts = 0;   // shared counter -> needs synchronization

    public SmartFactory(int assemblyMachines, int testingStations, int packagingStations) {
        assembly = new Stage("assembly machine", "assembly", assemblyMachines, 500, 1000);
        testing = new Stage("testing station", "testing", testingStations, 300, 700);
        packaging = new Stage("packaging station", "packaging", packagingStations, 200, 500);
    }

    public void assemble(int productId) throws InterruptedException {
        assembly.process(productId);
    }

    public void test(int productId) throws InterruptedException {
        testing.process(productId);
    }

    public void pack(int productId) throws InterruptedException {
        packaging.process(productId);
    }

    /** Critical section: many threads update the same counter. */
    public synchronized void productFinished(int productId) {
        finishedProducts++;
        System.out.println("Product " + productId + " is finished. (" + finishedProducts + " done)");
    }

    public synchronized int getFinishedProducts() {
        return finishedProducts;
    }

    /** Used by Main to check that the capacities were never exceeded. */
    public void printCapacityReport() {
        System.out.println("Maximum machines used at the same time:");
        System.out.println("  assembly:  " + assembly.maxInUse.get() + " (capacity " + assembly.permits.availablePermits() + ")");
        System.out.println("  testing:   " + testing.maxInUse.get() + " (capacity " + testing.permits.availablePermits() + ")");
        System.out.println("  packaging: " + packaging.maxInUse.get() + " (capacity " + packaging.permits.availablePermits() + ")");
    }
}