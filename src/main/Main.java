package main;
import java.util.ArrayList;
import java.util.List;

/**
 * Usage (all arguments are optional):
 *   java Main [products] [assemblyMachines] [testingStations] [packagingStations]
 * Default: 20 products, 3 assembly machines, 2 testing stations, 2 packaging stations.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numProducts = args.length > 0 ? Integer.parseInt(args[0]) : 20;
        int assemblyMachines = args.length > 1 ? Integer.parseInt(args[1]) : 3;
        int testingStations = args.length > 2 ? Integer.parseInt(args[2]) : 2;
        int packagingStations = args.length > 3 ? Integer.parseInt(args[3]) : 2;

        System.out.println("Factory: " + assemblyMachines + " assembly, "
                + testingStations + " testing, " + packagingStations + " packaging, "
                + numProducts + " products.");

        SmartFactory factory = new SmartFactory(assemblyMachines, testingStations, packagingStations);
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= numProducts; i++) {
            products.add(new Product(i, factory));
        }

        long start = System.currentTimeMillis();

        for (Product p : products) {
            p.start();
        }
        for (Product p : products) {
            p.join();          // wait for every product to finish
        }

        long elapsed = System.currentTimeMillis() - start;

        factory.printCapacityReport();
        System.out.println("Finished products: " + factory.getFinishedProducts() + "/" + numProducts);
        System.out.println("Total time: " + elapsed + " ms");
        System.out.println("Production completed.");
    }
}