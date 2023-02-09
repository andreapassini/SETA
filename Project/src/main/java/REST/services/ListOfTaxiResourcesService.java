package REST.services;

import REST.beans.ListOfTaxiResources;
import REST.beans.TaxiResource;
import SETA.Position;
import REST.beans.TaxiResourceMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("taxis") // defines URI where res of this class are located
public class ListOfTaxiResourcesService {

    // Get Taxi list
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getTaxiResourcesList(){
        TaxiResourceMessage taxiResourceMessage = new TaxiResourceMessage(
                ListOfTaxiResources.getInstance().getTaxiList());
        return Response.ok(taxiResourceMessage).build();
    }

    @Path("termination")
    @POST
    @Produces({"application/json", "application/xml"})
    public Response getTermination(int taxiId){
        // Remove taxi from list
        ListOfTaxiResources.getInstance().RemoveTaxi(taxiId);

        System.out.println("Taxi: " + taxiId + " has been removed from the list");

        return Response.ok().build();
    }

    //permette di inserire un Taxi.Taxi (id, ip, port)
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addTaxi(TaxiResource t){
        System.out.println("Adding new Taxi");

        if(ListOfTaxiResources.getInstance().AddTaxi(t)){
            System.out.println("Taxi (ID: " + t.getId() + ") has been accepted");

            return Response.ok().build();
        }

        System.out.println("Adding request denied");
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    //permette di prelevare un taxi con un determinato id
    @Path("get/{id}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getByName(@PathParam("id") int  id){
        // I don't use the sync since I don't want the lock of the list
        // for a simple output to client
        //      (The interaction between taxi and adminServer is more important)
        TaxiResource t = ListOfTaxiResources.getInstance().getTaxiById(id);
        if(t!=null)
            return Response.ok(t).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

}
