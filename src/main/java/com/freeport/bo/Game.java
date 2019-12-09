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
 * Encapsulates data for business entity Game.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.Game", noClassnameStored = true )
 public class Game extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public Game() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the GamePrimaryKey
     * @return GamePrimaryKey   
     */
    public GamePrimaryKey getGamePrimaryKey() 
    {    
    	GamePrimaryKey key = new GamePrimaryKey(); 
		key.setGameId( this.gameId );
        return( key );
    } 

    
// AIB : #getBOAccessorMethods(true)
   /**
	* Returns the frames
  	* @return java.lang.Integer	
	*/                    		    	    	    
	public java.lang.Integer getFrames() 	    	   
	{
		return this.frames;		
	}
	
   /**
    * Assigns the frames
    * @param frames	java.lang.Integer
    */
    public void setFrames( java.lang.Integer frames )
    {
        this.frames = frames;
    }
    	
   /**
	* Returns the Player
  	* @return Player	
	*/                    		    	    	    
	public Player getPlayer() 	    	   
	{
		return this.player;		
	}
	
   /**
    * Assigns the player
    * @param player	Player
    */
    public void setPlayer( Player player )
    {
        this.player = player;
    }
    	
   /**
	* Returns the gameId
  	* @return Long	
	*/                    		    	    	    
	public Long getGameId() 	    	   
	{
		return this.gameId;		
	}
	
   /**
    * Assigns the gameId
    * @param gameId	Long
    */
    public void setGameId( Long gameId )
    {
        this.gameId = gameId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	Game		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( Game object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Game:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.gameId = object.getGameId();
        this.frames = object.getFrames();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	Game		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( Game object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Game:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( true )
        this.player = object.getPlayer();
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
		returnString.append( "gameId = " + this.gameId + ", ");
		returnString.append( "frames = " + this.frames + ", ");
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
		StringBuilder identity = new StringBuilder( "Game" );
		
			identity.append(  "::" );
		identity.append( gameId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("Game");
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
			
	    if (!(object instanceof Game)) 
	        return false;
	        
		Game bo = (Game)object;
		
		return( getGamePrimaryKey().equals( bo.getGamePrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long gameId = null;
      	protected java.lang.Integer frames = new java.lang.Integer("0");
      @Reference
         		  	  	      protected Player player = null;
// ~AIB

}
