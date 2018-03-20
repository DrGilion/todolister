package dialogs;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.StageStyle;
import model.Rating;

public class EntryRatingDialog extends ChoiceDialog<Rating>{

	public EntryRatingDialog(Rating r){
		super(r,Rating.values());
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Rating Change");
		this.setHeaderText("Changing existing Entry's Rating");
		this.setContentText("rating for the entry: ");
	}
}
