package pl.com.mat.imagecomparer.utils;

import java.util.ArrayList;

public class ImageReprezentation {
	
	private ArrayList <Point> points;
	private int height;
	private int width;
	
	public void setReprezentation(ArrayList<Point> points, int width, int height) {
		this.points = points;
		this.width = width;
		this.height = height;
		
		normalize();
	}
	
	public boolean isLoaded(){
		return points != null;
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}
	
	public void normalize(){
		for(Point p: points){
			p.normalize(width, height);
		}
	}

}