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

/**
 * Een netwerkcomponent met een naam, kosten, theoretische beschikbaarheid en positie.
 *
 */
public class NetworkComponent implements Selectable {
	
	/**
	 * De naam van het netwerkcomponent.
	 */
	private String naam;
	/**
	 * De naam van het type van het netwerkcomponent.
	 */
	private String type;
	/**
	 * Hoeveel het netwerkcomponent kost
	 */
	private int costs;
	/**
	 * De theoretische beschikbaarheid
	 */
	private double uptime;
	/**
	 * De x coördinaat van het component op het canvas.
	 */
	private int xLoc;
	/**
	 * De y coördinaat van het component op het canvas.
	 */
	private int yLoc;
	
	
	/**
	 * Maak een nieuw netwerkcomponent aan
	 * @param naam De naam van het netwerkcomponent
	 * @param type De naam van het type van het netwerkcomponent
	 * @param costs Hoeveel het netwerkcomponent kost
	 * @param uptime De theoretische beschikbaarheid
	 * @param xLoc De x coördinaat van het component op het canvas
	 * @param yLoc De y coördinaat van het component op het canvas
	 */
	public NetworkComponent(String naam, String type, int costs, double uptime, int xLoc, int yLoc) {
		this.naam = naam;
		this.type = type;
		this.costs = costs;
		this.uptime = uptime;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}

	/**
	 * @param naam the naam to set
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(int costs) {
		this.costs = costs;
	}

	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(double uptime) {
		this.uptime = uptime;
	}

	/**
	 * @param xLoc the xLoc to set
	 */
	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	/**
	 * @param yLoc the yLoc to set
	 */
	public void setyLoc(int yLoc) {
		this.yLoc = yLoc;
	}

	/**
	 * De naam van het netwerkcomponent.
	 */
	public String getNaam() {
		return naam;
	}
	
	/**
	 * De naam van het type van het netwerkcomponent.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Hoeveel het netwerkcomponent kost
	 */
	public int getCosts() {
		return costs;
	}
	
	/**
	 * De theoretische beschikbaarheid
	 */
	public double getUptime() {
		return uptime;
	}
	
	/**
	 * De x coördinaat van het component op het canvas.
	 */
	public int getxLoc() {
		return xLoc;
	}
	
	/**
	 * De y coördinaat van het component op het canvas.
	 */
	public int getyLoc() {
		return yLoc;
	}
	
}
