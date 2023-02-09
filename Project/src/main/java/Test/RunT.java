package Test;

public class RunT implements Runnable{
    head head;

    RunT(head h) {
        this.head = h;
    }

    public void run() {
        int i = 0;

        while (true){
            try {
                Thread.sleep(3* 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //head.InterP(i);
            i++;
        }
    }
}
