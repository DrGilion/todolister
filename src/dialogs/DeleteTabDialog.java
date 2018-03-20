package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.StageStyle;

public class DeleteTabDialog extends Alert{

	private ButtonType cancel;
	private ButtonType confirm;

	
	public DeleteTabDialog(){
		super(AlertType.CONFIRMATION);
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Category Deletion");
		this.setHeaderText("Deleting a category");
		this.setContentText("Are you sure you want to delete this category and all its underlying entries?");
		confirm = new ButtonType("Yes, delete", ButtonData.OK_DONE);
		cancel = new ButtonType("No, don't delete", ButtonData.CANCEL_CLOSE);
		this.getButtonTypes().setAll(confirm, cancel);
	}
	
	public boolean isCancel(){
		return this.getResult() == cancel;
	}
	
	public boolean isConfirm(){
		return this.getResult() == confirm;
	}
}
