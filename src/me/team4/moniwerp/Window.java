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
package me.team4.moniwerp;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import me.team4.moniwerp.design.TabDesign;
import me.team4.moniwerp.monitor.TabMonitor;

/**
 * Het venster voor de applicatie. Dit venster beheert de meerder tabbladen voor
 * het programma.
 *
 */
public class Window extends JFrame implements ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Button id voor de "Nieuw" menu knop
	 */
	public static final int BUTTON_NEW = 1;
	/**
	 * Button id voor de "Open" menu knop
	 */
	public static final int BUTTON_OPEN = 2;
	/**
	 * Button id voor de "Opslaan" menu knop
	 */
	public static final int BUTTON_SAVE = 3;
	/**
	 * Button id voor de "Optimize" menu knop
	 */
	public static final int BUTTON_OPTIMIZE = 4;
	/**
	 * De geselecteerde tabblad
	 */
	private Tab selectedTab;
	private TabMonitor monTab;
	private JPanel root;
	private JPanel tabPanel;

	public Window() {
		// Als je op het kruisje klikt, dan stopt het programma
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// We hebben een panel nodig waar wij alles aan kunnen toevoegen.
		root = new JPanel();
		root.setLayout(new BorderLayout());

		// Maak onze panels aan.
		monTab = new TabMonitor();
		tabPanel = new JPanel();

		// Voeg ze toe
		tabPanel.add(monTab);
		// Wij willen weten wanneer de grootte aanpast, zodat wij de layout kunnen
		// aanpassen.
		tabPanel.addComponentListener(this);

		root.add(tabPanel, BorderLayout.CENTER);

		setContentPane(root);

		// Set de grootte
		setSize(800, 600);
		// Zet het venster in het midden van het scherm.
		setLocationRelativeTo(null);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// Zeg tegen het tabblad dat de layout opnieuw moet worden gedaan.
		monTab.onResizeTab(tabPanel.getWidth(), tabPanel.getHeight());
		// Zeg tegen AWT/Swing dat de layout is aangepast en forceer een update.
		tabPanel.revalidate();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
	/**
	 * @return De TabMonitor instantie die wordt gebruikt in dit venster.
	 */
	public TabMonitor getMonitorTab() {
		return monTab;
	}
	
	/**
	 * @return De TabDesign instantie die wordt gebruikt in dit venster.
	 */
	public TabDesign getDesignTab() {
		return null;
	}

}
