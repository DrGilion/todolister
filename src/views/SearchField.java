package views;

import java.util.function.Consumer;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchField extends HBox{

	private TextField field;
	private Consumer<String> function;
	
	public SearchField(Consumer<String> func){
		super();
		field = new TextField();
		field.setPromptText("Search...");
		setSearchAlgorithm(func);
		this.getChildren().addAll(new Label("Search: "),field);
	}
	
	
	public void setSearchAlgorithm(Consumer<String> func){
		function = func;
		if(this.function != null) field.textProperty().addListener( (observable,oldvalue,newValue) -> function.accept(newValue));
	}
	
	public String getSearchString(){
		return field.getText() == null ? "" : field.getText();
	}
}
