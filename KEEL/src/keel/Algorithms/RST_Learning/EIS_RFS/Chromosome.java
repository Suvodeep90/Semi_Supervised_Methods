/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
    J. Alcalá-Fdez (jalcala@decsai.ugr.es)
    A. Fernández (alberto.fernandez@ujaen.es)
    S. García (sglopez@ujaen.es)
    F. Herrera (herrera@decsai.ugr.es)
    L. Sánchez (luciano@uniovi.es)
    J. Luengo (julianlm@decsai.ugr.es)


   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/


package keel.Algorithms.RST_Learning.EIS_RFS;

import keel.Algorithms.RST_Learning.KNNClassifier;
import keel.Algorithms.RST_Learning.RSTData;

import org.core.Randomize;

/**
 * 
 * File: Chromosome.java
 * 
 * A implementation of a chromosome class for EIS_RFS.
 * 
 * @author Written by Joaquín Derrac (University of Granada) 13/11/2011 
 * @version 1.0 
 * @since JDK1.5
 * 
 */

public class Chromosome implements Comparable<Chromosome>{

	private static int size;
	private static double init;
	private static double alpha;
	private static double mutationProb;
	private double fitness;
	
	private int body[];
	
        /**
     * Sets the size of the chromosome with the value given as parameter.
     * @param value given size to set.
     */
	public static void setSize(int value){
		size=value;
	}
	
            /**
     * Sets the initial probability to build the chromosome.
     * @param value probability to set.
     */
	public static void setInitProb(double value){
		init=value;
	}
	
            /**
     * Sets the alpha parameter with the value given.
     * @param value alpha to set.
     */
	public static void setAlpha(double value){
		alpha=value;
	}
	
            /**
     * Sets the mutation probability with the value given.
     * @param value probability to set.
     */
	public static void setMutationProbability(double value){
		mutationProb=value;
	}
	
            /**
     * Default constructor. Initiates the chromosome with the class attributes (size, init,...)
     */
	public Chromosome(){
		
		body=new int [size];
		
		for(int i=0;i<size;i++){
			body[i]= i;
		}
		
		shuffleBody();
		
		for(int i=0;i<size;i++){
			if(body[i]<=(int)(size*init)){
				body[i]=1;
			}
			else{
				body[i]=0;
			}
		}		
		
		fitness=-1.0;
		
	}
	
            /**
     * Parameter constructor. Initiates the chromosome with the vector given.
     * @param newBody vector given to initiate the chromosome.
     */
	public Chromosome(int [] newBody){
		
		body=new int [size];
		
		for(int i=0;i<size;i++){
			body[i]= newBody[i];
		}

		fitness=-1.0;
		
	}

            /**
     * Parameter constructor. Initiates the chromosome with the vector and the fitness given.
     * @param newBody vector given to initiate the chromosome.
     * @param fitnessValue fitness to set.
     */
	public Chromosome(int [] newBody, double fitnessValue){
		
		body=new int [size];
		
		for(int i=0;i<size;i++){
			body[i]= newBody[i];
		}

		fitness=fitnessValue;
		
	}
	
	private void shuffleBody(){
		
		int pos,tmp;
		
	    for (int i=0; i<size; i++) {
	    	
	    	pos = Randomize.Randint (0, size);
	    	tmp = body[i];
	    	body[i] = body[pos];
	    	body[pos] = tmp;
	    }
	}
	
            /**
     * Returns the size of the chromosome.
     * @return the size of the chromosome.
     */
	public static int getSize(){	
		return size;
	}
	
            /**
     * Checks if the chromosome has been evaluated.
     * @return True if the chromosome has been evaluated.
     */
	public boolean isEvaluated(){
		
		if(fitness==-1.0){
			return false;
		}
		
		return true;
	}
	
            /**
     * Returns the fitness of the chromosome.
     * @return the fitness of the chromosome.
     */
	public double getFitness(){
		
		return fitness;
	}
	
            /**
     * Sets the fitness of this chromosome with the value given.
     * @param value fitness value to set.
     */
	public void setFitness(double value){
		
		fitness=value;
	}
	
            /**
     * Returns the binary value (0 or 1) of the position given.
     * @param pos position given.
     * @return the binary value (0 or 1) of the position given.
     */
	public int get(int pos){
		
		return body[pos];
	}
	
            /**
     * Sets the value in the chromosome of the given position with the value given.
     * @param pos position given.
     * @param value value to set.
     */
	public void set(int pos,int value){
		
		body[pos]=value;
	}
	
            /**
     * Returns the chromosome representation (array of integers).
     * @return the chromosome representation (array of integers).
     */
	public int [] getAll(){
		
		return body;
	}
	
            /**
     * Computes and returns the reduction ratio coded in the chromosome.
     * @return  the reduction ratio coded in the chromosome.
     */
	public double computeRed(){
		
		double count=0;
		double result;
		
		for(int i=0;i<size;i++){
			if(body[i]==1){
				count+=1.0;
			}
		}
		
		result=(double)(count/(double)size);
		result=1.0-result;
		
		return result;
	}
	
    /**
     * Function that evaluates a chromosome. The atttribute {@code fitness} will be set.
     */
	public void evaluate () {

		double acc;
		double red;
		  
		red=computeRed();
		
		if(red==1.0){
			fitness=-1.0;
		}
		else{
			KNNClassifier.setInstances(body);
			acc=KNNClassifier.accuracy();
			fitness=(alpha*acc)+((1.0-alpha)*red);
		}

	}
	
    /**
     * Returns the best features for the chromosome representation.
     * @return the best features for the chromosome representation.
     */
    public int [] getBestFeatures(){
		
		RSTData.setInstances(body);
		RSTData.computeBestFeatures();
		
		return RSTData.getAttributes();
	}
	
    /**
     * Mutates the chromosome.
     */
	public void mutation() {
		
	    for (int i=0; i<body.length; i++) {
	    	if (Randomize.Rand() < mutationProb) {
	    		body[i]=(body[i]+1)%2;
	    	}
	    }
	    
	}	
	
    /**
     * Crosses this chromosome with the one given as parameter with PMX operator.
     * The chromosome will be modified, being one of the child, and the other will be returned.
     * @param parent given chromosome (as vector) to cross with.
     * @return the other chromosome child (as vector).
     */
	public int [] crossPMX (int [] parent) {
	
		int point1,point2;
		int down,up;
		int [] offspring;
		
		point1 = Randomize.Randint (0, parent.length-1);
		point2 = Randomize.Randint (0, parent.length-1);
		
	    if (point1 > point2) {
	    	up = point1;
	    	down = point2;
	    } 
	    else {
	    	up = point2;
	    	down = point1;
	    }
	    
	    //save body genes
	    int  [] copy;
	    
	    copy=new int[body.length];
	    
	    System.arraycopy(body, 0, copy, 0, copy.length);
	    
	    //crossing first offspring (self)
	    
	    for(int i=down; i<up; i++){
	    	body[i]=parent[i];
	    }
	    
	    //crossing second offspring (outter)
	    
	    offspring= new int [parent.length];
	    
	    for(int i=0; i<down; i++){
	    	offspring[i]=parent[i];
	    }
	    
	    for(int i=down; i<up; i++){
	    	offspring[i]=copy[i];
	    }
	    
	    for(int i=up; i<parent.length; i++){
	    	offspring[i]=parent[i];
	    }
	    
	    return offspring;
	}
	
	@Override
	public int compareTo(Chromosome other) {
		
		if(this.fitness>other.fitness){
			return -1;
		}
		
		if(this.fitness<other.fitness){
			return 1;
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		
		String text="";
		
		for(int i=0;i<body.length;i++){
			text+=body[i];
		}
		text+=" Fitness: "+fitness;
		
		return text;
	}
    
} //end-class 
