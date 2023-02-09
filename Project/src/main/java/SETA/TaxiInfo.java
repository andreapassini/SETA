package SETA;

public class TaxiInfo {
    private int id;
    private int district;

    public TaxiInfo(int id, int district) {
        this.id = id;
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }
}
