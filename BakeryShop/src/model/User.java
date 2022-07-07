package model;

public class User {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private String employeeId;

    public User() {
    }

    public User(String userName, String email, String password, String employeeId) {
        //this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.employeeId = employeeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "User{" +
                //"userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
