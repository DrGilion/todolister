package views;

import controller.PersistenceController;
import controller.GUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Entry;
import model.Rating;

public class InfoPanel extends BorderPane{
	
	private Entry entry;
	private Label namelabel;
	private Label entrynamelabel;
	private TextField entrynamefield;
	private HBox namepane;
	
	private Label typelabel;
	private Label entrytypelabel;
	private ComboBox<String> typebox;
	private HBox typepane;
	
	private Label ratinglabel;
	private Label entryratinglabel;
	private ComboBox<Rating> ratingbox;
	private StarBox starbox;
	private HBox ratingpane;
	
	private Label statuslabel;
	private Button statusbutton;
	private HBox statuspane;
	
	private TextArea textpane;
	private VBox textbox;
	
	private Button deletebutton;
	
	private VBox toppane;
	
	
	private static final String infopanelstyle = "stylesheets/infopanel.css";
	private static final String infopanelstyle_colorblind = "stylesheets/infopanel_colorblind.css";
	
	public InfoPanel(){
		super();
		this.entry = new Entry();
		buildEmpty();
	}
	
	public InfoPanel(Entry entry){
		super();
		this.entry = entry;
		buildPanel();
	}
	
	private void buildPanel(){
		this.getStylesheets().add(PersistenceController.isColorblind() ? infopanelstyle_colorblind : infopanelstyle);
		this.getChildren().clear();
		this.getStyleClass().add("border-pane");
		
		this.buildNamePane();
		this.buildTypePane();
		this.buildRatingPane();
		this.buildLinkPane();
		this.buildStatusPane();
		this.buildDescriptionPane();
		
		deletebutton = new Button("DELETE ENTRY");
		deletebutton.setOnAction( evt -> {
			if(GUIController.removeEntry(entry)) this.buildEmpty();
		});
		deletebutton.setTooltip(new Tooltip("delete the current shown entry"));
		deletebutton.getStyleClass().add("deletebutton");
		
		toppane = new VBox(namepane,typepane,ratingpane,statuspane);
		toppane.getStyleClass().add("vbox");
		
		this.setTop(toppane);
		this.setCenter(textbox);
		this.setBottom(new StackPane(deletebutton));
		
	}
	
	private void buildNamePane(){
		namelabel = new Label("Name : ");
		entrynamelabel = new Label(entry.getTitle());
		
		ConfirmationButtonSet buttonset = new ConfirmationButtonSet("change the name of this entry","Confirm change","Cancel change and keep original name");
		EventHandler<ActionEvent> confirmevent = evt -> {
			GUIController.changeEntryName(entry,entrynamefield.getText());
			namepane.getChildren().set(1, entrynamelabel = new Label(entry.getTitle()));
		};
		EventHandler<ActionEvent> cancelevent = evt -> {
			namepane.getChildren().set(1, entrynamelabel = new Label(entry.getTitle()));
		};
		buttonset.getEditbutton().addEventHandler(ActionEvent.ACTION, evt -> {
			entrynamefield = new TextField(entrynamelabel.getText());
			entrynamefield.setMinWidth(Math.min(105,entrynamefield.getText().length() * 7));
			entrynamefield.setPrefWidth(Math.min(400,entrynamefield.getText().length() * 7));
			entrynamefield.setMaxWidth(400);
			entrynamefield.setOnKeyPressed( key -> {
				if(key.getCode() == KeyCode.ENTER){
					confirmevent.handle(new ActionEvent());
				}
				if(key.getCode() == KeyCode.ESCAPE){
					cancelevent.handle(new ActionEvent());
				}
			});
			
			namepane.getChildren().set(1, entrynamefield);
		});
		buttonset.getConfirmbutton().addEventHandler(ActionEvent.ACTION, confirmevent);
		buttonset.getCancelbutton().addEventHandler(ActionEvent.ACTION, cancelevent);
		namepane = new HBox(namelabel,entrynamelabel,buttonset);
		namepane.setSpacing(15);
		namepane.getStyleClass().add("hbox");
	}
	
	private void buildTypePane(){
		typelabel = new Label("Category : ");
		entrytypelabel = new Label(entry.getType());
		
		ConfirmationButtonSet buttonset = new ConfirmationButtonSet("change the category of this entry","Confirm change","Cancel change and keep original category");
		EventHandler<ActionEvent> confirmevent = evt -> {
			GUIController.changeEntryType(entry,typebox.getValue());
			//typepane.getChildren().set(1, entrytypelabel = new Label(entry.getType()));
		};
		EventHandler<ActionEvent> cancelevent = evt -> {
			typepane.getChildren().set(1, entrytypelabel = new Label(entry.getType()));
		};
		buttonset.getEditbutton().addEventHandler(ActionEvent.ACTION, evt -> {
			ObservableList<String> typelist = FXCollections.observableArrayList(Entry.types());
			typebox = new ComboBox<String>(typelist);
			typebox.setValue(entry.getType());
			
			typepane.getChildren().set(1, typebox);
		});
		buttonset.getConfirmbutton().addEventHandler(ActionEvent.ACTION, confirmevent);
		buttonset.getCancelbutton().addEventHandler(ActionEvent.ACTION, cancelevent);
		
		typepane = new HBox(typelabel,entrytypelabel,buttonset);
		typepane.setSpacing(15);
		typepane.getStyleClass().add("hbox");
	}
	
	private void buildRatingPane(){
		ratingpane = new HBox();
		ratinglabel = new Label("Rating : ");
		ConfirmationButtonSet buttonset = new ConfirmationButtonSet("change the rating of this entry","Confirm change","Cancel change and keep original rating");
		switch(PersistenceController.getRatingDisplay()){
		case TEXT:
			entryratinglabel = new Label(entry.getRating().getUIString());
			
			buttonset.getEditbutton().addEventHandler(ActionEvent.ACTION, evt -> {
				ObservableList<Rating> ratinglist = FXCollections.observableArrayList(Rating.values());
				ratingbox = new ComboBox<Rating>(ratinglist);
				ratingbox.setValue(entry.getRating());
				
				ratingpane.getChildren().set(1, ratingbox);
			});
			buttonset.getConfirmbutton().addEventHandler(ActionEvent.ACTION, evt -> {
				GUIController.changeEntryRating(entry,ratingbox.getValue());
				ratingpane.getChildren().set(1, entryratinglabel = new Label(entry.getRating().getUIString()));
			});
			buttonset.getCancelbutton().addEventHandler(ActionEvent.ACTION, evt -> {
				ratingpane.getChildren().set(1, entryratinglabel = new Label(entry.getRating().getUIString()));
			});
			ratingpane.getChildren().addAll(ratinglabel,entryratinglabel,buttonset);
			break;
		case STARS:
			String[] ratingstrings = new String[Rating.values().length];
			for(int i = 0; i< ratingstrings.length ; ++i){
				ratingstrings[i] = Rating.values()[i].getUIString();
			}
			starbox = new StarBox(ratingstrings);
			starbox.setStars(entry.getRating().ordinal());
			
			EventHandler<ActionEvent> confirmevent = evt -> {
				Rating tmprating = Rating.not_rated;
				for(Rating r : Rating.values()){
					if(r.ordinal()== starbox.getCurrentStars()){
						tmprating = r;
						break;
					}
				}
				GUIController.changeEntryRating(entry,tmprating);
				starbox.setStars(tmprating.ordinal());
				
				ratingpane.getChildren().set(1, starbox);
				starbox.activateGlow(false);
				
			};
			EventHandler<ActionEvent> cancelevent = evt -> {
				ratingpane.getChildren().set(1, starbox);
				starbox.activateGlow(false);
			};
			buttonset.getEditbutton().addEventHandler(ActionEvent.ACTION, evt -> {
				starbox.activateGlow(true);
			});
			buttonset.getConfirmbutton().addEventHandler(ActionEvent.ACTION, confirmevent);
			buttonset.getCancelbutton().addEventHandler(ActionEvent.ACTION, cancelevent);
			ratingpane.getChildren().addAll(ratinglabel,starbox,buttonset);
			
			break;
		}
		ratingpane.setSpacing(15);
		ratingpane.getStyleClass().add("hbox");
	}
	
	private void buildLinkPane(){
		
	}
	
	private void buildStatusPane(){
		statuslabel = new Label("Status : " + (entry.hasExperienced() ? "Finished" : "Unfinished"));
		int iconsize = 32;
		statusbutton = new Button("",new ImageView(new Image("icons/arrows.png",iconsize,iconsize,true,true)));
		statusbutton.setOnAction( evt -> GUIController.changeEntryStatus(entry));
		statusbutton.setTooltip(new Tooltip("switch between finished and unfinished"));
		statuspane = new HBox(statuslabel,statusbutton);
		statuspane.setSpacing(15);
		statuspane.getStyleClass().add("hbox");
	}
	
	private void buildDescriptionPane(){
		textpane = new TextArea(entry.getDescription());
		textpane.setEditable(false);
		ConfirmationButtonSet buttonset = new ConfirmationButtonSet("edit description for this entry","Confirm change","Cancel changes and set to original description");
		buttonset.getEditbutton().addEventHandler(ActionEvent.ACTION, evt -> {
			textpane.setEditable(true);
		});
		buttonset.getConfirmbutton().addEventHandler(ActionEvent.ACTION, evt -> {
			textpane.setEditable(false);
			GUIController.changeEntryDescription(entry,textpane.getText());
		});
		buttonset.getConfirmbutton().addEventHandler(ActionEvent.ACTION, evt -> {
			textpane.setEditable(false);
			textpane.setText(entry.getDescription());
		});
		textbox = new VBox(textpane,buttonset);
		textbox.getStyleClass().add("vbox");
	}
	
	private void buildEmpty(){
		this.getChildren().clear();
		this.getStylesheets().add(PersistenceController.isColorblind() ? infopanelstyle_colorblind : infopanelstyle);
		Label nosel = new Label("no item selected");
		this.setCenter(nosel);
		this.entry = null;
	}
	
	public void rebuild(Entry entry){
		this.entry = entry;
		if(this.entry== null) {
			buildEmpty();
		}else buildPanel();
	}
	
	public Button getSwitchButton(){
		return statusbutton;
	}
	
	public Entry getCurrentFocus(){
		return entry;
	}

}
