package warehouse.admin;

public class Airports {

    private String code;
    private String name;
    private String location;

    Airports(String code, String name, String location) {
        this.code = code;
        this.name = name;
        this.location = location;
    }

    public String getAirport_Code() {
        return code;
    }

    public String getAirport_Name() {
        return name;
    }

    public String getAirport_Location() {
        return location;
    }
}
