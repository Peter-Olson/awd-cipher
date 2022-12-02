/**
* HashTable.java (AWD 1.0) by Peter Olson
*
* **********PRIVATE VARIABLES**********
* HashEntry[] entries		--> Stores the hash table entries in an array of HashEntrys
* int totalEntries			--> Number of occupied cells in the hash table
*
* CONSTRUCTOR: Uses totalEntries (num of occupied cells in hash table) to define the size of the hash table
*
* **********PUBLIC METHODS*************
* Object find( Object elem )		--> Returns the element if found, returns null otherwise:
*												 used in void insert( Object elem ) and void delete( Object elem )
*												 @@CHECK active status is irrelevant
* void insert( Object elem )		--> Uses quadratic probing to insert an element into the hash table;
*												 @@CHECK Doesn't write over inactive elements
* void delete( Object elem )		--> If element is found and is active, sets the HashEntry's active
*												 status to 'inactive'
* void printTable()					--> Prints out the array of HashEntrys, including index number and active status
* int elementCount()					--> Returns the number of active elements in the hash table
* boolean isEmpty()					--> Returns true if there are no active HashEntrys
* void makeEmpty()					--> Makes the hash table have no active/inactive HashEntrys
* void outputData()					--> Prints out the array of HashEntrys using the iterator, @@CHECK all on one line 
*
* **********PRIVATE METHODS*************
* boolean checkIfPrime( int number )	--> Return true if the number if prime, false otherwise;
*														 used in void rehash() and the constructor
* int numOccupied()							--> Returns the number of occupied HashEntrys in the hash table;
*														 used by void insert( Object elem )
* void rehash()								--> Rehashes the hash table by increasing the size to be equal to
*														 the closest prime number to the current number of elements
* int quadraticHash( Object elem )		--> Uses quadratic probing to find the next available index to
*														 place an element; used in void insert( Object elem )
* int hash( Object elem )					--> Uses hashCode() to find the hash value of the element, which is
*														 used to find the index of that HashEntry; used in
*														 Object find( Object elem ), void insert( Object elem ),
*														 int quadraticHash( Object elem ), void delete( Object elem )
*
*@author Peter Olson
*@version 1.0
*/

//import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable 
{
   //PUBLIC VARIABLES
   //private LinkedList[] arr; 
   private HashEntry[] entries;
   private int totalEntries; 
   
   /**
	* HashEntry Private Class
	*
   * Defines each entry in the hash table as containg an element
   * and a boolean that tells whether the element is active or not
   *
	* **********PUBLIC VARIABLES**********
	* Object element			--> Value of HashEntry (int)
	* boolean isActive		--> True if active, false otherwise; turned inactive when deleted @@CHECK
	*
	* CONSTRUCTOR: Initiates both public variables
	*
	* **********PRIVATE METHODS********** @@CHECK
	* Object getElement()			--> Returns the HashEntry's element
	* boolean getActiveStatus()	--> Returns the HashEntry's isActive status
	*
   */
   private class HashEntry
   {
      public Object element;
      public boolean isActive;
      
      /**
      * Creates a new hash entry that assigns the
      * hash entry's element and it's active status
      *
      *@param element The value that is contained in the HashEntry
      *@param isActive True if active, false otherwise
      */
      public HashEntry(Object element, boolean isActive)
      {
         this.element = element;
         this.isActive = isActive;
      }
      
      /**
      * Gets the HashEntry's element
      *
      *@return Object The element of the HashEntry
      */
      private Object getElement()
      {
         return element;
      }
      
      /**
      * Gets the HashEntry's active status
      *
      *@return boolean True if active, false otherwise
      */
      private boolean getActiveStatus()
      {
         return isActive;
      }
      
   }
   
   /**
   * Creates an array of empty LinkedLists<T>
   *
   *@param totalEntries The number of elements in the array
   */
   public HashTable(int totalEntries)
   {
      
      int size = totalEntries * 2;///* * 2*/;
      
		///*
		if(size % 2 == 0) //make size odd if even
		{
			size += 1;
		}
		
      while(!checkIfPrime(size))
      {
         size += 2; //assuming size starts off odd, don't need to test even numbers as they will be
						  //divisible by 2
      }
      //*/
      
      entries = new HashEntry[size];
      
   }
   
   /**
   * Returns true if the number is prime and false otherwise
   *
   *@param number The number to check if prime
   */
   private boolean checkIfPrime(int number)
   {
      //if number is even, return false since 2 divides evenly
      if(number % 2 == 0)
      {
         return false;
      }
      
      /*
         Start at 3 and check every odd integer up to the square root 
         of the number to see if it divides evenly
      */
      for(int i = 3; i * i <= number; i += 2)
      {
         if(number % i == 0)
         {
            return false;
         }
      }
      
      return true;
   }
   
   /**
	* HashIterator Private Class 
	*
	* Creates an iterator that uses a cursor to traverse through the hash table and skips
   * over values that are inactive or that don't exist at the current index. Contains
   * hasNext(), next(), and remove(), but does not support remove()
	*
	* **********PUBLIC VARIABLES**********
	* int cursor				--> Keeps track of where the iterator is pointing to
	*
	* CONSTRUCTOR: sets the cursor to be equal to the index
	*					of the first non-null, active HashEntry in the hash table
	*
	* **********PUBLIC METHODS**********
	* boolean hasNext()		--> Tells whether there are more unvisited elements or not (for the cursor)
	* Object next()			--> Sets the cursor to the next index of a valid HashEntry and returns the element
	*									 of the next HashEntry
	* void remove()			--> Not supported
	*
	* ERRORS: Throws UnsupportedOperationException() if remove() is called
	*
	*/
	private class HashIterator implements Iterator<Object>
	{
		public int cursor = 0;
		
		/**
		* Instantiates cursor to be equal to the first active element in the HashTable
		*
		*/
		public HashIterator()
		{
			for(int k = 0; k < entries.length; k++)
			{
				if(entries[k] != null && entries[k].isActive)
				{
					cursor = k;
					break;
				}
			}
			//debug
			//System.out.print("First Index: " + i + " ");
		}
		
		/**
		 * Determines whether there is another unvisited active element or not
		 *
		 *@return boolean True if there is another element, false otherwise
		 */
       public boolean hasNext()
       {
		 	 if(cursor < entries.length)
          {
            return true;
          }
			 
          return false;
       }
       
		 /**
		 * Returns the next element in the Hash Table
		 *
		 *@return Object The value of the next active element in the Hash Table
		 */
       public Object next()
       {
          if(!hasNext())
          {
             throw new NoSuchElementException();
          }
			 
          //Skip over empty spots or inactive HashEntrys
          while(entries[cursor] == null || !entries[cursor].isActive)
          {
             cursor += 1;
				 
				 if(cursor >= entries.length)
				 {
				 	 return null;
				 }
          }
          
			 Object data = entries[cursor].element; //get data of current location (which is next element)

			 /* 
             Note that according to the instructions, this is preemptive. The cursor appears to
             be one ahead of where we think
          */
          
          cursor += 1; //move cursor up
          
          return data;
       }
      
		 /**
		 * Does not support the remove() function that comes
		 * with the iterator, throws an exception if called
		 *
		 */
       public void remove() 
       {
          throw new UnsupportedOperationException();
       }
		 
	} //end HashIterator class
   
   /**
   * Finds whether the element is in the HashTable or not
   *
   *@param elem The element to find in the HashTable
   *@return Object The element of the HashEntry; null if not found
   */
   public Object find(Object elem)
   {
      int index = hash(elem);
		
      if(entries[index] != null && entries[index].element.equals(elem) && entries[index].isActive)
      {
         return elem;
      }
      
      return null;
   }
   
	/**
	* Finds whether the ENGBit specified by the id is in the HashTable or not. If it is in
	* the HashTable, return the ENGBit's String letter, otherwise return null
	*
	*@param id The id used to find the ENGBit
	*@return String The ENGBit's String letter
	*/
	public String findLetter(int id)
	{
		int index = hashID(id);
		
		if(entries[index] != null && ((ENGBit)entries[index].element).getID() == id)
		{
			return ((ENGBit)entries[index].element).getLetter(); //get the ENGBit String letter and return it
		}
		
		return null;
	}
	
   /**
   * Inserts the element into the HashEntry[] and then
   * uses hash(Object elem) to put the element into
   * the correct spot in the hash table
   *
   *@param elem The element to insert into the HashTable
   */
   public void insert(Object elem)
   {
      Object data = find(elem);
		
      //if elem is already in hash table, do nothing
      if(data == null)
      {
			int index;
			
			HashEntry test = entries[hash(elem)];
			
         if(test == null || (test != null && test.isActive))
			{
         	index = quadraticHash(elem);
			}
			else //when HashEntry is inactive
			{
				index = hash(elem);
			}
         
         //insert element if space doesn't have an element
         if(entries[index] == null || !entries[hash(elem)].isActive)
         {
            entries[index] = new HashEntry(elem, true); //true == active
         }
      
			//rehash when array is half-way filled with HashEntrys
         if(numOccupied() >= entries.length/2)
			{
				//rehash();
			}
      }
   }
	
	/**
	* Returns the number of HashEntries in the hash table (active status doesn't matter)
	*
	*@return int The number of occupied HashEntrys
	*/
	private int numOccupied()
	{
		int count = 0;
		
		for(int i = 0; i < entries.length; i++)
		{
         if(entries[i] != null)
         {
            count++;
         }
		}
		
		return count;
	}
	
	/**
	* Resizes the HashEntry[] to be the size of the next largest prime number
	*
	*/
	private void rehash()
	{
		
      int size = entries.length * 2;
      
		if(size % 2 == 0) //make size odd
		{
			size += 1;
		}
		
		//set size to next highest prime number
      while(!checkIfPrime(size))
      {
         size += 2; //check odd numbers
      }
      
		HashEntry[] tempEntries = new HashEntry[size];
		
		//move old values into temp array
		for(int i = 0; i < entries.length; i++)
		{
			if(entries[i] != null)
			{
				tempEntries[i] = entries[i];
			}
		}
      
		entries = tempEntries;
	}
   
   /**
   * Uses a quadratic hash to find the index of the next open spot for
   * the element to be placed
   *
   *@param elem The element to be 
   */
   private int quadraticHash(Object elem)
   {
      int index = hash(elem); //find where elem is
      int quadraticVar = 1;
      
      //keep looking until find the index where there is no entry
      while(entries[index] != null)
      {
         index += (quadraticVar * quadraticVar) % entries.length;
         quadraticVar += 1;
         
         //set index back by the length of the hash table if the index has exceeded the length
         index %= entries.length;
      }
      
      return index;
   }
   
   /**
   * Finds the hash value of the element and returns
   *
   *@param elem The element to get the hash value of
   *@return int The hash value of the element
   */
   private int hash(Object elem)
   {  
		return Math.abs(elem.hashCode()) % entries.length;
   }
   
	/**
	* Find the hash value of the id and returns
	*
	*@param id The id to get the hash value of
	*@return int The hash value of the id
	*/
	private int hashID(int id)
	{
		return Math.abs(((Integer)id).hashCode()) % entries.length;
	}
	
   /**
   * Removes the element specified by using hash to find its
   * location and then removing it from the HashEntry[]
   *
   *@param elem The element to remove from the array
   */
   public void delete(Object elem)
   {
		Object found = find(elem);
	
      if(found != null)
      {
         int index = hash(elem);
			
			if(entries[index].isActive)
			{
	         entries[index].isActive = false;
			}
      }
   }
     
   /**
   * Print out the HashTable elements
   *
   */
   public void printTable()
   {
      for(int i = 0; i < entries.length; i++)
      {
         if(entries[i] != null) //if there is an entry at the index
         {
            String active = "active";
         
            if(!entries[i].isActive)
            {
               active = "in" + active;
            }
         
            System.out.println("[" + i + "]: " + entries[i].element + ", " + active);
         }
         else //when no entry
         {
            System.out.println("[" + i + "]: empty");
         }
      }
   }
   	
	/**
	* Returns the number of active elements in the HashTable O(n)
	*
	*@return int The number of active elements in the HashTable
	*/
	public int elementCount()
	{
		int count = 0;
		
		for(int i = 0; i < entries.length; i++)
		{
         if(entries[i] != null && entries[i].isActive)
         {
            count++;
         }
		}
		
		return count;
	}
   
   /**
   * Finds whether the HashTable is empty or not
   *
   *@return boolean True if empty, false otherwise
   */
   public boolean isEmpty()
   {
   
      //go through hash table to see if there are any active entries
      for(int i = 0; i < entries.length; i++)
      {
         if(entries[i] != null)
         {
            if(entries[i].isActive)
            {
               return false; //return false when an active entry is found
            }
         }
      }
      
      return true; //if none found, hash table is empty
   }
   
	/**
	* Makes the HashTable empty 
	*
	*/
	public void makeEmpty()
	{
      for(int i = 0; i < entries.length; i++)
      {
         entries[i] = null;
      }
	}
	
	/**
	* Uses an iterator to traverse through the hash table and
	* print out all the values on separate lines
	*
	*/
	public void outputData()
	{
		HashIterator iterator = new HashIterator();
		String output = "";
      
      while(iterator.hasNext())
      {
			Object value = iterator.next();
			
			if(value != null)
			{
         	output = value + ", active"; //all must be active
            System.out.println(output);
			}
      }
      
      //System.out.println(output);
      
	} // end output
	
} //end MyHashTable class