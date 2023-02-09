package SETA;

public class IDGenerator {

    private static IDGenerator instance = null;

    private int id;

    private IDGenerator(){
        id=0;
    }

    public static IDGenerator getInstance(){
        // Create the object only if it does not exist
        if (instance == null){
            instance = new IDGenerator();
        }
        return instance;
    }

    public synchronized int getID(){
        id++;
        return id;
    }

}
