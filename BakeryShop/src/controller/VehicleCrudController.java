package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Vehicle;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleCrudController {

    // Add Vehicle
    public boolean saveVehicle(Vehicle v) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Vehicle VALUES (?,?)",v.getNumber(),v.getType());
    }

    // Delete Vehicle
    public boolean deleteVehicle(Vehicle v1) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Vehicle WHERE number=?",v1.getNumber());
    }

    // Update Vehicle
    public boolean updateVehicle(Vehicle v2) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Vehicle SET type=? WHERE number=?",v2.getType(),v2.getNumber());
    }

    // Get vehicle numbers
    public static ArrayList<String> getVehicleNumbers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT number FROM Vehicle");
        ArrayList<String> numbers= new ArrayList<>();
        while(result.next()){
            numbers.add(result.getString(1));
        }
        return numbers;
    }

    // Get vehicle details using vehicle number
    public static Vehicle getVehicle(String number) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle WHERE number=?", number);
        if (result.next()) {
            return new Vehicle(
                    result.getString(1),
                    result.getString(2)
            );
        }
        return null;
    }

    // Load all Vehicle
    public static ObservableList<Vehicle> getVehicleDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle");
        ObservableList<Vehicle> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Vehicle(
                            result.getString(1),
                            result.getString(2)
                    )
            );
        }
        return obList;
    }
}
