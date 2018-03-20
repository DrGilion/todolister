package dialogs;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class AboutDialog extends Alert{

	public AboutDialog(){
		super(Alert.AlertType.INFORMATION);
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("About this Application");
		this.setHeaderText(null);
		this.setContentText("Hello! This application can manage lists of categories. You can add new categories,add entries to your created lists and edit these entries to your liking. Have fun :)");
	}
}
