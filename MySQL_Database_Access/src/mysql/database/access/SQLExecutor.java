package mysql.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class SQLExecutor {

	private DatabaseAccess databaseAccess;
	private ExecutorService executorService;

	public SQLExecutor(DatabaseAccess databaseAccess, ExecutorService executorService) {
		this.databaseAccess = databaseAccess;
		this.executorService = executorService;
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
				executorService.execute(new SelectHandlerRunnable(dbConnection, preparedStatement));
			} else {
				preparedStatement.close();
				dbConnection.close();
			}
			isExecuted = true;
		} catch (SQLException e) {
			System.err.println("Execution of SQL statement '" + query
					+ "' failed. Check your url, username, password! Check your SQL statement!");
		} catch (ClassNotFoundException e) {
			System.err.println("Check driver path!");
		}

		return isExecuted;
	}

}
