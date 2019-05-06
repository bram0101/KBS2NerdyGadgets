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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	private JButton butMonitor;
	private JButton butOntwerpen;

	public Window() {
		// Als je op het kruisje klikt, dan stopt het programma
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// We hebben een panel nodig waar wij alles aan kunnen toevoegen.
		root = new JPanel();
		root.setLayout(new BorderLayout());

		// Maak onze panels aan.
		monTab = new TabMonitor();
		selectedTab = monTab;
		tabPanel = new JPanel();

		// Voeg ze toe
		tabPanel.add(monTab);
		// Wij willen weten wanneer de grootte aanpast, zodat wij de layout kunnen
		// aanpassen.
		root.addComponentListener(this);

		root.add(tabPanel, BorderLayout.SOUTH);

		setContentPane(root);

		// Set de grootte
		setSize(800, 600);
		// Zet het venster in het midden van het scherm.
		setLocationRelativeTo(null);

		// Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		// Build the first menu.
		JMenu menu1 = new JMenu("Bestand");
		menu1.setMnemonic(KeyEvent.VK_A);
		menu1.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu1);

		// Create menu1 items
		JMenuItem m1 = new JMenuItem("New");
		JMenuItem m2 = new JMenuItem("Open");
		JMenuItem m3 = new JMenuItem("Save");

		// Add menu items to menu1
		menu1.add(m1);
		menu1.add(m2);
		menu1.add(m3);

		// Build the second menu.
		JMenu menu2 = new JMenu("Optimaliseer");
		menu2.setMnemonic(KeyEvent.VK_A);
		menu2.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu2);

		// Create menu2 items
		JMenuItem m4 = new JMenuItem("Optimaliserem");

		// Add menu items to menu2
		menu2.add(m4);

		this.setJMenuBar(menuBar);

		// Functions menu items
		m1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_NEW);
				System.out.println("Dit is stringie New");
			}

		});

		m2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_OPEN);
				System.out.println("Dit is stringie Open");

				// Create a file chooser
				final JFileChooser Of = new JFileChooser();

				// In response to a button click:
				int returnVal = Of.showOpenDialog(Of);

				// If statement returnVal
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//
				} else if (returnVal == JFileChooser.CANCEL_OPTION) {

				} else if (returnVal == JFileChooser.ERROR_OPTION) {
					// melding
				}
			}

		});

		m3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_SAVE);
				System.out.println("Dit is stringie Save");
			}

		});

		m4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_OPTIMIZE);
				System.out.println("Dit is stringie Optimaliseren");
			}

		});
		butMonitor = new JButton("Monitor") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
			}
		};
		butOntwerpen = new JButton("Ontwerpen");
		root.add(butMonitor, BorderLayout.WEST);
		root.add(butOntwerpen, BorderLayout.EAST);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		butMonitor.setPreferredSize(new Dimension(root.getWidth()/2,96));
		butOntwerpen.setPreferredSize(new Dimension(root.getWidth()/2,96));
		// Zeg tegen het tabblad dat de layout opnieuw moet worden gedaan.
		monTab.onResizeTab(root.getWidth(), root.getHeight()-96);
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
