package producer_consumer;

public class Consumer implements Runnable {
    SharedQueue sharedQueue;
    String name;
    int sleepTimeSec;
    public Consumer(SharedQueue sharedQueue, String name, int sleepTimeSec) {
        this.sharedQueue = sharedQueue;
        this.name = name;
        this.sleepTimeSec = sleepTimeSec;
    }

    @Override
    public void run() {
        try {
            while (true) {
                consumeFromSharedQueue();
                Thread.sleep(this.sleepTimeSec);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void consumeFromSharedQueue() {
        sharedQueue.getElem(name);
    }
}
