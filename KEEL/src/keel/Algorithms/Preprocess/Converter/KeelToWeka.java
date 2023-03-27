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
 * KeelToWeka.java
 */
package keel.Algorithms.Preprocess.Converter;

import keel.Dataset.*;
import java.io.FileWriter;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <b> KeelToWeka </b>
 * </p>
 *
 * This class extends from the Exporter class. It is used to read 
 * data with KEEL format and transform them to the Weka format.
 *
 * @author Teresa Prieto López (UCO)
 * @version 1.0
 */
public class KeelToWeka extends Exporter {


    /** KeelToWeka class Constructor.
     * Initializes the variable that stores the symbols used to identify null 
     * values.
     */
    public KeelToWeka() {
        nullValue = "<null>";
    }


    /**
     * Method used to transform the data from the KEEL file given as parameter to 
     * Weka format file which will be stored in the second file given. It calls the method
     * Start of its super class Exporter and then call the method Save.
     *
     * @param pathnameInput KEEL file path.
     * @param pathnameOutput Weka file path.
     *
     * @throws Exception if the files can not be read or written.
     */
    public void Start(String pathnameInput, String pathnameOutput) throws Exception {

        super.Start(pathnameInput);

        Save(pathnameOutput);

    }//end Start()

    /**
     * Method that creates the output file with Weka format given as parameter 
     * using all the structures built by the start method of the Exporter class.  
     * @param pathnameOutput Weka file path to generate.
     * @throws Exception if the file can not be written.
     */
    public void Save(String pathnameOutput) throws Exception {
        Attribute attributeCurrent = new Attribute();
        String filename = pathnameOutput;
        String nameAttribute = new String();
        String ending = new String();
        String element = new String();
        int i;
        int j;
        int type;
        int numNominalValues;
        double max;
        double min;


        /* Comprobamos si el nombre del fichero tiene la extensiÃ³n .dat, si no la tiene
         * se la ponemos */
        if (pathnameOutput.endsWith(".arff")) {
            filename = pathnameOutput;
        } else {
            filename = pathnameOutput.concat(".arff");
        }
        FileWriter fileWriter = new FileWriter(filename);

        fileWriter.write("@relation " + nameRelation + "\n");

        for (i = 0; i < numAttributes; i++) {
            attributeCurrent = attribute[i];
            nameAttribute = attributeCurrent.getName();
            min = attributeCurrent.getMinAttribute();
            max = attributeCurrent.getMaxAttribute();
            type = attributeCurrent.getType();

            String aux = "@attribute " + nameAttribute;

            switch (type) {
                case 0:
                    numNominalValues = attributeCurrent.getNumNominalValues();

                    if (numNominalValues < 10) {
                        aux += " {";
                        ending = ",";
                        for (j = 0; j < numNominalValues; j++) {
                            if (j == attributeCurrent.getNumNominalValues() - 1) {
                                ending = "";
                            }
                            aux += (String) attributeCurrent.getNominalValue(j) + ending;
                        }
                        aux += '}';
                    } else {
                        aux += " STRING";
                    }
                    break;
                case 1:
                    if (min != 0.0 && max != 0.0) {
                        aux += " INTEGER [" + (new Integer((int) min)).toString();
                        aux += "," + (new Integer((int) max)).toString() + "]";
                    } else {
                        aux += " INTEGER";
                    }
                    break;
                case 2:
                    if (min != 0.0 && max != 0.0) {
                        aux += " NUMERIC [" + (new Double(min)).toString();
                        aux += "," + (new Double(max)).toString() + "]";
                    } else {
                        aux += " NUMERIC";
                    }
                    break;

            }

            fileWriter.write(aux + "\n");
        }


        fileWriter.write("@data" + "\n");

        for (i = 0; i < data[0].size(); i++) {

            for (j = 0; j < numAttributes; j++) {
                element = (String) data[j].elementAt(i);

                Pattern p = Pattern.compile("[^A-ZÃa-zÃ±0-9_-]+");
                Matcher m = p.matcher(element);

                if (m.find() && !element.equals("?") && !element.equals(nullValue) && attribute[j].getType() == NOMINAL) {
                    element = "'" + element + "'";
                }
                if (j == (numAttributes - 1)) {
                    fileWriter.write(element + "");
                } else {
                    fileWriter.write(element + ",");
                }
            }

            fileWriter.write("\n");
        }

        fileWriter.close();

        File f = new File(filename);

        System.out.println("Fichero " + f.getName() + " creado correctamente");

    }//end Save()
}//end class KeelToWeka


