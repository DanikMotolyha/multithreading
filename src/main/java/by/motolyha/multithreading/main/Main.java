package by.motolyha.multithreading.main;

import by.motolyha.multithreading.entity.PortBase;
import by.motolyha.multithreading.entity.Ship;
import by.motolyha.multithreading.exception.ThreadException;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws ThreadException {
        var base = PortBase.getInstance();
        var gson = new Gson();
        var inputStream = Main.class.getClassLoader().getResourceAsStream("data\\ships.json");
       if (inputStream != null) {
            JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray()
                    .forEach(ship -> base.addShip(gson.fromJson(ship, Ship.class)));
        }
        base.start();
    }
}
