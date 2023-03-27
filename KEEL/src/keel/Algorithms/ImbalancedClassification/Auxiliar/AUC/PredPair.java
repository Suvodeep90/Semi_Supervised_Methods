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

/**
* <p>
* @author Written by Sarah Vluymans (University of Ghent) 27/01/2014
* @version 0.1
* @since JDK 1.5
*</p>
*/

package keel.Algorithms.ImbalancedClassification.Auxiliar.AUC;

/**
 * This class represents pairs holding the class prediction and value of the
 * voting procedure.
 *
 * @author Written by Sarah Vluymans (University of Ghent) 28/01/2014
 * @version 1.1 (28-01-14)
 */

public class PredPair {
    
    // Class prediction
    private String prediction;
    
    // Value of the voting procedure
    private double sum;
    
    /** Constructor
     *
     * @param pred		Predicted class for an instance
     * @param sum	Given value of the voting procedure
     */
    public PredPair (String pred, double sum){
        prediction = pred;
        this.sum = sum;
    }
    
    /**
     * Provides the predicted class associated to a given instance
     *
     * @return	Predicted class associated to a given instance
     */  
    public String getPrediction(){
        return prediction;
    }    

    /**
     * Provides the value of the voting procedure associated to a given instance
     *
     * @return	Value of the voting procedure associated to a given instance
     */      
    public double getVotingValue(){
        return sum;
    }
}

