package view.tm;

import javafx.scene.control.Button;

public class VehicleTM {
    private String number;
    private String type;
    private Button btn;

    public VehicleTM() {
    }

    public VehicleTM(String number, String type, Button btn) {
        this.number = number;
        this.type = type;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "VehicleTM{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", btn=" + btn +
                '}';
    }
}
