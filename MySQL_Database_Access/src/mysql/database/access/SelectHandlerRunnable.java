package mysql.database.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SelectHandlerRunnable implements Runnable {

	private Connection dbConnection;
	private PreparedStatement preparedStatement;

	public SelectHandlerRunnable(Connection dbConnection, PreparedStatement preparedStatement) {
		this.dbConnection = dbConnection;
		this.preparedStatement = preparedStatement;
	}

	@Override
	public void run() {

		int numberOfColumns = 0;

		try {
			ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
			numberOfColumns = resultSetMetaData.getColumnCount();

			ResultSet resultSet = preparedStatement.getResultSet();

			System.out.println("Result from " + resultSetMetaData.toString() + " is:");

			while (resultSet.next()) {
				for (int i = 1; i < numberOfColumns; i++) {
					System.out.printf("%s: " + resultSet.getObject(i) + " ", resultSetMetaData.getColumnName(i));
				}
				System.out.println("");
			}

		} catch (SQLException ignored) {
		} finally {
			while (true) {
				try {
					preparedStatement.close();
					break;
				} catch (SQLException ignored) {
				}
			}
			while (true) {
				try {
					dbConnection.close();
					break;
				} catch (SQLException ignored) {
				}
			}
		}

	}

}
