package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ConfirmationButtonSet extends HBox{

	private Button editbutton;
	private Button confirmbutton;
	private Button cancelbutton;
	
	public ConfirmationButtonSet(String editTooltip,String confirmTooltip, String cancelTooltip){
		super(15);
		this.getStyleClass().add("hbox");
		int iconsize = 32;
		EventHandler<ActionEvent> event = evt ->{
			switchButtons();
		};
		editbutton = new Button("", new ImageView(new Image("icons/edit.png",iconsize,iconsize,true,true)));
		editbutton.setOnAction(event);
		editbutton.setTooltip(new Tooltip(editTooltip));
		
		confirmbutton = new Button("", new ImageView(new Image("icons/Ok.png",iconsize,iconsize,true,true)));
		confirmbutton.setOnAction(event);
		confirmbutton.setTooltip(new Tooltip(confirmTooltip));

		cancelbutton = new Button("", new ImageView(new Image("icons/Cancel.png",iconsize,iconsize,true,true)));
		cancelbutton.setOnAction(event);
		confirmbutton.setTooltip(new Tooltip(cancelTooltip));

		this.getChildren().add(editbutton);
	}
	
	public void switchButtons(){
		if(this.getChildren().size() == 1){
			this.getChildren().clear();
			this.getChildren().addAll(confirmbutton,cancelbutton);
		}else{
			this.getChildren().clear();
			this.getChildren().add(editbutton);
		}
	}

	public Button getEditbutton() {
		return editbutton;
	}


	public Button getConfirmbutton() {
		return confirmbutton;
	}


	public Button getCancelbutton() {
		return cancelbutton;
	}
}
