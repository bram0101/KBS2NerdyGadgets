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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * laat de toolbar zien boven het designgedeelte
 */
public class Toolbar extends JPanel implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Het netwerkcomponenttype dat geselecteerd is
	 */
	private NetworkComponentType selected;

	/**
	 * Of de connectiontool gebruikt wordt
	 */
	private boolean connectionTool;

	private JPanel componentPanel;
	private JToggleButton[] buttons;
	private JToggleButton connectionButton;

	public Toolbar() {
		// Stel de layout in
		setLayout(new BorderLayout());

		// In deze panel zitten alle componenttypes
		componentPanel = new JPanel();
		componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));

		// Een lijstje voor de knoppen, dan kunnen wij er later bij.
		buttons = new JToggleButton[NetworkComponentTypes.getTypes().length + 1];

		// Ga langs elk type en voeg daar een knop vor toe.
		for (int i = 0; i < buttons.length - 1; i++) {
			NetworkComponentType type = NetworkComponentTypes.getTypes()[i];
			JToggleButton button = new JToggleButton(type.getName());
			button.addActionListener(this);
			buttons[i] = button;
			componentPanel.add(button);
			// Selecteer de eerste knop alvast.
			if (i == 0) {
				button.setSelected(true);
				selected = type;
			}
		}

		// Voeg een knop toe voor onbekendes, voor de optimalisatie functie.
		JToggleButton buttonUnknown = new JToggleButton("Onbekende");
		buttonUnknown.addActionListener(this);
		buttons[buttons.length - 1] = buttonUnknown;
		componentPanel.add(buttonUnknown);

		// Voeg de componenten panel toe.
		add(componentPanel, BorderLayout.CENTER);

		// Voeg een knop toe om verbindingen te maken.
		connectionButton = new JToggleButton("\u2192");
		connectionButton.setFont(new Font("Arial", Font.PLAIN, 36));
		connectionButton.addActionListener(this);
		add(connectionButton, BorderLayout.SOUTH);
	}

	/**
	 * Het netwwerkcomponenttype dat geselecteerd is
	 *
	 * @return selected
	 */
	public NetworkComponentType getSelected() {
		return selected;
	}

	/**
	 * Of de connectiontool gebruikt wordt
	 *
	 * @return ConnectionTool
	 */
	public boolean useConnectiontool() {
		return connectionTool;
	}

	public void onResize(int width, int height) {
		// Zet de groottes
		int h = Math.min(height / (buttons.length + 1), width);
		for (JToggleButton button : buttons) {
			button.setMaximumSize(new Dimension(width, h));
			button.setMinimumSize(new Dimension(width, h));
			button.setPreferredSize(new Dimension(width, h));
		}
		connectionButton.setMaximumSize(new Dimension(width, h));
		connectionButton.setMinimumSize(new Dimension(width, h));
		connectionButton.setPreferredSize(new Dimension(width, h));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Check voor elke knop
		for (JToggleButton button : buttons) {
			// Heb je hier niet op geklikt, zet hem dan uit, zodat er maar ��n knop tegelijk
			// aan kan.
			if (e.getSource() != button) {
				button.setSelected(false);
			} else {
				button.setSelected(true); // Voorkom deselectie
				// Als de knop een onbekende is, moeten wij een speciale type toevoegen.
				if (button.getText().equals("Onbekende")) {
					selected = new NetworkComponentType("Unknown", 0, 1);
				} else {
					selected = NetworkComponentTypes.getTypes(button.getText());
				}
			}
		}
		// Check hetzelfde ook nog even voor de connectionbutton
		if (e.getSource() == connectionButton) {
			connectionButton.setSelected(true);
			connectionTool = true;
		} else {
			connectionButton.setSelected(false);
			connectionTool = false;
		}
	}
}
