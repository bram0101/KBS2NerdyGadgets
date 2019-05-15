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
package me.team4.nettest;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class NetworkUtils {

	public static boolean ping(String ip) {
		try {
			InetAddress inet = InetAddress.getByName(ip);
			if (inet.isReachable(5000))
				return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean http(String ip, String file) {
		try {
			URL url = new URL("http://" + ip + "/" + file);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setInstanceFollowRedirects(true);

			int status = con.getResponseCode();

			if (status == 200)
				return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static boolean https(String ip, String file) {
		try {
			URL url = new URL("https://" + ip + "/" + file);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setInstanceFollowRedirects(true);

			int status = con.getResponseCode();

			if (status == 200)
				return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean sql(String ip, String database, String user, String pass) {
		try {
			String DB_URL = "jdbc:mysql://" + ip + "/" + database + "?useLegacyDatetimeCode=false&serverTimezone=UTC";
			Connection connection = DriverManager.getConnection(DB_URL, user, pass);
			Statement stmt = connection.createStatement();
			stmt.executeQuery("SHOW VARIABLES LIKE \"%version%\";");
			connection.close();
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
