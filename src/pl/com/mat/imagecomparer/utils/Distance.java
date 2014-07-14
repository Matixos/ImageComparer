package pl.com.mat.imagecomparer.utils;

public class Distance implements Comparable<Distance> {
	
	private Point point1;
	private Point point2;
	private double distance;
	
	public Distance(Point p1 , Point p2){
		point1 = p1;
		point2 = p2;
		calculateDistance();
	}
	
	@Override
	public int compareTo(Distance o) {
		return new Double(distance).compareTo(o.getDistance());
	}
	
	public void calculateDistance() {
		distance = (((double)point1.getX()) - ((double)point2.getX())) * (((double)point1.getX()) - ((double)point2.getX()))
					+
					((point1.getY()) - ((double)point2.getY())) * (((double)point1.getY()) - ((double)point2.getY()));
	}
	
	public double getDistance() {
		return distance;
	}
	
	public Point getFirst(){
		return point1;
	}
	
	public Point getSecond(){
		return point2;
	}

}