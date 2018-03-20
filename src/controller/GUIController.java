package controller;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;

import dialogs.DeleteEntryDialog;
import dialogs.DeleteTabDialog;
import dialogs.ExitAppDialog;
import dialogs.NewEntryDialog;
import dialogs.NewTabDialog;
import dialogs.RenameTabDialog;
import dialogs.SavedAnimation;
import dialogs.SettingsDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import model.Entry;
import model.Rating;
import views.ListTab;
import views.MainFrame;
import views.Program;

public class GUIController {

	private GUIController() {
	}

	private static MainFrame mainframe;

	public static void initiate(MainFrame frame) {
		mainframe = frame;
		PersistenceController.loadSettings();
	}

	public static void changeEntryName(Entry entry, String newname) {
		if( newname.equals("")){
			new Alert(Alert.AlertType.ERROR, "Name is empty!").show();
			return;
		}
		for(Entry tmp : Entry.EntriesFor(entry.getType())){
			if(newname.equals(tmp.getTitle()) && entry != tmp){
				new Alert(Alert.AlertType.ERROR, "entry name already exists!").show();
				return ;
			}
		}
		entry.setTitle(newname);
		mainframe.getTab(entry.getType()).getTypeAccordion(entry).refresh();
		mainframe.getTab(entry.getType()).updateInfoPanel(entry);
		GUIController.rewriteTitle(true);
	
	}

	public static void changeEntryRating(Entry entry, Rating newrating) {
		if(newrating == null){
			new Alert(Alert.AlertType.ERROR, "Invalid Rating!").show();
			return;
		}
		entry.setRating(newrating);
		mainframe.getTab(entry.getType()).getTypeAccordion(entry).refresh();
		mainframe.getTab(entry.getType()).updateInfoPanel(entry);
	}

	public static void changeEntryDescription(Entry entry,String newtext) {
		entry.setDescription(newtext);
		mainframe.getTab(entry.getType()).getTypeAccordion(entry).refresh();
		mainframe.getTab(entry.getType()).updateInfoPanel(entry);
		GUIController.rewriteTitle(true);
	}
	
	public static void changeEntryLink(){
		
	}

	public static void changeEntryType(Entry entry, String newtype) {
		if (newtype != null && !newtype.equals("")) {
			ListTab t = mainframe.getTab(entry.getType());
			if (t != null) {
				for(Entry tmp : Entry.EntriesFor(newtype)){
					if(entry.getTitle().equals(tmp.getTitle()) && entry != tmp){
						new Alert(Alert.AlertType.ERROR, "entry name already exists in the other category!").show();
						return;
					}
				}
				t.removeEntry(entry);
				Entry.removeEntryFrom(entry, entry.getType());
			}
			entry.setType(newtype);
			addEntry(entry);
			t = mainframe.getTab(entry.getType());
			mainframe.getTabpane().getSelectionModel().select(t);
			t.updateInfoPanel(entry);
			GUIController.rewriteTitle(true);
		} else {
			new Alert(Alert.AlertType.ERROR, "No category chosen!").show();
		}
	}

	public static void changeEntryStatus(Entry entry) {
		ListTab t = mainframe.getTab(entry.getType());
		if (t != null) {
			t.swapEntry(entry);
			GUIController.rewriteTitle(true);
		}
	}

	public static boolean removeEntry(Entry entry) {
		Optional<ButtonType> result = new DeleteEntryDialog().showAndWait();
		if (result.isPresent() && result.get()== ButtonType.OK) {
			ListTab t = mainframe.getTab(entry.getType());
			if (t != null) {
				t.removeEntry(entry);
				boolean returnval = Entry.removeEntryFrom(entry, entry.getType());
				GUIController.rewriteTitle(returnval);
				return returnval;
			}
			return false;
		}
		return false;
	}

	public static void addNewTab() {
		Optional<String> result = new NewTabDialog().showAndWait();
		result.ifPresent(name -> {
			if ((!name.equals("")) && Entry.addNewCategory(name)) {
				ListTab newtab = new ListTab(name);
				mainframe.addNewTab(newtab);
				GUIController.rewriteTitle(true);
			} else {
				new Alert(Alert.AlertType.ERROR, "Category name is empty or already exists!").show();
			}
		});
	}

	public static void renameTab(ListTab tab) {
		Optional<String> result = new RenameTabDialog(tab.getType()).showAndWait();
		result.ifPresent(name -> {
			if (!name.equals("") && Entry.changeCategoryName(tab.getType(), result.get())) {
				tab.renameTab(result.get());
				GUIController.rewriteTitle(true);
			} else {
				new Alert(Alert.AlertType.ERROR, "Category name is empty or already exists!").show();
			}
		});
	}

	public static boolean deleteTab(ListTab tab) {
		DeleteTabDialog dialog = new DeleteTabDialog();
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && dialog.isConfirm()) {
			Entry.deleteCategory(tab.getType());
			mainframe.deleteTab(tab);
			GUIController.rewriteTitle(true);
			return true;
		}
		return false;
	}

	public static Entry createEntry() {
		Entry result = null;

		Optional<Entry> dialogresult = new NewEntryDialog().showAndWait();
		if (dialogresult.isPresent()) result = dialogresult.get();
		return result;
	}

	public static Entry createEntry(String type, boolean done) {
		Entry result = null;
		
		Optional<Entry> dialogresult = new NewEntryDialog(type, done).showAndWait();
		if (dialogresult.isPresent())
			result = dialogresult.get();
		return result;
	}

	public static boolean addEntry(Entry entry) {
		if (entry == null)
			return false;
		ListTab t = mainframe.getTab(entry.getType());
		if (t != null) {
			for(Entry tmp : Entry.EntriesFor(entry.getType())){
				if(entry.getTitle().equals(tmp.getTitle())){
					new Alert(Alert.AlertType.ERROR, "entry name already exists!").show();
					return false;
				}
			}
			t.getTypeAccordion(entry).addNewItem(entry);
			Entry.addEntryTo(entry, entry.getType());
			GUIController.rewriteTitle(true);
			return true;
		} else
			return false;
	}
	
	public static void filterAndSort(String input,Comparator<Entry> comparator){
		for(ListTab t : mainframe.getTabs()){
			t.updateListPanes(input, comparator);
		}
	}
	
	public static void FilterAndSort(){
		filterAndSort(mainframe.getSearchBox().getSearchString(), mainframe.getSortBox().getSortAlgorithm());
	}
	
	public static void filter(String search){
		filterAndSort(search, mainframe.getSortBox().getSortAlgorithm());
	}
	
	public static void sort(Comparator<Entry> comparator){
		filterAndSort(mainframe.getSearchBox().getSearchString(), comparator);
	}
	
	public static void openSettings(){
		SettingsDialog dialog = new SettingsDialog();
		Optional<Boolean> result = dialog.showAndWait();
		if(result.isPresent()){
			if(result.get().booleanValue() == true){
				SavedAnimation.play();
				GUIController.rebuildMainFrame();
				GUIController.rewriteTitle(true);
			}
		}
	}
	
	public static void rebuildMainFrame(){
		mainframe.rebuild();
	}
	
	public static void rewriteTitle(boolean change){
		PersistenceController.setChanged(change);
		Program.getStage().setTitle(change ? "To-Do-Lister "+Program.getVersion() +" *" : "To-Do-Lister "+Program.getVersion());
	}

	public static void loadFile() {
		FileChooser fileChooser = new FileChooser();
		if(PersistenceController.getDirectoryURL()!= null && new File(PersistenceController.getDirectoryURL()).exists())
			try {
				fileChooser.setInitialDirectory(new File(PersistenceController.getDirectoryURL()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("List files (*.list)", "*.list");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(Program.getStage());
		if (file != null) {
			PersistenceController.setFileURL(file.getAbsolutePath());
			PersistenceController.load(new File());
			mainframe.rebuild();
		}
	}

	public static void loadBackupFile() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Backup for list files (*.backup)", "*.backup");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(Program.getStage());
		if (file != null) {
			PersistenceController.readBackUpFile(file.getAbsolutePath());
			mainframe.rebuild();
		}
	}

	public static void saveFile() {
		PersistenceController.save(null);
		GUIController.rewriteTitle(false);
	}

	public static void saveFileAs() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("List files (*.list)", "*.list");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showSaveDialog(Program.getStage());
		if (file != null) {
			PersistenceController.setFileURL(file.getAbsolutePath());
			PersistenceController.save(null);
		}
	}
	
	public static void loadSettings(){
		//TODO
	}
	
	public static void saveSettings(){
		//TODO
	}
	
	public static void addExternFile(){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Backup files or list files(*.partlist, *.partbackup)", "*.partlist", "*.partbackup");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(Program.getStage());
		if (file != null) {
			if(file.getAbsolutePath().endsWith(".partlist")){
				PersistenceController.AddListFile(file);
			}
			if(file.getAbsolutePath().endsWith(".partbackup")){
				PersistenceController.AddBackupFile(file);
			}
			mainframe.rebuild();
		}
	}

	public static void exitApp() {
		ExitAppDialog dialog;
		//if file was loaded and changes have been made
		if (PersistenceController.getFileURL() != null && PersistenceController.isChanged()) {
			dialog = new ExitAppDialog(true);
			dialog.showAndWait();
				if (dialog.isSaveConfirm()) {
					GUIController.saveFile();
					System.exit(0);
				}
				if (dialog.isLeaveConfirm()) {
					System.exit(0);
				}
				if (dialog.isCancel()) {
					// nothing
				}
		} else {
			if (!Entry.getEntryMap().isEmpty()) {
				(dialog = new ExitAppDialog(false)).showAndWait();
				if (dialog.isOK()) {
					GUIController.saveFileAs();
				} else
					System.exit(0);
			} else {
				System.exit(0);
			}
		}
	}

}
