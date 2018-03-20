package dialogs;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class DeleteEntryDialog extends Alert{

	public DeleteEntryDialog(){
		super(AlertType.CONFIRMATION);
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Deletion");
		this.setHeaderText("Deleting an entry");
		this.setContentText("Are you sure you want to delete this entry?");
	}
}
