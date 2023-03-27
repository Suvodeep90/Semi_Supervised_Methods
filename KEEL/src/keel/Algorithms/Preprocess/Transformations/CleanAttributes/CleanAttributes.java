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



package keel.Algorithms.Preprocess.Transformations.CleanAttributes;
import java.io.*;
import java.util.*;
import keel.Dataset.*;
import keel.Algorithms.Preprocess.Basic.*;

/**
 * <p>
 * This class performs the decimal scaling transformation on the data.
 * It finds the minimum power of ten for each attribute such that dividing all
 * the attribute values, the new values are always less than 1.0
 * </p>
 * <p>
 * @author Written by Julián Luengo Martín 10/11/2005
 * @version 0.3
 * @since JDK 1.5
 * </p>
 */
public class CleanAttributes {
    int []exp;
    double tempData = 0;
    double new_min,new_max;
    String[][] X = null;
    
    int ndatos = 0;
    int nentradas = 0;
    int tipo = 0;
    int direccion = 0;
    int nvariables = 0;
    int nsalidas = 0;
    
    
    InstanceSet IS;
    String input_train_name = new String();
    String input_test_name = new String();
    String output_train_name = new String();
    String output_test_name = new String();
    String temp = new String();
    String data_out = new String("");
    
    /** Creates a new instance of decimal_scaling
     * @param fileParam The path to the configuration file with all the parameters in KEEL format
     */
    public CleanAttributes(String fileParam) {
        config_read(fileParam);
        IS = new InstanceSet();
    }
    
    /**
     * <p>
     * Process the training and test files provided in the parameters file to the constructor.
     * </p>
     */
    public void applyClean(){
        try {
            
            // Load in memory a dataset that contains a classification problem
            IS.readSet(input_train_name,true);
            int in = 0;
            int out = 0;
            
            ndatos = IS.getNumInstances();
            nvariables = Attributes.getNumAttributes();
            nentradas = Attributes.getInputNumAttributes();
            nsalidas = Attributes.getOutputNumAttributes();
            
            X = new String[ndatos][nvariables];//matrix with transformed data
            
            
            for(int i = 0;i < ndatos;i++){
                Instance inst = IS.getInstance(i);
                
                in = 0;
                out = 0;
                
                for(int j = 0; j < nvariables;j++){
                    Attribute a = Attributes.getAttribute(j);
                    
                    direccion = a.getDirectionAttribute();
                    tipo = a.getType();
                    if(usefulAttribute(a)){
                    	if(direccion == Attribute.INPUT){
                    		if(tipo != Attribute.NOMINAL && !inst.getInputMissingValues(in)){
                    			X[i][j] = new String(String.valueOf(inst.getInputRealValues(in)));
                    		} else{
                    			if(!inst.getInputMissingValues(in))
                    				X[i][j] = inst.getInputNominalValues(in); 
                    			else
                    				X[i][j] = new String("?");
                    		}
                    	} else{
                    		if(direccion == Attribute.OUTPUT){
                    			if(tipo != Attribute.NOMINAL && !inst.getOutputMissingValues(out)){
                    				X[i][j] = new String(String.valueOf(inst.getOutputRealValues(out)));
                    			} else{
                    				if(!inst.getOutputMissingValues(out))
                    					X[i][j] = inst.getOutputNominalValues(out);
                    				else
                    					X[i][j] = new String("?");
                    			}
                    		}
                    		/*else{
                           What should we do with non-defined direction values?
                        	}*/
                    	}
                    }
                    if(direccion == Attribute.INPUT)
                    	in++;
                    else if(direccion == Attribute.OUTPUT)
                    	out++;
                }
                
            }
        }catch (Exception e){
            System.out.println("Dataset exception = " + e );
            System.exit(-1);
        }
        write_results(output_train_name);
        /***************************************************************************************/
        //does a test file associated exist?
        if(input_train_name.compareTo(input_test_name)!=0){
            try {
                
            	//delete the modified attributes! the test set has the older
            	//bounds
            	Attributes.clearAll();
                // Load in memory a dataset that contains a classification problem
                IS.readSet(input_test_name,true);
                int in = 0;
                int out = 0;
                
                ndatos = IS.getNumInstances();
                nvariables = Attributes.getNumAttributes();
                nentradas = Attributes.getInputNumAttributes();
                nsalidas = Attributes.getOutputNumAttributes();
                
                X = new String[ndatos][nvariables];//matrix with transformed data
                
                for(int i = 0;i < ndatos;i++){
                    Instance inst = IS.getInstance(i);
                    
                    in = 0;
                    out = 0;

                    for(int j = 0; j < nvariables;j++){
                    	Attribute a = Attributes.getAttribute(j);

                    	direccion = a.getDirectionAttribute();
                    	tipo = a.getType();

                    	if(usefulAttribute(a)){
                    		if(direccion == Attribute.INPUT){
                    			if(tipo != Attribute.NOMINAL && !inst.getInputMissingValues(in)){
                    				X[i][j] = new String(String.valueOf(inst.getInputRealValues(in)));
                    			} else{
                    				if(!inst.getInputMissingValues(in))
                    					X[i][j] = inst.getInputNominalValues(in); 
                    				else
                    					X[i][j] = new String("?");
                    			}
                    		} else{
                    			if(direccion == Attribute.OUTPUT){
                    				if(tipo != Attribute.NOMINAL && !inst.getOutputMissingValues(out)){
                    					X[i][j] = new String(String.valueOf(inst.getOutputRealValues(out)));
                    				} else{
                    					if(!inst.getOutputMissingValues(out))
                    						X[i][j] = inst.getOutputNominalValues(out); 
                    					else
                    						X[i][j] = new String("?");
                    				}
                    			}
                    			/*else{
                           What should we do with non-defined direction values?
                        	}*/
                    		}
                    	}
                    	if(direccion == Attribute.INPUT)
                        	in++;
                        else if(direccion == Attribute.OUTPUT)
                        	out++;
                    }
                    
                }

            }catch (Exception e){
                System.out.println("Dataset exception = " + e );
                System.exit(-1);
            }
            write_results(output_test_name);
        }
    }
    
    private void config_read(String fileParam){
        File inputFile = new File(fileParam);
        
        if (inputFile == null || !inputFile.exists()) {
            System.out.println("parameter "+fileParam+" file doesn't exists!");
            System.exit(-1);
        }
        //begin the configuration read from file
        try {
            FileReader file_reader = new FileReader(inputFile);
            BufferedReader buf_reader = new BufferedReader(file_reader);
            //FileWriter file_write = new FileWriter(outputFile);
            
            String line;
            
            do{
                line = buf_reader.readLine();
            }while(line.length()==0); //avoid empty lines for processing -> produce exec failure
            String out[]= line.split("algorithm = ");
            //alg_name = new String(out[1]); //catch the algorithm name
            //input & output filenames
            do{
                line = buf_reader.readLine();
            }while(line.length()==0);
            out= line.split("inputData = ");
            out = out[1].split("\\s\"");
            input_train_name = new String(out[0].substring(1, out[0].length()-1));
            input_test_name = new String(out[1].substring(0, out[1].length()-1));
            if(input_test_name.charAt(input_test_name.length()-1)=='"')
                input_test_name = input_test_name.substring(0,input_test_name.length()-1);
            
            do{
                line = buf_reader.readLine();
            }while(line.length()==0);
            out = line.split("outputData = ");
            out = out[1].split("\\s\"");
            output_train_name = new String(out[0].substring(1, out[0].length()-1));
            output_test_name = new String(out[1].substring(0, out[1].length()-1));
            if(output_test_name.charAt(output_test_name.length()-1)=='"')
                output_test_name = output_test_name.substring(0,output_test_name.length()-1);
            
            file_reader.close();
            
        } catch (IOException e) {
            System.out.println("IO exception = " + e );
            System.exit(-1);
        }
    }
    
    private void write_results(String output){
    	Attribute a;
    	String header = "";
		int i, j, k,inputDel,outputDel;
		int aux;
		boolean first;
		
		inputDel = outputDel = 0;
		for(i=0;i<Attributes.getNumAttributes();i++){
			a = Attributes.getAttribute(i);
			if(a.getDirectionAttribute()==Attribute.INPUT){
				if(!usefulAttribute(a)){
					Attributes.removeAttribute(true, i-inputDel);
					inputDel++;
				}
			}
			if(a.getDirectionAttribute()==Attribute.OUTPUT){
				if(!usefulAttribute(a)){
					Attributes.removeAttribute(false, i-outputDel);
					outputDel++;
				}
			}	
			
		}
		
        //File OutputFile = new File(output_train_name.substring(1, output_train_name.length()-1));
		try {

			FileWriter file_write = new FileWriter(output);

			/* Printing input attributes */
			header += "@relation " + Attributes.getRelationName()+ "\n";
			for (i = 0; i < Attributes.getNumAttributes(); i++) {
				a = Attributes.getAttribute(i);
				if(usefulAttribute(a)){
					// Printing the input attribute
					if(a.getDirectionAttribute()==Attribute.INPUT){
						header += "@attribute " + a.getName() + " ";
						if (a.getType() == Attribute.NOMINAL) {
							header += "{";
							for (j = 0; j < a.getNominalValuesList().size(); j++) {
								header += (String) a.getNominalValuesList()
								.elementAt(j);
								if (j < a.getNominalValuesList().size() - 1) {
									header += ", ";
								}
							}
							header += "}\n";
						} else {
							if (a.getType() == Attribute.INTEGER) {
								header += "integer";
								header += " ["
									+ String.valueOf((int) a.getMinAttribute()) + ", "+ String.valueOf((int) a.getMaxAttribute()) + "]\n";
							} else {
								header += "real";
								header += " ["
									+ String.valueOf(a.getMinAttribute())
									+ ", "
									+ String.valueOf(a.getMaxAttribute())
									+ "]\n";
							}
						}
					}else{
						/* Printing output attribute */
						header += "@attribute " + a.getName() + " ";
						if (a.getType() == Attribute.NOMINAL) {
							header += "{";
							for (j = 0; j < a.getNominalValuesList().size(); j++) {
								header += (String) a.getNominalValuesList().elementAt(j);
								if (j < a.getNominalValuesList().size() - 1) {
									header += ", ";
								}
							}
							header += "}\n";
						} else {
							header += "integer ["
								+ String.valueOf((int) a.getMinAttribute()) + ", "
								+ String.valueOf((int) a.getMaxAttribute()) + "]\n";
						}
					}
				}
    		}
    		file_write.write(header);
    		
    		file_write.write(Attributes.getInputHeader()+"\n");
    		file_write.write(Attributes.getOutputHeader()+"\n");
    		
            //now, print the data
            file_write.write("@data\n");
            for(i=0;i<ndatos;i++){
            	first = true;
                for(j=0;j<nvariables;j++){
                	a = Attributes.getAttribute(j);
                	if(usefulAttribute(a)){
                		if(!first)
                			file_write.write(","+X[i][j]);
                		else{
                			file_write.write(X[i][j]);
                			first = false;
                		}
                	}
                }
                file_write.write("\n");
            }
            file_write.close();
        } catch (IOException e) {
            System.out.println("IO exception = " + e );
            System.exit(-1);
        }
    }
    
    /**
     * Returns true if the attribute given is useful, false otherwise.
     * @param at attribute class given.
     * @return  true if the attribute given is useful, false otherwise.
     */
    public boolean usefulAttribute(Attribute at){
    	if(at.getType()==Attribute.NOMINAL && at.getNumNominalValues()<2){
    		return false;    		
    	}
    	if(at.getType()!=Attribute.NOMINAL && at.getMaxAttribute()==at.getMinAttribute()){
    		return false;
    	}
    	return true;
    }
    
}
