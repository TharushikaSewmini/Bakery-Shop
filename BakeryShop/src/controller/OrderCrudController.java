package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Order;
import model.OrderDetails;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderCrudController {
    // Insert Values to `Order` table
    public boolean saveOrder(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO `Order` VALUES(?,?,?)",
                order.getId(), order.getDate(), order.getCustomerId());
    }

    // Insert Order details to OrderDetail Table
    public boolean saveOrderDetails(ArrayList<OrderDetails> details) throws SQLException, ClassNotFoundException {
        for (OrderDetails det:details
        ) {
            boolean isDetailsSaved=CrudUtil.execute("INSERT INTO OrderDetail VALUES(?,?,?,?,?)",
                    det.getOrderId(),det.getProductCode(),det.getQty(),det.getUnitPrice(),det.getDiscount());
            if (isDetailsSaved){
                if (!updateQty(det.getProductCode(), det.getQty())){
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }


    // Update qty from OrderDetail table
    private boolean updateQty(String productCode, int qtyOnHand) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Product SET qtyOnHand=qtyOnHand-? WHERE code=?", qtyOnHand,productCode);
    }


    // Get the last saved Order id
    public static String getOrderId(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM `Order` ORDER BY id DESC LIMIT 1");
        if(set.next()){
            // Convert last OrderId to next OrderId (O001-> O002)
            String id = set.getString(column);
            String[] splitted = id.split("(O)");
            return String.format("O%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "O001";
        }
    }


    // Get Order Ids
    public static ArrayList<String> getOrderIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM `Order` ORDER BY id ASC");
        ArrayList<String> ids= new ArrayList<>();
        while(result.next()){ // until the next()==null
            ids.add(result.getString(1));
        }
        return ids;
    }


    // Search Order , Get Order details using id
    public static Order getOrder(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM `Order` WHERE id=?", id);
        if (result.next()) {
            return new Order(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3)
            );
        }
        return null;
    }


    // Load all OrderIds
    public static ObservableList<Order> getAllOrderIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM `Order`");
        ObservableList<Order> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Order(
                            result.getString(1)
                    )
            );
        }
        return obList;
    }



    // Get the total of placed Orders
    public static int getOrderCount(int ids) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(id) FROM `Order`");
        while(result.next()){
            ids=(result.getInt(1));
        }
        return ids;
    }
}
