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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import me.team4.moniwerp.Main;

/*
 * De viewport van het design 
 */
public class ViewportDesign extends JPanel
		implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	private NetworkComponent selected;
	private LinkedList<NetworkDesign> undoQueue = new LinkedList<NetworkDesign>();
	private LinkedList<NetworkDesign> redoQueue = new LinkedList<NetworkDesign>();

	private NetworkDesign design;
	private JFrame frame;
	private float panningX = 1;
	private float panningY = 1;
	private float prevMouseX = 0;
	private float prevMouseY = 0;
	private float prevCompX = 0;
	private float prevCompY = 0;
	private float scale = 5;
	private boolean isPanning;
	private NetworkComponent connFirst = null;
	private int kosten;
	private float uptime;

	public ViewportDesign() {
		// Maak een nieuw design instantie.
		design = new NetworkDesign();
		// In dit geval hardcoded een ontwerp
		NetworkComponent pfSense = new NetworkComponent("pfSense", "firewall", 1000, 0.9999F, 0, 50);
		NetworkComponent w1 = new NetworkComponent("W1", "Webserver", 1000, 0.9999F, 40, 55);
		NetworkComponent w2 = new NetworkComponent("W2", "Webserver", 1000, 0.9999F, 40, 65);
		NetworkComponent lb = new NetworkComponent("LB1", "Loadbalancer", 1000, 0.9999F, 40, 40);
		NetworkComponent db1 = new NetworkComponent("DB1", "Database server", 1000, 0.9999F, 80, 35);
		NetworkComponent db2 = new NetworkComponentUnknown("DB2", "Database server", 1000, 0.9999F, 80, 45,
				new ArrayList<Integer>());
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
		design.calcBounds();

		// We willen kunnen kijken wanneer je er op klikt zodat we een component kunnen
		// laten selecteren. Daarnaast willen we ook nog dat we kunnen kijken of
		// de muis bewogen word en of de scrollknop gebruikt wordt.
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);

		//er word geluisterd of de combinatie van ctrl en z word gebruikt
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_MASK), "undo");
		getActionMap().put("undo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				undo();
			}

		});
		//er word geluisterd of de combinatie van ctrl en y word gebruikt
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Y', KeyEvent.CTRL_MASK), "redo");
		getActionMap().put("redo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				redo();
			}

		});
		
		//zorgt dat er om een specifieke tijd de kosten en uptime word berekend van het gehele netwerk ontwerp
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {			
			@Override
			public void run() {
				Calculator c = new Calculator();
				kosten  = c.calcCosts(design);
				//we doen het maal 100 zodat we een percentage hebben van de uptime
				uptime = c.calcUptime(design) * 100;
			}
		}, 2000, 2000);

	}

	/**
	 * redo knop: Je undo undo-en :)
	 */
	//de functie voor de redo van een aanpassing die je hebt gemaakt van het netwerk ontwerp
	public void redo() {
		if (redoQueue.isEmpty())
			return;
		// wanneer de functie word aangeroepen word de eerste record verwijderd 
		NetworkDesign design = redoQueue.removeFirst();
		// er word een kopie gemaakt van het huidige ontwerp en word toegevoegd aan de queue
		NetworkDesign undoDesign = copyDesign(this.design);
		this.design = design;
		//een maximale aantal ban 10 onderdelen die je kan redo-en
		undoQueue.addFirst(undoDesign);
		if (undoQueue.size() > 10) {
			undoQueue.removeLast();
		}
		repaint();
	}

	/**
	 * undo knop: een stap terug
	 */
	//de functie voor de undo van een aanpassing die je hebt gemaakt van het netwerk ontwerp
	public void undo() {
		if (undoQueue.isEmpty())
			return;
		// wanneer de functie word aangeroepen word de eerste record verwijderd 
		NetworkDesign design = undoQueue.removeFirst();
		// er word een kopie gemaakt van het huidige ontwerp en word toegevoegd aan de queue
		NetworkDesign redoDesign = copyDesign(this.design);
		this.design = design;
		//een maximale aantal ban 10 onderdelen die je kan undo-en
		redoQueue.addFirst(redoDesign);
		if (redoQueue.size() > 10) {
			redoQueue.removeLast();
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Teken de achtergrond. Het is net niet 100% wit.
		g.setColor(new Color(251, 251, 251));
		g.fillRect(0, 0, getWidth(), getHeight());
		// Teken een border. Gewoon een lijntje
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		// De componenten kunnen overal staan. Dus moeten wij een klein beetje wiskunde
		// doen.
		// De formule is x = (pX - offsetX) * scaleX
		// waarbij x de uiteindelijke x locatie in pixels is, pX de x locatie van het
		// component,
		// offsetX de offset op de x as en scaleX de schaal.
		// Wij willen het canvas verschuiven en schalen zodat hij het venster opvult.
		float xOffset = panningX - 10; // -10 is padding
		float yOffset = panningY - 10;
		// Zet de font. Dit doen wij voor de grootte.
		g.setFont(new Font("Arial", Font.PLAIN, (int) (3.5 * scale)));

		// Ga langs elk netwerkcomponent en teken het
		for (NetworkComponent comp : design.getComponents()) {
			// Hij staat aan, dus blauw.
			if (comp == connFirst) {
				g.setColor(new Color(150, 255, 255));
			} else if (comp == selected) {
				if (comp instanceof NetworkComponentUnknown) {
					g.setColor(new Color(150, 255, 200));
				} else {
					// Hij is geselecteerd dus een lichtere blauw.
					g.setColor(new Color(150, 200, 255));
				}
			} else {
				if (comp instanceof NetworkComponentUnknown) {
					g.setColor(new Color(100, 255, 150));
				} else {
					// Hij is niet geselecteerd dus een normale blauw
					g.setColor(new Color(100, 150, 255));
				}
			}

			// Vul de rechthoek. Dit is de basis van een component.
			g.fillRect((int) ((comp.getxLoc() - xOffset) * scale), (int) ((comp.getyLoc() - yOffset) * scale),
					(int) (20 * scale), (int) (5 * scale));
			// Zet de kleur voor de border (randje) dat om de rechthoek wordt getekent.
			if (comp == selected) {
				g.setColor(Color.LIGHT_GRAY);
			} else {
				g.setColor(Color.DARK_GRAY);
			}
			// Teken de border
			g.drawRect((int) ((comp.getxLoc() - xOffset) * scale), (int) ((comp.getyLoc() - yOffset) * scale),
					(int) (20 * scale), (int) (5 * scale));
			// Zet de kleur voor de tekst
			g.setColor(Color.black);
			if (comp instanceof NetworkComponentUnknown) {
				// teken unknown
				int x1 = (int) ((comp.getxLoc() - xOffset) * scale);
				int y1 = (int) ((comp.getyLoc() - yOffset) * scale);

				g.drawLine((int) (x1 + (5 / 3 * scale)), (int) (y1 + (5 / 3F * scale)),
						(int) (x1 + ((20 - 5 / 3) * scale)), (int) (y1 + (5 / 3F * scale)));
				g.drawLine((int) (x1 + (5 / 3 * scale)), (int) (y1 + ((5 - 5 / 3F) * scale)),
						(int) (x1 + ((20 - 5 / 3) * scale)), (int) (y1 + ((5 - 5 / 3F) * scale)));
			} else {
				// Hiermee vragen wij de grootte van de text op in pixels. Dan kunnen wij het
				// centreren.
				Rectangle2D textBounds = g.getFontMetrics().getStringBounds(comp.getNaam(), g);
				// Teken de text. Pak de pixel locatie van het midden van het component en haal
				// de helft van de grootte van de textbounds eraf.
				g.drawString(comp.getNaam(),
						(int) ((comp.getxLoc() - xOffset) * scale + 10.0 * scale - textBounds.getCenterX()),
						(int) ((comp.getyLoc() - yOffset) * scale + 2.5 * scale - textBounds.getCenterY()));
			}
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
				// Eerste component + breedte van component (20 * scale) voor de rechterkant van
				// het component
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale) + (int) (20 * scale);
				// De + (2.5 * scale) is zodat hij in het midden zit qua hoogte.
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);
			} else if (xDiff < -Math.abs(yDiff)) {
				// Naar links toe
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale);
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale) + (int) (20 * scale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);
			} else if (yDiff > Math.abs(xDiff)) {
				// Naar onderen toe
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale) + (int) (10 * scale);
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale) + (int) (5 * scale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale) + (int) (10 * scale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale);
			} else {
				// Naar rechts toe
				x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale) + (int) (10 * scale);
				y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale);

				x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale) + (int) (10 * scale);
				y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale) + (int) (5 * scale);
			}
			// Teken het lijntje
			int radiusX = (int) ((x1 + x2) / 2 - 1.5 * scale + 0.5);
			int radiusY = (int) ((y1 + y2) / 2 - 1.5 * scale + 0.5);

			g.drawLine(x1, y1, x2, y2);
			g.fillOval(radiusX, radiusY, (int) (3 * scale), (int) (3 * scale));
			g.fillRect((int) (x2 - (1 * scale)), (int) (y2 - (1 * scale)), (int) (2 * scale), (int) (2 * scale));
		}
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.setColor(new Color(220, 220, 220));
		g.fillRect(1, 1, 125, 40);
		g.setColor(Color.black);
		g.drawRect(0, 0, 125, 40);
		g.drawString("Kosten: $"+kosten, 5, 15);
		g.drawString("Uptime:" + uptime+"%", 5, 30);
		repaint();
	}

	/**
	 * geeft netwerkontwerpen weer.
	 */
	public NetworkDesign getNetworkDesign() {
		return design;
	}

	/**
	 * run de optimizer, de optimizer optimaliseerd het huidige ontwerp door middel
	 * van CulledHierarchys
	 */
	public void optimize() {
		// TODO
	}

	@Override
	public void mousePressed(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		isPanning = true;

		// Er is geklikt. Wij kijken nu bij elk component de muis in de rechthoek zit.
		// Dit is hetzelfde als bij het tekenen. Wij moeten alle componenten verschuiven
		// en schalen.
		float xOffset = panningX - 10; // -10 is padding
		float yOffset = panningY - 10;

		// Ga langs elk component.
		for (NetworkComponent comp : design.getComponents()) {
			// Krijg de min en max locaties van de rechthoek
			int minX = (int) ((comp.getxLoc() - xOffset) * scale);
			int minY = (int) ((comp.getyLoc() - yOffset) * scale);
			int maxX = minX + (int) (20 * scale);
			int maxY = minY + (int) (5 * scale);
			// Kijk of de muis in de rechthoek zit.
			if (e.getX() >= minX && e.getY() >= minY && e.getX() < maxX && e.getY() < maxY) {
				// Zet de selected variabele naar het goede component
				selected = comp;
				isPanning = false;
				prevCompX = comp.getxLoc();
				prevCompY = comp.getyLoc();
				// Zeg dat de viewport opnieuw moet worden getekent.
				repaint();
				// Wij hebben al een hit gevonden, dus wij stoppen hier.
				return;
			}
		}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		// er word gekeken of de linker klik is gedubbelklikked en of het niet op het kanvas is
		if ((e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) && !e.isConsumed() && !isPanning) {
			// er word gekeken of het gedubbleklikde een onbekende netwerkcomponent is
			if (selected instanceof NetworkComponentUnknown) {
				// er word een dialog geopent waarmee ja kan aangeven welke componenten eraan kunnen zitten
				typeDialoog tD = new typeDialoog(frame);
				tD.setSelected((NetworkComponentUnknown) selected);
			} else {
				// hij laat een dialog zien waarmee je de naam van een netwerkcomponenet kan veranderen
				naamDialoog nD = new naamDialoog(frame);
				nD.setSelected(selected);
			}
		}
		// er word gekeken of er op de linker klik is gedrukt en of er het niet op het kanvas is
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (!isPanning) {
				NetworkComponentType type = Main.getWindow().getDesignTab().getToolbar().getSelected();
				if (Main.getWindow().getDesignTab().getToolbar().useConnectiontool()) {
					// Maak een verbinding
					if (connFirst == null) {
						connFirst = selected;
					} else if (connFirst != selected) {
						NetworkConnection conn = new NetworkConnection(connFirst, selected);
						connFirst = null;
						addHistory();
						design.getConnections().add(conn);
					}
					repaint();
				} else {
					connFirst = null;
					// Maak een nieuw component
					float xOffset = panningX - 10; // -10 is padding
					float yOffset = panningY - 10;
					int xLoc = (int) (prevMouseX / scale + xOffset);
					int yLoc = (int) (prevMouseY / scale + yOffset);
					NetworkComponent comp = null;
					if (type.getName().equals("Unknown")) {
						comp = new NetworkComponentUnknown(type.getName(), type.getName(), type.getCosts(),
								type.getUptime(), xLoc, yLoc, new ArrayList<Integer>());
					} else {
						comp = new NetworkComponent(type.getName(), type.getName(), type.getCosts(), type.getUptime(),
								xLoc, yLoc);
					}
					addHistory();
					design.getComponents().add(comp);
					repaint();
				}
			}
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			// Er is geklikt. Wij kijken nu bij elk component de muis in de rechthoek zit.
			// Dit is hetzelfde als bij het tekenen. Wij moeten alle componenten verschuiven
			// en schalen.
			float xOffset = panningX - 10; // -10 is padding
			float yOffset = panningY - 10;

			NetworkComponent compRem = null;

			// Ga langs elk component.
			for (NetworkComponent comp : design.getComponents()) {
				// Krijg de min en max locaties van de rechthoek
				int minX = (int) ((comp.getxLoc() - xOffset) * scale);
				int minY = (int) ((comp.getyLoc() - yOffset) * scale);
				int maxX = minX + (int) (20 * scale);
				int maxY = minY + (int) (5 * scale);
				// Kijk of de muis in de rechthoek zit.
				if (e.getX() >= minX && e.getY() >= minY && e.getX() < maxX && e.getY() < maxY) {
					// Zet de selected variabele naar het goede component
					compRem = comp;
					selected = null;
					// Wij hebben al een hit gevonden, dus wij stoppen hier.
					break;
				}
			}
			if (compRem != null) {
				addHistory();
				design.getComponents().remove(compRem);
				Iterator<NetworkConnection> it = design.getConnections().iterator();
				while (it.hasNext()) {
					NetworkConnection conn = it.next();
					if (conn.getFirst() == compRem || conn.getSecond() == compRem)
						it.remove();
				}
				repaint();
			} else {
				NetworkConnection connRem = null;
				// Ga langs elke connectie en teken die.
				for (NetworkConnection con : design.getConnections()) {
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
						// Eerste component + breedte van component (20 * scale) voor de rechterkant van
						// het component
						x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale) + (int) (20 * scale);
						// De + (2.5 * scale) is zodat hij in het midden zit qua hoogte.
						y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);

						x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale);
						y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);
					} else if (xDiff < -Math.abs(yDiff)) {
						// Naar links toe
						x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale);
						y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);

						x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale) + (int) (20 * scale);
						y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale) + (int) (2.5 * scale);
					} else if (yDiff > Math.abs(xDiff)) {
						// Naar onderen toe
						x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale) + (int) (10 * scale);
						y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale) + (int) (5 * scale);

						x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale) + (int) (10 * scale);
						y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale);
					} else {
						// Naar rechts toe
						x1 = (int) ((con.getFirst().getxLoc() - xOffset) * scale) + (int) (10 * scale);
						y1 = (int) ((con.getFirst().getyLoc() - yOffset) * scale);

						x2 = (int) ((con.getSecond().getxLoc() - xOffset) * scale) + (int) (10 * scale);
						y2 = (int) ((con.getSecond().getyLoc() - yOffset) * scale) + (int) (5 * scale);
					}
					// Teken het lijntje
					int minX = (int) ((x1 + x2) / 2 - 1.5 * scale + 0.5);
					int minY = (int) ((y1 + y2) / 2 - 1.5 * scale + 0.5);
					int maxX = minX + ((int) (3 * scale));
					int maxY = minY + ((int) (3 * scale));
					// Kijk of de muis in de rechthoek zit.
					if (e.getX() >= minX && e.getY() >= minY && e.getX() < maxX && e.getY() < maxY) {
						// Zet de selected variabele naar het goede component
						connRem = con;
						// Wij hebben al een hit gevonden, dus wij stoppen hier.
						break;
					}
				}

				if (connRem != null) {
					addHistory();
					design.getConnections().remove(connRem);
					repaint();
				}

			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		scale += e.getPreciseWheelRotation();
		if (scale < 1) {
			scale = 1;
		}
		if (scale > 25) {
			scale = 25;
		}
		repaint();
	}

	@Override
	// 
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (isPanning) {
			panningX -= (e.getX() - prevMouseX) / scale;
			panningY -= (e.getY() - prevMouseY) / scale;
		} else {
			prevCompX += (e.getX() - prevMouseX) / scale;
			prevCompY += (e.getY() - prevMouseY) / scale;
			selected.setxLoc((int) prevCompX);
			selected.setyLoc((int) prevCompY);
		}
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private NetworkDesign copyDesign(NetworkDesign design) {
		NetworkDesign designCopy = new NetworkDesign();
		HashMap<NetworkComponent, NetworkComponent> compConv = new HashMap<NetworkComponent, NetworkComponent>();
		for (NetworkComponent comp : design.getComponents()) {
			if (comp instanceof NetworkComponentUnknown) {
				NetworkComponent compCopy = new NetworkComponentUnknown(comp.getNaam(), comp.getType(), comp.getCosts(),
						comp.getUptime(), comp.getxLoc(), comp.getyLoc(),
						((NetworkComponentUnknown) comp).GetComponentTypes());
				designCopy.getComponents().add(compCopy);
				compConv.put(comp, compCopy);
			} else {
				NetworkComponent compCopy = new NetworkComponent(comp.getNaam(), comp.getType(), comp.getCosts(),
						comp.getUptime(), comp.getxLoc(), comp.getyLoc());
				designCopy.getComponents().add(compCopy);
				compConv.put(comp, compCopy);
			}

		}
		for (NetworkConnection conn : design.getConnections()) {
			NetworkConnection connCopy = new NetworkConnection(compConv.get(conn.getFirst()),
					compConv.get(conn.getSecond()));
			designCopy.getConnections().add(connCopy);
		}
		return designCopy;
	}

	public void addHistory() {
		undoQueue.addFirst(copyDesign(design));
		if (undoQueue.size() > 10)
			undoQueue.removeLast();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public NetworkComponent getSelected() {
		return selected;
	}
}
