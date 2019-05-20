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

package me.team4.moniwerp;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import me.team4.moniwerp.design.NetworkComponentTypes;
import me.team4.moniwerp.io.DataRetriever;

public class Main {

	private static Window window;
	private static JWindow splashscreen;

	public static void main(String[] args) {
		// Zorg dat het programma eruit ziet alsof het native is.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		showSplashscreen();
		// Wacht even met de rest van de code. Dan heeft AWT de tijd om de splashscreen
		// ook echt weer te geven.
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// Haal alvast een groot deel van de data uit de database server. Dit kan lang
				// duren, daar is de splashscreen voor.
				NetworkComponentTypes.load();
				DataRetriever.getInstance().poll();
				// Maak een venster aan en geef het weer.
				window = new Window();
				window.setVisible(true);
				hideSplashscreen();
			}

		});
	}

	private static void showSplashscreen() {
		// Maak een venster zonder titlebar en voeg de afbeelding toe.
		splashscreen = new JWindow();
		splashscreen.getContentPane().add(new JLabel("",
				new ImageIcon(Main.class.getClassLoader().getResource("splashscreen.png")), SwingConstants.CENTER));
		// zet de grootte
		splashscreen.setSize(400, 200);
		// plaatst het in het midden
		splashscreen.setLocationRelativeTo(null);
		// geef het venster weer.
		splashscreen.setVisible(true);
	}

	private static void hideSplashscreen() {
		splashscreen.setVisible(false);
	}

	/**
	 * @return Het hoofdvenster van deze applicatie.
	 */
	public static Window getWindow() {
		return window;
	}

}
