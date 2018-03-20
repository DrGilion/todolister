package views;

import java.util.ArrayList;

import controller.PersistenceController;
import controller.GUIController;
import dialogs.AboutDialog;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Entry;

public class MainFrame extends BorderPane {
	
	private MenuBar menubar;
	private ToolBar toolbar;
	private SearchField searchbox;
	private EntrySortBox sortbox;
	private TabPane tabpane;
	private ArrayList<ListTab> tabs;
	
	
	public MainFrame() {
		super();
		GUIController.initiate(this);
		createMenu();
		createTabs();
	}
	
	private void createMenu() {
		menubar = new MenuBar();
		toolbar = new ToolBar();
		this.setTop(new VBox(menubar,toolbar));
		int iconsize = GUI_Constants.MENU_ICONSIZE;
		
		Menu file = new Menu("Files");
		
		MenuItem load = new MenuItem("Load...");
		load.setOnAction( evt -> GUIController.loadFile());
		load.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
		load.setGraphic(new ImageView(new Image("icons/file_open.png",iconsize,iconsize,true,true)));
		
		MenuItem loadbackup = new MenuItem("Load backup file...");
		loadbackup.setOnAction( evt -> GUIController.loadBackupFile());
		
		MenuItem print = new MenuItem("Print...");
		print.setOnAction( evt -> System.out.println("TODO"));
		print.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		print.setGraphic(new ImageView(new Image("icons/print.png",iconsize,iconsize,true,true)));
		
		MenuItem save = new MenuItem("Save");
		save.setOnAction( evt -> {
			if(PersistenceController.getFileURL()== null) GUIController.saveFileAs();
			else GUIController.saveFile();
		});
		save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		save.setGraphic(new ImageView(new Image("icons/save.png",iconsize,iconsize,true,true)));
		
		MenuItem saveAs = new MenuItem("Save as...");
		saveAs.setOnAction( evt -> GUIController.saveFileAs());
		
		MenuItem close = new MenuItem("Close");
		close.setOnAction( evt -> {
			GUIController.exitApp();
		});
		close.setGraphic(new ImageView(new Image("icons/exit.png",iconsize,iconsize,true,true)));
		
		file.getItems().addAll( load, loadbackup, print, save, saveAs, close);
		file.setOnShowing( evt -> save.setDisable(Entry.types().isEmpty() && PersistenceController.getFileURL()!= null));
		
		
		
		Menu actions = new Menu("Actions");
		
		MenuItem addtab = new MenuItem("Add new Tab");
		addtab.setOnAction( evt -> GUIController.addNewTab());
		addtab.setGraphic(new ImageView(new Image("icons/tab_add.png",iconsize,iconsize,true,true)));
		
		MenuItem addentry = new MenuItem("Create New Entry");
		addentry.setOnAction( evt -> {
			Entry e = GUIController.createEntry();
			GUIController.addEntry(e);
		});
		addentry.setGraphic(new ImageView(new Image("icons/entry_add.png",iconsize,iconsize,true,true)));
		addentry.setDisable(true);
		
		MenuItem addList = new MenuItem("Add extern list(s)...");
		addList.setOnAction( evt -> GUIController.addExternFile());
		addList.setGraphic(new ImageView(new Image("icons/add_folder.png",iconsize,iconsize,true,true)));
		addList.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		
		actions.getItems().addAll(addtab, addentry, addList);
		actions.setOnShowing( evt -> addentry.setDisable(Entry.types().isEmpty()));
		
		
		
		Menu options = new Menu("Options");
		
		MenuItem settings = new MenuItem("Settngs");
		settings.setOnAction( evt -> GUIController.openSettings());
		settings.setGraphic(new ImageView(new Image("icons/settings.png",iconsize,iconsize,true,true)));
		
		options.getItems().add(settings);
		
		
		
		Menu about = new Menu("About");
		
		MenuItem info = new MenuItem("About this Application");
		info.setOnAction( evt -> showInfo());
		info.setGraphic(new ImageView(new Image("icons/info.png",iconsize,iconsize,true,true)));
		about.getItems().add(info);
		
		searchbox = new SearchField(GUIController::filter);
		searchbox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(searchbox, Priority.ALWAYS);
		sortbox = new EntrySortBox(GUIController::sort);
		sortbox.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(sortbox, Priority.ALWAYS);
		
		menubar.getMenus().addAll(file,actions,options,about);
		
		toolbar.getItems().addAll(sortbox,searchbox);
		
		
	}
	
	public void createTabs() {
		tabpane = new TabPane();
		this.setCenter(tabpane);
		
		tabs = new ArrayList<ListTab>();
		for(String tmp : Entry.types()){
			tabs.add(new ListTab(tmp));
		}
		
		Tab t = new Tab();
		Label b = new Label("add new tab");
		t.setGraphic(b);
		t.setClosable(false);
		b.setOnMouseClicked( evt -> {
			GUIController.addNewTab();
		});
		tabpane.getTabs().add(t);
		tabpane.getTabs().addAll(tabs);
			
	}
	
	public void rebuild(){
		this.createMenu();
		this.createTabs();
	}

	public void addNewTab(ListTab newtab){
		tabs.add(newtab);
		tabpane.getTabs().add(newtab);
	}
	
	public void deleteTab(ListTab tab){
		this.getTabs().remove(tab);
		this.tabpane.getTabs().remove(tab);
	}
	
	public ListTab getTab(String type){
		for(ListTab t : this.tabs){
			if( t.getType().equals(type)){
				return t;
			}
		}
		return null;
	}
	
	public ArrayList<ListTab> getTabs(){
		return tabs;
	}
	
	public static void showInfo(){
		new AboutDialog().show();
	}

	public TabPane getTabpane() {
		return tabpane;
	}

	public SearchField getSearchBox() {
		return searchbox;
	}

	public EntrySortBox getSortBox() {
		return sortbox;
	}
}
