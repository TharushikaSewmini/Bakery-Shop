package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.SQLException;
import java.util.HashMap;

public class DeliveryOrderFormController {

    public JFXTextField txtDeliveryOrderId;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXComboBox<String> cmbOrderId;
    public JFXTextField txtOrderDate;
    public JFXComboBox<String> cmbDriverId;
    public JFXTextField txtDriverName;
    public JFXTextField txtDriverAddress;
    public JFXTextField txtDriverContact;

    public JFXTextField txtVehicleType;
    public JFXTextField txtDeliveryCharge;
    public JFXComboBox<String> cmbVehicleNumber;


    public void initialize(){

        setDeliveryOrderId(); // get next deliveryOrderId
        setOrderIds();
        setDriverIds();
        setVehicleNumbers();

        //------------------------------------------
        cmbOrderId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setOrderDetails(newValue);
            setCustomerDetails(newValue);
        });

        cmbDriverId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setDriverDetails(newValue);
        });
        cmbVehicleNumber.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setVehicleDetails(newValue);
        });

    }



    // Set Next DeliveryOrderId
    public void setDeliveryOrderId(){
        try {
            String doId = DeliveryOrderCrudController.getDeliveryOrderId(1);
            txtDeliveryOrderId.setText(doId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    // Load Order to ComboBox
    public void setOrderIds(){
        try {
            ObservableList<String> oIdObList = FXCollections.observableList(OrderCrudController.getOrderIds()
            );
            cmbOrderId.setItems(oIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    // Load Order Name,Address,Contact to the TextFields
    private void setOrderDetails(String selectedOrderId) {
        try {
            Order o= OrderCrudController.getOrder(selectedOrderId);
            if(o!=null){
                txtOrderDate.setText(o.getDate());
                txtId.setText(o.getCustomerId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    // Load Customer Name,Address,Contact to the TextFields
    private void setCustomerDetails(String selectedCustomerId) {
        try {
            Customer c= CustomerCrudController.getCustomer(txtId.getText());
            if(c!=null){
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtContact.setText(String.valueOf(c.getContact()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load setDriverIds to ComboBox
    private void setDriverIds() {
        try {
            cmbDriverId.setItems(FXCollections.observableList(DriverCrudController.getDriverIds()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load setDriver Name,Address,Contact to the TextFields
    private void setDriverDetails(String selectedDriverId) {
        try {
            Driver i=DriverCrudController.getDriver(selectedDriverId);
            if(i!=null){
                txtDriverName.setText(i.getName());
                txtDriverAddress.setText(i.getAddress());
                txtDriverContact.setText(String.valueOf(i.getContact()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Load setVehicleNumbers to ComboBox
    private void setVehicleNumbers() {
        try {
            cmbVehicleNumber.setItems(FXCollections.observableList(VehicleCrudController.getVehicleNumbers()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load setVehicle Number,Type to the TextFields
    private void setVehicleDetails(String selectedVehicleId) {
        try {
            Vehicle i=VehicleCrudController.getVehicle(selectedVehicleId);
            if(i!=null){
                txtVehicleType.setText(i.getType());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void saveDeliveryOrderOnAction(ActionEvent actionEvent) {

        DeliveryOrder isDeliveryOrderIdExists = isDeliveryOrderIdExists(txtDeliveryOrderId.getText());
        if (isDeliveryOrderIdExists!=null) { // if deliveryOrder Id is already exists
            new Alert(Alert.AlertType.ERROR, "DeliveryOrder Id is Exists!").show();
        }else {
            DeliveryOrder deliveryOrder = new DeliveryOrder(
                    txtDeliveryOrderId.getText(),
                    txtOrderDate.getText(),
                    txtId.getText(),
                    cmbDriverId.getValue(),
                    cmbVehicleNumber.getValue()
            );

            // Add Delivery Order to DeliveryOrder table
            try {
                boolean isDeliveryOrderSaved = new DeliveryOrderCrudController().saveDeliveryOrder(deliveryOrder);
                if (isDeliveryOrderSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e);
            }
        }
    }


    // Check the deliveryOrderId is exists
    private DeliveryOrder isDeliveryOrderIdExists(String DeliveryOrderId) {

        try {
            ObservableList<DeliveryOrder> obList = FXCollections.observableArrayList(DeliveryOrderCrudController.getAllDeliveryOrderIds());
            for (DeliveryOrder id : obList
            ) {
                if (id.getId().equals(DeliveryOrderId)) {
                    return id;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }





    public void printInvoiceOnAction(ActionEvent actionEvent) {
        String deliveryOrderId = txtDeliveryOrderId.getText();
        String orderId = cmbOrderId.getValue();
        String customerId = txtId.getText();
        String customerName = txtName.getText();
        String customerAddress = txtAddress.getText();
        int customerContact = Integer.parseInt(txtContact.getText());
        String driverName = txtDriverName.getText();
        String vehicleNumber = cmbVehicleNumber.getValue();
        double deliveryCharge = Double.parseDouble(txtDeliveryCharge.getText());

        HashMap paramMap = new HashMap();
        paramMap.put("deliveryOrderId", deliveryOrderId);// id = report param name // customerID = UI typed value
        paramMap.put("orderId", orderId);
        paramMap.put("customerId", customerId);
        paramMap.put("customerName", customerName);
        paramMap.put("customerAddress", customerAddress);
        paramMap.put("customerContact", customerContact);
        paramMap.put("driverName", driverName);
        paramMap.put("vehicleNumber", vehicleNumber);
        paramMap.put("deliveryCharge", deliveryCharge);

        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/DeliveryOrderBill.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }
        clearAllText(); // clear text
    }

    public void clearAllText() {
        txtDeliveryOrderId.clear();
        cmbOrderId.setValue(null);
        txtOrderDate.clear();
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        cmbDriverId.setValue(null);
        txtDriverName.clear();
        txtDriverAddress.clear();
        txtDriverContact.clear();
        cmbVehicleNumber.setValue(null);
        txtVehicleType.clear();
        txtDeliveryCharge.clear();
        setDeliveryOrderId();
        cmbOrderId.requestFocus();
    }
    
}
