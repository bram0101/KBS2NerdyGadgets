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

public class NetworkTestWeb implements NetworkTest {

	@Override
	public String getName() {
		return "Webdirect";
	}

	@Override
	public void run(TestResult result) {
		String w1_4 = "192.168.10.1"; //ipv4 adres van webserver 1
		String w1_6 = "[FC00:0:0:10:3000:0:0:1]"; //ipv6 adres van webserver 1
		String w2_4 = "192.168.10.2"; //ipv4 adres van webserver 2
		String w2_6 = "[FC00:0:0:10:3000:0:0:2]"; //ipv6 adres van webserver 2

		result.addResult("ping W1 IPv4", NetworkUtils.ping(w1_4)); //ping requests
		result.addResult("ping W1 IPv6", NetworkUtils.ping(w1_6));
		result.addResult("ping W2 IPv4", NetworkUtils.ping(w2_4));
		result.addResult("ping W1 IPv6", NetworkUtils.ping(w1_6));
		result.addResult("ping W2 IPv6", NetworkUtils.ping(w2_6));
		result.addResult("Webservers1", null);

		// Http test op de webservers voor de beschikbaarheid van de website en voeg resultaten toe aan log file.

		result.addResult("HTTP W1 IPv4", NetworkUtils.http(w1_4, ""));
		result.addResult("HTTP W2 IPv4", NetworkUtils.http(w2_4, ""));
		result.addResult("HTTP W1 IPv6", NetworkUtils.http(w1_6, ""));
		result.addResult("HTTP W2 IPv6", NetworkUtils.http(w2_6, ""));
	}

}
