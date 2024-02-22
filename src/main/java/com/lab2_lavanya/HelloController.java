package com.lab2_lavanya;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController  implements Initializable {
    private static final String url="jdbc:mysql://localhost:3306/studentdblab2";
    private static final String username="root";
    private static final String password="Oreo@2004";

    public TableView<Student> tableview;
    public TableColumn<Student,Integer> tablestudentId;
    public TableColumn<Student,String> tableName;
    public TableColumn<Student,String> tableCourse;
    public Label message;
    public TextField nameInput;
    public TextField courseInput;
    public TextField idInput;

    ObservableList<Student> list= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablestudentId.setCellValueFactory(new PropertyValueFactory<Student,Integer>("id"));
        tableName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        tableCourse.setCellValueFactory(new PropertyValueFactory<Student,String>("course"));

        tableview.setItems(list);
    }


    public void fetchAllData(){
        tableview.getItems().clear();

        try{
            Class.forName("com.cj.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            String query="SELECT * FROM student";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                String course=resultSet.getString("course");

                tableview.getItems().add(new Student(id,name,course));
                setEmpty();
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void viewAllRecords() {
        message.setText("");
       fetchAllData();
    }

    public void deleteData() {
        message.setText("");
        String id= idInput.getText();
        if(id==""){
            message.setText("ENTER VALID STUDENT ID");
            return;
        }

        try{
            Class.forName("com.cj.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            String query="DELETE FROM student WHERE id=?";

            PreparedStatement preparedStatement=connection.prepareStatement(query);
            int studentId= Integer.parseInt(id);

            preparedStatement.setInt(1,studentId);


            int  rowsAffected=preparedStatement.executeUpdate();
            if(rowsAffected>0){
                message.setText("RECORD DELETED SUCCESSFULLY !! ID: "+ id);
                fetchAllData();
                setEmpty();
            }
            else{
                message.setText("FAILED TO DELETE RECORD!!");
                return;
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateData() {
        message.setText("");
        tableview.getItems().clear();

        String id= idInput.getText();
        String name=nameInput.getText();
        String course=courseInput.getText();

        if(name.equals("")||course.equals("") || id==""){
            message.setText("ID,NAME AND COURSE FIELDS ARE REQUIRED !!");
            return;
        }


        try{
            Class.forName("com.cj.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{

//Inserting Record
            Connection connection=DriverManager.getConnection(url,username,password);
            String query="UPDATE student SET name=?, course=? WHERE id=?";
            int idInt=Integer.parseInt(id);
            PreparedStatement preparedStatement=connection.prepareStatement(query);

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,course);
            preparedStatement.setInt(3,idInt);

            int  rowsAffected=preparedStatement.executeUpdate();

            if(rowsAffected>0){
                message.setText("RECORD UPDATED SUCCESSFULLY !!");
            }
            else{
                message.setText("FAILED TO UPDATE RECORD!!");
                return;
            }
//Showing inserted record

            String query2="SELECT * FROM student WHERE id=?";
            PreparedStatement preparedStatement2=connection.prepareStatement(query2);
            preparedStatement2.setInt(1,idInt);

            ResultSet resultSet=preparedStatement2.executeQuery();
            if(resultSet.next()){
                int idResult=resultSet.getInt("id");
                String nameResult=resultSet.getString("name");
                String courseResult=resultSet.getString("course");
                tableview.getItems().add(new Student(idResult,nameResult,courseResult));
                setEmpty();
            }
            else{
                message.setText("No records found");
            }


        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void insertData() {
        message.setText("");
        tableview.getItems().clear();

        String name=nameInput.getText();
        String course=courseInput.getText();

        if(name.equals("")||course.equals("")){
            message.setText("NAME AND COURSE FIELDS ARE REQUIRED !!");
            return;
        }


        try{
            Class.forName("com.cj.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{

//Inserting Record
            Connection connection=DriverManager.getConnection(url,username,password);
            String query="INSERT INTO student (name,course) VALUES (?,?)";

            PreparedStatement preparedStatement=connection.prepareStatement(query);

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,course);

            int  rowsAffected=preparedStatement.executeUpdate();

            if(rowsAffected>0){
                message.setText("RECORD INSERTED SUCCESSFULLY !!");
            }
            else{
                message.setText("FAILED TO INSERT RECORD!!");
                return;
            }
//Showing inserted record

            String query2="SELECT * FROM student WHERE name=? AND course=?";
            PreparedStatement preparedStatement2=connection.prepareStatement(query2);
            preparedStatement2.setString(1,name);
            preparedStatement2.setString(2,course);

            ResultSet resultSet=preparedStatement2.executeQuery();
            if(resultSet.next()){
                int id=resultSet.getInt("id");
                String nameResult=resultSet.getString("name");
                String courseResult=resultSet.getString("course");
                tableview.getItems().add(new Student(id,nameResult,courseResult));
                setEmpty();
            }
            else{
                message.setText("No records found");
            }


        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public void setEmpty(){
      idInput.setText("");
      nameInput.setText("");
      courseInput.setText("");
    }
}