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
import java.util.HashSet;
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

	public void buildNodeNetwork(NetworkDesign design) {
		byte id = 0;
		List<int[]> problemDefinitionTemp = new ArrayList(); // Hier staan de componenten in die op de plaats van een
																// "onbekende" node kunnen staan.
		List<NetworkComponent> comp = design.getComponents(); // stop alle componenten uit het ontwerp in een lijst
		List<NetworkConnection> con = design.getConnections(); // stop alle connecties uit het ontwerp in een lijst
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
				if (comp instanceof NetworkComponentUnknown) {

					firstNode = new Node(comp.get(i), id);
					NetworkComponentUnknown compunknown = (NetworkComponentUnknown) comp;
					int[] types = new int[compunknown.GetComponentTypes().size()];
					for (int j = 0; j < types.length; j++)
						types[j] = compunknown.GetComponentTypes().get(j);
					problemDefinitionTemp.add(types); // hier voegt hij alle componenten toe die op de plaats van de
														// onbekende node kunnen.
					id++;
				} else {
					firstNode = new Node(comp.get(i), (byte) -1);
				}
				break;
			}
		}
		HashSet<NetworkComponent> loopCheck = new HashSet<NetworkComponent>();
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(firstNode); // voeg de eerste node toe aan de queue
		while (queue.isEmpty() == false) {
			Node n = queue.removeFirst(); // verwijder de eerste node uit de queue, (je verwerkt hem nog wel!!!)
			for (int j = 0; j < con.size(); j++) { // kijk voor elke connectie of het de eerste is.
				if (con.get(j).getFirst() == n.comp) {
					System.out.println(n.comp.getNaam() + ":" + con.get(j).getSecond().getNaam());
					if (loopCheck.contains(con.get(j).getSecond()) == false) { // kijkt of de connectie juist is, je kan
																				// bijv. geen connectie van node 5 naar
																				// 3 maken.

						if (comp instanceof NetworkComponentUnknown) {
							// als het component onbekend is, voeg hem toe aan types array, zodat deze later
							// verwerkt kunnen worden.
							Node o = new Node(con.get(j).getSecond(), id);
							NetworkComponentUnknown compunknown = (NetworkComponentUnknown) comp;
							int[] types = new int[compunknown.GetComponentTypes().size()];

							for (int k = 0; k < types.length; k++)
								types[k] = compunknown.GetComponentTypes().get(k); // zet de gegevens van compunkown
																					// over naar types, om het zo in een
																					// array te plaatsen. Dit is nodig
																					// omdat problemDefinition ook een
																					// array is.

							problemDefinitionTemp.add(types);
							id++;
							queue.add(o);
							n.getNodes().add(o);
							loopCheck.add(con.get(j).getSecond());
						} else {
							// Het component is bekend, zet het ID op -1.
							Node o = new Node(con.get(j).getSecond(), (byte) -1);
							queue.add(o);
							n.getNodes().add(o);
							loopCheck.add(con.get(j).getSecond());
						}
					}
				}
			}
		}
		problemDefinition = (int[][]) problemDefinitionTemp.toArray();
		offsetGrootte = new int[problemDefinition.length]; // Zet de offsets voor de solve, om het makkelijker te maken
															// om de solve[] te gebruiken.
		int i = 0;
		for (int j = 0; j < problemDefinition.length; j++) {
			offsetGrootte[j] = i;
			i += problemDefinition[j].length;
		}
	}

	private float getUptime(Node n) {
		// Deze functie wordt gebruikt als het ontwerp uit alleen "bekende" componenten
		// bestaat.
		if (n.getNodes().size() == 0) { // Dit is de laatste node, er is geen berekening meer mogelijk dus returnt hij
										// de uptime
			return n.getComp().getUptime();
		} else if (n.getNodes().size() == 1) {
			return n.getComp().getUptime() * getUptime(n.getNodes().get(0)); // Er komt één connectie uit een node, dus
																				// kan het sequentieel worden berekend.
		} else {
			float v = 1;
			for (int j = 0; j < n.getNodes().size(); j++) {
				v *= 1 - getUptime(n.getNodes().get(j)); // berekend de uptime van meerdere componenten (parallel)
			}
			return (1 - v) * n.getComp().getUptime();
		}
	}

	/**
	 * Berekent de uptime
	 * 
	 * @param ontwerp uptime van het huidige ontwerp
	 * @return uptime in seconden
	 */

	public float calcUptime(NetworkDesign ontwerp) {
		buildNodeNetwork(ontwerp);
		return getUptime(firstNode);
	}

	private float getUptime(Node n, byte solve[]) {
		// Deze functie wordt gebruikt als er 1 of meer onbekende nodes in het ontwerp
		// zitten. De onbekende nodes worden later "berekend" door de CulledHierarchy.
		float uptime = n.getComp().getUptime();
		if (n.GetID() >= 0) { // kijkt hier of het een onbekend component is.
			float v = 1;
			for (int i = 0; i < problemDefinition[n.GetID()].length; i++) {
				v *= Math.pow(1.0f - NetworkComponentTypes.getTypes()[problemDefinition[n.GetID()][i]].getUptime(),
						solve[offsetGrootte[n.GetID()] + i]); // Berekend de uptime van de mogelijke componenten
			}
			uptime = 1.0f - v;
		}
		if (n.getNodes().size() == 0) {
			return uptime; // Hij is bij de laatste node gekomen, is dus klaar en returnt de uptime
		} else if (n.getNodes().size() == 1) { // er komt één connectie uit een node, deze uptime kan sequentieel worden
												// berekend.
			return uptime * getUptime(n.getNodes().get(0), solve);
		} else {
			float v = 1;// hier komt er uit een node meer connecties, dus worden de uptime hiervan
						// parallel berekend.
			for (int j = 0; j < n.getNodes().size(); j++) {
				v *= 1 - getUptime(n.getNodes().get(j), solve);
			}
			return (1 - v) * uptime;
		}
	}

	/**
	 * 
	 * @param problem Het huidige probleem
	 * @param solve   De huidige (mogelijke) oplossing
	 * @return uptime in seconden
	 */
	public float calcUptime(byte solve[]) {
		for (int i = 0; i < problemDefinition.length; i++) {
			int k = 0;
			for (int j = 0; j < problemDefinition[i].length; j++) {
				k += solve[offsetGrootte[i] + j]; // kijk hoeveel componenten er in problemdefinition staan en gooi deze in "k"
			}
			if(k < 2)
				return 0F;	// als er minder dan 2 componenten in staan is het netwerk niet redundant, dus return 0
		}
		return getUptime(firstNode, solve);
	}

	/**
	 * 
	 * @param design huidige kosten van het ontwerp
	 * @return costs
	 */
	public int calcCosts(NetworkDesign design) { // berekend de kosten van het netwerkdesign
		List<NetworkComponent> comps = design.getComponents();
		int costs = 0;
		for (int i = 0; i < comps.size(); i++) {// Bij elk component worden de kosten bij de variabele "costs"
												// toegevoegd.
			costs += comps.get(i).getCosts();
		}

		return costs;
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
