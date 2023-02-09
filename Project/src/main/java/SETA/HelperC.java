package SETA;

import java.util.List;

public class HelperC {
    public static int calculateDistrict(Position p){
        int district = 0;

        if(p.getX() < 5){
            // District I or IV
            if(p.getY() < 5){
                // District I
                district = 1;
            } else {
                // District IV
                district = 4;
            }
        } else {
            // District II or III
            if(p.getY() < 5){
                // District II
                district = 2;
            } else {
                // District III
                district = 3;
            }
        }

        return district;
    }

    public static int distance(Position to, Position from){
        int d = (int) Math.sqrt((to.getX() - from.getX())^2 + (to.getY() - from.getY())^2);

        return d;
    }

    public static boolean contains(List<RideRequest> list, RideRequest rideRequest){
        for (RideRequest r: list){
            if(r.getId() == rideRequest.getId()){
                return true;
            }
        }
        return false;
    }

    public static void remove(List<RideRequest> list, RideRequest rideRequest) {
        for (RideRequest r : list) {
            if (r.getId() == rideRequest.getId()) {
                list.remove(r);
                return;
            }
        }
    }
}
