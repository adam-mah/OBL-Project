package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Main window of the OBL Library System
 * @author Adam Mahameed [Athl1n3]
 *
 */

public class MainFormController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ImageView imgLogin;

	@FXML
	private ImageView imgSearch;

	/**
	 * Open user login form
	 * @param event
	 */
	@FXML
	void imgLoginClicked(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = ((Node) event.getSource()).getScene();
		SceneController.push(scene);
		LoginController LoginForm = new LoginController();
		try {
			LoginForm.start(stage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Open search form
	 * @param event
	 */
	@FXML
	void imgSearchClicked(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = ((Node) event.getSource()).getScene();
		SceneController.push(scene);
		SearchController SearchForm = new SearchController();
		try {
			SearchForm.start(stage,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		//DatabaseController.saveFile();
	}

	void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../gui/MainForm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Librarian Main");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
