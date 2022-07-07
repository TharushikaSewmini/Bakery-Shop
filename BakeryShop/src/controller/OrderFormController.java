package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import model.Order;
import model.OrderDetails;
import model.Product;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ValidationUtil;
import view.tm.CartTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class OrderFormController {
    public JFXTextField txtOrderId;
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXComboBox<String> cmbProductCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public JFXTextField txtDiscount;
    public TableView<CartTM> tblOrder;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colDiscount;
    public TableColumn colTotalCost;
    public TableColumn colOption;
    public Label lblTotalCost;
    public AnchorPane orderContext;
    public JFXButton btnAddToCart;
    public JFXDatePicker dateOrderDate;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern idPattern = Pattern.compile("^(O)[0-9]{3,5}$");;
        Pattern qtyPattern = Pattern.compile("^[0-9]{1,}$");
        Pattern discountPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtOrderId, idPattern);
        map.put(txtQty, qtyPattern);
        map.put(txtDiscount, discountPattern);


        // Initializing table columns with CartTM properties
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setOrderId(); //get Next Order Id
        setCustomerIds(); //get Customer Ids From Database
        setProductCodes(); //get Product codes From Database

        //---------------------------
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerDetails(newValue);
        });
        cmbProductCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setProductDetails(newValue);
        });

    }


    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnAddToCart);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddToCart);
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


    // Set Next OrderId
    public void setOrderId(){
        try {
            String oId = OrderCrudController.getOrderId(1);
            txtOrderId.setText(oId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Load CustomerIds to ComboBox
    public void setCustomerIds(){
        try {
            // Create String type ObservableList and assign the customer Ids to it
            // load the ObservableList to combo box
            cmbCustomerId.setItems(FXCollections.observableList(CustomerCrudController.getCustomerIds()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load Customer Name,Address,Contact to the TextFields
    private void setCustomerDetails(String selectedCustomerId) {
        try {
            Customer c= CustomerCrudController.getCustomer(selectedCustomerId);
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

    // Load ProductCodes to ComboBox
    private void setProductCodes() {
        try {
            // Create String type ObservableList and assign the Product codes to it
            // load the ObservableList to ComboBox
            cmbProductCode.setItems(FXCollections.observableList(ProductCrudController.getProductCodes()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load Product Description,QtyOnHand,UnitPrice to the TextFields
    private void setProductDetails(String selectedItemCode) {
        try {
            // Create Product Object and assign the Product codes to it
            // get the values from Object and set them to TextFields
            Product i=ProductCrudController.getProduct(selectedItemCode);
            if(i!=null){
                txtDescription.setText(i.getDescription());
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Load values to the Table
    ObservableList<CartTM> tmList = FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        double totalCost = (unitPrice * qty)-discount;


        Order isOrderIdExists = isOrderIdExists(txtOrderId.getText());
        if (isOrderIdExists!=null) { // if order is already exists
            new Alert(Alert.AlertType.ERROR, "Order Id is Exists!").show();
        }else {

            CartTM isExists = isExists(cmbProductCode.getValue());

            if (isExists != null) { // if productCode is already exists
                for (CartTM temp : tmList
                ) {
                    if (temp.equals(isExists)) { // if productCode is in tmList
                        // update it's qty, discount and total Cost in Table
                        temp.setQty((temp.getQty() + qty));
                        temp.setDiscount((temp.getTotalCost() + discount));
                        temp.setTotalCost(temp.getTotalCost() + totalCost);
                    }
                }
            } else {
                // Add new values
                Button btn = new Button("Delete");

                // Get values and assign them to CartTM Object
                CartTM tm = new CartTM(
                        cmbProductCode.getValue(),
                        txtDescription.getText(),
                        unitPrice,
                        qty,
                        discount,
                        totalCost,
                        btn
                );

                btn.setOnAction(e -> {
                    tmList.remove(tm);
                    calculateTotal(); // after removing values, Calculate total Cost
                });

                tmList.add(tm); // Add values of CartTM Object to tmList
                tblOrder.setItems(tmList); // Pass tmList to Table
            }
        }
        tblOrder.refresh(); // get the updated values
        calculateTotal();

        setBorders(txtOrderId,txtQty,txtDiscount);
    }


    // Check the product code is exists
    private CartTM isExists(String productCode) {
        for (CartTM tm : tmList
        ) {
            if (tm.getCode().equals(productCode)) {
                return tm;
            }
        }
        return null;
    }



    // Check the orderId is exists
    private Order isOrderIdExists(String orderId) {

        try {
            ObservableList<Order> obList = FXCollections.observableArrayList(OrderCrudController.getAllOrderIds());
        for (Order order : obList
        ) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }





    // Calculate Total Cost and set it to Label
    private void calculateTotal() {
        double total = 0;
        for (CartTM tm : tmList
        ) {
            total+=tm.getTotalCost();
        }
        lblTotalCost.setText(String.valueOf(total));
    }


    public void newCustomerOnAction(ActionEvent actionEvent) throws IOException {
        orderContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"));
        orderContext.getChildren().add(parent);
    }


    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException {

        //txtOrderDate.setText(lblDate.getText());
        Order order = new Order(
                txtOrderId.getText(),
                String.valueOf(dateOrderDate.getValue()),
                cmbCustomerId.getValue()
        );

        ArrayList<OrderDetails> details = new ArrayList<>();
        for (CartTM tm : tmList
        ) {
            // add the all data in the table to ArrayList
            details.add(
                    new OrderDetails(
                            order.getId(),
                            tm.getCode(),
                            tm.getQty(),
                            tm.getUnitPrice(),
                            tm.getDiscount()
                    )
            );
        }

        //------------------

        Connection connection = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isOrderSaved = new OrderCrudController().saveOrder(order);
            if (isOrderSaved) {
                boolean isDetailsSaved = new OrderCrudController().saveOrderDetails(details);
                if (isDetailsSaved) {
                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error!").show();
                }
            } else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Error!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }


    public void clearAllTexts() {
        txtOrderId.clear();
        dateOrderDate.setValue(null);
        cmbCustomerId.setValue(null);
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        cmbProductCode.setValue(null);
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        txtDiscount.clear();
        tblOrder.setItems(null);
        lblTotalCost.setText(null);
        setOrderId(); // set next orderId
        dateOrderDate.requestFocus();
        setBorders(txtOrderId,txtQty,txtDiscount);
    }



    // Print customer invoice
    public void printInvoiceOnAction(ActionEvent actionEvent) {
        //We should gather information to send to the report
        String customerName = txtName.getText();
        String orderId = txtOrderId.getText();
        double total = Double.parseDouble(lblTotalCost.getText());

        HashMap paramMap = new HashMap();
        paramMap.put("customerName", customerName);// id = report param name // customerID = UI typed value
        paramMap.put("orderId", orderId);
        paramMap.put("total", total);

        // get values from CartTM
        ObservableList<CartTM> tableRecords = tblOrder.getItems(); // bean collection

        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/CustomerInvoice.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, paramMap, new JRBeanCollectionDataSource(tableRecords));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }
        // Clear texts and table
        clearAllTexts();
    }

}
