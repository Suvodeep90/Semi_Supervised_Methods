/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. Sánchez (luciano@uniovi.es)
    J. Alcalá-Fdez (jalcala@decsai.ugr.es)
    S. García (sglopez@ujaen.es)
    A. Fernández (alberto.fernandez@ujaen.es)
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

/***********************************************************************

	This file is part of the Fuzzy Instance Based Learning package, a
	Java package implementing Fuzzy Nearest Neighbor Classifiers as 
	complementary material for the paper:
	
	Fuzzy Nearest Neighbor Algorithms: Taxonomy, Experimental analysis and Prospects

	Copyright (C) 2012
	
	J. Derrac (jderrac@decsai.ugr.es)
	S. García (sglopez@ujaen.es)
	F. Herrera (herrera@decsai.ugr.es)

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



package keel.Algorithms.Fuzzy_Instance_Based_Learning.FENN;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.core.Files;

import keel.Algorithms.Fuzzy_Instance_Based_Learning.FuzzyIBLAlgorithm;
import keel.Algorithms.Fuzzy_Instance_Based_Learning.ReportTool;
import keel.Algorithms.Fuzzy_Instance_Based_Learning.Timer;
import keel.Algorithms.Fuzzy_Instance_Based_Learning.Util;

/**
 * 
 * File: FENN.java
 * 
 * The FENN algorithm. 
 * 
 * @author Written by Joaquín Derrac (University of Granada) 13/11/2011 
 * @version 1.0 
 * @since JDK1.5
 * 
 */
public class FENN extends FuzzyIBLAlgorithm {

	private static final double MAX_NORM = 100000000;
	private int K;
	private int KEdit;
	private boolean use [];
	private int KInit;
	private double M = 2.0;
	
	private double membership [][];
	
	/** 
	 * Reads the parameters of the algorithm. 
	 * 
	 * @param script Configuration script
	 * 
	 */
	@Override
	protected void readParameters(String script) {
		
		String file;
		String line;
		StringTokenizer fileLines, tokens;
		
	    file = Files.readFile (script);
	    fileLines = new StringTokenizer (file,"\n\r");
	    
	    //Discard in/out files definition
	    fileLines.nextToken();
	    fileLines.nextToken();
	    fileLines.nextToken();
	    
	    //Getting the K parameter
	    line = fileLines.nextToken();
	    tokens = new StringTokenizer (line, "=");
	    tokens.nextToken();
	    K = Integer.parseInt(tokens.nextToken().substring(1));
	    
	    //Getting the KEdit parameter
	    line = fileLines.nextToken();
	    tokens = new StringTokenizer (line, "=");
	    tokens.nextToken();
	    KEdit = Integer.parseInt(tokens.nextToken().substring(1));   
	    
	    //Getting the KInit parameter
	    line = fileLines.nextToken();
	    tokens = new StringTokenizer (line, "=");
	    tokens.nextToken();
	    KInit = Integer.parseInt(tokens.nextToken().substring(1));  
	    
	} //end-method	
	
	/**
	 * Main builder. Initializes the methods' structures
	 * 
	 * @param script Configuration script
	 */
	public FENN(String script){
		
		readDataFiles(script);
		
		//Naming the algorithm
		name="FENN";
		
		use = new boolean [trainData.length];
		membership = new double [trainData.length][nClasses];
		
		Arrays.fill(use, true);

	    //Initialization of Reporting tool
	    ReportTool.setOutputFile(outFile[2]);
	    
	} //end-method	
	
	/**
	 * Classifies the training set (leave-one-out)
	 */
	public void classifyTrain(){
	    
		//Start of training time
		Timer.resetTime();
		
		classifyTrainSet();
		
		//End of training time
		Timer.setTrainingTime();
		
		//Showing results
		System.out.println(name+" "+ relation + " Training " + Timer.getTrainingTime() + "s");
		
	} //end-method	
	
	/**
	 * Classifies the test set
	 */
	public void classifyTest(){
		    
		//Start of training time
		Timer.resetTime();
		
		classifyTestSet();
		
		//End of test time
		Timer.setTestTime();
		
		//Showing results
		System.out.println(name+" "+ relation + " Test " + Timer.getTestTime() + "s");	
		
	} //end-method	
	
	/**
	 * Classifies the training set
	 */
	public void classifyTrainSet(){
				
		for(int i=0;i<trainData.length;i++){
			
			trainPrediction[i]=classifyTrain(i,trainData[i]);
			
		}

	} //end-method	
	
	/**
	 * Classifies the test set
	 */
	public void classifyTestSet(){

		for(int i=0;i<testData.length;i++){

			testPrediction[i]=classifyTest(i,testData[i]);
			
		}

	} //end-method	
	
	/** 
	 * Evaluates a instance to predict its class membership
	 * 
	 * @param index Index of the instance in the test set
	 * @param example Instance evaluated 
	 * 
	 */
	private int classifyTrain(int index, double example[]) {
	
		double minDist[];
		int nearestN[];
		double dist;
		boolean stop;
		double pertenence [];
		int result;
		
		pertenence= new double [nClasses];

		nearestN = new int[K];
		minDist = new double[K];
	
	    for (int i=0; i<K; i++) {
			nearestN[i] = 0;
			minDist[i] = Double.MAX_VALUE;
		}
		
	    //KNN Method starts here
	    
		for (int i=0; i<trainData.length; i++) {
		
			if((i!=index)&&(use[i])){ //leave-one-out
				
			    dist = Util.euclideanDistance(trainData[i],example);
	
				//see if it's nearer than our previous selected neighbors
				stop=false;
					
				for(int j=0;j<K && !stop;j++){
					
					if (dist < minDist[j]) {
						    
						for (int l = K - 1; l >= j+1; l--) {
							minDist[l] = minDist[l - 1];
							nearestN[l] = nearestN[l - 1];
						}	
							
						minDist[j] = dist;
						nearestN[j] = i;
						stop=true;
					}
				}
			}
		}	
			
		//compute membership
		double norm[];
		double sum;
		
		norm = new double [nearestN.length];
		sum = 0.0;
		
		Arrays.fill(pertenence, 0.0);
		
		for(int i = 0;i<nearestN.length;i++){
			
			if(minDist[i]==0.0){
				norm[i]=MAX_NORM;
			}
			
			norm[i] = 1.0/ Math.pow(minDist[i],(2.0/(M-1.0))); 
			
			norm[i]=Math.min(norm[i],MAX_NORM);
			
			sum+=norm[i];
		}
		
		for(int i = 0;i<nearestN.length;i++){
			for(int c=0;c<nClasses;c++){
				pertenence[c]+= membership[nearestN[i]][c]*(norm[i]/sum);
			}
		}
			
		result=computeClass(pertenence);
		
		return result;
	
	}
	
	/** 
	 * Evaluates a instance to predict its class membership
	 * 
	 * @param index Index of the instance in the test set
	 * @param example Instance evaluated 
	 * 
	 */
	private int classifyTest(int index, double example[]) {
	
		double minDist[];
		int nearestN[];
		double dist;
		boolean stop;
		double pertenence [];
		int result;
		
		pertenence= new double [nClasses];

		nearestN = new int[K];
		minDist = new double[K];
	
	    for (int i=0; i<K; i++) {
			nearestN[i] = 0;
			minDist[i] = Double.MAX_VALUE;
		}
		
	    //KNN Method starts here
	    
		for (int i=0; i<trainData.length; i++) {
		
			if(use[i]){ //leave-one-out
				
			    dist = Util.euclideanDistance(trainData[i],example);
	
				//see if it's nearer than our previous selected neighbors
				stop=false;
					
				for(int j=0;j<K && !stop;j++){
					
					if (dist < minDist[j]) {
						    
						for (int l = K - 1; l >= j+1; l--) {
							minDist[l] = minDist[l - 1];
							nearestN[l] = nearestN[l - 1];
						}	
							
						minDist[j] = dist;
						nearestN[j] = i;
						stop=true;
					}
				}
			}
		}	
			
		//compute membership
		double norm[];
		double sum;
		
		norm = new double [nearestN.length];
		sum = 0.0;
		
		Arrays.fill(pertenence, 0.0);
		
		for(int i = 0;i<nearestN.length;i++){
			
			if(minDist[i]==0.0){
				norm[i]=MAX_NORM;
			}
			
			norm[i] = 1.0/ Math.pow(minDist[i],(2.0/(M-1.0))); 
			
			norm[i]=Math.min(norm[i],MAX_NORM);
			
			sum+=norm[i];
		}
		
		for(int i = 0;i<nearestN.length;i++){
			for(int c=0;c<nClasses;c++){
				pertenence[c]+= membership[nearestN[i]][c]*(norm[i]/sum);
			}
		}
			
		result=computeClass(pertenence);
		
		return result;
	
	}
	
	
	/**
	 * Generates the model of the algorithm
	 */
	public void generateModel(){
		
		//Start of model time
		Timer.resetTime();	
		
		assignMembership();
		
		editTrainingSet();

		//End of model time
		Timer.setModelTime();
	
		//Showing results
		System.out.println(name+" "+ relation + " Model " + Timer.getModelTime() + "s");
		
	}
	
	private void editTrainingSet(){
		
		int previous;
		int computed;
		double minDist[];
		int nearestN[];
		double dist;
		boolean stop;
		
		double pertenence [];
		
		pertenence= new double [nClasses];
		
		for(int instance=0;instance<trainData.length;instance++){
		
			previous=computeClass(membership[instance]);

			nearestN = new int[KEdit];
			minDist = new double[KEdit];
				
			for (int i=0; i<KEdit; i++) {
				nearestN[i] = 0;
				minDist[i] = Double.MAX_VALUE;
			}
					
			//KNN Method starts here
				    
			for (int i=0; i<trainData.length; i++) {
					
				dist = Util.euclideanDistance(trainData[i],trainData[instance]);

				if (i != instance){ //leave-one-out
						
					//see if it's nearer than our previous selected neighbors
					stop=false;
							
					for(int j=0;j<KEdit && !stop;j++){
							
						if (dist < minDist[j]) {
								    
							for (int l = KEdit - 1; l >= j+1; l--) {
								minDist[l] = minDist[l - 1];
								nearestN[l] = nearestN[l - 1];
							}	
									
							minDist[j] = dist;
							nearestN[j] = i;
							stop=true;
						}
					}
				}
			}
			
			//compute membership
			double norm[];
			double sum;
			
			norm = new double [nearestN.length];
			sum = 0.0;
			
			Arrays.fill(pertenence, 0.0);
			
			for(int i = 0;i<nearestN.length;i++){
				
				if(minDist[i]==0.0){
					norm[i]=MAX_NORM;
				}
				
				norm[i] = 1.0/ Math.pow(minDist[i],(2.0/(M-1.0))); 
				
				norm[i]=Math.min(norm[i],MAX_NORM);
				
				sum+=norm[i];
			}
			
			for(int i = 0;i<nearestN.length;i++){
				for(int c=0;c<nClasses;c++){
					pertenence[c]+= membership[nearestN[i]][c]*(norm[i]/sum);
				}
			}

			
			computed=computeClass(pertenence);

			if(previous!=computed){
				use[instance]= false;
			}
		}
	} 
	
	/**
	 * Computes the class of a instance given its membership array
	 * @param pertenence Membership array
	 * 
	 * @return Class assigned (crisp)
	 */
	private int computeClass(double pertenence[]){
		
		double max = Double.MIN_VALUE;
		
		int output=-1;
		
		for(int i=0; i< pertenence.length;i++){
			if(max<pertenence[i]){
				max=pertenence[i];
				output=i;
			}
		}
		
		return output;
		
	} //end-method	
	
	/**
	 * Assign class membership to each instance of the training set
	 */
	private void assignMembership(){
				
		for(int instance=0;instance<trainData.length;instance++){
					
			double minDist[];
			int nearestN[];
			int selectedClasses[];
			double dist;
			boolean stop;

			nearestN = new int[KInit];
			minDist = new double[KInit];
				
			for (int i=0; i<KInit; i++) {
				nearestN[i] = 0;
				minDist[i] = Double.MAX_VALUE;
			}
					
			//KNN Method starts here
				    
			for (int i=0; i<trainData.length; i++) {
					
				dist = Util.euclideanDistance(trainData[i],trainData[instance]);

				if (i != instance){ //leave-one-out
						
					//see if it's nearer than our previous selected neighbors
					stop=false;
							
					for(int j=0;j<KInit && !stop;j++){
							
						if (dist < minDist[j]) {
								    
							for (int l = KInit - 1; l >= j+1; l--) {
								minDist[l] = minDist[l - 1];
								nearestN[l] = nearestN[l - 1];
							}	
									
							minDist[j] = dist;
							nearestN[j] = i;
							stop=true;
						}
					}
				}
			}
			
			//we have check all the instances... see what is the most present class
			selectedClasses= new int[nClasses];
				
			Arrays.fill(selectedClasses, 0);
					
			for (int i=0; i<KInit; i++) {
				selectedClasses[trainOutput[nearestN[i]]]++;
			}
					
					
			Arrays.fill(membership[instance], 0.0);
					
			double term;
			
			for (int i=0; i<nClasses; i++) {
				term = ((double)selectedClasses[i]/(double)KInit);
				if(trainOutput[instance]==i){
					membership[instance][i]=0.51+0.49*term;
				}else{
					membership[instance][i]=0.49*term;
				}
			}

		}

	} //end-method	
	
	/**
	 * Reports the results obtained
	 */
	public void printReport(){
		
		writeOutput(outFile[0], trainOutput, trainPrediction);
		writeOutput(outFile[1], testOutput, testPrediction);
		
		ReportTool.setResults(trainOutput,trainPrediction,testOutput,testPrediction,nClasses);
		
		ReportTool.printReport();
		
	} //end-method	
	
} //end-class 
