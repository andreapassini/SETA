package REST.Client;

import REST.beans.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class AdministratorClient {
    static Client client;
    static String serverAddress = "http://localhost:1337";
    static ClientResponse clientResponse;

    public static void main(String[] args) throws IOException {
        client = Client.create();

        Menu();
    }

    private static void Menu() throws IOException {

        while(true) {


            System.out.println("List of Taxis => 1");
            System.out.println("n Average Stats of taxi => 2");
            System.out.println("Average Stats between t1, t2 => 3");

            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();

            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));

            // ID
            System.out.println("Insert Choice: ");
            // read a line from the user
            int choice = Integer.parseInt(input);

            if (choice == 2) {
                System.out.println("Insert n: ");
                // read a line from the user
                int n = Integer.parseInt(inFromUser.readLine());

                System.out.println("Insert id: ");
                // read a line from the user
                int id = Integer.parseInt(inFromUser.readLine());

                //GET REQUEST #1
                String getPath = "/stat/get/single/" + id + "/" + n;
                clientResponse = getRequest(client, serverAddress + getPath);
                System.out.println(clientResponse.toString());
                if (clientResponse.getStatus() != 404) {
                    Statistic users = clientResponse.getEntity(Statistic.class);
                    System.out.println("Avg Stats ");

                    System.out.println(
                            "Id: " + users.getIdTaxi()
                                    + " KmDriven: " + users.getNumberOfKmTraveled()
                                    + " Rides: " + users.getNumberOfRides()
                                    + " Battery cons: " + users.getNumberOfKmTraveled()
                                    + " Pollution " + users.getPollution()
                    );
                }
            } else if (choice == 1) {
                //GET REQUEST #1
                String getPath = "/taxis";
                clientResponse = getRequest(client, serverAddress + getPath);
                System.out.println(clientResponse.toString());
                if (clientResponse.getStatus() != 404) {
                    TaxiResourceMessage users = clientResponse.getEntity(TaxiResourceMessage.class);
                    System.out.println("Taxis ");

                    for (TaxiResource u : users.getList()) {
                        System.out.println("Id: " + u.getId());
                    }
                }
            } else if (choice == 3) {
                System.out.println("Insert t1: ");
                // read a line from the user
                int t1 = Integer.parseInt(inFromUser.readLine());

                System.out.println("Insert t2: ");
                // read a line from the user
                int t2 = Integer.parseInt(inFromUser.readLine());

                //GET REQUEST #1
                String getPath = "/stat/get/all/" + t1 + "/" + t2;
                clientResponse = getRequest(client, serverAddress + getPath);
                System.out.println(clientResponse.toString());
                Statistic users = clientResponse.getEntity(Statistic.class);
                System.out.println("Stats ");

                System.out.println(
                        " KmDriven: " + users.getNumberOfKmTraveled()
                                + " Rides: " + users.getNumberOfRides()
                                + " Battery cons: " + users.getNumberOfKmTraveled()
                                + " Pollution " + users.getPollution()
                );
            }
        }

    }

    public static ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
}
