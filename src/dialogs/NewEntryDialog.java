package dialogs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Entry;
import model.Rating;

public class NewEntryDialog extends Dialog<Entry> {

	
	public NewEntryDialog(){
		super();
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Creation");
		this.setHeaderText("Create a new entry");

		// Set the button types.
		ButtonType acceptButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField entryname = new TextField();
		entryname.setPromptText("Name");
		entryname.requestFocus();
		
		ObservableList<Rating> ratinglist = FXCollections.observableArrayList(Rating.values());
		ComboBox<Rating> ratingbox = new ComboBox<Rating>(ratinglist);
		ratingbox.setCellFactory(new Callback<ListView<Rating>, ListCell<Rating>>() {
			@Override
			public ListCell<Rating> call(ListView<Rating> p) {
				final ListCell<Rating> cell = new ListCell<Rating>() {
					@Override
					protected void updateItem(Rating r, boolean bln) {
						super.updateItem(r, bln);
						if (r != null) {
							setText(r.getUIString());
							setTextFill(javafx.scene.paint.Color.BLACK);
						} else {
							setText(null);
						}
					}
				};
				return cell;
			}
		});
		ratingbox.setValue(Rating.not_rated);
		
		ObservableList<String> typelist = FXCollections.observableArrayList(Entry.types());
		ComboBox<String> typebox = new ComboBox<String>(typelist);
		typebox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> p) {
				final ListCell<String> cell = new ListCell<String>() {
					@Override
					protected void updateItem(String s, boolean bln) {
						super.updateItem(s, bln);
						if (s != null) {
							setText(s.toString());
							setTextFill(Color.BLACK);
						} else {
							setText(null);
						}
					}
				};
				return cell;
			}
		});
		
		CheckBox donebox = new CheckBox("Finished?");
		
		TextArea descriptionArea = new TextArea();
		descriptionArea.setPromptText("Enter detailed description here");

		grid.add(new Label("Name of the entry:"), 0, 0);
		grid.add(entryname, 1, 0);
		grid.add(new Label("Rating:"), 0, 1);
		grid.add(ratingbox, 1, 1);
		grid.add(new Label("Category"), 0, 2);
		grid.add(typebox, 1, 2);
		grid.add(donebox,0,3);
		
		grid.add(new Label("Description"), 0, 4);
		grid.add(descriptionArea, 0, 5);

		this.getDialogPane().setContent(grid);
		Platform.runLater(() -> entryname.requestFocus());

		this.setResultConverter(dialogButton -> {
			if (dialogButton == acceptButtonType) {
				Entry result = null;
				try {
					result = new Entry(entryname.getText(), descriptionArea.getText(), ratingbox.getValue(), typebox.getValue(),
							donebox.isSelected());
				} catch (IllegalArgumentException ex) {
					Alert erroralert = new Alert(Alert.AlertType.ERROR);
					erroralert.setContentText("Your Input was incorrect!");
					erroralert.showAndWait();
					return null;
				}
				return result;
			}
			return null;
		});
	}
	
	public NewEntryDialog(String type, boolean done) {
		super();
		this.initStyle(StageStyle.UTILITY);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		this.setTitle("Entry Creation");
		this.setHeaderText("Create a new entry");

		// Set the button types.
		ButtonType acceptButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField entryname = new TextField();
		entryname.setPromptText("Name");
		
		ObservableList<Rating> ratinglist = FXCollections.observableArrayList(Rating.values());
		ComboBox<Rating> ratingbox = new ComboBox<Rating>(ratinglist);
		ratingbox.setCellFactory(new Callback<ListView<Rating>, ListCell<Rating>>() {
			@Override
			public ListCell<Rating> call(ListView<Rating> p) {
				final ListCell<Rating> cell = new ListCell<Rating>() {
					@Override
					protected void updateItem(Rating r, boolean bln) {
						super.updateItem(r, bln);
						if (r != null) {
							setText(r.getUIString());
							setTextFill(javafx.scene.paint.Color.BLACK);
						} else {
							setText(null);
						}
					}
				};
				return cell;
			}
		});
		ratingbox.setValue(Rating.not_rated);
		
		
		TextArea descriptionArea = new TextArea();
		descriptionArea.setPromptText("Enter detailed description here");

		grid.add(new Label("Name of the entry:"), 0, 0);
		grid.add(entryname, 1, 0);
		grid.add(new Label("Rating:"), 0, 1);
		grid.add(ratingbox, 1, 1);
		grid.add(new Label("Description:"), 0, 2);
		grid.add(descriptionArea, 0, 3);

		this.getDialogPane().setContent(grid);
		Platform.runLater(() -> entryname.requestFocus());

		this.setResultConverter(dialogButton -> {
			if (dialogButton == acceptButtonType) {
				Entry result = null;
				try {
					result = new Entry(entryname.getText(), descriptionArea.getText(), ratingbox.getValue(), type, done);
				} catch (IllegalArgumentException ex) {
					Alert erroralert = new Alert(Alert.AlertType.ERROR);
					erroralert.setContentText("Your Input was incorrect!");
					erroralert.show();
					return null;
				}
				return result;
			}
			return null;
		});
	}
}
