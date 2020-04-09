package warehouse.admin;

public class TicketTypes {

    private String code;
    private Float ticket_description;

    TicketTypes(String code, Float description) {
        this.code = code;
        this.ticket_description = description;
    }

    public String getticket_code() {
        return code;
    }

    public Float getticket_description() {
        return ticket_description;
    }
}
