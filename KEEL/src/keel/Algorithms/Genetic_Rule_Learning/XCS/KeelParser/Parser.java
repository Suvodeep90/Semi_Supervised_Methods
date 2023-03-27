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



/* Generated By:JavaCC: Do not edit this line. Parser.java */
package keel.Algorithms.Genetic_Rule_Learning.XCS.KeelParser;
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * <p> Parser class of the parameters for the algorithm XCS
 * @author Written by Albert Orriols (La Salle, Ramón Llull University - Barcelona) 28/03/2004
 * @author Modified by Xavi Solé (La Salle, Ramón Llull University - Barcelona) 03/12/2008
 * @version 1.1
 * @since JDK1.2
 * </p>
 */
public class Parser implements ParserConstants {


        static Parser keelParser = null;

        Parser() {
        }

      /**
	  * Main function 
	  * It receives as parameters:
	  *	. Name of input file 
	  */
  public static void doParse(String fileName) {
    try {
        FileInputStream f = null;
        try {
            f = new FileInputStream(fileName);
        }catch (FileNotFoundException e){
            System.out.println ("The input file '"+fileName+"' doesn't exist.");
            System.exit(0);
        }

        if (keelParser == null){
            keelParser = new Parser(f);
        }

        // Calling the axiom symbol 
        keelParser.ReInit(f);  // We have to reInit the parser.
        keelParser.ppal();

        // Closing the input file 
        f.close();

    } catch(Exception e) {
        // If any exception occurs, an error mesage is printed 
        e.printStackTrace(System.err);
    }
  }

//// PRODUCTIONS OF OUR GRAMAR




///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// INITIAL PRODUCTION OF THE GRAMMAR /////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void ppal() throws ParseException {
    readAlgorithm();
    readInputData();
    readOutputData();
    readParameters();
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void readAlgorithm() throws ParseException {
    jj_consume_token(ALGORITHM);
    jj_consume_token(EQUALS);
    jj_consume_token(IDENT);
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void readInputData() throws ParseException {
    jj_consume_token(INPUTDATA);
    jj_consume_token(EQUALS);
    jj_consume_token(CAD_CONST);
                                           Config.trainFile = getToken(0).image.substring (1, getToken(0).image.length() -1);
    moreInputData(1);
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void moreInputData(int num) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CAD_CONST:
      jj_consume_token(CAD_CONST);
                                        if (num == 2)   Config.testFile = getToken(0).image.substring (1, getToken(0).image.length() -1);
                                        else if (num == 3) Config.populationFile = getToken(0).image.substring (1, getToken(0).image.length() -1);
      moreInputData(num+1);
      break;
    default:
      jj_la1[0] = jj_gen;

    }
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void readOutputData() throws ParseException {
    jj_consume_token(OUTPUTDATA);
    jj_consume_token(EQUALS);
    jj_consume_token(CAD_CONST);
                                            Config.statisticFileOutName = getToken(0).image.substring (1, getToken(0).image.length() -1);
                                                                                Config.fOTrainFileName = getToken(0).image.substring (1, getToken(0).image.length() -1);
    moreOutputData(1);
  }

  static final public void moreOutputData(int num) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CAD_CONST:
      jj_consume_token(CAD_CONST);
                                 setOutputFile(num, getToken(0).image.substring (1, getToken(0).image.length() -1));
      moreOutputData(num+1);
      break;
    default:
      jj_la1[1] = jj_gen;

    }
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void readParameters() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TYPEOFPROBLEM:
    case NUMBEROFEXPLORES:
    case SEED:
    case EXPLORESBETWEENEXPLOITS:
    case XCSRUN:
    case POPSIZE:
    case ALPHA:
    case BETA:
    case GAMMA:
    case DELTA:
    case NU:
    case THETA_MNA:
    case THETA_DEL:
    case THETA_SUB:
    case EPSILON_0:
    case DOASSUBSUMPTION:
    case PREDICTIONERRORREDUCTION:
    case FITREDUCTION:
    case INITIALPREDICITON:
    case INITIALFITNESS:
    case INITIALPERROR:
    case PX:
    case PM:
    case THETA_GA:
    case DOGASUBSUMPTION:
    case TOURNAMENTSIZE:
    case TYPEOFMUTATION:
    case TYPEOFSELECTION:
    case TYPEOFCROSSOVER:
    case PERMITWITHINCROSSOVER:
    case PDONTCARE:
    case R_0:
    case L_0:
    case M_0:
    case DOSPECIFY:
    case PSPECIFY:
    case NSPECIFY:
    case DOSTATISTICS:
    case STATISTICWINDOWSIZE:
    case REALDRAWNPRECISION:
    case DOTEST:
    case TESTWINDOW:
    case SEQUENTIALTEST:
    case IDENT:
      readOneParameter();
      readParameters();
      break;
    default:
      jj_la1[2] = jj_gen;

    }
  }

  static final public void readOneParameter() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TYPEOFPROBLEM:
      jj_consume_token(TYPEOFPROBLEM);
      jj_consume_token(EQUALS);
      jj_consume_token(PROBLEMTYPE);
                                                 Config.typeOfProblem = getToken(0).image;
      break;
    case NUMBEROFEXPLORES:
      jj_consume_token(NUMBEROFEXPLORES);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                 Config.numberOfExplores = new Integer(getToken(0).image).intValue();
      break;
    case SEED:
      jj_consume_token(SEED);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                         Config.seed = new Long(getToken(0).image).longValue();
      break;
    case EXPLORESBETWEENEXPLOITS:
      jj_consume_token(EXPLORESBETWEENEXPLOITS);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                                                 Config.exploresBetweenExploits = (new Integer(getToken(0).image)).intValue();
      break;
    case XCSRUN:
      jj_consume_token(XCSRUN);
      jj_consume_token(EQUALS);
      RunType();
                                                         Config.XCSRun = getToken(0).image.toLowerCase();
      break;
    case POPSIZE:
      jj_consume_token(POPSIZE);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                 Config.popSize = new Integer(getToken(0).image).intValue();
      break;
    case ALPHA:
      jj_consume_token(ALPHA);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.alpha = new Double(getToken(0).image).doubleValue();
      break;
    case BETA:
      jj_consume_token(BETA);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.beta    = new Double(getToken(0).image).doubleValue();
      break;
    case GAMMA:
      jj_consume_token(GAMMA);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.gamma = new Double(getToken(0).image).doubleValue();
      break;
    case DELTA:
      jj_consume_token(DELTA);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.delta = new Double(getToken(0).image).doubleValue();
      break;
    case NU:
      jj_consume_token(NU);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.nu = new Double(getToken(0).image).doubleValue();
      break;
    case THETA_MNA:
      jj_consume_token(THETA_MNA);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                 Config.theta_mna = new Integer(getToken(0).image).intValue();
      break;
    case THETA_DEL:
      jj_consume_token(THETA_DEL);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.theta_del = new Double(getToken(0).image).doubleValue();
      break;
    case THETA_SUB:
      jj_consume_token(THETA_SUB);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.theta_sub = new Double(getToken(0).image).doubleValue();
      break;
    case EPSILON_0:
      jj_consume_token(EPSILON_0);
      jj_consume_token(EQUALS);
      RealConst();
                                                 Config.epsilon_0 = new Double(getToken(0).image).doubleValue();
      break;
    case DOASSUBSUMPTION:
      jj_consume_token(DOASSUBSUMPTION);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                                                         if (getToken(0).image.toLowerCase().equals("true"))
                                                                                                                                                                Config.doASSubsumption = true;
                                                                        else Config.doASSubsumption = false;
      break;
    case PX:
      jj_consume_token(PX);
      jj_consume_token(EQUALS);
      RealConst();
                                                                 Config.pX = new Double(getToken(0).image).doubleValue();
      break;
    case PM:
      jj_consume_token(PM);
      jj_consume_token(EQUALS);
      RealConst();
                                                                 Config.pM = new Double(getToken(0).image).doubleValue();
      break;
    case THETA_GA:
      jj_consume_token(THETA_GA);
      jj_consume_token(EQUALS);
      RealConst();
                                                         Config.theta_GA = new Double(getToken(0).image).doubleValue();
      break;
    case DOGASUBSUMPTION:
      jj_consume_token(DOGASUBSUMPTION);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                                                                 if (getToken(0).image.toLowerCase().equals("true")) Config.doGASubsumption = true;
                                                                                                 else Config.doGASubsumption = false;
      break;
    case TOURNAMENTSIZE:
      jj_consume_token(TOURNAMENTSIZE);
      jj_consume_token(EQUALS);
      RealConst();
                                                                 Config.tournamentSize = new Double(getToken(0).image).doubleValue();
      break;
    case TYPEOFMUTATION:
      jj_consume_token(TYPEOFMUTATION);
      jj_consume_token(EQUALS);
      jj_consume_token(MUTATIONTYPE);
                                                                 Config.typeOfMutation = getToken(0).image;
      break;
    case TYPEOFSELECTION:
      jj_consume_token(TYPEOFSELECTION);
      jj_consume_token(EQUALS);
      jj_consume_token(SELECTIONTYPE);
                                                                 Config.typeOfSelection = getToken(0).image;
      break;
    case TYPEOFCROSSOVER:
      jj_consume_token(TYPEOFCROSSOVER);
      jj_consume_token(EQUALS);
      jj_consume_token(CROSSOVERTYPE);
                                                                 Config.typeOfCrossover = getToken(0).image;
      break;
    case PERMITWITHINCROSSOVER:
      jj_consume_token(PERMITWITHINCROSSOVER);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                                                                         if (getToken(0).image.toLowerCase().equals("true")) Config.permitWithinCrossover = true;
                                                                                                         else Config.permitWithinCrossover = false;
      break;
    case PREDICTIONERRORREDUCTION:
      jj_consume_token(PREDICTIONERRORREDUCTION);
      jj_consume_token(EQUALS);
      RealConst();
                                                      Config.predictionErrorReduction = new Double(getToken(0).image).doubleValue();
      break;
    case FITREDUCTION:
      jj_consume_token(FITREDUCTION);
      jj_consume_token(EQUALS);
      RealConst();
                                                      Config.fitReduction = new Double(getToken(0).image).doubleValue();
      break;
    case INITIALPREDICITON:
      jj_consume_token(INITIALPREDICITON);
      jj_consume_token(EQUALS);
      RealConst();
                                                      Config.initialPrediction = new Double(getToken(0).image).doubleValue();
      break;
    case INITIALFITNESS:
      jj_consume_token(INITIALFITNESS);
      jj_consume_token(EQUALS);
      RealConst();
                                                      Config.initialFitness = new Double(getToken(0).image).doubleValue();
      break;
    case INITIALPERROR:
      jj_consume_token(INITIALPERROR);
      jj_consume_token(EQUALS);
      RealConst();
                                                      Config.initialPError = new Double(getToken(0).image).doubleValue();
      break;
    case PDONTCARE:
      jj_consume_token(PDONTCARE);
      jj_consume_token(EQUALS);
      RealConst();
                                              Config.pDontCare = new Double(getToken(0).image).doubleValue();
      break;
    case R_0:
      jj_consume_token(R_0);
      jj_consume_token(EQUALS);
      RealConst();
                                              Config.r_0 = new Double(getToken(0).image).doubleValue();
      break;
    case M_0:
      jj_consume_token(M_0);
      jj_consume_token(EQUALS);
      RealConst();
                                              Config.m_0 = new Double(getToken(0).image).doubleValue();
      break;
    case L_0:
      jj_consume_token(L_0);
      jj_consume_token(EQUALS);
      RealConst();
                                              Config.l_0 = new Double(getToken(0).image).doubleValue();
      break;
    case DOSPECIFY:
      jj_consume_token(DOSPECIFY);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                               if (getToken(0).image.toLowerCase().equals("true")) Config.doSpecify = true;
                                                                                   else Config.doSpecify = false;
      break;
    case PSPECIFY:
      jj_consume_token(PSPECIFY);
      jj_consume_token(EQUALS);
      RealConst();
                                           Config.Pspecify = new Double(getToken(0).image).doubleValue();
      break;
    case NSPECIFY:
      jj_consume_token(NSPECIFY);
      jj_consume_token(EQUALS);
      RealConst();
                                           Config.Nspecify = new Double(getToken(0).image).doubleValue();
      break;
    case DOSTATISTICS:
      jj_consume_token(DOSTATISTICS);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                                   if (getToken(0).image.toLowerCase().equals("true")) Config.doStatistics = true;
                                                else Config.doStatistics = false;
      break;
    case STATISTICWINDOWSIZE:
      jj_consume_token(STATISTICWINDOWSIZE);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                       Config.statisticWindowSize = new Integer(getToken(0).image).intValue();
      break;
    case REALDRAWNPRECISION:
      jj_consume_token(REALDRAWNPRECISION);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                         Config.realDrawnPrecision= new Integer(getToken(0).image).intValue();
      break;
    case DOTEST:
      jj_consume_token(DOTEST);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                             System.out.println ("Parsing doTest = "+getToken(0).image);
                                             if (getToken(0).image.toLowerCase().equals("true")) Config.doTest = true;
                                             else Config.doTest = false;
      break;
    case SEQUENTIALTEST:
      jj_consume_token(SEQUENTIALTEST);
      jj_consume_token(EQUALS);
      jj_consume_token(BOOLEAN_CONST);
                                                     if (getToken(0).image.toLowerCase().equals("true")) Config.sequentialTest = true;
                                                else Config.sequentialTest = false;
      break;
    case TESTWINDOW:
      jj_consume_token(TESTWINDOW);
      jj_consume_token(EQUALS);
      jj_consume_token(INT_CONST);
                                                 Config.testWindow = new Integer(getToken(0).image).intValue();
      break;
    case IDENT:
      jj_consume_token(IDENT);
                    Token t = getToken(0);
                    System.err.println("Token '"+t.image+"' unrecognized. Line: "+t.beginLine+", Col: "+t.beginColumn);
                    System.err.println("The configuration parameters parsing is stopped");
                    System.exit(0);
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static final public void RunType() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RUNTYPE:
      jj_consume_token(RUNTYPE);
      break;
    case TEST:
      jj_consume_token(TEST);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

   //end RunType
  static final public void RealConst() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT_CONST:
      jj_consume_token(INT_CONST);
      break;
    case REAL_CONST:
      jj_consume_token(REAL_CONST);
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static void write(String s) throws ParseException {
        System.out.println(s);
  }

  static void setOutputFile(int num, String fName) throws ParseException {
        switch (num){
                case 1:
                                Config.fOTestFileName = fName;
                                break;
                case 2:
                                //Config.fTimeFileName = fName; //change for educational
                				Config.fPopFileName = fName;
                                break;

                case 3:
                                //Config.fPopFileName = fName; //change for educational
                				Config.fTimeFileName = fName;
                                break;
                case 4:
                                Config.fPopNormFileName = fName;
                                break;
                case 5:
                                Config.fTrainFileName = fName;
                                break;
                case 6:
                                Config.fTestFileName = fName;
                                break;
                case 7:
                                Config.fIncFileName = fName;
                                break;
                case 8:
                                Config.fDrawFileName = fName;
                                break;

        }
  }

  static private boolean jj_initialized_once = false;
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  static public Token token, jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[6];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static private int[] jj_la1_3;
  static {
      jj_la1_0();
      jj_la1_1();
      jj_la1_2();
      jj_la1_3();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x0,0x0,0xf800000,0xf800000,0x80080000,0x0,};
   }
   private static void jj_la1_1() {
      jj_la1_1 = new int[] {0x0,0x0,0xd1ffffff,0xd1ffffff,0x0,0x0,};
   }
   private static void jj_la1_2() {
      jj_la1_2 = new int[] {0x0,0x0,0x3cbc1,0x3cbc1,0x0,0x0,};
   }
   private static void jj_la1_3() {
      jj_la1_3 = new int[] {0x40,0x40,0x80,0x80,0x0,0x24,};
   }

  public Parser(java.io.InputStream stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  static public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  static final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.Vector jj_expentries = new java.util.Vector();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  static public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[107];
    for (int i = 0; i < 107; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 6; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
          if ((jj_la1_3[i] & (1<<j)) != 0) {
            la1tokens[96+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 107; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  static final public void enable_tracing() {
  }

  static final public void disable_tracing() {
  }

   //end doParse

}
