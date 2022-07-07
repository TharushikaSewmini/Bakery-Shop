package view.tm;

import javafx.scene.control.Button;

public class EmployeeTM {
    private String id;
    private String name;
    private String address;
    private int contact;
    private Button btn;

    public EmployeeTM() {
    }

    public EmployeeTM(String id, String name, String address, int contact, Button btn) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "EmployeeTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact=" + contact +
                ", btn=" + btn +
                '}';
    }
}
