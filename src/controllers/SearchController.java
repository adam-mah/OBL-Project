package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import entities.*;
import client.ClientConnection;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SearchController implements Initializable {

	@FXML
	private ImageView imgBack;

	@FXML
	private TextField txtSearch;

	@FXML
	private Button btnSearch;

	@FXML
	private ComboBox<String> cmbSearchBy;

	@FXML
	private TableView<Book> tableView;

	@FXML
	private TableColumn<Book, String> bookNameCol;

	@FXML
	private TableColumn<Book, String> editionCol;

	@FXML
	private TableColumn<Book, String> printYearCol;

	@FXML
	private TableColumn<Book, String> bookAuthorCol;

	@FXML
	private TableColumn<Book, String> subjectCol;

	@FXML
	private TableColumn<Book, String> availableCopiesCol;

	@FXML
	private TableColumn<Book, String> descriptionCol;

	@FXML
	private TableColumn<Book, String> shelfCol;

	@FXML
	private Button btnViewInfo;

	@FXML
	private Button btnClear;

	@FXML
	private Button btnOrderBook;

	ObservableList<String> list;
	ObservableList<Book> bookList;
	

	/**
	 * clear the table contents
	 * @param event
	 */
	@FXML
	void btnClearPressed(ActionEvent event) {
		tableView.getItems().clear();
		tableView.refresh();
		txtSearch.clear();
	}
	
	/**
	 * make an order for book if there is no available copies
	 * @param event
	 */
	@SuppressWarnings("static-access")
	@FXML
	void btnOrderBookPressed(ActionEvent event) {
		try {
			OrderController orderController = new OrderController();
			//passing the selected book data to order controller
			Book selectedBook = tableView.getSelectionModel().getSelectedItem();
			orderController.start(selectedBook);
			
		}catch(NullPointerException e) {
			showAlert("error","");
		}
	}
	
	/**
	 * search for specific book by its (name,author,subject,description)
	 * @param event
	 */
	@FXML
	void btnSearchPressed(ActionEvent event) {
		/*
		Book newBook = new Book(1, "java", "kasem","First Edition" , 2019, "java programing", "learning java", 20, "thread,javafx,gui", "A12", 10, "Regular", 10);
		Book newBook2 = new Book(1, "java", "saleh","First Edition" , 2019, "java programing", "learning java", 20, "thread,javafx,gui", "A12", 10, "Regular", 10);
		Book newBook3 = new Book(1, "java3", "kasem","First Edition" , 2019, "java programing", "learning java", 20, "thread,javafx,gui", "A12", 10, "Regular", 10);
		DatabaseController.addBook(newBook);
		DatabaseController.addBook(newBook2);
		DatabaseController.addBook(newBook3);
		*/
		String searchBy = cmbSearchBy.getValue();
		
		//make a default search by name if the user didn't make a selection from Combo Box  
		if(searchBy==null) {
			searchBy = "name";
			cmbSearchBy.setValue("Name");
		}
		ArrayList<Book> arr = DatabaseController.bookSearch(txtSearch.getText(), searchBy);
		if(!arr.isEmpty()) {
		bookList = FXCollections.observableArrayList(arr);
		tableView.setItems(bookList);
		}else {
			showAlert("No Match Result For "+txtSearch.getText(),"Please Enter New " + searchBy);
			tableView.getItems().clear();
			tableView.refresh();
		}

	}

	@FXML
	void btnViewInfoPressed(ActionEvent event) {

	}


	/**
	 * return to the previous window
	 * @param event
	 */
	@FXML
	void imgBackClicked(MouseEvent event) {
		Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
    	// get the previous scene
		Scene scene = SceneController.pop();
		stage.setScene(scene);
		stage.setTitle("User Main");
	}

	/**
	 * initialize the search form, prepare the search By combo box and the table
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		bookNameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
		editionCol.setCellValueFactory(new PropertyValueFactory<Book, String>("edition"));
		printYearCol.setCellValueFactory(new PropertyValueFactory<Book, String>("printYear"));
		bookAuthorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		subjectCol.setCellValueFactory(new PropertyValueFactory<Book, String>("subject"));
		availableCopiesCol.setCellValueFactory(new PropertyValueFactory<Book, String>("availableCopies"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
		shelfCol.setCellValueFactory(new PropertyValueFactory<Book, String>("shelf"));

		setCmbSearchBy();
		//disable the search button if the search text field is empty!
		BooleanBinding booleanBind = txtSearch.textProperty().isEmpty();
	    btnSearch.disableProperty().bind(booleanBind);
	   // if(!LoggedAccount.logged)
	    //	btnOrderBook.setDisable(true);
	}

	public void start(Stage stage) {
		openNewForm("../gui/SearchForm.fxml", "Search Form");

	}

	/**
	 * initialize the Search By combo box
	 */
	private void setCmbSearchBy() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Name");
		arr.add("Author");
		arr.add("Subject");
		arr.add("Description");
		list = FXCollections.observableArrayList(arr);
		cmbSearchBy.setItems(list);

	}
	
	public void showAlert(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR,content);
		alert.setHeaderText(header);
		alert.show();
	}
	
	public void openNewForm(String resource, String title) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(resource));
			Stage stage = new Stage();
  			Scene scene = new Scene(root);
  			stage.setScene(scene);
  			stage.setTitle(title);
  			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


