package service;

import java.io.File;
import java.util.List;

import model.Entry;

public interface PersistenceService {
	public String getType();
	public abstract List<Entry> load(File file);
	public abstract boolean save(String path);
	
}
