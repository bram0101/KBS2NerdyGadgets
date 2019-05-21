/*MIT License

Copyright (c) 2019 Bram Stout, Dylan R�sch, Fiene Botha, Roland Regtop, Sven Reijne, Syb van Gurp

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	public NetworkDesign() {
		this.minX = 0;
		this.minY = 0;
		this.maxX = 100;
		this.maxY = 100;
		components = new ArrayList<NetworkComponent>();
		connections = new ArrayList<NetworkConnection>();
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

		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;

		// kijk voor elk netwerkcomponent
		for (int i = 0; i < components.size(); i++) {

			// kijk of de X locatie binnen de marges valt, zo niet word het canvas groter.
			if (components.get(i).getxLoc() < minX) {
				this.minX = components.get(i).getxLoc();

				// kijk of de X locatie binnen de marges valt, zo niet word het canvas groter.
			}
			if (components.get(i).getxLoc() + 20 > maxX) {
				this.maxX = components.get(i).getxLoc() + 20;

				// kijk of de Y locatie binnen de marges valt, zo niet word het canvas groter.
			}
			if (components.get(i).getyLoc() < minY) {
				this.minY = components.get(i).getyLoc();

				// kijk of de Y locatie binnen de marges valt, zo niet word het canvas groter.
			}
			if (components.get(i).getyLoc() + 5 > maxY) {
				this.maxY = components.get(i).getyLoc() + 5;
			}
		}
	}

	/**
	 * Sla dit ontwerp op naar de DataOutputStream
	 *
	 * @param dos
	 */
	public void save(DataOutputStream dos) {
		try {
			dos.writeInt(0x4d574400); // magic number voor het formaat MWD
			dos.writeByte(0x02); // versienummer
			dos.writeInt(minX);
			dos.writeInt(minY);
			dos.writeInt(maxX);
			dos.writeInt(maxY);
			dos.writeInt(components.size());

			// geeft een nummer aan de netwerkcomponenten tijdens het ophalen en zet deze in
			// de HashMap.
			HashMap<NetworkComponent, Integer> idConv = new HashMap<NetworkComponent, Integer>();
			int id = 0;
			for (NetworkComponent comp : components) {
				dos.writeInt(id);
				dos.writeUTF(comp.getNaam());
				dos.writeUTF(comp.getType());
				dos.writeInt(comp.getCosts());
				dos.writeDouble(comp.getUptime());
				dos.writeInt(comp.getxLoc());
				dos.writeInt(comp.getyLoc());
				if (comp instanceof NetworkComponentUnknown) {
					NetworkComponentUnknown compU = (NetworkComponentUnknown) comp;
					dos.writeInt(compU.GetComponentTypes().size());
					for (int i = 0; i < compU.GetComponentTypes().size(); i++) {
						dos.writeInt(compU.GetComponentTypes().get(i));
					}
				}

				// vult de hashmap met het ID en het componentobject
				idConv.put(comp, id);

				// elk id moet uniek worden, dus doen we een auto-increment
				id++;
			}

			dos.writeInt(connections.size());
			// loopt door de connections en zet het componentID erbij
			for (NetworkConnection con : connections) {
				dos.writeInt(idConv.get(con.getFirst()));
				dos.writeInt(idConv.get(con.getSecond()));

				// er wordt idConv gebruikt, want de component object zelf kan niet worden
				// gebruikt, dus is het componentID nodig.
			}
			dos.flush(); // schrijft de data daadwerkelijk weg

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Laad het ontwerp uit de DataInputStream
	 *
	 * @param dis
	 */
	public void load(DataInputStream dis) {
		try {
			// als de magicnumber niet klopt, geen .MWD
			if (dis.readInt() != 0x4d574400) {
				throw new IOException("Bestand is niet het goede formaat");
			}
			// check of versienummer klopt
			if (dis.readByte() != 0x02) {
				throw new IOException("Incorrect versienummer");
			}
			// leegt de lijst
			connections.clear();
			components.clear();

			// leest de waardes
			this.minX = dis.readInt();
			this.minY = dis.readInt();
			this.maxX = dis.readInt();
			this.maxY = dis.readInt();
			int componentSize = dis.readInt();

			// voegt de componenten toe aan de lijst, gebruikt idConv om het
			// networkcomponent nummer om te zetten naar de naam
			HashMap<Integer, NetworkComponent> idConv = new HashMap<Integer, NetworkComponent>();
			for (int i = 0; i < componentSize; i++) {
				int id = dis.readInt();
				String naam = dis.readUTF();
				String type = dis.readUTF();
				int costs = dis.readInt();
				double uptime = dis.readDouble();
				int xLoc = dis.readInt();
				int yLoc = dis.readInt();
				NetworkComponent comp = null;
				if (type.equals("Unknown")) {
					List<Integer> compList = new ArrayList<Integer>();
					int compListSize = dis.readInt();
					for (int j = 0; j < compListSize; j++) {
						compList.add(dis.readInt());
					}
					comp = new NetworkComponentUnknown(naam, type, costs, uptime, xLoc, yLoc, compList);
				} else {
					comp = new NetworkComponent(naam, type, costs, uptime, xLoc, yLoc);
				}
				idConv.put(id, comp);
				components.add(comp);
			}
			// kijkt hoe groot de connectionlijst is
			int connectionSize = dis.readInt();
			// loopt voor het aantal keer dat de lijst groot is, leest first en second en
			// voegt het ID toe
			for (int i = 0; i < connectionSize; i++) {
				int first = dis.readInt();
				int second = dis.readInt();

				// kijkt welk networkcomponent het is
				NetworkComponent nc1 = idConv.get(first);
				NetworkComponent nc2 = idConv.get(second);

				NetworkConnection con = new NetworkConnection(nc1, nc2);
				// voegt de connections toe aan de lijst
				connections.add(con);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lijst van netwerkcomponenten die in deze lijst zitten
	 *
	 * @return
	 */
	public List<NetworkComponent> getComponents() {
		return components;
	}

	/**
	 * Lijst van verbindingen tussen netwerkcomponenten
	 *
	 * @return
	 */
	public List<NetworkConnection> getConnections() {
		return connections;
	}

}
