package producer_consumer;

public class Tester {
    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue(5);
        Producer pt1 = new Producer(sharedQueue, "P1", 0);
        Thread t1 = new Thread(pt1);
        t1.start();
        Consumer ct1 = new Consumer(sharedQueue, "C1", 100);
        Thread t2 = new Thread(ct1);
        t2.start();

        Consumer ct3 = new Consumer(sharedQueue, "C2", 300);
        Thread t3 = new Thread(ct3);
        t3.start();
    }
}
