package pl.com.mat.imagecomparer.utils;

import java.util.ArrayList;

public class Point {
	
	private int x;
	private int y;
	
	private double normal_x;         // normalized (0,1)
	private double normal_y;
	
	private ArrayList <Integer> features;	// cechy
	
	private Point neighbor;
	
	public Point(int x, int y, ArrayList<Integer> features) {
		this.x = x;
		this.y = y;
		this.features = features;
	}
	
	public void normalize(int width , int height) {
		normal_x = ((double) x) / ((double) width);
		normal_y = ((double) y) / ((double) height);
	}
	
	@Override
	public int hashCode() {
		return x + y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Point){
			Point p = (Point)o;
			return x == p.getX() && y == p.getY();
			
		}else{
			return false;
		}
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getNormal_X() {
		return normal_x;
	}


	public double getNormal_Y() {
		return normal_y;
	}

	public ArrayList<Integer> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Integer> features) {
		this.features = features;
	}

	public Point getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(Point neighbor) {
		this.neighbor = neighbor;
	}
	
}