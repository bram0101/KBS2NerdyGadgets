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

public class NetworkTestpfSense implements NetworkTest{

	@Override
	public String getName(){
		return "pfSense";
	}
	
	@Override
	public void run(TestResult result) {
		String pfSense_4_1 = "192.168.10.254";
		String pfSense_4_2 = "192.168.20.254";
		String pfSense_4_3 = "192.168.30.254";
		String pfSense_6_1 = "[FC00:0:0:10:1000:0:0:1]";
		String pfSense_6_2 = "[FC00:0:0:20:1000:0:0:1]";
		String pfSense_6_3 = "[FC00:0:0:30:1000:0:0:1]";
				
		result.addResult("ping pfSense_4_1 IPv4", NetworkUtils.ping(pfSense_4_1));
		result.addResult("ping pfSense_4_2 IPv4", NetworkUtils.ping(pfSense_4_2));
		result.addResult("ping pfSense_4_3 IPv4", NetworkUtils.ping(pfSense_4_3));
		result.addResult("ping pfSense_6_1 IPv6", NetworkUtils.ping(pfSense_6_1));
		result.addResult("ping pfSense_6_2 IPv6", NetworkUtils.ping(pfSense_6_2));
		result.addResult("ping pfSense_6_3 IPv6", NetworkUtils.ping(pfSense_6_3));
		
		result.addResult("Index pfSense_4_1 IPv4", NetworkUtils.http(pfSense_4_1, ""));
		result.addResult("Index pfSense_4_2 IPv4", NetworkUtils.http(pfSense_4_2, ""));
		result.addResult("Index pfSense_4_3 IPv4", NetworkUtils.http(pfSense_4_3, ""));
		result.addResult("Index pfSense_6_1 IPv6", NetworkUtils.http(pfSense_6_1, ""));
		result.addResult("Index pfSense_6_2 IPv6", NetworkUtils.http(pfSense_6_2, ""));
		result.addResult("Index pfSense_6_3 IPv6", NetworkUtils.http(pfSense_6_3, ""));
	}
}
