/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    import com.freeport.primarykey.*;
    import com.freeport.bo.*;
    
/**
 * Encapsulates data for business entity Matchup.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.Matchup", noClassnameStored = true )
 public class Matchup extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public Matchup() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the MatchupPrimaryKey
     * @return MatchupPrimaryKey   
     */
    public MatchupPrimaryKey getMatchupPrimaryKey() 
    {    
    	MatchupPrimaryKey key = new MatchupPrimaryKey(); 
		key.setMatchupId( this.matchupId );
        return( key );
    } 

    
// AIB : #getBOAccessorMethods(true)
   /**
	* Returns the name
  	* @return String	
	*/                    		    	    	    
	public String getName() 	    	   
	{
		return this.name;		
	}
	
   /**
    * Assigns the name
    * @param name	String
    */
    public void setName( String name )
    {
        this.name = name;
    }
    	
   /**
	* Returns the Games
  	* @return Set<Game>	
	*/                    		    	    	    
	public Set<Game> getGames() 	    	   
	{
		return this.games;		
	}
	
   /**
    * Assigns the games
    * @param games	Set<Game>
    */
    public void setGames( Set<Game> games )
    {
        this.games = games;
    }
    	
   /**
	* Returns the matchupId
  	* @return Long	
	*/                    		    	    	    
	public Long getMatchupId() 	    	   
	{
		return this.matchupId;		
	}
	
   /**
    * Assigns the matchupId
    * @param matchupId	Long
    */
    public void setMatchupId( Long matchupId )
    {
        this.matchupId = matchupId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	Matchup		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( Matchup object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Matchup:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.matchupId = object.getMatchupId();
        this.name = object.getName();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	Matchup		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( Matchup object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Matchup:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( true )
        this.games = object.getGames();
// ~AIB 

    }

    /**
     * Returns a string representation of the object.
     * @return String
     */
    public String toString()
    {
        StringBuilder returnString = new StringBuilder();

        returnString.append( super.toString() + ", " );     

// AIB : #getToString( false )
		returnString.append( "matchupId = " + this.matchupId + ", ");
		returnString.append( "name = " + this.name + ", ");
// ~AIB 

        return returnString.toString();
    }

	public java.util.Collection<String> attributesByNameUserIdentifiesBy()
	{
		Collection<String> names = new java.util.ArrayList<String>();
				
	return( names );
	}	
	
    public String getIdentity()
    {
		StringBuilder identity = new StringBuilder( "Matchup" );
		
			identity.append(  "::" );
		identity.append( matchupId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("Matchup");
    }	

//************************************************************************
// Object Overloads
//************************************************************************

	public boolean equals( Object object )
	{
	    Object tmpObject = null;	    
	    if (this == object) 
	        return true;
	        
		if ( object == null )
			return false;
			
	    if (!(object instanceof Matchup)) 
	        return false;
	        
		Matchup bo = (Matchup)object;
		
		return( getMatchupPrimaryKey().equals( bo.getMatchupPrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long matchupId = null;
      protected String name = null;
  @Embedded
    		  	  	      protected Set<Game> games = null;
// ~AIB

}
