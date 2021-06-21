package by.motolyha.multithreading.entity;

import by.motolyha.multithreading.exception.ThreadException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class Ship implements Callable<Ship> {
    private static final Logger LOGGER = LogManager.getLogger();
    private int id;
    private int containers;
    private ShipState state;

    public Ship(int id, int containers)
    {
        this.id = id;
        this.containers = containers;
        state = ShipState.NEW;
    }

    public int getId() {
        return id;
    }

    @Override
    public Ship call() throws Exception {
        this.state = ShipState.UNLOADING;
        do {
            this.operation();
        } while (this.state.equals(ShipState.UNLOADING));
        return this;
    }
    public void operation() {
        this.state = ShipState.UNLOADING;
        var base = PortBase.getInstance();
        var port = base.getFreePier();
        LOGGER.log(Level.INFO,"ship id: " + this.id + " go to port by id " + port.getId());
        port.process(this);
        base.releasePier(port);
    }

    public enum ShipState {
        NEW,
        UNLOADING,
        FINISHED
    }

    public Ship(int count) {
        state = ShipState.NEW;
        containers = count;
    }
    public void setState(ShipState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var ship = (Ship) o;
        return containers == ship.containers;
    }

    @Override
    public int hashCode() {
        return 31 * containers;
    }

    @Override
    public String toString() {
        return "Ship{" +
                ", containers=" + containers +
                '}';
    }

    public int getContainers() {
        return containers;
    }

    public void removeContainers(int count) throws ThreadException {
        if (containers - count < 0) {
            throw new ThreadException();
        }
        this.containers -= count;
    }
}
