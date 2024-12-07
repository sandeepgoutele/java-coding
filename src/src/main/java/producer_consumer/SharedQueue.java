package producer_consumer;

import java.util.LinkedList;
import java.util.Queue;

public class SharedQueue {
    private int capacity;
    private Queue<Integer> queue;

    SharedQueue(int cap) {
        this.capacity = cap;
        this.queue = new LinkedList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    synchronized public int getElem(String name) {
        int removed = -1;
        try {
            while (queue.isEmpty()) {
                System.out.println("Empty queue...");
                wait();
            }
            removed = queue.poll();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(name + " - consumed: " + removed);
        notifyAll();
        return removed;
    }

    synchronized public boolean addElem(int elem, String name) {
        try {
            while (queue.size() == capacity) {
                System.out.println("Queue is full...");
                wait();
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        this.queue.add(elem);
        System.out.println(name + " - produced: " + elem);
        notifyAll();
        return true;
    }
}
