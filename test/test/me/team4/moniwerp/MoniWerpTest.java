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
package test.me.team4.moniwerp;

import me.team4.moniwerp.design.Calculator;
import me.team4.moniwerp.design.NetworkComponent;
import me.team4.moniwerp.design.NetworkConnection;
import me.team4.moniwerp.design.NetworkDesign;

public class MoniWerpTest {
	
	public static void main(String[] args) {
		NetworkDesign design = new NetworkDesign();
		// In dit geval hardcoded een ontwerp
		NetworkComponent pfSense = new NetworkComponent("pfSense", "firewall", 1000, 0.99999F, 0, 50);
		NetworkComponent w1 = new NetworkComponent("W1", "Webserver", 1000, 0.8F, 40, 55);
		NetworkComponent w2 = new NetworkComponent("W2", "Webserver", 1000, 0.9F, 40, 65);
		NetworkComponent lb = new NetworkComponent("LB1", "Loadbalancer", 1000, 0.99999F, 40, 40);
		NetworkComponent db1 = new NetworkComponent("DB1", "Database server", 1000, 0.9F, 80, 35);
		NetworkComponent db2 = new NetworkComponent("DB2", "Database server", 1000, 0.9F, 80, 45);
		design.getComponents().add(pfSense);
		design.getComponents().add(w1);
		design.getComponents().add(w2);
		design.getComponents().add(lb);
		design.getComponents().add(db1);
		design.getComponents().add(db2);
		design.getConnections().add(new NetworkConnection(pfSense, w1));
		design.getConnections().add(new NetworkConnection(pfSense, w2));
		design.getConnections().add(new NetworkConnection(pfSense, lb));
		design.getConnections().add(new NetworkConnection(lb, db1));
		design.getConnections().add(new NetworkConnection(lb, db2));
		design.calcBounds();
		
		Calculator calc = new Calculator();
		
		System.out.println(calc.calcUptime(design));
	}
	
}
