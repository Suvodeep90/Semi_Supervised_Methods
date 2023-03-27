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

package keel.Algorithms.Decision_Trees.DT_GA;



import org.core.Randomize;

/**
 * <p>Title: Selector (Selector). </p>
 *
 * <p>Description: This class implements a Selector or Antecedent of a rule (An attribute with a given condition). </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Selector {

  int atributo; //posicion del atributo/variable en el dataset
  int operador; // =, <= ó >

    /**
     * Identifier for the equal condition operator.
     */
    public static int IGUAL = 0;

    /**
     * Identifier for the lesser-equal condition operator.
     */
    public static int MENOR_IGUAL = 1;

    /**
     * Identifier for the greater condition operator.
     */
    public static int MAYOR = 2;
  double valor; //si el atributo es real
  String valoresNom[]; //si el atributo es nominal (para imprimir)
  double valores[]; //si el atributo es nominal (para comprobar)
  String nombreAtributos[];
  myDataset train;

    /**
     * Default Constructor. Basic structures will be initialized.
     */
    public Selector() {
  }

        /**
     * Paramenter constructor. The structures will be initialized with the parameters given.
     * @param atributo attribute of the antecedent.
     * @param operador condition operator. 
     * @param valor condition value.
     */
  public Selector(String atributo, String operador, String valor) {
    //atributo es del tipo AttX con X == posicion del atributo
    String numero = new String("" + atributo.charAt(3));
    if (atributo.length() > 4) {
      numero += atributo.charAt(4);
    }
    this.atributo = Integer.parseInt(numero);
    valoresNom = new String[1];
    valores = new double[1];
    if (operador.equalsIgnoreCase("=")) {
      this.operador = IGUAL;
      valoresNom[0] = valor;
      valores[0] = myDataset.valorReal(this.atributo,valor);
    }
    else if (operador.equalsIgnoreCase("<=")) {
      this.operador = MENOR_IGUAL;
      this.valor = Double.parseDouble(valor);
    }
    else if (operador.equalsIgnoreCase(">")) {
      this.operador = MAYOR;
      this.valor = Double.parseDouble(valor);
    }
    else {
      System.err.println("There was an error in the parsing of the tree");
      System.exit(0);
    }
  }
  
        /**
     * Paramenter constructor. The structures will be initialized randomly with the parameters given.
     * @param atributo attribute of the antecedent.
     * @param train training dataset.
     */
  public Selector(int atributo, myDataset train){
    this.train = train;
    this.atributo = atributo;
    adjuntaNombres(train.nombres());
    if (train.getTipo(atributo) == train.NOMINAL){
      this.operador = IGUAL;
      int totalNominales = train.totalNominales(atributo);
      int nominalesEscogidos = Randomize.RandintClosed(1,totalNominales);
      valoresNom = new String[nominalesEscogidos];
      valores = new double[nominalesEscogidos];
      int [] noSeleccionados = new int[totalNominales];
      for (int i = 0; i < totalNominales; i++){
        noSeleccionados[i] = i;
      }
      for (int i = 0; i < valoresNom.length; i++){
        int seleccion = Randomize.RandintClosed(0,totalNominales-1);
        valores[i] = 1.0*noSeleccionados[seleccion];
        valoresNom[i] = train.valorNominal(atributo,valores[i]);
        noSeleccionados[seleccion] = noSeleccionados[totalNominales-1];
        totalNominales--;
      }
    }else{
      valoresNom = new String[1];
      valores = new double[1];
      this.operador = Randomize.RandintClosed(this.MENOR_IGUAL, this.MAYOR);
      int ejemplo = Randomize.RandintClosed(0, train.size()-1);
      this.valor = train.getExample(ejemplo)[atributo];
    }
  }

    /**
     * Sets the attributes names with the given ones.
     * @param atributos Attributes names.
     */
    public void adjuntaNombres(String[] atributos) {
    nombreAtributos = new String[atributos.length];
    nombreAtributos = atributos.clone();
  }

    /**
     * Returns String representation of the Selector.
     * @return String representation of the Selector.
     */
    public String printString() {
    String cadena = new String("");
    cadena += " " + nombreAtributos[atributo];
    if (operador == IGUAL) {
      cadena += " = {";
      int i;
      for (i = 0; i < valores.length - 1; i++) {
        cadena += valoresNom[i] + ", ";
      }
      cadena += valoresNom[i] + "} ";
    }
    else if (operador == MENOR_IGUAL) {
      cadena += " <= " + valor + " ";
    }
    else {
      cadena += " > " + valor + " ";
    }
    return cadena;
  }

    /**
     * Returns a copy of the Selector.
     * @return a copy of the Selector.
     */
    public Selector copia(){
    Selector s = new Selector();
    s.atributo = atributo;
    s.operador =  operador; // =, <= ó >
    s.valor = valor;
    s.valoresNom = new String[valoresNom.length];
    s.valoresNom = valoresNom.clone();
    s.valores = new double[valores.length];
    s.valores = valores.clone();
    s.nombreAtributos = new String[nombreAtributos.length];
    s.nombreAtributos = nombreAtributos.clone();
    s.train = this.train;
    return s;
  }

    /**
     * Checks if an example given is covered by the selector.
     * @param ejemplo given example.
     * @return True if an example given is covered by the selector.
     */
  public boolean cubre(double[] ejemplo) {
    boolean cubierto = false;
    if (this.operador == IGUAL) {
      for (int i = 0; i < valores.length; i++) { //Si es igual a alguno
        cubierto = cubierto || (ejemplo[atributo] == valores[i]);
      }
    }
    else if (this.operador == MENOR_IGUAL){
      cubierto = ejemplo[atributo] <= valor;
    }else{
      cubierto = ejemplo[atributo] > valor;
    }
    return cubierto;
  }

  /**
   * Modifies slightly the selector to cover the given example. 
   * @param ejemplo double[] given example.
   */
  public void modifica(double [] ejemplo){
    if (this.operador == IGUAL){
      double [] aux = valores.clone();
      String [] auxNom = valoresNom.clone();
      valores = new double[aux.length+1];
      valoresNom = new String[auxNom.length+1];
      int i;
      for (i = 0; i < aux.length; i++){
        valores[i] = aux[i];
        valoresNom[i] = auxNom[i];
      }
      valores[i] = ejemplo[atributo];
      valoresNom[i] = train.valorNominal(atributo, valores[i]);
    }else if (this.operador == MENOR_IGUAL){
      valor = ejemplo[atributo];
    }else{
      valor = ejemplo[atributo]-1.0;//uffff
    }
  }

    /**
     * Returns the antecedent attribute of this selector.
     * @return the antecedent attribute.
     */
    public int getAtributo(){
    return atributo;
  }

  /**
   * Mutates the selector by chaging the condition value.
   */
  public void mutar(){
    if (operador == this.IGUAL){
      int totalNominales = train.totalNominales(atributo);
      int nominalesEscogidos = Randomize.RandintClosed(1,totalNominales);
      valoresNom = new String[nominalesEscogidos];
      valores = new double[nominalesEscogidos];
      int [] noSeleccionados = new int[totalNominales];
      for (int i = 0; i < totalNominales; i++){
        noSeleccionados[i] = i;
      }
      for (int i = 0; i < valoresNom.length; i++){
        int seleccion = Randomize.RandintClosed(0, totalNominales-1);
        valores[i] = 1.0 * noSeleccionados[seleccion];
        valoresNom[i] = train.valorNominal(atributo, valores[i]);
        noSeleccionados[seleccion] = noSeleccionados[totalNominales - 1];
        totalNominales--;
      }
    }else{
      int ejemplo = Randomize.RandintClosed(0, train.size()-1);
      this.valor = train.getExample(ejemplo)[atributo];
    }
  }
}

