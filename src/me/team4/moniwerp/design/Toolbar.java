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

import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * laat de toolbar zien boven het designgedeelte
 */
public class Toolbar extends JPanel{
	
	
	/**
	 * Het netwerkcomponenttype dat geselecteerd is
	 */
	private NetworkComponentType selected;
	
	/**
	 * Of de connectiontool gebruikt wordt
	 */
	private boolean ConnectionTool;
	
	public Toolbar() {
		selected = new NetworkComponentType("Webserver", 100, 0.9f);
		JToggleButton button = new JToggleButton();
		add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectionTool = !ConnectionTool;
			}
			
		});
	}
	
	/**
	 * Het netwwerkcomponenttype dat geselecteerd is
	 * @return selected
	 */
	public NetworkComponentType getSelected() {
		return selected;
	}
	/**
	 * Of de connectiontool gebruikt wordt
	 * @return ConnectionTool
	 */
	public boolean useConnectiontool() {
		return ConnectionTool;
	}
}
