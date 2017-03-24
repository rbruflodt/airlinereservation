package MainPackage;

import java.time.ZonedDateTime;

/**
 * Created by Rachel on 3/13/2017.
 */
public class Flight implements Comparable<Flight>{
    private String depart_city;
    private String arrive_city;
    private String aircraft_name;
    private int depart_hours;
    private int depart_minutes;
    private String depart_AMPM;
    private String depart_timezone;
    private int arrive_hours;
    private int arrive_minutes;
    private String arrive_AMPM;
    private String arrive_timezone;
    private String flight_id;
    private String once;
    private String weekly;
    private String monthly;
    private String until;
    private boolean same_day;

    public Flight(String depart_city, String arrive_city, String aircraft_name, int depart_hours, int depart_minutes, String depart_AMPM, String depart_timezone, int arrive_hours, int arrive_minutes, String arrive_AMPM, String arrive_timezone, String flight_id, String once, String weekly, String monthly, boolean same_day, String until) {
        this.depart_city = depart_city;
        this.arrive_city = arrive_city;
        this.aircraft_name = aircraft_name;
        this.depart_hours = depart_hours;
        this.depart_minutes = depart_minutes;
        this.depart_AMPM = depart_AMPM;
        this.depart_timezone = depart_timezone;
        this.arrive_hours = arrive_hours;
        this.arrive_minutes = arrive_minutes;
        this.arrive_AMPM = arrive_AMPM;
        this.arrive_timezone = arrive_timezone;
        this.flight_id = flight_id;
        this.once = once;
        this.weekly = weekly;
        this.monthly = monthly;
        this.same_day=same_day;
        this.until = until;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public int compareTo(Flight f){
        return flight_id.compareTo(f.getFlight_id());
    }
    public boolean isSame_day() {
        return same_day;
    }

    public void setSame_day(boolean same_day) {
        this.same_day = same_day;
    }

    public int getDepart_hours() {
        return depart_hours;
    }

    public void setDepart_hours(int depart_hours) {
        this.depart_hours = depart_hours;
    }

    public int getDepart_minutes() {
        return depart_minutes;
    }

    public void setDepart_minutes(int depart_minutes) {
        this.depart_minutes = depart_minutes;
    }

    public int getArrive_hours() {
        return arrive_hours;
    }

    public void setArrive_hours(int arrive_hours) {
        this.arrive_hours = arrive_hours;
    }

    public int getArrive_minutes() {
        return arrive_minutes;
    }

    public void setArrive_minutes(int arrive_minutes) {
        this.arrive_minutes = arrive_minutes;
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



}
