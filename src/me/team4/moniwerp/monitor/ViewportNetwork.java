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
package me.team4.moniwerp.monitor;

import java.awt.Color;

import javax.swing.JPanel;

import me.team4.moniwerp.design.NetworkComponent;
import me.team4.moniwerp.design.NetworkDesign;

/**
 * Grafische representatie van het netwerk
 *
 */
public class ViewportNetwork extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Het netwerkcomponent dat geselecteerd is
	 */
	private NetworkComponent selected;
	
	/**
	 * Het netwerkontwerp dat wordt weergegeven.
	 */
	private NetworkDesign design;
	
	public ViewportNetwork() {
		setBackground(Color.green);
	}
	
	/**
	 * Geeft het geselecteerde netwerkcomponent
	 * @return Het geselecteerde netwerkcomponent
	 */
	public NetworkComponent getSelectedNetworkComponent() {
		return selected;
	}
	
	public NetworkDesign getNetworkDesign() {
		return design;
	}
	
	public void setNetworkDesign(NetworkDesign design) {
		// TODO: implement
	}
	
	/**
	 * Deze methode wordt geroepen als er een update moet plaatsvinden. Deze wordt
	 * elke seconde geroepen door TabMonitor
	 */
	public void update() {
		// TODO: implement
	}

}
