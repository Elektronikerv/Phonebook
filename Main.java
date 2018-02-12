import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;

public class Main extends Application  {

	private TableView table = new TableView();
	private static DatabaseConnector connector = new DatabaseConnector();
	private static  ObservableList<Person> list;
	
	public static void main(String[] args) throws SQLException  {
		try {
			list = FXCollections.observableArrayList(connector.getAll()); 
			launch(args);

			connector.closeConnection();
		}
		catch(SQLException e) {
			e.printStackTrace();
		} 	
	}
 
 @Override
    public void start(Stage stage) throws SQLException {
       
        stage.setTitle("Phone Book");

        final Label label = new Label("Phone Book");
        label.setFont(new Font("Times New Roman", 18));
 
  		TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(
        	new PropertyValueFactory<Person, String>("firstName"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(
        	new PropertyValueFactory<Person, String>("lastName"));
        TableColumn numberCol = new TableColumn("Number");
        numberCol.setCellValueFactory(
        	new PropertyValueFactory<Person, String>("number"));
        table.setItems(list);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        table.getColumns().addAll(firstNameCol, lastNameCol, numberCol);
 
 		Button btnAdd = new Button("Add");
 		Button btnDel = new Button("Delete");
 		btnAdd.setOnAction(e -> {
 			showAddWindow();
 		});
 		btnDel.setOnAction(e -> {
 			showDelWindow();
 		});
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 0, 10));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(btnAdd, btnDel);
        vbox.getChildren().addAll(label, table, hbox);
 		
        Scene scene = new Scene(vbox, 400, 500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
	
    public void showDelWindow() {
    	Stage stage = new Stage();
    	stage.setTitle("Delete contact");
    	VBox vbox = new VBox();
    	vbox.setAlignment(Pos.CENTER);
    	vbox.setPadding(new Insets(10));
    	vbox.setSpacing(10);
        Button btnDel = new Button("Delete");
       	TextField textNumber = new TextField();
       	Label lnum = new Label("Enter number:");
        btnDel.setOnAction(e -> {
        	try{
        		if(!containsNumber(textNumber.getText())) {
        			Alert noNumber = new Alert(Alert.AlertType.ERROR);
        			noNumber.setContentText("No such number in database!");
        			noNumber.showAndWait();
        			stage.close();
        		}
        		else {
        			connector.delete(textNumber.getText());
    				list.setAll(connector.getAll()); //update table
        			}
        		}
        	catch(SQLException ex) {
        		ex.printStackTrace();
        	}
        	finally{
        		stage.close();
        	}
        });
        vbox.getChildren().addAll(lnum, textNumber, btnDel);
    	Scene scene = new Scene(vbox, 250, 150);
    	stage.setScene(scene);
    	stage.show();
    }

    public static boolean containsNumber(String number) {
    	for(Person p : list) {
    		if (number.equals(p.getNumber())) 
    			return true;
    	}
    	return false;
    }

    public void showAddWindow() {
        Stage stage = new Stage();
        stage.setTitle("Add new contact");
        VBox box = new VBox();
        box.setPadding(new Insets(8));

        box.setAlignment(Pos.CENTER);
    	box.setSpacing(10);
        TextField textFirstName = new TextField();
        textFirstName.setPromptText("enter first name");
        TextField textLastName = new TextField();
        textLastName.setPromptText("enter last name");
        TextField textNumber = new TextField();
        textNumber.setPromptText("enter number");
        Button btnAdd = new Button();
        btnAdd.setText("add");

        btnAdd.setOnAction( e -> {
      		try {
                	String firstName = textFirstName.getText();
                	String lastName = textLastName.getText();
                	String number = textNumber.getText(); 
                	if(!firstName.trim().isEmpty() && !number.trim().isEmpty()) {        	
            			connector.add(firstName, lastName, number);
            			list.add(new Person(firstName, lastName, number));
            		}
                	else {
                	    Alert emptyFieldsError = new Alert(Alert.AlertType.ERROR);
                	    emptyFieldsError.setContentText("First name and number fields must be filled!");
                	    emptyFieldsError.showAndWait();
                	}
          	}
        	catch (SQLException ex) {
        		ex.printStackTrace();
        	}
        	finally {
            stage.close();
            }
        });

        box.getChildren().addAll(textFirstName,
         textLastName, textNumber, btnAdd);
        Scene scene = new Scene(box, 250, 150);
        stage.setScene(scene);
        stage.show();
    }
}
