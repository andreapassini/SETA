package REST.beans;

import SETA.Position;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxiResource {
    private int id;
    private String iPAddress;
    private int port;

    private Position position;

    private int pos_X;
    private int pos_Y;

    private int district;

    public TaxiResource(){}

    public TaxiResource(int id, String ip, int port) {
        this.id = id;
        this.iPAddress = ip;
        this.port = port;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public String getiPAddress() {
        return iPAddress;
    }

    public int getPort() {
        return port;
    }

    public Position getPosition() {
        return position;
    }

    public int getPos_X(){
        return pos_X;
    }

    public int getPos_Y(){
        return pos_Y;
    }

    public void setPos_X(int pos_X) {
        this.pos_X = pos_X;
    }

    public void setPos_Y(int pos_Y) {
        this.pos_Y = pos_Y;
    }

    public String stampPosition(){
        String response = position.getX() + " - " + position.getY();
        return response;
    }

    public void setPosition(Position position) {
        this.position = position;
        this.pos_X = position.getX();
        this.pos_Y = position.getY();
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }
}
