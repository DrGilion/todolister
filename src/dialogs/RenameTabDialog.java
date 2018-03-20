package dialogs;

import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class RenameTabDialog extends TextInputDialog{

	public RenameTabDialog(String oldname){
		super();
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Renaming Category");
		this.setHeaderText("Renaming existing Category");
		this.setContentText("name for the category: ");
	}
}
