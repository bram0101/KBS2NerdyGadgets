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

import java.util.concurrent.atomic.AtomicInteger;

import me.team4.nettest.NetworkTest;
import me.team4.nettest.NetworkUtils;
import me.team4.nettest.TestResult;

public class NetworkTestWebStress implements NetworkTest {

	private static final int testAmount = 20;
	private static final int threadAmount = 20;
	private static AtomicInteger counter = new AtomicInteger(0);
	
	@Override
	public String getName() {
		return "Web Stresstest";
	}

	private boolean test(String ip) {
		counter.set(0);
		Thread[] t = new Thread[threadAmount];
		for(int i = 0; i < threadAmount; i++) {
			t[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					for(int i = 0; i < testAmount; i++) {
						if(NetworkUtils.httpStress(ip, ""))
							counter.incrementAndGet();
						else
							break;
					}
				}
				
			});
			t[i].start();
		}
		
		for(int i = 0; i < threadAmount; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return counter.get() == testAmount * threadAmount;
	}
	
	@Override
	public void run(TestResult result) {
		String w1_4 = "192.168.10.1"; //ipv4 adres van webserver 1
		String w2_4 = "192.168.10.2"; //ipv4 adres van webserver 2
		String ha_4 = "192.168.40.221"; //ipv4 adres van HA proxy op pfSense

		// Http test op de webservers voor de beschikbaarheid van de website en voeg resultaten toe aan log file.
		
		result.addResult("W1", test(w1_4));
		result.addResult("W2", test(w2_4));
		result.addResult("HA_proxy", test(ha_4));
		
	}

}
