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
 * Encapsulates data for business entity Tournament.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.Tournament", noClassnameStored = true )
 public class Tournament extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public Tournament() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the TournamentPrimaryKey
     * @return TournamentPrimaryKey   
     */
    public TournamentPrimaryKey getTournamentPrimaryKey() 
    {    
    	TournamentPrimaryKey key = new TournamentPrimaryKey(); 
		key.setTournamentId( this.tournamentId );
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
	* Returns the Matchups
  	* @return Set<Matchup>	
	*/                    		    	    	    
	public Set<Matchup> getMatchups() 	    	   
	{
		return this.matchups;		
	}
	
   /**
    * Assigns the matchups
    * @param matchups	Set<Matchup>
    */
    public void setMatchups( Set<Matchup> matchups )
    {
        this.matchups = matchups;
    }
    	
   /**
	* Returns the Type
  	* @return TournamentType	
	*/                    		    	    	    
	public TournamentType getType() 	    	   
	{
		return this.type;		
	}
	
   /**
    * Assigns the type
    * @param type	TournamentType
    */
    public void setType( TournamentType type )
    {
        this.type = type;
    }
    	
   /**
	* Returns the tournamentId
  	* @return Long	
	*/                    		    	    	    
	public Long getTournamentId() 	    	   
	{
		return this.tournamentId;		
	}
	
   /**
    * Assigns the tournamentId
    * @param tournamentId	Long
    */
    public void setTournamentId( Long tournamentId )
    {
        this.tournamentId = tournamentId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	Tournament		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( Tournament object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Tournament:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.tournamentId = object.getTournamentId();
        this.name = object.getName();
        this.type = object.getType();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	Tournament		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( Tournament object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Tournament:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( true )
        this.matchups = object.getMatchups();
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
		returnString.append( "tournamentId = " + this.tournamentId + ", ");
		returnString.append( "name = " + this.name + ", ");
		returnString.append( "type = " + this.type + ", ");
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
		StringBuilder identity = new StringBuilder( "Tournament" );
		
			identity.append(  "::" );
		identity.append( tournamentId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("Tournament");
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
			
	    if (!(object instanceof Tournament)) 
	        return false;
	        
		Tournament bo = (Tournament)object;
		
		return( getTournamentPrimaryKey().equals( bo.getTournamentPrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long tournamentId = null;
      protected String name = null;
  @Embedded
    		  	  	      protected Set<Matchup> matchups = null;
      	protected TournamentType type = TournamentType.getDefaultValue();
// ~AIB

}
