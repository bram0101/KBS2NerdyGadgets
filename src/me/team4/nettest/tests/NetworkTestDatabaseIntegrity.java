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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import me.team4.nettest.NetworkTest;
import me.team4.nettest.NetworkUtils;
import me.team4.nettest.TestResult;

public class NetworkTestDatabaseIntegrity implements NetworkTest {

	@Override
	public String getName() {
		return "DB integrity";
	}

	@Override
	public void run(TestResult result) {

		// Variabelen IPv4.
		String DB1_4 = "192.168.20.1:3306";
		String DB2_4 = "192.168.20.2:3306";

		boolean integrity = true;

		long lastTimestamp = System.currentTimeMillis() / 1000L;

		Entry<Connection, ResultSet> res1 = NetworkUtils
				.sqlQuery(
						"select * from Netwerk where timestamp > " + (lastTimestamp - 60 * 60 * 24)
								+ " AND timestamp < " + (lastTimestamp - 4) + " order by timestamp;",
						DB1_4, "monDB", "monitor", "sfcou%345");
		Entry<Connection, ResultSet> res2 = NetworkUtils
				.sqlQuery(
						"select * from Netwerk where timestamp > " + (lastTimestamp - 60 * 60 * 24)
								+ " AND timestamp < " + (lastTimestamp - 4) + " order by timestamp;",
						DB2_4, "monDB", "monitor", "sfcou%345");
		ResultSet set1 = res1.getValue();
		ResultSet set2 = res2.getValue();
		try {
			while (set1.next()) {
				if (!set2.next()) {
					// Set 1 heeft nog data maar set 2 niet.
					integrity = false;
					break;
				}
				if (set1.getLong("timestamp") != set2.getLong("timestamp")) {
					// timestamps niet gelijk
					integrity = false;
					break;
				}
				if (set1.getInt("ComponentID") != set2.getInt("ComponentID")) {
					// component ids niet gelijk
					integrity = false;
					break;
				}
				if (set1.getFloat("cpu") != set2.getFloat("cpu")) {
					// cpu niet gelijk
					integrity = false;
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				res1.getKey().close();
				res2.getKey().close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		result.addResult("Integrity", integrity);
	}
}
