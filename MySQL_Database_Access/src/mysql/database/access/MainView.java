package mysql.database.access;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainView extends Stage {

	private TextField urlField;
	private TextField usernameField;
	private TextField passwordField;
	private TextField queryField;
	private DatabaseAccess databaseAccess;
	private SQLExecutor sqlExecutor;

	private Button startQuery;

	public void showView() {

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		databaseAccess = new DatabaseAccess();
		sqlExecutor = new SQLExecutor(databaseAccess, executorService);

		this.setTitle("MySQL Database Access");
		this.setResizable(false);

		Pane rootpane = new Pane();

		Scene scene = new Scene(rootpane);
		this.setWidth(500);
		this.setHeight(600);

		this.setScene(scene);

		rootpane.getChildren().addAll(getLayout(getUrlField(), getUsernameField(), getPasswordField(), getQueryField(),
				getStartQueryButton()));

		this.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				executorService.shutdownNow();
			}
		});

	}

	private TextField getQueryField() {
		queryField = new TextField();
		queryField.setLayoutX(25);
		return queryField;
	}

	private TextField getUrlField() {
		urlField = new TextField();
		urlField.setLayoutX(25);
		return urlField;
	}

	private TextField getPasswordField() {
		passwordField = new TextField();
		passwordField.setLayoutX(25);
		return passwordField;
	}

	private TextField getUsernameField() {
		usernameField = new TextField();
		usernameField.setLayoutX(25);
		return usernameField;
	}

	private Button getStartQueryButton() {

		startQuery = new Button("Process Query");
		startQuery.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String query = queryField.getText();

				databaseAccess.setUrl(urlField.getText());
				databaseAccess.setUsername(usernameField.getText());
				databaseAccess.setPassword(passwordField.getText());
				databaseAccess.setDriver("com.mysql.jdbc.Driver");

				if (sqlExecutor.executeSQLStatement(query)) {
					Alert information = new Alert(AlertType.INFORMATION);
					information.setTitle("Information");
					information.setHeaderText("SQL statement was executed.");
					information.setContentText("Execution of SQL statement '" + query + "' was successful.");

				}
			}
		});

		return startQuery;
	}

	private VBox getLayout(TextField urlField, TextField usernameField, TextField passwordField, TextField queryField,
			Button startQuery) {
		VBox vbox = new VBox();
		HBox hboxUrl = new HBox();
		HBox hboxUsername = new HBox();
		HBox hboxPassword = new HBox();
		HBox hboxQuery = new HBox();

		Label urlLabel = new Label("URL:");
		hboxUrl.getChildren().addAll(urlLabel, urlField);
		hboxUrl.setSpacing(40);

		Label usernameLabel = new Label("Username:");
		hboxUsername.getChildren().addAll(usernameLabel, usernameField);
		hboxUsername.setSpacing(8);

		Label passwordLabel = new Label("Password:");
		hboxPassword.getChildren().addAll(passwordLabel, passwordField);
		hboxPassword.setSpacing(11);

		Label queryLabel = new Label("SQL Query:");
		hboxQuery.getChildren().addAll(queryLabel, queryField);
		hboxQuery.setSpacing(4);

		vbox.getChildren().addAll(hboxUrl, hboxUsername, hboxPassword, hboxQuery, startQuery);
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(5, 1, 1, 50));

		return vbox;
	}

}
