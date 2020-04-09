/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warehouse.admin;

public class AircraftTypes {

    private String code;
    private String name;
    private int capacity;

    AircraftTypes(String code, String name, int capacity) {
        this.code = code;
        this.name = name;
        this.capacity = capacity;
    }

    public String getaircraft_code() {
        return code;
    }

    public String getaircraft_name() {
        return name;
    }

    public int getaircraft_capacity() {
        return capacity;
    }

}
