package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeCrudController {
    // Save new Employee
    public boolean saveEmployee(Employee e) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Employee VALUES (?,?,?,?)",e.getId(),e.getName(),e.getAddress(),e.getContact());
    }

    // Delete Employee
    public boolean deleteEmployee(Employee e1) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE id=?",e1.getId());
    }

    // Update Employee
    public boolean updateEmployee(Employee e2) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Employee SET name=? , address=? , contact=? , WHERE id=?",e2.getName(),e2.getAddress(),e2.getContact(),e2.getId());
    }

    // Get Employee Id
    public static ArrayList<String> getEmployeeIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT id FROM Employee");
        ArrayList<String> ids= new ArrayList<>();
        while(result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    // Get Employee details using id
    public static Employee getEmployee(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Employee WHERE id=?", id);
        if (result.next()) {
            return new Employee(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4)
            );
        }
        return null;
    }

    // Load all Employee
    public static ObservableList<Employee> getEmployeeDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Employee");
        ObservableList<Employee> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
            new Employee(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4)
            )
            );
        }
        return obList;
    }


    // Get the last saved employee id
    public static String getEmployeeId(int column) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Employee ORDER BY id DESC LIMIT 1");
        if(set.next()){
            // Convert last employeeId to next employeeId (E001-> E002)
            String id = set.getString(column);
            String[] splitted = id.split("(E)");
            return String.format("E%03d", (Integer.parseInt(splitted[1]) + 1));
        }else{
            return "E001";
        }
    }

}
