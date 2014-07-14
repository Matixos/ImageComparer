package pl.com.mat.imagecomparer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.imgscalr.Scalr;

import pl.com.mat.imagecomparer.utils.Pair;
import pl.com.mat.imagecomparer.utils.Point;


public class ViewPanel extends JPanel {

	private static final long serialVersionUID = -3509189409841025334L;

	private BufferedImage first;
	private BufferedImage second;

	private ArrayList<Pair> toDraw;
	private ArrayList<Point> pointsOfFirst;
	private ArrayList<Point> pointsOfSecond;
	private ArrayList<Pair> mutuallyNeighbors;

	public ViewPanel() {
		setBackground(Color.WHITE);
	}

	public void visualize(ArrayList<Pair> pairsToDraw, ArrayList<Point> pointsOfFirst, ArrayList<Point> pointsOfSecond,
			ArrayList<Pair> mutuallyNeighbors) {
		this.toDraw = pairsToDraw;
		this.pointsOfFirst = pointsOfFirst;
		this.pointsOfSecond = pointsOfSecond;
		this.mutuallyNeighbors = mutuallyNeighbors;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		int heightOfFirst = 0;
		int heightOfSecond = 0;
		
		if (first != null) {
			BufferedImage obraz_przeskalowany = Scalr.resize(first , Scalr.Mode.FIT_TO_WIDTH , 460 , (BufferedImageOp[])null);
			heightOfFirst = obraz_przeskalowany.getHeight();
			g2d.drawImage(obraz_przeskalowany , null , 0, 0);
		}
		
		g2d.drawRect(0, 0, 460, 610);
		
		if (second != null) {
			BufferedImage obraz_przeskalowany = Scalr.resize(second , Scalr.Mode.FIT_TO_WIDTH , 460 , (BufferedImageOp[])null);
			heightOfSecond = obraz_przeskalowany.getHeight();
			g2d.drawImage(obraz_przeskalowany , null , 510, 0);
		}	
			
		g2d.drawRect(510, 0, 460, 610);
		
		if (pointsOfFirst != null) {
			for (Point p: pointsOfFirst) {
				g2d.setColor(Color.RED);
					
					g2d.drawOval(
							(int)(p.getNormal_X() * 460) - 1
							,(int)(p.getNormal_Y() * heightOfFirst) - 1
							,2
							,2);
			}
		}
		
		if (pointsOfSecond != null) {
			
			for (Point p: pointsOfSecond) {
				g2d.setColor(Color.RED);
					
					g2d.drawOval(
							(int)(p.getNormal_X() * 460) - 1 + 510
							,(int)(p.getNormal_Y() * heightOfSecond) - 1
							,2
							,2);
			}
		}
		
		if (mutuallyNeighbors != null) {
			g2d.setColor(Color.BLUE);
			
			for (Pair para: mutuallyNeighbors) {
				Point p1 = para.getFirst();
				Point p2 = para.getSecond();
			
				int x1 = (int)(p1.getNormal_X() * 460);
				int y1 = (int)(p1.getNormal_Y() * heightOfFirst);
			
				int x2 = (int)(p2.getNormal_X() * 460) + 510;
				int y2 = (int)(p2.getNormal_Y() * heightOfSecond);
			
				g2d.drawOval(x1 - 1, y1 - 1, 2, 2);
				g2d.drawOval(x2 - 1, y2 - 1, 2, 2);
			}
		}
		
		if (toDraw != null) { 
			g2d.setColor(Color.YELLOW);
			
			for (Pair para: toDraw) {
				Point p1 = para.getFirst();
				Point p2 = para.getSecond();
			
				int x1 = (int)(p1.getNormal_X() * 460);
				int y1 = (int)(p1.getNormal_Y() * heightOfFirst);
			
				int x2 = (int)(p2.getNormal_X() * 460) + 510;
				int y2 = (int)(p2.getNormal_Y() * heightOfSecond);
			
				g2d.drawLine(x1, y1, x2, y2);
			}
		}
	}

	public BufferedImage getFirst() {
		return first;
	}

	public BufferedImage getSecond() {
		return second;
	}

	public void loadFirstImage(File file) {
		try {
			first = ImageIO.read(file);
		} catch (IOException e) {}
		repaint();
	}

	public void loadSecondImage(File file) {
		try {
			second = ImageIO.read(file);
		} catch (IOException e) {}
		repaint();
	}
	
	public void clear() {
		first = null;
		second = null;
		pointsOfFirst = null;
		pointsOfSecond = null;
		mutuallyNeighbors = null;
		toDraw = null;
		repaint();
	}

}