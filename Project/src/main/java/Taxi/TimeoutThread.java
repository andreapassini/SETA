package Taxi;

public class TimeoutThread extends Thread{
    Taxi taxi;

    public TimeoutThread(Taxi t){
        this.taxi = t;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        taxi.setBusy(false);

        //taxi.setRideRequest(null);
        interrupt();
    }
}
