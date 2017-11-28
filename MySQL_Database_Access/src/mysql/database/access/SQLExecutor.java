package mysql.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SQLExecutor {

	private DatabaseAccess databaseAccess;
	private ExecutorService executorService;
	private SelectTableView table;

	public SQLExecutor(DatabaseAccess databaseAccess, ExecutorService executorService, SelectTableView table) {
		this.databaseAccess = databaseAccess;
		this.executorService = executorService;
		this.table = table;
	}

	public boolean executeSQLStatement(String query) {

		boolean isExecuted = false;

		try {
			Connection dbConnection = DriverManager.getConnection(databaseAccess.getUrl(), databaseAccess.getUsername(),
					databaseAccess.getPassword());
			Class.forName(databaseAccess.getDriver());

			PreparedStatement preparedStatement = dbConnection.prepareStatement(query);

			/*
			 * - Use executeUpdate() for INSERT INTO, UPDATE, DELETE, CREATE statements
			 * 
			 * - Use executeQuery() for SELECT statement
			 * 
			 * - Use execute() if you don't know which statement will be executed (returns
			 * true for SELECT statement, false otherwise)
			 * 
			 */

			if (preparedStatement.execute()) {
				executorService.execute(new SelectHandlerRunnable(dbConnection, preparedStatement, table));
			} else {
				preparedStatement.close();
				dbConnection.close();
			}
			isExecuted = true;
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not execute SQL statement!");
			alert.setContentText("Check your url, username, password! Check your SQL statement!");
			alert.showAndWait();

		} catch (ClassNotFoundException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Driver path is incorrect!");
			alert.setContentText("Check your driver path.");
			alert.showAndWait();
		}

		return isExecuted;
	}

}
