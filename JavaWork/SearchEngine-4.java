import java.util.*;
import java.io.*;

//Rebecca Salganik 260673178

// This class implements a google-like search engine
public class SearchEngine {

	public HashMap<String, LinkedList<String>> wordIndex; // this will contain a
															// set of pairs
															// (String,
															// LinkedList of
															// Strings)
	public DirectedGraph internet; // this is our internet graph

	// Constructor initializes everything to empty data structures
	// It also sets the location of the internet files
	SearchEngine() {
		// Below is the directory that contains all the internet files
		HtmlParsing.internetFilesLocation = "internetFiles";
		wordIndex = new HashMap<String, LinkedList<String>>();
		internet = new DirectedGraph();
	} // end of constructor//2017

	// Returns a String description of a searchEngine
	public String toString() {
		return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
	}

	// This does a graph traversal of the internet, starting at the given url.
	// For each new vertex seen, it updates the wordIndex, the internet graph,
	// and the set of visited vertices.

	void traverseInternet(String url) throws Exception {
		
		/*
		 * Hints 0) This should take about 50-70 lines of code (or less) 1) To
		 * parse the content of the url, call htmlParsing.getContent(url), which
		 * returns a LinkedList of Strings containing all the words at the given
		 * url. Also call htmlParsing.getLinks(url). and assign their results to
		 * a LinkedList of Strings. 2) To iterate over all elements of a
		 * LinkedList, use an Iterator, as described in the text of the
		 * assignment 3) Refer to the description of the LinkedList methods at
		 * http://docs.oracle.com/javase/6/docs/api/ . You will most likely need
		 * to use the methods contains(String s), addLast(String s), iterator()
		 * 4) Refer to the description of the HashMap methods at
		 * http://docs.oracle.com/javase/6/docs/api/ . You will most likely need
		 * to use the methods containsKey(String s), get(String s), put(String
		 * s, LinkedList l).
		 */
		if (internet.getVisited(url) == false) { //if url hasn't been visited
			internet.addVertex(url); // adds current url to graph
			internet.visited.put(url, true); // now url has been visited 
			LinkedList<String> content = HtmlParsing.getContent(url); //linked list of words from url
			Iterator<String> i = content.iterator(); //iterate through L.L of words
			while (i.hasNext()) { //cycle through them 
				String word = i.next(); //each word 
				if (!wordIndex.containsKey(word)) { //if not in wordIndex, add
					LinkedList<String> tempLinks = new LinkedList<String>(); //new L.L of links to the word
					tempLinks.add(url); //add url to L.L 
					wordIndex.put(word, tempLinks); //put in wordIndex with "word" as key
				} else {
					wordIndex.get(word).addLast(url); //if already exists, just add url to L.L in wordIndex
				}
			}
			LinkedList<String> links = HtmlParsing.getLinks(url); //L.L of links linked to by url
			Iterator<String> k = links.iterator(); // iterates over L.L
			while (k.hasNext()) { // cycle
				String link = k.next(); //each link
				internet.addEdge(url, link); //add to internet graph 
				traverseInternet(link); // recursive call
			}
		}

	} // end of traverseInternet

	/*
	 * This computes the pageRanks for every vertex in the internet graph. It
	 * will only be called after the internet graph has been constructed using
	 * traverseInternet. Use the iterative procedure described in the text of
	 * the assignment to compute the pageRanks for every vertices in the graph.
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	void computePageRanks() {
		
		LinkedList<String> vertices = internet.getVertices();
		Iterator<String> a = vertices.iterator(); // for each vertex
		while (a.hasNext()) { // iterate
			String curPage = a.next(); //current page 
			Double OGRank = 1.0; // original pRank
			internet.setPageRank(curPage, OGRank); //set each page with 
		}
		for (int i = 0; i < 100; i++) {
			Iterator<String> b = vertices.iterator();
			while (b.hasNext()) {
				String curPage = b.next();
				LinkedList<String> pointingUrls = internet.getEdgesInto(curPage);
				Iterator<String> c = pointingUrls.iterator();
				Double curPageRank = .5; // updated pRank
				while (c.hasNext()) {
					String intoPage = c.next();
					curPageRank = curPageRank + .5 * (internet.getPageRank(intoPage) / (double) internet.getOutDegree(intoPage));
				}
				internet.setPageRank(curPage, curPageRank);
			}
		}

	} // end of computePageRanks

	/*
	 * Returns the URL of the page with the high page-rank containing the query
	 * word Returns the String "" if no web site contains the query. This method
	 * can only be called after the computePageRanks method has been executed.
	 * Start by obtaining the list of URLs containing the query word. Then
	 * return the URL with the highest pageRank. This method should take about
	 * 25 lines of code.
	 */
	String getBestURL(String query) {
		// System.out.println(wordIndex.containsValue(query));
		String best; //will be returned 
		if (!wordIndex.containsKey(query)) { //if word not in wordIndex 
			System.out.println("does not contain query");
			return null;
		}
		LinkedList<String> containing = wordIndex.get(query); //if word included in wordIndex
		Iterator<String> m = containing.iterator(); //iterate through adjacency list 
		best = containing.getFirst(); 
		while (m.hasNext()) {
			String check = m.next();

			if (internet.getPageRank(check) > internet.getPageRank(best)) {
				best = check; //compare and find highest pagerank

			}
		}

		return best;
		
	} // end of getBestURL

	public static void main(String args[]) throws Exception {
		SearchEngine mySearchEngine = new SearchEngine();
		// to debug your program, start with.
		// mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");

		// When your program is working on the small example, move on to
		mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");

		mySearchEngine.computePageRanks();

		System.out.println(mySearchEngine);

		BufferedReader stndin = new BufferedReader(new InputStreamReader(System.in));
		String query;
		do {
			System.out.print("Enter query: ");
			query = stndin.readLine();
			if (query != null && query.length() > 0) {
				System.out.println("Best site = " + mySearchEngine.getBestURL(query));
			}
		} while (query != null && query.length() > 0);
	} // end of main
}