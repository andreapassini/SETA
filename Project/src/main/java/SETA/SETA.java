package SETA;

import com.example.grpc.RIdeService;
import com.google.gson.Gson;
import com.google.rpc.Help;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SETA {
    String pubTopicArray [] = new String[] {
            "seta/smartcity/rides/district1",
            "seta/smartcity/rides/district2",
            "seta/smartcity/rides/district3",
            "seta/smartcity/rides/district4"
    };

    static String[] topicTaxiPresence = {
            "seta/smartcity/taxi/district1",
            "seta/smartcity/taxi/district2",
            "seta/smartcity/taxi/district3",
            "seta/smartcity/taxi/district4"
    };

    static String topicRideComplete = "seta/smartcity/ride";
    static String topicTaxiDead = "seta/smartcity/death";
    static String topicTraveling = "seta/smartcity/traveling";

    static int qos;

    static String broker = "tcp://localhost:1883";

    private static String clientId;
    private static MqttClient client;
    private static MqttConnectOptions connOpts;

    private static List<RideRequest> listRideSent;
    private static List<RideRequest> listRideCompleted;
    private static List<RideRequest> listRideInTravel;

    private static List<RideRequest> listRideTaxiDead;

    public SETA() throws MqttException, InterruptedException    {
        listRideSent = new ArrayList<RideRequest>();
        listRideCompleted = new ArrayList<RideRequest>();
        listRideInTravel = new ArrayList<RideRequest>();
        listRideTaxiDead = new ArrayList<RideRequest>();

        clientId = MqttClient.generateClientId(); // Create and ID

        qos = 2;

        client = new MqttClient(broker, clientId);
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        //connOpts.setUserName(username); // optional
        //connOpts.setPassword(password.toCharArray()); // optional
        //connOpts.setWill("this/is/a/topic","will message".getBytes(),1,false);  // optional
        //connOpts.setKeepAliveInterval(60);  // optional

        // Connect the client
        System.out.println(clientId + " Connecting Broker " + broker);
        client.connect(connOpts);
        System.out.println(clientId + " Connected");

        SETAWaitForDeads setaWaitForDeads = new SETAWaitForDeads(
                this,
                10,
                listRideInTravel
        );

        SubscribeMQTT();

        List<RideRequest> listCopy = new ArrayList<RideRequest>();

        // To avoid passing ref of the same objects
        // Just un-mutable parameters
        // Look at RideRequest constructor
        for(RideRequest r: listRideInTravel){
            listCopy.add(new RideRequest(r));
        }

        // Every 5 sec generates 2 request
        while (true){
            if (!client.isConnected()){
                client.connect(connOpts);
                System.out.println("Publisher " + clientId + " re-connecting");
            }

            for(int i =0; i<2; i++){
                RideRequest r1 = createRequest();
                r1.setTaxiId(-1);

                try {
                    Gson gson = new Gson();

                    String payload = gson.toJson(r1);

                    MqttMessage message = new MqttMessage(payload.getBytes());

                    // Set the QoS on the Message
                    message.setQos(qos);
                    System.out.println(clientId + " Publishing message: " + payload + "...");
                    client.publish("seta/smartcity/rides/district" + String.valueOf(HelperC.calculateDistrict(r1.getStartingPos())), message);
                    System.out.println(clientId + " Message published on topic: " + "seta/smartcity/rides/district" + String.valueOf(HelperC.calculateDistrict(r1.getStartingPos())));

                    listRideSent.add(r1);

                } catch (MqttException me ) {
                    System.out.println("reason " + me.getReasonCode());
                    System.out.println("msg " + me.getMessage());
                    System.out.println("loc " + me.getLocalizedMessage());
                    System.out.println("cause " + me.getCause());
                    System.out.println("excep " + me);
                    me.printStackTrace();
                }
            }

            /*
            try {
                System.out.println("Waiting 5 sec");
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
             */

            System.out.println("Waiting for line");
            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();
        }
    }

    private void SubscribeMQTT() throws MqttException {
        // Callback
        client.setCallback(new MqttCallback() {
            public void messageArrived(String topic, MqttMessage message) {
                // Called when a message arrives from the server that matches any subscription made by the client
                String receivedMessage = new String(message.getPayload());
                System.out.println(clientId +" Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                        "\n\tTopic:   " + topic +
                        "\n\tMessage: " + receivedMessage +
                        "\n\tQoS:     " + message.getQos() + "\n");
                if(topic.equals(topicRideComplete)){
                    Gson gson = new Gson();

                    RideRequest ride = gson.fromJson(receivedMessage, RideRequest.class);

                    // To test concur error
                    try {
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Remove it from Sent to Completed
                    HelperC.remove(listRideSent, ride);
                    HelperC.remove(listRideInTravel, ride);
                    listRideCompleted.add(ride);

                    System.out.println("Ride COMPLETE id: " + ride.getId());

                    //int district = Position.CalculateDistrict(ride.getEndingPosition());
                    //SentOutAllRequest("seta/smartcity/rides/district" + district);
                }
                else if (topic.equals(topicTaxiPresence[0])){
                    // Sent out all the ride request with district 1
                    System.out.println("Taxi presence in district: " + topicTaxiPresence[0]);
                    SentOutAllRequest(pubTopicArray[0]);
                }
                else if (topic.equals(topicTaxiPresence[1])){
                    System.out.println("Taxi presence in district: " + topicTaxiPresence[1]);
                    // Sent out all the ride request with district 2
                    SentOutAllRequest(pubTopicArray[1]);
                }
                else if (topic.equals(topicTaxiPresence[2])){
                    System.out.println("Taxi presence in district: " + topicTaxiPresence[2]);
                    // Sent out all the ride request with district 3
                    SentOutAllRequest(pubTopicArray[2]);
                }
                else if (topic.equals(topicTaxiPresence[3])){
                    System.out.println("Taxi presence in district: " + topicTaxiPresence[3]);
                    // Sent out all the ride request with district 4
                    SentOutAllRequest(pubTopicArray[3]);
                }
                else if (topic.equals(topicTraveling)){
                    // Taxi traveling
                    Gson gson = new Gson();

                    RideRequest ride = gson.fromJson(receivedMessage, RideRequest.class);

                    // Remove it from Sent
                    HelperC.remove(listRideSent, ride);
                    // Add to In travel
                    listRideInTravel.add(ride);

                    System.out.println("Ride TRAVELING id: " + ride.getId());
                }
                else if (topic.equals(topicTaxiDead)){
                    Gson gson = new Gson();

                    RIdeService.ExitRequest exitRequest = gson.fromJson(receivedMessage, RIdeService.ExitRequest.class);

                    // Check Taxi id from in travel
                    CheckTaxiDeadInTravel(exitRequest);
                }

                PrintAllLists();
            }
            public void connectionLost(Throwable cause) {
                System.out.println(clientId + " Connection lost! cause: " + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());
                System.out.println("Reconnecting: ...");
                // Connect the client
                System.out.println(clientId + " Trying Connecting to Broker " + broker);
                while(!client.isConnected())
                {
                    try {
                        client.connect(connOpts);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());
            }
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Not used here
            }
        });

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicTaxiPresence[0],qos);
        System.out.println(clientId + " Subscribed to topics : " + topicTaxiPresence[0]);

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicTaxiPresence[1],qos);
        System.out.println(clientId + " Subscribed to topics : " + topicTaxiPresence[1]);

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicTaxiPresence[2],qos);
        System.out.println(clientId + " Subscribed to topics : " + topicTaxiPresence[2]);

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicTaxiPresence[3],qos);
        System.out.println(clientId + " Subscribed to topics : " + topicTaxiPresence[3]);

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicRideComplete,qos);
        System.out.println(clientId + " Subscribed to topics : " + topicRideComplete);

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicTaxiDead,qos);
        System.out.println(clientId + " Subscribed to topics : " + topicTaxiDead);

        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        client.subscribe(topicTraveling, qos);
        System.out.println(clientId + " Subscribed to topics : " + topicTraveling);
    }

    private void SentOutAllRequest(String taxiPresenceTopic) {
        System.out.println("Sending out All Request");

        if(taxiPresenceTopic.equals(topicTaxiPresence[0])){
            for(RideRequest ride: listRideSent){
                if(Position.CalculateDistrict(ride.getStartingPosition()) == 1
                        && !HelperC.contains(listRideInTravel, ride)
                        && !HelperC.contains(listRideCompleted, ride)){
                    try {
                        Gson gson = new Gson();
                        String payload = gson.toJson(ride);
                        MqttMessage message = new MqttMessage(payload.getBytes());
                        // Set the QoS on the Message
                        message.setQos(qos);
                        System.out.println(clientId + " Publishing message: " + payload + "...");
                        client.publish(taxiPresenceTopic, message);
                        System.out.println(clientId + " Message published on topic: " + "seta/smartcity/rides/district" + String.valueOf(HelperC.calculateDistrict(ride.getStartingPos())));
                    } catch (MqttException me ) {
                        System.out.println("reason " + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause " + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }
                }
            }
        } else if(taxiPresenceTopic.equals(topicTaxiPresence[1])){
            for(RideRequest ride: listRideSent){
                if(Position.CalculateDistrict(ride.getStartingPosition()) == 2
                        && !HelperC.contains(listRideInTravel, ride)
                        && !HelperC.contains(listRideCompleted, ride)){
                    try {

                        Gson gson = new Gson();

                        String payload = gson.toJson(ride);

                        MqttMessage message = new MqttMessage(payload.getBytes());

                        // Set the QoS on the Message
                        message.setQos(qos);
                        System.out.println(clientId + " Publishing message: " + payload + "...");
                        client.publish(taxiPresenceTopic, message);
                        System.out.println(clientId + " Message published on topic: " + "seta/smartcity/rides/district" + String.valueOf(HelperC.calculateDistrict(ride.getStartingPos())));

                    } catch (MqttException me ) {
                        System.out.println("reason " + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause " + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }
                }
            }
        } else if(taxiPresenceTopic.equals(topicTaxiPresence[2])){
            for(RideRequest ride: listRideSent){
                if(Position.CalculateDistrict(ride.getStartingPosition()) == 3
                        && !HelperC.contains(listRideInTravel, ride)
                        && !HelperC.contains(listRideCompleted, ride)){
                    try {

                        Gson gson = new Gson();

                        String payload = gson.toJson(ride);

                        MqttMessage message = new MqttMessage(payload.getBytes());

                        // Set the QoS on the Message
                        message.setQos(qos);
                        System.out.println(clientId + " Publishing message: " + payload + "...");
                        client.publish(taxiPresenceTopic, message);
                        System.out.println(clientId + " Message published on topic: " + "seta/smartcity/rides/district" + String.valueOf(HelperC.calculateDistrict(ride.getStartingPos())));
                    } catch (MqttException me ) {
                        System.out.println("reason " + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause " + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }
                }
            }
        } else if(taxiPresenceTopic.equals(topicTaxiPresence[3])){
            for(RideRequest ride: listRideSent){
                if(Position.CalculateDistrict(ride.getStartingPosition()) == 4
                        && !HelperC.contains(listRideInTravel, ride)
                        && !HelperC.contains(listRideCompleted, ride)){
                    try {

                        Gson gson = new Gson();

                        String payload = gson.toJson(ride);

                        MqttMessage message = new MqttMessage(payload.getBytes());

                        // Set the QoS on the Message
                        message.setQos(qos);
                        System.out.println(clientId + " Publishing message: " + payload + "...");
                        client.publish(taxiPresenceTopic, message);
                        System.out.println(clientId + " Message published on topic: " + "seta/smartcity/rides/district" + String.valueOf(HelperC.calculateDistrict(ride.getStartingPos())));

                    } catch (MqttException me ) {
                        System.out.println("reason " + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause " + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }
                }
            }
        }

        PrintAllLists();
    }

    public RideRequest createRequest(){
        int idRide = IDGenerator.getInstance().getID();

        Position startingP = new Position((int) (0 + (Math.random() * 10)), (int) (0 + (Math.random() * 10)));
        Position endP = new Position((int) (0 + (Math.random() * 10)), (int) (0 + (Math.random() * 10)));

        // Starting Pos and Ending Pos must be different
        while (startingP == endP){
            endP = new Position((int) (0 + (Math.random() * 10)), (int) (0 + (Math.random() * 10)));
        }

        RideRequest r = new RideRequest(
                idRide,
                startingP,
                endP);

        return r;
    }

    // Check when a specific taxi died
    public synchronized void CheckTaxiDeadInTravel(RIdeService.ExitRequest exitRequest){
        for(RideRequest ride: listRideInTravel){
            if(ride.getTaxiId() == exitRequest.getTaxiId()){
                // Remove from InTravel
                HelperC.remove(listRideInTravel, ride);
                listRideSent.add(ride);

                SentOutAllRequest("seta/smartcity/taxi/district" + HelperC.calculateDistrict(ride.getStartingPosition()));
            }
        }
    }

    private void PrintAllLists(){
        System.out.println("Printing all lists: \n");
        System.out.println("Sent List");
        for(RideRequest r: listRideSent){
            System.out.println("id: " + r.getId());
        }
        System.out.println("In Travel List");
        for(RideRequest r: listRideInTravel){
            System.out.println("id: " + r.getId());
        }
        System.out.println("Completed List");
        for(RideRequest r: listRideCompleted){
            System.out.println("id: " + r.getId());
        }
    }
}
