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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.team4.moniwerp.Tab;
import me.team4.moniwerp.Window;

/**
 * Het tabblad voor de ontwerp kant van de applicatie
 *
 */
public class TabDesign extends JPanel implements Tab {

	private static final long serialVersionUID = 1L;

	private Toolbar toolbar;
	private ViewportDesign viewport;

	public TabDesign() {
		// Voeg een layout toe
		BorderLayout mainlayout = new BorderLayout();
		setLayout(mainlayout);

		// Maak de componenten die nodig zijn voor TabDesign
		toolbar = new Toolbar();
		viewport = new ViewportDesign();

		// Voeg de componenten toe
		add(toolbar, BorderLayout.WEST);
		add(viewport, BorderLayout.CENTER);

	}

	@Override
	public void onMenuButton(int buttonID) {
		if (buttonID == Window.BUTTON_NEW) {
			// We willen een niew ontwerp. Dit kan simpelweg door de lijst met componeten en
			// verbindingen te legen
			viewport.getNetworkDesign().getComponents().clear();
			viewport.getNetworkDesign().getConnections().clear();
			NetworkComponent internetComponent = new NetworkComponent("INTERNET", "INTERNET", 0, 1D, 10, 10);
			viewport.getNetworkDesign().getComponents().add(internetComponent);
		}
		if (buttonID == Window.BUTTON_OPEN) {
			// Create a file chooser
			final JFileChooser Of = new JFileChooser();
			Of.setFileFilter(new FileNameExtensionFilter("Moniwerp Design", "mwd"));

			// In response to a button click:
			int returnVal = Of.showOpenDialog(Of);

			// If statement returnVal
			// Open FilesChooser en laad geselecteerd bestand
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					DataInputStream dis = new DataInputStream(new FileInputStream(Of.getSelectedFile()));
					viewport.getNetworkDesign().load(dis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// Sluit de FileChooser af zonder verdere acties.
			}
		}
		if (buttonID == Window.BUTTON_SAVE) {
			// Create a file chooser
			final JFileChooser Of = new JFileChooser();
			Of.setFileFilter(new FileNameExtensionFilter("Moniwerp Design", "mwd"));

			// In response to a button click:
			int returnVal = Of.showSaveDialog(Of);

			// If statement returnVal
			// Open FilesChooser en sla het geselecteerd bestand op
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					String fileName = Of.getSelectedFile().getAbsolutePath();
					if(!fileName.endsWith(".mwd"))
						fileName = fileName + ".mwd";
					else if(!fileName.endsWith(".MWD"))
						fileName = fileName + ".MWD";
					DataOutputStream dis = new DataOutputStream(new FileOutputStream(fileName));
					viewport.getNetworkDesign().save(dis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// Sluit de FileChooser af zonder verdere acties.
			}
		}
		if (buttonID == Window.BUTTON_OPTIMIZE) {
			// Zeg tegen de viewport dat wij het ontwerp willen laten optimalizeren
			viewport.optimize();
		}
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	public ViewportDesign getViewportDesign() {
		return viewport;
	}

	@Override
	public void onResizeTab(int width, int height) {
		// Geef de componenten hun nieuwe grootte.
		setPreferredSize(new Dimension(width, height));
		toolbar.setPreferredSize(new Dimension(144, height));
		viewport.setPreferredSize(new Dimension(width - 144, height));
		toolbar.onResize(144, height);
	}
}
