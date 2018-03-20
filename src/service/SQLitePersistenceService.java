package service;

import java.io.File;

public class SQLitePersistenceService implements PersistenceService {

	@Override
	public boolean load(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean save(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getType() {
		return "sqlite";
	}

}
