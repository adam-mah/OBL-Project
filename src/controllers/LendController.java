package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import entities.Account;
import entities.Book;
import entities.Book.bookType;
import entities.BookCopy;
import entities.LentBook;
import entities.LibrarianAccount;
import entities.UserAccount;
import entities.UserAccount.accountStatus;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 
 * @author Alaa Grable
 * @version 1.0 [17.1.2019]
 * 
 */

public class LendController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<LentBook> tableView;

	@FXML
	private ImageView imgBack;

	@FXML
	private TextField txtBookID;

	@FXML
	private TextField txtBookName;

	@FXML
	private TextField txtBookType;

	@FXML
	private TextField txtAvailableCopies;

	@FXML
	private TextField txtUserID;

	@FXML
	private DatePicker dtDueDate;

	@FXML
	private TextField txtName;

	@FXML
	private Button btnBookLookup;

	@FXML
	private DatePicker dtIssueDate;

	@FXML
	private Button btnLendBook;

	@FXML
	private Button btnClear;

	@FXML
	private TextField txtSerialNumber;

	private Account lenderAccount;
	private Book lentBook;
	private boolean lookedUp;
	private Book selectedBook;

	/**
	 * When BookLookUp button is pressed , this method will be called
	 * 
	 * @param event
	 */
	@FXML
	void btnBookLookupPressed(ActionEvent event) {

		if (lookedUp == false) {
			lentBook = DatabaseController.getBook(Integer.parseInt(txtBookID.getText()));
			// validate if there is such a book with the inputed book ID
			if (lentBook == null) {
				// if not , then let the user know
				alertWarningMessage("There is no such book in the library");
			} else {
				lenderAccount = DatabaseController.getAccount(Integer.parseInt(txtUserID.getText()));
				// validate if there is such an account with the inputed ID
				if (lenderAccount == null) {
					// if not , then let the user know
					alertWarningMessage("User doesn't exist!");
				} else {
					// validate if the user account status is not active
					if (lenderAccount instanceof UserAccount) {
						if (!((UserAccount) lenderAccount).getStatus().equals(accountStatus.Active)) {
							alertWarningMessage(
									"This account is " + ((UserAccount) lenderAccount).getStatus().toString()
											+ "\nNo lending can be performed");
						} else {
							// if the book ID & the user ID is found in the DB , then display the details
							// about the book and the user
							txtBookName.setText(lentBook.getName());
							txtBookType.setText(lentBook.getBookType().toString());
							txtAvailableCopies.setText(String.valueOf(lentBook.getAvailableCopies()));
							txtName.setText(lenderAccount.getFirstName());

							if (lentBook.getBookType() == bookType.Wanted)
								dtDueDate.setValue(LocalDate.now().plusDays(3));// the book is "wanted" so lent the book
																				// for // 3 days only
							else
								dtDueDate.setValue(LocalDate.now().plusWeeks(2));// the books is "regular" , then lent
																					// the
																					// book for 2 weeks
							// the user account status is active , then validate if there is copies of that
							// book

							if (lentBook.getAvailableCopies() == 0) {
								// if there is no copies of that book then let the user know that
								alertWarningMessage("There are no copies are available of the book " + "'"
										+ lentBook.getName() + "'");
								btnLendBook.setDisable(true);
							} else {
								// if everything is okay then enable the button to let the user be able to lent
								// the book
								btnLendBook.setDisable(false);
							}
							txtBookID.setDisable(true);
							txtUserID.setDisable(true);
							lookedUp = true;
						}
					} else if (lenderAccount instanceof LibrarianAccount)
						alertWarningMessage("Can't use a librarian ID for lending!");
				}
			}
		} else
			alertWarningMessage("User/Book already looked up!");
	}

	/*
	 * Clear all textFields
	 */
	@FXML
	void btnClearPressed(ActionEvent event) {
		txtBookID.clear();
		txtBookName.clear();
		txtBookType.clear();
		txtAvailableCopies.clear();
		txtUserID.clear();
		txtName.clear();
		dtDueDate.getEditor().clear();
		txtBookID.setDisable(false);
		txtUserID.setDisable(false);
		lookedUp = false;
		txtSerialNumber.clear();
	}

	/**
	 * When LendBook button is pressed , this method will be called
	 * 
	 * @param event
	 */
	@FXML
	void btnLendBookPressed(ActionEvent event) {

		// create the lent book request with the appropriate returning time
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// you need to get the copy from bookCopy table
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BookCopy bookCopy = DatabaseController.getBookCopy(txtBookID.getText(), txtSerialNumber.getText());
		if (bookCopy != null) {
			if (DatabaseController.getCount("savedCopy", "bookID", txtBookID.getText()) != lentBook
					.getAvailableCopies()) {
				if (bookCopy.isLent())
					alertWarningMessage("Book with this serial number is already lent");
				else {
					LentBook lntbook = new LentBook(lenderAccount.getAccountID(), lentBook, bookCopy, LocalDate.now(),
							dtDueDate.getValue(), LocalDate.parse("1970-01-01"), false);
					// lent the book to the user
					bookCopy.setLent(true);
					DatabaseController.addLentBook(lntbook);
					DatabaseController.updateBookCopy(bookCopy);
					DatabaseController.updateBookAvailableCopies(lentBook, -1);
					// let the user know that the lent process has been cone successfully
					DatabaseController.addActivity(lenderAccount.getAccountID(),
							"Lent Book [Book ID: " + lntbook.getBook().getBookID() + "]");
					Alert alert = new Alert(AlertType.INFORMATION, "Book has been lent successfully", ButtonType.OK);
					alert.show();
				}
			} else
				alertWarningMessage("This book has already saved copies for other users");
		} else
			alertWarningMessage("This book serial number doesn't exist!");

	}

	/**
	 * Back to the previous screen
	 */
	@FXML
	void imgBackClicked(MouseEvent event) {
		Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
		// get the previous scene
		Scene scene = SceneController.pop();
		stage.setScene(scene);
		stage.setTitle("Main");
	}

	/**
	 * Initialise the current screen
	 */
	@FXML
	void initialize() {
		lookedUp = false;
		// a listener to validate if the ID length is not greater than 9 digits and if
		// it's only contain numbers
		txtBookID.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtBookID.setText(newValue.replaceAll("[^\\d]", ""));
				alertWarningMessage("The book ID must contain only numbers");
			}
			
			
		});

		// a listener to validate if the ID length is not greater than 9 digits and if
		// it's only contain numbers
		txtUserID.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtUserID.setText(newValue.replaceAll("[^\\d]", ""));
				alertWarningMessage("The ID must contain only numbers");
			}
			if (txtUserID.getLength() > 9) {
				txtUserID.setText(oldValue);
				alertWarningMessage("The ID must be 9 numbers");
			}
		});
		// display the date of today
		dtIssueDate.setValue(LocalDate.now());
		// make this field inedible
		dtIssueDate.setEditable(false);
		// disable LendBook button
		btnLendBook.setDisable(true);

		// Enable BookLookUp button only when the book ID textfield & user ID textField
		// is not empty
		BooleanBinding booleanBind = txtBookID.textProperty().isEmpty().or(txtUserID.textProperty().isEmpty());
		btnBookLookup.disableProperty().bind(booleanBind);
	}

	void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../gui/LendForm.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Lend book");
		stage.sizeToScene();
		stage.setScene(scene);
		stage.show();
	}
	
	void start(Stage stage, Book selectedBook) throws Exception {
		this.selectedBook= selectedBook;
		Parent root = FXMLLoader.load(getClass().getResource("../gui/LendForm.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Lend book");
		stage.sizeToScene();
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Show an appropriate alert to the user when an error occur
	 * 
	 * @param msg
	 */
	private void alertWarningMessage(String msg) {
		new Alert(AlertType.WARNING, msg, ButtonType.OK).show();
	}
}