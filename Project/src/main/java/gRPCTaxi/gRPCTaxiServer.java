package gRPCTaxi;

import Taxi.Taxi;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class gRPCTaxiServer {

    static Taxi taxi;

    /*
    public static void main(String[] args) {
        try{
            io.grpc.Server server = ServerBuilder.forPort(taxi.getPort())
                    .addService(new RIdeServiceImpl(taxi))
                    .build();

            server.start();

            System.out.println("Server started!");

            server.awaitTermination();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

     */
    
    public gRPCTaxiServer(Taxi t){
        this.taxi = t;

        /*
        String[] arg = new String[0];
        main(arg);

         */

        try{
            io.grpc.Server server = ServerBuilder.forPort(taxi.getPort())
                    .addService(new RIdeServiceImpl(taxi))
                    .build();

            server.start();

            System.out.println("Server started!");

            server.awaitTermination();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
