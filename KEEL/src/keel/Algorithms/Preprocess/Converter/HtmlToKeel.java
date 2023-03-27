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
 * HtmlToKeel.java
 */
package keel.Algorithms.Preprocess.Converter;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.*;
import org.jdom.input.*;
import org.w3c.tidy.Tidy;

/**
 * <p>
 * <b> HtmlToKeel </b>
 * </p>
 * This class extends from the Importer class. It is used to read 
 * data from a html table format and transform them to the KEEL format.
 *
 * @author Teresa Prieto López (UCO)
 * @version 1.0
 */
public class HtmlToKeel extends Importer {

//Variable auxiliar para almacenar el texto de todos los descendientes de un nodo.
    private String lineAux = new String();//Variable almacena el elemento o etiqueta principal que forma el documento xml.
    private Element root;

    /** HtmlToKeel class Constructor.
     * Initializes the variables that stores the symbols used to identify null 
     * values.
     *
     * @param  nullValueUser. Null value symbols.
     */
    public HtmlToKeel(String nullValueUser) {
        nullValue = nullValueUser;
    }

    /**
     * Method used to transform the data from the html file given as parameter to 
     * KEEL format file which will be stored in the second file given.
     *
     * @param pathnameInput html file path.
     * @param pathnameOutput KEEL file path.
     *
     * @throws Exception if the files can not be read or written.
     */
    public void Start(String pathnameInput, String pathnameOutput) throws Exception {
        Pattern p;
        Matcher m;
        File f;
        Element children;
        String nameChildren = new String();
        String value = new String();
        String fileXhtml = new String();
        int type = -1;
        int numElements = 0;
        int j = 0;
        int i = 0;
        int k = 0;
        int actualValueInt;
        double actualValue;
        double min;
        double max;
        List<Element> firstInstance;
        String vowel[] = {"a", "e", "i", "o", "u", "A", "E", "I", "O", "U"};
        String vowel_accent[] = {"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú"};

        File fileInput = new File(pathnameInput);


// Transformamos el fichero html a formato xhtml

        p = Pattern.compile("\\.[A-Za-z]+");
        m = p.matcher(fileInput.getName());
        fileXhtml = m.replaceAll("");
        fileXhtml = "tempOf" + fileXhtml + ".xthml";


        ConverterXhtml(pathnameInput, fileXhtml);

        try {

            SAXBuilder builder = new SAXBuilder(false);

            Document doc = builder.build(new File(fileXhtml));

            root = doc.getRootElement();

            FindParent(root, "tr");

//todos los hijos que tengan
            List elements = root.getChildren();

            numElements = elements.size();



//Calculamos el nÃºmero de hijos que tiene cada elemento
            if (numElements > 0) {
                i = 0;
                children = (Element) elements.get(i);
                nameChildren = children.getName();

                while (!nameChildren.equalsIgnoreCase("tr")) {
                    i++;
                    children = (Element) elements.get(i);
                    nameChildren = children.getName();
                }

                firstInstance = ((Element) elements.get(i)).getChildren();

            } else {
                System.out.println("No hay elementos");
                return;
            }

//Reservamos memoria para almacenar la definiciÃ³n de los atributos y de los datos
            numAttributes = firstInstance.size();
            attribute = new keel.Dataset.Attribute[numAttributes];
            data = new Vector[numAttributes];
            types = new Vector[numAttributes];

            for (i = 0; i < numAttributes; i++) {
                attribute[i] = new keel.Dataset.Attribute();
                data[i] = new Vector();
                types[i] = new Vector();
            }


            Iterator it = elements.iterator();


        if(processHeader){

// El primer hijo contiene el nombre de los atributos
            while (it.hasNext()) {
                children = (Element) it.next();
                nameChildren = (String) children.getName();

                if (nameChildren.equalsIgnoreCase("tr")) {

                    List instances = children.getChildren();
                    Iterator it_instance = instances.iterator();

                    j = 0;
                    while (it_instance.hasNext()) {
                        Element element_children = (Element) it_instance.next();
                        String element_name = (String) element_children.getName();

                        if (element_name.equalsIgnoreCase("td") || element_name.equalsIgnoreCase("th")) {

                            String nameAttribute = "";

                            nameAttribute = element_children.getText();

                            p = Pattern.compile("^\\s+");
                            m = p.matcher(nameAttribute);
                            nameAttribute = m.replaceAll("");

                            p = Pattern.compile("\\s+$");
                            m = p.matcher(nameAttribute);
                            nameAttribute = m.replaceAll("");


                            nameAttribute = nameAttribute.replace("'", "");
                            nameAttribute = nameAttribute.replace("\"", "");

                            nameAttribute = nameAttribute.replace("&nbsp;", "");
                            nameAttribute = nameAttribute.replace("&lt;", "<");
                            nameAttribute = nameAttribute.replace("&gt;", ">");
                            nameAttribute = nameAttribute.replace("&quot;", "\"");
                            nameAttribute = nameAttribute.replace("&shy;", "-");
                            nameAttribute = nameAttribute.replace("&amp;", "&");
                            nameAttribute = nameAttribute.replace("&lt;", "<");
                            nameAttribute = nameAttribute.replace("&gt;", ">");

                            for (k = 0; k < vowel.length; k++) {
                                nameAttribute = nameAttribute.replace("&" + vowel[k] + "acute;", vowel_accent[k]);
                            }
                            p = Pattern.compile("\\s+");
                            m = p.matcher(nameAttribute);
                            nameAttribute = m.replaceAll(" ");

                            p = Pattern.compile("\\s");
                            m = p.matcher(nameAttribute);
                            nameAttribute = m.replaceAll("");

                            if (nameAttribute.contains(" ")) {
                                StringTokenizer tokenUcfirts = new StringTokenizer(nameAttribute, " ");
                                String lineUcfirts = "";
                                if (tokenUcfirts.hasMoreTokens()) {
                                    lineUcfirts = tokenUcfirts.nextToken();
                                }
                                while (tokenUcfirts.hasMoreTokens()) {
                                    lineUcfirts = lineUcfirts.concat(UcFirst(tokenUcfirts.nextToken()));
                                }

                                nameAttribute = lineUcfirts;

                            }


                            if (nameAttribute.equals("") || nameAttribute.equals("?") || nameAttribute.equals("<null>") || nameAttribute.equals(nullValue)) {
                                nameAttribute = "ATTRIBUTE_" + (j + 1) + "";
                            }
                            attribute[j].setName(nameAttribute);
                            j++;

                        }//end if

                    }//end while

                }//end if

                if (nameChildren.equalsIgnoreCase("tr")) {
                    break;
                }
            }
        }
        else{
            for (i = 0; i < numAttributes; i++) {
                    attribute[i].setName("a" + i);
            }
        }



// Los demÃ¡s hijos contienen los datos
            while (it.hasNext()) {
                children = (Element) it.next();
                nameChildren = (String) children.getName();

                if (nameChildren.equalsIgnoreCase("tr")) {
                    List instances = children.getChildren();
                    Iterator it_instance = instances.iterator();

                    j = 0;
                    while (it_instance.hasNext()) {
                        Element element_children = (Element) it_instance.next();
                        String element_name = (String) element_children.getName();

                        if (element_name.equalsIgnoreCase("td") || element_name.equalsIgnoreCase("th")) {
                            value = "";

                            value = element_children.getText();

                            p = Pattern.compile("^\\s+");
                            m = p.matcher(value);
                            value = m.replaceAll("");

                            p = Pattern.compile("\\s+$");
                            m = p.matcher(value);
                            value = m.replaceAll("");

                            value = value.replace("&nbsp;", "");
                            value = value.replace("&lt;", "<");
                            value = value.replace("&gt;", ">");
                            value = value.replace("&quot;", "\"");
                            value = value.replace("&shy;", "-");
                            value = value.replace("&amp;", "&");
                            value = value.replace("&lt;", "<");
                            value = value.replace("&gt;", ">");

                            for (k = 0; k < vowel.length; k++) {
                                value = value.replace("&" + vowel[k] + "acute;", vowel_accent[k]);
                            }
                            if (value.equals("") || value.equals(" ") || value.equals("<null>") || value.equals(nullValue)) {
                                value = "?";
                            }
                            data[j].addElement(value);
                            j++;
                        }//end if


                    }//end while

                }//end if


            }//end while


            for (i = 0; i < data[0].size(); i++) {
                for (j = 0; j < numAttributes; j++) {
                    value = (String) data[j].elementAt(i);
                    types[j].addElement(DataType(value));
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

                    value = (String) data[j].elementAt(i);

                    type = attribute[j].getType();


                    if (type == NOMINAL) {
                        p = Pattern.compile("[^A-ZÃa-zÃ±0-9_-]+");
                        m = p.matcher(value);
                        /**
                         * Cambio hecho para que los nominales con espacios en blanco se dejen
                         * con subrayado bajo "_" y sin comillas simples. Se aÃ±ade la siguiente linea
                         */
                        value = value.replace(" ", "_");

                        if (m.find() && !value.startsWith("'") && !value.endsWith("'") && !value.equals("?")) {
                            /**
                             * Cambio hecho para que los nominales con espacios en blanco se dejen
                             * con subrayado bajo "_" y sin comillas simples. Se comenta la siguiente linea
                             */
                            /*
                            //value="'"+value+"'";
                             */
                            data[j].set(i, value);
                        }


                        if (!(attribute[j].isNominalValue(value)) && !value.equals("?")) {
                            attribute[j].addNominalValue(value);
                        }
                    }


                    if (type == INTEGER) {
                        if (!value.equals("?")) {
                            actualValueInt = Integer.valueOf(value);
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
                        if (!value.equals("?")) {
                            actualValue = Double.valueOf(value);
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

                }//end while

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


            f = new File(fileXhtml);
            f.delete();


            super.Save(pathnameOutput);


        } catch (Exception e) {

            f = new File(fileXhtml);
            f.delete();

            System.out.println(e);
            System.exit(1);
        }

    }

    
    /**
     * Recursive method that returns the text containing all the descendants of a xml tag.
     * @param current actual node or xml tag.
     * @param cont  descendants counter.
     * @return the text containing all the descendants of a xml tag, separated by blanks.
     */
    public String ListChildrenText(Element current, int cont) {

        if (cont == 0) {
            lineAux = "";
        }
        if ((current.getChildren()).size() == 0) {
            lineAux = lineAux.concat(current.getText() + " ");
        }
        List children = current.getChildren();
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();
            ListChildrenText(child, cont++);
        }

        return lineAux;

    } //end listChildrenText()


    /**
     * Method used to transform the data from the html file given as parameter to 
     * xhtml format file which will be stored in the second file given.
     * @param fileHtml input html file path.
     * @param fileXhtmlAux output xhtml file path.
     * @throws Exception if the files can not be read or written.
     */
    public void ConverterXhtml(String fileHtml, String fileXhtmlAux) throws Exception {

        Tidy tidy = new Tidy();

        FileInputStream in = new FileInputStream(fileHtml);
        FileOutputStream out = new FileOutputStream(fileXhtmlAux);

        tidy.setTidyMark(false);
        tidy.setDocType("omit");
        tidy.setAltText("");
        tidy.setFixBackslash(true);
        tidy.setFixComments(true);
        tidy.setXmlPi(true);
        tidy.setQuoteAmpersand(true);
        tidy.setQuoteNbsp(true);
        tidy.setNumEntities(true);
        tidy.setXmlOut(true);
        tidy.setWraplen(999);
        tidy.setWriteback(true);
        tidy.setQuoteMarks(true);
        tidy.setLogicalEmphasis(true);
        tidy.setEncloseText(true);
        tidy.setHideEndTags(true);
        tidy.setShowWarnings(false);
        tidy.setQuiet(true);
        tidy.setXHTML(true);
        tidy.parse(in, out);

        in.close();
        out.close();


    }//end ConverterXhtml()


    /**
     * Searchs in the whole xml tree to find the parent of the node or xaml tag whose name is 
     * equals to the one given as parameter. The node found will be assigned to the member variable "root".
     * @param current actual node or xml tag.
     * @param childrenName name node asked.
     */
    public void FindParent(Element current, String childrenName) {

        if (current.getName().equalsIgnoreCase(childrenName)) {
            this.root = current.getParentElement();
            return;
        } else {
            List children = current.getChildren();
            Iterator iterator = children.iterator();
            while (iterator.hasNext()) {
                Element child = (Element) iterator.next();
                FindParent(child, childrenName);
            }

        }

    } //end FindParent()
}//end class HtmlToKeel()

