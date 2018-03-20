package views;

import java.util.Arrays;

import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class StarBox extends HBox{
	
	private int stars;
	private int currentStarIndex;
	private ImageView[] starImages;
	private String[] tooltips;

	public StarBox(String... starTooltips){
		super(5);
		tooltips = starTooltips;
		stars = starTooltips.length;
		starImages = new ImageView[stars];
		buildStars(0);
		
	}
	
	private void buildStars(int s){
		this.getChildren().clear();
		for(int i =0 ; i< stars ; i++){
			starImages[i] = new ImageView(new Image( i <= s ? "icons/star_full.png" : "icons/star_empty.png", GUI_Constants.ICONSIZE, GUI_Constants.ICONSIZE,true,true));
			Tooltip.install(starImages[i], new Tooltip(tooltips[i]));
			this.getChildren().add(starImages[i]);
		}
	}
	
	public int getCurrentStars(){
		return this.currentStarIndex;
	}

	public void setStars(int starindex) {
		if(starindex <0 || starindex > stars-1){
			throw new ArrayIndexOutOfBoundsException("stars out of bounds\nMaxIndex: "+(stars-1) + "\tYour Index: " + starindex);
		}
		currentStarIndex = starindex;
		buildStars(starindex);
		
	}

	public void activateGlow(boolean b) {
		Arrays.stream(starImages).forEach( im ->{ 
			im.setEffect( b ? new DropShadow(5,3,3,Color.YELLOW) : null);
			im.setOnMouseEntered( evt ->{
				im.setEffect( b ? new DropShadow(5,0,0,Color.BLACK) : null);
			});
			im.setOnMouseExited( evt ->{
				im.setEffect( b ? new DropShadow(5,3,3,Color.YELLOW) : null);
			});
			im.setOnMouseClicked( evt ->{
				for(int x = 0 ; x < stars ; ++x){
					if(im == starImages[x]) this.setStars(x);
					this.activateGlow(true);
				}
			});
		});
	}
}
