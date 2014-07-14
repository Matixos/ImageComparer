package pl.com.mat.imagecomparer.utils;

import java.util.ArrayList;

import Jama.Matrix;

public class Calculations {

	public static int getDistanceBetweenVectors(ArrayList<Integer> vector1 , ArrayList<Integer> vector2) {
		int result = 0;
		
		for (int i = 0 ; i < vector1.size() ; i++){
			result += (vector1.get(i) - vector2.get(i)) * (vector1.get(i) - vector2.get(i));
		}
		
		return result;
	}
	
	public static Matrix getTransform(Pair p1 , Pair p2, Pair p3) {
		
		double [][] firstMatrix = { {p1.getFirst().getX(), p1.getFirst().getY() , 1 , 0 , 0 , 0},
										 {p2.getFirst().getX(), p2.getFirst().getY() , 1 , 0 , 0 , 0},
										 {p3.getFirst().getX(), p3.getFirst().getY() , 1 , 0 , 0 , 0},
										 {0 , 0 , 0 , p1.getFirst().getX() , p1.getFirst().getY() , 1},
										 {0 , 0 , 0 , p2.getFirst().getX() , p2.getFirst().getY() , 1},
										 {0 , 0 , 0 , p3.getFirst().getX() , p3.getFirst().getY() , 1}
									   };
		
		double [][] secondMatrix = {
										{p1.getSecond().getX()},
										{p2.getSecond().getX()},
										{p3.getSecond().getX()},
										{p1.getSecond().getY()},
										{p2.getSecond().getY()},
										{p3.getSecond().getY()}
									};
		
		Matrix matrix1 = new Matrix(firstMatrix);
		Matrix matrix2 = new Matrix(secondMatrix);
		
		matrix1 = matrix1.inverse();
		matrix1 = matrix1.times(matrix2);
		
		double [][] tmp = matrix1.getArray();
		
		double [][] result = {
								{tmp[0][0] , tmp[1][0], tmp[2][0]},
								{tmp[3][0] , tmp[4][0], tmp[5][0]},
								{0 , 0 , 1}
							};
		
		return new Matrix(result);	
	}
	
	public static double modelError (Matrix model, Pair p) {
		Point point1 = p.getFirst();      				// p1 ->   p2
		Point point2 = p.getSecond();
		
		double [][] point = {
								{point1.getX()},
								{point1.getY()},
								{1}
							};
		
		Matrix multiply = new Matrix(point);
		
		Matrix resultMatrix = model.times(multiply);
		
		point = resultMatrix.getArray();
		
		double result =  
						Math.sqrt(
						((point[0][0] - (point2.getX())) * (point[0][0] - (point2.getX())))
						+
						((point[1][0] - (point2.getY())) * (point[1][0] - (point2.getY())))
						)
						;
		if(result < 0)
			System.out.println("Error");
		
		return result;
	}

}