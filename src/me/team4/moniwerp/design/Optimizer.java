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
		c.buildNodeNetwork(ontwerp);
		
		if(c.getFirstNode() == null)
			return;
		
		CulledHierarchy ch = new CulledHierarchy();

		solve = ch.execute(c.getProblemDefinition(), c, minimumUptime);

		for (int i = 0; i < c.getProblemDefinition().length; i++) {
			for (int j = 0; j < c.getProblemDefinition()[i].length; j++) {
				System.out.println(NetworkComponentTypes.getTypes()[c.getProblemDefinition()[i][j]].getName() + ": "
						+ solve[c.getOffsetGrootte()[i] + j]);
			}
		}

		replaceComponents(ontwerp, c.getFirstNode());
	}

	private void replaceComponents(NetworkDesign design, Calculator.Node node) {
		if (node.GetID() >= 0) {
			List<NetworkComponent> inConnections = new ArrayList<NetworkComponent>();
			List<NetworkComponent> outConnections = new ArrayList<NetworkComponent>();
			
			Iterator<NetworkConnection> connections = design.getConnections().iterator();
			NetworkConnection conn = null;
			while(connections.hasNext()) {
				conn = connections.next();
				if(conn.getFirst() == node.getComp()) {
					outConnections.add(conn.getSecond());
					connections.remove();
				}
				if(conn.getSecond() == node.getComp()) {
					inConnections.add(conn.getFirst());
					connections.remove();
				}
			}
			
			int k = 0;
			for (int j = 0; j < c.getProblemDefinition()[node.GetID()].length; j++) {
				k += solve[c.getOffsetGrootte()[node.GetID()] + j];
			}

			int offset = -k / 2 * 7;
			for (int j = 0; j < c.getProblemDefinition()[node.GetID()].length; j++) {
				int amt = solve[c.getOffsetGrootte()[node.GetID()] + j];
				NetworkComponentType type = NetworkComponentTypes.getTypes()[c.getProblemDefinition()[node.GetID()][j]];
				for (int m = 0; m < amt; m++) {
					NetworkComponent newComp = new NetworkComponent(type.getName(), type.getName(), type.getCosts(),
							type.getUptime(), node.getComp().getxLoc(), node.getComp().getyLoc() + offset);
					design.getComponents().add(newComp);
					for(NetworkComponent inComp : inConnections) {
						design.getConnections().add(new NetworkConnection(inComp, newComp));
					}
					for(NetworkComponent outComp : outConnections) {
						design.getConnections().add(new NetworkConnection(newComp, outComp));
					}
					offset += 7;
				}
			}
			if(design.getComponents().contains(node.getComp()))
				design.getComponents().remove(node.getComp());
		}
		for (int i = 0; i < node.getNodes().size(); i++) {
			replaceComponents(design, node.getNodes().get(i));
		}
	}
}
