package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class ReportsFormController {
    public JFXDatePicker dateFromDate;
    public JFXDatePicker dateToDate;
    public JFXComboBox<String> cmbMonth;

    public void initialize(){
        setMonth();
    }

    private void setMonth() {
        ObservableList<String> monthList = FXCollections.observableArrayList();
        monthList.addAll("January","February","March","April","May","June","July","August","September","October","November","December");
        cmbMonth.setItems(monthList);
    }


    public void ProductSalesChartOnAction(ActionEvent actionEvent) {
        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/ProductSalesChart.jasper"));
            Connection connection = DBConnection.getInstance().getConnection();



            String fromDate = String.valueOf(dateFromDate.getValue());
            String toDate = String.valueOf(dateToDate.getValue());

            HashMap map = new HashMap();
            map.put("fromDate", fromDate);
            map.put("toDate", toDate);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, connection);
            JasperViewer.viewReport(jasperPrint, false);


        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void EmployeeSalaryChartOnAction(ActionEvent actionEvent) {
        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/EmployeeSalaryChart.jasper"));
            Connection connection = DBConnection.getInstance().getConnection();

            String month = cmbMonth.getValue();

            HashMap map = new HashMap();
            map.put("month", month);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, connection);
            JasperViewer.viewReport(jasperPrint, false);


        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
