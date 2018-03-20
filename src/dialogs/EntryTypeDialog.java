package dialogs;

import java.util.Collection;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.StageStyle;

public class EntryTypeDialog extends ChoiceDialog<String>{
	
	public EntryTypeDialog(String b,Collection<String> list){
		super(b,list);
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Type Change");
		this.setHeaderText("Changing existing entry type");
		this.setContentText("Type for the entry: ");
	}
}
