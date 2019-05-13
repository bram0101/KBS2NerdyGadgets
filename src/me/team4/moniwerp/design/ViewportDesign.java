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

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.JPanel;
/*
 * De viewport van het design 
 */
public class ViewportDesign extends JPanel{

	private Selectable selected;
	private LinkedList<NetworkDesign> UndoQueue = new LinkedList<NetworkDesign>();
	private LinkedList<NetworkDesign> redoQueue = new LinkedList<NetworkDesign>();
	
	public ViewportDesign() {
		setBackground(Color.blue);
	}
	/**
	 * redo knop: Je undo undo-en :)
	 */
	public void redo() {
		//TODO
	}
	/**
	 * undo knop: een stap terug
	 */
	public void undo() {
		//TODO
	}
	/**
	 *  geeft netwerkontwerpen weer.
	 */
	public NetworkDesign getNetworkDesign() {
		return new NetworkDesign();
	}
	
	/**
	 * run de optimizer, de optimizer optimaliseerd het huidige ontwerp door middel van CulledHierarchys
	 */
	public void optimize() {
		//TODO
	}
}
