package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import model.types.EntryType;

public class Entry implements Serializable {

	private static final long serialVersionUID = 1L;
	/*public static final String nameProperty = "name";
	public static final String typeProperty = "type";
	public static final String tagsProperty = "tags";*/
	/*
	 * private String title; private String description; private int rating;
	 * private String type; private boolean experienced;
	 */
	
	
	private String name;
	private EntryType type;
	private Set<String> tags;
	private Map<String, Property<?>> properties;

	/*
	 * private static HashMap<String,ArrayList<Entry>> entryMap;
	 * 
	 * static{ entryMap = new HashMap<String, ArrayList<Entry>>(); }
	 */

	public Entry(String name, EntryType type, String... tags) {
		properties = new HashMap<String, Property<?>>();
		this.setName(name);
		this.setType(type);
		this.setTags(tags);
	}

	/*public Entry() {
		this("Unnamed" + new Random().nextInt(Integer.MAX_VALUE), "untyped");

	}*/

	/*
	 * public Entry(String title, String description, int rating, String type,
	 * boolean exp) { super(); setTitle(title); setDescription(description);
	 * setRating(rating); setType(type); setExperienced(exp); }
	 */

	/*public static Set<String> types() {
		return properties.keySet();
	}

	public static ArrayList<Entry> EntriesFor(String input) {
		return properties.get(input);
	}

	public static boolean addEntryTo(Entry entry, String type) {
		for (String s : entryMap.keySet()) {
			if (s.equals(type)) {
				boolean success = entryMap.get(s).add(entry);
				return success;
			}
		}
		return false;
	}

	public static boolean removeEntryFrom(Entry entry, String type) {
		for (String s : entryMap.keySet()) {
			if (s.equals(type)) {
				boolean success = entryMap.get(s).remove(entry);
				return success;
			}
		}
		return false;
	}

	public static boolean changeCategory(String oldname, String newname) {
		if (entryMap.containsKey(newname))
			return false;
		ArrayList<Entry> value = entryMap.remove(oldname);
		value.stream().forEach(e -> e.setType(newname));
		entryMap.put(newname, value);
		return true;
	}*/
	
//+++++++++++++++++++++++++++++++++Properties+++++++++++++++++++++++++++++++++

	public void addProperty(String name, Property<?> prop) {
		properties.put(name, prop);
	}
	
	public boolean removeProperty(String propName) {
		if(propName.equals(nameProperty) || propName.equals(typeProperty) || propName.equals(tagsProperty)){
			return false;
		}else{
			return properties.remove(propName) != null;
		}
	}

	public boolean hasProperty(String propName) {
		return properties.containsKey(propName);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTotalPropertyCount(){
		return properties.size();
	}

//+++++++++++++++++++++++++++++++++Name+++++++++++++++++++++++++++++++++
	
	public String getName() {
		return (String) properties.get(nameProperty).getValue();
	}

	public void setName(String name) throws IllegalArgumentException {
		if (name == null || name.equals("")) throw new IllegalArgumentException("no title given");
		else properties.put(nameProperty, new SimpleStringProperty(name));
	}
	
//+++++++++++++++++++++++++++++++++Type+++++++++++++++++++++++++++++++++
	
	public String getType() {
		return (String) properties.get(typeProperty).getValue();
	}

	public void setType(String type) throws IllegalArgumentException {
		if (type == null || type.equals("")) throw new IllegalArgumentException("no type given");
		else properties.put(typeProperty, new SimpleStringProperty(type));
	}
	
//+++++++++++++++++++++++++++++++++Tags+++++++++++++++++++++++++++++++++

	@SuppressWarnings("unchecked")
	public Set<String> getTags() {
		return ((SimpleSetProperty<String>) properties.get(tagsProperty)).get();
	}
	
	@SuppressWarnings("unchecked")
	public String getTagsString(){
		return ((SimpleSetProperty<String>) properties.get(tagsProperty)).get().stream().collect(Collectors.joining(","));
	}

	public void setTags(String... tags) {
		if (tags != null)
			properties.put(tagsProperty, new SimpleSetProperty<String>(
					FXCollections.observableSet(Arrays.stream(tags).filter( tag -> tag != null && !tag.isEmpty()).toArray(String[]::new))));
	}

	@SuppressWarnings("unchecked")
	public void addTag(String tag) {
		((SimpleSetProperty<String>) properties.get(tagsProperty)).add(tag);
	}
	
	public void addTags(String... tags){
		for(String tmp : tags){
			this.addTag(tmp);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean removeTag(String tag) {
		return ((SimpleSetProperty<String>) properties.get(tagsProperty)).remove(tag);
	}
	
	public void removeTags(String... tags){
		for(String tmp : tags){
			this.removeTag(tmp);
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasTag(String tag){
		return ((SimpleSetProperty<String>) properties.get(tagsProperty)).contains(tag);
	}

	public String toString() {
		StringBuilder info = new StringBuilder();
		properties.entrySet().stream().sorted((left, right) -> left.getKey().equals(nameProperty) ? 1 : -1)
				.forEach(entry -> info.append(entry.getKey() + ": " + entry.getValue().toString() + "\n"));
		return info.toString();
	}

	public boolean equals(Object other) {
		Entry o = (Entry) other;
		if (!this.properties.keySet().equals(o.properties.keySet()))
			return false;
		for (String key : this.properties.keySet()) {
			if (!this.properties.get(key).equals(o.properties.get(key)))
				return false;
		}
		return true;
	}

	public boolean equalsNameType(Entry other) {
		return this.properties.get(nameProperty).equals(other.properties.get(nameProperty))
				&& this.properties.get(typeProperty).equals(other.properties.get(typeProperty));
	}
}
