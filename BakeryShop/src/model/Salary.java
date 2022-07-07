package model;

public class Salary {
    private String id;
    private String employeeId;
    private String month;
    private int workingDays;
    private double salaryPerDay;
    private double bonus;

    public Salary() {
    }

    public Salary(String id, String employeeId, String month, int workingDays, double salaryPerDay, double bonus) {
        this.id = id;
        this.employeeId = employeeId;
        this.month = month;
        this.workingDays = workingDays;
        this.salaryPerDay = salaryPerDay;
        this.bonus = bonus;
    }

    public Salary(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public double getSalaryPerDay() {
        return salaryPerDay;
    }

    public void setSalaryPerDay(double salaryPerDay) {
        this.salaryPerDay = salaryPerDay;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", month='" + month + '\'' +
                ", workingDays=" + workingDays +
                ", salaryPerDay=" + salaryPerDay +
                ", bonus=" + bonus +
                '}';
    }
}
