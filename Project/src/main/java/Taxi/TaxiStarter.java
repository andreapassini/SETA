package Taxi;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class TaxiStarter {
    public static void main(String[] args) throws IOException, InterruptedException, MqttException {
        // Start Taxi
        Taxi t = new Taxi();


        // TEST

        //Taxi t = new Taxi();
        //t.TestHearthBeat();
    }
}
