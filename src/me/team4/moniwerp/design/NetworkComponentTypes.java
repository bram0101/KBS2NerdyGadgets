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
package me.team4.moniwerp.design;
/**
 * Maakt een lijst met alle netwerkcomponenttypes.
 */
public class NetworkComponentTypes {
	/**
	 * Een lijst met NetworkComponentTypes
	 */
	private static NetworkComponentType[] types;

	//TODO implement
	/**
	 * Een getter voor de lijst NetworkComponentTypes, 
	 * @return NetworkComponentType[]
	 */
	public static NetworkComponentType[] getTypes() {
		return new NetworkComponentType[] {
				new NetworkComponentType("Database", 1000, 0.95F),
				new NetworkComponentType("Webserver", 1500, 0.9F),
				new NetworkComponentType("pfSense", 2000, 0.9F),
		};
	}
	/**
	 * 	Een getter voor een specifiek NetworkComponentType
	 * @param name
	 */
	public static NetworkComponentType getTypes(String name) {
		return new NetworkComponentType(name, 0, 0);
	}
	/**
	 * Laadt het aantal verschillende Networkcomponenttypes uit het configuratiebestand
	 */
	public static void load() {
	}
}
