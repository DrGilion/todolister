package views;

import java.awt.Toolkit;
import java.util.Locale;

import controller.GUIController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Program extends Application {
	
	private static Stage stage = null;
	final static double version = 0.4;

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Program.stage = primaryStage;
		Program.stage.getIcons().add(new Image("icons/list.png"));
		Scene scene = new Scene(new MainFrame());
		primaryStage.setTitle("To-Do-Lister "+version);
		
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		primaryStage.setWidth(screenWidth*0.7);
		primaryStage.setMinWidth(screenWidth/2);
		primaryStage.setHeight(screenHeight/1.5);
		primaryStage.setMinHeight(screenHeight/2);
		
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(evt -> {
			GUIController.exitApp();
			evt.consume();
		});
		primaryStage.show();
	}

	public static Stage getStage() {
		return stage;
	}

	public static double getVersion() {
		return version;
	}

}
