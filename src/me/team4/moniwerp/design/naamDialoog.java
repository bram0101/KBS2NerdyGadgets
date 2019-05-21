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
package me.team4.moniwerp.design;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import me.team4.moniwerp.Main;

public class naamDialoog extends JDialog implements ActionListener {
	private JTextField naam;
	private JButton ok, cancel;
	private JLabel titel;
	private String inputNaam;
	private NetworkComponent selected;

	// Maak een dialoog voor het veranderen van de naam
	public naamDialoog(JFrame frame) {
		super(frame);
		
		setSize(300, 100);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel root = new JPanel();
		naam = new JTextField(5);
		JPanel buttonPanel = new JPanel();
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		titel = new JLabel("Voer een naam in voor het geselecteerde component");

		// maak de layout van de Dialoog
		root.add(titel);
		root.add(naam);
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		root.add(buttonPanel);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		add(root);

		pack();

		setVisible(true);

		// maak een listener die kijkt of er op een knop is gedrukt
		ok.addActionListener(this);
		cancel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Kijk of er op de OK knop is gedrukt
		if (e.getSource() == ok) {
			try {
				// De input van de textfield word omgezet in een variable
				inputNaam = naam.getText().trim();
				// Kijk of de input niet leeg is
				if (!inputNaam.isEmpty() || selected.getType().equals("INTERNET")) {
					// Verander de naam van het geselecteerde component
					selected.setNaam(inputNaam);
				}
				Main.getWindow().getDesignTab().getViewportDesign().repaint();
				dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// Kijk of er op de Cancel knop is gedrukt
		} else {
			dispose();
		}
	}

	public String getinputNaam() {
		return inputNaam;
	}

	public NetworkComponent getSelected() {
		return selected;
	}

	public void setSelected(NetworkComponent s) {
		this.selected = s;
		naam.setText(s.getNaam());
	}
}
