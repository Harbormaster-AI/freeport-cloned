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
 * Encapsulates data for business entity League.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.League", noClassnameStored = true )
 public class League extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public League() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the LeaguePrimaryKey
     * @return LeaguePrimaryKey   
     */
    public LeaguePrimaryKey getLeaguePrimaryKey() 
    {    
    	LeaguePrimaryKey key = new LeaguePrimaryKey(); 
		key.setLeagueId( this.leagueId );
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
	* Returns the Players
  	* @return Set<Player>	
	*/                    		    	    	    
	public Set<Player> getPlayers() 	    	   
	{
		return this.players;		
	}
	
   /**
    * Assigns the players
    * @param players	Set<Player>
    */
    public void setPlayers( Set<Player> players )
    {
        this.players = players;
    }
    	
   /**
	* Returns the leagueId
  	* @return Long	
	*/                    		    	    	    
	public Long getLeagueId() 	    	   
	{
		return this.leagueId;		
	}
	
   /**
    * Assigns the leagueId
    * @param leagueId	Long
    */
    public void setLeagueId( Long leagueId )
    {
        this.leagueId = leagueId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	League		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( League object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" League:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.leagueId = object.getLeagueId();
        this.name = object.getName();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	League		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( League object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" League:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( true )
        this.players = object.getPlayers();
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
		returnString.append( "leagueId = " + this.leagueId + ", ");
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
		StringBuilder identity = new StringBuilder( "League" );
		
			identity.append(  "::" );
		identity.append( leagueId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("League");
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
			
	    if (!(object instanceof League)) 
	        return false;
	        
		League bo = (League)object;
		
		return( getLeaguePrimaryKey().equals( bo.getLeaguePrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long leagueId = null;
      protected String name = null;
  @Embedded
    		  	  	      protected Set<Player> players = null;
// ~AIB

}
