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


package keel.Algorithms.Decision_Trees.SLIQ;

import java.util.Vector;

/**
 * This class implements the histogram associated with the tree nodes.
 *
 * @author Francisco Charte Ojeda
 * @version 1.0 (29-12-09)
 */
public class Histograma {
    // Elementos que quedan a izquierda y derecha
    Vector<Integer>
            izquierda = new Vector<Integer>(),
            derecha = new Vector<Integer>();

    // NÃºmero de elementos en cada parte
    int totalIzquierda, totalDerecha, total;

    /**
     * Default constructor.
     */
    Histograma() {
        total = totalIzquierda = totalDerecha = 0;
    }


    /**
     * Parameter Constructor. Initiates the number of classes considered on the histogram with the one given.
     * @param numeroClases  given number of classses.
     */
    Histograma(int numeroClases) {
        total = totalIzquierda = totalDerecha = 0;

        for(int indice = 0; indice < numeroClases; indice++) {
            izquierda.add(0);
            derecha.add(0);
        }
    }

    /**
     * Actualizes the histogram after processing a data row with the given class.
     * @param clase given class.
     */
    void actualiza(int clase) {
       izquierda.set(clase, izquierda.get(clase) - 1);
       derecha.set(clase, derecha.get(clase) + 1);

       totalIzquierda--;
       totalDerecha++;
    }

    /**
     * Increases the counter for the class given.
     * @param clase class given.
     */
    void incIzquierda(int clase) {
        total++;
        totalIzquierda++;

        izquierda.set(clase, izquierda.get(clase) + 1);
    }

    
    /**
     * Swaps the values of the right and left vectors.
     */
    void intercambia() {
        Vector<Integer> temporal = (Vector<Integer> )izquierda.clone();
        izquierda.removeAllElements();
        izquierda.addAll(derecha);
        derecha.removeAllElements();
        derecha.addAll(temporal);

        totalIzquierda = totalDerecha;
        totalDerecha = 0;
    }

    /**
     * Computes the Gini index with the histogram information.
     * @return the Gini index
     */
    double gini() {
        double resultado = 0, frecuencia = 0.0, temporal = 1.0;

        if(totalDerecha != 0) {
            for(int indice = 0; indice < derecha.size(); indice++)
                if(derecha.get(indice) != 0) {
                    frecuencia = derecha.get(indice).floatValue() / (float )totalDerecha;
                    temporal -= frecuencia * frecuencia;
                }

            resultado += temporal * totalDerecha;
            temporal = 1.0;
        }

        if(totalIzquierda != 0) {
            for(int indice = 0; indice < izquierda.size(); indice++)
                if(izquierda.get(indice) != 0) {
                    frecuencia = izquierda.get(indice).floatValue() / (float )totalIzquierda;
                    temporal -= frecuencia * frecuencia;
                }

            resultado += temporal * totalIzquierda;
        }

        return resultado / (float )total;
    }



}

