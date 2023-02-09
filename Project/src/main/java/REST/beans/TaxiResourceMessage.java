package REST.beans;

import REST.beans.TaxiResource;
import Taxi.Taxi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxiResourceMessage {
    private List<TaxiResource> list;

    public TaxiResourceMessage(){}

    public TaxiResourceMessage(List<TaxiResource> l){
        this.list = new ArrayList<TaxiResource>(l);
    }

    public List<TaxiResource> getList(){
        return list;
    }

    public void setList(List<TaxiResource> list) {
        this.list = list;
    }
}
