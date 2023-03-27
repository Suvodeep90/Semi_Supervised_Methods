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


package keel.Algorithms.Decision_Trees.C45_Binarization;

import org.core.*;
import java.util.StringTokenizer;

/**
 * 
 * File: ENN.java
 * 
 * The ENN Instance Selection algorithm.
 * 
 * @author Written by Salvador García (University of Granada) 20/07/2004 
 * @version 0.1 
 * @since JDK1.5
 * 
 */
public class ENN extends Metodo {

	/*Own parameters of the algorithm*/
	private int k;
        private int nClases;

    /**
     * Default builder. Construct the algoritm by using the superclass builder.
	 *
     * @param ficheroScript Name of the configuration files.
     */
	public ENN (String ficheroScript) {
		super (ficheroScript);
            /*Getting the number of differents classes*/
		nClases = 0;
		for (int i=0; i<clasesTrain.length; i++)
		  if (clasesTrain[i] > nClases)
			nClases = clasesTrain[i];
		nClases++;
		
	}//end-method 
	
    /**
     * Sets the number of NN used.
     * @param k number of NN used.
     */
    public void setK(int k) {
            this.k = k;
        }

    /**
     * Returns the number k of the NN used.
     * @return the number k of the NN used.
     */
    public int getK() {
            return k;
        }
        
    
    /**
     * Executes KNN to predict the class of the instance i.
     * @param i Index of the instance to predict.
     * @param vecinos Resulting neighbors of the new instance.
     * @param distancias Resulting distances to each neighbor.
     * @param votos Resulting votes for each class.
     * @param test True if the instance belong to the test dataset.
     * @param distanceEu True= Euclidean distance; False= HVDM
     * @return Class of the new instance
     */
    public int evaluaKNN(int i, int[] vecinos, double[] distancias, int[] votos, boolean test, boolean distanceEu) {
            if (!test)
                return KNN.evaluacionKNN2 (k, datosTrain, realTrain, nominalTrain, nulosTrain, clasesTrain, datosTrain[i], realTrain[i], nominalTrain[i], nulosTrain[i], nClases, distanceEu, vecinos, distancias, votos);
            else
                return KNN.evaluacionKNN2 (k, datosTrain, realTrain, nominalTrain, nulosTrain, clasesTrain, datosTest[i], realTest[i], nominalTest[i], nulosTest[i], nClases, distanceEu, vecinos, distancias, votos);
             
        }
        
        
 	/**
	 * Executes the algorithm
	 */ 
	public void ejecutar () {

		int i, j, l;
		int nClases;
		int claseObt;
		boolean marcas[];
		int nSel = 0;
		
		double conjS[][];
		double conjR[][];
		int conjN[][];
		boolean conjM[][];
		int clasesS[];

		long tiempo = System.currentTimeMillis();

		/*Inicialization of the flagged instances vector for a posterior copy*/
		marcas = new boolean[datosTrain.length];
		for (i=0; i<datosTrain.length; i++)
		  marcas[i] = false;

		/*Getting the number of differents classes*/
		nClases = 0;
		for (i=0; i<clasesTrain.length; i++)
		  if (clasesTrain[i] > nClases)
			nClases = clasesTrain[i];
		nClases++;

		/*Body of the algorithm. For each instance in T, search the correspond class conform his mayority
		 from the nearest neighborhood. Is it is positive, the instance is selected.*/
		for (i=0; i<datosTrain.length; i++) {
		  /*Apply KNN to the instance*/
		  claseObt = KNN.evaluacionKNN2 (k, datosTrain, realTrain, nominalTrain, nulosTrain, clasesTrain, datosTrain[i], realTrain[i], nominalTrain[i], nulosTrain[i], nClases, distanceEu);
		  if (claseObt == clasesTrain[i]) { //agree with your majority, it is included in the solution set
			marcas[i] = true;
			nSel++;
		  }
		}

		/*Building of the S set from the flags*/
		conjS = new double[nSel][datosTrain[0].length];
		conjR = new double[nSel][datosTrain[0].length];
		conjN = new int[nSel][datosTrain[0].length];
		conjM = new boolean[nSel][datosTrain[0].length];
		clasesS = new int[nSel];
		for (i=0, l=0; i<datosTrain.length; i++) {
		  if (marcas[i]) { //the instance will be copied to the solution
			for (j=0; j<datosTrain[0].length; j++) {
			  conjS[l][j] = datosTrain[i][j];
			  conjR[l][j] = realTrain[i][j];
			  conjN[l][j] = nominalTrain[i][j];
			  conjM[l][j] = nulosTrain[i][j];
			}
			clasesS[l] = clasesTrain[i];
			l++;
		  }
		}

		System.out.println("ENN "+ relation + " " + (double)(System.currentTimeMillis()-tiempo)/1000.0 + "s");

                // COn conjS me vale.
                 int trainRealClass[][];
                 int trainPrediction[][];
                
                 trainRealClass = new int[conjS.length][1];
		 trainPrediction = new int[conjS.length][1];	
                
                 //Working on training
                 for ( i=0; i<conjS.length; i++) {
                     trainRealClass[i][0] = clasesS[i];
                     trainPrediction[i][0] = KNN.evaluate(conjS[i],datosTrain, nClases, clasesTrain, this.k);
                 }
                 
                 KNN.writeOutput(ficheroSalida[0], trainRealClass, trainPrediction,  entradas, salida, relation);
                 
                 
                //Working on test
		int realClass[][] = new int[datosTest.length][1];
		int prediction[][] = new int[datosTest.length][1];	
		
		//Check  time		
				
		for (i=0; i<realClass.length; i++) {
			realClass[i][0] = clasesTest[i];
			prediction[i][0]= KNN.evaluate(datosTest[i],conjS, nClases, clasesS, this.k);
		}
                
                KNN.writeOutput(ficheroSalida[1], realClass, prediction,  entradas, salida, relation);

		
	}//end-method 

	/** 
	 * Reads configuration script, and extracts its contents.
	 * 
	 * @param ficheroScript Name of the configuration script  
	 * 
	 */	
	public void leerConfiguracion (String ficheroScript) {

		String fichero, linea, token;
		StringTokenizer lineasFichero, tokens;
		byte line[];
		int i, j;

		ficheroSalida = new String[2];

		fichero = Fichero.leeFichero (ficheroScript);
		lineasFichero = new StringTokenizer (fichero,"\n\r");

		lineasFichero.nextToken();
		linea = lineasFichero.nextToken();

		tokens = new StringTokenizer (linea, "=");
		tokens.nextToken();
		token = tokens.nextToken();

		/*Getting the names of the training and test files*/
		line = token.getBytes();
		for (i=0; line[i]!='\"'; i++);
		i++;
		for (j=i; line[j]!='\"'; j++);
		ficheroTraining = new String (line,i,j-i);
		for (i=j+1; line[i]!='\"'; i++);
		i++;
		for (j=i; line[j]!='\"'; j++);
		ficheroTest = new String (line,i,j-i);

		/*Getting the path and base name of the results files*/
		linea = lineasFichero.nextToken();
		tokens = new StringTokenizer (linea, "=");
		tokens.nextToken();
		token = tokens.nextToken();

		/*Getting the names of output files*/
		line = token.getBytes();
		for (i=0; line[i]!='\"'; i++);
		i++;
		for (j=i; line[j]!='\"'; j++);
		ficheroSalida[0] = new String (line,i,j-i);
		for (i=j+1; line[i]!='\"'; i++);
		i++;
		for (j=i; line[j]!='\"'; j++);
		ficheroSalida[1] = new String (line,i,j-i);
		
		/*Getting the number of neighbors*/
		linea = lineasFichero.nextToken();
		tokens = new StringTokenizer (linea, "=");
		tokens.nextToken();
		k = Integer.parseInt(tokens.nextToken().substring(1));
	  
		/*Getting the type of distance function*/
		linea = lineasFichero.nextToken();
		tokens = new StringTokenizer (linea, "=");
		tokens.nextToken();
		distanceEu = tokens.nextToken().substring(1).equalsIgnoreCase("Euclidean")?true:false;    
		
	}//end-method 
	
}//end-class
