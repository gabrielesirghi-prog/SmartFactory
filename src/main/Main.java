package main;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Per i test cambia questi numeri
        int numProducts = 100;
        int assemblyMachines = 3;
        int testingStations = 2;
        int packagingStations = 2;

        SmartFactory factory = new SmartFactory(assemblyMachines, testingStations, packagingStations);

        ArrayList<Product> products = new ArrayList<>();
        for (int i = 1; i <= numProducts; i++) {
            products.add(new Product(i, factory));
        }

        long start = System.currentTimeMillis();

        for (Product p : products) {
            p.start();
        }
        for (Product p : products) {
            p.join();   // aspetta che ogni prodotto finisca
        }

        long end = System.currentTimeMillis();

        System.out.println("Finished products: " + factory.getFinishedProducts());
        System.out.println("Total time: " + (end - start) + " ms");
        System.out.println("Production completed.");
    }
}