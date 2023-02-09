package SETA;

import com.example.grpc.RIdeService;

public class RideRequest {
    private int id;

    private int taxiId;

    private Position startingPosition;

    private Position endingPosition;

    private float distance;

    private int batteryLvl;

    private int timestamp;

    public RideRequest(){

    }

    public RideRequest(int id, Position startingPos, Position destinationPos) {
        this.id = id;
        this.startingPosition = startingPos;
        this.endingPosition = destinationPos;
    }

    public RideRequest(RideRequest r){
        this.id = r.getId();
        this.startingPosition = r.getStartingPosition();
        this.endingPosition = r.getEndingPosition();
        this.taxiId = r.getTaxiId();
    }

    public RideRequest(RIdeService.RideRequest rideRequest) {
        id = rideRequest.getTaxiId();
        startingPosition = Position.ConvertPosition(rideRequest.getRide().getStartingPosition());
        endingPosition = Position.ConvertPosition(rideRequest.getRide().getEndingPosition());
        taxiId = rideRequest.getTaxiId();
        batteryLvl = rideRequest.getBatteryLevel();
        distance = rideRequest.getDistance();
        timestamp = rideRequest.getTimeStamp();
    }


    //region Getter & Setter
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public Position getStartingPos() {
        return this.startingPosition;
    }

    public Position getDestinationPos() {
        return endingPosition;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }

    public Position getEndingPosition() {
        return endingPosition;
    }

    public void setEndingPosition(Position endingPosition) {
        this.endingPosition = endingPosition;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    //endregion
}
