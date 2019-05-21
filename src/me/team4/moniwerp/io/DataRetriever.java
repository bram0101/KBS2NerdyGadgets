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
package me.team4.moniwerp.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

/**
 * Deze klas haalt de monitor data op uit de database.
 *
 */

public class DataRetriever {

	/**
	 * Cache. De key is de naam van het netwerkcomponent. De value is een lijst met
	 * monitordata. De eerste item in de lijst is de meest recente data.
	 */
	private HashMap<String, LinkedList<MonitorData>> cache;
	/**
	 * Status cache. De key is de naam van het netwerkcomponent. De value is een
	 * boolean dat aangeeft of het netwerkcomponent aan staat of niet.
	 */
	private HashMap<String, Boolean> statusCache;

	private HashMap<String, Float> uptimeCache = new HashMap<String, Float>();
	
	private Connection connection;

	private HashMap<Integer, String> naamConversie = new HashMap<Integer, String>();
	

	private long lastTimestamp;
	private int uptimeCacheCounter = 60;

	private DataRetriever() {
		// initialiseer de beide hashmaps
		cache = new HashMap<String, LinkedList<MonitorData>>();
		statusCache = new HashMap<String, Boolean>();
		// 604800 is 1 week aan seconden
		lastTimestamp = System.currentTimeMillis() / 1000L - 2 - 6000;
	}

	/**
	 * Haal de laatste data op.
	 */
	public void poll() {
		// maak een connectie met de SQL database
		try {
			if (connection == null) {
				String DB_URL = "jdbc:mysql://192.168.20.221:3306/monDB"
						+ "?useLegacyDatetimeCode=false&serverTimezone=UTC";
				// Als er geen verbinding kan worden gemaakt, stop er dan na 2 seconde.
				DriverManager.setLoginTimeout(2);
				connection = DriverManager.getConnection(DB_URL, "monitor", "sfcou%345");
				// Als het langer dan een seconde duurt, stop er dan mee
				connection.setNetworkTimeout(new Executor() {

					@Override
					public void execute(Runnable command) {
						command.run();
					}

				}, 5000);

				// Maakt een SQL statement mogelijk
				Statement stmt = connection.createStatement();
				// Een ResultSet geeft het resultaat van een Select statement
				ResultSet rs = stmt.executeQuery("select * from Netwerkcomponent;");
				// Geeft elke regel weer
				while (rs.next()) {
					naamConversie.put(rs.getInt("ComponentID"), rs.getString("naam"));
					if (!cache.containsKey(rs.getString("naam"))) {
						cache.put(rs.getString("naam"), new LinkedList<MonitorData>());
						// pakt elke ID + Naam samen en gooit ze in een LinkedList
					}
				}
			}
			Statement statement = connection.createStatement();
			long timestamp = System.currentTimeMillis() / 1000L - 1;
			// initialiseer de variablen uit de database
			int uptime = 0;
			float cpu = 0;
			float ramUsed = 0;
			float ramTotal = 0;
			float diskUsed = 0;
			float diskTotal = 0;
			int diskBusyTime = 0;
			int bytesSent = 0;
			int bytesReceived = 0;
			String name = "";
			LinkedList<MonitorData> dataList = null;

			// aanroepen welke tabel gebruik wordt

			ResultSet rs = statement.executeQuery(
					"select * from Netwerk where timestamp > " + (lastTimestamp - 2) + "  order by timestamp;");
			while (rs.next()) {
				// haal de gegevens uit de database op
				timestamp = rs.getLong("timestamp");
				uptime = rs.getInt("uptime");
				cpu = rs.getFloat("cpu");
				ramUsed = rs.getFloat("ram gebruikt");
				ramTotal = rs.getFloat("ram totaal");
				diskUsed = rs.getFloat("schijfruimte gebruikt");
				diskTotal = rs.getFloat("schijfruimte totaal");
				diskBusyTime = rs.getInt("schijf busytime");
				bytesSent = rs.getInt("bytes sent");
				bytesReceived = rs.getInt("bytes received");
				name = naamConversie.get(rs.getInt("ComponentID"));
				dataList = cache.get(name);
				// zet de ComponentID bij de opgehaalde MonitorData in de linkedlist van cache
				if (!dataList.isEmpty() && timestamp <= dataList.peekFirst().getTimestamp())
					continue; // Deze zou al in de lijst moeten zitten, dus voeg hem niet weer toe.
				dataList.addFirst(new MonitorData(timestamp, uptime, cpu, ramUsed, ramTotal, diskUsed, diskTotal,
						diskBusyTime, bytesSent, bytesReceived));
				// vervang de oude timestamp als er iets nieuws is
				if (lastTimestamp < timestamp) {
					lastTimestamp = timestamp;
				}
			}
			// kijkt of het apparaat aan staat aan de hand van de timestamp
			for (Entry<String, LinkedList<MonitorData>> e : cache.entrySet()) {
				if (!e.getValue().isEmpty()) {
					if (e.getValue().peekFirst().getTimestamp() >= lastTimestamp - 3) {
						statusCache.put(e.getKey(), true);
					} else {
						statusCache.put(e.getKey(), false);
					}
				} else {
					statusCache.put(e.getKey(), false);
				}
			}
			
			uptimeCacheCounter++;
			if(uptimeCacheCounter >= 60) {
				uptimeCacheCounter = 0;
				rs = statement.executeQuery("Select ComponentID, MIN(timestamp) as min, COUNT(ComponentID) as count From Netwerk group by ComponentID;");
				while (rs.next()) {
					int compID = rs.getInt("ComponentID");
					long minTime = rs.getLong("min");
					double count = (double) rs.getLong("count");
					double timeDiff = (double) ((lastTimestamp - 2) - minTime);
					double uptimeVal = Math.max(Math.min(count / timeDiff, 1D), 0D);
					uptimeCache.put(naamConversie.get(compID), (float) uptimeVal);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.close();
			} catch (Exception ex2) {
				ex.printStackTrace();
			}
			connection = null;
		}
	}

	/**
	 * Krijg de monitor data uit de cache voor het netwerkcomponent.
	 *
	 * @param name De netwerkcomponent waar de data van wordt opgehaald.
	 * @return Lijst met data. De eerste item in de lijst is de meest recente data.
	 */
	public LinkedList<MonitorData> getDataForComponent(String name) {
		return cache.get(name);
	}

	/**
	 * Krijg de meest recente monitor data voor het netwerkcomponent.
	 *
	 * @param name De netwerkcomponent waar de data van wordt opgehaald.
	 * @return Monitor data
	 */

	public MonitorData getLatestDataForComponent(String name) {
		if (!cache.containsKey(name)) {
			return null;
		}
		return cache.get(name).peekFirst();
	}

	/**
	 * Deze methode kijkt of het netwerkcomponent aan of uit staat.
	 *
	 * @param name De naam van het netwerkcomponent.
	 * @return True als het netwerkcomponent aan staat.
	 */
	public boolean getStatusForComponent(String name) {
		if (!statusCache.containsKey(name)) {
			return true;
		}
		return statusCache.get(name);
	}
	
	public float getUptimeForComponent(String name) {
		if(!uptimeCache.containsKey(name))
			return 0F;
		return uptimeCache.get(name);
	}

	/**
	 * Er hoeft maar ��n instantie te zijn voor DataRetriever, dus die slaan wij
	 * hier op.
	 */
	private static DataRetriever instance = new DataRetriever();

	/**
	 * Voor deze klas hebben wij maar ��n instantie.
	 *
	 * @return De instantie van DataRetriever
	 */
	public static DataRetriever getInstance() {
		return instance;
	}
}
