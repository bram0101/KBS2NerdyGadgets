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

import java.util.Arrays;

import me.team4.moniwerp.design.Calculator;
import me.team4.moniwerp.design.CulledHierarchy;
import me.team4.moniwerp.design.NetworkComponent;
import me.team4.moniwerp.design.NetworkComponentTypes;
import me.team4.moniwerp.design.NetworkComponentUnknown;
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
		NetworkComponent ws1 = new NetworkComponentUnknown("DB1", "Database server", 1000, 0.9F, 80, 35, Arrays.asList(1));
		NetworkComponent db1 = new NetworkComponentUnknown("DB1", "Database server", 1000, 0.9F, 80, 35, Arrays.asList(0));
		//design.getComponents().add(pfSense);
		//design.getComponents().add(w1);
		//design.getComponents().add(w2);
		//design.getComponents().add(lb);
		design.getComponents().add(db1);
		design.getComponents().add(ws1);
		//design.getConnections().add(new NetworkConnection(pfSense, w1));
		//design.getConnections().add(new NetworkConnection(pfSense, w2));
		//design.getConnections().add(new NetworkConnection(pfSense, lb));
		//design.getConnections().add(new NetworkConnection(lb, db1));
		design.getConnections().add(new NetworkConnection(ws1, db1));
		design.calcBounds();
		
		Calculator calc = new Calculator();
		calc.buildNodeNetwork(design);
		CulledHierarchy ch = new CulledHierarchy();
		for(int[] l : calc.getProblemDefinition()) {
			for(int i : l) {
				System.out.println(NetworkComponentTypes.getTypes()[i].getName());
			}
			System.out.println();
		}
		byte[] solve = ch.execute(calc.getProblemDefinition(), calc, 0.999F);
		
		System.out.println("Kosten: " + calc.calcCosts(solve));
		System.out.println("Uptime: " + calc.calcUptime(solve));
		for (int i = 0; i < calc.getProblemDefinition().length; i++) {
			for (int j = 0; j < calc.getProblemDefinition()[i].length; j++) {
				System.out.println(NetworkComponentTypes.getTypes()[calc.getProblemDefinition()[i][j]].getName() + ": " + solve[calc.getOffsetGrootte()[i] + j]);
			}	
		}
	}
}
