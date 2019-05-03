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
package me.team4.moniwerp.io;

/**
 * Deze klas slaat de monitordata op voor een netwerkcomponent op een bepaalde tijd
 *
 */
public class MonitorData {

	/**
	 * De tijd wanneer data was opgehaald. De waarde is in seconden sinds Epoch
	 */
	private long timestamp;
	/**
	 * Het aantal seconden dat de machine aan staat.
	 */
	private int uptime;
	/**
	 * Hoeveel cpu er wordt gebruikt. 0.0F is 0%. 1.0F is 100%
	 */
	private float cpu;
	/**
	 * Het aantal geheugen dat wordt gebruikt in gigabytes
	 */
	private float ramUsed;
	/**
	 * Het totaal aantal geheugen in het systeem in gigabytes.
	 */
	private float ramTotal;
	/**
	 * Het aantal gigabytes van de schijf dat wordt gebruikt.
	 */
	private float diskUsed;
	/**
	 * Het totaal aantal gigabytes dat de schijven hebben.
	 */
	private float diskTotal;
	/**
	 * Hoe lang de hardeschijf bezig is, in milliseconden.
	 */
	private int diskBusyTime;
	/**
	 * Het aantal bytes dat is verstuurt via het netwerk.
	 */
	private int bytesSent;
	/**
	 * Het aantal bytes dat is binnengekomen via het netwerk.
	 */
	private int bytesReceived;

	/**
	 * @param timestamp     De tijd wanneer data was opgehaald. De waarde is in
	 *                      seconden sinds Epoch
	 * @param uptime        Het aantal seconden dat de machine aan staat.
	 * @param cpu           Hoeveel cpu er wordt gebruikt. 0.0F is 0%. 1.0F is 100%
	 * @param ramUsed       Het aantal geheugen dat wordt gebruikt in gigabytes
	 * @param ramTotal      Het aantal gigabytes van de schijf dat wordt gebruikt.
	 * @param diskUsed      Het aantal gigabytes van de schijf dat wordt gebruikt.
	 * @param diskTotal     Het totaal aantal gigabytes dat de schijven hebben.
	 * @param diskBusyTime  Hoe lang de hardeschijf bezig is, in milliseconden.
	 * @param bytesSent     Het aantal bytes dat is verstuurt via het netwerk.
	 * @param bytesReceived Het aantal bytes dat is binnengekomen via het netwerk.
	 */
	public MonitorData(long timestamp, int uptime, float cpu, float ramUsed, float ramTotal, float diskUsed,
			float diskTotal, int diskBusyTime, int bytesSent, int bytesReceived) {
		super();
		this.timestamp = timestamp;
		this.uptime = uptime;
		this.cpu = cpu;
		this.ramUsed = ramUsed;
		this.ramTotal = ramTotal;
		this.diskUsed = diskUsed;
		this.diskTotal = diskTotal;
		this.diskBusyTime = diskBusyTime;
		this.bytesSent = bytesSent;
		this.bytesReceived = bytesReceived;
	}

	/**
	 * De tijd wanneer data was opgehaald. De waarde is in seconden sinds Epoch
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Het aantal seconden dat de machine aan staat.
	 */
	public int getUptime() {
		return uptime;
	}

	/**
	 * Hoeveel cpu er wordt gebruikt. 0.0F is 0%. 1.0F is 100%
	 */
	public float getCpu() {
		return cpu;
	}

	/**
	 * Het aantal geheugen dat wordt gebruikt in gigabytes
	 */
	public float getRamUsed() {
		return ramUsed;
	}

	/**
	 * Het totaal aantal geheugen in het systeem in gigabytes.
	 */
	public float getRamTotal() {
		return ramTotal;
	}

	/**
	 * Het aantal gigabytes van de schijf dat wordt gebruikt.
	 */
	public float getDiskUsed() {
		return diskUsed;
	}

	/**
	 * Het totaal aantal gigabytes dat de schijven hebben.
	 */
	public float getDiskTotal() {
		return diskTotal;
	}

	/**
	 * Hoe lang de hardeschijf bezig is, in milliseconden.
	 */
	public int getDiskBusyTime() {
		return diskBusyTime;
	}

	/**
	 * Het aantal bytes dat is verstuurt via het netwerk.
	 */
	public int getBytesSent() {
		return bytesSent;
	}

	/**
	 * Het aantal bytes dat is binnengekomen via het netwerk.
	 */
	public int getBytesReceived() {
		return bytesReceived;
	}

}
