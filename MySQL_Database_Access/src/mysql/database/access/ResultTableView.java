package mysql.database.access;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ResultTableView extends Stage {

	private TableView table;

	public void initView(TableView table) {

		this.table = table;

		this.setTitle("Result Table");
		Pane pane = new Pane();
		Scene scene = new Scene(pane);
		this.setWidth(500);
		this.setHeight(500);

		this.setScene(scene);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(table);
		scrollPane.setPadding(new Insets(10, 10, 10, 10));
		scrollPane.setMaxHeight(500);
		scrollPane.setMaxWidth(500);

		pane.getChildren().add(scrollPane);

		this.showAndWait();
	}

	private MenuBar getMenuBar() {

		return null;
	}

	private MenuItem getMenuItem() {
		return null;
	}
}
