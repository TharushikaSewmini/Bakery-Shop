package view.tm;

import javafx.scene.control.Button;

public class SalaryTM {
    private String id;
    private String employeeId;
    private String month;
    private int workingDays;
    private double salaryPerDay;
    private double bonus;
    private double total;
    private Button btn;

    public SalaryTM() {
    }

    public SalaryTM(String id, String employeeId, String month, int workingDays, double salaryPerDay, double bonus, double total, Button btn) {
        this.id = id;
        this.employeeId = employeeId;
        this.month = month;
        this.workingDays = workingDays;
        this.salaryPerDay = salaryPerDay;
        this.bonus = bonus;
        this.total = total;
        this.btn = btn;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "SalaryTM{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", month='" + month + '\'' +
                ", workingDays=" + workingDays +
                ", salaryPerDay=" + salaryPerDay +
                ", bonus=" + bonus +
                ", total=" + total +
                ", btn=" + btn +
                '}';
    }
}
