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

package keel.Algorithms.Discretizers.ExtendedChi2_Discretizer;

import keel.Algorithms.Genetic_Rule_Learning.Globals.Parameters;

/**
 * <p>
 * @author Written by Salvador García (University of Jaén - Jaén) 3/04/2009
 * @version 1.0
 * @since JDK1.5
 * </p>
 */
	public class Interval {
	/**
	 * <p>
	 * Interval class.
	 * </p>
	 */	
		
		int attribute;
		int begin;
		int end;
		int []values;
		int []cd;
		int classOfInstances[];
		
		/**
		 * <p>
		 * Compute the interval ratios.
		 * </p>
		 * @param _attribute attribute to compute the ratios
		 * @param _values different values of the attribute
		 * @param _begin begining index
		 * @param _end ending index
                 * @param classes number of instances per classes
		 */
		public Interval(int _attribute,int []_values,int _begin,int _end,int classes[]) {
			attribute=_attribute;
			begin=_begin;
			end=_end;
			values=_values;
			
			classOfInstances = new int[classes.length];
			for (int i=0; i<classes.length; i++) {
				classOfInstances[i] = classes[i];
			}

			computeIntervalRatios();
		}

		void computeIntervalRatios() {
			cd=classDistribution(attribute,values,begin,end);
		}
		
		/**
		 * <p>
		 * Enlarge the interval using a new "end"
		 * </p>
		 * @param newEnd indicates the new end
		 */
		public void enlargeInterval(int newEnd) {
			end=newEnd;
			computeIntervalRatios();
		}

		int []classDistribution(int attribute,int []values,int begin,int end) {
			int []classCount = new int[Parameters.numClasses];
			for(int i=0;i<Parameters.numClasses;i++) classCount[i]=0;

			for(int i=begin;i<=end;i++) classCount[classOfInstances[values[i]]]++;
			return classCount;	
		}
}

