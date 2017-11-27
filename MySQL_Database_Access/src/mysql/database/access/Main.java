package mysql.database.access;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainView view = new MainView();
		view.showView();
		view.showAndWait();
	}

}
