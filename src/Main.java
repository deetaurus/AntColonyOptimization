package com.aco;

import java.util.Random;

public class Main {
	public static void main(String[] args){
		int no_of_cities=50;
		int no_of_ants=20;
		int no_of_iterations=1500;
		double[][] m_delta=new double[no_of_cities][no_of_cities];
		Random rand=new Random();
		for(int i=0;i<no_of_cities;i++)
			for(int j=0;j<no_of_cities;j++)
				m_delta[i][j]=rand.nextDouble()*no_of_cities;
		
		AntGraph antgraph=new AntGraph(no_of_cities,m_delta);
		AntColony4TSP antcolony=new AntColony4TSP(antgraph,no_of_ants,no_of_iterations);
		antcolony.start();
	}

}
