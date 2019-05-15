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
		List<int[]> problemDefinitionTemp = new ArrayList();
		List<NetworkComponent> comp = design.getComponents();
		List<NetworkConnection> con = design.getConnections();
		for (int i = 0; i < comp.size(); i++) {
			boolean found = true;
			// loop om eerste te vinden
			for (int j = 0; j < con.size(); j++) {
				if (con.get(j).getSecond() == comp.get(i)) {
					found = false;
					break;
				}
			}
			// Dit is dus de eerste node
			if (found == true) {
				if (comp instanceof NetworkComponentUnknown) {
					firstNode = new Node(comp.get(i), id);
					NetworkComponentUnknown compunknown = (NetworkComponentUnknown) comp;
					int[] types = new int[compunknown.GetComponentTypes().size()];
					for (int j = 0; j < types.length; j++)
						types[j] = compunknown.GetComponentTypes().get(j);
					problemDefinitionTemp.add(types);
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
			Node n = queue.removeFirst();
			for (int j = 0; j < con.size(); j++) {
				if (con.get(j).getFirst() == n.comp) {
					System.out.println(n.comp.getNaam() + ":" + con.get(j).getSecond().getNaam());
					if (loopCheck.contains(con.get(j).getSecond()) == false) {

						if (comp instanceof NetworkComponentUnknown) {
							Node o = new Node(con.get(j).getSecond(), id);
							NetworkComponentUnknown compunknown = (NetworkComponentUnknown) comp;
							int[] types = new int[compunknown.GetComponentTypes().size()];

							for (int k = 0; k < types.length; k++)
								types[k] = compunknown.GetComponentTypes().get(k);

							problemDefinitionTemp.add(types);
							id++;
							queue.add(o);
							n.getNodes().add(o);
							loopCheck.add(con.get(j).getSecond());
						} else {
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
		offsetGrootte = new int[problemDefinition.length];
		int i = 0;
		for (int j = 0; j < problemDefinition.length; j++) {
			offsetGrootte[j] = i;
			i += problemDefinition[j].length;
		}
	}

	private float getUptime(Node n) {
		if (n.getNodes().size() == 0) {
			return n.getComp().getUptime();
		} else if (n.getNodes().size() == 1) {
			return n.getComp().getUptime() * getUptime(n.getNodes().get(0));
		} else {
			float v = 1;
			for (int j = 0; j < n.getNodes().size(); j++) {
				v *= 1 - getUptime(n.getNodes().get(j));
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
		float uptime = n.getComp().getUptime();
		if (n.GetID() >= 0) {
			float v = 1;
			for (int i = 0; i < problemDefinition[n.GetID()].length; i++) {
				v *= Math.pow(1.0f - NetworkComponentTypes.getTypes()[problemDefinition[n.GetID()][i]].getUptime(),
						solve[offsetGrootte[n.GetID()] + i]);
			}
			uptime = 1.0f - v;
		}
		if (n.getNodes().size() == 0) {
			return uptime;
		} else if (n.getNodes().size() == 1) {
			return uptime * getUptime(n.getNodes().get(0), solve);
		} else {
			float v = 1;
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
		return getUptime(firstNode, solve);
	}

	/**
	 * 
	 * @param design huidige kosten van het ontwerp
	 * @return costs
	 */
	public int calcCosts(NetworkDesign design) {
		List<NetworkComponent> comps = design.getComponents();
		int costs = 0;
		for (int i = 0; i < comps.size(); i++) {
			costs += comps.get(i).getCosts();
		}

		return costs;
	}

	public static class Node {

		private NetworkComponent comp;
		private List<Node> nodes;
		private Byte ID;

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
