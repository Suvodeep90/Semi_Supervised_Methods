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


package keel.Algorithms.Statistical_Tests.Regression.Contrast;

import keel.Algorithms.Statistical_Tests.Shared.*;
import keel.Algorithms.Shared.Parsing.*;

/**
 * File: Main.java
 * 
 * A Main class to process the paramethers of the method and launch the test
 * 
 * @author Written by Joaquín Derrac (University of Granada) 29/04/2010
 * @version 1.1 
 * @since JDK1.5
*/
public class Main {
	
	/**
	* Main method
	*
	* @param args Arguments of the program
	*/
    public static void main(String args[]) {

        boolean tty = false;
        ProcessConfig pc = new ProcessConfig();
        System.out.println("Reading configuration file: " + args[0]);
        if (pc.fileProcess(args[0]) < 0) {
            return;
        }

        ParseFileList pl = new ParseFileList();

        pl.statisticalTest(StatTest.ContrastR, tty, pc);

    }//end-method
	
}//end-class

