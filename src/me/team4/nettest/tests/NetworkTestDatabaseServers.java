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
package me.team4.nettest.tests;

import me.team4.nettest.NetworkTest;
import me.team4.nettest.NetworkUtils;
import me.team4.nettest.TestResult;

public class NetworkTestDatabaseServers implements NetworkTest{
	
	@Override
	public String getName(){
		return "DBdirect";
	}
	
	@Override
	public void run(TestResult result) {
		
		// Variabelen IPv4.
		String DB1_4 = "192.168.20.1";
		String DB2_4 = "192.168.20.2";
		
		// Variabelen IPv6.
		String DB1_6 = "[FC00:0:0:20:3000:0:0:1]";
		String DB2_6 = "[FC00:0:0:20:3000:0:0:2]";
		
		// Database servers ping tests op IPv4 en IPv6 en voeg resultaten toe aan log file.
		result.addResult("ping DB1 IPv4", NetworkUtils.ping(DB1_4));
		result.addResult("ping DB2 IPv4", NetworkUtils.ping(DB2_4));
		result.addResult("ping DB1 IPv6", NetworkUtils.ping(DB1_6));
		result.addResult("ping DB2 IPv6", NetworkUtils.ping(DB2_6));
	}
}
