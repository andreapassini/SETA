package Taxi;

import gRPCTaxi.gRPCTaxiServer;

public class GRPCThread extends Thread{
    private Taxi taxi;

    public GRPCThread(Taxi t) {
        taxi = t;
    }

    @Override
    public void run() {
        gRPCTaxiServer  gRPCTaxiServer = new gRPCTaxiServer(taxi);

        System.out.println("After Creation");
    }
}
