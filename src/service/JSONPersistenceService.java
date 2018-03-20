package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Entry;

public class JSONPersistenceService implements PersistenceService {

	@Override
	public List<Entry> load(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(String path) {
		JSONArray liste = new JSONArray();
		data.forEach( (type,list) -> list.forEach(e -> {
			JSONObject obj = new JSONObject();
			obj.put("Type", e.getType());
			obj.put("Name", e.getTitle());
			obj.put("Rating", e.getRating().ordinal());
			obj.put("Description", e.getDescription());
			liste.put(obj);
		}));
		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("jsonliste.json")) {
			liste.write(file);
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + liste);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}

	@Override
	public String getType() {
		return "json";
	}

}
