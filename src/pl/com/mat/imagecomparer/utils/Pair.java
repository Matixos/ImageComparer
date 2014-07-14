package pl.com.mat.imagecomparer.utils;

public class Pair {
	
	private Point first;
	private Point second;
	
	public Pair(Point first , Point second){
		this.first = first;
		this.second = second;
	}

	public Point getFirst() {
		return first;
	}

	public void setFirst(Point first) {
		this.first = first;
	}

	public Point getSecond() {
		return second;
	}

	public void setSecond(Point second) {
		this.second = second;
	}

}