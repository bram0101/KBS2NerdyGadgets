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
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

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

		setTitle("Moniwerp - Monitoren");

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
		menuBar.add(menu2);

		// Create menu2 items
		JMenuItem m4 = new JMenuItem("Optimaliseren");

		// Add menu items to menu2
		menu2.add(m4);

		// Build the second menu.
		JMenu menu3 = new JMenu("Netwerk testen");
		menuBar.add(menu3);

		// Create menu3 items
		JMenuItem m5 = new JMenuItem("Netwerk testen");

		// Add menu items to menu4
		menu3.add(m5);

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

		m5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Maak een venster aan om het resultaat in weer te geven.
				JFrame resultFrame = new JFrame("Nettest resultaten");
				resultFrame.setSize(500, 800);
				// In een textpane voegen wij het resultaat toe.
				JTextPane textPane = new JTextPane();
				// Je mag de text niet aanpassen.
				textPane.setEditable(false);
				// Een monospace font want dat is mooier.
				textPane.setFont(new Font("Consolas", Font.PLAIN, 14));
				// Dark theme
				textPane.setBackground(new Color(40, 40, 40));
				// Je moet kunnen scrollen als dat nodig is.
				JScrollPane sp = new JScrollPane(textPane);
				resultFrame.add(sp);
				// Zet er alvast in dat het aan het laden is.
				textPane.setText("Loading...");
				// Plaats het venster in het midden van het scherm.
				resultFrame.setLocationRelativeTo(null);
				// Geef het venster weer.
				resultFrame.setVisible(true);

				// Maak wat stijlen aan voor color codes.
				Style greenStyle = textPane.addStyle("green", null);
				StyleConstants.setForeground(greenStyle, new Color(40, 230, 40));
				Style redStyle = textPane.addStyle("red", null);
				StyleConstants.setForeground(redStyle, new Color(230, 40, 40));
				Style normalStyle = textPane.addStyle("red", null);
				StyleConstants.setForeground(normalStyle, new Color(230, 230, 230));

				// Als het venster van grootte verandert, willen wij ook de textgrootte
				// aanpassen.
				textPane.addComponentListener(new ComponentListener() {

					@Override
					public void componentResized(ComponentEvent e) {
						textPane.setFont(new Font("Consolas", Font.PLAIN, textPane.getWidth() / 36));
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

				});

				// Zeg tegen Swing dat de code later moet worden uitgevoerd, dan heeft swing de
				// tijd om het venster alvast weer te geven.
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						BufferedReader br = null;
						try {
							// Voer de test uit.
							me.team4.nettest.MainNetTest.main(new String[] {});
							// Lees het resultatenbestand
							br = new BufferedReader(new FileReader(new File("./results.txt")));
							StringBuilder sb = new StringBuilder();
							String s = "";
							// Maak de textpane leeg.
							textPane.getStyledDocument().remove(0, textPane.getStyledDocument().getLength());
							// Lees regel voor regel de text.
							// Afhankelijk van wat in de regel staat geven wij het een bepaalde stijl.
							while ((s = br.readLine()) != null) {
								if (s.contains(": PASSED")) {
									textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
											s, greenStyle);
								} else if (s.contains(": FAILED")) {
									textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
											s, redStyle);
								} else {
									textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
											s, normalStyle);
								}
								textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
										"\n", normalStyle);
							}
							br.close();
						} catch (Exception ex) {
							ex.printStackTrace();
							textPane.setText("Er is iets fout gegaan tijdens de test. Probeer het later opnieuw.");
						} finally {
							// Sluit de inputstream, want anders dan blijft het bestand geopent en kunnen
							// andere programma's er niks mee doen. De testscripts kunnen dan zelfs niet
							// meer zijn resultaten in het bestand schrijven.
							try {
								if (br != null)
									br.close();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}

				});
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
				if (selectedTab == monTab) {
					g.setColor(new Color(147, 188, 255));
				} else {
					g.setColor(new Color(114, 134, 165));
				}
				//
				g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
				g.setColor(Color.black);
				// Een berekening om de tekst te centreren in de knop.
				int h = butMonitor.getHeight() / 2;
				int b = butMonitor.getWidth() / 2;
				g.drawString("Monitor", (int) (b - textBounds.getCenterX()), (int) (h - textBounds.getCenterY()));
			}

		};

		// Voegt functies toe aan de knop Monitor.
		butMonitor.addActionListener(new ActionListener() {

			@Override
			// Maakt selectedTab gelijk aan monTab, verwijderd alles wat is getekend en
			// voegt monTab toe.
			public void actionPerformed(ActionEvent e) {
				selectedTab = monTab;
				tabPanel.removeAll();
				tabPanel.add(monTab);
				tabPanel.revalidate();
				setTitle("Moniwerp - Monitoren");
				root.repaint();
			}
		});

		// Maakt de knop ontwerpen aan.
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
				if (selectedTab == designTab) {
					g.setColor(new Color(147, 188, 255));
				} else {
					g.setColor(new Color(114, 134, 165));
				}
				//
				g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
				g.setColor(Color.black);
				// Een berekening om de tekst te centreren in de knop.
				int h = butOntwerpen.getHeight() / 2;
				int b = butOntwerpen.getWidth() / 2;
				g.drawString("Ontwerpen", (int) (b - textBounds.getCenterX()), (int) (h - textBounds.getCenterY()));

			}
		};

		// Voegt functies toe aan de knop Monitor.
		butOntwerpen.addActionListener(new ActionListener() {

			@Override
			// Maakt selectedTab gelijk aan designTab, verwijderd alles wat is getekend en
			// voegt designTab toe.
			public void actionPerformed(ActionEvent e) {
				selectedTab = designTab;
				tabPanel.removeAll();
				tabPanel.add(designTab);
				tabPanel.revalidate();
				setTitle("Moniwerp - Ontwerpen");
				root.repaint();
			}

		});

		// Zet de knoppen op de goede plaats (links en rechts).
		root.add(butMonitor, BorderLayout.WEST);
		root.add(butOntwerpen, BorderLayout.EAST);
	}

	@Override
	public void componentResized(ComponentEvent e) {

		butMonitor.setPreferredSize(new Dimension(root.getWidth() / 2, 60));
		butOntwerpen.setPreferredSize(new Dimension(root.getWidth() / 2, 60));

		// Zeg tegen het tabblad dat de layout opnieuw moet worden gedaan.
		monTab.onResizeTab(root.getWidth(), root.getHeight() - 60);
		designTab.onResizeTab(root.getWidth(), root.getHeight() - 60);
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
