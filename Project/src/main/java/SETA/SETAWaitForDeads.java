package SETA;

import java.util.List;

public class SETAWaitForDeads {

    private SETA seta;
    private int timer;
    private List<RideRequest> listPreviouslyInTravel;

    public SETAWaitForDeads(SETA s, int timerInSec, List<RideRequest> listPreviouslyInTravel) throws InterruptedException {
        this.timer = timerInSec;
        this.listPreviouslyInTravel = listPreviouslyInTravel;

        Thread.sleep(timerInSec);
    }


}
