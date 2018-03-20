package views;

import java.util.ArrayList;
import java.util.Comparator;

import controller.PersistenceController;
import controller.GUIController;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Entry;

public class ListTab extends Tab{
	
	private HBox content;
	private InfoPanel infopane;
	private ListPane finishedSide;
	private ListPane unfinishedSide;
	private String type;
	
	public ListTab(String type){
		super();
		this.type = type;
		buildTab();
	}
	
	public void buildTab(){
		content = new HBox();
		SplitPane split = new SplitPane();
		BorderPane leftBorder = new BorderPane();
		leftBorder.setPadding(new Insets(10,0,10,0));
		BorderPane rightBorder = new BorderPane();
		rightBorder.setPadding(new Insets(10,0,10,0));
		
		//spliting data
		ArrayList<Entry> alldata = Entry.EntriesFor(type);
		ArrayList<Entry> finished = new ArrayList<Entry>();
		ArrayList<Entry> unfinished = new ArrayList<Entry>();
		alldata.stream().forEach( entry ->{ 
			if(entry.hasExperienced()) finished.add(entry);
			else unfinished.add(entry);
		});
		
		
		//propertions and stuff
		finishedSide = new ListPane(this,type,finished,true);		
		unfinishedSide = new ListPane(this,type,unfinished,false);
		infopane = new InfoPanel();
		
		if(PersistenceController.isSidesFlipped()) split.getItems().addAll(unfinishedSide,infopane,finishedSide);
		else split.getItems().addAll(finishedSide,infopane,unfinishedSide);
		split.setDividerPositions(0.25f,0.75f);
		split.prefWidthProperty().bind(Program.getStage().widthProperty());
		split.prefHeightProperty().bind(Program.getStage().heightProperty());
		content.getChildren().add(split);
		this.setContent(content);
		Label tablabel = new Label(type);
		tablabel.setOnMousePressed( mouse -> {
			if(mouse.getButton()==MouseButton.SECONDARY){
				MenuItem item = new MenuItem("Rename Tab");
				item.setOnAction( action -> GUIController.renameTab(this));
				new ContextMenu(item).show(this.getGraphic(),Side.RIGHT,0,0);
			}
		});
		this.setGraphic(tablabel);
		this.setOnCloseRequest( evt -> {
			if(!GUIController.deleteTab(this)){
				evt.consume();
			}
		});
	}
	
	public void updateListPanes(String input,Comparator<Entry> comparator){
		finishedSide.refresh(input,comparator);
		unfinishedSide.refresh(input,comparator);
		updateInfoPanel(null);
	}
	
	public void renameTab(String newname){
		this.type= newname;
		buildTab();
		this.updateInfoPanel(null);
	}
	
	public void updateInfoPanel(Entry entry){
		infopane.rebuild(entry);
	}
	
	public void swapEntry(Entry entry){
		this.getTypeAccordion(entry).transferTo(this.getTypeAccordion(!entry.hasExperienced()), entry);
		updateInfoPanel(entry);
	}
	
	public void removeEntry(Entry entry){
		this.getTypeAccordion(entry).removeItem(entry);
	}
	
	public ListPane getTypeAccordion(boolean exp){
		return exp ? finishedSide : unfinishedSide;
	}
	
	public ListPane getTypeAccordion(Entry entry){
		return this.getTypeAccordion(entry.hasExperienced());
	}

	public String getType() {
		return type;
	}

}
