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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

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
	
	private DataRetriever() {
	}
	
	/**
	 * Initializeer de klas
	 */
	public void initialize() {
		// TODO: implement
	}
	
	/**
	 * Haal de laatste data op.
	 */
	public void poll() {
		// TODO: implement
	}

	/**
	 * Krijg de monitor data uit de cache voor het netwerkcomponent.
	 * 
	 * @param name De netwerkcomponent waar de data van wordt opgehaald.
	 * @return Lijst met data. De eerste item in de lijst is de meest recente data.
	 */
	public LinkedList<MonitorData> getDataForComponent(String name) {
		// TODO: implement
		long timestamp = System.currentTimeMillis() / 1000L;
		return new LinkedList<MonitorData>(Arrays
				.asList(new MonitorData[] { new MonitorData(timestamp, 30, 0.36F, 0.4F, 1.0F, 2F, 4F, 100, 1024, 512),
						new MonitorData(timestamp - 1, 29, 0.26F, 0.5F, 1.0F, 2F, 4F, 0, 2024, 0),
						new MonitorData(timestamp - 2, 28, 0.6F, 0.4F, 1.0F, 2F, 4F, 0, 2024, 0),
						new MonitorData(timestamp - 3, 27, 0.0F, 0.4F, 1.0F, 2.4F, 4F, 100, 4024, 4),
						new MonitorData(timestamp - 4, 26, 0.0F, 0.5F, 1.0F, 2.8F, 4F, 100, 1024, 100),
						new MonitorData(timestamp - 5, 25, 0.2F, 0.5F, 1.0F, 1F, 4F, 900, 24, 400),
						new MonitorData(timestamp - 6, 24, 0.4F, 0.6F, 1.0F, 2F, 4F, 1000, 0, 900),
						new MonitorData(timestamp - 7, 23, 0.9F, 0.7F, 1.0F, 4F, 4F, 600, 0, 100),
						new MonitorData(timestamp - 8, 22, 0.9F, 0.6F, 1.0F, 2F, 4F, 400, 1024, 512) }));
	}

	/**
	 * Krijg de meest recente monitor data voor het netwerkcomponent.
	 * 
	 * @param name De netwerkcomponent waar de data van wordt opgehaald.
	 * @return Monitor data
	 */
	public MonitorData getLatestDataForComponent(String name) {
		// TODO: implement
		return new MonitorData(System.currentTimeMillis() / 1000L, 30, 0.36F, 0.4F, 1.0F, 2F, 4F, 100, 1024, 512);
	}

	/**
	 * Deze methode kijkt of het netwerkcomponent aan of uit staat.
	 * 
	 * @param name De naam van het netwerkcomponent.
	 * @return True als het netwerkcomponent aan staat.
	 */
	public boolean getStatusForComponent(String name) {
		return name.equals("W1") ? false : true;
	}
	

	/**
	 * Er hoeft maar ��n instantie te zijn voor DataRetriever, dus die slaan wij
	 * hier op.
	 */
	private static DataRetriever instance = new DataRetriever();
	
	/**
	 * Voor deze klas hebben wij maar ��n instantie.
	 * @return De instantie van DataRetriever
	 */
	public static DataRetriever getInstance() {
		return instance;
	}

}