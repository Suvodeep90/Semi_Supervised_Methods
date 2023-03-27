/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010

	F. Herrera (herrera@decsai.ugr.es)
    L. SÃ¡nchez (luciano@uniovi.es)
    J. AlcalÃ¡-Fdez (jalcala@decsai.ugr.es)
    S. GarcÃ­a (sglopez@ujaen.es)
    A. FernÃ¡ndez (alberto.fernandez@ujaen.es)
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

package keel.Algorithms.Semi_Supervised_Learning.Basic;

import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeGenerator;
import keel.Algorithms.Semi_Supervised_Learning.Basic.PrototypeSet;
import keel.Algorithms.Semi_Supervised_Learning.utilities.Distance;
import keel.Algorithms.Semi_Supervised_Learning.utilities.Parameters;
import keel.Algorithms.Semi_Supervised_Learning.utilities.KNN.*;
import keel.Algorithms.Semi_Supervised_Learning.utilities.*;
import keel.Dataset.InstanceSet;

import java.util.*;
import org.core.*;

/**
 * Implements a generic Semi-supervised method
 * @author Isaac Triguero 
 */
public class PrototypeGenerator {
    
    /** Original training data set to be condensed */
    public static InstanceSet Instancetrain;
    /** Original test data set to be condensed */
    public static InstanceSet Instancetest;
    
    /**
     * Training prototype set.
     */
    protected PrototypeSet trainingDataSet;
    
    /**
     * Transductive prototype set.
     */
    protected PrototypeSet transductiveDataSet;
    
    /**
     * Test prototype set.
     */
    protected PrototypeSet testDataSet;
    
    /** Condensed data */
    protected Pair<PrototypeSet,PrototypeSet> generatedDataSet = null;
    
    /** Name of the reduction tecnique */
    protected String algorithmName = "1NN";
   
    /** Default seed value to the Random Number Generator */
    public static long SEED = 7161592756342978231L;
    
    /** Default seed list to the Random Number Generator */
    public final long[] seedDefaultValueList = {7161592756342978231L, 4132097864321L, 1498764321724L, 897043219780L, 24397804321L};
    
    /** Index of the seed value in the parameters of the algorithm */
    //protected static final int ISEED = 0;
    
    /** Start time of the algoritm */
    private long _time = 0;

    /**
     * Init the timer
     */
    private void startTimer() {
        _time = System.nanoTime();
    }

    /**
     * Stop and get the timer
     * @return Time elapsed.
     */
    private long stopTimer() {
        _time = System.nanoTime() - _time;
        return _time;
    }

    /**
     * Set the seed of the random generator.
     * @param _seed Seed value.
     */
    public static void setSeed(long _seed)
    {
        SEED = _seed;
        RandomGenerator.setSeed(_seed);
    }
    
    
    /**
     * Sets the training instances set given.
     * @param uno the training instances set to set.
     */
    public static void setInstanceTrain(InstanceSet uno)
    {
        Instancetrain = new InstanceSet(uno);
    }
    
     /**
     * Sets the test instances set given.
     * @param uno the test instances set to set.
     */
    public static void setInstanceTest(InstanceSet uno)
    {
        Instancetest = new InstanceSet(uno);
    }
    
    /**
     * Get the seed of the random generator.
     * @return Seed value of the random generator.
     */
    public static long getSeed()
    {
        return SEED;
    }

    /**
     * Tell the time elapsed.
     * @return Time elapsed.
     */
    public long getTime() {
        return _time;
    }
    
    /**
     * Number of prototypes corresponding of the desired percentage of the reduced set.
     * @param percentage Percentage of size that will have reduced set.
     * @return Number of prototypes corresponding of the desired percentage of the reduced set.
     * 
     */
    protected int getSetSizeFromPercentage(double percentage)
    {
        return PrototypeGenerator.getSetSizeFromPercentage(trainingDataSet, percentage);
    }
    
    /**
     * Number of prototypes corresponding of the desired percentage of the reduced set.
     * @param set Original prototype set.
     * @param percentage Percentage of size that will have reduced set.
     * @return Number of prototypes corresponding of the desired percentage of the reduced set.
     *
     */
    protected static int getSetSizeFromPercentage(PrototypeSet set, double percentage)
    {
        int size = (int)Math.floor((set.size())*percentage/100.0);
        //Debug.errorln(percentage +"% of " + set.size() + " is " + size);
        return size;
    }
        
    /**
     * Extract a random prototype set of the original traning data set.
     * @param numberOfPrototypesSelected Number of prototypes extracted.
     * @param usePriorProb Use the a priori probabilites of the set.
     * @return  Set which have the specified number of prototypes randomly selected.
     */
    public PrototypeSet selecRandomSet(int numberOfPrototypesSelected, boolean usePriorProb) {
        //Debug.errorln("selecRandomSet");
        //Debug.errorln("num " + numberOfPrototypesSelected);
        //Debug.errorln("size " + trainingDataSet.size());
        //No tiene sentido usar las probabilidades a priori si seleccionamos todo el conjunto
        if (usePriorProb  &&  numberOfPrototypesSelected != trainingDataSet.size()) {
            int numberOfInstances_1 = trainingDataSet.size() - 1;
            int _size = trainingDataSet.size();
            double prop = numberOfPrototypesSelected / (double) (_size);
            PrototypeSet edited = new PrototypeSet();

            HashMap<Double, Integer> sizeOfPartition = trainingDataSet.countPrototypesOfEachOutput();

            ArrayList<Double> values = Prototype.possibleValuesOfOutput();
            for (double class_i : values) {
                int n_class_i = (int) Math.floor(prop * sizeOfPartition.get(class_i));
                //System.out.println("Clase " + class_i + " tiene " + n_class_i + " protos");
                HashSet<Integer> forbidden = new HashSet<Integer>();
                int k = 0;
                while (k < n_class_i) {
                    int chosen;
                    do {
                        chosen = RandomGenerator.Randint(0, numberOfInstances_1);
                        //System.err.println("CHOSEN: " + chosen);
                    } while (forbidden.contains(chosen) || trainingDataSet.get(chosen).firstOutput() != class_i);
                    forbidden.add(chosen);
                    //System.out.println("tam_" + k + ": " + edited.size());
                    edited.add(trainingDataSet.get(chosen));
                    ++k;
                }
            }
            HashSet<Integer> forbidden = new HashSet<Integer>();
            //Le metemos prototipos aleatorios mientras no se cumpla que se tiene
            //el nÃºmero de prototipos requerido
            while (edited.size() < numberOfPrototypesSelected) {
                int chosen;
                do
                {
                    chosen = RandomGenerator.Randint(0, numberOfInstances_1);
                   // System.err.println("CHOSEN_EXTRA: " + chosen);
                }
                while (forbidden.contains(chosen) || edited.contains(trainingDataSet.get(chosen)));
                forbidden.add(chosen);
                edited.add(trainingDataSet.get(chosen));
            }
            //Debug.errorln("end selecRandomSet");
            return edited;
        }
        else
        {
            PrototypeSet edited = new PrototypeSet(numberOfPrototypesSelected);
            RandomGenerator.generateDifferentRandomIntegers(0, trainingDataSet.size());
            ArrayList<Integer> indexes =  RandomGenerator.generateDifferentRandomIntegers(0, trainingDataSet.size()-1);
            for (int i=0; i< numberOfPrototypesSelected;i++){
                edited.add(trainingDataSet.get(indexes.get(i)));
                //System.out.println("i =" + indexes.get(i));
            } //Debug.errorln("end selecRandomSet");
            return edited;            
        }
        
    }

    /**
     * Makes the trivial reduction. That is none reduction of the set
     * @return A copy of the training data set
     * @throws java.lang.Exception if the algorithm can not be applied.
     */
    public Pair<PrototypeSet,PrototypeSet> applyAlgorithm() throws Exception  {
        return null;
    }

    /**
     * Execute the reduction of the data set. Note that this function, <i>au contraire</i> that reduceSet, uses timers.
     * @return Reduced data set.
     * @throws java.lang.Exception if the algorithm can not be executed.
     */
    public final Pair<PrototypeSet,PrototypeSet> execute() throws Exception {
        startTimer();
        Pair<PrototypeSet,PrototypeSet> resultSet = applyAlgorithm();
        stopTimer();
        generatedDataSet = resultSet;
        return resultSet;
    }

    /**
     * Execute the reduction of the data set
     * @return Reduced data set.
     * @throws java.lang.Exception if the reduction can not be done.
     */
    public Pair<PrototypeSet, PrototypeSet> generateReducedDataSet() throws Exception {
        return execute();
    }

    /**
     * Construct the PrototypeGenerator
     * @param _trainingDataSet Original data to be condensed.
     */
    public PrototypeGenerator(PrototypeSet _trainingDataSet) {
        trainingDataSet = _trainingDataSet;
        Distance.setNumberOfInputs(_trainingDataSet.get(0).numberOfInputs());
        PrototypeGenerator.setSeed(SEED);        
    }

    /**
     * Construct the PrototypeGenerator
     * @param _trainingDataSet Original data to be condensed.
     * @param seedIndex Index of the seedDefaultValueList
     */
    public PrototypeGenerator(PrototypeSet _trainingDataSet, int seedIndex) {
        trainingDataSet = _trainingDataSet;
        Distance.setNumberOfInputs(_trainingDataSet.get(0).numberOfInputs());
        seedIndex = seedIndex % seedDefaultValueList.length;
        PrototypeGenerator.setSeed(seedDefaultValueList[seedIndex]);
    }

    /**
     * Construct the PrototypeGenerator
     * @param _trainingDataSet Original data to be condensed.
     * @param parameters Parameters of the algorithm (the random seedDefaultValueList in [0])
     */
    public PrototypeGenerator(PrototypeSet _trainingDataSet, Parameters parameters) {
        trainingDataSet = _trainingDataSet;
        Distance.setNumberOfInputs(_trainingDataSet.get(0).numberOfInputs());
        PrototypeGenerator.setSeed(parameters.getNextAsInt());
    }

    
    /**
     * Construct the PrototypeGenerator for Supervised Learning (pos-processing)
     * @param _trainingDataSet Original data to be condensed.
     * @param _testDataSet Original test prototype set.
     * @param parameters Parameters of the algorithm (the random seedDefaultValueList in [0])
     */
    public PrototypeGenerator(PrototypeSet _trainingDataSet, PrototypeSet _testDataSet, Parameters parameters) {
    	
        trainingDataSet = _trainingDataSet;
        testDataSet = _testDataSet;
        Distance.setNumberOfInputs(_trainingDataSet.get(0).numberOfInputs());
        PrototypeGenerator.setSeed(parameters.getNextAsInt());
    }
    
    /**
     * Construct the PrototypeGenerator
     * @param _trainingDataSet Original data to be condensed.
     * @param _unlabeledDataSet Original unlabeled prototype set for SSL.
     * @param _testDataSet Original test prototype set.
     * @param parameters Parameters of the algorithm (the random seedDefaultValueList in [0])
     */
    public PrototypeGenerator(PrototypeSet _trainingDataSet, PrototypeSet _unlabeledDataSet , PrototypeSet _testDataSet, Parameters parameters) {
        this.trainingDataSet = _trainingDataSet;
        this.transductiveDataSet = _unlabeledDataSet;
        this.testDataSet = _testDataSet;
        Distance.setNumberOfInputs(_trainingDataSet.get(0).numberOfInputs());
        PrototypeGenerator.setSeed(parameters.getNextAsInt());
    }

    /**
     * Calculate the absolute accuracy between two sets
     * @param condensed Reduced data set
     * @param test Test data set
     * @return Number of prototypes of test set that has been well classificated with 1NN on condensed.
     */
    protected static int absoluteAccuracy(PrototypeSet condensed, PrototypeSet test)
    {
        //double test_size = (double) test.size();
        //double generated_size = (double) condensed.size();
        //System.out.println(condensed.size());
        //System.out.println(test.size());
        int accuracy1NN = KNN.classficationAccuracy1NN(condensed, test);
        //double porc_aciertos1NN = (double) (accuracy1NN) / test_size * 100.0;
        //double porc_aciertos_original = porc_aciertos;
        //double porc_reduction = 100.0 - (double) generated_size / (double) training_size * 100.0;
        return accuracy1NN;
    }
    
    
    /**
     * Calculate the absolute accuracy between two sets
     * @param condensed Reduced data set
     * @param test Test data set
     * @param k number of nearest neighbours considered.
     * @return Number of prototypes of test set that has been well classificate with KNN on condensed.
     * @author Isaac Triguero
     */
    protected static int absoluteAccuracyKNN(PrototypeSet condensed, PrototypeSet test, int k)
    {

    	//if( k== 1)  return absoluteAccuracy(condensed,  test);
    
        int accuracy1NN = KNN.classficationAccuracy(condensed, test, k);
        return accuracy1NN;
    }
    
    
    /**
     * Calculate the percetage of well classificated prototypes of one set using 1NN in a reduced set.
     * @param condensed Reduced data set
     * @param test Test data set
     * @return Percetage of well classificated prototypes of test in condendesd set.
     */
    protected static double accuracy(PrototypeSet condensed, PrototypeSet test)
    {
        double absAccuracy =  (double)absoluteAccuracy(condensed, test);
        return 100.0 * (absAccuracy / (double)test.size());
    }
    
    /**
     * Calculate the percetage of well classificated prototypes of one set using 1NN in a reduced set.
     * @param condensed Reduced data set
     * @param test Test data set
     * @return Percetage of well classificated prototypes of test in condendesd set.
     */    
    public double accuracy2(PrototypeSet condensed, PrototypeSet test)
    {
        double absAccuracy =  (double)absoluteAccuracy(condensed, test);
        return 100.0 * (absAccuracy / (double)test.size());
    }
    /**
     * Calculate the absolute accuracy between two sets
     * @param condensed Reduced data set
     * @param test Test data set
     * @return Number of prototypes well classificated
     */
    protected static Pair<Integer,Integer> absoluteAccuracyAndError(PrototypeSet condensed, PrototypeSet test)
    {
        //double test_size = (double) test.size();
        //double generated_size = (double) condensed.size();
        //System.out.println(condensed.size());
        //System.out.println(test.size());
        return KNN.classficationAccuracyAndError1NN(condensed, test);
        //double porc_aciertos1NN = (double) (accuracy1NN) / test_size * 100.0;
        //double porc_aciertos_original = porc_aciertos;
        //double porc_reduction = 100.0 - (double) generated_size / (double) training_size * 100.0;
        //return accuracy1NN;
    }
    


    
     /**
     * Internal function that gets only the accuracy of the condensation.
     * @param name Name of the data set.
     * @param algoName Algorithm name.
     * @param accuracy1NN Number of well-classificated prototypes with KNN.
     * @param test Test prototype set.
     * @return Return the results of the accuracy. 
     */
    public String getResultingAccuracy(String name, String algoName, int accuracy1NN, PrototypeSet test)
    {
        double test_size = (double) test.size();
        //double training_size = (double) trainingDataSet.size();
        //double generated_size = (double) generatedDataSet.size();

        double porc_aciertos1NN = (double) (accuracy1NN) / test_size * 100.0;
        //double porc_aciertos_original = porc_aciertos;
        //double porc_reduction = 100.0 - (double) generated_size / (double) training_size * 100.0;
        String out = "";
        out += "-------------------------------------------------\n";
        out += "RESULTS OF REDUCED DATA " + name + " USING "+algoName+":\n";
        out += "ACCURACY: " + porc_aciertos1NN + "% (" + accuracy1NN + " of " + test_size + " prototypes)\n";
        out += "-------------------------------------------------\n";
        return out;
    }
    

    
    /**
     * Internal function that gets the parameters of accuracy of the condensation.
     * @param name Name of the data set.
     * @param algorithmUsed Name of the algorithm used to reduce the set.
     * @param reduced Reduced prototype set.
     * @param test Test prototype set.
     * @return Return the results of the accuracy as string. 
     */
    public static String getResultsOfAccuracy(String name, String algorithmUsed, PrototypeSet reduced, PrototypeSet test) {
        int accuracy1NN = KNN.classficationAccuracy1NN(reduced, test);
        System.err.println("accuracy1NN: " + accuracy1NN);
        double test_size = (double) test.size();
        double porc_aciertos1NN = (double) (accuracy1NN) / test_size * 100.0;
        System.err.println("porcAciertos: " + porc_aciertos1NN);
        String out = "";
        out += "-------------------------------------------------\n";
        out += "RESULTADOS DE BONDAD DEL CONJUNTO " + name + " GENERADO MEDIANTE "+algorithmUsed+":\n";
        out += "ACIERTOS: " + porc_aciertos1NN + "% (" + accuracy1NN + " de " + test_size + " prototipos)\n";
        out += "-------------------------------------------------\n";
        return out;
    }
    
    /**
     * Internal function that saves the parameters of accuracy of the condensation.
     * @param name Name of the data set.
     * @param algorithmUsed Name of the algorithm used to reduce the set.
     * @param reduced Reduced prototype set.
     * @param test Test prototype set.
     * @param fileName File name that will contain the accuracy.
     * @param append If is TRUE, appends the results to the output file; if is false rewrites the file with the new data.
     */
    public static void saveResultsOfAccuracyIn(String name, String algorithmUsed, PrototypeSet reduced, PrototypeSet test, String fileName, boolean append)
    {
        String data = PrototypeGenerator.getResultsOfAccuracy(name, algorithmUsed, reduced, test);
        if(append)
            KeelFile.append(fileName, data);
        else
            KeelFile.write(fileName, data);
    }

    /**
     * Initiates the vector given.
     * @param vector given vector to initiates
     */
    public void inic_vector(int vector[]){

    	for(int i=0; i<vector.length; i++) vector[i] = i; // Lo inicializo de 1 a n-1
    }

    /**
     * Initiates the vector given without the index given.
     * @param vector given vector to initiates
     * @param without index given.
     */    
    public void inic_vector_sin(int vector[], int without){

    	for(int i=0; i<vector.length; i++) 
    		if(i!=without)
    			vector[i] = i; // Lo inicializo de 1 a n-1
    }

    /**
     * Cuando quitas uno, con el inic vector, el desordenar no puede coger el ultimo..
     * necesito otro metodo
     * @param vector
     */
    public void desordenar_vector_sin(int vector[]){
    	int tmp, pos;
    	for(int i=0; i<vector.length-1; i++){
    		pos = Randomize.Randint(0, vector.length-1);
    		tmp = vector[i];
    		vector[i] = vector[pos];
    		vector[pos] = tmp;
    	}
    }

    /**
     * Shuffles the vector given.
     * @param vector given vector to shuffles.
     */
    public void desordenar_vector(int vector[]){
    	int tmp, pos;
    	for(int i=0; i<vector.length; i++){
    		pos = Randomize.Randint(0, vector.length-1);
    		tmp = vector[i];
    		vector[i] = vector[pos];
    		vector[pos] = tmp;
    	}
    }
    
    

    /**
     * General main for all the prototoype generators
     * Arguments:
     * 0: Filename with the training data set to be condensed.
     * 1: Filename wich will contain the test data set
     * 3: k Number of neighbors used in the KNN function
     * @param args Arguments of the main function.
     */
    public static void main(String[] args) {
        Parameters.setUse("PrototypeGenerator", "");
        Parameters.assertBasicArgs(args);

        PrototypeSet training = PrototypeGenerationAlgorithm.readPrototypeSet(args[0]);
        PrototypeSet test = PrototypeGenerationAlgorithm.readPrototypeSet(args[1]);

        PrototypeGenerator generator = new PrototypeGenerator(training);
        //resultingSet.save(args[1]);

        ArrayList<Double> p = Prototype.possibleValuesOfOutput();

        
        //PrototypeSet resultingSet = generator.execute();

       // int accuracy1NN = KNN.classficationAccuracy1NN(resultingSet, test);
        //generator.showResultsOfAccuracy(Parameters.getFileName(), accuracy1NN, test);
    }
}

