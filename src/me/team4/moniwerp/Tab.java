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

/**
 * Een tabblad dat kan worden gebruikt in een venster
 *
 */
public interface Tab {

	/**
	 * Deze methode wordt geroepen wanneer een menu knop wordt ingedrukt. buttonID
	 * geeft aan welke knop is ingedrukt.
	 * 
	 * @param buttonID Id van de menu knop die is ingedrukt. Komt overeen met
	 *                 Window.BUTTON_*
	 */
	public void onMenuButton(int buttonID);

	/**
	 * Deze methode wordt geroepen wanneer het venster van grootte aanpast, waardoor
	 * alle groottes opnieuw moet worden berekent.
	 * 
	 * @param width De nieuwe breedte
	 * @param height De nieuwe hoogte
	 */
	public void onResizeTab(int width, int height);

}
