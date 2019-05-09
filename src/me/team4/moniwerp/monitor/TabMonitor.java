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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import me.team4.moniwerp.Main;
import me.team4.moniwerp.Tab;
import me.team4.moniwerp.Window;
import me.team4.moniwerp.io.DataRetriever;

/**
 * Het tabblad voor de monitor kant van de applicatie.
 * 
 * Deze klas zorgt er ook voor dat de UI elke seconde update.
 *
 */
public class TabMonitor extends JPanel implements Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InfoList infoList;
	private ViewportGraph viewportGraph;
	private ViewportNetwork viewportNetwork;

	public TabMonitor() {
		// Maak de componenten aan en voeg ze toe
		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);

		infoList = new InfoList();
		JPanel leftSide = new JPanel();
		BorderLayout leftLayout = new BorderLayout();
		leftSide.setLayout(leftLayout);
		viewportGraph = new ViewportGraph();
		viewportNetwork = new ViewportNetwork();

		add(leftSide, BorderLayout.WEST);
		add(infoList, BorderLayout.EAST);
		leftSide.add(viewportNetwork, BorderLayout.NORTH);
		leftSide.add(viewportGraph, BorderLayout.SOUTH);

		// Maakt een timer aan en update MonitorTab elke seconde.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (Main.getWindow().getSelectedTab() == Main.getWindow().getMonitorTab()) {
					DataRetriever.getInstance().poll();
					infoList.update();
					viewportNetwork.update();
					viewportGraph.update();
				}
			}
		}, 1000, 1000);
	}

	@Override
	public void onMenuButton(int buttonID) {
		// TODO Auto-generated method stub
		if(buttonID == Window.BUTTON_OPEN) {
			// Create a file chooser
			final JFileChooser Of = new JFileChooser();

			// In response to a button click:
			int returnVal = Of.showOpenDialog(Of);

			// If statement returnVal
			// Open FilesChooser en laad geselecteerd bestand
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					DataInputStream dis = new DataInputStream(new FileInputStream(Of.getSelectedFile()));
					viewportNetwork.getNetworkDesign().load(dis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			// Sluit de FileChooser af zonder verdere acties.
			} else if (returnVal == JFileChooser.CANCEL_OPTION) {
				
			// Sluit de FileChooser af zonder verdere acties.
			} else if (returnVal == JFileChooser.ERROR_OPTION) {
				
			}
		}
	}

	@Override
	public void onResizeTab(int width, int height) {
		// Geef de componenten hun nieuwe grootte.
		setPreferredSize(new Dimension(width, height));
		infoList.setPreferredSize(new Dimension(width / 3, height));
		viewportNetwork.setPreferredSize(new Dimension(width - width / 3, height - height / 3));
		viewportGraph.setPreferredSize(new Dimension(width - width / 3, height / 3));
	}

	public InfoList getInfoList() {
		return infoList;
	}

	public ViewportGraph getViewportGraph() {
		return viewportGraph;
	}

	public ViewportNetwork getViewportNetwork() {
		return viewportNetwork;
	}

}
