package mysql.database.access;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableView;

public class SelectTableView {

	private TableView table;

	private List<String> columns;

	public SelectTableView() {
		this.columns = new ArrayList<String>();
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public TableView getTable() {
		return table;
	}

	public void setTable(TableView table) {
		this.table = table;
	}
}
