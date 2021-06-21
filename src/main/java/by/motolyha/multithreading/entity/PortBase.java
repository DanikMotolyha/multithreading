package by.motolyha.multithreading.entity;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PortBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int PIERS_COUNT = 3;
    private static PortBase instance;
    private final Queue<Pier> piers = new ArrayDeque<>();
    private final Queue<Ship> shipsQueue = new ArrayDeque<>();
    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private final ReentrantLock pierQueueLocker = new ReentrantLock();
    private final Condition pierQueueCondition = pierQueueLocker.newCondition();

    private PortBase() {
        for (int i = 0; i < PIERS_COUNT; i++) {
            piers.add(new Pier(i));
        }
    }

    public static PortBase getInstance() {
        while (instance == null) {
            if (isInitialized.compareAndSet(false, true)) {
                instance = new PortBase();
            }
        }

        return instance;
    }

    public void addShip(Ship ship) {
        shipsQueue.add(ship);
    }

    public Pier getFreePier() {
        Pier pier = null;
        pierQueueLocker.lock();
        try {
            while ((pier = piers.poll()) == null) {
                pierQueueCondition.await();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            pierQueueLocker.unlock();
        }
        return pier;
    }

    public void releasePier(Pier pier) {
        if (pier != null) {
            pierQueueLocker.lock();
            try {
                System.out.println("realized pier" + pier.getId());
                piers.add(pier);
                pierQueueCondition.signal();
            } finally {
                pierQueueLocker.unlock();
            }
        }

    }

    public void start() {
        ExecutorService service = Executors.newFixedThreadPool(shipsQueue.size());
        while (!shipsQueue.isEmpty()) {
            service.submit(shipsQueue.poll());
        }
        service.shutdown();
    }
}
