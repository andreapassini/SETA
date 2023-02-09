package Taxi;

import REST.beans.Statistic;
import REST.beans.TaxiResource;
import REST.beans.TaxiResourceMessage;
import SETA.*;
import Simulator.BufferPollution;
import Simulator.PM10Simulator;
import com.example.grpc.RIdeService;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import gRPCTaxi.gRPCTaxiClient;
import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Taxi {

    //region Attributes

    //region Taxi.Taxi attributes
    private int id;
    private String ip = "localhost";
    private int port;

    private Position position;
    private int batteryCharge;

    private volatile List<TaxiResource> taxiList;
    //endregion

    //region Input
    private String decision;

    private BufferedReader inFromUser;
    //endregion

    //region Lamport’s Logical Clock
    private int logicalClock;

    private int logicalIncrement;


    //endregion

    //region Heart Beat
    private volatile List<Integer> heartBeatSent;
    private volatile List<Integer> heartBeatReceived;
    //endregion

    //region Ricart and Agrawala Mutual Exclusion
    private volatile boolean busy;
    private volatile boolean wantsToRecharge;
    private volatile boolean recharging;
    private volatile boolean wantsToLeave;
    private volatile boolean leaving;

    // Ride request attributes
    private RideRequest rideRequest;
    private List<Integer> taxiReceivedResponseRideRequestList;
    private List<Integer> taxiSentRideRequestList;

    // Recharge attributes
    private RIdeService.RechargeRequest rechargeRequest;
    private List<Integer> taxiSentRechargeRequest;
    private List<Integer> taxiReceivedResponseRechargeRequest;
    private List<Integer> taxiInLineForRechargingAtMyStation;

    //endregion

    //region Stats
    int completedRides;
    int kmDriven; // we do not really need batteryConsumption since 1% of battery is 1 km
    private PM10Simulator pm10Simulator;
    private BufferPollution buffer;
    private StatsThread statsThread;
    //endregion

    private int listeningPortNumber = 1883;
    private String adminServerAddress = "tcp://localhost:";

    //region REST Attributes
    private String serverAddress = "http://localhost:1337";
    private ClientResponse clientResponse;
    static Client client;
    //endregion

    // MQTT client for SETA ride Request
    //region MQTT Client Attributes
    // All the possible topic for ride requests
    static String[] topicsRideRequests = {
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

    static String topicStats = "seta/smartcity";

    static String topicTaxiDead = "seta/smartcity/death";
    static String topicRideInTravel = "seta/smartcity/traveling";

    static String broker = "tcp://localhost:1883";

    static int qos = 2;

    static MqttClient MQTTclient;

    static String clientId;
    //endregion

    //region Input Attributes
    private TaxiThread taxiThread;
    //endregion

    private GRPCThread grpcThread;
    //endregion

    public Taxi() throws IOException, InterruptedException, MqttException {
        System.out.println("Taxi Starting");

        Init();
    }

    //region Taxi Initiation
    public void Init() throws IOException, InterruptedException, MqttException {
        AddTaxi();

        // set-up taxi attributes
        batteryCharge = 100;
        kmDriven = 0;

        // set-up status
        busy = false;
        wantsToRecharge = false;
        recharging = false;
        leaving = false;
        wantsToLeave = false;

        logicalClock = 0;

        // Init lists
        taxiList = new ArrayList<TaxiResource>();
        taxiSentRideRequestList = new ArrayList<Integer>();
        taxiSentRechargeRequest = new ArrayList<Integer>();
        taxiReceivedResponseRideRequestList = new ArrayList<Integer>();
        taxiReceivedResponseRechargeRequest = new ArrayList<Integer>();
        taxiInLineForRechargingAtMyStation = new ArrayList<Integer>();

        heartBeatReceived = new ArrayList<Integer>();
        heartBeatSent = new ArrayList<Integer>();

        ListOfTaxi();

        buffer = new BufferPollution();
        // Start Sensor
        pm10Simulator = new PM10Simulator(buffer);
        StartStatsThread(15);

        BroadcastGreetings();

        clientId = MqttClient.generateClientId();

        if((Position.CalculateDistrict(position)-1) < 0){
            System.out.println("Position not valid");
        }

        String topic = topicsRideRequests[Position.CalculateDistrict(position)-1];

        // The Mqtt will start after Admin approval
        StartSubMQTTClient(topic);

        ChangeDistrict(new Position(-1, -1), position);

        StartGRPCServer();

        // Start Input Thread
        taxiThread = new TaxiThread();
        taxiThread.setTaxi(this);
        taxiThread.start();

        // Start heart beat
        BroadcastHeartbeat();
    }

    private void StartGRPCServer(){
        AddLogicalTime();

        // gRPC Module
        // Start Server
        //gRPCTaxiServer gRPCServer = new gRPCTaxiServer(this);

        grpcThread = new GRPCThread(this);
        grpcThread.start();

        System.out.println("Thread created");
    }

    private void StartSubMQTTClient(String topic){
        AddLogicalTime();

        try {
            MQTTclient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(clientId + " Connecting Broker " + broker);
            MQTTclient.connect(connOpts);
            System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

            // Callback
            MQTTclient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws MqttException, InterruptedException {
                    // Called when a message arrives from the server that matches any subscription made by the client
                    double time = logicalClock;
                    String receivedMessage = new String(message.getPayload());
                    System.out.println(clientId +" Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                            "\n\tTime:    " + time +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + receivedMessage +
                            "\n\tQoS:     " + message.getQos() + "\n");
                    if(topic.equals(topicsRideRequests[Position.CalculateDistrict(position) - 1]) ){
                        Gson gson = new Gson();

                        RideRequest ride = gson.fromJson(receivedMessage, RideRequest.class);

                        // do all checks
                        if(!isRecharging() || !isWantsToRecharge() || !isWantsToLeave() || !isLeaving() || !busy){
                            System.out.println("RIDE from SETA <= Ride id: " + ride.getId());

                            BroadcastRideRequest(ride);
                        }
                    }
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(clientId + " Connectionlost! cause:" + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());

                    System.out.println("Reconnecting: ...");
                    // Connect the client
                    System.out.println(clientId + " Connecting Broker " + broker);
                    try {
                        MQTTclient.connect(connOpts);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }

            });
            System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            MQTTclient.subscribe(topic, qos);
            System.out.println(clientId + " Subscribed to topics : " + topic);

            //System.out.println("\n ***  Press a random key to exit *** \n");
            //Scanner command = new Scanner(System.in);
            //command.nextLine();
            //MQTTclient.disconnect();

        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void AddTaxi() throws IOException {
        AddLogicalTime();

        client = Client.create();

        // Logical Increment
        logicalIncrement = (int) ((Math.random() * (10 - 0)) + 1);
        System.out.println("Logical Increment: " + logicalIncrement);

        do{
            // input stream initialization (from user keyboard)
            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));

            // PORT
            System.out.println("Insert Port: ");
            // read a line from the user
            port = Integer.parseInt(inFromUser.readLine());

            // ID
            System.out.println("Insert Id: ");
            // read a line from the user
            id = Integer.parseInt(inFromUser.readLine());

            // POST EXAMPLE
            String postPath = "/taxis/add";
            TaxiResource taxiResource = new TaxiResource( id, ip, port );
            System.out.println(client + " " + serverAddress + postPath + " " + taxiResource);
            clientResponse = postRequest(client, serverAddress + postPath, taxiResource);
            System.out.println(clientResponse.toString());

        } while(clientResponse.getStatus() == 406);
    }

    public void ListOfTaxi(){
        //GET REQUEST #1
        String getPath = "/taxis";
        clientResponse = getRequest(client,serverAddress+getPath);
        System.out.println(clientResponse.toString());
        TaxiResourceMessage users = clientResponse.getEntity(TaxiResourceMessage.class);
        System.out.println("Taxis List");

        for (TaxiResource t : users.getList()){
            System.out.println("ID: " + t.getId() + " Port: " + t.getPort() + " Starting Pos: " + t.getPos_X() + " - " + t.getPos_Y());

            Position p = new Position(t.getPos_X(), t.getPos_Y());
            t.setDistrict(Position.CalculateDistrict(p));

            taxiList.add(t);

            if(t.getId() == id){
                position = p;

                System.out.print("My Taxi \n" +
                        " ID =" + id + "\n" +
                        " Position = " + p.getX() + " - " + p.getY() + "\n" +
                        " listening District " + topicsRideRequests[Position.CalculateDistrict(position)-1] + "\n" );
            }
        }
    }
    //endregion

    //region REST
    public ClientResponse postRequest(Client client, String url, TaxiResource t){
        AddLogicalTime();

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(t);
        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    public ClientResponse postRequestStats(Client client, String url, Statistic t){
        AddLogicalTime();

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(t);
        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    public ClientResponse postRequestRemoval(Client client, String url, int id){
        AddLogicalTime();

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(id);
        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    public ClientResponse getRequest(Client client, String url){
        AddLogicalTime();

        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
    //endregion

    //region MQTT
    public void PublishChangeDistrict(int dis) throws MqttException {
        TaxiInfo taxiInfo = new TaxiInfo(id, dis);

        Gson gson = new Gson();

        String payload = gson.toJson(taxiInfo);

        MqttMessage message = new MqttMessage(payload.getBytes());
        // Set the QoS on the Message
        message.setQos(qos);
        System.out.println(clientId + " Publishing message: " + payload + " ...");
        MQTTclient.publish(topicTaxiPresence[dis-1], message);
        System.out.println(clientId + " Message published - Thread PID: " + Thread.currentThread().getId());
    }

    public void PublishRideInTravel(RideRequest rideRequest) throws MqttException {
        Gson gson = new Gson();

        String payload = gson.toJson(rideRequest);

        MqttMessage message = new MqttMessage(payload.getBytes());
        // Set the QoS on the Message
        message.setQos(qos);
        System.out.println(clientId + " Publishing message: " + payload + " ...");
        MQTTclient.publish(topicRideInTravel, message);
        System.out.println(clientId + " Message published - Thread PID: " + Thread.currentThread().getId());
    }

    public void PublishRideComplete(RideRequest rideRequest) throws MqttException {
        Gson gson = new Gson();

        String payload = gson.toJson(rideRequest);

        MqttMessage message = new MqttMessage(payload.getBytes());
        // Set the QoS on the Message
        message.setQos(qos);
        System.out.println(clientId + " Publishing message: " + payload + " ...");
        MQTTclient.publish(topicRideComplete, message);
        System.out.println(clientId + " Message published - Thread PID: " + Thread.currentThread().getId());
    }

    public void PublishTaxiDeadHeartBeat(Integer taxiResource_id) throws MqttException {
        Gson gson = new Gson();

        RIdeService.ExitRequest exitRequest = RIdeService.ExitRequest.newBuilder()
                .setTaxiId(taxiResource_id)
                .setTimeStamp(logicalClock)
                .build();

        String payload = gson.toJson(exitRequest);

        MqttMessage message = new MqttMessage(payload.getBytes());
        // Set the QoS on the Message
        message.setQos(qos);
        System.out.println(clientId + " Publishing message: " + payload + " ...");
        MQTTclient.publish(topicTaxiDead, message);
        System.out.println(clientId + " Message published - Thread PID: " + Thread.currentThread().getId());
    }
    //endregion

    //region Control
    public void CheckStatus() throws InterruptedException {
        if(wantsToRecharge && !busy && !recharging){
            BroadcastRechargeRequest();
            return;
        }

        if(wantsToLeave && !busy && !leaving){
            InitTermination();
            return;
        }
    }

    public void CheckInRechargeLine(){

    }
    public void CheckInRideRequestElection(){

    }
    //endregion

    //region Taxi Removal
    public void InitTermination(){
        wantsToLeave = true;

        if(busy || leaving)
            return;

        System.out.println(" X X X X X X X ");
        System.out.println("  Termination");
        System.out.println(" X X X X X X X ");

        AddLogicalTime();

        // Communicate to Admin Server
        SendTermination(id);

        // Communicate the death of Taxi to SETA
        try {
            PublishTaxiDeadHeartBeat(id);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        // Communicate with Taxi
        BroadcastTerminate();

        // Kill threads
        //statsThread.interrupt();

        // Kill Taxi
        System.exit(1);
    }

    private void SendTermination(int idLeaving){
        // Create Rest Client
        Client client = Client.create();
        String serverAddress = "http://localhost:1337";
        ClientResponse clientResponse = null;

        // PATH
        String postPathAdd = "/taxis/termination";

        // POST ADD
        clientResponse = postRequestRemoval(client, serverAddress+postPathAdd, idLeaving);
        System.out.println(clientResponse.toString());
    }
    //endregion

    //region Election
    // Richard Agrawal
    // Broadcast request to all taxis (Included me)
    //  Criteria:
    //      Not Busy
    //      Minimum Distance
    //      Higher Battery Lvl
    //      Higher ID
    // It may be needed a timeout
    public void BroadcastRideRequest(RideRequest rideRequest) throws MqttException, InterruptedException {
        AddLogicalTime();

        if(wantsToRecharge || recharging || leaving || wantsToLeave)
            return;

        System.out.println("Broadcasting Ride Request");

        busy = true;
        this.rideRequest = rideRequest;

        // Calculate Distance
        float distance = Position.CalculateDistance(this.position, rideRequest.getStartingPosition());
        int myDistrict = Position.CalculateDistrict(this.position);

        boolean noOneInMyDistrict = true;

        if(taxiList.size() <= 1){
            System.out.println("List Empty");
            Travel(rideRequest);
            return;
        }

        // Print taxi list
        for (TaxiResource t: taxiList){
            System.out.println("Taxi id: " + t.getId());
            System.out.println("Taxi district: " + t.getDistrict());
        }


        System.out.println("Ride Request to other Taxi");
        // Broadcast to every Taxi.Taxi in my district the request
        for (TaxiResource taxiResource: taxiList) {

            int taxiDistrict = taxiResource.getDistrict();

            // District Check
            if(myDistrict == taxiDistrict && id != taxiResource.getId()){
                System.out.println("Async Send Out Request to " + taxiResource.getId());
                noOneInMyDistrict = false;
                gRPCTaxiClient.asyncSendOutRequest(taxiResource.getPort(), this, distance, batteryCharge, rideRequest);
            }
        }

        if(noOneInMyDistrict){
            System.out.println("No-one in my district");
            Travel(rideRequest);
        }
    }

    public void BroadcastGreetings(){
        for(TaxiResource taxiResource: taxiList){
            // Exclude my self
            if(taxiResource.getId() == this.getTaxiId()){
                continue;
            }

            AddLogicalTime();

            gRPCTaxiClient.asyncGreetings(taxiResource.getPort(), this);
        }
    }

    public void BroadcastChangeDistrict(int oldDistrict, int newDistrict){
        System.out.println("Broadcast Change District");

        for(TaxiResource taxiResource: taxiList){
            // Exclude my self
            if(taxiResource.getId() == this.getTaxiId()){
                continue;
            }

            AddLogicalTime();

            if(taxiResource.getDistrict() == oldDistrict){
                System.out.println("Sending Change District: " + taxiResource.getId());
                gRPCTaxiClient.asyncChangeDistrict(taxiResource.getPort(), this, oldDistrict, newDistrict);
            }
        }
    }

    public void BroadcastRechargeRequest() throws InterruptedException {
        wantsToRecharge = true;

        if(busy || recharging || leaving || wantsToLeave)
            return;

        System.out.println("Broadcast Recharge Request");

        boolean noOneNearMe = true;

        for (TaxiResource taxiResource: taxiList){
            AddLogicalTime();

            if(taxiResource.getDistrict() == Position.CalculateDistrict(position)
                    && taxiResource.getId() != id ){
                noOneNearMe = false;
                System.out.println("Sending Recharge Request: " + taxiResource.getId());
                gRPCTaxiClient.asyncSendRechargeRequest(taxiResource.getPort(), this);
            }
        }

        if(noOneNearMe){
            System.out.println("No one in my district - Recharging");
            Recharge();
        }
    }

    public void BroadcastTerminate(){
        System.out.println("Broadcast Terminate");

        wantsToLeave = true;

        if(busy || leaving)
            return;

        ClearRequestSent();
        CleanResponseReceived();

        for (TaxiResource taxiResource: taxiList){
            // Exclude my self
            if(taxiResource.getId() != this.getTaxiId()){
                AddLogicalTime();

                System.out.println("Sending Leaving Request to: " + taxiResource.getId());
                gRPCTaxiClient.asyncLeavingRequest(taxiResource.getPort(), this);
            }
        }
    }

    public void StartTimeoutRideRequest(){
        AddLogicalTime();

        // Thread for Timeout
        System.out.println("Creating a timeout Thread");

        TimeoutThread theThread = new TimeoutThread(this);
        theThread.start();
    }

    public void Travel(RideRequest rideRequest) throws InterruptedException, MqttException {
        busy = true;

        AddLogicalTime();
        System.out.println("------------------");
        System.out.println("Traveling");
        System.out.println("------------------");

        // Clean list of sent and received ride request
        ClearRequestSent();
        CleanResponseReceived();

        rideRequest.setTaxiId(id);

        // Tell S.E.T.A. in Travel
        PublishRideInTravel(rideRequest);

        // Wait
        Thread.sleep(5*1000);

        PublishRideComplete(rideRequest);

        System.out.println("------------------");
        System.out.println("Ride Complete");
        System.out.println("------------------");

        // Consume battery
        int distanceToStart = (int) Position.CalculateDistance(position, rideRequest.getStartingPosition());
        int distanceToEnd = (int) Position.CalculateDistance(rideRequest.getStartingPosition(), rideRequest.getEndingPosition());
        batteryCharge -= distanceToStart;
        batteryCharge -= distanceToEnd;

        // Add Km Traveled
        kmDriven += distanceToStart + distanceToEnd;

        // Add number of rides
        completedRides ++;

        // Change district if needed
        ChangeDistrict(position, rideRequest.getEndingPosition());

        // Go to another position
        position = rideRequest.getEndingPosition();

        busy = false;

        // Check if my battery is too low
        if(batteryCharge <= 30){
            BroadcastRechargeRequest();
        }

        CheckStatus();
    }

    public void ChangeDistrict(Position oldPosition, Position newPosition) throws MqttException, InterruptedException {
        AddLogicalTime();

        int oldDistrict = Position.CalculateDistrict(oldPosition);
        int newDistrict = Position.CalculateDistrict(newPosition);

        if(oldDistrict == newDistrict){
            return;
        }

        System.out.println("Changing District");

        // Communicate to SETA the change in district
        System.out.println("Telling SETA District Change");

        // gRPC Change District
        BroadcastChangeDistrict(oldDistrict, newDistrict);

        String newTopic = topicsRideRequests[0];

        if(newDistrict == 1){
            newTopic = topicsRideRequests[0];
        } else if(newDistrict == 2){
            newTopic = topicsRideRequests[1];
        } else if(newDistrict == 3){
            newTopic = topicsRideRequests[2];
        } else if(newDistrict == 4){
            newTopic = topicsRideRequests[3];
        }

        MQTTclient.subscribe(newTopic);

        PublishChangeDistrict(Position.CalculateDistrict(position));

        CheckStatus();
    }

    public void CleanResponseReceived(){
        AddLogicalTime();

        taxiReceivedResponseRideRequestList.clear();
    }

    public void ClearRequestSent(){
        AddLogicalTime();

        taxiSentRideRequestList.clear();
    }

    public void ClearRechargeResponse(){
        AddLogicalTime();

        taxiReceivedResponseRechargeRequest.clear();
    }

    public void ClearRechargeSent(){
        AddLogicalTime();

        taxiSentRechargeRequest.clear();
    }

    // Create received response list of a specific ride request
    // So you can check if every response arrived to me

    public synchronized void AddTaxiRideResponse(int taxiIdResponse){
        AddLogicalTime();

        taxiReceivedResponseRideRequestList.add(taxiIdResponse);
    }

    public synchronized void AddTaxiRideRequest(int taxiIdRequest){
        AddLogicalTime();

        taxiSentRideRequestList.add(taxiIdRequest);
    }

    public synchronized boolean CheckRideRequestSentReceived(){
        AddLogicalTime();

        if(taxiSentRideRequestList.size() > taxiReceivedResponseRideRequestList.size()){
            return false;
        }

        for(Integer s: taxiSentRideRequestList){
            if(!taxiReceivedResponseRideRequestList.contains(s)){
                return false;
            }
        }

        return true;
    }

    public synchronized void CheckRideRequestSentReceivedTaxiDead(int taxiDeadId) throws MqttException, InterruptedException {
        AddLogicalTime();

        for(Integer taxiId : taxiSentRideRequestList){
            if(taxiId == taxiDeadId){
                // Remove it from the list
                taxiSentRideRequestList.remove(getIndexById(taxiSentRideRequestList, taxiDeadId));
                taxiReceivedResponseRideRequestList.remove(getIndexById(taxiReceivedResponseRideRequestList, taxiDeadId));

                if(CheckRideRequestSentReceived()){
                    // go to the next step of the ride request
                    BroadcastRideRequest(rideRequest);
                }
            }
        }
    }

    public synchronized void CheckRechargeRequestSentReceivedTaxiDead(int taxiDeadId) throws MqttException, InterruptedException {
        AddLogicalTime();

        for(Integer taxiId : taxiSentRechargeRequest){
            if(taxiId == taxiDeadId){
                // Remove it from the list
                taxiReceivedResponseRechargeRequest.remove(getIndexById(taxiReceivedResponseRechargeRequest, taxiDeadId));
                taxiSentRechargeRequest.remove(getIndexById(taxiSentRechargeRequest, taxiDeadId));

                if(CheckRechargeSentReceived()){
                    // go to the next step of the ride request
                    BroadcastRechargeRequest();
                }
            }
        }
    }

    public boolean CheckRechargeSentReceived(){
        AddLogicalTime();

        if(taxiSentRechargeRequest.size() > taxiReceivedResponseRechargeRequest.size()){
            return false;
        }

        for(Integer s: taxiSentRechargeRequest){
            if(!taxiReceivedResponseRechargeRequest.contains(s)){
                return false;
            }
        }

        System.out.println("Recharge sent-received TRUE");
        return true;
    }

    public synchronized void RemoveTaxiFromList(int idTaxiToRemove){
        AddLogicalTime();

        if(FindTaxiInList(idTaxiToRemove) == -1){
            return;
        }

        taxiList.remove(idTaxiToRemove);
    }

    //endregion

    //region Heartbeat
    public synchronized List<Integer> getSentButNotReceivedHeartbeat(){
        AddLogicalTime();

        List<Integer> listOfDead = new ArrayList<Integer>();

        for(int s: heartBeatSent){
            if(!heartBeatReceived.contains(s)){
                listOfDead.add(s);
            }
        }

        //Clean the heathBeat List
        heartBeatReceived.clear();
        heartBeatSent.clear();

        return listOfDead;
    }

    public void BroadcastHeartbeat() throws InterruptedException, MqttException {
        AddLogicalTime();
        //System.out.println("Broadcast Heartbeat to " + (taxiList.size() - 1) + " taxis");

        for (TaxiResource taxiResource: taxiList){
            // Exclude my self
            if(taxiResource.getId() != id){

                //System.out.println("Sending Heartbeat Request to: " + taxiResource.getId());
                AddHeartBeatSent(taxiResource.getId());
                gRPCTaxiClient.asyncHeartBeat(taxiResource.getPort(), this);
            }
        }

        //taxiThread.Menu();

        Thread.sleep(3*1000);

        List<Integer> listDeadTaxi = getSentButNotReceivedHeartbeat();

        // Check if any taxi is dead
        if(!listDeadTaxi.isEmpty()){
            for(int i = 0; i < listDeadTaxi.size(); i++){
                System.out.println("Taxi " + listDeadTaxi.get(i) + " died");

                // Find Taxi in taxi list and remove it
                taxiList.remove(FindTaxiInList(listDeadTaxi.get(i)));
                // list recharge
                CheckRechargeRequestSentReceivedTaxiDead(listDeadTaxi.get(i)); // TO IMPLEMENT
                // list ride request
                CheckRideRequestSentReceivedTaxiDead(listDeadTaxi.get(i));

                // Communicate the death of Taxi to SETA
                PublishTaxiDeadHeartBeat(listDeadTaxi.get(i));

                // Communicate to Admin Server the death
                SendTermination(listDeadTaxi.get(i));
            }
        }

        CheckStatus();

        BroadcastHeartbeat();
    }
    //endregion

    //region Recharge
    public void Recharge(RIdeService.RechargeRequest request) throws InterruptedException {
        AddLogicalTime();

        // Set Recharging
        recharging = true;
        wantsToRecharge = false;

        System.out.println("Recharging");

        ClearRechargeSent();
        ClearRechargeResponse();

        // Consume battery
        int distance = (int) Position.CalculateDistance(position, request.getRechargeStation());
        batteryCharge -= distance;

        // Add Km Traveled
        kmDriven += distance;

        // Get recharger position
        Position rechargerPosition = new Position(request.getRechargeStation().getX(),
                request.getRechargeStation().getY());

        //Move to recharger
        position = rechargerPosition;

        System.out.println("Sleeping for 10 sec");

        // Sleep
        Thread.sleep(10*1000);

        batteryCharge = 100;
        System.out.println("Fully Charged");

        // Un-Busy
        busy = false;
        recharging = false;

        // Respond to every Recharge request arrived during recharging process
        // I can easily fo it with another gRPC, the receiver will check the same list of response
        RespondToRechargeLine();

        if(leaving){
            InitTermination();
            return;
        }
    }

    private void Recharge() throws InterruptedException {
        // Build request
        RIdeService.RechargeRequest request = RIdeService.RechargeRequest.newBuilder()
                .setTaxiId(id)
                .setRechargeStation(RIdeService.Position.newBuilder()
                        .setX(Position.RechargerPosition(position).getX())
                        .setY(Position.RechargerPosition(position).getY())
                        .build())
                .setTimeStamp(logicalClock)
                .build();

        Recharge(request);
    }

    private void RespondToRechargeLine() {
        System.out.println("Responding to Taxi in Recharge-Line");

        // Check if there are any waiter in line
        if(taxiInLineForRechargingAtMyStation.isEmpty()){
            System.out.println("LINE EMPTY");
            return;
        }

        for(int waiterId: taxiInLineForRechargingAtMyStation){
            AddLogicalTime();

            // Find the relative port
            int waiterPort = FindPortFromId(waiterId);

            // Send a "response" for the recharge request
            gRPCTaxiClient.asyncReleaseRecharger(waiterPort, this);
        }

        ClearRechargeSent();
        ClearRechargeResponse();
        ClearRechargeLine();
    }

    //endregion

    //region Stats Methods
    public void SendStatsOut(){
        // Calculate the Travel stats
        String payloadStats =
                "km" + kmDriven + "-" +
                        "rides" + completedRides + "-";

        // Create Rest Client
        Client client = Client.create();
        String serverAddress = "http://localhost:1337";
        ClientResponse clientResponse = null;

        // PATH
        String postPathAdd = "/stats/add";

        // POST ADD
        Statistic statistic = new Statistic(
                kmDriven,
                completedRides,
                buffer.readAllAndClean(),
                id,
                System.currentTimeMillis(),
                batteryCharge);

        System.out.println("Sending out Stats like kmDriven: " + kmDriven);

        clientResponse = postRequestStats(client, serverAddress+postPathAdd, statistic);
        System.out.println(clientResponse.toString());

        kmDriven = 0;
        completedRides = 0;
    }

    public void StartStatsThread(int waitTime){
        // Thread for Timeout
        System.out.println("Creating a Stats Thread");

        StatsThread theThread = new StatsThread(this, waitTime);
        theThread.start();
    }
    //endregion

    //region Logical Clock
    public void AddLogicalTime(){
        logicalClock += logicalIncrement;
    }

    public synchronized void SyncLogicalClock(int senderTimestamp){
        if(logicalClock <= senderTimestamp){
            logicalClock = senderTimestamp + 1;
        }
    }

    //endregion

    //region Getter & Setter
    public boolean isRecharging(){
        return recharging;
    }

    public int getPort() {
        return port;
    }

    public Position getPosition() {
        return position;
    }

    public int getTaxiId() {
        return id;
    }

    public int getBatteryCharge() {
        return batteryCharge;
    }

    public void setBatteryCharge(int charge){batteryCharge = charge;}

    public int getLogicalClock() {
        return logicalClock;
    }

    public void setLogicalClock(int time){
        logicalClock = time;
    }

    public synchronized List<Integer> getTaxiReceivedResponseRideRequestList() {
        return taxiReceivedResponseRideRequestList;
    }

    public void setTaxiReceivedResponseRideRequestList(List<Integer> taxiReceivedResponseRideRequestList) {
        this.taxiReceivedResponseRideRequestList = taxiReceivedResponseRideRequestList;
    }

    public synchronized List<Integer> getTaxiSentRideRequestList() {
        return taxiSentRideRequestList;
    }

    public void setTaxiSentRideRequestList(List<Integer> taxiSentRideRequestList) {
        this.taxiSentRideRequestList = taxiSentRideRequestList;
    }

    public List<TaxiResource> getTaxiList() {
        return taxiList;
    }

    public synchronized int FindIdFromPort (int port){
        for(TaxiResource t: taxiList){
            if(t.getPort() == port){
                return t.getId();
            }
        }

        return -1;
    }

    public synchronized int FindPortFromId(int tId){
        for(TaxiResource t: taxiList){
            if(t.getId() == tId){
                return t.getPort();
            }
        }

        return -1;
    }

    public synchronized int FindTaxiInList(int tId){
        for(int i = 0; i < taxiList.size(); i++){
            if(taxiList.get(i).getId() == tId){
                return i;
            }
        }

        return -1;
    }

    // Synchronized because is used also by other threads:
    //      Timeout Thread
    //      gRPCServer
    public synchronized boolean isBusy() {
        return busy;
    }

    public synchronized void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isLeaving(){
        return leaving;
    }

    public List<Integer> getHeartBeatReceived(){
        return heartBeatReceived;
    }

    public void AddTaxiToHeartBeatReceived(int taxiAliveId){
        heartBeatReceived.add(taxiAliveId);
    }

    public RideRequest getRideRequest() {
        return rideRequest;
    }

    public boolean isWantsToRecharge() {
        return wantsToRecharge;
    }

    public void setWantsToRecharge(boolean value){
        wantsToRecharge = value;
    }

    public synchronized void AddTaxiToList(TaxiResource youngTaxi){
        System.out.println("Add Taxi to List");
        System.out.println("    Taxi id: " + youngTaxi.getId());
        System.out.println("    in district: " + youngTaxi.getDistrict());
        taxiList.add(youngTaxi);
        System.out.println("Taxi list size: " + taxiList.size());
    }

    public boolean isTaxiInList(int id){
        if(taxiList.contains(id))
            return true;
        return false;
    }

    public void AddHeartBeatSent(int idTaxiHeartBeat){
        heartBeatSent.add(idTaxiHeartBeat);
    }

    public boolean isWantsToLeave() {
        return wantsToLeave;
    }

    public synchronized void ClearRideRequest(){
        rideRequest = null;
    }

    public static int getIndexById(List<Integer> list, int idToFind){
        int index = -1;

        for(int i = 0; i < list.size(); i++){
            if(idToFind == list.get(i)){
                return i;
            }
        }

        return index;
    }
    public synchronized void addSentRecharge(int taxiRechargeSentTo){
        taxiSentRechargeRequest.add(taxiRechargeSentTo);
    }
    public synchronized void addResponseRecharge(int taxiRechargeSentTo){
        taxiReceivedResponseRechargeRequest.add(taxiRechargeSentTo);
    }
    public synchronized void addTaxiRechargeInLine(int taxiIdInLine){
        taxiInLineForRechargingAtMyStation.add(taxiIdInLine);
    }
    public void ClearRechargeLine(){
        taxiInLineForRechargingAtMyStation.clear();
    }
    public void ClearSentRechargeList(){
        taxiSentRechargeRequest.clear();
    }
    public void ClearResponseRechargeRequest(){
        taxiReceivedResponseRechargeRequest.clear();
    }

    //endregion
}
