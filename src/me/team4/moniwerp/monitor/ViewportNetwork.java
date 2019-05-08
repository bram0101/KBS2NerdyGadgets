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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import me.team4.moniwerp.Main;
import me.team4.moniwerp.design.NetworkComponent;
import me.team4.moniwerp.design.NetworkConnection;
import me.team4.moniwerp.design.NetworkDesign;
import me.team4.moniwerp.io.DataRetriever;

/**
 * Grafische representatie van het netwerk
 *
 */
public class ViewportNetwork extends JPanel implements MouseListener {

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
		// Maak een nieuw design instantie.
		design = new NetworkDesign();
		// In dit geval hardcoded een ontwerp
		NetworkComponent pfSense = new NetworkComponent("pfSense", "firewall", 1000, 0.9999F, 0, 50);
		NetworkComponent w1 = new NetworkComponent("W1", "Webserver", 1000, 0.9999F, 40, 55);
		NetworkComponent w2 = new NetworkComponent("W2", "Webserver", 1000, 0.9999F, 40, 65);
		NetworkComponent lb = new NetworkComponent("LB2", "Loadbalancer", 1000, 0.9999F, 40, 40);
		NetworkComponent db1 = new NetworkComponent("DB1", "Database server", 1000, 0.9999F, 80, 35);
		NetworkComponent db2 = new NetworkComponent("DB2", "Database server", 1000, 0.9999F, 80, 45);
		design.getComponents().add(pfSense);
		design.getComponents().add(w1);
		design.getComponents().add(w2);
		design.getComponents().add(lb);
		design.getComponents().add(db1);
		design.getComponents().add(db2);
		design.getConnections().add(new NetworkConnection(pfSense, w1));
		design.getConnections().add(new NetworkConnection(pfSense, w2));
		design.getConnections().add(new NetworkConnection(pfSense, lb));
		design.getConnections().add(new NetworkConnection(lb, db1));
		design.getConnections().add(new NetworkConnection(lb, db2));

		// We willen kunnen kijken wanneer je er op klikt zodat we een component kunnen
		// laten selecteren.
		addMouseListener(this);
	}

	/**
	 * Geeft het geselecteerde netwerkcomponent
	 * 
	 * @return Het geselecteerde netwerkcomponent
	 */
	public NetworkComponent getSelectedNetworkComponent() {
		return selected;
	}

	public NetworkDesign getNetworkDesign() {
		return design;
	}

	public void setNetworkDesign(NetworkDesign design) {
		this.design = design;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Teken de achtergrond. Het is net niet 100% wit.
		g.setColor(new Color(240, 240, 240));
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		// Teken een border. Gewoon een lijntje
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

		// De componenten kunnen overal staan. Dus moeten wij een klein beetje wiskunde
		// doen.
		// De formule is x = (pX - offsetX) * scaleX
		// waarbij x de uiteindelijke x locatie in pixels is, pX de x locatie van het
		// component,
		// offsetX de offset op de x as en scaleX de schaal.
		// Wij willen het canvas verschuiven en schalen zodat hij het venster opvult.
		int xOffset = design.getBounds()[0] - 10; // -10 is padding
		int yOffset = design.getBounds()[1] - 10;
		// breedte van de viewport / (maxX - minY + padding*2 (links en rechts))
		float xScale = g.getClipBounds().width / ((float) (design.getBounds()[2] - design.getBounds()[0]) + 20f);
		float yScale = g.getClipBounds().height / ((float) (design.getBounds()[3] - design.getBounds()[1]) + 20f);
		// De schaal voor de grootte van het blokje berekenen.
		// Hiervoor kijken wij naar de schaal voor de x en voor de y en nemen daar het
		// kleinste van.
		// Hierdoor heeft elk blokje dezelfde grootte.
		float widthScale = ((float) g.getClipBounds().width) / (design.getBounds()[2] - design.getBounds()[0]);
		float heightScale = ((float) g.getClipBounds().height) / (design.getBounds()[3] - design.getBounds()[1]);
		float sizeScale = Math.min(widthScale, heightScale);
		// Zet de font. Dit doen wij voor de grootte.
		g.setFont(new Font("Arial", Font.PLAIN, (int) (3.5 * sizeScale)));

		// Ga langs elk netwerkcomponent en teken het
		for (NetworkComponent comp : design.getComponents()) {
			// Afhankelijk van de status van het component geven wij het een andere kleur.
			if (DataRetriever.getInstance().getStatusForComponent(comp.getNaam())) {
				// Hij staat aan, dus blauw.
				if (comp == selected) {
					// Hij is geselecteerd dus een lichtere blauw.
					g.setColor(new Color(150, 200, 255));
				} else {
					// Hij is niet geselecteerd dus een normale blauw
					g.setColor(new Color(100, 150, 255));
				}
			} else {
				// Hij staat uit dus rood
				if (comp == selected) {
					// Hij is geselecteerd dus een lichtere rood
					g.setColor(new Color(230, 90, 90));
				} else {
					// Hij is niet geselecteerd dus een normale rood
					g.setColor(new Color(210, 50, 50));
				}
			}
			// Vul de rechthoek. Dit is de basis van een component.
			g.fillRect((int) ((comp.getxLoc() - xOffset) * xScale), (int) ((comp.getyLoc() - yOffset) * yScale),
					(int) (20 * sizeScale), (int) (5 * sizeScale));
			// Zet de kleur voor de border (randje) dat om de rechthoek wordt getekent.
			if (comp == selected) {
				g.setColor(Color.LIGHT_GRAY);
			} else {
				g.setColor(Color.DARK_GRAY);
			}
			// Teken de border
			g.drawRect((int) ((comp.getxLoc() - xOffset) * xScale), (int) ((comp.getyLoc() - yOffset) * yScale),
					(int) (20 * sizeScale), (int) (5 * sizeScale));
			// Zet de kleur voor de tekst
			g.setColor(Color.black);
			// Hiermee vragen wij de grootte van de text op in pixels. Dan kunnen wij het
			// centreren.
			Rectangle2D textBounds = g.getFontMetrics().getStringBounds(comp.getNaam(), g);
			// Teken de text. Pak de pixel locatie van het midden van het component en haal
			// de helft van de grootte van de textbounds eraf.
			g.drawString(comp.getNaam(),
					(int) ((comp.getxLoc() - xOffset) * xScale + 10.0 * sizeScale - textBounds.getCenterX()),
					(int) ((comp.getyLoc() - yOffset) * yScale + 2.5 * sizeScale - textBounds.getCenterY()));
		}
		// Ga langs elke connectie en teken die.
		for (NetworkConnection con : design.getConnections()) {
			// Zet de kleur naar zwart
			g.setColor(Color.black);
			// Als de componenten onder elkaar zitten dan willen wij de lijn van de
			// onderkant naar de bovenkant laten gaan. Zitten de componenten naast elkaar,
			// dan willen wij dat de lijn naar de zijkanten gaan. x1, y1, x2 en y2 zijn de
			// locaties van het beginpunt en eindpunt van de lijntjes en die worden gezet
			// naar de goede waarde afhankelijk van waar de componenten staan
			int x1 = 0;
			int y1 = 0;
			int x2 = 0;
			int y2 = 0;
			// Verschil in x en y locatie berekenen. Hiermee kunnen wij kijken of het
			// lijntje naar rechts, links, boven of onderen toe moet
			float xDiff = con.getSecond().getxLoc() - con.getFirst().getxLoc();
			float yDiff = con.getSecond().getyLoc() - con.getFirst().getyLoc();
			if (xDiff > Math.abs(yDiff)) { 
				// Naar rechts toe
				// Eerste component + breedte van component (20 * sizeScale) voor de rechterkant van het component
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * xScale) + (int) (20 * sizeScale);
				// De + (2.5 * sizeScale) is zodat hij in het midden zit qua hoogte.
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * yScale) + (int) (2.5 * sizeScale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * xScale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * yScale) + (int) (2.5 * sizeScale);
			} else if (xDiff < -Math.abs(yDiff)) { 
				// Naar links toe
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * xScale);
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * yScale) + (int) (2.5 * sizeScale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * xScale) + (int) (20 * sizeScale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * yScale) + (int) (2.5 * sizeScale);
			} else if (yDiff > Math.abs(xDiff)) { 
				// Naar onderen toe
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * xScale) + (int) (10 * sizeScale);
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * yScale) + (int) (5 * sizeScale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * xScale) + (int) (10 * sizeScale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * yScale);
			} else { 
				// Nar rechts toe
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * xScale) + (int) (10 * sizeScale);
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * yScale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * xScale) + (int) (10 * sizeScale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * yScale) + (int) (5 * sizeScale);
			}
			// Teken het lijntje
			g.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * Deze methode wordt geroepen als er een update moet plaatsvinden. Deze wordt
	 * elke seconde geroepen door TabMonitor
	 */
	public void update() {
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Er is geklikt. Wij kijken nu bij elk component de muis in de rechthoek zit.
		// Dit is hetzelfde als bij het tekenen. Wij moeten alle componenten verschuiven en schalen.
		int xOffset = design.getBounds()[0] - 10;
		int yOffset = design.getBounds()[1] - 10;
		float xScale = this.getWidth() / ((float) (design.getBounds()[2] - design.getBounds()[0]) + 20f);
		float yScale = this.getHeight() / ((float) (design.getBounds()[3] - design.getBounds()[1]) + 20f);
		float widthScale = ((float) this.getWidth()) / 100f;
		float heightScale = ((float) this.getHeight()) / 100f;
		float sizeScale = Math.min(widthScale, heightScale);
		
		// Ga langs elk component.
		for (NetworkComponent comp : design.getComponents()) {
			// Krijg de min en max locaties van de rechthoek
			int minX = (int) ((comp.getxLoc() - xOffset) * xScale);
			int minY = (int) ((comp.getyLoc() - yOffset) * yScale);
			int maxX = minX + (int) (20 * sizeScale);
			int maxY = minY + (int) (5 * sizeScale);
			// Kijk of de muis in de rechthoek zit.
			if (e.getX() >= minX && e.getY() >= minY && e.getX() < maxX && e.getY() < maxY) {
				// Zet de selected variabele naar het goede component
				selected = comp;
				// Zeg dat de viewport opnieuw moet worden getekent.
				repaint();
				// Vertel de infolijst het
				Main.getWindow().getMonitorTab().getInfoList().setSelectedComponent(comp.getNaam());
				// Wij hebben al een hit gevonden, dus wij stoppen hier.
				return;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
