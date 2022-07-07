package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerCrudController {

    // Save new Customer
    public boolean saveCustomer(Customer c) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)",c.getId(),c.getName(),c.getAddress(),c.getContact());
    }

    // Delete Customer
    public boolean deleteCustomer(Customer c1) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Customer WHERE id=?",c1.getId());
    }

    // Update Customer
    public boolean updateCustomer(Customer c2) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Customer SET name=? , address=? , contact=? WHERE id=?",c2.getName(),c2.getAddress(),c2.getContact(),c2.getId());
    }


    // Get Customer Id
    public static ArrayList<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM Customer");
        ArrayList<String> ids= new ArrayList<>();
        while(result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    // Search Customer , Get Customer details using id
    public static Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE id=?", id);
        if (result.next()) {
            return new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4)
            );
        }
        return null;
    }

    // Load all Customers
    public static ObservableList<Customer> getCustomerDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
        ObservableList<Customer> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Customer(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getInt(4)
                    )
            );
        }
        return obList;
    }

    // Get customers total
    public static int getCustomerCount(int ids) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(id) FROM Customer");
        while(result.next()){
            ids=(result.getInt(1));
        }
        return ids;
    }


    // Get the last saved customer id
    public static String getCustomerId(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        if(set.next()){
            // Convert last customerId to next customerId (C001-> C002)
            String id = set.getString(column);
            String[] splitted = id.split("(C)");
            return String.format("C%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "C001";
        }
    }

}
