package REST.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Statistics {

    @XmlElement(name="my_stats")
    private List<Statistic> listOfStats;

    private static Statistics instance;

    private Statistics() {
        this.listOfStats = new ArrayList<Statistic>();
    }

    // do we need to synch this? Cant i just synch the add?
    public static Statistics getInstance(){
        if(instance == null)
            instance = new Statistics();
        return instance;
    }

    public synchronized void add(Statistic statistic){
        this.listOfStats.add(statistic);
    }

    public synchronized void remove(Statistic statistic){
        this.listOfStats.remove(statistic);
    }

    public List<Statistic> getStatsList() {
        return new ArrayList<>(listOfStats);
    }


}
