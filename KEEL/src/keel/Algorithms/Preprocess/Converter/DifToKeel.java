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
 * DifToKeel.java
 */
package keel.Algorithms.Preprocess.Converter;

import keel.Dataset.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <b> DifToKeel </b>
 * </p>
 *
 * This class extends from the Importer class. It is used to read 
 * data with DIF format and transform them to the KEEL format.
 *
 * @author Teresa Prieto López (UCO)
 * @version 1.0
 */
public class DifToKeel extends Importer {

    /** DifToKeel class Constructor.
     * Initializes the variables that stores the symbols used to identify null 
     * values.
     *
     * @param  nullValueUser. Null value symbols.
     */
    public DifToKeel(String nullValueUser) {
        nullValue = nullValueUser;
    }

    /**
     * Method used to transform the data from the DIF file given as parameter to 
     * KEEL format file which will be stored in the second file given.
     *
     * @param pathnameInput DIF file path.
     * @param pathnameOutput KEEL file path.
     *
     * @throws Exception if the files can not be read or written.
     */
    public void Start(String pathnameInput, String pathnameOutput) throws Exception {
        BufferedReader reader;
        Pattern p;
        Matcher m;
        File f;
        StringTokenizer token;
        String element = new String();
        String line = new String();
        String filename = "tempOf";
        int i = 0;
        int j = 0;
        int type = -1;
        int numInstances = 0;
        int actualValueInt;
        double actualValue;
        double min;
        double max;

        File fileInput = new File(pathnameInput);
        filename = filename.concat(fileInput.getName());


        reader = new BufferedReader(new FileReader(pathnameInput));
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        while ((line = reader.readLine()) != null && line != "") {
            p = Pattern.compile("^\\s+");
            m = p.matcher(line);
            line = m.replaceAll("");

            p = Pattern.compile("\\s+$");
            m = p.matcher(line);
            line = m.replaceAll("");

            if (line != "") {
                writer.write(line + "\n");
            }
        }
        writer.close();
        reader.close();



        reader = new BufferedReader(new FileReader(filename));

        line = reader.readLine();

        while (line != null && !line.equalsIgnoreCase("EOD") && !line.equalsIgnoreCase("BOT")) {

            if (line.equalsIgnoreCase("VECTORS")) {
                line = reader.readLine();
                token = new StringTokenizer(line, ",");
                token.nextToken();
                numInstances = Integer.valueOf(token.nextToken());
            }

            if (line.equalsIgnoreCase("TUPLES")) {
                line = reader.readLine();
                token = new StringTokenizer(line, ",");
                token.nextToken();
                numAttributes = Integer.valueOf(token.nextToken());
            }

            line = reader.readLine();

        }


//Reservamos memoria para almacenar la definiciÃ³n de los atributos y de los datos

        attribute = new Attribute[numAttributes];
        data = new Vector[numAttributes];
        types = new Vector[numAttributes];

        for (i = 0; i < numAttributes; i++) {
            attribute[i] = new Attribute();
            data[i] = new Vector();
            types[i] = new Vector();
        }



        if (line.equalsIgnoreCase("BOT")) {

            i = 0;
            while (!(line = reader.readLine()).startsWith("-1")) {
                p = Pattern.compile("^\\s+");
                m = p.matcher(line);
                line = m.replaceAll("");

                p = Pattern.compile("\\s+$");
                m = p.matcher(line);
                line = m.replaceAll("");

                token = new StringTokenizer(line, ",");

                if ((token.nextToken()).equals("1")) {
                    element = reader.readLine();
                } else {
                    element = token.nextToken();

                    if (token.hasMoreTokens()) {
                        element = element.concat("." + token.nextToken());
                    }

                    reader.readLine();
                }

                element = element.replace("'", "");
                element = element.replace("\"", "");

                p = Pattern.compile("\\s+");
                m = p.matcher(element);
                element = m.replaceAll(" ");

                if (element.contains(" ")) {
                    StringTokenizer tokenUcfirts = new StringTokenizer(element, " ");
                    String lineUcfirts = "";
                    if (tokenUcfirts.hasMoreTokens()) {
                        lineUcfirts = tokenUcfirts.nextToken();
                    }
                    while (tokenUcfirts.hasMoreTokens()) {
                        lineUcfirts = lineUcfirts.concat(UcFirst(tokenUcfirts.nextToken()));
                    }

                    element = lineUcfirts;

                }


                if (element.equals("") || element.equals("?") || element.equals("<null>") || element.equals(nullValue)) {
                    element = "ATTRI_" + (i + 1) + "";
                }
                attribute[i].setName(element);

                i++;
            }//end while

        }//end if



        while (!(line = reader.readLine()).equalsIgnoreCase("EOD")) {
            p = Pattern.compile("^\\s+");
            m = p.matcher(line);
            line = m.replaceAll("");

            p = Pattern.compile("\\s+$");
            m = p.matcher(line);
            line = m.replaceAll("");

            if (line.equalsIgnoreCase("BOT")) {
                i = 0;
                while (!(line = reader.readLine()).startsWith("-1")) {
                    token = new StringTokenizer(line, ",");


                    if ((token.nextToken()).equals("1")) {
                        element = reader.readLine();
                    } else {
                        if (token.hasMoreTokens()) {
                            element = token.nextToken();
                        } else {
                            element = "?";
                        }
                        if (token.hasMoreTokens()) {
                            element = element.concat("." + token.nextToken());
                        }

                        reader.readLine();
                    }

                    element = element.replace("'", "");
                    element = element.replace("\"", "");

                    if (element.equals("") || element.equals("<null>") || element.equals(nullValue)) {
                        element = "?";
                    }
                    data[i].addElement(element);
                    i++;
                }
            }

        }

        reader.close();



// Asignamos el tipo de los atributos
        for (i = 0; i < data[0].size(); i++) {
            for (j = 0; j < numAttributes; j++) {
                element = (String) data[j].elementAt(i);
                types[j].addElement(DataType(element));
            }

        }


        for (i = 0; i < numAttributes; i++) {
            if (types[i].contains(NOMINAL)) {
                attribute[i].setType(NOMINAL);
            } else {
                if (types[i].contains(REAL)) {
                    attribute[i].setType(REAL);
                } else {
                    if (types[i].contains(INTEGER)) {
                        attribute[i].setType(INTEGER);
                    } else {
                        attribute[i].setType(-1);
                    }
                }
            }

        }


        for (i = 0; i < data[0].size(); i++) {

            for (j = 0; j < numAttributes; j++) {

                element = (String) data[j].elementAt(i);

                type = attribute[j].getType();


                if (type == NOMINAL) {
                    p = Pattern.compile("[^A-ZÃa-zÃ±0-9_-]+");
                    m = p.matcher(element);

                    /**
                     * Cambio hecho para que los nominales con espacios en blanco se dejen
                     * con subrayado bajo "_" y sin comillas simples. Se aÃ±ade la siguiente linea
                     */
                    element = element.replace(" ", "_");

                    if (m.find() && !element.equals("?")) {
                        /**
                         * Cambio hecho para que los nominales con espacios en blanco se dejen
                         * con subrayado bajo "_" y sin comillas simples. Se comenta la siguiente linea
                         */
                        /*
                        //element="'"+element+"'";
                         */
                        data[j].set(i, element);
                    }

                    if (!(attribute[j].isNominalValue(element)) && !element.equals("?")) {
                        attribute[j].addNominalValue(element);
                    }
                }


                if (type == INTEGER) {
                    if (!element.equals("?")) {
                        actualValueInt = Integer.valueOf(element);
                        data[j].set(i, actualValueInt);

                        if ((attribute[j].getFixedBounds()) == false) {
                            attribute[j].setBounds(actualValueInt, actualValueInt);
                        } else {
                            min = attribute[j].getMinAttribute();
                            max = attribute[j].getMaxAttribute();
                            if (actualValueInt < min) {
                                attribute[j].setBounds(actualValueInt, max);
                            }
                            if (actualValueInt > max) {
                                attribute[j].setBounds(min, actualValueInt);
                            }
                        }
                    }

                }

                if (type == REAL) {
                    if (!element.equals("?")) {
                        actualValue = Double.valueOf(element);
                        data[j].set(i, actualValue);

                        if ((attribute[j].getFixedBounds()) == false) {
                            attribute[j].setBounds(actualValue, actualValue);
                        } else {
                            min = attribute[j].getMinAttribute();
                            max = attribute[j].getMaxAttribute();
                            if (actualValue < min) {
                                attribute[j].setBounds(actualValue, max);
                            }
                            if (actualValue > max) {
                                attribute[j].setBounds(min, actualValue);
                            }
                        }
                    }
                }

            }//end for

        }//end while



        /* Insertamos el nombre de la relaciÃ³n que serÃ¡ el mismo que el del
         * fichero pasado, pero sin extensiÃ³n*/

        nameRelation = fileInput.getName();
        p = Pattern.compile("\\.[A-Za-z]+");
        m = p.matcher(nameRelation);
        nameRelation = m.replaceAll("");

        p = Pattern.compile("\\s+");
        m = p.matcher(nameRelation);
        nameRelation = m.replaceAll("");

        f = new File(filename);
        f.delete();

// Llamamos a save para que me transforme los datos almacenamos a formato keel
        super.Save(pathnameOutput);


    }
}//end DifToKeel()

