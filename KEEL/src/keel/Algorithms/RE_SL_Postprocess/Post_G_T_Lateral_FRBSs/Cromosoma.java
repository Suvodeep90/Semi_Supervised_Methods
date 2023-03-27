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

package keel.Algorithms.RE_SL_Postprocess.Post_G_T_Lateral_FRBSs;
/**
 * the class which contains the characteristics of the chromosome
 * @author Diana Arquillos
 */
public class Cromosoma {
	private double [] Gene;	
	private double Perf;
	private int HaEntrado;
	
        /**
	 * Get the value of a gene
	 *
	 * @param pos Index of the gene
	 *
	 * @return Value of the especified gene
	 */
	public double gene(int pos){
		return Gene[pos];
	}
	
        /**
	 * Get the performance of a chromosome
	 *
	 * @return performance of the chromosome
	 */
	public double perf(){
		
		return Perf;
	}
        
        /**
	 * Returns the values of all genes
	 *
	 * @return the values of all genes
	 */
	public double [] Gene(){
		
		return Gene;
	}
        
        /**
	 * Set the performance of a chromosome
	 *
         * @param value the performance to be set.
	 */
	public void set_perf(double value){
		Perf=value;
		
	}
        
        /**
	 * Returns the flag value that carry the information about if this chromosome has been selected to be crossed or not.
	 * @return the flag value (1 = has been selected for the crossover, 0 = not selected yet)
	 */
	public int entrado(){
		
		return HaEntrado;
	}
        
        /**
	 * Sets the flag value that carry the information about if this chromosome has been selected to be crossed or not.
	 * @param value the flag value to set (1 = has been selected for the crossover, 0 = not selected yet)
	 */
	public void set_entrado(int value){
		HaEntrado=value;
		
	}
        /**
         * Creates a new chromosome with the size given.
         * @param Genes number of genes or size of the chromosome representation.
         */
	public Cromosoma(int Genes) {
        Gene = new double [Genes];
    }
        /**
         * Set the value given to a given gene
         * @param pos position of the gene to be modified.
         * @param value given value to be set
         */
	public void set_gene(int pos , double value){
		Gene[pos]=value;
	}
	
}

