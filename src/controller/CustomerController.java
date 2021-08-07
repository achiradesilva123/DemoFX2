/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.CustomerTM;

/**
 * FXML Controller class
 *
 * @author Achira De Silva
 */
public class CustomerController implements Initializable {

    @FXML
    private JFXTextField txtID;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtSalary;
    @FXML
    private JFXButton btnSave;
    @FXML
    private TableView<CustomerTM> tblCustomer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCustomer.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("salary"));

        load();
    }

    @FXML
    private void SaveAction(ActionEvent event) {
        try {
            String id= txtID.getText();
            String name= txtName.getText();
            String address= txtAddress.getText();
            double salary= Double.parseDouble(txtSalary.getText());

            String sql= "insert into customer values(?,?,?,?)";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbdemofx2", "root", "ijse");
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setObject(1, id);
            stm.setObject(2, name);
            stm.setObject(3, address);
            stm.setObject(4, salary);
            int i = stm.executeUpdate();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tblClicked(MouseEvent event) {
        String id = tblCustomer.getSelectionModel().selectedItemProperty().get().getId();
        String name = tblCustomer.getSelectionModel().selectedItemProperty().get().getName();
        String address = tblCustomer.getSelectionModel().selectedItemProperty().get().getAddress();
        double salary = tblCustomer.getSelectionModel().selectedItemProperty().get().getSalary();

        txtID.setText(id);
        txtName.setText(name);
        txtAddress.setText(address);
        txtSalary.setText(Double.toString(salary));
    }

    void load(){
        try {
            String sql= "select * from customer";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbdemofx2", "root", "ijse");
            Statement stm = con.createStatement();
            ResultSet rst = stm.executeQuery(sql);

            ArrayList<CustomerTM> arrayList= new ArrayList<>();
            while(rst.next()){
                CustomerTM customerTM= new CustomerTM(
                        rst.getString("id"),
                        rst.getString("name"),
                        rst.getString("address"),
                        rst.getDouble("salary")
                );
                arrayList.add(customerTM);
            }
            tblCustomer.setItems(FXCollections.observableArrayList(arrayList));

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
