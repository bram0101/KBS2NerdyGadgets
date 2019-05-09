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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

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

	// De hoeveelheid seconden per tijdsnotatie
	private final static int minuut = 60; // 1 minuut in seconden
	private final static int uur = 60 * 60; // 1 uur in seconden
	private final static int dag = 60 * 60 * 24; // 1 dag in seconden
	private final static int week = 60 * 60 * 24 * 7; // 1 week in seconden
	// De hoeveelheid seconden aan geschiedenis dat wordt weergegeven in de grafiek.
	private int timeRange = minuut;

	private BufferedImage img;

	public ViewportGraph() {
		img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.drawImage(img, 0, 0, null);
		// De breedte en hoogte van de rechthoek worden hier in een variable gezet die
		// vervolgens samen met de padding gebruikt worden om het grafiek te maken
		int breedte = g.getClipBounds().width;
		int hoogte = g.getClipBounds().height;
		int padding = 40;
		// De breedte en hoogte van het grafiek zelf
		int breedteGraf = breedte - padding * 2;
		int hoogteGraf = hoogte - padding * 2;
		// De achtergrond van het grafiek
		g.setColor(new Color(40, 40, 40));
		g.fillRect(padding, padding, breedte - padding * 2, hoogte - padding * 2);

		// Horizontale lijnen voor de grafiek
		g.setColor(new Color(96, 96, 96));
		int amtHorzLines = 4;
		for (float x = 0; x < 1.0; x += (1.0f / amtHorzLines)) {
			drawHorizontalline(x, g);
		}

		// Verticale lijnen voor de grafiek
		int amtVertLines = 0;
		// De vertikale lijnen worden getekend aan de hand van de timeRange
		if (timeRange <= minuut) {
			// Er word gekeken hoevaak de timerange in de gegeven tijdsnotitie past, als die
			// er vaker in past dan maak je meer dan 4 lijnen
			// anders worden er 4 lijnen getekend
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

		// De data specifiekatie van de grafiek word gedefinieerd
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

		g.setColor(Color.black);

		// Er word een label gegeven voor de x en y-as
		g.drawString("nu", breedte - padding - 10, hoogte - padding + 10);
		g.drawString(t1, padding, hoogte - padding + 10);
		g.drawString("0%", padding - 20, hoogte - padding);
		g.drawString("100%", padding - 30, padding);

		// Alle monitor data word in een linkedlist gezet
		LinkedList<MonitorData> data = DataRetriever.getInstance()
				.getDataForComponent(Main.getWindow().getMonitorTab().getInfoList().getSelectedComponent());
		if (data != null) {

			long currentTimestamp = System.currentTimeMillis() / 1000L; // Huidige timestamp
			java.util.ListIterator<MonitorData> list_Iter = data.listIterator(0);// maak een iterator. hiermee kunnen
																					// wij
																					// over elk item in de lijst gaan.

			// Er word anti aliasing aangezet voor de grafiek en de bij horende data
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// De dikte van de lijn voor de grafiek word gezet naar 2 pixel
			((Graphics2D) g).setStroke(new BasicStroke(3));

			// De y posite van de datapunten worden gemaakt en in op een latere punt
			// gebruikt
			int prevX = 0;
			int prevYCPU = 0;
			int prevYRAM = 0;
			int prevYDisk = 0;
			int prevYBusy = 0;
			int prevYSend = 0;
			int prevYReceived = 0;
			float maxBytesSendReceived = 100000;

			// Ga door de data en hou de maximale waardes bij zodat wij de grafiek goed
			// kunnen schalen.
			while (list_Iter.hasNext()) {
				MonitorData data1 = list_Iter.next(); // Nu heb je de data in de variabele 'data' gedaan
				if (data1.getTimestamp() < currentTimestamp - timeRange) {
					break; // De data is verder dan onze timerange, dus stoppen wij hier met 'break'
				}
				maxBytesSendReceived = Math.max(maxBytesSendReceived, Math.max(data1.getBytesSent(), data1.getBytesReceived()));
			}

			list_Iter = data.listIterator(0);// reset de iterator
			while (list_Iter.hasNext()) {
				MonitorData data1 = list_Iter.next(); // Nu heb je de data in de variabele 'data' gedaan
				if (data1.getTimestamp() < currentTimestamp - timeRange) {
					break; // De data is verder dan onze timerange, dus stoppen wij hier met 'break'
				}
				// de data van het totale ram en gebruikte ram worden door elkaar gedeeld voor
				// een percentage
				float RAMused = data1.getRamUsed() / data1.getRamTotal();
				// de data van de totale schijf ruimte en gebruikte schijf tuimte worden door
				// elkaar gedeeld voor een percentage
				float diskused = data1.getDiskUsed() / data1.getDiskTotal();
				// de data van de busy time van de disk word gedeeld door de maximale busy time
				// voor een percentage
				float busy = data1.getDiskBusyTime() / 1000F;
				// de data ven de verzonden bytes worden gedeeld door honderdduizend(bytes) voor
				// een percentage
				float send = data1.getBytesSent() / maxBytesSendReceived;
				// de data van de received bytes worden gedeeld door honderdduizen(bytes) voor
				// een percentage
				float received = data1.getBytesReceived() / maxBytesSendReceived;

				long minVal = currentTimestamp - 2; // Het huidige tijdsstip
				long maxVal = currentTimestamp - timeRange; // Maximale timeRange voor de grafiek
				float p = ((float) (data1.getTimestamp() - minVal)) / ((float) (maxVal - minVal)); // percentage van de
																									// timeStamp
				if (p < 0)
					continue; // buiten het venster
				int xPos = Math.round(breedte - (padding + p * breedteGraf)); //
				// De y positie van de gemonitoorde data word berekend door de percentage van de
				// cpu te vermenigvuldigen
				// met de hoogte van de grafiek, die data vervolgens bij de padding optellen en
				// dan de data van de totale hoogte
				// aftrekken
				int yPosCPU = Math.round(hoogte - (padding + data1.getCpu() * hoogteGraf));
				int yPosRAM = Math.round(hoogte - (padding + hoogteGraf * RAMused));
				int yPosDisk = Math.round(hoogte - (padding + hoogteGraf * diskused));
				int yPosBusy = Math.round(hoogte - (padding + hoogteGraf * busy));
				int yPosSend = Math.round(hoogte - (padding + hoogteGraf * send));
				int yPosReceived = Math.round(hoogte - (padding + hoogteGraf * received));

				// er word gekeken of de voorkeur van de x positie gelijk is aan nul, zo ja dan
				// worden de posities van de vorige
				// positie gebruikt anders word de berekende positie gebruikt
				if (prevX == 0) {
					prevX = xPos;
					prevYCPU = yPosCPU;
					prevYRAM = yPosRAM;
					prevYDisk = yPosDisk;
					prevYBusy = yPosBusy;
					prevYSend = yPosSend;
					prevYReceived = yPosReceived;
				}
				// Een geselecteer monitor item word weergegeven in de grafiek met een gegeven
				// kleur en transpirantie
				if (isSelected("cpu")) {
					Color colorcpu = Main.getWindow().getMonitorTab().getInfoList().getColours().get("cpu");
					g.setColor(new Color(colorcpu.getRed(), colorcpu.getGreen(), colorcpu.getBlue(), 200));
					g.drawLine(prevX, prevYCPU - 1, xPos, yPosCPU - 1);
				}
				if (isSelected("ram")) {
					Color colorram = Main.getWindow().getMonitorTab().getInfoList().getColours().get("ram");
					g.setColor(new Color(colorram.getRed(), colorram.getGreen(), colorram.getBlue(), 200));
					g.drawLine(prevX, prevYRAM - 1, xPos, yPosRAM - 1);
				}
				if (isSelected("diskUsage")) {
					Color colordisk = Main.getWindow().getMonitorTab().getInfoList().getColours().get("diskUsage");
					g.setColor(new Color(colordisk.getRed(), colordisk.getGreen(), colordisk.getBlue(), 200));
					g.drawLine(prevX, prevYDisk - 1, xPos, yPosDisk - 1);
				}
				if (isSelected("diskBusyTime")) {
					Color colorbusy = Main.getWindow().getMonitorTab().getInfoList().getColours().get("diskBusyTime");
					g.setColor(new Color(colorbusy.getRed(), colorbusy.getGreen(), colorbusy.getBlue(), 200));
					g.drawLine(prevX, prevYBusy - 1, xPos, yPosBusy - 1);
				}

				if (isSelected("bytesSend")) {
					Color colorsend = Main.getWindow().getMonitorTab().getInfoList().getColours().get("bytesSend");
					g.setColor(new Color(colorsend.getRed(), colorsend.getGreen(), colorsend.getBlue(), 200));
					g.drawLine(prevX, prevYSend - 1, xPos, yPosSend - 1);
				}
				if (isSelected("bytesReceived")) {
					Color colorreceived = Main.getWindow().getMonitorTab().getInfoList().getColours()
							.get("bytesReceived");
					g.setColor(
							new Color(colorreceived.getRed(), colorreceived.getGreen(), colorreceived.getBlue(), 200));
					g.drawLine(prevX, prevYReceived - 1, xPos, yPosReceived - 1);
				}

				// De berekende x en y positie worden in een variable gezet die er voor zorgt
				// dat de vorige positie word
				// opgeslagen voor het tekenen van de lijnen tussen de punten
				prevX = xPos;
				prevYCPU = yPosCPU;
				prevYRAM = yPosRAM;
				prevYDisk = yPosDisk;
				prevYBusy = yPosBusy;
				prevYSend = yPosSend;
				prevYReceived = yPosReceived;

			}
		}
		// De dikte van de lijn voor de grafiek word gezet naar 1 pixel
		((Graphics2D) g).setStroke(new BasicStroke(1));
		// De lijnen voor de grafiek worden getekend
		g.setColor(new Color(0, 0, 0));
		g.drawLine(padding, padding, padding, hoogte - padding);
		g.drawLine(breedte - padding, hoogte - padding, padding, hoogte - padding);
	}

	/**
	 * Deze mothode word aangeroepen als er een geselecteerde data getekend moet
	 * worden in de grafiek
	 * 
	 * @param selected Hier word een monitor onderdeel meegegeven bijvoorbeeld: cpu
	 * @return Hij geeft hier een true/false statement aan de hand van de
	 *         gecontroleerde data
	 */
	private boolean isSelected(String selected) {
		// Er word een lijst meegegeven met welke data is geselecteerd, deze lijst word
		// daarna op elke waarde gecontrolleerd
		// of die weergegeven moet worden.
		// Als deze waarde in de lijst staat geeft hij een true terug, anders geeft hij
		// false terug
		List<String> selectedData = Main.getWindow().getMonitorTab().getInfoList().getSelectedData();
		for (int i = 0; i < selectedData.size(); i++) {
			if (selectedData.get(i).equals(selected)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Deze methode word aangeroepen als er een horizontale lijn getekend moet
	 * worden
	 * 
	 * @param x Dit is de x positie van lijnen die er getekend moet worden
	 * @param g Dit is een afkorting van graphics voor het gebruiksgemak
	 */
	private void drawHorizontalline(double x, Graphics g) {
		int breedte = g.getClipBounds().width;
		int hoogte = g.getClipBounds().height;
		int padding = 40;
		g.drawLine(breedte - padding, (int) (padding + (hoogte - padding * 2) * x), padding,
				(int) (padding + (hoogte - padding * 2) * x));
	}

	/**
	 * Deze methode word aangeroepen als er een verticale lijn getekend moet worden
	 * 
	 * @param x Dit is de y positie van lijnen die er getekend moet worden
	 * @param g Dit is een afkorting van graphics voor het gebruiksgemak
	 */
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

		// Als de grootte is aangepast, maak een nieuwe image.
		if (img.getWidth() != getWidth() || img.getHeight() != getHeight()) {
			img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		}

		Graphics g = img.getGraphics();
		g.setClip(0, 0, getWidth(), getHeight());

	}

}
