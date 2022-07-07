package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductCrudController {

    // Save new Product
    public boolean saveProduct(Product p) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Product VALUES (?,?,?,?)",p.getCode(),p.getDescription(),p.getQtyOnHand(),p.getUnitPrice());
    }

    // Delete Product
    public boolean deleteProduct(Product p1) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Product WHERE code=?",p1.getCode());
    }

    // Update Product
    public boolean updateProduct(Product p2) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Product SET description=? , qtyOnHand=? , unitPrice=? WHERE code=?",p2.getDescription(),p2.getQtyOnHand(),p2.getUnitPrice(),p2.getCode());
    }

    // Get ProductId
    public static ArrayList<String> getProductCodes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT code FROM Product");
        ArrayList<String> codeList= new ArrayList<>();
        while(result.next()){
            codeList.add(result.getString(1));
        }
        return codeList;
    }

    // Get Product details using code
    public static Product getProduct(String code) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Product WHERE code=?", code);
        if(result.next()) {
            return new Product(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getDouble(4)
            );
        }
        return null;
    }

    // Load all Product
    public static ObservableList<Product> getProductDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Product");
        ObservableList<Product> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Product(
                            result.getString(1),
                            result.getString(2),
                            result.getInt(3),
                            result.getDouble(4)
                    )
            );
        }
        return obList;
    }

    // Get total of products
    public static int getProductCount(int ids) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(code) FROM Product");
        while(result.next()){
            ids=(result.getInt(1));
        }
        return ids;
    }


    // Load all Product
    public static ObservableList<Product> getProductSalesDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Product ORDER BY code;");
        ObservableList<Product> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Product(
                            result.getString(1),
                            result.getString(2),
                            result.getInt(3),
                            result.getDouble(4)
                    )
            );
        }
        return obList;
    }




    // Get the last saved product code
    public static String getProductCode(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT code FROM Product ORDER BY code DESC LIMIT 1");
        if(set.next()){
            // Convert last productCode to next productCode (P001-> P002)
            String code = set.getString(column);
            String[] splitted = code.split("(P)");
            return String.format("P%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "P001";
        }
    }

}
