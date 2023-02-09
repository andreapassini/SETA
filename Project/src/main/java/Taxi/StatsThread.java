package Taxi;

public class StatsThread extends Thread{
    private Taxi taxi;
    private int timer = 15;

    // public variables
    public boolean stopCondition = false;

    public StatsThread(Taxi t, int waitTime){
        this.taxi = t;
        timer = waitTime;
    }

    public void run() {
        while(!stopCondition){
            try {
                Thread.sleep(timer*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            taxi.SendStatsOut();
        }

        interrupt();
    }
}
