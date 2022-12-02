/**
* ENGBit.java (AWD 1.0) by Peter Olson
*
* Holds information regarding ENG digital bit values
*
* *********** PRIVATE GLOBAL VARIABLES ***********************************************
* 
* int id				--> Used to give the ENGBit a unique value
* 
* String letter	--> Holds a String consisting of one alphabetic letter
*
* *********** CONSTRUCTORS ***********************************************************
*
* ENGBit( String letter, int id )			--> Creates a new ENGBit with a unique id and an alphabetic letter
*
* *********** PUBLIC METHODS *********************************************************
*
* boolean equals( Object other )				--> Overrides equals(..) method
*
* String toString()								--> Overrides toString() method
*
* int hashCode()									--> Overrides hashCode() method
*
* String getLetter()								--> Gets the letter of this object
*
* int getID()										--> Gets the id of this object
*
*
*
*@author Peter Olson
*@version 1.0
*/

public class ENGBit
{
   private int id; //can assume larger than 0; identifies only one student
   private String letter; //has no spaces
   
   /**
   * Creates an ENGBit with an id and a letter
   *
	*@param letter The ENG letter the id corresponds to
   *@param id The id of the letter, which represents the accumulative digital bit scores of the letter
   */
   public ENGBit(String letter, int id)
   {
      this.letter = letter;
		this.id = id;
   }
   
   /**
   * Returns true if other's id is equal to this id
   *
   *@param other The other ENGBit that is being compared to this one
   *@return boolean True if both ids are the same and both letters are the same, false otherwise
   */
   public boolean equals(Object other)
   {
      return ((ENGBit)other).letter.equals(letter) && ((ENGBit)other).id == id;  
   }
   
   /**
   * Produces and returns a String representation of the ENGBit's information
   *
   *@return String The information of the ENGBit, i.e. "A 297"
   */
   public String toString()
   {
      return letter + " " + id;
   }
   
   /**
   * Returns the hash code of the id
   *
   *@return int The hash of the id
   */
   public int hashCode()
   {
      return ((Integer)id).hashCode();
   }
	
	/**
	* Gets letter value
	*
	*@return String The letter value
	*/
	public String getLetter()
	{
		return letter;
	}
	
	/**
	* Gets id
	*
	*@return int The id value
	*/
	public int getID()
	{
		return id;
	}
}