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
import model.Customer;
import util.ValidationUtil;
import view.tm.CustomerTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class CustomerFormController {
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCustomerContact;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colOption;
    public TextField txtSearchId;
    public JFXButton btnSaveCustomer;
    public JFXButton btnUpdateCustomer;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern idPattern = Pattern.compile("^(C)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-Z][A-z ]{3,20}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern contactPattern = Pattern.compile("^[0-9]{9,10}$");

        map.put(txtCustomerId, idPattern);
        map.put(txtCustomerName, namePattern);
        map.put(txtCustomerAddress, addressPattern);
        map.put(txtCustomerContact, contactPattern);

        // Initializing table columns with CustomerTM properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setCustomerId(); // get next customerId
        loadAllCustomers(); // Load all Customers to the TableView

    }



    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnSaveCustomer);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSaveCustomer);
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
    public void setCustomerId(){
        try {
            String cId = CustomerCrudController.getCustomerId(1);
            txtCustomerId.setText(cId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c = new Customer(
                txtCustomerId.getText(), txtCustomerName.getText(), txtCustomerAddress.getText(), Integer.parseInt(txtCustomerContact.getText())
        );

        // Add new Customer to Customer table
        try {
            boolean isCustomerSaved = new CustomerCrudController().saveCustomer(c);
            if (isCustomerSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        // Clear the table data
        tmList.clear();

        // Load all Customers to the TableView
        loadAllCustomers();

        setBorders(txtCustomerId,txtCustomerName,txtCustomerAddress,txtCustomerContact);

    }


    private void loadAllCustomers() {

        try {
            ObservableList<Customer> obList = FXCollections.observableArrayList(CustomerCrudController.getCustomerDetails());

        for (Customer customer : obList
        ) {
            Button btn= new Button("Delete");

            CustomerTM tm= new CustomerTM(customer.getId(),customer.getName(),customer.getAddress(),customer.getContact(), btn);
            tmList.add(tm);

            btn.setOnAction(e->{
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                        "Are You Sure?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().equals(ButtonType.YES)){

                    // Delete Customer from Customer table
                    try{
                        boolean isCustomerDeleted = new CustomerCrudController().deleteCustomer(customer);
                        if (isCustomerDeleted) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                        }else{
                            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                        }
                    }catch (SQLException | ClassNotFoundException E){
                        E.printStackTrace();
                    }

                    // Remove Employee From tmList
                    tmList.remove(tm);
                }
            });
            tblCustomer.setItems(tmList);
        }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    // Clear textFields for add new Customer
    public void addCustomerOnAction(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerContact.clear();
        txtSearchId.clear();
        setCustomerId();
        txtCustomerName.requestFocus();
        setBorders(txtCustomerId,txtCustomerName,txtCustomerAddress,txtCustomerContact);
    }



    public void updateCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ObservableList<Customer> obList = FXCollections.observableArrayList(CustomerCrudController.getCustomerDetails());

        for (Customer tm :obList
        ) {
            if(tm.getId().equals(txtCustomerId.getText())){
                tm.setName(txtCustomerName.getText());
                tm.setAddress((txtCustomerAddress.getText()));
                tm.setContact(Integer.parseInt(txtCustomerContact.getText()));

                // Update Customer From Customer table
                try{
                    boolean isCustomerUpdated = new CustomerCrudController().updateCustomer(tm);
                    if (isCustomerUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                    }else{
                        new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                    }

                }catch (SQLException | ClassNotFoundException e){
                    e.printStackTrace();
                }

                // Clear the table data
                tmList.clear();
                loadAllCustomers();

                return;
            }
        }
    }



    public void txtSearchOnAction(ActionEvent actionEvent) {
        // Assign search TextField id
        String selectedCustomerId=txtSearchId.getText();

        // Search Customer from Customer table
        try {
            Customer c= CustomerCrudController.getCustomer(selectedCustomerId);

            if(c!=null){
                txtCustomerId.setText(c.getId());
                txtCustomerName.setText(c.getName());
                txtCustomerAddress.setText(c.getAddress());
                txtCustomerContact.setText(String.valueOf(c.getContact()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
