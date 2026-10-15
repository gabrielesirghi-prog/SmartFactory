package main;
class Product extends Thread {
    private int id;
    private SmartFactory factory;

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
            System.out.println("Product " + id + " was interrupted.");
        }
    }
}