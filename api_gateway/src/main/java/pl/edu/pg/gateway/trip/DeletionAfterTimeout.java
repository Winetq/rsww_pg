package pl.edu.pg.gateway.trip;

import java.util.List;

public class DeletionAfterTimeout implements Runnable {
    private final List<Long> list;
    private final List<Long> elements;
    private final int timeout;

    public DeletionAfterTimeout(List<Long> list, List<Long> elements, int timeout) {
        this.list = list;
        this.elements = elements;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        list.removeAll(elements);
    }
}
