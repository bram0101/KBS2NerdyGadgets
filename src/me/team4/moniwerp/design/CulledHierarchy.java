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

import java.util.concurrent.LinkedTransferQueue;

/**
 * roept de algoritme aan die de ideale infrastructuur berekend.
 */
public class CulledHierarchy {
	/**
	 * @param probleem de beschikbaarheid
	 * @return de meest geschikte infrastructuur
	 */
	public byte[] execute(int[][] problem, Calculator calculator, float minimumUptime) {
		// Met bestSolve houden wij bij wat de beste oplossing is. Het is nu
		// geinitialiseerd naar een oplossing met de hoogste kosten en geen echte solve.
		// Hiermee hoef je geen rekening te houden met dat bestSolve null kan zijn (want
		// die zal nooit null meer zijn) en alle echte oplossingen zullen altijd
		// goedkoper zijn dan Float.MAX.
		byte[] bestSolve = null;
		int bestCosts = Integer.MAX_VALUE;
		// De queue is een lijst met nodes die wij moeten verwerken. Wij pakken de
		// eerste node uit de lijst en als wij nodes toevoegen komen die aan het einde
		// van de lijst. Dit heet FIFO (first-in-first-out). LinkedTransferQueue is
		// gebaseerd op linked lists, een manier van lijsten bijhouden die efficiënter
		// is dan een normale array of ArrayList.
		LinkedTransferQueue<Node> queue = new LinkedTransferQueue<Node>();
		// Voor de snelheid en leesbaarheid slaan wij deze waarde op in een variabele.
		int listAmount = problem.length;
		// Bouw de offsetlijst op.
		int totalSolveSize = 0;
		int[] solveListOffset = new int[listAmount];
		for (int i = 0; i < problem.length; i++) {
			solveListOffset[i] = totalSolveSize;
			totalSolveSize += problem[i].length;
		}
		// Maak alvast wat variabelen aan.
		// De huidige node die wij aan het verwerken zijn.
		Node currentNode = null;
		// De huidige solve list met hoeveel componenten de solve heeft.
		byte[] currentComponentsList = null;
		// De huidige empty list.
		boolean[] currentEmptyList = null;
		// Uptime
		float uptime = 0F;
		// Random int variabel
		int i = 0;
		// Random boolean variabel
		boolean check = false;

		// Maak alvast een nieuwe solve lijst aan
		currentComponentsList = new byte[totalSolveSize];
		// Random int variabel
		int j = 0;
		// Random int variabel
		int k = 0;
		// Hier vinden wij een eerste solve. Dit is nodig voor de score systeem
		// Zolang de uptime nog niet het minimum heeft berijkt, voeg een
		// netwerkcomponent toe.
		while ((uptime = calculator.calcUptime(currentComponentsList)) < minimumUptime) {
			// Voeg eentje toe. Eerst bij A, dan bij B, dan bij C
			currentComponentsList[solveListOffset[j] + (k % problem[j].length)] += 1;
			j++;
			if (j == listAmount) {
				j = 0;
				k++;
			}
		}
		// Maak een solvedproblem aan.
		bestSolve = currentComponentsList;
		bestCosts = calculator.calcCosts(currentComponentsList);

		// Bereken de score voor deze solve.
		float perfRef = uptime / bestCosts;

		// Voeg de eerste paar nodes toe.
		// -1 als component id betekent geen component. Deze zit niet in de lijst van
		// problemDefinition dus doe voegen wij zo toe.
		currentComponentsList = new byte[totalSolveSize];
		queue.add(new Node(0, 0, currentComponentsList, new boolean[listAmount]));
		// Ga langs alle mogelijke componenten en voeg die toe aan de queue.
		i = 0;
		for (int l : problem[0]) {
			currentComponentsList = new byte[totalSolveSize];
			currentComponentsList[i++] = 1;
			queue.add(
					new Node(0, NetworkComponentTypes.getTypes()[l].getCosts(), currentComponentsList, new boolean[listAmount]));
		}

		// Ga door elke node in de queue en verwerk die.
		//
		// currentNode = eerste in de lijst. Is de lijst leeg, dan currentNode == null.
		// Met de expressie (currentNode = queue.poll()) != null ga ik door alle
		// elementen in de queue totdat die leeg is, maar op een manier waardoor ik
		// ondertussen ook elementen kan toevoegen.
		while ((currentNode = queue.poll()) != null) {
			// Wij zoeken de goedkoopste oplossing, dus als een node even duur of duurder
			// is, dan kunnen wij hem negeren.
			if (currentNode.cost >= bestCosts)
				continue;

			// Bereken de uptime
			uptime = calculator.calcUptime(currentNode.components);

			// Als de uptime het minimuum heeft behaald, dan is dit onze nieuwe beste
			// oplossing.
			// Vanwege de if statement hierboven, weten wij al dat de kosten ook later zijn.
			if (uptime >= minimumUptime) {
				bestSolve = currentNode.components;
				bestCosts = currentNode.cost;
			} else {
				// De kosten zijn wel lager, maar de uptime is nog niet bereikt dus we zoeken
				// verder.
				// Als de diepte verder is dan 2xlistAmount dan kunnen wij checken of de score
				// goed is.
				// Minimum is twee componenten per onbekende, dus daar komt die 2 vandaan.
				if (currentNode.depth >= listAmount * 2) {
					// Score moet minimaal 1% van referentie score zijn
					if ((uptime / currentNode.cost) < perfRef * 0.01F)
						continue;
				}

				// Een mogelijk is ook dat wij geen component toevoegen. Hiervoor moeten wij wel
				// een check doen, anders dan heb je een pad die oneindig lang doorgaat omdat je
				// elke keer geen componenten toevoegt.
				check = true;
				for (i = 0; i < listAmount; i++) {
					if (!currentNode.empty[i]) { // Check of er ergens nog een item is die niet true is, dan negeren wij
													// niet deze oplossing.
						check = false;
						break;
					}
				}
				// Stop met deze node als check true is
				if (check) {
					continue;
				}

				// Voeg nieuwe mogelijkheden toe.
				// Eerst toevoegen als wij geen componenten willen toevoegen.
				currentEmptyList = currentNode.empty.clone();
				currentEmptyList[(currentNode.depth + 1) % listAmount] = true;
				queue.add(new Node(currentNode.depth + 1, currentNode.cost, currentNode.components, currentEmptyList));

				// Ga langs de problemDefinition en voeg voor elk component een nieuwe
				// mogelijkheid toe.
				for (i = 0; i < problem[(currentNode.depth + 1) % listAmount].length; i++) {
					currentComponentsList = currentNode.components.clone();
					currentComponentsList[solveListOffset[(currentNode.depth + 1) % listAmount] + i] += 1;
					queue.add(new Node(currentNode.depth + 1,
							currentNode.cost + NetworkComponentTypes.getTypes()[problem[(currentNode.depth + 1) % listAmount][i]].getCosts(),
							currentComponentsList, currentNode.empty));
				}
			}
		}
		// Als wij een solve hebben gevonden, geef die terug.
		return bestSolve;
	}

	private static class Node {

		/**
		 * De diepte in de hierarchie van deze node
		 */
		public int depth;
		/**
		 * Hoeveel deze node cost. In dit geval als float, maar had ook int kunnen zijn
		 */
		public int cost;
		/**
		 * Hoeveel componenten deze node heeft.
		 */
		public byte[] components;
		/**
		 * Houdt bij of er was aangegeven dat er niks meer wordt toegevoegd.
		 */
		public boolean[] empty;

		public Node(int depth, int cost, byte[] components, boolean[] empty) {
			this.depth = depth;
			this.cost = cost;
			this.components = components;
			this.empty = empty;
		}

	}

}