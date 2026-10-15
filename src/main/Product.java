/**
 * One electronic device. Every product is a thread, so all the products
 * go through the factory at the same time.
 */
package main;
public class Product extends Thread {
    private final int id;
    private final SmartFactory factory;

    public Product(int id, SmartFactory factory) {
        this.id = id;
        this.factory = factory;
    }

    @Override
    public void run() {
        try {
            factory.assemble(id);
            factory.test(id);
            factory.pack(id);
            factory.productFinished(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Product " + id + " was interrupted.");
        }
    }
}