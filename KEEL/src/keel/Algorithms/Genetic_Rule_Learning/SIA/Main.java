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

package keel.Algorithms.Genetic_Rule_Learning.SIA;

import java.util.StringTokenizer;
import org.core.Files;

/**
 * <p>Title: SIA Main Program</p>
 * <p>Description: This is the main class, which is executed when we launch the program</p>
 * @author Written by Alberto Fernández (University of Granada) 02/13/2005
 * @version 1.0
 * @since JDK1.4
 */
public class Main {

  private String ficheroTrain;
  private String ficheroEval;
  private String ficheroTest;
  private String ficheroSalidatr;
  private String ficheroSalidatst;
  private String ficheroSalida;
  private long semilla;
  private int nb;
  private double alfa, beta;
  private double Tstr;

  /**
   * Default builder
   * */
  public Main() {
  }

  /**
   * It obtains all the neccesary information of the parameter file<br/>
   * First, it reads the names of the training and tests data-set files<br/>
   * Then, it reads the output files<br/>
   * Finally, it reads the algorithm parameters, such as the seed or the number of iterations<br/>
   *
   * @param nomFichero Name of the parameter file
   *
     */
  private void preparaArgumentos(String nomFichero) {
    StringTokenizer linea, datos;
    String fichero = Files.readFile(nomFichero);
    String una_linea;
    linea = new StringTokenizer(fichero, "\n\r");
    linea.nextToken(); //Algorithm's name
    una_linea = linea.nextToken(); //Leo una linea
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //inputData
    ficheroTrain = datos.nextToken();
    ficheroEval = datos.nextToken();
    ficheroTest = datos.nextToken();
    una_linea = linea.nextToken();
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //outputData
    ficheroSalidatr = datos.nextToken();
    ficheroSalidatst = datos.nextToken();
    ficheroSalida = datos.nextToken();
    una_linea = linea.nextToken();
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //seed
    semilla = Long.parseLong(datos.nextToken());
    una_linea = linea.nextToken();
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //nb
    nb = Integer.parseInt(datos.nextToken());

    una_linea = linea.nextToken();
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //alfa
    alfa = Double.parseDouble(datos.nextToken());

    una_linea = linea.nextToken();
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //beta
    beta = Double.parseDouble(datos.nextToken());

    una_linea = linea.nextToken();
    datos = new StringTokenizer(una_linea, " = \" ");
    datos.nextToken(); //Tstr
    Tstr = Double.parseDouble(datos.nextToken());

  };

  /**
   * It launches the SIA algorithm
     */
  private void execute() {
    SIA sia = new SIA(ficheroTrain, ficheroEval, ficheroTest, ficheroSalidatr,
                      ficheroSalidatst, ficheroSalida, semilla, nb, alfa, beta,
                      Tstr);
    sia.lanzar();
  }

  /**
   * Main program
   * @param args It contains the name of the parameter file<br/>
   * Format:<br/>
   * <em>algorith = &lt;algorithm name&gt;</em><br/>
   * <em>inputData = "&lt;training file&gt;" "&lt;validation file&gt;" "&lt;test file&gt;"</em> ...<br/>
   * <em>outputData = "&lt;training file&gt;" "&lt;test file&gt;"</em> ...<br/>
   * <br/>
   * <em>seed = value</em><br/>
   * <em>&lt;Description1&gt; = &lt;value1&gt;</em><br/>
   * <em>&lt;Description2&gt; = &lt;value2&gt;</em> ...<br/>
     */
  public static void main(String[] args) {
      long t_ini = System.currentTimeMillis();
      Main program = new Main();
      program.preparaArgumentos(args[0]);
      System.out.println("Executing CN2.");
      program.execute();
      long t_fin = System.currentTimeMillis();
      long t_exec = t_fin - t_ini;
      long hours = t_exec/3600000;
      long rest = t_exec%3600000;
      long minutes = rest/60000;
      rest %= 60000;
      long seconds = rest/1000;
      rest %=1000;
        System.out.println("Execution Time: "+ hours + ":" + minutes + ":" + seconds + "." + rest);
  }

}
