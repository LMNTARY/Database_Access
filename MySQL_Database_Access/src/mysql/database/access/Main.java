package mysql.database.access;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url = "jdbc:mysql://IP/dbName";
		String username = "Username";
		String password = "Password";
		String dbTable = "Table";
		String driver = "com.mysql.jdbc.Driver";

		String query = "SELECT * FROM " + dbTable;

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		DatabaseAccess databaseAccess = new DatabaseAccess();
		databaseAccess.setDriver(driver);
		databaseAccess.setUrl(url);
		databaseAccess.setUsername(username);
		databaseAccess.setPassword(password);

		if (new SQLExecutor(databaseAccess, executorService).executeSQLStatement(query)) {
			System.out.println("Execution of SQL statement '" + query + "' was successful.");
		}

		executorService.shutdown();
	}

}
