package keel.Algorithms.Preprocess.NoiseFilters.INFFC;

import java.util.Arrays;
import keel.Dataset.Attribute;
import keel.Dataset.Attributes;
import keel.Dataset.Instance;
import keel.Dataset.InstanceSet;


public class Distance{

private Instance[] instances;
int nclasses, ninstances, nattributes, numPartitions;

boolean used[];
int[] numExClass;
int[] sortedIndex;
int[] numUsedPerClass;

double[] stdDev;
double[][][] nominalDistance;
//*******************************************************************************************************************************

public Distance(String source_file){
	
	InstanceSet is = new InstanceSet();

	//Attributes.clearAll();
	try {	
		is.readSet(source_file, false);
    }catch(Exception e){
        System.exit(1);
    }
    
    instances = is.getInstances();
    nclasses = Attributes.getOutputAttribute(0).getNumNominalValues();
    nattributes = Attributes.getInputAttributes().length;
    ninstances = instances.length;
    
    
    calculo_previo_hvdm();
}

public void computeKNN(int num_vecinos, int instancia, int cl, int[] neighbors, int[] clase){
	
	boolean distance = false; //hvdm distance
	
    evaluationKNNClass (num_vecinos, instancia, nclasses, distance, neighbors, cl, clase);
}
	
//*******************************************************************************************************************************

public int evaluationKNNClass (int nvec, int instancia, int nClases, boolean distance, int vecinos[], int clase, int[] clase_vecino) {

        int i, j, l;
        boolean parar = false;
        int vecinosCercanos[];
        double minDistancias[];
        int votos[];
        double dist;
        int votada, votaciones;

        if (nvec > ninstances)
                nvec = ninstances;

        votos = new int[nClases];
        vecinosCercanos = new int[nvec];
        minDistancias = new double[nvec];
        for (i=0; i<nvec; i++) {
                vecinosCercanos[i] = -1;
                minDistancias[i] = Double.POSITIVE_INFINITY;
        }

        for (i=0; i<ninstances; i++) {
        	
        	//if(!used[i]){
        	
                dist = distancia(i, instancia);
                if (dist > 0 /*&& instances[i].getOutputNominalValuesInt(0) == clase*/) {
                		
                	
                        parar = false;
                        for (j = 0; j < nvec && !parar; j++) {
                                if (dist < minDistancias[j]) {
                                        parar = true;
                                        for (l = nvec - 1; l >= j+1; l--) {
                                                minDistancias[l] = minDistancias[l - 1];
                                                vecinosCercanos[l] = vecinosCercanos[l - 1];
                                        }
                                        minDistancias[j] = dist;
                                        vecinosCercanos[j] = i;
                                }
                        }
                }
                
        	//}
        }
        
        

        for (j=0; j<nClases; j++) {
        	votos[j] = 0;
        }

        for (j=0; j<nvec; j++){
        	if (vecinosCercanos[j] >= 0)
        		votos[instances[vecinosCercanos[j]].getOutputNominalValuesInt(0)] ++;
        }

        votada = 0;
        votaciones = votos[0];
        for (j=1; j<nClases; j++) {
                if (votaciones < votos[j]) {
                        votaciones = votos[j];
                        votada = j;
                }
        }

        for (i=0; i<vecinosCercanos.length; i++)
                vecinos[i] = vecinosCercanos[i];
        
        Arrays.fill(clase_vecino, -1);
        for (i=0; i<vecinosCercanos.length; i++){
        	if (vecinosCercanos[i] >= 0)
        		clase_vecino[i] = instances[vecinosCercanos[i]].getOutputNominalValuesInt(0);
        }
        

        return votada;
}


boolean quedanEjemplos (int clase){
	return (numUsedPerClass[clase]<numExClass[clase]);
}

int instanciaNoUsada(int clase){
	
	for(int i = 0 ; i < ninstances ; ++i){
		if(!used[sortedIndex[i]] && instances[sortedIndex[i]].getOutputNominalValuesInt(0)==clase)
			return sortedIndex[i];
	}
	
	return -1;
}


public double distancia (int ej1, int ej2) {

	int i;
	double suma = 0;
		
	for (i=0; i<nattributes; i++) {
		if (instances[ej1].getInputMissingValues(i) == true || instances[ej2].getInputMissingValues(i) == true) {
			suma += 1;
		} else if (Attributes.getInputAttribute(i).getType() == Attribute.NOMINAL) {
			suma += nominalDistance[i][instances[ej1].getInputNominalValuesInt(i)][instances[ej2].getInputNominalValuesInt(i)];
		} else {
			suma += Math.abs(instances[ej1].getInputRealValues(i)-instances[ej2].getInputRealValues(i)) / 4*stdDev[i];
		}
	}
	
	suma = Math.sqrt(suma);       	
	
	return suma;  
}



public void calculo_previo_hvdm(){
	
	double VDM, Nax, Nay,Naxc,Nayc,media, SD;
	stdDev = new double[Attributes.getInputNumAttributes()];
	nominalDistance = new double[Attributes.getInputNumAttributes()][][];
	int nClases = Attributes.getOutputAttribute(0).getNumNominalValues();


	for (int i=0; i<nominalDistance.length; i++) {
		if (Attributes.getInputAttribute(i).getType() == Attribute.NOMINAL) {
                    nominalDistance[i] = new double[Attributes.getInputAttribute(i).getNumNominalValues()][Attributes.getInputAttribute(i).getNumNominalValues()];
                    for (int j=0; j<Attributes.getInputAttribute(i).getNumNominalValues(); j++) { 
                            nominalDistance[i][j][j] = 0.0;

                    }
                    for (int j=0; j<Attributes.getInputAttribute(i).getNumNominalValues(); j++) {
                            for (int l=j+1; l<Attributes.getInputAttribute(i).getNumNominalValues(); l++) {
                                    VDM = 0.0;
                                    Nax = Nay = 0;
                                    for (int m=0; m<ninstances; m++) {
                                            if ( instances[m].getInputNominalValuesInt(i) == j) {
                                                    Nax++;

                                            }
                                            if (instances[m].getInputNominalValuesInt(i) == l) {
                                                    Nay++;


                                            }
                                    }
                                    for (int m=0; m<nClases; m++) {
                                            Naxc = Nayc = 0;
                                            for (int n=0; n<ninstances; n++) {
                                                    if ( instances[n].getInputNominalValuesInt(i) == j && instances[n].getOutputNominalValuesInt(0) == m) {
                                                            Naxc++;

                                                    }
                                                    
                                                    if ( instances[n].getInputNominalValuesInt(i) == l && instances[n].getOutputNominalValuesInt(0) == m) {
                                                        Nayc++;
                                                    }
                                            }
                                            VDM += (((double)Naxc / (double)Nax) - ((double)Nayc / (double)Nay)) * (((double)Naxc / (double)Nax) - ((double)Nayc / (double)Nay));

                                    }
                                    nominalDistance[i][j][l] = Math.sqrt(VDM);
                                    nominalDistance[i][l][j] = Math.sqrt(VDM);



                            }
                    }
            } else {
                    media = 0;
                    SD = 0;
                    for (int j=0; j<ninstances; j++) {
                            media += instances[j].getInputRealValues(i);
                            SD += instances[j].getInputRealValues(i)*instances[j].getInputRealValues(i);

                    }
                    media /= (double)ninstances;
                    stdDev[i] = Math.sqrt((SD/((double)ninstances)) - (media*media));



            }
    }
 


}



}