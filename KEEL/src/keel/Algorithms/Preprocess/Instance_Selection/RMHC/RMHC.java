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

//
//  RMHC.java
//
//  Salvador García López
//
//  Created by Salvador García López 17-7-2004.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package keel.Algorithms.Preprocess.Instance_Selection.RMHC;

import keel.Algorithms.Preprocess.Basic.*;

import org.core.*;
import java.util.StringTokenizer;

/**
 * 
 * File: RMHC.java
 * 
 * The RMHC Instance Selection algorithm.
 * 
 * @author Written by Salvador García (University of Granada) 20/07/2004 
 * @version 0.1 
 * @since JDK1.5
 * 
 */
public class RMHC extends Metodo{

  /*Own parameters of the algorithm*/
  private long semilla;
  private int k;
  private double porcentaje;
  private int n;

  /**
     * Default constructor. Construct the algoritm by using the superclass builder.
	  * @param ficheroScript Configuration script
     */
  public RMHC (String ficheroScript) {
    super (ficheroScript);
  }

  /**
	 * Executes the algorithm
	 */ 
  public void ejecutar () {

    int i, j, l, m, o;
    int nClases;
    int claseObt;
    boolean marcas[];
    double conjS[][];
    double conjR[][];
    int conjN[][];
    boolean conjM[][];
    int clasesS[];
    int eleS[], eleT[];
    int bestAc, aciertos;
    int temp[];
    int pos, tmp, pos2;

    long tiempo = System.currentTimeMillis();

    /*Getting the numebr of different classes*/
    nClases = 0;
    for (i=0; i<clasesTrain.length; i++)
      if (clasesTrain[i] > nClases)
        nClases = clasesTrain[i];
    nClases++;

    /*Inicialization if the flagged instances vector of the S set*/
    marcas = new boolean[datosTrain.length];
    for (i=0; i<datosTrain.length; i++)
      marcas[i] = false;

    /*Allocate memory for random selection*/
    m = (int)((porcentaje * datosTrain.length) / 100.0);
    eleS = new int[m];
    eleT = new int[datosTrain.length - m];
    temp = new int[datosTrain.length];
    for (i=0; i<datosTrain.length; i++)
      temp[i] = i;

    /*Initial random distribution of instances in each set*/
    Randomize.setSeed (semilla);
    for (i=0; i<eleS.length; i++) {
      pos = Randomize.Randint (i, datosTrain.length-1);
      tmp = temp[i];
      temp[i] = temp[pos];
      temp[pos] = tmp;
      eleS[i] = temp[i];
    }
    for (i=0; i<eleT.length; i++) {
      pos = Randomize.Randint (m+i, datosTrain.length-1);
      tmp = temp[m+i];
      temp[m+i] = temp[pos];
      temp[pos] = tmp;
      eleT[i] = temp[m+i];
    }
    for (i=0; i<eleS.length; i++)
      marcas[eleS[i]] = true;

    /*Building of S set from the flags*/
    conjS = new double[m][datosTrain[0].length];
    conjR = new double[m][datosTrain[0].length];
    conjN = new int[m][datosTrain[0].length];
    conjM = new boolean[m][datosTrain[0].length];
    clasesS = new int[m];
    for (o=0, l=0; o<datosTrain.length; o++) {
      if (marcas[o]) { //the instance will be evaluated
        for (j=0; j<datosTrain[0].length; j++) {
          conjS[l][j] = datosTrain[o][j];
          conjR[l][j] = realTrain[o][j];
          conjN[l][j] = nominalTrain[o][j];
          conjM[l][j] = nulosTrain[o][j];
          
        }
        clasesS[l] = clasesTrain[o];
        l++;
      }
    }

    /*Evaluation of s set*/
    bestAc = 0;
    for (i=0; i<datosTrain.length; i++) {
      claseObt = KNN.evaluacionKNN2(k, conjS, conjR, conjN, conjM, clasesS, datosTrain[i], realTrain[i], nominalTrain[i], nulosTrain[i], nClases, distanceEu);	
      if (claseObt == clasesTrain[i])  //correct clasification
        bestAc++;
    }

    /*Body of the RMHC algorithm. Change only an element of the S set and see if it is
     improves classification than previous. Is it is true, maintain the new set; if it
     is not better, undo the change*/
    for (i=0; i<n && eleS.length > 0; i++) {
      pos = Randomize.Randint (0,eleS.length-1);
      pos2 = Randomize.Randint (0, eleT.length-1);

      /*Interchange of instances*/
      tmp = eleS[pos];
      eleS[pos] = eleT[pos2];
      eleT[pos2] = tmp;
      marcas[eleS[pos]] = true;
      marcas[eleT[pos2]] = false;

      /*Building of S set from the flags*/
      for (o=0, l=0; o<datosTrain.length; o++) {
        if (marcas[o]) { //the instance will evaluate
          for (j=0; j<datosTrain[0].length; j++) {
            conjS[l][j] = datosTrain[o][j];
            conjR[l][j] = realTrain[o][j];
            conjN[l][j] = nominalTrain[o][j];
            conjM[l][j] = nulosTrain[o][j];
          }
          clasesS[l] = clasesTrain[o];
          l++;
        }
      }

      /*Evaluation of the S set*/
      aciertos = 0;
      for (j=0; j<datosTrain.length; j++) {
          claseObt = KNN.evaluacionKNN2(k, conjS, conjR, conjN, conjM, clasesS, datosTrain[j], realTrain[j], nominalTrain[j], nulosTrain[j], nClases, distanceEu);	
        if (claseObt == clasesTrain[j])  //correct clasification
          aciertos++;
      }

      if (aciertos > bestAc) { //maintain S
        bestAc = aciertos;
      } else { //undo changes
        tmp = eleS[pos];
        eleS[pos] = eleT[pos2];
        eleT[pos2] = tmp;
        marcas[eleS[pos]] = true;
        marcas[eleT[pos2]] = false;
      }
    }

    /*Building S s from the flags*/
    for (o=0, l=0; o<datosTrain.length; o++) {
      if (marcas[o]) { //the instance will evaluate
        for (j=0; j<datosTrain[0].length; j++) {
          conjS[l][j] = datosTrain[o][j];
          conjR[l][j] = realTrain[o][j];
          conjN[l][j] = nominalTrain[o][j];
          conjM[l][j] = nulosTrain[o][j];
        }
        clasesS[l] = clasesTrain[o];
        l++;
      }
    }

    System.out.println("RMHC "+ relation + " " + (double)(System.currentTimeMillis()-tiempo)/1000.0 + "s");

    OutputIS.escribeSalida(ficheroSalida[0], conjR, conjN, conjM, clasesS, entradas, salida, nEntradas, relation);
    OutputIS.escribeSalida(ficheroSalida[1], test, entradas, salida, nEntradas, relation);
  }

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

    /*Getting the names of training and test files*/
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

    /*Getting the names of the output files*/
    line = token.getBytes();
    for (i=0; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroSalida[0] = new String (line,i,j-i);
    for (i=j+1; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroSalida[1] = new String (line,i,j-i);

    /*Getting the seed*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    semilla = Long.parseLong(tokens.nextToken().substring(1));

    /*Getting the number of neighbors*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    k = Integer.parseInt(tokens.nextToken().substring(1));

    /*Getting the percentage*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    porcentaje = Double.parseDouble(tokens.nextToken().substring(1));

    /*Getting the number of iterations*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    n = Integer.parseInt(tokens.nextToken().substring(1));
  
    /*Getting the type of distance function*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    distanceEu = tokens.nextToken().substring(1).equalsIgnoreCase("Euclidean")?true:false;    
}
}
