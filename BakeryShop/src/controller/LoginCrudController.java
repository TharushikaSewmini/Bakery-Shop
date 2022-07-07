package controller;

import model.User;
import util.CrudUtil;

import java.sql.SQLException;

public class LoginCrudController {
    // Save new User
    public boolean saveUser(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO User VALUES (?,?,?,?,?)",user.getUserId(),user.getUserName(),user.getEmail(),user.getPassword(),user.getEmployeeId());
    }
}
