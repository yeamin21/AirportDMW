/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warehouse.admin;

/**
 *
 * @author yeami
 */
public class AirlineAircrafts {
    private String al_ar_code, ar_code, al_code;

    public AirlineAircrafts(String al_ar_code, String ar_code, String al_code) {
        this.al_ar_code = al_ar_code;
        this.ar_code = ar_code;
        this.al_code = al_code;
    }

    public String getAl_ar_code() {
        return al_ar_code;
    }

    public void setAl_ar_code(String al_ar_code) {
        this.al_ar_code = al_ar_code;
    }

    public String getAr_code() {
        return ar_code;
    }

    public void setAr_code(String ar_code) {
        this.ar_code = ar_code;
    }

    public String getAl_code() {
        return al_code;
    }

    public void setAl_code(String al_code) {
        this.al_code = al_code;
    }
    
}
