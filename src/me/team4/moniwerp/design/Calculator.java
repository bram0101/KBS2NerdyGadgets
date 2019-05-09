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

/** Een calculator om de uptime en kosten te berekenen
 *
 */
public class Calculator {

	/**
	 * Berekent de uptime
	 * @param ontwerp uptime van het huidige ontwerp
	 * @return float 
	 */
	public float calcUptime(NetworkDesign ontwerp) {
		//TODO: implement
		return 0;
	}
	/**
	 * 
	 * @param problem Het huidige probleem
	 * @param solve De huidige (mogelijke) oplossing
	 * @return
	 */
	public float calcUptime(int problem[][], byte solve[]) {
		//TODO:implement
		return 0;
	}
	/**
	 * 
	 * @param design huidige kosten van het ontwerp
	 * @return 
	 */
	public int calcCosts(NetworkDesign design) {
		//TODO: implement
		
		return 10000;
	}
	/**
	 * 
	 * @param problem Het huidige probleem
	 * @param solve De huidige (mogelijke) oplossing
	 * @return
	 */
	public int calcCosts(int problem[][], byte solve[]) {
		//TODO: implement
		return 24000;
	}
}
