package dialogs;

import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class EntryNameDialog extends TextInputDialog{

	public EntryNameDialog(String startvalue){
		super();
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Name Change");
		this.setHeaderText("Changing existing Entry's Name");
		this.setContentText("Name of the entry: ");
		this.getEditor().setText(startvalue);
	}
}
