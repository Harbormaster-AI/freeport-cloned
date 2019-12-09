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
 * Encapsulates data for business entity Player.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.Player", noClassnameStored = true )
 public class Player extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public Player() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the PlayerPrimaryKey
     * @return PlayerPrimaryKey   
     */
    public PlayerPrimaryKey getPlayerPrimaryKey() 
    {    
    	PlayerPrimaryKey key = new PlayerPrimaryKey(); 
		key.setPlayerId( this.playerId );
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
	* Returns the dateOfBirth
  	* @return java.util.Date	
	*/                    		    	    	    
	public java.util.Date getDateOfBirth() 	    	   
	{
		return this.dateOfBirth;		
	}
	
   /**
    * Assigns the dateOfBirth
    * @param dateOfBirth	java.util.Date
    */
    public void setDateOfBirth( java.util.Date dateOfBirth )
    {
        this.dateOfBirth = dateOfBirth;
    }
    	
   /**
	* Returns the height
  	* @return java.lang.Double	
	*/                    		    	    	    
	public java.lang.Double getHeight() 	    	   
	{
		return this.height;		
	}
	
   /**
    * Assigns the height
    * @param height	java.lang.Double
    */
    public void setHeight( java.lang.Double height )
    {
        this.height = height;
    }
    	
   /**
	* Returns the isProfessional
  	* @return java.lang.Boolean	
	*/                    		    	    	    
	public java.lang.Boolean getIsProfessional() 	    	   
	{
		return this.isProfessional;		
	}
	
   /**
    * Assigns the isProfessional
    * @param isProfessional	java.lang.Boolean
    */
    public void setIsProfessional( java.lang.Boolean isProfessional )
    {
        this.isProfessional = isProfessional;
    }
    	
   /**
	* Returns the playerId
  	* @return Long	
	*/                    		    	    	    
	public Long getPlayerId() 	    	   
	{
		return this.playerId;		
	}
	
   /**
    * Assigns the playerId
    * @param playerId	Long
    */
    public void setPlayerId( Long playerId )
    {
        this.playerId = playerId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	Player		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( Player object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Player:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.playerId = object.getPlayerId();
        this.name = object.getName();
        this.dateOfBirth = object.getDateOfBirth();
        this.height = object.getHeight();
        this.isProfessional = object.getIsProfessional();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	Player		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( Player object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Player:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( true )
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
		returnString.append( "playerId = " + this.playerId + ", ");
		returnString.append( "name = " + this.name + ", ");
		returnString.append( "dateOfBirth = " + this.dateOfBirth + ", ");
		returnString.append( "height = " + this.height + ", ");
		returnString.append( "isProfessional = " + this.isProfessional + ", ");
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
		StringBuilder identity = new StringBuilder( "Player" );
		
			identity.append(  "::" );
		identity.append( playerId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("Player");
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
			
	    if (!(object instanceof Player)) 
	        return false;
	        
		Player bo = (Player)object;
		
		return( getPlayerPrimaryKey().equals( bo.getPlayerPrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long playerId = null;
      protected String name = null;
      protected java.util.Date dateOfBirth = null;
      	protected java.lang.Double height = new java.lang.Double("0.0");
      	protected java.lang.Boolean isProfessional = new java.lang.Boolean("false");
// ~AIB

}
