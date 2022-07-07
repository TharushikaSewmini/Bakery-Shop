package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DeliveryOrder;
import model.Order;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryOrderCrudController {
    // Insert Values to DeliveryOrder table
    public boolean saveDeliveryOrder(DeliveryOrder deliveryOrder) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO DeliveryOrders VALUES(?,?,?,?,?)",
                deliveryOrder.getId(), deliveryOrder.getDate(), deliveryOrder.getCustomerId(),deliveryOrder.getDriverId(),deliveryOrder.getVehicleId());
    }

    // Get deliveryOrders total
    public static int getDeliveryOrderCount(int ids) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(id) FROM DeliveryOrders");
        while(result.next()){
            ids=(result.getInt(1));
        }
        return ids;
    }


    // Load all DeliveryOrderIds
    public static ObservableList<DeliveryOrder> getAllDeliveryOrderIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM DeliveryOrders");
        ObservableList<DeliveryOrder> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new DeliveryOrder(
                            result.getString(1)
                    )
            );
        }
        return obList;
    }




    // Get the last saved deliveryOrder id
    public static String getDeliveryOrderId(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM DeliveryOrders ORDER BY id DESC LIMIT 1");
        if(set.next()){
            // Convert last deliveryOrderId to next deliveryOrderId (DO001-> DO002)
            String id = set.getString(column);
            String[] splitted = id.split("(DO)");
            return String.format("DO%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "DO001";
        }
    }

}
