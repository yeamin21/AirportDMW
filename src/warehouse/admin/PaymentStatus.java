package warehouse.admin;

public class PaymentStatus {

    private int code;
    private String status;

    PaymentStatus(int id, String name) {
        this.code = id;
        this.status = name;
    }

    public int getpayment_id() {
        return code;
    }

    public String getpayment_status() {
        return status;
    }
}
