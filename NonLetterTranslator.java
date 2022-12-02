/**
* NonLetterTranslator.java (Used by AWD.java and Island.java primarily) by Peter Olson
*
* Translates characters that are not from the english alphabet into english letter equivalents.
* In order to maintain a dynamic state of code, each nonletter is randomly selected from a list
* of the nonletter's equivalent english letter sequences.
*
* With the exception of ZSPC, each nonletter sequence is opened and closed by various two-letter
* non-English sequences, e.g. QV, ZX, JZ, etc. These are chosen randomly from a set as well.
*
* *********** PUBLIC GLOBAL VARIABLES **********************************************************
*
* String text									--> Text to work with, can be instantiated using
*														 NonLetterTranslator(String text) constructor
*
* *********** CONSTRUCTORS *********************************************************************
*
* NonLetterTranslator()						--> Default constructor
*
* NonLetterTranslator( String text )		 Construct an NLT with text information
*
* *********** PUBLIC METHODS *******************************************************************
*
* static void main( String[] args )				--> Runs tests on the NLT via user input to check that NLT
*														 		 is working
*
* static String translate( String input )		--> Translates the input String into alpha format or nonletter
*																 format depending on whether the the input contains OC
*																 sequences. Holds all the informational arrays regarding
*																 punctuation translations and oc info; used in tests() method
* 
* *********** PRIVATE METHODS ******************************************************************
*
* static void tests()								--> Runs tests on the NLT via user input to check that the
*																 NLT is working; used in main() method
*
* static String toAlpha( String input, ArrayList<String[]> punctuation, String[] ocLetters,
*								 String[][] openersClosers, String[] punctuationMarks, String[][] textOCS )
*															--> Converts text from something that may have punctuation to
*																 text that completely consists of alphabetic letters, where
*																 punctuation is translated into various sequences of
*																 representative letters; used in translate(..) method
*
* static String getOC( String replacement, String[] ocLetters, String[][] openersClosers )
*															--> Gets a random OC (openerCloser), used to encapsulate
*																 PAS (punctuation alpha sequences) in order to be able to
*																 translate the text back into text containing punctuation
*																 later on; used in toAlpha(..) method
*
* static boolean isDone( String result )		--> Returns true if the String consists only of alphabetic
*																 values, and returns false if it doesn't; used in translate(..)
*																 method and toAlpha(..) method
*
* static boolean contains( String text, String[] container ) 
*															--> Returns true if the text is equal to another String in the
*																 String array; used by toAlpha(..), getOC(..), containsOC(..),
*																 toNonLetter(..), and the getClosestOCCharIndex(..) methods
*
* static boolean containsOC( String text, String[] ocChars )
*															--> Determines whether the text has OC in it or not (this includes
*																 textOC); used in translate(..)
*
* static String toNonLetter( String input, ArrayList<String[]> punctuation, String[] ocLetters,
*												 String[][] openersClosers, String[] punctuationMarks, String[][] textOCS )
*															--> Translates alpha text (text consisting only of alphabetic values,
*																 some of which represent punctuation marks) to nonletter text
*																 (text with punctuation/numbers); used in translate(..) method
*
* static int getClosestOCCharIndex( String original, int startIndex, String[] ocLetters )
*															--> Finds the index of the first character of the nearest OC pair
*																 (going forward); used in toNonLetter(..) method
*
*
*
*@author Peter Olson
*@version 1.0
*
*/

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

/**
* Translates text, specifically text containing punctuation and other non-alphabetic characters,
* to text entirely represented by alphabetic characters. To do this, each punctuation is represented by
* varying series of alphabetic equivalents and contained by 'opening' and 'closing' series of combinations
* of uncommon if not nonexistant two-letter consonant combinations, such as 'XQ' or 'ZV'.
*
* In order to avoid mix-ups between text and openers and closers (OCs for short), OCs are also assigned in front
* of and after OC letters found in the text, 'J', 'Q', 'V', 'X', and 'Z' to be specific. These OCs are 'JJ', 'QQ',
* 'VV', 'XX', or 'ZZ' specifically.
*
* In order to keep the text dynamic as possible, all OCs and punctuation alphabetic equivalent expressions are
* taken from a series of similar sequences, and randomly chosen.
*
* OCs and punctuation alpha sequences (PAS) can be added to as needed in order to increase complexity
*
*/
public class NonLetterTranslator{
	
	public String text;
	
	/**
	* Runs test functions
	*
	*@param args Command line arguments (not used)
	*/
	public static void main(String[] args){
		tests();
	}
	
	/**
	* Tests functionality of NonLetterTranslator, specifically it's two main functions,
	* toAlpha(..) and toNonLetter(..)
	*
	*/
	private static void tests(){
		Scanner scan = new Scanner(System.in);
	
		System.out.println("What would you like to translate?");
		String text = "";
		text += scan.nextLine();
		
		String result = translate(text);
		
		System.out.println("Translation: " + result);
		
	}
	
	/**
	* Create a new NonLetterTranslator
	*
	*/
	public NonLetterTranslator(){}
	
	/**
	* Create a new NonLetterTranslator and set the text of the NLT
	*
	*@param text The text to be translated (presumably)
	*/
	public NonLetterTranslator(String text){
		this.text = text;
	}
	
	/**
	* Calls the correct translation method, toAlpha(..) or toNonLetter(..) and contains all the PAS
	* and OC information, which is passed on to both methods
	*
	*@param input The text to translate
	*@return String The translated text
	*/
	public static String translate(String input){
		/* TO ADD TO LIST OF READABLE CHARACTERS: ***********************************************************
			
			1) In the array called punctuationMarks below, insert the correct unicode for the character or
				the correct string literal. Be aware that string literals have to be compatible with regex
				defined characters.
			2) At the end of all of the NonLetterTranslation Arrays, create a new array, named by whatever
				character you are adding to the list of readable characters. Within the array, add a series of
				strings that will represent your punctuation. Be sure and check that these strings don't exist
				in any of the other NonLetterTranslation Arrays by using ctrl + F (find).
			3) Add NonLetterTranslation Array to punctuation ArrayList<String[]>
			
		*/
	
		// Holds all of the nonletter translation arrays
		ArrayList<String[]> punctuation = new ArrayList<String[]>();
		
		// Allows for easier oc calculations and handling
		String[] ocLetters = {"J", "V", "Q", "X", "Z"};
		
		// Opening and closing strings for text containing OC letters -- Using a 2D array in order to be able to
		// use the getOC function
		String[][] textOCS = {{"JJ"}, {"VV"}, {"QQ"}, {"XX"}, {"ZZ"}};
		
		// Opening and closing strings that are used to recognize translated punctuation when converting back
		// to the original nonletter format
		String[][] openersClosers = {{"JV", "JQ", "JX", "JZ"}, {"VJ", "VQ", "VX", "VZ"}, {"QJ", "QV", "QX", "QZ"},
											{"XJ", "XV", "XQ", "XZ"}, {"ZJ", "ZV", "ZQ", "ZX"}};
		
		// All punctuation that is checked to be translated
		// Still need to handle carriage returns
		String cr = System.getProperty("line.separator");
		String[] punctuationMarks = {" ", "!", "?", ".", ",", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "\"",
											  "<", ">", "/", "'", ":", ";", "[", "]", "{", "}", "|", "\\", "`", "~", "@", "#",
											  "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "\t", "\u201C",
											  "\u201D", "\u2018", "\u2019", "\u2026", "\u003A", "\u2013", "\u0085", cr,
											  "\u0009"};
		
		//************** NonLetter Translation Arrays ***************************************************************
		String[] space = {"spc", "ssp", "sp", "ss", "ssc", "sc", "zpc", "zsp", "zp", "zs", "zsc", "zc"};
		punctuation.add(space);
		String[] exclamationMark = {"ex", "exc", "fact", "exm", "em", "fct", "fac", "bang", "bng", "shriek", "shrek",
											 "pling", "plng", "png", "not", "emark", "emrk"};
		punctuation.add(exclamationMark);
		String[] questionMark = {"qm", "qnm", "qmrk", "qmark", "qstn"};
		punctuation.add(questionMark);
		String[] period = {"period", "perd", "prd", "dot", "dt", "pd", "pod", "prod", "fllstp", "fstp"};
		punctuation.add(period);
		String[] comma = {"comma", "cmma", "cma", "ca", "co", "brth", "breath"};
		punctuation.add(comma);
		String[] one = {"one", "uno", "oe", "uo", "single", "sngle", "sngl", "on", "point", "pt"};
		punctuation.add(one);
		String[] two = {"two", "too", "to", "dos", "couple", "cpl", "cupl", "tw", "line", "ln"};
		punctuation.add(two);
		String[] three = {"three", "tree", "thr", "tr", "triple", "trip", "trp", "trpl", "tres", "tri"};
		punctuation.add(three);
		String[] four = {"four", "for", "fr", "quad", "qd", "cuatro", "ctro", "rect", "rct"};
		punctuation.add(four);
		String[] five = {"five", "fv", "quint", "qnt", "cinco", "cnco", "cnc", "penta", "pent", "pnt"};
		punctuation.add(five);
		String[] six = {"six", "sx", "hex", "hx", "seis", "sex", "dice", "dc"};
		punctuation.add(six);
		String[] seven = {"seven", "sevn", "svn", "sept", "siete", "ste", "lucky", "lcky", "lck", "sieben", "sbn"};
		punctuation.add(seven);
		String[] eight = {"eight", "eght", "egt", "ate", "et", "ocho", "och", "oct", "octa"};
		punctuation.add(eight);
		String[] nine = {"nine", "nin", "nueve", "nve", "nn", "nv", "nov", "nova", "nein", "non"};
		punctuation.add(nine);
		String[] zero = {"zero", "zro", "zo", "nada", "zilch", "zlch", "off", "nil", "nl", "zip", "none", "nne"};
		punctuation.add(zero);
		String[] quote = {"quote", "qot", "qte", "qt", "bears", "brs", "invcma", "icma"};
		punctuation.add(quote);
		String[] lessThan = {"ralg", "lsst", "lst", "rlg", "rnom", "rcmp", "rchvrn", "rchv"};
		punctuation.add(lessThan);
		String[] moreThan = {"lalg", "grth", "grt", "llg", "lnom", "lcmp", "lchvrn", "lchv"};
		punctuation.add(moreThan);
		String[] slash = {"slash", "slsh", "slh", "rdiag", "rdg", "virgle", "vrgl", "vgl", "stroke", "strk",
								"solids", "slds", "sds"};
		punctuation.add(slash);
		String[] apostrophe = {"apost", "apst", "apo", "apt", "ap", "app", "redrum", "rdrm"};
		punctuation.add(apostrophe);
		String[] colon = {"colon", "coln", "cln", "cl", "cn", "twodot", "tdot", "tdt", "limb", "lmb", "cola"};
		punctuation.add(colon);
		String[] semicolon = {"scolon", "scoln", "scln", "scl", "scn", "stdot", "stdt"};
		punctuation.add(semicolon);
		String[] leftBracket = {"lbrckt", "lbrk", "lbk", "lbkt", "opnbrk", "obrck", "obrk", "obrkt"};
		punctuation.add(leftBracket);
		String[] rightBracket = {"rbrckt", "rbrk", "rbk", "rbkt", "clsbrk", "cbrck", "cbrk", "cbrkt"};
		punctuation.add(rightBracket);
		String[] leftCurlyBrace = {"lcurly", "lcrly", "lcly", "lcy", "opcrly", "ocrly", "ocly", "ocy", "lcb", "ocb"};
		punctuation.add(leftCurlyBrace);
		String[] rightCurlyBrace = {"rcurly", "rcrly", "rcly", "rcy", "clcrly", "ccrly", "ccly", "ccy", "rcb", "ccb"};
		punctuation.add(rightCurlyBrace);
		String[] bar = {"bar", "br", "pipe", "ppe", "pip", "pi", "wall", "wll", "wl", "polon", "pln", "sstroke",
							 "shstrk", "sstrk", "vbar", "vbr", "vline", "vlin", "vslsh", "vslh", "glidus", "glds",
							 "cthink", "cthnk", "cthk", "poley", "poly", "ply", "dline", "dlin"};
		punctuation.add(bar);
		String[] backslash = {"bslash", "bslsh", "bslh", "bsh", "bs", "bh", "hack", "hck", "hk", "whack", "whck",
									 "whk", "escape", "esc", "rslash", "rslh", "rsh", "slosh", "sloh", "bslant", "bslnt",
									 "dhill", "dhil", "bwack", "bwk", "bash", "rslant", "rslt", "rvirgl", "rvg"};
		punctuation.add(backslash);
		String[] accent = {"accent", "accnt", "acnt", "act", "acet", "dcrtc", "dcrt", "dct", "dmark", "dmrk", "dsign",
								 "dsgn", "grave", "grv"};
		punctuation.add(accent);
		String[] tilde = {"tilde", "tild", "tlde", "tld", "sqggly", "sqgly", "sqg", "flrsh", "flr"};
		punctuation.add(tilde);
		String[] atSign = {"atsign", "atsgn", "atsn", "ats", "at", "atsymb", "atsym", "comat", "cat", "arroba",
								 "arba", "asprnd", "aspr", "ampsat", "amsat", "asat", "apetl", "aptl"};
		punctuation.add(atSign);
		String[] numberSign = {"numbsn", "number", "numb", "num", "nmbr", "psign", "psgn", "hash", "hsh", "hkey",
									  "hky", "hashtag", "hshtag", "htag", "htg", "sharp", "shrp", "shp"};
		punctuation.add(numberSign);
		String[] dollarSign = {"dollar", "dllr", "dlr", "money", "mony", "mny", "cash", "csh", "dlrsgn", "dosign",
									  "dosgn", "pesosgn", "pesgn", "kesha", "ksha"};
		punctuation.add(dollarSign);
		String[] percent = {"percent", "persgn", "pcsign", "pcsgn", "perc", "prcnt", "prc", "decml", "dcm"};
		punctuation.add(percent);
		String[] caret = {"caret", "cret", "crt", "hat", "ht", "control", "contrl", "ctrl", "uparrow", "uarrow",
								"uarow", "uarw", "urw", "chevron", "chvrn", "chv", "xorsign", "xors", "xor", "xsign",
								"xsgn", "tothe", "toth", "tth", "fang", "fng", "shark", "shrk", "pointer", "pntr", "ptr",
								"wedge", "wdg", "cflex", "clfx"};
		punctuation.add(caret);
		String[] ampersand = {"ampsand", "asand", "asd", "eshand", "epshnd", "eshnd", "eshd", "epd", "and"};
		punctuation.add(ampersand);
		String[] asterisk = {"asterisk", "astrsk", "astk", "arisk", "arsk", "star", "str", "splat", "splt", "times",
									"tms"};
		punctuation.add(asterisk);
		String[] leftParenthesis = {"lparen", "lprn", "lpar", "lpr", "rlparen", "rlprn", "rlpar", "rlpr"};
		punctuation.add(leftParenthesis);
		String[] rightParenthesis = {"rparen", "rprn", "rpar", "rpr", "rrparen", "rrprn", "rrpar", "rrpr"};
		punctuation.add(rightParenthesis);
		String[] dash = {"dash", "dsh", "hyphen", "hyphn", "hphen", "hphn", "hpn", "minus", "mins", "mns", "mnus"};
		punctuation.add(dash);
		String[] underscore = {"uscore", "uscr", "ustrike", "ustrk", "lowlin", "lwline", "lwlin", "lwln", "udash",
									  "udsh", "ldash", "ldsh"};
		punctuation.add(underscore);
		String[] plus = {"plus", "pls", "plsgn", "add", "more", "mor", "mr", "concat", "cnct"};
		punctuation.add(plus);
		String[] equals = {"equals", "eqls", "eqs", "eqsign", "esign", "esgn", "equiv", "eqv", "same", "sme"};
		punctuation.add(equals);
		String[] tab = {"tab", "tb", "indent", "indnt", "ndnt", "ndt", "indt", "ind", "nd", "level", "levl",
							 "lvl", "lv"};
		punctuation.add(tab);
		String[] leftDoubleQuotationMark = {"ldblqte", "ltoqmrk", "ldqm", "ldquote", "ldqte", "ldq", "sldqm",
														"sdqm", "slantdq", "sldq"};
		punctuation.add(leftDoubleQuotationMark);
		String[] rightDoubleQuotationMark = {"rdblqte", "rtoqmrk", "rdqm", "rdquote", "rdqte", "rdq", "updqm",
														"udqm", "updq", "udq"};
		punctuation.add(rightDoubleQuotationMark);
		String[] leftSingleQuotationMark = {"lsngqte", "lsqmrk", "lsqm", "lsquote", "lsqte", "lsq", "lsapo",
														"lsa"};
		punctuation.add(leftSingleQuotationMark);
		String[] rightSingleQuotationMark = {"rsngqte", "rsqmrk", "rsqm", "rsquote", "rsqte", "rsq", "rsapo",
														"rsa"};
		punctuation.add(rightSingleQuotationMark);
		String[] horizontalEllipsis = {"helps", "hlps", "help", "hell", "hll", "hlp", "hzelps", "hzlps", "hzntle",
												 "horell", "hrll"};
		punctuation.add(horizontalEllipsis);
		String[] unicodeColonAKAMicrosoftWordColon = {"mscolon", "mscoln", "mscln", "mscl", "mscn", "mstwodot",
																	 "mstdot", "mstdt", "mslimb", "mslmb", "mscola"};
		punctuation.add(unicodeColonAKAMicrosoftWordColon);
		String[] enDash = {"ndash", "endash", "endsh", "ndsh", "indash", "indsh", "end", "enda"};
		punctuation.add(enDash);
		String[] nextLine = {"nextline", "nxtline", "nxline", "nxtln", "nxln", "nln", "nline", "nlin", "nextl",
									"newline", "nwline", "newlin", "newl", "nwl", "enter", "entr", "ntr", "return",
									"rtrn", "slashn", "slshn"};
		punctuation.add(nextLine);
		String[] carriageReturn = {"cnextline", "cnxtline", "cnxline", "cnxtln", "cnxln", "cnln", "cnline",
											"cnlin", "cnextl", "cnewline", "cnwline", "cnewlin", "cnewl", "cnwl",
											"center", "centr", "cntr", "creturn", "crtrn", "cslashn", "cslshn"};
		punctuation.add(carriageReturn);
		String[] characterTabulation = {"chartab", "chrtab", "chrtb", "ctbltn", "chtab", "chtbln", "slasht",
												  "slsht", "slt", "stab"};
		punctuation.add(characterTabulation);
		//************************************************************************************************************
		
		String result = "";

		if(!isDone(input) || !containsOC(input, ocLetters))
			result = toAlpha(input, punctuation, ocLetters, openersClosers, punctuationMarks, textOCS);
		else
			result = toNonLetter(input, punctuation, ocLetters, openersClosers, punctuationMarks, textOCS);
		
		return result;
	}
	
	/**
	* Translates text to a String consisting only of alphabetic values, that is [A-Z]. In order to
	* be able to translate the String back into a String with the original punctuation marks, punctuation
	* alpha sequences (PAS), recognizable alphabetic equivalents of the punctuation, are replaces with the
	* punctuation and openers and closers (OCs) are used to sandwich the PAS so that when translating the
	* String back to punctuation format, so the PAS can be recognized by the program and interpreted back
	* into punctuation.
	*
	* Different types of OCs are also used to sandwich OC alpha values found in the text, so that the OCs can
	* be distinguished from the text values.
	*
	*@param input The text to be translated
	*@return String The translated text, now only containing alphabetic values (also all caps)
	*
	*/
	private static String toAlpha(String input, ArrayList<String[]> punctuation, String[] ocLetters,
											String[][] openersClosers, String[] punctuationMarks, String[][] textOCS){
		
		input = input.toUpperCase();
		
		if(input.length() == 0)
			return "";
		
		String inputRevised = "";
		int k = 0;
		// Replace text OC letters with openers and closers
		while(k < input.length()){
		
			String currentLetter = Character.toString(input.charAt(k));
			
			// If the current character is in the ocLetters array, add OC
			if(contains(currentLetter, ocLetters)){
			
				String opener = getOC(currentLetter, ocLetters, textOCS);
				String closer = getOC(currentLetter, ocLetters, textOCS);
				String replacement = opener + currentLetter + closer;
				
				inputRevised += replacement;
			}else{
				inputRevised += currentLetter;
			}
			++k;
		}
		
		// Since String is immutable, this is unnecessary. Simply done for the name change
		String result = inputRevised;
		
		Random rand = new Random();		// Used to randomly take translation equivalences and oc
		int i = 0;								// Keeps track of the punctuation arrays in the ArrayList
		String oldText = result;			// Used to show when replaceFirst(..) can't replace any more

		// Go through punctuation translation arrays replacing all punctuation with its equivalent letter sequence
		// and setting the resulting text as you go along
		while(i < punctuation.size()){
			
			oldText = result;
			if(result.contains(punctuationMarks[i]))
				oldText = "";		// Used as a reference-- once oldText.equals(result), there are no more replacements
			while(!result.equals(oldText)){ //^^
				oldText = result;
			
				String replacement = "";
				// Randomly get punctuation letter sequence
				replacement += punctuation.get(i)[rand.nextInt(punctuation.get(i).length)];
				replacement = replacement.toUpperCase();
				
				String opener = getOC(replacement, ocLetters, openersClosers);
				String closer = getOC(replacement, ocLetters, openersClosers);
				
				// Add opener and closer String to the beginning and end of the replacement punctuation letter sequence
				replacement = opener + replacement + closer;
				
				// Replace old punctuation with letter sequence equivalent and set to result
				String fixMeta = "";
				// Some characters are recognized regex formatting characters. Add '\\' to fix this
				if(punctuationMarks[i].equals("?") || punctuationMarks[i].equals("$") ||
				   punctuationMarks[i].equals("^") || punctuationMarks[i].equals("*") ||
					punctuationMarks[i].equals(".") || punctuationMarks[i].equals("[") ||
					punctuationMarks[i].equals("]") || punctuationMarks[i].equals("{") ||
					punctuationMarks[i].equals("}") || punctuationMarks[i].equals("\\") ||
					punctuationMarks[i].equals("|") || punctuationMarks[i].equals("(") ||
					punctuationMarks[i].equals(")") || punctuationMarks[i].equals("+"))
					fixMeta += "\\";
				String fixedPunc = fixMeta + punctuationMarks[i];
				result = oldText.replaceFirst(fixedPunc, replacement);
				
				//@@DEBUG
				//System.out.println("result: " + result);
				//System.out.println("oldText: " + oldText);
				
				// Check if the result is all alphabetic letters. If it is, exit loops to stop checking
				if(isDone(result)){
					oldText = result;
					i = punctuation.size();
				}
				
			}
			++i;
		}
		
		return result;
	}
	
	/**
	* Finds a random, plausible OC that doesn't conflict with the inner PAS values.
	*
	*@param replacement The PAS String, used to see if it has any OC alpha values
	*@param ocLetters Used as a reference to see which alpha letters make up the OC values
	*@param openersClosers Contains all the possible combinations of OC
	*@return String A two-letter string, consisting of two different OC alpha values
	*/
	private static String getOC(String replacement, String[] ocLetters, String[][] openersClosers){
		Random rand = new Random();
	
		// Keeps track of letters in the letter sequence that overlap with oc letters
		ArrayList<String> hit = new ArrayList<String>();
		int j = 0;
		// Go through letter sequence in replacement text, adding overlapping letters to the hit list
		while(j < ocLetters.length){
			if(replacement.contains(ocLetters[j]))
				hit.add(ocLetters[j]);
			++j;
		}
		int ocLength = openersClosers.length;
		int ocLetterArrayIndex = rand.nextInt(ocLength);	// Get random first oc letter
		int ocLetter = rand.nextInt(ocLength - 1);					// Get random second oc letter
		
		// For textOCS array, use ocLetter = 0 since each array has only one String in it
		if(openersClosers[0].length == 1)
			ocLetter = 0;
		
		// If the letter sequence contains all the oc letters (which should be impossible), skip oc check
		if(hit.size() != ocLength){
			while(hit.contains(ocLetters[ocLetterArrayIndex]))
				ocLetterArrayIndex = rand.nextInt(ocLength);	// Get new random first letter
			while(hit.contains(Character.toString(openersClosers[ocLetterArrayIndex][ocLetter].charAt(1))))
				ocLetter = rand.nextInt(ocLength - 1);				// Get new random second letter
		}
		
		// Get oc String
		String oc = openersClosers[ocLetterArrayIndex][ocLetter];
		return oc;
	}
	
	/**
	* Checks if the String is made up of all alphabetic values. If so, returns true and returns false
	* otherwise.
	*
	*@param result The text to be checked
	*@return boolean True if String is completely alphabetic, false otherwise
	*/
	private static boolean isDone(String result){
		char[] chArr = result.toCharArray();
		boolean done = true;
		for(int i = 0; i < chArr.length; i++)
			if(!Character.isAlphabetic(chArr[i]) /*|| Character.isWhitespace(chArr[i]) || chArr[i] == ' '*/)
				return false;
		return true;
	}
	
	/**
	* Checks if a String is in an array of Strings.
	* If it is found to be one of the elements in the String, return true,
	* otherwise return false
	*
	*@param text The text to check and see if it is in the array of Strings
	*@param container An array of Strings to be checked
	*@return boolean True if the text is in the array, false otherwise
	*/
	private static boolean contains(String text, String[] container){
		boolean hasText = false;
		for(int i = 0; i < container.length; i++){
			if(container[i].equals(text))
				hasText = true;
		}
		return hasText;
	}
	
	/**
	* Checks if the text contains an OC or textOC within it. If it does, return true,
	* otherwise return false
	*
	*@param text The text to check if it contains OC
	*@param ocChars The array of OC letters. This is used to piece together all possible OC to check against
	*@return boolean True if the text contains OC, false otherwise
	*/
	private static boolean containsOC(String text, String[] ocChars){
		boolean hasText = false;
		int i = 0;
		while(i < text.length()){
			if(contains(Character.toString(text.charAt(i)), ocChars) && i != text.length() - 1 &&
				contains(Character.toString(text.charAt(i + 1)), ocChars))
				hasText = true;
			++i;
		}
		return hasText;
	}
	
	/**
	* Translates an alpha-based text sequence back to a nonLetter punctuation format.
	* In order to do this, this method goes through the text stopping at OCs and assessing how to
	* correctly get the embedded text between OC values.
	*
	*@param input The text to be translated
	*@param punctuation Holds all of the punctuation array String sets
	*@param ocLetters Holds which characters are OC characters
	*@param openersClosers Holds all of the valid OC Strings
	*@param punctuationMarks Holds the info on all of the recognized non-letter characters
	*@param textOCS Holds all the valid text OC strings
	*@return String The translated text, which now contains non-letter characters and no OC pairs
	*/
	private static String toNonLetter(String input, ArrayList<String[]> punctuation, String[] ocLetters,
												 String[][] openersClosers, String[] punctuationMarks, String[][] textOCS){
		
		String original = input;
		int i = 0;
		String result = "";
		
		if(original.length() == 0)
			return result;
		
		//@@DEBUG
		//System.out.println("Input: " + input);
		
		/* Go through text stopping at OC characters and assesing whether they belong to an PAS OC or
		// a text OC. When text OC is found, add the index character to the result. When PAS OC is found,
		// find the correct punctuation in the ArrayList punctuation and add it to the result.
		// If a character is not an OC character, simply add it to the result
		*/
		while(i < original.length()){
		
			// Current index character
			String currentChar = Character.toString(original.charAt(i));
			
			// If currentChar is an OC character
			if(contains(currentChar, ocLetters)){
			
				// Character following currentChar
				String nextChar = Character.toString(original.charAt(i + 1));
				
				// If currentChar is equal to nextChar, a text OC has been found
				if(currentChar.equals(nextChar)){
				
					// Get text oc character
					String thirdChar = Character.toString(original.charAt(i + 2));
					result += thirdChar;
					/* If the text being assessed looks like this: "...QQJZZ...", where the first 'Q'
					// is the starting character and '...' represents more characters in the string,
					// increasing i by 4 puts the index at the second 'Z'. At the end of the while loop
					// i is increased 1 more to put the index past the text OC and ready to assess the next
					// character
					*/
					i += 4;
					
				}else{ // When the OC found is a PAS OC
					
					//String oc = currentChar + nextChar; //@@ Not needed, I guess
					
					int startIndex = i + 2; // Find index of first char in PAS
					// Find index of first char in next valid OC pair
					int endIndex = getClosestOCCharIndex(original, startIndex, ocLetters);
					
					//@@DEBUG
					if(endIndex < 0 || startIndex < 0)
						System.out.println("Original: " + original + ", Line length: " + original.length());
					
					// Find PAS using substring and indices just found
					String PAS = original.substring(startIndex, endIndex);
					PAS = PAS.toLowerCase();
					
					// Find index of array where PAS is contained and use the index to get the right punctuation
					// in punctuationMark array and add it to the result
					for(int m = 0; m < punctuation.size(); m++)
						if(contains(PAS, punctuation.get(m)))
							result += punctuationMarks[m];
					
					// Set i to be equal to second letter of closing OC pair
					// Note that i is incremented at the end of loop so that i is then pointing to the next character
					// in the text
					i = startIndex + PAS.length() + 1;
					
				}
			}else{
				result += currentChar;
			}
			
			++i;
		}
		
		return result;
	}
	
	/**
	* Find the index of the first character in the closest OC pairing starting at the
	* given index. For example, if original = "...ZSCXQ...", where startIndex is the index of 'Z'
	* and '...' represents additional characters, this function would return (startIndex + 3), the
	* index of 'X', since it is the first char in the OC pair 'XQ', which is a recognized pairing.
	*
	* If there is no OC starting at the startIndex, return -1
	*
	*@param original The text to search, starting at startIndex
	*@param startIndex The index of the text to start from
	*@param ocLetters The recognized OC characters to use as reference
	*@return int The index of the first character in the closest recognized OC pairing
	*/
	private static int getClosestOCCharIndex(String original, int startIndex, String[] ocLetters){
		int i = startIndex;
		int index = -1;	// If index is never changed, returns -1 indicating that no OC was found
		while(i < original.length()){
			String currentChar = Character.toString(original.charAt(i));
			if(contains(currentChar, ocLetters)){ // If current character is an OC character
				String nextChar = Character.toString(original.charAt(i + 1));
				if(contains(nextChar, ocLetters)){ // If char after currentChar is an OC char
					index = i; 					// Save index
					i = original.length();	// End loop
				}
			}
			++i;
		}
		return index;
	}
	
}