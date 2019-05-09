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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
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
	private TabDesign designTab;
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
		designTab = new TabDesign();
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
				
			}

		});

		m2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_OPEN);

				
			}

		});

		m3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_SAVE);
				
			}

		});

		m4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTab.onMenuButton(BUTTON_OPTIMIZE);
				
			}

		});
		
		// Maakt de knop Monitor aan.
		butMonitor = new JButton("Monitor") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			// Hier word een eigen knop getekend.
			protected void paintComponent(Graphics g) {
				// Tekst opmaak van de knop.
				g.setFont(new Font("Arial", Font.BOLD, 20));
				//
				Rectangle2D textBounds = g.getFontMetrics().getStringBounds("Monitor", g);
				// Als de knop is geselecteerd word er een lichtere kleur gegeven.
				if (selectedTab == monTab){
					g.setColor(new Color(147, 188, 255));
				}else {
					g.setColor(new Color(114,134,165));
				}
				//
				g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
                g.setColor(Color.black);
                // Een berekening om de tekst te centreren in de knop.
                int h = butMonitor.getHeight()/2; 
                int b = butMonitor.getWidth()/2;
				g.drawString("Monitor", (int) (b - textBounds.getCenterX()), (int) (h - textBounds.getCenterY()));
			}
			
			
		};
		
		// Voegt functies toe aan de knop Monitor.
		butMonitor.addActionListener(new ActionListener() {

			@Override
			// Maakt selectedTab gelijk aan monTab, verwijderd alles wat is getekend en voegt monTab toe.
			public void actionPerformed(ActionEvent e) {
				selectedTab = monTab;
				tabPanel.removeAll();
				tabPanel.add(monTab);
				tabPanel.revalidate();
			}
		});
		
		// Maakt de knop Monitor aan.
		butOntwerpen = new JButton("Ontwerpen") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			// Hier word een eigen knop getekend.
			protected void paintComponent(Graphics g) {
				// Tekst opmaak van de knop.
				g.setFont(new Font("Arial", Font.BOLD, 20));
				//
				Rectangle2D textBounds = g.getFontMetrics().getStringBounds("Ontwerpen", g);
				// Als de knop is geselecteerd word er een lichtere kleur gegeven.
				if (selectedTab == designTab){
					g.setColor(new Color(147, 188, 255));
				}else {
					g.setColor(new Color(114,134,165));
				}
				//
				g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
				g.setColor(Color.black);
				// Een berekening om de tekst te centreren in de knop.
                int h = butOntwerpen.getHeight()/2; 
                int b = butOntwerpen.getWidth()/2;
				g.drawString("Ontwerpen", (int) (b - textBounds.getCenterX()), (int) (h - textBounds.getCenterY()));
				
		    }
		}; 
		
		// Voegt functies toe aan de knop Monitor.
		butOntwerpen.addActionListener(new ActionListener() {

			@Override
			// Maakt selectedTab gelijk aan designTab, verwijderd alles wat is getekend en voegt designTab toe.
			public void actionPerformed(ActionEvent e) {
				selectedTab = designTab;
				tabPanel.removeAll();
				tabPanel.add(designTab);
				tabPanel.revalidate();
			}

		});
		
		// Zet de knoppen op de goede plaats (links en rechts).
		root.add(butMonitor, BorderLayout.WEST);
		root.add(butOntwerpen, BorderLayout.EAST);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		
		butMonitor.setPreferredSize(new Dimension(root.getWidth()/2,60));
		butOntwerpen.setPreferredSize(new Dimension(root.getWidth()/2,60));
		
		// Zeg tegen het tabblad dat de layout opnieuw moet worden gedaan.
		monTab.onResizeTab(root.getWidth(), root.getHeight()-60);
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
		return designTab;
	}
	
	/**
	 * 
	 * @return Geeft geselecteerde tab terug.
	 */
	public Tab getSelectedTab() {
		return selectedTab;
	}

}
