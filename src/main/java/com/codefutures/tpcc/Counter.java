package com.codefutures.tpcc;

public class Counter {

    private long count = 0;

    public Counter() {
    }

    public synchronized long increment() {
        return ++count;
    }

    public synchronized long get() {
        return count;
    }

    public synchronized long reset() {
        long ret = count;
        count = 0;
        return ret;
    }

}
