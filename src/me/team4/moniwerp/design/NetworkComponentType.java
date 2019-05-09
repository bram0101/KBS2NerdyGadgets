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

public class NetworkComponentType {
	/**
	 * De naam van het NetworkComponentType
	 */
	private String name;
	
	/**
	 * De kosten van het NetworkComponentType
	 */
	private int costs;
	
	/**
	 * De uptime van het NetworkComponentType
	 */
	private float uptime;
	/**
	 * Een constructor die een NetworkComponentType aanmaakt met de gegeven parameters
	 * @param name
	 * @param costs
	 * @param uptime
	 */
	public NetworkComponentType(String name, int costs, float uptime) {
		this.name = name;
		this.costs = costs;
		this.uptime =uptime;
	}
	//TODO
	
	/**
	 * Een getter voor de naam
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Een getter voor de kosten
	 * @return costs
	 */
	public int getCosts() {
		return costs;
	}
	
	/**
	 * Een getter voor de uptime
	 * @return uptime
	 */
	public float getUptime() {
		return uptime;
	}
}
