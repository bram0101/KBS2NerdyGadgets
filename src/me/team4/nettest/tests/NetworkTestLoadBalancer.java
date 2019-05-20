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
package me.team4.nettest.tests;

import me.team4.nettest.NetworkTest;
import me.team4.nettest.NetworkUtils;
import me.team4.nettest.TestResult;

public class NetworkTestLoadBalancer implements NetworkTest {

	@Override
	public String getName() {
		return "DB Loadbalancer";
	}

	@Override
	public void run(TestResult result) {

		// Variabelen IPv4.
		String LB1_4 = "192.168.20.221";
		String DB1_4 = "192.168.20.1:3306";
		String DB2_4 = "192.168.20.2:3306";

		// Variabelen IPv6.
		String LB1_6 = "[FC00:0:0:20:2000:0:0:1]:3306";
		String DB1_6 = "[FC00:0:0:20:3000:0:0:1]:3306";
		String DB2_6 = "[FC00:0:0:20:3000:0:0:2]:3306";

		// LoadBalancer ping test op IPv4 en IPv6 en voeg resultaten toe aan log file.
		result.addResult("ping LB1 IPv4", NetworkUtils.ping(LB1_4));
		result.addResult("ping LB1 IPv6", NetworkUtils.ping("[FC00:0:0:20:2000:0:0:1]"));
		result.addResult("LB1", null);

		// LoadBalancer en Database servers SQL query test en voeg resultaten toen aan log file.
		result.addResult("SQL query IPv4 LB1", NetworkUtils.sql(LB1_4+":3306", "monDB", "monitor", "sfcou%345"));
		result.addResult("SQL query IPv4 DB1", NetworkUtils.sql(DB1_4, "monDB", "monitor", "sfcou%345"));
		result.addResult("SQL query IPv4 DB2", NetworkUtils.sql(DB2_4, "monDB", "monitor", "sfcou%345"));
		result.addResult("SQL query IPv6 LB1", NetworkUtils.sql(LB1_6, "monDB", "monitor", "sfcou%345"));
		result.addResult("SQL query IPv6 DB1", NetworkUtils.sql(DB1_6, "monDB", "monitor", "sfcou%345"));
		result.addResult("SQL query IPv6 DB2", NetworkUtils.sql(DB2_6, "monDB", "monitor", "sfcou%345"));
	}

}
