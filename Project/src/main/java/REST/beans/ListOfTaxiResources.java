package REST.beans;

import SETA.Position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class ListOfTaxiResources {
    @XmlElement(name="my_taxis")
    private volatile List<TaxiResource> list;

    private static ListOfTaxiResources instance;

    private ListOfTaxiResources() {list = new ArrayList<TaxiResource>();}

    //singleton
    public synchronized static ListOfTaxiResources getInstance(){
        if(instance==null)
            instance = new ListOfTaxiResources();
        return instance;
    }

    // Lock the resource ListOfTaxiResources and do together
    //  to check ID
    //  add ID
    // Without releasing the lock
    public synchronized boolean AddTaxi(TaxiResource taxiResource){
        if(getTaxiById(taxiResource.getId()) == null){
            // Generate rnd position in the recharger
            Position startingPosition = Position.GenerateStartingPosition();
            // Add this starting Position to the list so when the taxi will get the list will also get its positionl
            taxiResource.setPos_X(startingPosition.getX());
            taxiResource.setPos_Y(startingPosition.getY());
            this.list.add(taxiResource);
            return true;
        }
        return false;
    }

    // Remove with lock
    // to avoid
    //  remove
    //      getTaxiById
    //  at the same time AddTaxi
    //  this lock the list
    //  if I'm checking with the same id I'm removing
    //  I will not be able to add even if this id will be removed the next instruction
    public synchronized void RemoveTaxi(int taxiId){
        TaxiResource t = getTaxiById(taxiId);
        this.list.remove(t);
    }

    public synchronized List<TaxiResource> getTaxiList() {
        return new ArrayList<>(list);
    }

    public TaxiResource getTaxiById(int id){
        for(TaxiResource t: this.list)
            if(t.getId() == id)
                return t;
        return null;
    }
}
