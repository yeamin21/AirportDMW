package warehouse.admin;

public class BookingDatas {

    private String code;
    private String airline_code;
    private String aircraft_code;
    private String origin_airport;
    private String destination_airport;
    private String departure_date;
    private String arrival_date;
    private float base_price;

    BookingDatas(String code, String origin_airport, String destination_airport, String airline, String aircraft, String date1, String date2, float base_price) {
        this.code = code;
        this.origin_airport = origin_airport;
        this.destination_airport = destination_airport;
        this.airline_code = airline;
        this.aircraft_code = aircraft;
        this.departure_date = date1;
        this.arrival_date = date2;
        this.base_price = base_price;
    }

    public String getflight_code() {
        return code;
    }

    public String getorigin_airport() {
        return origin_airport;
    }

    public String getdestination_airport() {
        return destination_airport;
    }

    public String getairline_code() {
        return airline_code;
    }

    public String getaircraft_code() {
        return aircraft_code;
    }

    public String getdeparture_date() {
        return departure_date;
    }

    public String getarrival_date() {
        return arrival_date;
    }

    public float getbase_price() {
        return base_price;
    }
}
