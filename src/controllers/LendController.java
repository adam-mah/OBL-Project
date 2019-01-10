package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LendController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imgBack;

    @FXML
    private TextField txtBookIdD;

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
    void btnBookLookupPressed(ActionEvent event) {

    }

    @FXML
    void btnClearPressed(ActionEvent event) {

    }

    @FXML
    void btnLendBookPressed(ActionEvent event) {

    }

    @FXML
    void imgBackClicked(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }
}
