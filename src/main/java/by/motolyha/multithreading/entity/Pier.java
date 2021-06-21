package by.motolyha.multithreading.entity;


import by.motolyha.multithreading.exception.ThreadException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;

public class Pier {
    private final int id;
    private static final Logger LOGGER = LogManager.getLogger();
    private int storage = 0;

    public Pier(int id) {
        this.id = id;
    }

    public void process(Ship ship) {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
            unload(ship);
            ship.setState(Ship.ShipState.FINISHED);
        } catch (InterruptedException | ThreadException e) {
            Thread.currentThread().interrupt();
        }

    }

    public int getId() {
        return id;
    }

    private void unload(Ship ship) throws ThreadException {
        LOGGER.log(Level.INFO, "pier load ship by id:"
                + ship.getId() + " containers (" + ship.getContainers() + ')');
        int containers = ship.getContainers();
        ship.removeContainers(containers);
        storage += containers;
    }


    public int getStorage() {
        return storage;
    }
}
