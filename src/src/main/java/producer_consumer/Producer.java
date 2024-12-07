package producer_consumer;

public class Producer implements Runnable {
    SharedQueue sharedQueue;
    int startRange;
    String name;
    public Producer(SharedQueue sharedQueue, String name, int startRange) {
        this.sharedQueue = sharedQueue;
        this.name = name;
        this.startRange = startRange;
    }

    @Override
    public void run() {
        try {
            while (true) {
                produceToSharedQueue(startRange);
                startRange++;
//                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void produceToSharedQueue(int val) throws InterruptedException {
        sharedQueue.addElem(val, name);
    }
}
