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
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

import me.team4.moniwerp.Main;
import me.team4.moniwerp.io.DataRetriever;
import me.team4.moniwerp.io.MonitorData;

/**
 * De grafiek die de geschiedenis weergeeft van de waardes van een
 * netwerkcomponent.
 *
 */
public class ViewportGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * De hoeveelheid seconden aan geschiedenis dat wordt weergegeven.
	 */
	private final static int minuut = 60;
	private final static int uur = 60 * 60;
	private final static int dag = 60 * 60 * 24;
	private final static int week = 60 * 60 * 24 * 7;
	private int timeRange = 20; // 1 dag

	public ViewportGraph() {
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/**
		 * De breedte en hoogte van de rechthoek worden hier in een variable gezet die
		 * vervolgens samen met de padding gebruikt worden om het grafiek te maken
		 */
		int breedte = g.getClipBounds().width;
		int hoogte = g.getClipBounds().height;
		int padding = 40;
		// Achtergrond voor de grafiek
		g.setColor(new Color(255, 255, 255));
		g.fillRect(padding, padding, breedte - padding * 2, hoogte - padding * 2);

		// Datalijnen voor de grafiek
		g.setColor(new Color(211, 211, 211));

		// Horizontale lijnen voor de grafiek
		int amtHorzLines = 4;
		for (float x = 0; x < 1.0; x += (1.0f / amtHorzLines)) {
			drawHorizontalline(x, g);
		}

		// Verticale lijnen voor de grafiek
		int amtVertLines = 5;
		// TODO: set amtVertLines
		if (timeRange <= minuut) {
			amtVertLines = Math.max(timeRange / (4), 4);
		} else if (timeRange <= uur) {
			amtVertLines = Math.max(timeRange / (1 * 60), 4);
		} else if (timeRange <= dag) {
			amtVertLines = Math.max(timeRange / (30 * 60), 4);
		} else if (timeRange <= week) {
			amtVertLines = Math.max(timeRange / (60 * 60 * 12), 4);
		} else {
			amtVertLines = Math.max(timeRange / (60 * 60 * 24 * 2), 4);
		}

		for (float x = 0; x < 1.0; x += (1.0f / amtVertLines)) {
			drawVerticalline(x, g);
		}

		// Lijnen voor de grafiek
		g.setColor(new Color(0, 0, 0));
		g.drawLine(padding, padding, padding, hoogte - padding);
		g.drawLine(breedte - padding, hoogte - padding, padding, hoogte - padding);

		// De data specifiekatie
		String t1 = "";
		if (timeRange > dag * 2) {
			t1 = (timeRange / (dag)) + " dagen";
		} else if (timeRange > uur * 2) {
			t1 = (timeRange / (uur)) + " uren";
		} else if (timeRange > minuut * 2) {
			t1 = (timeRange / (minuut)) + " minuten";
		} else {
			t1 = timeRange + " seconden";
		}

		g.drawString("nu", breedte - padding - 10, hoogte - padding + 10);
		g.drawString(t1, padding, hoogte - padding + 10);

		g.drawString("0%", padding - 20, hoogte - padding);
		g.drawString("100%", padding - 30, padding);

		int prevX;
		int prevY;

		LinkedList<MonitorData> data = DataRetriever.getInstance().getDataForComponent(
				Main.getWindow().getMonitorTab().getInfoList().getSelectedComponent());
	}

	private void drawHorizontalline(double x, Graphics g) {
		int breedte = g.getClipBounds().width;
		int hoogte = g.getClipBounds().height;
		int padding = 40;
		g.drawLine(breedte - padding, (int) (padding + (hoogte - padding * 2) * x), padding,
				(int) (padding + (hoogte - padding * 2) * x));
	}

	private void drawVerticalline(double x, Graphics g) {
		int breedte = g.getClipBounds().width;
		int hoogte = g.getClipBounds().height;
		int padding = 40;
		g.drawLine((int) (padding + (breedte - padding * 2) * x), padding,
				(int) (padding + (breedte - padding * 2) * x), hoogte - padding);
	}

	/**
	 * Deze methode wordt geroepen als er een update moet plaatsvinden. Deze wordt
	 * elke seconde geroepen door TabMonitor
	 */
	public void update() {
		repaint();
		// TODO: implement
	}

}
