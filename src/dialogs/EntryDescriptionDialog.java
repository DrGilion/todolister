package dialogs;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

public class EntryDescriptionDialog extends Dialog<String>{

	public EntryDescriptionDialog(String oldtext){
		super();
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Description Change");
		this.setHeaderText("Change existing entry's Description");
		
		// Set the button types.
		ButtonType acceptButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);
		
		Label label = new Label("Description: ");
		
		TextArea textfield = new TextArea();
		textfield.setPromptText("Description");
		textfield.setText(oldtext);
		
		BorderPane pane = new BorderPane();
		pane.setTop(label);
		pane.setCenter(textfield);
		
		this.getDialogPane().setContent(pane);
		
		this.setResultConverter(dialogButton -> {
			return dialogButton == acceptButtonType ? textfield.getText() : null;
		});
		
		
	}
}
