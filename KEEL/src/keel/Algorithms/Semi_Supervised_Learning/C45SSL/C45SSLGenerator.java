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


/*
	C45SSL.java
	Isaac Triguero Velazquez.
	
	Created by Isaac Triguero Velazquez  11-1-2011
	Copyright (c) 2008 __MyCompanyName__. All rights reserved.

*/

package keel.Algorithms.Semi_Supervised_Learning.C45SSL;

import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeSet;
import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeGenerator;
import keel.Algorithms.Semi_Supervised_Learning.Basic.Prototype;
import keel.Algorithms.Semi_Supervised_Learning.Basic.ParametersC45;
import keel.Algorithms.Semi_Supervised_Learning.Basic.C45.*;
import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeGenerationAlgorithm;

import keel.Algorithms.Semi_Supervised_Learning.*;
import java.util.*;

import keel.Algorithms.Semi_Supervised_Learning.utilities.*;
import keel.Algorithms.Semi_Supervised_Learning.utilities.KNN.*;
import keel.Dataset.Attribute;
import keel.Dataset.Attributes;

import org.core.*;

import org.core.*;

import java.util.StringTokenizer;



/**
 * This class implements the Self-traning wrapper. You can use: Knn, C4.5, SMO and Ripper as classifiers.
 * @author triguero
 *
 */

public class C45SSLGenerator extends PrototypeGenerator {

  /*Own parameters of the algorithm*/

 
 private int numberOfselectedExamples;

 private boolean prune;
 private double confidence;
 private int minItemsets;
 
    /**
     * Number of prototypes.
     */
    protected int numberOfPrototypes;  // Particle size is the percentage

    /**
     * Number of classes.
     */
    protected int numberOfClass;
  /** Parameters of the initial reduction process. */
  private String[] paramsOfInitialReducction = null;

  
  /**
   * Build a new C45SSLGenerator Algorithm
   * @param _trainingDataSet Original prototype set to be reduced.
     * @param neigbors number of neighbours considered. (not used)
     * @param poblacion population size. (not used)
   * @param perc Reduction percentage of the prototype set.
     * @param iteraciones number of iterations. (not used)
     * @param wend ending w value. (not used)
     * @param c1 class 1 value. (not used)
     * @param vmax maximum v value. (not used)
     * @param c2 class 2 value. (not used)
     * @param wstart starting w value. (not used)
   */
  public C45SSLGenerator(PrototypeSet _trainingDataSet, int neigbors,int poblacion, int perc, int iteraciones, double c1, double c2, double vmax, double wstart, double wend)
  {
      super(_trainingDataSet);
      algorithmName="C45SSL";
      
  }
  


  /**
   * Build a new C45SSLGenerator Algorithm
   * @param t Original prototype set to be reduced.
   * @param unlabeled Original unlabeled prototype set for SSL.
     * @param test Origital test prototype set.
     * @param parameters Parameters of the algorithm (only % of reduced set).
   */
  public C45SSLGenerator(PrototypeSet t, PrototypeSet unlabeled, PrototypeSet test, Parameters parameters)
  {
      super(t,unlabeled, test, parameters);
      algorithmName="C45SSL";
   
      
      //Last class is the Unknown 
      this.numberOfClass = trainingDataSet.getPosibleValuesOfOutput().size();
      String prune = parameters.getNextAsString();
      
      if(prune.equalsIgnoreCase("TRUE")){
    	  this.prune = true;
      }else{
    	  this.prune = false;
      }
    
      this.confidence = parameters.getNextAsDouble();
      this.minItemsets = parameters.getNextAsInt();
      
      ParametersC45.prune = this.prune;
      ParametersC45.confidence = this.confidence;
      ParametersC45.itemsetsPerLeaf = this.minItemsets;
      
      
      System.out.print("\nIsaacSSL dice:  " + this.numberOfselectedExamples+ ", "+ this.numberOfClass +"\n");

  }
  
    /**
     * Applies the C4.5 algorithm for the dataset given.
     * @param labeled labeled dataset to build the model.
     * @param unlabeled unlabeled dataset to obtain their predicted classes.
     * @return  predicted classes for the unlabeled dataset.
     * @throws Exception
     */
    public int [] applyC45(PrototypeSet labeled, PrototypeSet unlabeled)  throws Exception{

  
	  //labeled.save("labeled.dat");
	  //unlabeled.save("unlabeled.dat");
	
  
      //C45 c45 = new C45("labeled.dat", "unlabeled.dat");      // C4.5 called
      
	  C45 c45 = new C45(labeled.toInstanceSet(), unlabeled.toInstanceSet());      // C4.5 called
	  
      int[] pre = c45.getPredictions();    
      
      return pre;
  }
  
  
    /**
     * Apply the C45SSLGenerator method.
     * @return transductive test sets.
     * @throws java.lang.Exception if the algorithm can not be applied.
     */
    public Pair<PrototypeSet, PrototypeSet> applyAlgorithm() throws Exception
  {
	  System.out.print("\nThe algorithm SELF TRAINING is starting...\n Computing...\n");
	  
	  PrototypeSet labeled;
		  
	  labeled = new PrototypeSet(trainingDataSet.getAllDifferentFromClass(this.numberOfClass)); // Selecting labeled prototypes from the training set.

	    
	  PrototypeSet tranductive = new PrototypeSet(this.transductiveDataSet.clone());
	  PrototypeSet test = new PrototypeSet(this.testDataSet.clone());
	    
	  int traPrediction[] = applyC45(labeled, tranductive);
	  int tstPrediction[] = applyC45(labeled, test);
	  
	  int aciertoTrs = 0;
	  int aciertoTst = 0;
	  
		  //We have to return the classification done.
		  for(int i=0; i<this.transductiveDataSet.size(); i++){
			  if(tranductive.get(i).getOutput(0) == traPrediction[i]){
				  aciertoTrs++;
			  }
			  
			  tranductive.get(i).setFirstOutput(traPrediction[i]);
		  }
		  
		  System.out.println("% de acierto TRS = "+ (aciertoTrs*100.)/transductiveDataSet.size());
		  
		  for(int i=0; i<this.testDataSet.size(); i++){
			  if(test.get(i).getOutput(0) == tstPrediction[i]){
				  aciertoTst++;
			  }
			  test.get(i).setFirstOutput(tstPrediction[i]);
		  }
		  
		  System.out.println("% de acierto TST = "+ (aciertoTst*100.)/testDataSet.size());
	  
	  
      return new Pair<PrototypeSet,PrototypeSet>(tranductive,test);
  }
  
  /**
   * General main for all the prototoype generators
   * Arguments:
   * 0: Filename with the training data set to be condensed.
   * 1: Filename which contains the test data set.
   * 3: Seed of the random number generator.            Always.
   * **************************
   * @param args Arguments of the main function.
   */
  public static void main(String[] args)
  {  }

}
