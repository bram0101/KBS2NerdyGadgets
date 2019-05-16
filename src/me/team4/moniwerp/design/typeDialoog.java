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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class typeDialoog extends JDialog implements ActionListener {
	private JButton ok, cancel;
	private JLabel titel;
	private NetworkComponentUnknown selected;
	private JCheckBox[] checkboxes;

	public typeDialoog(JFrame frame) {
		setSize(300, 150);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel root = new JPanel();
		titel = new JLabel("Voer het type in voor het onbekende component");
		JPanel buttonPanel = new JPanel();
		ok = new JButton("OK");
		cancel = new JButton("Cancel");

		root.add(titel);

		int i = 0;
		checkboxes = new JCheckBox[NetworkComponentTypes.getTypes().length];
		for (NetworkComponentType type : NetworkComponentTypes.getTypes()) {
			checkboxes[i] = new JCheckBox(type.getName());
			root.add(checkboxes[i]);
			i++;
		}

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
			selected.GetComponentTypes().clear();
			for (int i = 0; i < checkboxes.length; i++) {
				if (checkboxes[i].isSelected()) {
					selected.GetComponentTypes().add(i);
				}
			}
			dispose();
		} else {
			dispose();
		}
	}

	public NetworkComponent getSelected() {
		return selected;
	}

	public void setSelected(NetworkComponentUnknown s) {
		this.selected = s;
		for (Integer i : selected.GetComponentTypes()) {
			checkboxes[i].setSelected(true);
		}
	}
}
