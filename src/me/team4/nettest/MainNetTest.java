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
package me.team4.nettest;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map.Entry;

import me.team4.nettest.tests.NetworkTestLoadBalancer;
import me.team4.nettest.tests.NetworkTestLocalHost;
import me.team4.nettest.tests.NetworkTestWeb;


public class MainNetTest {

	public static void main(String[] args) {
		NetworkTest[] tests = new NetworkTest[] {
				new NetworkTestLocalHost(),
				new NetworkTestWeb(),
				new NetworkTestLoadBalancer()
		};

		try {
			// initialiseren
			float ratio = 0F;
			float amt = 0F;

			PrintWriter pw = new PrintWriter(new File("./results.txt"));

			pw.println("====== Starting test at " + LocalDateTime.now() + " ======"); // zet de huidige tijd boven de testresultaten
			for (int i = 0; i < tests.length; i++) { // loopt door alle netwerkcomponenten  
				System.out.println(((int) (((float) i) / ((float) tests.length) * 100F)) + "%");
				pw.println("== TEST: " + tests[i].getName() + " ==");

				TestResult result = new TestResult();

				tests[i].run(result);

				ratio += result.passRatio();
				amt += 1.0F;

				for(Entry<String, Boolean> e : result.getResults().entrySet()) {
					pw.print("  " + e.getKey());
					for(int j = 0; j < 32 - e.getKey().length(); j++) // zorgt voor padding na de naam, zodat de percentages onder elkaar komen
						pw.print(" ");
					pw.print(": " + (e.getValue() ? "PASSED" : "FAILED"));
					pw.println();
				}
				pw.println();
			}

			pw.println();
			pw.println("PASSED: " + ((int) (ratio / amt * 100F)) + "%"); // de totale percentage
			pw.println();
			pw.println();
			pw.flush(); // zet de gegevens daadwerkelijk naar de results.txt
			pw.close(); // netjes afsluiten

			System.out.println("PASSED: " + ((int) (ratio / amt * 100F)) + "%"); // print de totale percentage

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
