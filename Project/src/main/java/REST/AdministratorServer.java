package REST;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class AdministratorServer {
    // REST

    // Taxi.Taxi will have ID and Port to communicate with Server
    // Add and Remove Taxis from the list
    //  Add =>
    //      Verify ID,
    //      Add ID to list,
    //
    //      Send to the taxi,
    //          List of Taxis,
    //          Random Position of a recharger
    //  Remove Taxi.Taxi from the List

    // Enable Client ID to Query Stats

    private static final String HOST = "localhost";
    private static final int PORT = 1337;

    //private static ListOfTaxiResources listOfTaxiResource;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://"+HOST+":"+PORT+"/");
        server.start();

        System.out.println("Server running!");
        System.out.println("Server started on: http://"+HOST+":"+PORT);
    }

}
