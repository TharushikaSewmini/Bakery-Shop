package util;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql, Object...params) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql); // Create Prepared Statement, get DBConnection and pass sql
        for(int i = 0; i < params.length; i++) {
            statement.setObject((i + 1), params[i]); // Set values
        }
        if(sql.startsWith("SELECT")){
            return(T) statement.executeQuery(); // Cast to T
        }else{
            return ((T)(Boolean)(statement.executeUpdate()>0)); // Cast Boolean and T
        }
    }
}
