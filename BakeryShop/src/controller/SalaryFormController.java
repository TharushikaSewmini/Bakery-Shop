package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.DeliveryOrder;
import model.Employee;
import model.Salary;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ValidationUtil;
import view.tm.SalaryTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class SalaryFormController {
    public JFXTextField txtSalaryId;
    public JFXComboBox<String> cmbSalaryMonth;
    public JFXComboBox<String> cmbEmployeeId;
    public JFXTextField txtWorkingDays;
    public JFXTextField txtSalaryPerDay;
    public JFXTextField txtEmployeeName;
    public JFXTextField txtEmployeeAddress;
    public JFXTextField txtEmployeeContact;
    public Label lblTotalCost;
    public JFXTextField txtBonus;
    public TableView<SalaryTM> tblSalary;
    public TableColumn colSalaryId;
    public TableColumn colEmployeeId;
    public TableColumn colMonth;
    public TableColumn colWorkingDays;
    public TableColumn colSalaryPerDay;
    public TableColumn colBonus;
    public TableColumn colTotalSalary;
    public TableColumn colOption;
    public JFXButton btnAdd;
    public TextField txtSearchSalaryId;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern idPattern = Pattern.compile("^(S)[0-9]{3,5}$");
        Pattern workingDaysPattern = Pattern.compile("^[0-9]{1,3}$");
        Pattern salaryPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        Pattern bonusPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtSalaryId, idPattern);
        map.put(txtWorkingDays, workingDaysPattern);
        map.put(txtSalaryPerDay, salaryPattern);
        map.put(txtBonus, bonusPattern);

        // Initializing table columns with SalaryTM properties
        colSalaryId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colWorkingDays.setCellValueFactory(new PropertyValueFactory<>("workingDays"));
        colSalaryPerDay.setCellValueFactory(new PropertyValueFactory<>("salaryPerDay"));
        colBonus.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        colTotalSalary.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setSalaryId(); // get next salaryId
        setSalaryMonth();
        setEmployeeIds();

        //--------------------------------------------
        cmbEmployeeId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setEmployee(newValue);
        });

    }


    public void textFieldsKeyReleased(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnAdd);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAdd);
            //if the response is a text field
            //that means there is a error
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();// if there is a error just focus it
            }
        }
    }


    //reset border colors to default color
    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color:white");
            textField.setStyle("-fx-prompt-text-fill: #b2b2b2");
        }
    }


    // Set Next SalaryId
    public void setSalaryId(){
        try {
            String sId = SalaryCrudController.getSalaryId(1);
            txtSalaryId.setText(sId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void setSalaryMonth() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        obList.addAll("January","February","March","April","May","June","July","August","September","October","November","December");
        cmbSalaryMonth.setItems(obList);
    }



    ObservableList<SalaryTM> tmList = FXCollections.observableArrayList();

    // Load EmployeeIds to ComboBox
    private void setEmployeeIds() {
        try {
            cmbEmployeeId.setItems(FXCollections.observableList(EmployeeCrudController.getEmployeeIds()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load Employee Name,Address,Contact to the TextFields
    private void setEmployee(String selectedEmployeeId) {
        try {
            Employee i=EmployeeCrudController.getEmployee(selectedEmployeeId);
            if(i!=null){
                txtEmployeeName.setText(i.getName());
                txtEmployeeAddress.setText(i.getAddress());
                txtEmployeeContact.setText(String.valueOf(i.getContact()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void addOnAction(ActionEvent actionEvent) {
        double salaryPerDay = Double.parseDouble(txtSalaryPerDay.getText());
        int workingDays = Integer.parseInt(txtWorkingDays.getText());
        double bonus = Double.parseDouble(txtBonus.getText());
        double totalSalary = (salaryPerDay * workingDays)+bonus;

        Salary isSalaryIdExists = isSalaryIdExists(txtSalaryId.getText());
        if (isSalaryIdExists!=null) { // if salaryId is already exists
            new Alert(Alert.AlertType.ERROR, "Salary Id is Exists!").show();
        }else {
            lblTotalCost.setText(String.valueOf(totalSalary));

            // Add new values
            Button btn = new Button("Delete");

            SalaryTM tm = new SalaryTM(
                    txtSalaryId.getText(),
                    cmbEmployeeId.getValue(),
                    cmbSalaryMonth.getValue(),
                    workingDays,
                    salaryPerDay,
                    bonus,
                    totalSalary,
                    btn
            );

            btn.setOnAction(e -> {
                tmList.remove(tm);
            });

            tmList.add(tm);
            tblSalary.setItems(tmList);
            tblSalary.refresh();
        }
    }

    
    
    public void saveOnAction(ActionEvent actionEvent) {
        Salary salary = new Salary(
                    txtSalaryId.getText(),
                    cmbEmployeeId.getValue(),
                    cmbSalaryMonth.getValue(),
                    Integer.parseInt(txtWorkingDays.getText()),
                    Double.parseDouble(txtSalaryPerDay.getText()),
                    Double.parseDouble(txtBonus.getText())
        );

        // Add salary to Salary table
        try {
            boolean isSalaryOrderSaved = new SalaryCrudController().saveSalary(salary);
            if (isSalaryOrderSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Error!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        setBorders(txtSalaryId,txtWorkingDays,txtSalaryPerDay,txtBonus);
    }


    // Check the salaryId is exists
    private Salary isSalaryIdExists(String SalaryId) {

        try {
            ObservableList<Salary> obList = FXCollections.observableArrayList(SalaryCrudController.getAllSalaryIds());
            for (Salary id : obList
            ) {
                if (id.getId().equals(SalaryId)) {
                    return id;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    // Print Payslip
    public void printPaySlipOnAction(ActionEvent actionEvent) {
        String month = cmbSalaryMonth.getValue();
        String employeeId = cmbEmployeeId.getValue();
        String employeeName = txtEmployeeName.getText();
        int workingDays = Integer.parseInt(txtWorkingDays.getText());
        double salaryPerDay = Double.parseDouble(txtSalaryPerDay.getText());
        double bonus = Double.parseDouble(txtBonus.getText());
        double total = Double.parseDouble(lblTotalCost.getText());

        HashMap paramMap = new HashMap();
        paramMap.put("month", month);// month = report param name // month = UI typed value
        paramMap.put("employeeId", employeeId);
        paramMap.put("employeeName", employeeName);
        paramMap.put("workingDays", workingDays);
        paramMap.put("salaryPerDay", salaryPerDay);
        paramMap.put("bonus", bonus);
        paramMap.put("total", total);

        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/EmployeePaySlip.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }
        clearAllText(); // clear data
    }


    public void clearAllText() {
        txtSalaryId.clear();
        cmbSalaryMonth.setValue(null);
        cmbEmployeeId.setValue(null);
        txtEmployeeName.clear();
        txtEmployeeAddress.clear();
        txtEmployeeContact.clear();
        txtWorkingDays.clear();
        txtSalaryPerDay.clear();
        txtBonus.clear();
        lblTotalCost.setText(null);
        tblSalary.setItems(null);
        setSalaryId();
        cmbSalaryMonth.requestFocus();
    }


    public void txtSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ObservableList<Salary> obList = FXCollections.observableArrayList(SalaryCrudController.getEmployeeSalaryDetails(txtSearchSalaryId.getText()));
        // Search Employee from Employee table
            for (Salary em : obList
            ) {

                // Add new values
                Button btn = new Button("Delete");

                double salaryPerDay = em.getSalaryPerDay();
                int workingDays = em.getWorkingDays();
                double bonus = em.getBonus();
                double totalSalary = (salaryPerDay * workingDays) + bonus;

                SalaryTM tm = new SalaryTM(em.getId(), em.getEmployeeId(), em.getMonth(), em.getWorkingDays(), em.getSalaryPerDay(), em.getBonus(), totalSalary, btn);
                tmList.add(tm);

                btn.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are You Sure?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {

                        // Remove Employee From tmList
                        tmList.remove(tm);
                    }
                });
            }
            tblSalary.setItems(tmList);
    }
}
