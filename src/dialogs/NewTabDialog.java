package dialogs;

import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class NewTabDialog extends TextInputDialog{

	public NewTabDialog(){
		super();
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("new Category");
		this.setHeaderText("Adding new category");
		this.setContentText("name of the new category: ");
	}
	
}
