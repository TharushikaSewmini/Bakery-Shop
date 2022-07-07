package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Driver;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DriverCrudController {

    // Save new Driver
    public boolean saveDriver(Driver d) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Driver VALUES (?,?,?,?,?)",d.getId(),d.getName(),d.getAddress(),d.getContact(),d.getSalary());
    }

    // Delete Driver
    public boolean deleteDriver(Driver d1) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Driver WHERE id=?",d1.getId());
    }

    // Update Driver
    public boolean updateDriver(Driver d2) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Driver SET name=? , address=? , contact=? WHERE id=?",d2.getName(),d2.getAddress(),d2.getContact(),d2.getId());
    }

    // Get driverIds
    public static ArrayList<String> getDriverIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM Driver");
        ArrayList<String> ids= new ArrayList<>();
        while(result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    // Get driver details using driverId
    public static Driver getDriver(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver WHERE id=?", id);
        if (result.next()) {
            return new Driver(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getDouble(5)
            );
        }
        return null;
    }

    // Load all Driver
    public static ObservableList<Driver> getDriverDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver");
        ObservableList<Driver> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Driver(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getInt(4),
                            result.getDouble(5)
                    )
            );
        }
        return obList;
    }

    // Get the last saved driver id
    public static String getDriverId(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Driver ORDER BY id DESC LIMIT 1");
        if(set.next()){
            // Convert last driverId to next driverId (D001-> D002)
            String id = set.getString(column);
            String[] splitted = id.split("(D)");
            return String.format("D%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "D001";
        }
    }

}
