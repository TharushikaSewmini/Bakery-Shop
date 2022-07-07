package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Product;
import util.Loader;
import view.tm.ProductTM;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public class DashBoardFormController1 implements Loader {
    public Label lblDate;
    public Label lblTime;
    public Label lblCustomerCount;
    public Label lblProductCount;
    public Label lblOrderCount;
    public Label lblDeliveryOrderCount;
    public AnchorPane dashBoardContext;
    public AnchorPane mainContext;
    public JFXButton btnLogout;
    public TableView<ProductTM> tblProduct;
    public TableColumn colProductCode;
    public TableColumn colProductDescription;
    public TableColumn colProductQtyOnHand;
    public TableColumn colProductUnitPrice;

    public void initialize() {
        loadDateAndTime();
        int customerIdCount=0;
        int productIdCount=0;
        int orderIdCount=0;
        int deliveryOrderIdCount=0;

        // Initializing table columns with ProductTM properties
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colProductUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));



        try {
            lblCustomerCount.setText(String.valueOf(new CustomerCrudController().getCustomerCount(customerIdCount)));
            lblProductCount.setText(String.valueOf(new ProductCrudController().getProductCount(productIdCount)));
            lblOrderCount.setText(String.valueOf(new OrderCrudController().getOrderCount(orderIdCount)));
            lblDeliveryOrderCount.setText(String.valueOf(new DeliveryOrderCrudController().getDeliveryOrderCount(deliveryOrderIdCount)));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadAllProducts();
    }

    private void loadDateAndTime() {
        //set Date
        lblDate.setText(new SimpleDateFormat("yyy-MMMM-dd").format(new Date()));

        //set Time
        Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e ->{
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" +
                    currentTime.getMinute()+ ":" +
                    currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) mainContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
        stage.centerOnScreen();
    }

    public void homeOnAction(ActionEvent actionEvent) throws IOException {
        mainContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/DashBoardForm1.fxml"));
        mainContext.getChildren().add(parent);
    }

    public void productOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("ProductForm");
    }

    public void customerOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("CustomerForm");
    }

    public void orderOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("OrderForm");
    }

    public void deliveryOrderOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("DeliveryOrderForm");
    }

    public void employeeOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("EmployeeForm");
    }

    public void vehicleOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("VehicleForm");
    }

    public void driverOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("DriverForm");
    }

    public void salaryOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("SalaryForm");
    }

    public void reportsOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("ReportsForm");
    }

    public void loadUi(String location) throws IOException {
        dashBoardContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        dashBoardContext.getChildren().add(parent);
    }


    ObservableList<ProductTM> tmList = FXCollections.observableArrayList();

    private void loadAllProducts() {
        try {
            ObservableList<Product> obList = FXCollections.observableArrayList(ProductCrudController.getProductDetails());

            for (Product product : obList
            ) {
                Button btn= new Button("Delete");

                ProductTM tm= new ProductTM(product.getCode(),product.getDescription(),product.getQtyOnHand(),product.getUnitPrice());
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

}
