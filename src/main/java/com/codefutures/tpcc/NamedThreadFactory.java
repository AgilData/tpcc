package com.codefutures.tpcc;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

    private String namePrefix;

    private int nextID = 1;

    public NamedThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public Thread newThread(Runnable runnable) {
        int id;
        synchronized (this) {
            id = nextID++;

        }
        return new Thread(runnable, namePrefix + "-" + id);
    }

}