package REST.services;

import REST.beans.Statistic;
import REST.beans.Statistics;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("stats") // defines URI where res of this class are located
public class StatisticsService {
    // add a Stat to the list
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addStats(Statistic t) {
        Statistics.getInstance().add(t);
        return Response.ok().build();
    }

    // get Average Travelled Km
    @Path("get/single/{id}/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getSingleStats(@PathParam("id") int id, @PathParam("n") int n) {
        Statistic avg = new Statistic();

        List<Statistic> statisticList = Statistics.getInstance().getStatsList();

        avg = avgOfAll(StatisticsService.getLastNElementsOfTaxi(statisticList,id, n));
        avg.setIdTaxi(id);

        return Response.ok(avg).build();
    }


    // get Average Travelled Km
    @Path("get/travelledKm/{id}/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getTravelledKm(@PathParam("id") int id, @PathParam("n") int n) {
        float avg = 0;

        List<Statistic> statisticList = Statistics.getInstance().getStatsList();

        avg = avgTravelledKm(StatisticsService.getLastNElementsOfTaxi(statisticList,id, n));

        return Response.ok(avg).build();
    }

    // get Average Battery Level
    @Path("get/battery/{id}/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getBatteryLevel(@PathParam("id") int id, @PathParam("n") int n) {
        float avg = 0;

        List<Statistic> statisticList = Statistics.getInstance().getStatsList();

        avg = avgBatteryLevel(StatisticsService.getLastNElementsOfTaxi(statisticList,id, n));

        return Response.ok(avg).build();
    }

    // get Average Pollution Level
    @Path("get/pollution/{id}/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getPollutionLevel(@PathParam("id") int id, @PathParam("n") int n) {
        float avg = 0;

        List<Statistic> statisticList = Statistics.getInstance().getStatsList();

        avg = avgPollutionLevel(StatisticsService.getLastNElementsOfTaxi(statisticList, id, n));

        return Response.ok(avg).build();
    }

    // get Average Ride Accomplished
    @Path("get/ride/{id}/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getRideAccomplishedTaxi(@PathParam("id") int id, @PathParam("n") int n) {
        float avg = 0;

        List<Statistic> statisticList = Statistics.getInstance().getStatsList();

        avg = avgRideAccomplished(StatisticsService.getLastNElementsOfTaxi(statisticList, id, n));

        return Response.ok(avg).build();
    }

    // get Average Ride Accomplished
    @Path("get/all/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getRideAccomplished(@PathParam("t1") int t1, @PathParam("t2") int t2) {
        List<Statistic> statisticList = Statistics.getInstance().getStatsList();
        List<Statistic> subSetInTimestamp = null;

        for (Statistic s : statisticList) {
            if (s.getStartingTime() >= t1 && s.getStartingTime() < t2)
                if (s.getEndTime() <= t2 && s.getStartingTime() > t1)
                    subSetInTimestamp.add(s);
        }

        Statistic statistic = new Statistic();

        statistic.setStartingTime(t1);
        statistic.setEndTime(t2);
        statistic.setBatteryLevel(avgBatteryLevel(subSetInTimestamp));
        // This is not really the average since we cast to int
        statistic.setNumberOfRides((int) avgRideAccomplished(subSetInTimestamp));
        statistic.setNumberOfKmTraveled(avgTravelledKm(subSetInTimestamp));
        statistic.setPollution(avgPollutionLevel(subSetInTimestamp));

        return Response.ok(statistic).build();
    }

    public static List<Statistic> getLastNElementsOfTaxi(List<Statistic> statisticList, int taxiId, int n) {
        // Check if list is long at least n
        if (statisticList.size() <= n) {
            n = statisticList.size();
        }

        List<Statistic> statsList = new ArrayList<Statistic>();

        // Get all stats of the specific Taxi
        for(Statistic s: statisticList){
            if(s.getIdTaxi() == taxiId)
                statsList.add(s);
        }

        // Take the last n elements of the list
        // size = 10
        // 0-9
        // last 3
        // 7-8-9
        // sublist start is inclusive (size - n)
        // subList end is exclusive so (size)
        statsList = statsList.subList(statsList.size() - n, statsList.size());

        return statsList;
    }

    public static float avgTravelledKm(List<Statistic> statisticList) {
        float avg = 0;

        for (Statistic s : statisticList) {
            avg += s.getNumberOfKmTraveled();
        }

        avg = avg / statisticList.size();

        return avg;
    }

    public static float avgBatteryLevel(List<Statistic> statisticList) {
        float avg = 0;

        for (Statistic s : statisticList) {
            avg += s.getBatteryLevel();
        }

        avg = avg / statisticList.size();

        return avg;
    }

    public static float avgPollutionLevel(List<Statistic> statisticList) {
        float avg = 0;

        for (Statistic s : statisticList) {
            avg += s.getPollution();
        }

        avg = avg / statisticList.size();

        return avg;
    }

    public static float avgRideAccomplished(List<Statistic> statisticList) {
        float avg = 0;

        for (Statistic s : statisticList) {
            avg += s.getNumberOfRides();
        }

        avg = avg / statisticList.size();

        return avg;
    }

    public static Statistic avgOfAll(List<Statistic> statisticList){
        Statistic avgStat = new Statistic();

        for(Statistic s: statisticList){
            avgStat.setNumberOfKmTraveled(avgStat.getNumberOfKmTraveled() + s.getNumberOfKmTraveled());
            avgStat.setNumberOfRides(avgStat.getNumberOfRides() + s.getNumberOfRides());
            avgStat.setBatteryLevel(avgStat.getBatteryLevel() + s.getBatteryLevel());
            avgStat.setPollution(avgStat.getPollution() + s.getPollution());
        }

        avgStat.setNumberOfKmTraveled(avgStat.getNumberOfKmTraveled() / statisticList.size());
        avgStat.setNumberOfRides(avgStat.getNumberOfRides() / statisticList.size());
        avgStat.setBatteryLevel(avgStat.getPollution() / statisticList.size());
        avgStat.setPollution(avgStat.getPollution() / statisticList.size());

        return avgStat;
    }
}
