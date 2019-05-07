/*MIT License

Copyright (c) 2019 Bram Stout, Dylan Rüsch, Fiene Botha, Roland Regtop, Sven Reijne, Syb van Gurp

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package me.team4.moniwerp.design;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

/**
 * Deze klas houdt een netwerk ontwerp bij.
 *
 */
public class NetworkDesign {
	/**
	 * De kleinste x waarde van het gebied van de canvas dat wordt gebruikt
	 */
	private int minX;
	/**
	 * De kleinste y waarde van het gebied van de canvas dat wordt gebruikt
	 */
	private int minY;
	/**
	 * De grootste x waarde van het gebied van de canvas dat wordt gebruikt
	 */
	private int maxX;
	/**
	 * De grootste y waarde van het gebied van de canvas dat wordt gebruikt
	 */
	private int maxY;

	/**
	 * De lijst met netwerkcomponenten die in dit ontwerp zitten
	 */
	private List<NetworkComponent> components;

	/**
	 * De lijst met verbindingen tussen netwerkcomponenten
	 */
	private List<NetworkConnection> connections;
	public NetworkDesign(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	/**
	 * Krijg het gebied van de canvas dat door het ontwerp wordt gebruikt
	 * 
	 * @return de bounds [minX, minY, maxX, maxY]
	 */
	public int[] getBounds() {
		return new int[] { minX, minY, maxX, maxY };
	}

	/**
	 * Bereken het nieuwe gebied dat wordt gebruikt door het ontwerp. Als
	 * componenten worden toegevoegd of aangepast, dan veranderen de bounds nog
	 * niet. Daar is deze methode voor.
	 */
	public void calcBounds() {
		// TODO: implement
	}
	
	/**
	 * Sla dit ontwerp op naar de DataOutputStream
	 * @param dos
	 */
	public void save(DataOutputStream dos) {
		// TODO: implement
	}
	
	/**
	 * Laad het ontwerp uit de DataInputStream
	 * @param dis
	 */
	public void load(DataInputStream dis) {
		// TODO: implement
	}
	
	/**
	 * Lijst van netwerkcomponenten die in deze lijst zitten
	 * @return
	 */
	public List<NetworkComponent> getComponents(){
		return components;
	}
	
	/**
	 * Lijst van verbindingen tussen netwerkcomponenten
	 * @return
	 */
	public List<NetworkConnection> getConnections(){
		return connections;
	}

}
