package warehouse.admin;

public class BookingAgents {

    private String id;
    private String name;
    private String password;

    BookingAgents(String id, String name,String password) {
        this.id = id;
        this.name = name;
        this.password=password;
    }

    public String getagent_id() {
        return id;
    }

    public String getagent_name() {
        return name;
    }
     public String getagent_password() {
        return password;
    }
}
