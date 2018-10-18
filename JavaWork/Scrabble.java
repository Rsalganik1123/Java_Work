
// STUDENT_NAME: Rebecca Salganik		
// STUDENT_ID: 260673178
import java.lang.Object;
//import org.apache.commons.lang.ArrayUtils;
import java.util.*;
import java.io.*;

public class Scrabble {

	static HashSet<String> myDictionary; // this is where the words of the
											// dictionary are stored

	// DO NOT CHANGE THIS METHOD
	// Reads dictionary from file

	public static void readDictionaryFromFile(String fileName) throws Exception {
		myDictionary = new HashSet<String>();

		BufferedReader myFileReader = new BufferedReader(new FileReader(fileName));

		String word;
		while ((word = myFileReader.readLine()) != null)
			myDictionary.add(word);

		myFileReader.close();
	}

	/*
	 * Arguments: char availableLetters[] : array of characters containing the
	 * letters that remain available String prefix : Word assembled to date
	 * Returns: String corresponding to the longest English word starting with
	 * prefix, completed with zero or more letters from availableLetters. If no
	 * such word exists, it returns the String ""
	 */
	public static String longestWord(char availableLetters[], String prefix) {
		String tmp2Longest = ""; //prefix 
		String tmpLongest = ""; 
		String longest = "";  
		if(availableLetters.length == 0){ //availableLetters.length == 0 && myDictionary.contains(prefix)
			longest = prefix; 
			//return longest; 
		}
		/*base case*/ 
		else if (availableLetters.length == 0 ) { //&& !(myDictionary.contains(prefix))
			//longest = "";
			return longest; 	
		}
		/* recursion */
		for (int i = 0; i < availableLetters.length; i++) {
			tmpLongest = prefix + availableLetters[i]; //tmpLongest = prefix + availableLetters[i]; tmpLongest += availableLetters[i]
			char availableNew [] = cycleThrough(availableLetters, i); 
			if (myDictionary.contains(tmpLongest) && tmpLongest.length() > longest.length()) {
				longest = tmpLongest; 
				} 
			tmp2Longest = longestWord(availableNew, tmpLongest); 
			if (tmp2Longest.length() > longest.length() && myDictionary.contains(tmp2Longest)){
				longest = tmp2Longest; 
			}
		}
		return longest;
	}
	
	
	
	
	public static char[] cycleThrough(char a[], int index){
		char b[] = new char[a.length -1]; 
		//System.out.println("a.length " + a.length);
		 
		for(int i = 0; i < index; i++){
			//System.out.println("a[i] = " + a[i]); 
			//System.out.println("here1");
				b[i] = a[i]; 
		}
		for(int j = index + 1; j < a.length ; j++) { 
			//System.out.println("b[j] = " + b[j]);
			//System.out.println("here2");
				b[j-1] = a[j]; 
		}
		return b; 
	}

public static void main (String args[]) throws Exception {
   
// First, read the dictionary
try {
    readDictionaryFromFile("englishDictionary.txt");
    }
    catch(Exception e) {
        System.out.println("Error reading the dictionary: "+e);
    }
    
    
    // Ask user to type in letters
    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in) );
    char letters[]; 
    do {
        System.out.println("Enter your letters (no spaces or commas):");
        
        letters = keyboard.readLine().toCharArray();

    // now, enumerate the words that can be formed
        String longest = longestWord(letters, "compu");
    System.out.println("The longest word is "+ longest);
    } while (letters.length!=0);

    keyboard.close();
    
}
}



