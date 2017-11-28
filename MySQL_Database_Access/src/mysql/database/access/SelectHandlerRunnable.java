package mysql.database.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class SelectHandlerRunnable implements Runnable {

	private Connection dbConnection;
	private PreparedStatement preparedStatement;
	private SelectTableView table;
	@SuppressWarnings("rawtypes")
	private TableView tableView;
	@SuppressWarnings("rawtypes")
	private ObservableList tableData;
	private List<String> columns;

	public SelectHandlerRunnable(Connection dbConnection, PreparedStatement preparedStatement, SelectTableView table) {
		this.dbConnection = dbConnection;
		this.preparedStatement = preparedStatement;
		this.table = table;
		this.columns = new ArrayList<String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {

		try {
			tableView = new TableView<>();
			tableData = FXCollections.observableArrayList();
			ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
			int numberOfColumns = resultSetMetaData.getColumnCount();

			ResultSet resultSet = preparedStatement.getResultSet();

			// System.out.println("Result from " + resultSetMetaData.toString() + " is:");
			// for (int i = 1; i <= numberOfColumns; i++) {
			// final int j = i;
			// TableColumn column = new TableColumn(resultSetMetaData.getColumnName(i));
			// columns.add(resultSetMetaData.getColumnName(i));
			// tableView.getColumns().add(column);
			// }
			for (int i = 1; i <= numberOfColumns; i++) {
				final int j = i;
				TableColumn column = new TableColumn(resultSetMetaData.getColumnName(i));
				column.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> data) {
								return new SimpleStringProperty(data.getValue().get(j - 1).toString());
							}
						});
				tableView.getColumns().addAll(column);
			}
			System.out.println("");
			while (resultSet.next()) {
				ObservableList row = FXCollections.observableArrayList();
				for (int i = 1; i <= numberOfColumns; i++) {
					System.out.printf("%s: " + resultSet.getObject(i) + " ", resultSetMetaData.getColumnName(i));
					row.add(resultSet.getObject(i));
				}
				tableData.add(row);
				System.out.println("");
			}
			tableView.setItems(tableData);
			table.setTable(tableView);
			table.setColumns(columns);

			// Use it so that it runs on FX application thread
			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					new ResultTableView().initView(tableView);
				}

			});

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
