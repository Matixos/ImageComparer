package pl.com.mat.imagecomparer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

import pl.com.mat.imagecomparer.utils.Calculations;
import pl.com.mat.imagecomparer.utils.Distance;
import pl.com.mat.imagecomparer.utils.ImageReprezentation;
import pl.com.mat.imagecomparer.utils.Pair;
import pl.com.mat.imagecomparer.utils.Point;

import Jama.Matrix;

public class Model {
	
	private static final int NEIGHBORHOOD_CNT = 50;				 // licznoœæ s¹siedztwa
	private static final int COHERENCY_THRESHOLD = 20;          // próg spójnoœci
	private static final int RANSAC_ITERATIONS = 200;
	private static final int MAX_ERROR = 20;
	
	private ViewManager win;
	
	private ImageReprezentation firstImage;
	private ImageReprezentation secondImage;
	private ArrayList<Pair> neighborhoodOfFirst;
	private ArrayList<Pair> neighborhoodOfSecond;
	private ArrayList<Pair> mutualNeighbors;
	private ArrayList<Pair> coherentNeighbors;
	private ArrayList<Pair> ransacPairs;
	
	public Model(ViewManager win) {
		this.win = win;
		
		firstImage = new ImageReprezentation();
		secondImage = new ImageReprezentation();
		neighborhoodOfFirst = new ArrayList<Pair>();
		neighborhoodOfSecond = new ArrayList<Pair>();
		mutualNeighbors = new ArrayList<Pair>();
		coherentNeighbors = new ArrayList<Pair>();
		ransacPairs = new ArrayList<Pair>();
	}
	
	public void loadFirstImage(File file , int width, int height){
		firstImage.setReprezentation(loadPoints(file) , width , height);
	}
	
	public void loadSecondImage(File file , int width, int height){
		secondImage.setReprezentation(loadPoints(file) , width , height);
	}
	
	private ArrayList<Point> loadPoints(File file) {
		Set<Point> result = new HashSet<Point>();
		
		File newFile = new File(file.getPath() + ".haraff.sift");

		try {
			Scanner sc = new Scanner(newFile);
			sc.next(); // skip 128

			int actualimageKeyPoints = Integer.parseInt(sc.next()); // number of points to load
			
			JOptionPane.showMessageDialog(win, "Iloœæ punktów kluczowych: " + actualimageKeyPoints,
					"Info",
					JOptionPane.INFORMATION_MESSAGE);

			for (int i = 0; i < actualimageKeyPoints; i++) {
				int x = new Double(sc.next()).intValue();
				int y = new Double(sc.next()).intValue();

				sc.next(); // skip A B C params
				sc.next();
				sc.next();

				ArrayList<Integer> tmp_features = new ArrayList<Integer>();
				for (int j = 0; j < 128; j++) {
					tmp_features.add(new Integer(sc.next()));
				}
				result.add(new Point(x, y, tmp_features));
			}

			sc.close();
		} catch (FileNotFoundException e) {}

		ArrayList<Point> listResult = new ArrayList<Point>();
		listResult.addAll(result);

		return listResult;
	}
	
	public void determineNeighborsOfFirst(){
		neighborhoodOfFirst.clear();
		
		for (int i = 0 ; i < firstImage.getPoints().size() ; i++) {
			Point first = firstImage.getPoints().get(i);
			Point second = secondImage.getPoints().get(0);
			
			int distance = Calculations.getDistanceBetweenVectors(first.getFeatures(), second.getFeatures());
			
			for (int j = 1 ; j < secondImage.getPoints().size() ; j++) {
				Point tmp_sec = secondImage.getPoints().get(j);
				int tmp_dist = Calculations.getDistanceBetweenVectors(first.getFeatures(), tmp_sec.getFeatures());
					
				if(tmp_dist < distance){
					distance = tmp_dist;
					second = tmp_sec;
				}
			}
			
			first.setNeighbor(second);
			neighborhoodOfFirst.add(new Pair(first , second));
		}
	}
	
	public void determineNeighborsOfSecond(){
		neighborhoodOfSecond.clear();
		
		for (int i = 0 ; i < secondImage.getPoints().size() ; i++) {
			Point first = secondImage.getPoints().get(i);
			Point second = firstImage.getPoints().get(0);
			
			int dist = Calculations.getDistanceBetweenVectors(first.getFeatures(), second.getFeatures());
			
			for (int j = 1 ; j < firstImage.getPoints().size() ; j++) {
				Point tmp_sec = firstImage.getPoints().get(j);
				int tmp_dist = Calculations.getDistanceBetweenVectors(first.getFeatures(), tmp_sec.getFeatures());
				
				if (tmp_dist < dist) {
					dist = tmp_dist;
					second = tmp_sec;
				}
			}
			
			first.setNeighbor(second);
			neighborhoodOfSecond.add(new Pair(first , second));
		}
	}
	
	public void determineMutualNeighbors(){
		mutualNeighbors.clear();
		
		for (Pair p1: neighborhoodOfFirst) {
			Point point1 = p1.getFirst();
			Point point2 = p1.getSecond();
			
			for (Pair p2: neighborhoodOfSecond) {
				Point point3 = p2.getFirst();
				Point point4 = p2.getSecond();
				
				if (point2.equals(point3)) {	
					if (point1.equals(point4)) {
						mutualNeighbors.add(new Pair(point1 , point2));
					}
				}
			}
		}
	}
	
	public void determineCoherentNeighbors(){
		coherentNeighbors.clear();
		
		for(int i = 0 ; i < mutualNeighbors.size() ; i++){  // iterate through every pair of mutual points
			Pair pair = mutualNeighbors.get(i);
			Point point1 = pair.getFirst();
			Point point2 = pair.getSecond();
			
			ArrayList<Point> allOfFirst = new ArrayList<Point>();   // wyci¹gniêcie punktów okolicznych
			ArrayList<Point> allOfSecond = new ArrayList<Point>();
			for (Pair p: mutualNeighbors) {
				allOfFirst.add(p.getFirst());
				allOfSecond.add(p.getSecond());
			}
			
			ArrayList<Distance> areaOfFirst = new ArrayList<Distance>();  // get each nbh
			ArrayList<Distance> areaOfSecond = new ArrayList<Distance>();
			for (int j = 0 ; j < mutualNeighbors.size() ; j++) {
				areaOfFirst.add(new Distance(point1 , allOfFirst.get(j)));
				areaOfSecond.add(new Distance(point2 , allOfSecond.get(j)));
				
			}
			
			Collections.sort(areaOfFirst);     // sort values INC
			Collections.sort(areaOfSecond);
			
			ArrayList <Point> closePointsOfFirst = new ArrayList<Point>();   // take first 50 of each
			ArrayList <Point> closePointsOfSecond = new ArrayList<Point>();
			
			int k = 0 ;
			while (closePointsOfFirst.size() <= NEIGHBORHOOD_CNT || closePointsOfSecond.size() <= NEIGHBORHOOD_CNT) {
				Distance close1 = areaOfFirst.get(k);
				if(close1.getDistance() > 0 && closePointsOfFirst.size() <= NEIGHBORHOOD_CNT)
					closePointsOfFirst.add(close1.getSecond());
				
				Distance close2 = areaOfSecond.get(k);
				if(close2.getDistance() > 0 && closePointsOfSecond.size() <= NEIGHBORHOOD_CNT)
					closePointsOfSecond.add(close2.getSecond());
				
				k++;
			}
			
			int coherent = 0;
			for(Point p1: closePointsOfFirst){
				
				for (Pair pa: mutualNeighbors) {
					Point paired = null;
					
					if (p1 == pa.getFirst()) {
						paired = pa.getSecond();
					} else if(p1 == pa.getSecond()) {
						paired = pa.getFirst();
					}	
					
					
					if (paired != null) {
						for (Point p2: closePointsOfSecond) {
							if(paired == p2) {
								coherent++;
							}
						}
					}
				}				
			}
			
			if (coherent >= COHERENCY_THRESHOLD) {
				coherentNeighbors.add(new Pair(point1, point2));
			}
		}
		
	}
	
	public void goRansac() {
		Matrix model = ransac();
		
		for (Pair para: mutualNeighbors) {
			double distance = Calculations.modelError(model, para);
			
			if(distance < MAX_ERROR){
				ransacPairs.add(para);
			}
		}
	}
	
	public Matrix ransac(){
		ransacPairs.clear();
		
		Matrix bestmodel = null;
		double bestscore = 0;
		
		Random random = new Random();
		
		for (int i = 0 ; i < RANSAC_ITERATIONS ; i++) {
			Matrix model = null;
			
			int przedzial = mutualNeighbors.size();
			
			Pair para1 = mutualNeighbors.get(random.nextInt(przedzial));
			Pair para2 = mutualNeighbors.get(random.nextInt(przedzial));
			Pair para3 = mutualNeighbors.get(random.nextInt(przedzial));
			
			try{
				model = Calculations.getTransform(para1, para2, para3);
			}catch(RuntimeException e){
				i--;
				continue;
			}
			
			int score = 0;
			
			for(Pair para: mutualNeighbors) {
				double error = Calculations.modelError(model, para);
				if (error < MAX_ERROR) {
					score++;
				}
			}
			
			if (score > bestscore) {
				bestscore = score;
				bestmodel = model;
			}
		}
		
		return bestmodel;
	}
	
	public void determineAll(int method) {
		determineNeighborsOfFirst();
		determineNeighborsOfSecond();
		determineMutualNeighbors();
		
		JOptionPane.showMessageDialog(win,
				"Wzajemnie sasiadujacych punktow na obrazie jest: " + mutualNeighbors.size(),
				"Info",
				JOptionPane.INFORMATION_MESSAGE);
		
		if (method == 0) {
			determineCoherentNeighbors();
			JOptionPane.showMessageDialog(win,
					"Punktow spojnych (spojnosc sasiedztwa) wyniosla: " + coherentNeighbors.size(),
					"Info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			goRansac();
			JOptionPane.showMessageDialog(win,
					"Punkty odnalezione metoda RANSAC: " + ransacPairs.size(),
					"Info",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void start(int method) {
		if(firstImage.isLoaded() && secondImage.isLoaded()){
			determineAll(method);
		}else{
			System.out.println("Obrazy nie zostaly jeszcze wczytane!");
		}
		
	}

	public ArrayList<Pair> getCoherentNeighbors() {
		return coherentNeighbors;
	}

	public ArrayList<Pair> getRansacPairs() {
		return ransacPairs;
	}
	
	public ArrayList<Pair> getMutualNeighbors() {
		return mutualNeighbors;
	}

	public ImageReprezentation getFirstImage() {
		return firstImage;
	}

	public ImageReprezentation getSecondImage() {
		return secondImage;
	}

}