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
import model.Employee;
import util.ValidationUtil;
import view.tm.EmployeeTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class EmployeeFormController {
    public JFXTextField txtEmployeeId;
    public JFXTextField txtEmployeeName;
    public JFXTextField txtEmployeeAddress;
    public JFXTextField txtEmployeeContact;
    public TableView<EmployeeTM> tblEmployee;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colOption;
    public TextField txtSearchEmployeeId;
    public JFXButton btnSaveEmployee;
    public JFXButton btnUpdateEmployee;


    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern idPattern = Pattern.compile("^(E)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern contactPattern = Pattern.compile("^[0][0-9]{9}$");

        map.put(txtEmployeeId, idPattern);
        map.put(txtEmployeeName, namePattern);
        map.put(txtEmployeeAddress, addressPattern);
        map.put(txtEmployeeContact, contactPattern);

        // Initializing table columns with EmployeeTM properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setEmployeeId(); // get next employeeId
        loadAllEmployees(); // load all employees from database

    }

    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnSaveEmployee);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSaveEmployee);
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


    // Set Next EmployeeId
    public void setEmployeeId(){
        try {
            String eId = EmployeeCrudController.getEmployeeId(1);
            txtEmployeeId.setText(eId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Create ObservableList for add values of tm to Table
    ObservableList<EmployeeTM> tmList = FXCollections.observableArrayList();

    public void saveEmployeeOnAction(ActionEvent actionEvent) {
        Employee em = new Employee(
                txtEmployeeId.getText(), txtEmployeeName.getText(), txtEmployeeAddress.getText(), Integer.parseInt(txtEmployeeContact.getText())
        );

        // Add new Employee to Employee table
        try {
            boolean isEmployeeSaved = new EmployeeCrudController().saveEmployee(em);
            if (isEmployeeSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException E) {
            E.printStackTrace();
            new Alert(Alert.AlertType.ERROR, E.getMessage()).show();
        }

        tmList.clear(); // Clear the table data

        loadAllEmployees(); // load all employees
    }


    public void updateEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ObservableList<Employee> obList = FXCollections.observableArrayList(EmployeeCrudController.getEmployeeDetails());

        for (Employee tm :obList
        ) {
            if(tm.getId().equals(txtEmployeeId.getText())){
                tm.setName(txtEmployeeName.getText());
                tm.setAddress((txtEmployeeAddress.getText()));
                tm.setContact(Integer.parseInt(txtEmployeeContact.getText()));

                // Update Employee From Employee table
                try{
                    boolean isEmployeeUpdated = new EmployeeCrudController().updateEmployee(tm);
                    if (isEmployeeUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();

                    }else{
                        new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                    }

                }catch (SQLException | ClassNotFoundException e){
                    e.printStackTrace();
                }

                tmList.clear(); // Clear the table data
                loadAllEmployees(); // load all employees

                return;
            }
        }

    }

    private void loadAllEmployees() {
        try {
            ObservableList<Employee> obList = FXCollections.observableArrayList(EmployeeCrudController.getEmployeeDetails());

            for (Employee em : obList
            ) {
                Button btn= new Button("Delete");

                EmployeeTM tm= new EmployeeTM(em.getId(),em.getName(),em.getAddress(),em.getContact(), btn);
                tmList.add(tm);

                btn.setOnAction(e->{
                    Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                            "Are You Sure?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get().equals(ButtonType.YES)){

                        // Delete Employee from Employee table in database
                        try{
                            boolean isEmployeeDeleted = new EmployeeCrudController().deleteEmployee(em);
                            if (isEmployeeDeleted) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                            }else{
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        }catch (SQLException | ClassNotFoundException ec){
                            ec.printStackTrace();
                        }

                        // Remove Employee From tmList
                        tmList.remove(tm);
                    }
                });
            }
            tblEmployee.setItems(tmList);

        } catch (SQLException c) {
            c.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Clear textFields for add new Employee
    public void addEmployeeOnAction(ActionEvent actionEvent) {
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtEmployeeAddress.clear();
        txtEmployeeContact.clear();
        txtSearchEmployeeId.clear();
        setEmployeeId();
        txtEmployeeName.requestFocus();
        setBorders(txtEmployeeId,txtEmployeeName,txtEmployeeAddress,txtEmployeeContact);
    }

    // Search Employee from Employee table
    public void txtSearchOnAction(ActionEvent actionEvent) {
        String selectedEmployeeId=txtSearchEmployeeId.getText();

        try {
            Employee employee= EmployeeCrudController.getEmployee(selectedEmployeeId);

            if(employee!=null){
                txtEmployeeId.setText(employee.getId());
                txtEmployeeName.setText(employee.getName());
                txtEmployeeAddress.setText(employee.getAddress());
                txtEmployeeContact.setText(String.valueOf(employee.getContact()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
