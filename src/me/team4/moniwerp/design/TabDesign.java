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

import javax.swing.JPanel;

import me.team4.moniwerp.Tab;

/**
 * Het tabblad voor de ontwerp kant van de applicatie
 *
 */
public class TabDesign extends JPanel implements Tab {

	private static final long serialVersionUID = 1L;

	private Toolbar toolbar;
	private ViewportDesign viewport;
	
	public TabDesign() {
		
		BorderLayout mainlayout = new BorderLayout();
		setLayout(mainlayout);
		
		toolbar = new Toolbar();
		viewport = new ViewportDesign();
		
		add(toolbar, BorderLayout.WEST);
		add(viewport, BorderLayout.CENTER);
		
	}
	
	@Override
	public void onMenuButton(int buttonID) {
		// TODO Auto-generated method stub
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}

	@Override
	public void onResizeTab(int width, int height) {
		// Geef de componenten hun nieuwe grootte.
		setPreferredSize(new Dimension(width, height));
		toolbar.setPreferredSize(new Dimension(96, height));
		viewport.setPreferredSize(new Dimension(width - 96, height));
	}
}