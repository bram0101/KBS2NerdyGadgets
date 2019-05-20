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
package me.team4.moniwerp.design;

import java.io.File; 

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Maakt een lijst met alle netwerkcomponenttypes.
 */
public class NetworkComponentTypes {
	/**
	 * Een lijst met NetworkComponentTypes
	 */
	private static NetworkComponentType[] types;

	/**
	 * Een getter voor de lijst NetworkComponentTypes,
	 * 
	 * @return NetworkComponentType[]
	 */
	public static NetworkComponentType[] getTypes() {
		return types;
	}

	/**
	 * Een getter voor een specifiek NetworkComponentType
	 * 
	 * @param name
	 */ 
	public static NetworkComponentType getTypes(String name) {
		// kijkt of de naam van de type gelijk is aan dar gene wat we zoeken, als die niks vindt dar returned die null 
		for (NetworkComponentType element: types) {
			if(element.getName().equals(name)) {
				return element; 
			}
		}
		return null; 
	}

	/**
	 * Laadt het aantal verschillende Networkcomponenttypes uit het
	 * configuratiebestand
	 */
	// xml file
	public static void load() {
		// 1. een xml file: componenttypes.xml
		File f = new File("./componenttypes.xml");
		try {
			//kijkt of het bestand wel bestaat, als het zo is return:  
			if (f.exists()) {
				System.out.println("File f exists");
			} else {
			//als het niet zo is return: 	
				System.out.println("File f not found");
				return;
			}

			//lees het bestand
			// 2. Instantiate XML file: DOM parser laadt het hele XML document in de 
			// memory en beschouwd elk XML tag als een element.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);

			// To get the element of an XML file
			Element e = doc.getDocumentElement();
			NodeList nodeList = e.getChildNodes();
			
			//Arraylist van type netwerkcomponenttype
			ArrayList<NetworkComponentType> netwerkComponentType = new ArrayList<NetworkComponentType>();
			
			//ga door elk element in het xml bestand en haal de data daaruit en voeg het toe aan het bestand.  
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node n = nodeList.item(i);
				//The node is an Element.
				if (n.getNodeType() == Node.ELEMENT_NODE) {
										
					//een nieuwe instantie van netwerkcomponenttype maken met 3 gegevens (name, costs, uptime) 
					NetworkComponentType nct = new NetworkComponentType(
							//
							n.getAttributes().getNamedItem("name").getNodeValue(),
							Integer.parseInt(n.getAttributes().getNamedItem("costs").getNodeValue()), 
							Float.parseFloat(n.getAttributes().getNamedItem("uptime").getNodeValue()));
					
					//nct toevoegen aan de arraylist 
					netwerkComponentType.add(nct); 
					
				}
			}
			
			//zet een arraylist om naar een array
			NetworkComponentTypes.types = new NetworkComponentType[netwerkComponentType.size()];
			for(int i = 0; i < netwerkComponentType.size(); i++) {
				NetworkComponentTypes.types[i] = netwerkComponentType.get(i);
			}


			
			// voeg elk type toe aan de variabele types
		} catch (Exception e) {
			System.out.println("Error reading file:");
			e.printStackTrace();
		}
	}
}
