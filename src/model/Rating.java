package model;

public enum Rating {
	not_rated,aweful,weak,bad,mediocre,ok,good,very_good,excellent,masterpiece;
	
	public String toString(){
		return super.toString();
	}
	
	public String getUIString(){
		return super.toString().replace("_"," ").toUpperCase();
	}
	
	public static Rating toRating(String s){
		return Rating.valueOf(s.toLowerCase().replace(" ","_"));
	}
	
	
	
}
