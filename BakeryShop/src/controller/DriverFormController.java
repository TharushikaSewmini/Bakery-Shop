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
import model.Driver;
import util.ValidationUtil;
import view.tm.DriverTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class DriverFormController {
    public JFXTextField txtDriverId;
    public JFXTextField txtDriverName;
    public JFXTextField txtDriverAddress;
    public JFXTextField txtDriverContact;
    public JFXTextField txtDriverSalary;
    public TableView<DriverTM> tblDriver;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOption;
    public TextField txtSearchDriverId;
    public JFXButton btnSaveDriver;
    public JFXButton btnUpdateDriver;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern idPattern = Pattern.compile("^(D)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-Z][A-z ]{3,20}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern contactPattern = Pattern.compile("^[0-9]{9,10}$");
        Pattern salaryPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtDriverId, idPattern);
        map.put(txtDriverName, namePattern);
        map.put(txtDriverAddress, addressPattern);
        map.put(txtDriverContact, contactPattern);
        map.put(txtDriverSalary, salaryPattern);

        // Initializing table columns with DriverTM properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setDriverId(); // get next driverId
        loadAllDrivers(); // Load all Driver to the TableView

    }


    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnSaveDriver);
//        TextField = error
//        boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSaveDriver);
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


    // Set Next DriverId
    public void setDriverId(){
        try {
            String dId = DriverCrudController.getDriverId(1);
            txtDriverId.setText(dId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    ObservableList<DriverTM> tmList = FXCollections.observableArrayList();

    public void saveDriverOnAction(ActionEvent actionEvent) {
        Driver d = new Driver(
                txtDriverId.getText(), txtDriverName.getText(), txtDriverAddress.getText(), Integer.parseInt(txtDriverContact.getText()), Double.parseDouble(txtDriverSalary.getText())
        );

        // Add new Driver to Driver table
        try {
            boolean isDriverSaved = new DriverCrudController().saveDriver(d);
            if (isDriverSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        // Clear the table data
        tmList.clear();
        loadAllDrivers(); // Load all Driver to the TableView

        setBorders(txtDriverId,txtDriverName,txtDriverAddress,txtDriverContact,txtDriverSalary);
    }


    private void loadAllDrivers() {
        try {
            ObservableList<Driver> obList = FXCollections.observableArrayList(DriverCrudController.getDriverDetails());

        for (Driver driver : obList
        ) {
            Button btn= new Button("Delete");

            DriverTM tm= new DriverTM(driver.getId(),driver.getName(),driver.getAddress(),driver.getContact(),driver.getSalary(), btn);
            tmList.add(tm);

            btn.setOnAction(e->{
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                        "Are You Sure?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().equals(ButtonType.YES)){

                    // Delete Driver from Driver table
                    try{
                        boolean isDriverDeleted = new DriverCrudController().deleteDriver(driver);
                        if (isDriverDeleted) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                        }else{
                            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                        }
                    }catch (SQLException | ClassNotFoundException ec){
                        ec.printStackTrace();
                    }

                    // Remove Driver From tmList
                    tmList.remove(tm);
                }
            });
            tblDriver.setItems(tmList);
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void updateDriverOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ObservableList<Driver> obList = FXCollections.observableArrayList(DriverCrudController.getDriverDetails());

        for (Driver tm :obList
        ) {
            if(tm.getId().equals(txtDriverId.getText())){
                tm.setName(txtDriverName.getText());
                tm.setAddress((txtDriverAddress.getText()));
                tm.setContact(Integer.parseInt(txtDriverContact.getText()));
                tm.setSalary(Double.parseDouble(txtDriverSalary.getText()));

                // Update Driver From Driver table
                try{
                    boolean isDriverUpdated = new DriverCrudController().updateDriver(tm);
                    if (isDriverUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                    }else{
                        new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                    }


                }catch (SQLException | ClassNotFoundException e){
                    e.printStackTrace();
                }

                // Clear the table data
                tmList.clear();
                loadAllDrivers();

                return;
            }
        }
    }

    public void addDriverOnAction(ActionEvent actionEvent) {
        txtDriverId.clear();
        txtDriverName.clear();
        txtDriverAddress.clear();
        txtDriverContact.clear();
        txtDriverSalary.clear();
        txtSearchDriverId.clear();
        setDriverId();
        txtDriverName.requestFocus();
        setBorders(txtDriverId,txtDriverName,txtDriverAddress,txtDriverContact,txtDriverSalary);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {

        String selectedDriverId=txtSearchDriverId.getText();

        // Search Driver from Driver table
        try {
            Driver d= DriverCrudController.getDriver(selectedDriverId);

            if(d!=null){
                txtDriverId.setText(d.getId());
                txtDriverName.setText(d.getName());
                txtDriverAddress.setText(d.getAddress());
                txtDriverContact.setText(String.valueOf(d.getContact()));
                txtDriverSalary.setText(String.valueOf(d.getSalary()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
