package model.types;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.Property;

public abstract class EntryType {
	protected Map<String,Class<?>> attributeMap;
	
	public EntryType(){
		attributeMap = new HashMap<>();
	}
	
	public void addProperty(String name, Class<?> prop) {
		attributeMap.put(name, prop);
	}
	
	public boolean removeProperty(String propName) {
		return attributeMap.remove(propName) != null;
	}

	public boolean hasProperty(String propName) {
		return attributeMap.containsKey(propName);
	}
	
	public int getPropertyCount(){
		return attributeMap.size();
	}
}
