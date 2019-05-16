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

	public naamDialoog(JFrame frame) {
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

		root.add(titel);
		root.add(naam);
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		root.add(buttonPanel);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		add(root);

		pack();

		setVisible(true);

		ok.addActionListener(this);
		cancel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			try {

				inputNaam = naam.getText().trim();
				if (!inputNaam.isEmpty()) {
					selected.setNaam(inputNaam);
				}
				Main.getWindow().getDesignTab().getViewportDesign().repaint();
				dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
	}
}
