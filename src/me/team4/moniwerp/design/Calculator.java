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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Een calculator om de uptime en kosten te berekenen
 *
 */
public class Calculator {

	private Node firstNode;
	private int[][] problemDefinition;
	private int[] offsetGrootte;
	private int minComponents = 2;

	/**
	 * Bouw een nieuwe representatie van het netwerkontwerp, om beter de uptime te
	 * kunnen berekenen.
	 * 
	 * @param design
	 */
	public void buildNodeNetwork(NetworkDesign design) {
		byte id = 0;
		// Hier staan de componenten in die op de plaats van een
		// "onbekende" node kunnen staan.
		List<int[]> problemDefinitionTemp = new ArrayList<int[]>();
		// stop alle componenten uit het ontwerp in een lijst
		List<NetworkComponent> comp = design.getComponents();
		// stop alle connecties uit het ontwerp in een lijst
		List<NetworkConnection> con = design.getConnections();

		// Probeer de eerste node in de netwerkontwerp te vinden.
		// De eerste node heeft geen inkomende connecties.
		for (int i = 0; i < comp.size(); i++) {
			boolean found = true;
			// loop om eerste node te vinden
			for (int j = 0; j < con.size(); j++) {
				if (con.get(j).getSecond() == comp.get(i)) {
					found = false;
					break;
				}
			} // Dit is dus de eerste node
			if (found == true) {
				// Kijk of het Netwerkcomponent bekend is.
				if (comp.get(i) instanceof NetworkComponentUnknown) {

					firstNode = new Node(comp.get(i), id);
					NetworkComponentUnknown compunknown = (NetworkComponentUnknown) comp.get(i);
					int[] types = new int[compunknown.GetComponentTypes().size()];
					for (int j = 0; j < types.length; j++)
						types[j] = compunknown.GetComponentTypes().get(j);
					if (types.length == 0) {
						firstNode = null;
					}
					problemDefinitionTemp.add(types); // hier voegt hij alle componenten toe die op de plaats van de
														// onbekende node kunnen.
					id++;
				} else {
					firstNode = new Node(comp.get(i), (byte) -1);
				}
				break;
			}
		}

		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(firstNode); // voeg de eerste node toe aan de queue
		int iterationCounter = 0;
		while (queue.isEmpty() == false) {
			iterationCounter++;
			// Als er ergens een loop zit, stopt hij nooit, dus hiermee stoppen wij als
			// hij te lang doorgaat.
			if (iterationCounter >= 10000) {
				firstNode = null;
				break;
			}
			// verwijder de eerste node uit de queue, (je verwerkt hem nog wel!!!)
			Node n = queue.removeFirst();
			// kijk voor elke connectie of het de eerste is.
			for (int j = 0; j < con.size(); j++) {
				if (con.get(j).getFirst() == n.comp) {

					if (con.get(j).getSecond() instanceof NetworkComponentUnknown) {
						// als het component onbekend is, voeg hem toe aan types array, zodat deze later
						// verwerkt kunnen worden.
						Node o = new Node(con.get(j).getSecond(), id);
						NetworkComponentUnknown compunknown = (NetworkComponentUnknown) con.get(j).getSecond();
						int[] types = new int[compunknown.GetComponentTypes().size()];

						for (int k = 0; k < types.length; k++)
							// zet de gegevens van compunkown
							// over naar types, om het zo in een
							// array te plaatsen. Dit is nodig
							// omdat problemDefinition ook een
							// array is.
							types[k] = compunknown.GetComponentTypes().get(k);
						if (types.length == 0) {
							firstNode = null;
						}
						problemDefinitionTemp.add(types);
						id++;
						queue.add(o);
						n.getNodes().add(o);
					} else {
						// Het component is bekend, zet het ID op -1.
						Node o = new Node(con.get(j).getSecond(), (byte) -1);
						queue.add(o);
						n.getNodes().add(o);
					}
				}
			}
		}
		problemDefinition = new int[problemDefinitionTemp.size()][1];
		for (int i = 0; i < problemDefinitionTemp.size(); i++) {
			problemDefinition[i] = problemDefinitionTemp.get(i);
		}
		// Zet de offsets voor de solve, om het makkelijker te maken
		// om de solve[] te gebruiken.
		offsetGrootte = new int[problemDefinition.length]; 
		int i = 0;
		for (int j = 0; j < problemDefinition.length; j++) {
			offsetGrootte[j] = i;
			i += problemDefinition[j].length;
		}
	}

	private double getUptime(Node n) {
		// Deze functie wordt gebruikt als het ontwerp uit alleen "bekende" componenten
		// bestaat.
		// Dit is de laatste node, er is geen berekening meer mogelijk dus returnt hij
		// de uptime
		if (n.getNodes().size() == 0) { 
			return n.getComp().getUptime();
		} else if (n.getNodes().size() == 1) {
			// Er komt één connectie uit een node, dus
			// kan het sequentieel worden berekend.
			return n.getComp().getUptime() * getUptime(n.getNodes().get(0)); 
		} else if (n.getComp().getType().equals("SERIE")) {
			double v = 1D;
			for (int j = 0; j < n.getNodes().size(); j++) {
				v *= getUptime(n.getNodes().get(j));
			}
			return v;
		} else {
			double v = 1D;
			for (int j = 0; j < n.getNodes().size(); j++) {
				// berekend de uptime van meerdere componenten (parallel)
				v *= 1D - getUptime(n.getNodes().get(j)); 
			}
			return (1D - v) * n.getComp().getUptime();
		}
	}

	/**
	 * Berekent de uptime
	 * 
	 * @param ontwerp uptime van het huidige ontwerp
	 * @return uptime in seconden
	 */

	public double calcUptime(NetworkDesign ontwerp) {
		// Als er geen componenten zijn, is de uptime natuurlijk 0%. Om een
		// nullpointerexception te voorkomen.
		if (ontwerp.getComponents().isEmpty())
			return 0D;
		buildNodeNetwork(ontwerp);
		if (firstNode == null) {
			return 0D;
		}
		return getUptime(firstNode);
	}

	private double getUptime(Node n, byte solve[]) {
		// Deze functie wordt gebruikt als er 1 of meer onbekende nodes in het ontwerp
		// zitten. De onbekende nodes worden later "berekend" door de CulledHierarchy.
		double uptime = n.getComp().getUptime();
		if (n.GetID() >= 0) { // kijkt hier of het een onbekend component is.
			double v = 1;
			for (int i = 0; i < problemDefinition[n.GetID()].length; i++) {
				// Berekend de uptime van de mogelijke componenten
				v *= Math.pow(1.0f - NetworkComponentTypes.getTypes()[problemDefinition[n.GetID()][i]].getUptime(),
						solve[offsetGrootte[n.GetID()] + i]); 
			}
			uptime = 1D - v;
		}
		if (n.getNodes().size() == 0) {
			// Hij is bij de laatste node gekomen, is dus klaar en returnt de uptime
			return uptime; 
		} else if (n.getNodes().size() == 1) { 
			// er komt één connectie uit een node, deze uptime kan sequentieel worden
			// berekend.
			return uptime * getUptime(n.getNodes().get(0), solve);
		} else if (n.getComp().getType().equals("SERIE")) {
			double v = 1D;
			for (int j = 0; j < n.getNodes().size(); j++) {
				v *= getUptime(n.getNodes().get(j), solve);
			}
			return v;
		} else {
			// hier komt er uit een node meer connecties, dus worden de uptime hiervan
			// parallel berekend.
			double v = 1D;
			for (int j = 0; j < n.getNodes().size(); j++) {
				v *= 1D - getUptime(n.getNodes().get(j), solve);
			}
			return (1D - v) * uptime;
		}
	}

	/**
	 * 
	 * @param problem Het huidige probleem
	 * @param solve   De huidige (mogelijke) oplossing
	 * @return uptime in seconden
	 */
	public double calcUptime(byte solve[]) {
		if (firstNode == null)
			return 0D;
		for (int i = 0; i < problemDefinition.length; i++) {
			int k = 0;
			for (int j = 0; j < problemDefinition[i].length; j++) {
				// kijk hoeveel componenten er in problemdefinition staan en gooi deze
				// in "k"
				k += solve[offsetGrootte[i] + j]; 
			}
			if (k < minComponents) {
				// als er minder dan 2 componenten in staan is het netwerk niet redundant, dus
				// return 0
				return 0D; 
			}
		}
		return getUptime(firstNode, solve);
	}

	/**
	 * 
	 * @param design huidige kosten van het ontwerp
	 * @return costs
	 */
	public int calcCosts(NetworkDesign design) {
		// Als er geen componenten zijn, zijn de kosten natuurlijk 0. Om een
		// nullpointerexception te voorkomen.
		if (design.getComponents().isEmpty())
			return 0;
		// berekend de kosten van het netwerkdesign
		List<NetworkComponent> comps = design.getComponents();
		int costs = 0;
		// Bij elk component worden de kosten bij de variabele "costs"
		// toegevoegd.
		for (int i = 0; i < comps.size(); i++) {
			costs += comps.get(i).getCosts();
		}

		return costs;
	}

	/**
	 * 
	 * @param problem Het huidige probleem
	 * @param solve   De huidige (mogelijke) oplossing
	 * @return kosten
	 */
	public int calcCosts(byte solve[]) {
		if (firstNode == null)
			return 0;
		int costs = 0;
		for (int i = 0; i < problemDefinition.length; i++) {
			for (int j = 0; j < problemDefinition[i].length; j++) {
				costs += NetworkComponentTypes.getTypes()[problemDefinition[i][j]].getCosts()
						* solve[offsetGrootte[i] + j];
			}
		}
		return costs;
	}

	public int[][] getProblemDefinition() {
		return problemDefinition;
	}

	public int[] getOffsetGrootte() {
		return offsetGrootte;
	}

	public void setMinComponents(int minComponents) {
		this.minComponents = minComponents;
	}

	public Node getFirstNode() {
		return firstNode;
	}

	public static class Node {

		private NetworkComponent comp; // Uit welk netwerkcomponent de node bestaat
		private List<Node> nodes; // de lijst waarin alle nodes uit het ontwerp staan
		private Byte ID; // Het id van de node

		public Node(NetworkComponent comp, Byte ID) {
			this.comp = comp;
			nodes = new ArrayList<Node>();
			this.ID = ID;
		}

		public NetworkComponent getComp() {
			return comp;
		}

		public Byte GetID() {
			return ID;
		}

		public List<Node> getNodes() {
			return nodes;
		}
	}

}
