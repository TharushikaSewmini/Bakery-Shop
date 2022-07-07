package model;

public class Vehicle {
    private String number;
    private String type;

    public Vehicle() {
    }

    public Vehicle(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
