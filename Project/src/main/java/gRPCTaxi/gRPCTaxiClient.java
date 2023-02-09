package gRPCTaxi;

import SETA.Position;
import SETA.RideRequest;
import Taxi.Taxi;
import com.example.grpc.RideServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.example.grpc.RIdeService;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.TimeUnit;

public class gRPCTaxiClient {

    public static void asyncSendOutRequest(int port, Taxi t, float distance, int batteryCharge, RideRequest ride){
        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        System.out.println("=> Ride Request to port " + port);

        RIdeService.RideRequest request = RIdeService.RideRequest.newBuilder()
                .setRide(
                        RIdeService.Ride.newBuilder()
                        .setRideId(ride.getId())
                        .setStartingPosition(
                                RIdeService.Position.newBuilder()
                                .setX(ride.getStartingPosition().getX())
                                .setY(ride.getStartingPosition().getY())
                                .build())
                        .setEndingPosition(
                                RIdeService.Position.newBuilder()
                                .setX(ride.getEndingPosition().getX())
                                .setY(ride.getEndingPosition().getY())
                                .build()))
                .setTaxiId(t.getTaxiId())
                .setDistance(distance)
                .setBatteryLevel(t.getBatteryCharge())
                .setTimeStamp(t.getLogicalClock())
                .build();

        // Add taxi to the list taxiRequestSend
        // find the taxi id
        int receiverId = t.FindIdFromPort(port);
        t.AddTaxiRideRequest(receiverId);
        System.out.println("Request added");

        //calling the RPC method. since it is asynchronous, we need to define handlers
        stub.reserveRide(request, new StreamObserver<RIdeService.RideResponse>() {
            //this handler takes care of each item received in the stream
            @Override
            public void onNext(RIdeService.RideResponse rideResponse) {
                t.SyncLogicalClock(rideResponse.getTimeStamp());
                t.AddTaxiRideResponse(rideResponse.getTaxiId());

                System.out.println("Response added");

                // check if all the request came back
                if(t.CheckRideRequestSentReceived()){
                    try {
                        t.Travel(ride);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
            }

            //when the stream is completed (the server called "onCompleted") just close the channel
            @Override
            public void onCompleted() {
                //closing the channel
                System.out.println("Closing Channel");
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncChangeDistrict(int port, Taxi t, int oldDistrict, int newDistrict){
        System.out.println("Async Change District");

        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        RIdeService.TaxiDistrictRequest request = RIdeService.TaxiDistrictRequest.newBuilder()
                .setTaxiId(t.getTaxiId())
                .setPreviousDistrict(Position.ConvertDistrict(oldDistrict))
                .setNewDistrict(Position.ConvertDistrict(newDistrict))
                .setTimeStamp(t.getLogicalClock())
                .build();

        //calling the RPC method. since it is asynchronous, we need to define handlers
        stub.changeDistrict(request, new StreamObserver<RIdeService.TaxiDistrictResponse>() {
            @Override
            public void onNext(RIdeService.TaxiDistrictResponse taxiDistrictResponse) {
                t.SyncLogicalClock(taxiDistrictResponse.getTimeStamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                //closing the channel
                System.out.println("Closing Channel");
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncSendRechargeRequest(int port, Taxi t){
        System.out.println("Async Recharge Request");

        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        // Build request
        RIdeService.RechargeRequest request = RIdeService.RechargeRequest.newBuilder()
                .setTaxiId(t.getTaxiId())
                .setRechargeStation(RIdeService.Position.newBuilder()
                        .setX(Position.RechargerPosition(t.getPosition()).getX())
                        .setY(Position.RechargerPosition(t.getPosition()).getY())
                        .build())
                .setTimeStamp(t.getLogicalClock())
                .build();

        System.out.println("Taxi: " + t.getTaxiId());
        System.out.println("Battery: " + t.getBatteryCharge());
        System.out.println(" => ");

        //If a taxi takes part to the mutual exclusion algorithm in
        //order to recharge its battery, it cannot accept any rides until the recharging
        //process of its battery is completed
        t.setWantsToRecharge(true);

        // Add taxi to the list taxiRequestSend
        // find the taxi id
        int receiverId = t.FindIdFromPort(port);

        t.addSentRecharge(receiverId);

        // Only the taxi that request recharge after I'm recharging will be included in the list
        // CLOCK INCREMENT NOT AT EVERY SENT OTHERWISE WE COULD HAVE A DIFFERENT WINNER

        //calling the RPC method. since it is asynchronous, we need to define handlers
        stub.reserveRecharger(request, new StreamObserver<RIdeService.RechargeResponse>() {
            @Override
            public void onNext(RIdeService.RechargeResponse rechargeResponse) {
                t.SyncLogicalClock(rechargeResponse.getTimeStamp());

                t.addResponseRecharge(rechargeResponse.getTaxiId());

                System.out.println(" <= ");
                System.out.println("Response from: " + rechargeResponse.getTaxiId());

                // check if all the request came back
                if(t.CheckRechargeSentReceived()){
                    try {
                        t.Recharge(request);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                //closing the channel
                System.out.println("Closing Channel");
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncReleaseRecharger(int port, Taxi t){
        System.out.println("Releasing Recharger at port: " + port);

        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        RIdeService.RechargeResponse response = RIdeService.RechargeResponse.newBuilder()
                .setTimeStamp(t.getLogicalClock())
                .setTaxiId(t.getTaxiId())
                .build();

        stub.releaseRecharger(response, new StreamObserver<RIdeService.RechargeResponse>() {
            @Override
            public void onNext(RIdeService.RechargeResponse rechargeResponse) {
                t.SyncLogicalClock(rechargeResponse.getTimeStamp());

                t.AddTaxiRideResponse(rechargeResponse.getTaxiId());

                if(t.CheckRideRequestSentReceived()){
                    t.ClearRequestSent();
                    t.CleanResponseReceived();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                //closing the channel
                System.out.println("Closing Channel");
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncGreetings(int port, Taxi t){
        System.out.println("Async Greetings");

        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        RIdeService.GreetingsRequest request = RIdeService.GreetingsRequest.newBuilder()
                .setTaxiId(t.getTaxiId())
                .setPort(t.getPort())
                .setDistrict(Position.CalculateDistrict(t.getPosition()))
                .build();

        System.out.println("Request created");
        System.out.println("Id: " + request.getTaxiId());
        System.out.println("Port: " + request.getPort());

        stub.greetings(request, new StreamObserver<RIdeService.GreetingsResponse>() {
            @Override
            public void onNext(RIdeService.GreetingsResponse greetingsResponse) {
                System.out.println("Response: " + greetingsResponse.getTaxiId()
                        + " Dis: " + greetingsResponse.getDistrict());

                System.out.println("taxi list " + t.getTaxiList().get(0).getId());

                t.SyncLogicalClock(greetingsResponse.getTimeStamp());

                // Add the district of the taxi to the list of taxi communicated by Admin
                t.getTaxiList()
                        .get(t.FindTaxiInList(greetingsResponse.getTaxiId()))
                        .setDistrict(greetingsResponse.getDistrict());

                System.out.println("Insert in taxi list");
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
                System.out.println("Can call IS DEAD from here");
            }
            @Override
            public void onCompleted() {
                //closing the channel
                // System.out.println("Closing Channel");
                // channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncLeavingRequest(int port, Taxi t){
        System.out.println("Async Leaving");

        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        RIdeService.ExitRequest exitRequest = RIdeService.ExitRequest.newBuilder()
                .setTaxiId(t.getTaxiId())
                .setTimeStamp(t.getLogicalClock())
                .build();

        System.out.println("Exiting request created");
        System.out.println("My Id: " + exitRequest.getTaxiId());
        System.out.println("Port of receiver: " + port);
        System.out.println(" => ");

        stub.leavingSystem(exitRequest, new StreamObserver<RIdeService.ExitResponse>() {
            @Override
            public void onNext(RIdeService.ExitResponse exitResponse) {
                System.out.println(" <= ");
                System.out.println("Response from taxi: " + exitRequest.getTaxiId());

                t.SyncLogicalClock(exitResponse.getTimeStamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                //closing the channel
                System.out.println("Closing Channel");
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncHeartBeat(int port, Taxi t){
        //System.out.println("Async Heart Beat");

        //plaintext channel on the address (ip/port) which offers the GreetingService service
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();

        //creating an asynchronous stub on the channel
        RideServiceGrpc.RideServiceStub stub = RideServiceGrpc.newStub(channel);

        RIdeService.HearthBeatRequest request = RIdeService.HearthBeatRequest.newBuilder()
                .setTaxiId(t.getTaxiId())
                .setTimeStamp(t.getLogicalClock())
                .build();

        //System.out.println("Request Heart Beat to " + request.getTaxiId() + " timestamp at " + request.getTimeStamp());
        //System.out.println(" =>  <3  ");

        stub.hearthBeat(request, new StreamObserver<RIdeService.HearthBeatResponse>() {
            @Override
            public void onNext(RIdeService.HearthBeatResponse hearthBeatResponse) {
                //System.out.println(" <=  <3  ");
                //System.out.println("Taxi: " + hearthBeatResponse.getTaxiId() + " IS ALIVE ");

                t.SyncLogicalClock(hearthBeatResponse.getTimeStamp());

                t.AddTaxiToHeartBeatReceived(hearthBeatResponse.getTaxiId());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error! "+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                //closing the channel
                //System.out.println("Closing Channel");
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
