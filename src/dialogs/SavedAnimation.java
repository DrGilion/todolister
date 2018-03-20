package dialogs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SavedAnimation extends Alert{

	private SavedAnimation(){
		super(AlertType.INFORMATION);
		this.initStyle(StageStyle.UTILITY);
		this.setContentText("Saved!");
	}
	
	public static void play(){
		SavedAnimation dialog = new SavedAnimation();
		dialog.show();
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), evt -> dialog.hide()));
		timeline.play();
	}
}
