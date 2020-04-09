package warehouse.admin;

public class Airlines {

    private String code;
    private String name;
    private String country;

    Airlines(String code, String name, String country) {
        this.code = code;
        this.name = name;
        this.country = country;
    }

    public String getairline_code() {
        return code;
    }

    public String getairline_name() {
        return name;
    }

    public String getairline_country() {
        return country;
    }

}
