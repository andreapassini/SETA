package REST.beans;

import Simulator.Measurement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement

public class Statistic {
    private int idTaxi;
    private String timeStamp;
    private float numberOfKmTraveled;
    private int numberOfRides;
    private List<Measurement> listAverageMeasurements;
    private float batteryLevel;

    private int startingTime;

    private int endTime;

    private float pollution;

    public Statistic(){}

    public Statistic(int kmDriven, int completedRides, List<Measurement> averagePollutionStatsList, int id, long time_stamp, int batteryCharge) {
    }

    public Statistic(float numberOfKmTraveled, int numberOfRides, List<Measurement> listAverageMeasurements, int idTaxi, String timeStamp, float batteryLevel) {
        this.numberOfKmTraveled = numberOfKmTraveled;
        this.numberOfRides = numberOfRides;
        this.listAverageMeasurements = listAverageMeasurements;
        this.idTaxi = idTaxi;
        this.timeStamp = timeStamp;
        this.batteryLevel = batteryLevel;
    }

    public float getNumberOfKmTraveled() {
        return numberOfKmTraveled;
    }

    public void setNumberOfKmTraveled(float numberOfKmTraveled) {
        this.numberOfKmTraveled = numberOfKmTraveled;
    }

    public int getNumberOfRides() {
        return numberOfRides;
    }

    public void setNumberOfRides(int numberOfRides) {
        this.numberOfRides = numberOfRides;
    }

    public List<Measurement> getListAverageMeasurements() {
        List<Measurement> listCopy = new ArrayList<>();

        for(Measurement m: listAverageMeasurements){
            listCopy.add(new Measurement(
                    m.getId(),
                    m.getType(),
                    m.getValue(),
                    m.getTimestamp()
            ));
        }

        return listCopy;
    }

    public void setListAverageMeasurements(List<Measurement> listAverageMeasurements) {
        this.listAverageMeasurements = listAverageMeasurements;
    }

    public int getIdTaxi() {
        return idTaxi;
    }

    public void setIdTaxi(int idTaxi) {
        this.idTaxi = idTaxi;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public float getPollution() {
        return pollution;
    }

    public void setPollution(float pollution) {
        this.pollution = pollution;
    }

    public int getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(int startingTime) {
        this.startingTime = startingTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
