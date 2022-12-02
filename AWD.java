/**
* AWD.java by Peter Olson
*
* Converts between AWD and English, uses file input/output or compile interface to store/display results
*
* -- *AWD is a dynamic code used to encrypt messages
*
* *********** PUBLIC GLOBAL VARIABLES *********************************************
*
* static final int MAX_AWD_VAL = 57					--> ASCII value of largest allowable digit used in AWD
*
* static final int MIN_ENG_VAL = 65					--> ASCII value of smallest alphabetic letter used in ENG
*
* static final int ALPHABET_SIZE = 26				--> Size of ENG alphabet
*
* static final int MAX_INT_DIGITS = 9				--> Largest allowed digit length an integer
*																	 (used for String parsing)
*
* *********** CONSTRUCTORS ********************************************************
*
* AWD()					--> Default constructor
*
* AWD( String text )	--> Instantiate text for the translator to use
*
* *********** PUBLIC METHODS ******************************************************
*
* static void main( String[] args )					--> Converts text or files to AWD or to English
*
* *********** PRIVATE METHODS *****************************************************
*
* static void convertUsingFile()						--> Convert an input text file to AWD or English and write
*																	 the translation to a user specified output text file;
*																	 used by main(..) method
*
* static boolean isNumber( String text )			--> Determines if the String is a number or not;
*																	 used by convertUsingFile() method
*
* static String getRandomMaxIntLongSubstring( String input )
*																--> Gets a substring that is up to MAX_INT_DIGITS long
*																	 from the String. The substring's starting index is
*																	 randomly chosen. If the String is shorter than
*																	 MAX_INT_DIGITS, the whole String is returned; used in
*																	 convertUsingFile() method
*
* static String convertUsingCommand()				--> Convert a String input to AWD or English using the
*																	 command prompt; used by main(..) method
*
* static String convert( String text )				--> Determines whether the text will be converted to English
*																	 or to AWD and calls the corresponding method, either
*																	 toEng(..) or toAWD(..); used by convertUsingFile() and
*																	 convertUsingCommand()
*
* static String toEng( String text )				--> Converts text from the AWD language to English;
*																	 used by convert(..) method
*
* static String toAWD( String text )				--> Converts text from English to the AWD language;
*																	 used by convert(..) method
*
*
*
* @author Peter Olson
* @version 1.0
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.IOException;

/**
* Main file that converts between the coded language AWD and English (ENG for short).
* Allows user to convert using a file, to choose whether to convert from AWD to ENG 
* or from ENG to AWD, or whether to handle output just using the terminal
*
*/
public class AWD{
	
	public static final int MAX_AWD_VAL = 57; 	// ASCII value of largest allowable digit used in AWD
	public static final int MIN_ENG_VAL = 65; 	// ASCII value of smallest alphabetic letter used in ENG
	public static final int ALPHABET_SIZE = 26; 	// Size of ENG alphabet
	public static final int MAX_INT_DIGITS = 9; 	// Largest allowed digit length of an integer 
																// (used for String parsing)
	public String text;
	
	/**
	* Asks whether the user wants to use a file as input to convert from or
	* whether the user wants to just use command line output. From here, the program goes
	* on to establish which was the conversion is taking place.
	*
	*@param args Command line arguments (not used)
	*/
	public static void main(String[] args){
	
		String willContinue = "";
	
		do{
	
			System.out.println("Would you like to use a file?");
			
			Scanner scan = new Scanner(System.in);
			String answer = scan.next();
			answer = answer.toUpperCase(); // INPUT --> TO_UPPERCASE
			
			if(answer.equals("YES") || answer.equals("Y") || answer.equals("TRUE"))
				convertUsingFile();
		   else if(answer.equals("NO") || answer.equals("N") || answer.equals("FALSE"))
				System.out.println("Translation: " + convertUsingCommand() + "\n");
			else
				System.out.println("\nInvalid input. Try answering 'yes' or 'no' next time.");
		
			System.out.println("Would you like to convert something else?");
			willContinue = scan.next();
			willContinue = willContinue.trim();
			willContinue = willContinue.toUpperCase();
		
		}while(willContinue.equals("YES") || willContinue.equals("Y") || willContinue.equals("TRUE"));
	}
	
	/**
	* Default constructor
	*
	*/
	public AWD(){}
	
	/**
	* Create a new AWD object with text to use for translation
	*
	*@param text The text for this object to hold
	*/
	public AWD(String text){
		this.text = text;
	}
	
	/**
	* Determines whether to convert from the input file converting from
	* AWD to ENG or from ENG to AWD. Calls the appropriate function that handles these
	* conversions based on user input.
	*
	*@@ADD Get file name from user and pass into functions
	*/
	private static void convertUsingFile(){
		
		Scanner inputReader = new Scanner(System.in);
		
		System.out.println("Enter the name of the file to be translated: ");
		String fileName = inputReader.nextLine();
		
		if(!fileName.contains(".txt"))
			fileName += ".txt";
		
		//@@DEBUG
		//System.out.println("file name: " + fileName);
		
		FileReader fr = null;
		Scanner scan = null;
		
		try{
			fr = new FileReader(fileName);
			scan = new Scanner(fr);
		}catch(FileNotFoundException e){
			System.out.println("File not found");
			return;
		}
		
		System.out.println("Would you like to use a NonLetterTranslator?");
		String response = inputReader.nextLine();
		response = response.trim();
		response = response.toUpperCase();
		NonLetterTranslator nlt = new NonLetterTranslator();
		
		boolean useNLTranslator = false;
		if(response.equals("YES") || response.equals("Y") || response.equals("TRUE") || response.equals("T"))
			useNLTranslator = true;
		
		String text = "";
		
		//get text from file
		while(scan.hasNextLine()){
		
			String line = scan.nextLine();
			
			//@@DEBUG
			//System.out.println("Line: " + line);
			
			// Get random substring of line to see if it's a number
			if(line.length() != 0){
				String section = getRandomMaxIntLongSubstring(line);
			
				// Still need to handle carriage returns
				if(useNLTranslator && !isNumber(section))
					line = nlt.translate(line) + nlt.translate(System.getProperty("line.separator"));
			}else{
				// When there is a hard 
				line = nlt.translate(System.getProperty("line.separator"));
			}
				
			text += line;
		}
		
		String result = convert(text);
		
		// Get random substring of line to see if it's a number
		String piece = getRandomMaxIntLongSubstring(result);
		
		if(useNLTranslator && !isNumber(piece))
			result = nlt.translate(result);

		//@@DEBUG
		//System.out.println("result: " + result);
		
		// Ask to see if user wants to see translation
		System.out.println("Would you like to see what the input file converted to?");
		String answer = inputReader.next();
		answer = answer.trim();
		answer = answer.toUpperCase();
		
		if(answer.equals("YES") || answer.equals("Y") || answer.equals("TRUE"))
			System.out.println("Translation: " + result + "\n");
		
		// Get output file name, only .txt files accepted
		System.out.println("What would you like to call your output file?");
		answer = inputReader.next();
		answer = answer.trim();
		
		if(!answer.contains(".txt"))
			answer += ".txt";
		
		// Only .txt files accepted
		if(!answer.contains(".txt"))
			System.out.println("Please only use a .txt file");
		
		try
		{
			File file = new File(answer);
			PrintStream print = new PrintStream(file);
			
			print.println(result);
		}
      catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	/**
	* Determines if the String is a number or not
	*
	*@param text String checked to see if it is a number
	*@return boolean True if it's a number, false otherwise
	*/
	private static boolean isNumber(String text){
	
   	try{
	 		Integer.parseInt(text);
      }catch(NumberFormatException ex){
      	return false;
      }
		
      return true;
	}
	
	/**
	* Gets a random substring from the input defined to be MAX_INT_DIGITS long, which is the 
	* largest allowed number of digits that an int can be in order to guarentee that there won't
	* be an error
	*
	*@param input The String to get a substring from
	*@return String A random substring taken from the input, which is MAX_INT_DIGITS long
	*/
	private static String getRandomMaxIntLongSubstring(String input){
		Random rand = new Random();
		
		//@@DEBUG
		//System.out.println("Input: " + input);
		
		int startIndex = 0;
		int endIndex = input.length() - 1;
		if(input.length() > MAX_INT_DIGITS){
			int buffer = input.length() - MAX_INT_DIGITS;
			
			//@@DEBUG
			//System.out.println("Buffer: " + buffer);
			
			startIndex = rand.nextInt(buffer);
			endIndex = startIndex + MAX_INT_DIGITS;
		}
		
		//@@DEBUG
		//System.out.println("MAX_INT_DIGITS: " + MAX_INT_DIGITS);
		//System.out.println("Start Index: " + startIndex + ", End Index: " + endIndex);
		
		return input.substring(startIndex, endIndex);
	}
	
	/**
	* Gets the text to translate from the user, determines whether the inputted text
	* is AWD or ENG and then converts the text to either AWD or ENG using the helper
	* functions toEng(..) and toAWD(..). Prints the result of the conversion to the screen.
	*
	*/
	private static String convertUsingCommand(){

		System.out.println("Enter text to translate: ");
		
		Scanner scan = new Scanner(System.in);
		String text = scan.nextLine();
		
		return convert(text);
	}
	
	/**
	* Deciphers whether the text should be converted to ENG or to AWD and then
	* use toEng(..) or toAWD(..) to convert the text appropriately. Returns the
	* converted text. Used if converting in command or in a file.
	*
	*/
	private static String convert(String text){
	
		String result = "";
	
		text = text.toUpperCase(); //INPUT --> TO_UPPERCASE (numbers unaffected)
		text = text.trim();
		
		// If text has spaces, link words together
		if(text.contains(" ")){
			String[] words = text.split(" ");
			text = "";
			for(int i = 0; i < words.length; i++)
				text += words[i];
		}
		
		if(text.length() > 1){		//requires at least two characters to make any word
			if(text.charAt(0) <= MAX_AWD_VAL) //@@EDGE_CASE May have to make sure user inputs a number
				result = toEng(text);
			else										 //@@EDGE_CASE Similarly, may have to make sure user inputs a letter
				result = toAWD(text);
		}
		
		//System.out.println("Translation: " + result + "\n");
		return result;
	}
	
	/**
	* Converts a given segment of AWD text to ENG which is then returned
	*
	*@param text The AWD text to be translated
	*@return String The translated text, ENG
	*/
	private static String toEng(String text){
	
		// Test input: 156074853057349 = 'TEST'
		// Test input: 7415682479315683164250 = 'ERIC'
		// Test input: 51603172031248523491745864505314269371481429347034917847193720723485172495274618160241702384235852701494172384270287312824130710247138463063159568671028 = 'The quick brown fox jumps over the lazy dog'
		// Test input: 371564284917423003129948 = 'BALLOON'
		/*
			Test input: 645923103497428438761529617530423581725921478612491768312913293482534794627101632503270203484704032040542703587040478748142384130379614975486357106713592647102471682516439631254051602403707192435696231504573853931628861703278165243704650215795497248617071426304703483207349439135695160234851432605748475954707694193204132932140140347219487034842700749641327071693210132034827483482824792487887321486243507168517481740417024318479374206159216570201648461291652706158165957162817293127482473851696751820528514785468472169542704972595374029134967031629345193541824792494256319521639273073201362584561948149329921347029437032921382437104078430651387461287254956104712691724694658412393270213479645230671045170645835716824041530431504320247302394720027496178716823823049574205384352854719741027307461320678645074162824581749243194781345762956139143034107420321696179132013293495361051603047202407024703124843218437297461816320427953169563129631259561918419213023837149720732837287407423184371287104523603265184703500530705423160263548832014278412691769312931203497428827318321748427130210426159671593720238140239972134939387305316051768327913248314874283451857134079428270748217381340372458746129516032023402388724384329783427023784812304137976135854796140237073287328329494973070348145697548263182437832953479217581403286135286519324931283287214039584175865406145842738872316581453268531246916578164816328350539742834184183283792478321882534603652102738796519725169271650457023618538435704187413204970340314821748
							= 'Lorem ipsum dolor sit amet consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum'
		*/
	
		String result = "";
		
		//Stores the digital clocklike segments of the letters
		boolean[] clockBits = {false, false, false, false, false, false, false};
		
		/* Stores the values associated to each digital bit segment, used to distinguish the letters based on
			an accumulative count of each of its digital bit segment values
		*/ 
		int[] digitalSegmentValues = {1, 2, 4, 8, 16, 32, 64};
		
		int digitalSegmentTotal = 0; //used to create the key for each 
		
		int index = 0; //start at the second number in the sequence
		int length = text.length();

		String[] table = {"C", "F", "R", "E", "H", "G", "I", "Y", "T", "Z", "F", "J", "I", "B", "Z", "O", "T", "N", "Q", "D", "A", "I", "R", "R", "H", "K", "L", "Q", "G", "L", "W", "J", "A", "T", "Q", "Y", "B", "M", "L", "I", "O", "L", "L", "B", "R", "Y", "O", "R", "F", "T", "V", "R", "K", "C", "V", "N", "L", "C", "S", "F", "N", "G", "U", "O", "G", "W", "K", "W", "S", "D", "Y", "M", "J", "P", "B", "Z", "D", "L", "P", "L", "E", "C", "P", "M", "H", "K", "N", "R", "E", "P", "X", "Z", "D", "T", "D", "L", "E", "F", "Q", "R", "X", "G", "C", "Q", "P", "D", "O", "M", "V", "S", "Y", "U", "V", "F", "M", "P", "K", "M", "H", "A", "J", "E", "A", "Z", "R", "X", "W", "B"};
		
		while(index < length){
		
			//end of number sequence when a number is one less than the one before it
			do{
				try{
					int num = Character.getNumericValue(text.charAt(index)); //uses num as index of clockBits array
					
					//reverse whatever the bit was at that index
					if(clockBits[num - 1])
						clockBits[num - 1] = false;
				   else
						clockBits[num - 1] = true;
					
				}catch(java.lang.ArrayIndexOutOfBoundsException e){
					System.out.println("Invalid character read. Terminating script.");
					return result;
				}
				
				++index;
			}while(text.charAt(index) != '0' && text.charAt(index) != '8' && text.charAt(index) != '9');
			
			++index;
			
			//get id of letter
			for(int i = 0; i < clockBits.length; i++)
				if(clockBits[i])
					digitalSegmentTotal += digitalSegmentValues[i];
			
			//@@DEBUG
			//System.out.println("DST: " + digitalSegmentTotal);
			
			if(index >= 3){	//make sure that a letter has been processed
				result += table[digitalSegmentTotal];
			
				//@@DBG
				//System.out.println("Result: " + result);
			
				while(index < length && text.charAt(index - 1) == text.charAt(index)){	//if there is a repeated letter
					result += Character.toString(result.charAt(result.length() - 1)); //add letter again
					++index;
				}
			}
			
			digitalSegmentTotal = 0; //reset letter digital bit segment count
		}
		
		return result;
	}
	
	/**
	* Converts a given sentence in English to AWD code, which consists of numbers between 1 and 7, inclusive,
	* which represent the the changing segment notation of each letter, and the numbers 0, 8, and 9, which
	* are randomly assigned after each defined letter to separate each letter. The String returned contains no
	* spaces and should be able to be converted back to the same English sentence using toEng()
	*
	*@param text The English sentence to translate to AWD
	*@return String The AWD converted text, consisting of the numbers 0-9, inclusive
	*/
	private static String toAWD(String text){
		
		// Test: HEY --> 23567 1234 1235
		// Test: BALLOON --> 3715642 4 17423 0/9/8 312 0/9/8 4
		// Test: THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG --> 516 3172 3124 5234 1745 645 531426 3714
		//			142 347 34 17 471 372 7234 51724 527461 16 2417 23 4235 527 14 41723 427
		//			2 7312 2413 71 24713 463 6315 56 671 2
		
		String result = "";
		
		text = text.trim();
		int length = text.length();
		int index = 0;
		
		String[] endNums = {"0", "8", "9"};
		
		boolean[] clockBits = {false, false, false, false, false, false, false};
		
		boolean[][] alphaBits = {{true, true, true, false, true, true, true}, {true, true, true, true, true, true, true},
										 {true, false, false, true, true, true, false}, {false, true, true, true, true, false, true},
										 {true, false, false, true, true, true, true}, {true, false, false, false, true, true, true},
										 {true, false, true, true, true, true, false}, {false, true, true, false, true, true, true},
										 {false, true, true, false, false, false, false}, {true, true, true, true, true, false, false},
										 {false, false, true, false, true, true, true}, {false, false, false, true, true, true, false},
										 {true, false, true, false, true, true, true}, {true, true, true, false, true, true, false},
										 {true, true, true, true, true, true, false}, {true, true, false, false, true, true, true},
										 {true, true, true, false, false, true, true}, {true, true, false, false, true, true, false},
										 {true, false, true, true, false, true, true}, {true, false, false, false, true, true, false},
										 {false, true, true, true, true, true, false}, {false, true, true, false, true, true, false},
										 {false, true, true, true, true, true, true}, {false, false, true, false, false, true, true},
										 {false, true, true, true, false, true, true}, {true, true, false, true, true, false, true}};
		
		ArrayList<Integer> outputOptions = new ArrayList<Integer>();
		Random rand = new Random();
		
		while(index < length){

			boolean[] current = new boolean[clockBits.length];
			
			for(int i = 0; i < clockBits.length; i++){
				int indexBlah = Character.getNumericValue(text.charAt(index)) - 10;
				
				//@@DEBUG -- KEEP THIS
				String charValue = Character.toString(text.charAt(index));
				//String charValue2 = "";
				//if(index + 1 != text.length())
				//	charValue2 = Character.toString(text.charAt(index + 1));
				
				try{
					current[i] = alphaBits[Character.getNumericValue(text.charAt(index)) - 10][i];
				}catch(ArrayIndexOutOfBoundsException e){
				
					//@@DEBUG -- KEEP THIS
					System.out.println("Unrecognized character: " + charValue);
					Character whatIsThis = charValue.charAt(0);
					String name = Character.getName(whatIsThis.charValue());
					System.out.println("Unrecognized character name: " + name);
					System.out.print("You may want to consider adding this character");
					System.out.println(" to the list of recognized characters");
					
					//Character whatIsThis2 = charValue2.charAt(0);
					//String name2 = Character.getName(whatIsThis2.charValue());
					//System.out.println("Unrecognized character name2: " + name2);
					
					return "Unrecognized character found.";
				}
			}
			
			// If the next letter is the same as the previous one, add the same number followed by a decremented one
			if(Arrays.equals(current, clockBits))
			{
				result += Character.toString(result.charAt(result.length() - 1));
				++index;
				continue;
			}
			
			for(int i = 0; i < clockBits.length; i++){
				clockBits[i] = (clockBits[i] || current[i]) && !(clockBits[i] && current[i]);
				if(clockBits[i])
					outputOptions.add(i + 1);
			}
			
			clockBits = current;

			ArrayList<Integer> temp = (ArrayList<Integer>)outputOptions.clone();
			String tempResult = "";
			
			do{
				int numIndex = rand.nextInt(temp.size());
				int num = temp.get(numIndex);
				int lastResultNum;
				
				// Find last number, dependent on whether result is currently "", whether tempResult is
				// currently "", or if tempResult has a last value
				if(!tempResult.isEmpty())
					lastResultNum = Character.getNumericValue(tempResult.charAt(tempResult.length() - 1));
				else if(!result.isEmpty())
					lastResultNum = Character.getNumericValue(result.charAt(result.length() - 1));
				else
					lastResultNum = 10; //an unachievable number, as to get past the if statement no matter what
					
				tempResult += num;
				temp.remove(numIndex);
				
			}while(!temp.isEmpty());
			
			result += tempResult;
			result += endNums[rand.nextInt(3)];
			
			++index;
			outputOptions.clear();
			
		}
		
		return result;
	}
}