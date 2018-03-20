package views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import controller.PersistenceController;
import controller.GUIController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Entry;

public class ListPane extends BorderPane {

	private ListTab tab;
	private TitledPane title;
	private VBox content;
	private ContextMenu context;
	private ScrollPane scroll;
	private String type;
	private ArrayList<Entry> data;
	private Button addButton;
	
	private static final String liststyle = "stylesheets/contentlist.css";
	private static final String liststyle_colorblind = "stylesheets/contentlist_colorblind.css";

	public ListPane(ListTab tab, String name, ArrayList<Entry> data,boolean finished) {
		super();
		this.getStylesheets().add(PersistenceController.isColorblind() ? liststyle_colorblind : liststyle);
		this.tab = tab;
		this.type = name;
		this.data = data;
		MenuItem item = new MenuItem("Add new entry");
		item.setOnAction( action -> { 
			Entry e = GUIController.createEntry(type, finished);
			GUIController.addEntry(e);
		});
		context = new ContextMenu(item);
		content = new VBox();
		content.getStyleClass().add("vbox");
		
		EventHandler<MouseEvent> mouseevent = evt -> {
			if(evt.isSecondaryButtonDown()){
				boolean b = true;
				for(Node n : content.getChildren()){
					if(n.localToScreen(n.getBoundsInLocal()).contains(evt.getScreenX(),evt.getScreenY())){
						b = false;
						break;
					}
				}
				if(b) context.show(content,evt.getScreenX(),evt.getScreenY());
				else context.hide();
			}else context.hide();
		};
		content.setOnMousePressed(mouseevent);
		title = new TitledPane();
		title.setCollapsible(false);
		title.setContent(content);
		Label headerlabel = new Label(type + " - " + (finished ? "Finished" : "Unfinished"));
		headerlabel.setAlignment(Pos.CENTER);
		title.setGraphic(headerlabel);
		title.setContentDisplay(ContentDisplay.CENTER);
		title.layout();
		title.setMinWidth(Program.getStage().getWidth() * 0.3);
		title.minWidthProperty().bind(this.widthProperty());
		
		this.scroll = new ScrollPane(title);
		scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.prefWidthProperty().bind(title.widthProperty());
		scroll.setOnMousePressed(mouseevent);

		addButton = new Button("Add new Entry");
		addButton.setPadding(new Insets(10, 10, 10, 10));
		addButton.setTooltip(new Tooltip("add a new entry to this list"));
		addButton.setOnAction(evt -> {
			Entry e = GUIController.createEntry(type, finished);
			GUIController.addEntry(e);
		});		
		
		//this.setTop(new StackPane(new Label(finished ? "Finished" : "Unfinished")));
		this.setCenter(scroll);
		this.setBottom(addButton);
		BorderPane.setAlignment(addButton, Pos.CENTER);

		refresh();
	}

	public void addNewItem(Entry entry) {
		this.data.add(entry);
		refresh();
	}

	public boolean removeItem(Entry entry) {
		this.data.remove(entry);
		refresh();
		return true;
	}

	public void refresh(String search,Comparator<Entry> comparator) {
		this.content.getChildren().clear();
		data.stream().filter(filterfunction(search)).sorted(comparator).forEach(entry -> {
			Label newlabel = new Label(entry.getTitle());
			newlabel.setOnMousePressed(evt ->{
				if(evt.isPrimaryButtonDown()) tab.updateInfoPanel(entry);
			});
			newlabel.setId("entrylabel");
			this.content.getChildren().add(newlabel);
		});
	}
	
	public void refresh(){
		GUIController.FilterAndSort();
	}
	
	private Predicate<Entry> filterfunction(String input){
		return entry -> input == null || input.equals("") ? true : entry.getTitle().toLowerCase().contains(input.toLowerCase());
	}

	public void transferTo(ListPane acc, Entry e) {
		this.data.remove(e);
		this.refresh();
		e.setExperienced(!e.hasExperienced());
		acc.data.add(e);
		acc.refresh();
	}
}
