package Test;

import java.util.Scanner;

public class Interruptor extends Thread {
    head h;

    public Interruptor(head h1) {
        h = h1;
    }

    @Override
    public void run() {
        int i = 0;

        System.out.println("Waiting for line");
        Scanner userInput = new Scanner(System.in);
        String input = userInput.nextLine();

        h.Inter(2);
    }
}
