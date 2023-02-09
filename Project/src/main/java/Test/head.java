package Test;

public class head {
    public head() throws InterruptedException {
        Interruptor interruptor = new Interruptor(this);
        interruptor.start();

        int i = 0;

        while (true){
            System.out.println(i);
            i++;

            Thread.sleep(1*1000);
        }
    }

    public void Inter(int ii){
        System.out.println("Inter " + ii);
    }
}
