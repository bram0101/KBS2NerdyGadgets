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
package me.team4.moniwerp.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import me.team4.moniwerp.io.DataRetriever;
import me.team4.moniwerp.io.MonitorData;

/**
 * Lijst met huidige informatie over de geselecteerde node
 *
 */
public class InfoList extends JPanel implements MouseListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * De naam van het component dat geselecteerd is in ViewportNetwork
	 */
	private String selectedComponent;

	/**
	 * Een lijst met welke waardes zijn geselecteerd in deze panel.
	 */
	private List<String> selectedData;

	//Lijst van alle variabelen.
	private JPanel JPuptime;
	private JLabel valUptime;

	private JPanel JPcpu;
	private JLabel cpuvalue;
	private boolean cpuSel = true;

	private JPanel JPram;
	private JLabel ramvalue;
	private boolean ramSel;

	private JPanel JPdiskUsage;
	private JLabel diskUsagevalue;
	private boolean diskUsageSel;

	private JPanel JPdiskBusyTime;
	private JLabel diskBusyTimevalue;
	private boolean diskBusyTimeSel;

	private JPanel JPbytesSend;
	private JLabel bytesSendvalue;
	private boolean bytesSendSel;

	private JPanel JPbytesReceived;
	private JLabel bytesReceivedvalue;
	private boolean bytesReceivedSel;

	public InfoList() {

		 // Layout is FlowLayout.
        this.setLayout(new FlowLayout());

        // Maak panel JPuptime
        JPuptime = new JPanel();
        JPuptime.setLayout(new BorderLayout());
        // Voeg JPuptime panel toe aan panel infoList
        this.add(JPuptime);

        // Maak labels voor JPuptime.
        JLabel uptime = new JLabel("    Uptime: ");
        uptime.setPreferredSize(new Dimension(120, 20));
        valUptime = new JLabel();

        // Voeg labels toe aan JPuptime.
        JPuptime.add(uptime, BorderLayout.WEST);
        JPuptime.add(valUptime, BorderLayout.CENTER);



        // Maak panel JPcpu
        JPcpu = new JPanel();
        JPcpu.setLayout(new BorderLayout());
        // Voeg JPcpu panel toe aan panel infoList
        this.add(JPcpu);

        JPcpu.addMouseListener(this);
        JPcpu.setBackground(getColours().get("cpu"));

        // Maak labels voor JPcpu.
        JLabel cpu = new JLabel("    Cpu usage: ");
        cpu.setPreferredSize(new Dimension(120, 20));
        cpuvalue = new JLabel();

        // Voeg labels toe aan JPcpu.
        JPcpu.add(cpu, BorderLayout.WEST);
        JPcpu.add(cpuvalue, BorderLayout.CENTER);



        // Maak panel JPram
        JPram = new JPanel();
        JPram.setLayout(new BorderLayout());
        // Voeg JPram panel toe aan panel infoList
        this.add(JPram);

        JPram.addMouseListener(this);

        // Maak labels voor JPram.
        JLabel ram = new JLabel("    Ram usage: ");
        ram.setPreferredSize(new Dimension(120, 20));
        ramvalue = new JLabel();

        // Voeg labels toe aan JPram.
        JPram.add(ram, BorderLayout.WEST);
        JPram.add(ramvalue, BorderLayout.CENTER);



        // Maak panel JPdiskUsage
        JPdiskUsage = new JPanel();
        JPdiskUsage.setLayout(new BorderLayout());
        // Voeg JPdiskUsage panel toe aan panel infoList
        this.add(JPdiskUsage);

        JPdiskUsage.addMouseListener(this);

        // Maak labels voor JPdiskUsage.
        JLabel diskUsage = new JLabel("    Disk usage: ");
        diskUsage.setPreferredSize(new Dimension(120, 20));
        diskUsagevalue = new JLabel();

        // Voeg labels toe aan JPdiskUsage.
        JPdiskUsage.add(diskUsage, BorderLayout.WEST);
        JPdiskUsage.add(diskUsagevalue, BorderLayout.CENTER);



        // Maak panel JPdiskBusyTime
        JPdiskBusyTime = new JPanel();
        JPdiskBusyTime.setLayout(new BorderLayout());
        // Voeg JPdiskBusyTime panel toe aan panel infoList
        this.add(JPdiskBusyTime);

        JPdiskBusyTime.addMouseListener(this);

        // Maak labels voor JPdiskBusyTime.
        JLabel diskBusyTime = new JLabel("    Disk BusyTime: ");
        diskBusyTime.setPreferredSize(new Dimension(120, 20));
        diskBusyTimevalue = new JLabel();

        // Voeg labels toe aan JPdiskBusyTime.
        JPdiskBusyTime.add(diskBusyTime, BorderLayout.WEST);
        JPdiskBusyTime.add(diskBusyTimevalue, BorderLayout.CENTER);



        // Maak panel JPbytesSend
        JPbytesSend = new JPanel();
        JPbytesSend.setLayout(new BorderLayout());
        // Voeg JPbytesSend panel toe aan panel infoList
        this.add(JPbytesSend);

        JPbytesSend.addMouseListener(this);

        // Maak labels voor JPbytesSend.
        JLabel bytesSend = new JLabel("    Bytes Sent: ");
        bytesSend.setPreferredSize(new Dimension(120, 20));
        bytesSendvalue = new JLabel();

        // Voeg labels toe aan JPnetwork.
        JPbytesSend.add(bytesSend, BorderLayout.WEST);
        JPbytesSend.add(bytesSendvalue, BorderLayout.CENTER);



        // Maak panel JPbytesReceived
        JPbytesReceived = new JPanel();
        JPbytesReceived.setLayout(new BorderLayout());
        // Voeg JPbytesReceived panel toe aan panel infoList
        this.add(JPbytesReceived);

        JPbytesReceived.addMouseListener(this);

        // Maak labels voor JPbytesReceived.
        JLabel bytesReceived = new JLabel("    Bytes Received: ");
        bytesReceived.setPreferredSize(new Dimension(120, 20));
        bytesReceivedvalue = new JLabel();

        // Voeg labels toe aan JPnetwork.
        JPbytesReceived.add(bytesReceived, BorderLayout.WEST);
        JPbytesReceived.add(bytesReceivedvalue, BorderLayout.CENTER);

        update();
	}

	/**
	 * Geef aan van welke component de data moet worden weergegeven.
	 *
	 * @param name De naam van het geselecteerde component
	 */
	public void setSelectedComponent(String name) {
		// TODO: implement
		selectedComponent = name;
	}

	/**
	 * De naam van het component waarvan de data moet worden weergegeven.
	 *
	 * @return Het geselecteerde component
	 */
	public String getSelectedComponent() {
		return selectedComponent;
	}

	/**
	 * Een lijst met de namen van de data dat geselecteerd is. Dit is om aan te
	 * geven welke waardes in de grafiek moeten worden weergegeven.
	 *
	 * @return lijst met de namen van geselecteerde data.
	 */
	public List<String> getSelectedData() {
		List<String> selectedData = new ArrayList<String>();
		if(cpuSel) {
			selectedData.add("cpu");
		}
		if(ramSel) {
			selectedData.add("ram");
		}
		if(diskUsageSel) {
			selectedData.add("diskUsageSel");
		}
		if(diskUsageSel) {
			selectedData.add("diskBusyTimeSel");
		}
		if(diskUsageSel) {
			selectedData.add("bytesSendSel");
		}
		if(diskUsageSel) {
			selectedData.add("bytesReceivedSel");
		}
		return selectedData;
	}

	/**
	 * De kleur dat moet worden gebruikt voor het tekenen van bepaalde data. De key
	 * is de naam van de data en de value is welke kleur het moet zijn.
	 *
	 * @return
	 */
	public HashMap<String, Color> getColours() {
		HashMap<String, Color> map = new HashMap<String, Color>();
		map.put("uptime", Color.BLUE);
		map.put("cpu", Color.CYAN);
		map.put("ram", Color.MAGENTA);
		map.put("diskUsage", Color.YELLOW);
		map.put("diskBusyTime", Color.ORANGE);
		map.put("bytesSend", Color.RED);
		map.put("bytesReceived", Color.GREEN);
		return map;
	}

	/**
	 * Deze methode wordt geroepen als er een update moet plaatsvinden. Deze wordt
	 * elke seconde geroepen door TabMonitor
	 */
	public void update() {
		// haal monitordata op
		MonitorData data = DataRetriever.getInstance().getLatestDataForComponent(getSelectedComponent());
		long timeActive = (System.currentTimeMillis()/1000L-data.getUptime());
		long days = timeActive/(24*60*60);
		long hours = (timeActive/(60*60))%24;
		long minutes = (timeActive/60)%60;
		long seconds = (timeActive)%60;

		valUptime.setText(days+":"+hours+":"+minutes+":"+seconds);
		cpuvalue.setText(Math.round(data.getCpu() * 100.0) + "%");
		ramvalue.setText(data.getRamTotal() + " GB / " + data.getRamUsed()+" GB");
		diskUsagevalue.setText(data.getDiskTotal() + " GB / " + data.getDiskUsed()+" GB");
		diskBusyTimevalue.setText(data.getDiskBusyTime()+" ms");
		bytesSendvalue.setText(data.getBytesSent()+" Byes");
		bytesReceivedvalue.setText(data.getBytesReceived()+" Bytes");
	}
	// Geef JPanels een bepaalde grootte.
	public void onResizeComponent(int width, int height) {
		JPuptime.setPreferredSize(new Dimension(width, 24));
		JPcpu.setPreferredSize(new Dimension(width, 24));
		JPram.setPreferredSize(new Dimension(width, 24));
		JPdiskUsage.setPreferredSize(new Dimension(width, 24));
		JPdiskBusyTime.setPreferredSize(new Dimension(width, 24));
		JPbytesSend.setPreferredSize(new Dimension(width, 24));
		JPbytesReceived.setPreferredSize(new Dimension(width, 24));
	}

	@Override
	// Kijk of het JPanel is geselecteerd en geef de correcte achtergrond kleur.
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getComponent() == JPcpu) {
			if(cpuSel) {
				cpuSel=false;
				JPcpu.setBackground(null);
			}else {
				cpuSel=true;
				JPcpu.setBackground(getColours().get("cpu"));;
			}
		}

		if(e.getComponent() == JPram) {
			if(ramSel) {
				ramSel=false;
				JPram.setBackground(null);
			}else {
				ramSel=true;
				JPram.setBackground(getColours().get("ram"));;
			}
		}

		if(e.getComponent() == JPdiskUsage) {
			if(diskUsageSel) {
				diskUsageSel=false;
				JPdiskUsage.setBackground(null);
			}else {
				diskUsageSel=true;
				JPdiskUsage.setBackground(getColours().get("diskUsage"));;
			}
		}

		if(e.getComponent() == JPdiskBusyTime) {
			if(diskBusyTimeSel) {
				diskBusyTimeSel=false;
				JPdiskBusyTime.setBackground(null);
			}else {
				diskBusyTimeSel=true;
				JPdiskBusyTime.setBackground(getColours().get("diskBusyTime"));;
			}
		}

		if(e.getComponent() == JPbytesSend) {
			if(bytesSendSel) {
				bytesSendSel=false;
				JPbytesSend.setBackground(null);
			}else {
				bytesSendSel=true;
				JPbytesSend.setBackground(getColours().get("bytesSend"));;
			}
		}

		if(e.getComponent() == JPbytesReceived) {
			if(bytesReceivedSel) {
				bytesReceivedSel=false;
				JPbytesReceived.setBackground(null);
			}else {
				bytesReceivedSel=true;
				JPbytesReceived.setBackground(getColours().get("bytesReceived"));;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
