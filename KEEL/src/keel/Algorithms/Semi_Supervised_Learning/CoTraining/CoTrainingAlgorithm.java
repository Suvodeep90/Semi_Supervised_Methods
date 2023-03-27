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
//  Main.java
//
//  Isaak Triguero
//
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package keel.Algorithms.Semi_Supervised_Learning.CoTraining;

import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeSet;
import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeGenerationAlgorithm;
import keel.Algorithms.Semi_Supervised_Learning.*;
import keel.Algorithms.Semi_Supervised_Learning.utilities.*;

import java.util.*;

/**
 * CoTraining algorithm calling.
 * @author Isaac Triguero
 */
public class CoTrainingAlgorithm extends PrototypeGenerationAlgorithm<CoTrainingGenerator>
{
    /**
     * Builds a new CoTrainingGenerator.
     * @param unlabeled Unlabeled data set.
     * @param test Test data set.
     * @param params Parameters of the algorithm of reduction.
     * @return  New prototype CoTrainingGenerator object with data and parameters full load. 
     */
    protected CoTrainingGenerator buildNewPrototypeGenerator(PrototypeSet train, PrototypeSet unlabeled, PrototypeSet test, Parameters params)
    {
       return new CoTrainingGenerator(train, unlabeled, test, params);    
    }
    
     /**
     * Main method. Executes CoTraining algorithm.
     * @param args Console arguments of the method.
     */
    public static void main(String args[])
    {
        CoTrainingAlgorithm isaak = new CoTrainingAlgorithm();
        isaak.execute(args);
    }
}
