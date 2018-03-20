package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Entry;

public class LegacyPersistenceService implements PersistenceService {

	@Override
	public List<Entry> load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			HashMap<String, ArrayList<data.Entry>> data = (HashMap<String, ArrayList<data.Entry>>) ois.readObject();
			ois.close();
			fis.close();
			ArrayList<Entry> result = new ArrayList<>();
			for(Map.Entry<String,ArrayList<data.Entry>> e : data.entrySet()){
				for( data.Entry e2 : e.getValue()){
					Entry entry = new Entry(e2.getTitle(),e2.getType());
					entry.addProperty("rating", new SimpleIntegerProperty(e2.getRating().ordinal()));
					entry.addProperty("experienced", new SimpleBooleanProperty(e2.hasExperienced()));
					entry.addProperty("description", new SimpleStringProperty(e2.getDescription()));
					
				}
			}
			return result;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean save(String path) {
		
		// single file
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data.Entry.getEntryMap());
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// multiple files
		String parent = new File(path).getParent() + File.separator;
		for (Map.Entry<String, ArrayList<data.Entry>> e : data.Entry.getEntryMap().entrySet()) {
			try {
				FileOutputStream fos = new FileOutputStream(parent + e.getKey() + ".partlist");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(e.getValue());
				oos.close();
				fos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public String getType() {
		return "legacy";
	}

}
