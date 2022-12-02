/**
* Island.java by Peter Olson
*
* Converts between Island language and English and vice versa
*
* -- *Island is a dynamic, alphabetic-looking language used to encrypt messages
*
*@author Peter Olson
*@version 1.0
*/

import java.util.Random;
import java.util.Scanner;

public class Island{
	
	public String text;
	
	/**
	* Default constructor
	*
	*/
	public Island(){}
	
	/**
	* Create a new Island object with text to use for encryption
	*
	*@param text Text to encrypt
	*/
	public Island(String text){
		this.text = text;
	}
	
	/**
	* Runs tests() to test functionality of program
	*
	*@param args Command-line arguments, not used
	*/
	public static void main(String[] args){
		tests();
	}
	
	/**
	* Tests functionality of program by prompting user for text to be
	* encrypted/decrypted
	*
	*/
	private static void tests(){
		
	}
	
	public static String toIsland(String text){
	
		String result = text;
		
		result = result.trim();
		result = result.toUpperCase();
	
		// If text contains non-alphabetic character, return error String
		if(!isAlpha(result))
			return "Unrecognized Non-Alphabetic Letter";
		
		// Generate island sequence
		int[][] islandSequence = new int[result.length()][result.length()];
		Random rand = new Random();
		
		for(int i = 0; i < islandSequence.length; i++){
			
		}
		
		return result;
	}
	
	/**
	* Checks if the String is made up of all alphabetic values. If so, returns true and returns false
	* otherwise.
	*
	*@param input The text to be checked
	*@return boolean True if String is completely alphabetic, false otherwise
	*/
	private static boolean isAlpha(String input){
		char[] chArr = input.toCharArray();
		boolean done = true;
		for(int i = 0; i < chArr.length; i++)
			if(!Character.isAlphabetic(chArr[i]))
				return false;
		return true;
	}
}