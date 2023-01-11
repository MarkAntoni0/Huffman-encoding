//////////////////////////////////////////////////////////
// This code aims to encode using Huffman encoding      //
// Written by: Mark Tharwat - 19200164                  //
// Eng.mark.antonio@icloud.com                          //
// https://www.linkedin.com/in/mark-antonio-b2777310a/  //
// Feel free to use or edit the code                    //
// No copy rights                                       //
// Update the globalLocation variable to run            //
//////////////////////////////////////////////////////////
package Code;

import java.util.PriorityQueue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

class Huffman {
	
//  Consider updating the following path to match your text file location 
	static String globalLocation = "/home/ironhide/eclipse-workspace/HuffmanFinalProject/src/Code/characterSet";
//  The string builder below aims to keep the encoded characters once they are discovered in the dictionary
	static StringBuilder toBeDecodedString = new StringBuilder();
//	The dictionary is being build once the print function discovers a new encoding for a letter
	static HashMap<Character, String> toBeDeencodedDictionary = new HashMap<Character, String>();
//  For statistics purposes 
	static int decodedLength=0;
	
	
//	The following functions aims to create a dictionary out of the discovered characters encoding
	public static void buildEncodedText() throws IOException {
		Path filePath = Path.of(globalLocation);
		String content = Files.readString(filePath);
		
		for(int i=0;i<content.length()-1;i++) {
			toBeDecodedString.append(toBeDeencodedDictionary.get(content.charAt(i)));
		}
	}
	
	
//	The following function aims to decode the values in the toBeDecodedString global variable with the assistance
//	of the tree, thus we pass the original tree to it
	public static void decodefinalCode(String encodedString, Node rootNode) {
	    StringBuilder decodedText = new StringBuilder();
	    Node currentNodetoBeDecodedString = rootNode;
	    for (int i = 0; i < encodedString.length(); i++) {
	        if(encodedString.charAt(i) == '1') {
	        	currentNodetoBeDecodedString = currentNodetoBeDecodedString.right;
	        }else {
	        	currentNodetoBeDecodedString = currentNodetoBeDecodedString.left;
	        }
	        
	        if (currentNodetoBeDecodedString.left == null && currentNodetoBeDecodedString.right == null) {
	            decodedText.append(currentNodetoBeDecodedString.c);
	            currentNodetoBeDecodedString = rootNode;
	        }
	    }
	    System.out.println("DecodedString text is :  "+decodedText);
	    decodedLength=decodedText.length();
	}
	
//	The printFinalCode function aims to print the encoding per character using 
//	Recursion. 
//	The function takes the root node which was generated using the main function 
//	and aims to traverse the nodes in order to generate the encoding.
//	The String s aims to save the encoding of the last recursion 
//	while entering the next recursion
	
	public static void printFinalCode(Node rootNode, String s) {
		if (rootNode.left == null && rootNode.right == null && Character.isLetter(rootNode.c)) {
			System.out.println(rootNode.c + " > " + s);
			//toBeDecodedString.append(s);
			toBeDeencodedDictionary.put(rootNode.c, s);
			return;
		} else {
			printFinalCode(rootNode.left, s + "0");
			printFinalCode(rootNode.right, s + "1");
		}
	}

//	The main function would start it's work by generating two arraylists to extract the chracters from the text
//	file into them. 
//	The variable n aims to keep track of the unique chracters found.
//  The reason why we use arrayList instead of array to receive valeus from the text is the remocal of LF character	
	
	public static void main(String[] args) throws IOException {
		int n = 0;

		ArrayList<Character> characters = new ArrayList<Character>();
		ArrayList<Integer>  frequencies = new ArrayList<Integer>();
		
//		This path can be changed to match the text file you would like to encode 
		Path filePath = Path.of(globalLocation);
		String content = Files.readString(filePath);

//		The following lines of code aims to find the frequency of each character in the
//		given text while placing the repeating characters to 0 to avoid duplication.
		int[] freq = new int[content.length()];                          // freq aims to keep track of the frequency of charactes
		System.out.println("The entered string is " + content);          // for debugging purposes
		
		char str1[] = content.toCharArray();                             // Convert the given string into character array
		for (int i = 0; i < str1.length; i++) {
			freq[i] = 1;
			for (int j = i + 1; j < content.length(); j++) {             // Traversing what is after the currentNodetoBeDecodedStringent character
				if (str1[i] == str1[j]) {
					freq[i]++;
					str1[j] = '0';                                       // Set str1[j] to 0 to avoid printing visited character
				}
			}
		}
		// Displays the characters and their corresponding frequency
		System.out.println("Characters     frequencies");
		for (int i = 0; i < freq.length - 1; i++) {                      // -1 for the lf character
			if (str1[i] != ' ' && str1[i] != '0') {
				System.out.println(str1[i] + "              " + freq[i]);
				n++;
				characters.add(str1[i]);                                 // inserting the character into array list
				frequencies.add(freq[i]);                                // inserting the frequency into array list
			}
		}

		System.out.println("Total unique character= " + (n));
		System.out.println("");

		System.out.println(Arrays.deepToString(characters.toArray()));  // for debugging purposes
		System.out.println(Arrays.deepToString(frequencies.toArray())); // for debugging purposes
		System.out.println("");

		char[] charArray = new char[characters.size()];                 // preparing array to receive array list values
		int[]   charfreq = new int[frequencies.size()];                 // preparing array to receive array list values

		for (int i = 0; i < n; i++) {                                   // looping over each element and adding them to array
			charArray[i] = characters.get(i);
			charfreq[i]  = frequencies.get(i);
		}

//		The priority queue rearrange itself based on a comparison criteria which is given through the comparison
		PriorityQueue<Node> q = new PriorityQueue<Node>(n, new ComparisonCriteria());
		
		for (int i = 0; i < n; i++) {                                   // Adding all created nodes to the queue
			Node newCreatedNode = new Node();
			newCreatedNode.c = charArray[i];
			newCreatedNode.frequency = charfreq[i];
			newCreatedNode.left = null;
			newCreatedNode.right = null;

			q.add(newCreatedNode);
		}

		Node rootNode = null;                                           // Grouping the nodes together to the roor
		while (q.size() > 1) {

			Node x = q.peek();                                          // Picking the first node with removal
			q.poll();                                                   // Moving the queue a step ahead
			Node y = q.peek();                                          // Picking the first node with removal
			q.poll();                                                   // Moving the queue a step ahead

			Node f = new Node();                                        // Creating a new root for the popped two nodes
			f.frequency = x.frequency + y.frequency;
			f.c = '*';

			f.left = x;
			f.right = y;

			rootNode = f;
			q.add(f);
		}

		
		System.out.println("");
		System.out.println("Encoding Dictionary");
		printFinalCode(rootNode, "");
		buildEncodedText();
		System.out.println("Encoded text is       :  "+toBeDecodedString);
		decodefinalCode(toBeDecodedString.toString(), rootNode);
		
		//printing compression statistics
		System.out.println("Original string size  :  "+(content.length()*8) + " bits");
		System.out.println("Compressed size       :  "+decodedLength+ "  bits");
	}
}

class Node {
	int frequency;
	char c;

	Node left;
	Node right;
}

class ComparisonCriteria implements Comparator<Node> {
	public int compare(Node x, Node y) {
		return x.frequency - y.frequency;
	}
}
