package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DeliveryOrder;
import model.Salary;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalaryCrudController {
    // Save new Salary
    public boolean saveSalary(Salary s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Salary VALUES (?,?,?,?,?,?)",s.getId(),s.getEmployeeId(),s.getMonth(),s.getWorkingDays(),s.getSalaryPerDay(),s.getBonus());
    }


    // Load all Employee
    public static ObservableList<Salary> getEmployeeSalaryDetails(String employeeId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Salary WHERE employeeId=?", employeeId);
        ObservableList<Salary> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Salary(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getInt(4),
                            result.getDouble(5),
                            result.getDouble(6)
                    )
            );
        }
        return obList;
    }


    // Load all salaryIds
    public static ObservableList<Salary> getAllSalaryIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM Salary");
        ObservableList<Salary> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Salary(
                            result.getString(1)
                    )
            );
        }
        return obList;
    }





    // Get the last saved salary id
    public static String getSalaryId(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Salary ORDER BY id DESC LIMIT 1");
        if(set.next()){
            // Convert last salaryId to next salaryId (S001-> S002)
            String id = set.getString(column);
            String[] splitted = id.split("(S)");
            return String.format("S%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "S001";
        }
    }

}
