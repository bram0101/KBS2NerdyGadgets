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
import java.util.Iterator;
import java.util.List;

/**
 * Optimaliseert het huidige ontwerp door middel van CulledHierarchy
 */
public class Optimizer {

	/**
	 * Zorgt ervoor dat CulledHierarchy het beste ontwerp kan gebruiken
	 * 
	 * @param ontwerp: Het huidige ontwerp
	 */
	private byte[] solve;
	private Calculator c;

	public Optimizer() {
		c = new Calculator();
	}

	public void optimize(NetworkDesign ontwerp, double minimumUptime) {
		// Check of er wel onbekendes in zitten.
		boolean check = false;
		for (NetworkComponent comp : ontwerp.getComponents()) {
			if (comp instanceof NetworkComponentUnknown) {
				check = true;
				break;
			}
		}
		// Geen onbekendes, stop dan nu.
		if (!check)
			return;

		// Bouw het node netwerk op. Nodig voor uptime berekenen.
		c.buildNodeNetwork(ontwerp);

		// Als er geen begin in het netwerk is, stop dan.
		if (c.getFirstNode() == null)
			return;

		CulledHierarchy ch = new CulledHierarchy();

		// Voer het algoritme uit
		solve = ch.execute(c.getProblemDefinition(), c, minimumUptime);

		// Vervang de onbekende componenten met de echte componenten die het algoritme
		// heeft berekend.
		replaceComponents(ontwerp, c.getFirstNode());
	}

	private void replaceComponents(NetworkDesign design, Calculator.Node node) {
		// Is het een onbekende?
		if (node.GetID() >= 0) {
			// Haal alle ingaande en uitgaande connecties op en sla ze op in deze lijsten
			List<NetworkComponent> inConnections = new ArrayList<NetworkComponent>();
			List<NetworkComponent> outConnections = new ArrayList<NetworkComponent>();

			Iterator<NetworkConnection> connections = design.getConnections().iterator();
			NetworkConnection conn = null;
			// Voor elke connectie
			while (connections.hasNext()) {
				conn = connections.next();
				// Is dit een uitgaande connectie?
				if (conn.getFirst() == node.getComp()) {
					// voeg het toe aan de lijst
					outConnections.add(conn.getSecond());
					// haal de connectie weg
					connections.remove();
				}
				// Is dit een ingaande connectie?
				if (conn.getSecond() == node.getComp()) {
					// voeg het toe aan de lijst
					inConnections.add(conn.getFirst());
					// haal de connectie weg
					connections.remove();
				}
			}

			// Kijk hoeveel componenten worden toegevoegd, zodat wij ze verticaal kunnen
			// centreren.
			int k = 0;
			for (int j = 0; j < c.getProblemDefinition()[node.GetID()].length; j++) {
				k += solve[c.getOffsetGrootte()[node.GetID()] + j];
			}

			int offset = -k / 2 * 7;
			// Ga langs elk mogelijke type voor de onbekende
			for (int j = 0; j < c.getProblemDefinition()[node.GetID()].length; j++) {
				// Kijk hoeveel componenten van dat type wij moeten toevoegen.
				int amt = solve[c.getOffsetGrootte()[node.GetID()] + j];
				// Haal de data van het type op.
				NetworkComponentType type = NetworkComponentTypes.getTypes()[c.getProblemDefinition()[node.GetID()][j]];
				// Loop het aantal van amt
				for (int m = 0; m < amt; m++) {
					// Maak een nieuw component aan
					NetworkComponent newComp = new NetworkComponent(type.getName(), type.getName(), type.getCosts(),
							type.getUptime(), node.getComp().getxLoc(), node.getComp().getyLoc() + offset);
					// Voeg het toe aan het ontwerp
					design.getComponents().add(newComp);
					// Voeg de verbindingen toe.
					for (NetworkComponent inComp : inConnections) {
						design.getConnections().add(new NetworkConnection(inComp, newComp));
					}
					for (NetworkComponent outComp : outConnections) {
						design.getConnections().add(new NetworkConnection(newComp, outComp));
					}
					offset += 7;
				}
			}
			// Haal de onbekende component uit het ontwerp
			if (design.getComponents().contains(node.getComp()))
				design.getComponents().remove(node.getComp());
		}
		// Voor alle nodes die er verbonden aan zijn, doe dit opnieuw.
		for (int i = 0; i < node.getNodes().size(); i++) {
			replaceComponents(design, node.getNodes().get(i));
		}
	}
}
