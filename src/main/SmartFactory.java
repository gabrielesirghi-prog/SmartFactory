package main;
import java.util.Random;
import java.util.concurrent.Semaphore;

// La fabbrica: contiene le risorse limitate (un semaforo per ognuna)
class SmartFactory {
    private Semaphore assembly;    // macchine di assemblaggio
    private Semaphore testing;     // postazioni di test
    private Semaphore packaging;   // postazioni di imballaggio
    private Random random = new Random();
    private int finishedProducts = 0;   // contatore condiviso da tutti i thread

    public SmartFactory(int assemblyMachines, int testingStations, int packagingStations) {
        assembly = new Semaphore(assemblyMachines);
        testing = new Semaphore(testingStations);
        packaging = new Semaphore(packagingStations);
    }

    public void assemble(int id) throws InterruptedException {
        System.out.println("Product " + id + " is waiting for an assembly machine.");
        assembly.acquire();
        try {
            System.out.println("Product " + id + " started assembly.");
            Thread.sleep(500 + random.nextInt(501));   // da 500 a 1000 ms
            System.out.println("Product " + id + " completed assembly.");
        } finally {
            assembly.release();
        }
    }

    public void test(int id) throws InterruptedException {
        System.out.println("Product " + id + " is waiting for a testing station.");
        testing.acquire();
        try {
            System.out.println("Product " + id + " started testing.");
            Thread.sleep(300 + random.nextInt(401));   // da 300 a 700 ms
            System.out.println("Product " + id + " completed testing.");
        } finally {
            testing.release();
        }
    }

    public void pack(int id) throws InterruptedException {
        System.out.println("Product " + id + " is waiting for a packaging station.");
        packaging.acquire();
        try {
            System.out.println("Product " + id + " started packaging.");
            Thread.sleep(200 + random.nextInt(301));   // da 200 a 500 ms
            System.out.println("Product " + id + " completed packaging.");
        } finally {
            packaging.release();
        }
    }

    // synchronized: due thread non possono aggiornare il contatore insieme
    public synchronized void productFinished(int id) {
        finishedProducts++;
        System.out.println("Product " + id + " is finished.");
    }

    public int getFinishedProducts() {
        return finishedProducts;
    }
}