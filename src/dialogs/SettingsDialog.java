package dialogs;

import java.io.File;

import controller.PersistenceController;
import controller.PersistenceController.RatingDisplay;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import views.Program;

public class SettingsDialog extends Dialog<Boolean>{
	
	private VBox content;
	private CheckBox colorblindbox;
	private CheckBox flipSides;
	private RadioButton ratingtextbutton;
	private RadioButton ratingstarbutton;
	/*private RadioButton separatesavebutton;
	private RadioButton compactsavebutton;*/
	private TextField directoryfield;
	private Button searchdirectorybutton;

	public SettingsDialog(){
		super();
		this.setTitle("Settings");
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.content = new VBox();
		this.content.setSpacing(30);
		
		this.colorblindbox = new CheckBox("activate colorblind mode?");
		this.flipSides = new CheckBox("swap list sides?");
		
		ToggleGroup ratinggroup = new ToggleGroup();
		this.ratingtextbutton = new RadioButton("display rating as text");
		this.ratingstarbutton = new RadioButton("display rating as stars");
		this.ratingtextbutton.setToggleGroup(ratinggroup);
		this.ratingstarbutton.setToggleGroup(ratinggroup);
		HBox ratingbox = new HBox(ratingstarbutton,ratingtextbutton);
		ratingbox.setSpacing(10);
		
		/*ToggleGroup savegroup = new ToggleGroup();
		this.separatesavebutton = new RadioButton("save lists in seperate files");
		this.compactsavebutton = new RadioButton("save lists in single file");
		this.separatesavebutton.setToggleGroup(savegroup);
		this.compactsavebutton.setToggleGroup(savegroup);
		HBox savebox = new HBox(separatesavebutton,compactsavebutton);
		savebox.setSpacing(10);*/
		
		this.directoryfield = new TextField();
		this.directoryfield.setText(PersistenceController.getDirectoryURL());
		this.searchdirectorybutton = new Button("Search directory...");
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose new folder for your lists");
		this.searchdirectorybutton.setOnAction( evt -> {
			File file = chooser.showDialog(Program.getStage());
			directoryfield.setText(file.getAbsolutePath());
		});
		HBox directorybox = new HBox(directoryfield,searchdirectorybutton);
		directorybox.setSpacing(10);
		directorybox.setAlignment(Pos.CENTER);
		
		content.getChildren().addAll(colorblindbox,flipSides,ratingbox,/*savebox,*/directorybox);
		
		this.getDialogPane().setContent(content);
		ButtonType acceptButtonType = new ButtonType("Save Settings", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);
		this.setResultConverter( button -> {
			if(button == acceptButtonType){
				PersistenceController.setColorblind(colorblindbox.isSelected());
				PersistenceController.setSidesFlipped(flipSides.isSelected());
				PersistenceController.setRatingDisplay(ratingtextbutton.isSelected() ? RatingDisplay.TEXT : RatingDisplay.STARS);
				//DataManager.setSavingType(separatesavebutton.isSelected() ? SavingType.MULTIPLE_FILES : SavingType.SINGLE_FILE);
				PersistenceController.setDirectoryURL(directoryfield.getText());
				return true;
			}
			return false;
		});
		
		initValues();
	}
	
	private void initValues(){
		if(PersistenceController.isColorblind()) colorblindbox.setSelected(true);
		if(PersistenceController.isSidesFlipped()) flipSides.setSelected(true);
		if(PersistenceController.getRatingDisplay() == RatingDisplay.TEXT) ratingtextbutton.setSelected(true);
		else ratingstarbutton.setSelected(true);
		/*if(DataManager.getSavingType() == SavingType.MULTIPLE_FILES) separatesavebutton.setSelected(true);
		else compactsavebutton.setSelected(true);*/
		directoryfield.setText(PersistenceController.getDirectoryURL());
	}
}
