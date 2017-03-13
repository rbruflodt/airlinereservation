package MainPackage;

import java.time.ZonedDateTime;

/**
 * Created by Rachel on 3/13/2017.
 */
public class Flight {
    private String depart_city;
    private String arrive_city;
    private String aircraft_name;
    private String depart_time;
    private String depart_AMPM;
    private String depart_timezone;
    private String arrive_time;
    private String arrive_AMPM;
    private String arrive_timezone;
    private String flight_id;
    private String once;
    private String weekly;
    private String monthly;

    public Flight(String depart_city, String arrive_city, String aircraft_name, String depart_time, String depart_AMPM, String depart_timezone, String arrive_time, String arrive_AMPM, String arrive_timezone, String flight_id, String once, String weekly, String monthly) {
        this.depart_city = depart_city;
        this.arrive_city = arrive_city;
        this.aircraft_name = aircraft_name;
        this.depart_time = depart_time;
        this.depart_AMPM = depart_AMPM;
        this.depart_timezone = depart_timezone;
        this.arrive_time = arrive_time;
        this.arrive_AMPM = arrive_AMPM;
        this.arrive_timezone = arrive_timezone;
        this.flight_id = flight_id;
        this.once = once;
        this.weekly = weekly;
        this.monthly = monthly;
    }

    public String getOnce() {
        return once;
    }

    public void setOnce(String once) {
        this.once = once;
    }

    public String getWeekly() {

        return weekly;
    }

    public void setWeekly(String weekly) {
        this.weekly = weekly;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getDepart_AMPM() {
        return depart_AMPM;
    }

    public void setDepart_AMPM(String depart_AMPM) {
        this.depart_AMPM = depart_AMPM;
    }

    public String getDepart_timezone() {
        return depart_timezone;
    }

    public void setDepart_timezone(String depart_timezone) {
        this.depart_timezone = depart_timezone;
    }

    public String getArrive_AMPM() {
        return arrive_AMPM;
    }

    public void setArrive_AMPM(String arrive_AMPM) {
        this.arrive_AMPM = arrive_AMPM;
    }

    public String getArrive_timezone() {
        return arrive_timezone;
    }

    public void setArrive_timezone(String arrive_timezone) {
        this.arrive_timezone = arrive_timezone;
    }

    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getDepart_city() {
        return depart_city;
    }

    public void setDepart_city(String depart_city) {
        this.depart_city = depart_city;
    }

    public String getArrive_city() {
        return arrive_city;
    }

    public void setArrive_city(String arrive_city) {
        this.arrive_city = arrive_city;
    }

    public String getAircraft_name() {
        return aircraft_name;
    }

    public void setAircraft_name(String aircraft_name) {
        this.aircraft_name = aircraft_name;
    }


    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }


    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }
}
