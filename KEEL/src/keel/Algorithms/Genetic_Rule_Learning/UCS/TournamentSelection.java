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


package keel.Algorithms.Genetic_Rule_Learning.UCS;

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * <p>This class implements the tournament selection method.
 * @author Written by Albert Orriols (La Salle University Ramón Lull, Barcelona)  28/03/2004
 * @author Modified by Xavi Solé (La Salle University Ramón Lull, Barcelona) 03/12/2008
 * @version 1.1
 * @since JDK1.2
 * </p>
 */

public class TournamentSelection implements Selection {
/**
 * <p>
 * This class implements the tournament selection method.
 * </p>
 */

/**
 * Creates a TournamentSelection object
 */
    public  TournamentSelection() {        
    } // end TournamentSelection        



/**
 * Initializes the tournament selection. It does nothing,
 * but it is required as this class implements Selection
 * 
 * @param correctSet is the population.
 */
  public void init( Population correctSet ) {        
  } // end init



/**
 * Applies the tournament selection.
 *
 * @param correctSet is the population.
 * @return a Classifier with the selected classifier
 */
  public Classifier makeSelection( Population correctSet ){        
	return correctSet.tournamentSelection ();
  } // end makeSelection        

} // END OF CLASS TournamentSelection




