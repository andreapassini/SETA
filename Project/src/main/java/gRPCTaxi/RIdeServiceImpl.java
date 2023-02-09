package gRPCTaxi;

import REST.beans.TaxiResource;
import SETA.Position;
import SETA.RideRequest;
import Taxi.Taxi;
import com.example.grpc.RIdeService;
import com.example.grpc.RideServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

public class RIdeServiceImpl extends RideServiceGrpc.RideServiceImplBase {

    private Taxi taxi;

    public RIdeServiceImpl(Taxi t) {
        taxi = t;
    }

    @Override
    public void reserveRide(RIdeService.RideRequest rideRequest, StreamObserver<RIdeService.RideResponse> responseObserver) {
        taxi.SyncLogicalClock(rideRequest.getTimeStamp());

        // If Taxi recharging or wantsToRecharge, wantsToLeave or leaving
        if(taxi.isRecharging() || taxi.isWantsToRecharge() || taxi.isWantsToLeave() || taxi.isLeaving()){
            int myId = taxi.getTaxiId();

            // Response: "OK"
            RIdeService.RideResponse response = RIdeService.RideResponse.newBuilder()
                    .setTaxiId(myId)
                    .setTimeStamp(taxi.getLogicalClock())
                    .build();

            responseObserver.onNext(response);

            responseObserver.onCompleted();

            try {
                taxi.CheckStatus();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        // I NEED TO CHECK IF IT's THE RESERVE RIDE of a RESPONDING TAXI
        // If Busy
        if(taxi.isBusy() && taxi.getRideRequest().getId() != rideRequest.getRide().getRideId()){
            System.out.println("DECLINING Ride Request BUSY");

            int myId = taxi.getTaxiId();

            RIdeService.RideResponse response = RIdeService.RideResponse.newBuilder()
                    .setTaxiId(myId)
                    .setTimeStamp(taxi.getLogicalClock())
                    .build();

            responseObserver.onNext(response);

            responseObserver.onCompleted();
            return;
        }

        // In an Election
        taxi.setBusy(true);

        RIdeService.Ride ride = rideRequest.getRide();

        int rideId = ride.getRideId();
        RIdeService.Position startingPosition = ride.getStartingPosition();
        RIdeService.Position endingPosition = ride.getEndingPosition();

        int requestTaxiId = rideRequest.getTaxiId();
        float requestDistance = rideRequest.getDistance();
        int requestBatteryLevel = rideRequest.getBatteryLevel();
        int requestTimeStamp = rideRequest.getTimeStamp();

        // Calculate your distance
        float taxiDistance = Position.CalculateDistance(taxi.getPosition(), startingPosition);
        int taxiBatteryLvl = taxi.getBatteryCharge();

        System.out.println("Request Pos X: " + startingPosition.getX()
            + " Pos Y: " + startingPosition.getX());

        // Compare the 2 distances
        System.out.println("Taxi Distance: " + taxiDistance);
        System.out.println("Request Distance: " + requestDistance);

        if(taxiDistance < requestDistance){
            System.out.println("WON Ride Request DISTANCE");

            System.out.println("Restarting Ride Request");

            RideRequest rideRequest1 = new RideRequest(rideRequest);
            try {
                taxi.BroadcastRideRequest(rideRequest1);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return;
        } else if(taxiDistance == requestDistance){
            // Now check Battery lvl too
            if (taxiBatteryLvl > requestBatteryLevel){
                // Should not answer but rebroadcast it to make sure to have won the ride
                System.out.println("WON Ride Request Battery");

                //completo e finisco la comunicazione
                //responseObserver.onCompleted();

                System.out.println("Restarting Ride Request");

                // Broadcast again
                RideRequest rideRequest1 = new RideRequest(rideRequest);
                try {
                    taxi.BroadcastRideRequest(rideRequest1);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return;
            } else if(taxiBatteryLvl == requestBatteryLevel){
                // Now check also ID
                if(taxi.getTaxiId() > requestTaxiId){
                    // Should not answer but rebroadcast it to make sure to have won the ride
                    System.out.println("WON Ride Request ID");

                    //completo e finisco la comunicazione
                    //responseObserver.onCompleted();

                    System.out.println("Restarting Ride Request");

                    // Broadcast again
                    RideRequest rideRequest1 = new RideRequest(rideRequest);
                    try {
                        taxi.BroadcastRideRequest(rideRequest1);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return;
                }
            }
        }

        // Lost the ride request election
        // Return OK
        int myId = taxi.getTaxiId();
        taxi.setBusy(false);

        // Build the response
        RIdeService.RideResponse response = RIdeService.RideResponse.newBuilder().setTaxiId(myId).build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

        System.out.println("DECLINING Ride Request ID " + rideId);

        // Clear Ride Request since I lost the election
        taxi.ClearRideRequest();
        taxi.ClearRequestSent();
        taxi.CleanResponseReceived();

        try {
            taxi.CheckStatus();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeDistrict(RIdeService.TaxiDistrictRequest request, StreamObserver<RIdeService.TaxiDistrictResponse> responseObserver) {
        taxi.SyncLogicalClock(request.getTimeStamp());

        System.out.println("Taxi: " + request.getTaxiId() + " changing district to " + request.getNewDistrict());

        // Update taxi in district list
        taxi.getTaxiList().get(taxi.FindTaxiInList(request.getTaxiId())).setDistrict(Position.ConvertDistrict(request.getNewDistrict()));

        // Build the response
        RIdeService.TaxiDistrictResponse response =
                RIdeService.TaxiDistrictResponse.newBuilder()
                .setTaxiId(taxi.getTaxiId())
                .build();

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    @Override
    public void reserveRecharger(RIdeService.RechargeRequest request, StreamObserver<RIdeService.RechargeResponse> responseObserver) {
        taxi.SyncLogicalClock(request.getTimeStamp());

        if(taxi.isRecharging()){
            taxi.addTaxiRechargeInLine(request.getTaxiId());

            //completo e finisco la comunicazione
            responseObserver.onCompleted();

            return;
        }

        if(taxi.isLeaving()){
            RIdeService.RechargeResponse response = RIdeService.RechargeResponse.newBuilder()
                    .setTaxiId(taxi.getTaxiId())
                    .setTimeStamp(taxi.getLogicalClock())
                    .build();

            //passo la risposta nello stream
            responseObserver.onNext(response);

            //completo e finisco la comunicazione
            responseObserver.onCompleted();

            return;
        }

        if(taxi.isBusy()){
            if(taxi.isRecharging()){
                //I'm recharging

                taxi.addTaxiRechargeInLine(request.getTaxiId());
                return;
            }

            // If Just busy
            RIdeService.RechargeResponse response = RIdeService.RechargeResponse.newBuilder()
                    .setTaxiId(taxi.getTaxiId())
                    .build();

            //passo la risposta nello stream
            responseObserver.onNext(response);

            //completo e finisco la comunicazione
            responseObserver.onCompleted();

            return;
        }

        Position recharger = new Position(request.getRechargeStation().getX(), request.getRechargeStation().getY());

        // Check the recharge station
        if(Position.CalculateDistrict(recharger) == Position.CalculateDistrict(taxi.getPosition())){
            // Check if my battery is low
            if(taxi.getBatteryCharge() <= 30){
                // Check timestamp
                if(taxi.getLogicalClock() < request.getTimeStamp()){
                    // Do not respond

                    // Start recharge request
                    try {
                        taxi.BroadcastRechargeRequest();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    taxi.addTaxiRechargeInLine(request.getTaxiId());

                    //responseObserver.onCompleted();
                    return;
                }
            }
        }

        RIdeService.RechargeResponse response = RIdeService.RechargeResponse.newBuilder()
                .setTaxiId(taxi.getTaxiId())
                .setTimeStamp(taxi.getLogicalClock())
                .build();

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    @Override
    public void releaseRecharger(RIdeService.RechargeResponse request, StreamObserver<RIdeService.RechargeResponse> responseObserver) {
        taxi.SyncLogicalClock(request.getTimeStamp());

        taxi.AddTaxiRideResponse(request.getTaxiId());

        // If i'm the first in line just RECHARGE
        if(taxi.CheckRideRequestSentReceived()){
            Position recharger = Position.RechargerPosition(taxi.getPosition());

            RIdeService.RechargeRequest request1 = RIdeService.RechargeRequest.newBuilder()
                    .setTaxiId(taxi.getTaxiId())
                    .setRechargeStation(RIdeService.Position.newBuilder()
                            .setX(recharger.getX())
                            .setY(recharger.getY())
                            .build())
                    .setTimeStamp(taxi.getLogicalClock())
                    .build();

            try {
                taxi.Recharge(request1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //responseObserver.onCompleted();
    }

    @Override
    public void greetings(RIdeService.GreetingsRequest request, StreamObserver<RIdeService.GreetingsResponse> responseObserver) {
        System.out.println("Greetings Received: ");
        System.out.println("    Taxi: " + request.getTaxiId());
        System.out.println("    Port: " + request.getPort());
        System.out.println("    District: " + request.getDistrict());

        taxi.SyncLogicalClock(request.getTimeStamp());

        if(!taxi.isTaxiInList(request.getTaxiId())){
            TaxiResource taxiToAdd = new TaxiResource(
                    request.getTaxiId(),
                    "localhost",
                    request.getPort()
            );

            taxiToAdd.setDistrict(request.getDistrict());

            // Add new taxi to the list
            taxi.AddTaxiToList(taxiToAdd);

            System.out.println("Printing my taxi list: ");
            for(TaxiResource t: taxi.getTaxiList()){
                System.out.println("    - id : " + t.getId());
                System.out.println("        district : " + t.getDistrict());
            }
        }

        // Respond specifying id and district
        RIdeService.GreetingsResponse response = RIdeService.GreetingsResponse.newBuilder()
                .setTaxiId(taxi.getTaxiId())
                .setDistrict(Position.CalculateDistrict(taxi.getPosition()))
                .setTimeStamp(taxi.getLogicalClock())
                .build();

        System.out.println("Response");
        System.out.println("Taxi: " + response.getTaxiId());
        System.out.println("District: " + response.getDistrict());

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        // responseObserver.onCompleted();
    }

    @Override
    public void leavingSystem(RIdeService.ExitRequest request, StreamObserver<RIdeService.ExitResponse> responseObserver) {
        System.out.println("Exiting Received: ");
        System.out.println("Taxi: " + request.getTaxiId());

        taxi.SyncLogicalClock(request.getTimeStamp());

        // Remove taxi from List
        TaxiResource taxiToRemove = taxi.getTaxiList().get(taxi.FindTaxiInList(request.getTaxiId()));
        taxi.getTaxiList().remove(taxiToRemove);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    @Override
    public void hearthBeat(RIdeService.HearthBeatRequest request, StreamObserver<RIdeService.HearthBeatResponse> responseObserver) {
        System.out.println("Hearth Beat: " + request.getTaxiId());

        taxi.SyncLogicalClock(request.getTimeStamp());

        RIdeService.HearthBeatResponse response = RIdeService.HearthBeatResponse.newBuilder()
                .setTaxiId(taxi.getTaxiId())
                .setTimeStamp(taxi.getLogicalClock())
                .build();

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }
}
