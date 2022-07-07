package view.tm;

import javafx.scene.control.Button;

public class DriverTM {
    private String id;
    private String name;
    private String address;
    private int contact;
    private double salary;
    private Button btn;

    public DriverTM() {
    }

    public DriverTM(String id, String name, String address, int contact, double salary, Button btn) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.salary = salary;
        this.btn = btn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "DriverTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact=" + contact +
                ", salary=" + salary +
                ", btn=" + btn +
                '}';
    }
}
