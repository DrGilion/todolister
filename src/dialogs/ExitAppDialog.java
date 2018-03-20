package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class ExitAppDialog extends Alert{

	private boolean b;
	
	ButtonType confirm_save;
	ButtonType confirm_leave;
	ButtonType cancel;
	
	public ExitAppDialog(boolean b){
		super(AlertType.CONFIRMATION);
		this.initStyle(StageStyle.UTILITY);
		this.b = b;
		this.setTitle("Exiting Application");
		this.setHeaderText(null);
		this.getDialogPane().setPrefSize(600, 140);
		this.getDialogPane().getStylesheets().add("stylesheets/dialog.css");
		if(b){
			this.setContentText("You are about to exit this application. Do you want to save your recent changes?");
			confirm_save = new ButtonType("Yes,save recent changes");
			confirm_leave = new ButtonType("No,don't save and exit");
			cancel = new ButtonType("Don't exit");
			this.getButtonTypes().setAll(confirm_save, confirm_leave, cancel);
		}else{
			this.setContentText("You are about to exit this application without saving your work. Do you want to save your progress?");
		}
	}
	
	public boolean isOK(){
		return !b && this.getResult() == ButtonType.OK;
	}
	
	public boolean isSaveConfirm(){
		return b && this.getResult() == confirm_save;
	}
	
	public boolean isLeaveConfirm(){
		return b && this.getResult() == confirm_leave;
	}
	
	public boolean isCancel(){
		return b && this.getResult() == cancel;
	}
}
