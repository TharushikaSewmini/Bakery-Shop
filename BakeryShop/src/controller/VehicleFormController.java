package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Vehicle;
import util.ValidationUtil;
import view.tm.VehicleTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class VehicleFormController {
    public JFXTextField txtVehicleId;
    public JFXTextField txtVehicleType;
    public TableView<VehicleTM> tblVehicle;
    public TableColumn colId;
    public TableColumn colType;
    public TableColumn colOption;
    public TextField txtSearchVehicleId;
    public JFXButton btnSaveVehicle;
    public JFXButton btnUpdateVehicle;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern idPattern = Pattern.compile("^[A-Z]{1,2}-[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-Z][A-z ]{2,10}$");

        map.put(txtVehicleId, idPattern);
        map.put(txtVehicleType, namePattern);

        // Initializing table columns with VehicleTM properties
        colId.setCellValueFactory(new PropertyValueFactory<>("number"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadAllVehicle(); // Load all Vehicle to the TableView
    }


    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnSaveVehicle);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSaveVehicle);
            //if the response is a text field
            //that means there is a error
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();// if there is a error just focus it
            }
        }
    }


    //reset border colors to default color
    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color:white");
            textField.setStyle("-fx-prompt-text-fill: #b2b2b2");
        }
    }



    ObservableList<VehicleTM> tmList = FXCollections.observableArrayList();

    public void saveVehicleOnAction(ActionEvent actionEvent) {
        Vehicle v = new Vehicle(
                txtVehicleId.getText(), txtVehicleType.getText()
        );

        // Add new Vehicle to Vehicle table
        try {
            boolean isVehicleSaved = new VehicleCrudController().saveVehicle(v);
            if (isVehicleSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        // Clear the table data
        tmList.clear();
        loadAllVehicle(); // Load all Vehicle to the TableView

    }



    private void loadAllVehicle() {
        try {
            ObservableList<Vehicle> obList = FXCollections.observableArrayList(VehicleCrudController.getVehicleDetails());

        for (Vehicle vehicle : obList
        ) {
            Button btn= new Button("Delete");

            VehicleTM tm= new VehicleTM(vehicle.getNumber(),vehicle.getType(), btn);
            tmList.add(tm);

            btn.setOnAction(e->{
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                        "Are You Sure?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().equals(ButtonType.YES)){

                    // Delete Vehicle from Vehicle table
                    try{
                        boolean isVehicleDeleted = new VehicleCrudController().deleteVehicle(vehicle);
                        if (isVehicleDeleted) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                        }else{
                            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                        }

                    }catch (SQLException | ClassNotFoundException ec){
                        ec.printStackTrace();
                    }

                    // Remove Vehicle From tmList
                    tmList.remove(tm);
                }
            });
            tblVehicle.setItems(tmList);
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void updateVehicleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ObservableList<Vehicle> obList = FXCollections.observableArrayList(VehicleCrudController.getVehicleDetails());

        for (Vehicle tm :obList
        ) {
            if(tm.getNumber().equals(txtVehicleId.getText())){
                tm.setType(txtVehicleType.getText());

                // Update Vehicle From Vehicle table
                try{
                    boolean isVehicleUpdated = new VehicleCrudController().updateVehicle(tm);
                    if (isVehicleUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                    }else{
                        new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                    }

                }catch (SQLException | ClassNotFoundException e){
                    e.printStackTrace();
                }

                tmList.clear(); // Clear the table data
                loadAllVehicle();

                return;
            }
        }
    }

    public void addVehicleOnAction(ActionEvent actionEvent) {
        txtVehicleId.clear();
        txtVehicleType.clear();
        txtSearchVehicleId.clear();
        txtVehicleId.requestFocus();
        setBorders(txtVehicleId,txtVehicleType);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        String selectedVehicleId=txtSearchVehicleId.getText();

        // Search Vehicle from Vehicle table
        try {
            Vehicle v= VehicleCrudController.getVehicle(selectedVehicleId);

            if(v!=null){
                txtVehicleId.setText(v.getNumber());
                txtVehicleType.setText(v.getType());
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
