package warehouse.admin;

public class TravelClasses {

    private String class_code;
    private Float class_description;

    TravelClasses(String code, Float description) {
        this.class_code = code;
        this.class_description = description;
    }

    public String getclass_code() {
        return class_code;
    }

    public Float getclass_description() {
        return class_description;
    }
}
