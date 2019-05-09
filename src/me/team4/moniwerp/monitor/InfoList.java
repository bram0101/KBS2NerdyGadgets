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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

/**
 * Lijst met huidige informatie over de geselecteerde node
 *
 */
public class InfoList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * De naam van het component dat geselecteerd is in ViewportNetwork
	 */
	private String selectedComponent;

	/**
	 * Een lijst met welke waardes zijn geselecteerd in deze panel.
	 */
	private List<String> selectedData;
	
	public InfoList() {
		setBackground(Color.red);
		selectedData = new ArrayList<String>();
		selectedData.add("cpu");
		selectedData.add("ram");
	}
	
	/**
	 * Geef aan van welke component de data moet worden weergegeven.
	 * 
	 * @param name De naam van het geselecteerde component
	 */
	public void setSelectedComponent(String name) {
		// TODO: implement
		selectedComponent = name;
	}

	/**
	 * De naam van het component waarvan de data moet worden weergegeven.
	 * 
	 * @return Het geselecteerde component
	 */
	public String getSelectedComponent() {
		return selectedComponent;
	}

	/**
	 * Een lijst met de namen van de data dat geselecteerd is. Dit is om aan te
	 * geven welke waardes in de grafiek moeten worden weergegeven.
	 * 
	 * @return lijst met de namen van geselecteerde data.
	 */
	public List<String> getSelectedData() {
		return selectedData;
	}

	/**
	 * De kleur dat moet worden gebruikt voor het tekenen van bepaalde data. De key
	 * is de naam van de data en de value is welke kleur het moet zijn.
	 * 
	 * @return
	 */
	public HashMap<String, Color> getColours() {
		HashMap<String, Color> map = new HashMap<String, Color>();
		map.put("uptime", Color.BLUE);
		map.put("cpu", Color.CYAN);
		map.put("ram", Color.GREEN);
		return map;
	}

	/**
	 * Deze methode wordt geroepen als er een update moet plaatsvinden. Deze wordt
	 * elke seconde geroepen door TabMonitor
	 */
	public void update() {
		// TODO: implement
	}

}
