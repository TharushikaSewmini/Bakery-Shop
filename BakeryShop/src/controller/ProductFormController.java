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
import model.Product;
import util.ValidationUtil;
import view.tm.ProductTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class ProductFormController {
    public JFXTextField txtCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public TableView<ProductTM> tblProduct;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colUnitPrice;
    public TableColumn colOption;
    public TextField txtSearchCode;
    public JFXButton btnSaveProduct;
    public JFXButton btnUpdateProduct;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern codePattern = Pattern.compile("^(P)[0-9]{3,5}$");
        Pattern descriptionPattern = Pattern.compile("^[A-Z][A-z0-9 ]{3,20}$");
        Pattern qtyPattern = Pattern.compile("^[0-9]{1,}$");
        Pattern unitPricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtCode, codePattern);
        map.put(txtDescription, descriptionPattern);
        map.put(txtQtyOnHand, qtyPattern);
        map.put(txtUnitPrice, unitPricePattern);

        // Initializing table columns with ProductTM properties
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setProductCode(); // get next productCode
        loadAllProducts(); // Load all Product to the TableView

    }


    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnSaveProduct);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSaveProduct);
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
    public void setProductCode(){
        try {
            String code = ProductCrudController.getProductCode(1);
            txtCode.setText(code);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    ObservableList<ProductTM> tmList = FXCollections.observableArrayList();

    public void saveProductOnAction(ActionEvent actionEvent) {

        Product p = new Product(
                txtCode.getText(), txtDescription.getText(), Integer.parseInt(txtQtyOnHand.getText()), Double.parseDouble(txtUnitPrice.getText())
        );

        // Add new Product to Product table
        try {
            boolean isProductSaved = new ProductCrudController().saveProduct(p);
            if (isProductSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        // Clear the table data
        tmList.clear();
        loadAllProducts(); // Load all Product to the TableView

        setBorders(txtCode,txtDescription,txtQtyOnHand,txtUnitPrice);
    }


    private void loadAllProducts() {
        try {
            ObservableList<Product> obList = FXCollections.observableArrayList(ProductCrudController.getProductDetails());

            for (Product product : obList
            ) {
                Button btn= new Button("Delete");

                ProductTM tm= new ProductTM(product.getCode(),product.getDescription(),product.getQtyOnHand(),product.getUnitPrice(), btn);
                tmList.add(tm);

                btn.setOnAction(e->{
                    Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                            "Are You Sure?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get().equals(ButtonType.YES)){

                        // Delete Product from Product table
                        try{
                            boolean isProductDeleted = new ProductCrudController().deleteProduct(product);
                            if (isProductDeleted) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                            }else{
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        }catch (SQLException | ClassNotFoundException ec){
                            ec.printStackTrace();
                        }

                        // Remove Product From tmList
                        tmList.remove(tm);
                    }
                });
                tblProduct.setItems(tmList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void addProductOnAction(ActionEvent actionEvent) {
        txtCode.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtSearchCode.clear();
        setProductCode();
        txtDescription.requestFocus();
        setBorders(txtCode,txtDescription,txtQtyOnHand,txtUnitPrice);
    }

    public void updateProductOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ObservableList<Product> obList = FXCollections.observableArrayList(ProductCrudController.getProductDetails());

        for (Product tm :obList
        ) {
            if(tm.getCode().equals(txtCode.getText())){
                tm.setDescription(txtDescription.getText());
                tm.setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                tm.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));

                // Update Product From Product table
                try{
                    boolean isProductUpdated = new ProductCrudController().updateProduct(tm);
                    if (isProductUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                    }else{
                        new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                    }


                }catch (SQLException | ClassNotFoundException e){
                    e.printStackTrace();
                }

                // Clear the table data
                tmList.clear();
                loadAllProducts();

                return;
            }
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        // Assign search TextField code
        String selectedProductCode=txtSearchCode.getText();

        // Search Product from Product table
        try {
            Product p= ProductCrudController.getProduct(selectedProductCode);

            if(p!=null){
                txtCode.setText(p.getCode());
                txtDescription.setText(p.getDescription());
                txtQtyOnHand.setText(String.valueOf(p.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(p.getUnitPrice()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
