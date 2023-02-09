package Taxi;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class TaxiThread extends Thread{
    Taxi taxi;

    private String decision;
    private BufferedReader inFromUser;

    public TaxiThread() throws MqttException, IOException, InterruptedException {
    }

    public void setTaxi(Taxi t){
        taxi = t;
    }

    public void run(){
        try {
            Menu();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void Menu() throws InterruptedException {
        System.out.println("Thread created");

        while(true) {
            System.out.println("Menu: ");
            System.out.println("Type: ");
            System.out.println("    Recharge or R - to force the Taxi to recharge");
            System.out.println("    Exit - to force the Taxi to leave the net");

            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();

            decision = input;

            if (decision.toLowerCase().equals("recharge") ||
                    input.toLowerCase().equals("r")) {
                if(taxi.isWantsToRecharge() || taxi.isRecharging()){
                    System.out.println("Taxi already wants to recharge");
                    continue;
                } else if(taxi.isLeaving() || taxi.isWantsToLeave()){
                    System.out.println("Taxi is leaving");
                }
                System.out.println("Recharge");
                // Wait the end of Traveling or RideRequest election
                // Stop evaluating ride requests
                // Start getting in line for the recharge
                taxi.BroadcastRechargeRequest();
            } else if (decision.toLowerCase().equals("exit") ||
                    input.toLowerCase().equals("e")) {

                if(taxi.isWantsToRecharge() || taxi.isRecharging()){
                    System.out.println("Taxi already wants to recharge");
                    continue;
                } else if(taxi.isLeaving() || taxi.isWantsToLeave()){
                    System.out.println("Taxi is leaving");
                }

                System.out.println("Exit");
                // Wait the end of Traveling or RideRequest election
                // Stop evaluating ride request
                // Start exiting the net
                taxi.InitTermination();
            }
        }
    }
}
