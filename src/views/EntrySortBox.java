package views;

import java.util.Comparator;
import java.util.function.Consumer;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import model.Entry;

public class EntrySortBox extends HBox{
	
	private RadioButton lexiDownButton;
	private RadioButton lexiUpButton;
	private RadioButton ratingDownButton;
	private RadioButton ratingUpButton;
	private ToggleGroup group;
	
	private Consumer<Comparator<Entry>> function;

	public EntrySortBox(Consumer<Comparator<Entry>> func){
		super();
		this.setSpacing(10);
		lexiDownButton = new RadioButton("Name(descending)");
		lexiUpButton = new RadioButton("name(ascending)");
		ratingDownButton = new RadioButton("Rating\n(descending)");
		ratingUpButton = new RadioButton("Rating\n(ascending)");
		group = new ToggleGroup();
		lexiDownButton.setToggleGroup(group);
		lexiUpButton.setToggleGroup(group);
		ratingDownButton.setToggleGroup(group);
		ratingUpButton.setToggleGroup(group);
		this.setSortAlgorithm(func);
		this.getChildren().addAll(new Label("sort by: "),lexiDownButton,lexiUpButton,ratingDownButton,ratingUpButton);
		lexiDownButton.setSelected(true);
	}
	
	public void setSortAlgorithm(Consumer<Comparator<Entry>> func){
		function = func;
		if(this.function != null){
			lexiDownButton.setOnAction( evt -> func.accept(this.lexiDownSort()));
			lexiUpButton.setOnAction( evt -> func.accept(this.lexiUpSort()));
			ratingDownButton.setOnAction( evt -> func.accept(this.ratingDownSort()));
			ratingUpButton.setOnAction( evt -> func.accept(this.ratingUpSort()));
		}
	}
	
	public Comparator<Entry> lexiDownSort() {
		return (Entry e1, Entry e2) -> e1.getTitle().compareToIgnoreCase(e2.getTitle());
	}
	
	public Comparator<Entry> lexiUpSort() {
		return (Entry e1, Entry e2) -> e2.getTitle().compareToIgnoreCase(e1.getTitle());
	}

	public Comparator<Entry> ratingDownSort() {
		return (Entry e1, Entry e2) -> e2.getRating().compareTo(e1.getRating());
	}
	
	public Comparator<Entry> ratingUpSort() {
		return (Entry e1, Entry e2) -> e1.getRating().compareTo(e2.getRating());
	}
	
	public Comparator<Entry> getSortAlgorithm(){
		if(lexiDownButton.isSelected()) return lexiDownSort();
		if(lexiUpButton.isSelected()) return lexiUpSort();
		if(ratingDownButton.isSelected()) return ratingDownSort();
		if(ratingUpButton.isSelected()) return ratingUpSort();
		return null;
	}
	 
}
