package MainPackage;

import java.util.ArrayList;

/**
 * Created by Rachel on 3/9/2017.
 */
public class Aircraft implements Comparable<Aircraft>{
    private String name;
    private String aircraft_type;
    private ArrayList<String> classes;
    private ArrayList<Integer> seats;

    public Aircraft(String name, String type){
        this.name=name;
        aircraft_type=type;
    }

    @Override
    public int compareTo(Aircraft aircraft){
        return name.compareTo(aircraft.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAircraft_type() {
        return aircraft_type;
    }

    public void setAircraft_type(String aircraft_type) {
        this.aircraft_type = aircraft_type;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList classes) {
        this.classes = classes;
    }

    public ArrayList<Integer> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList seats) {
        this.seats = seats;
    }
}
