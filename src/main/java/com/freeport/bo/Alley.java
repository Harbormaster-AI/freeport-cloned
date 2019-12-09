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
 * Encapsulates data for business entity Alley.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.Alley", noClassnameStored = true )
 public class Alley extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public Alley() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the AlleyPrimaryKey
     * @return AlleyPrimaryKey   
     */
    public AlleyPrimaryKey getAlleyPrimaryKey() 
    {    
    	AlleyPrimaryKey key = new AlleyPrimaryKey(); 
		key.setAlleyId( this.alleyId );
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
	* Returns the Leagues
  	* @return Set<League>	
	*/                    		    	    	    
	public Set<League> getLeagues() 	    	   
	{
		return this.leagues;		
	}
	
   /**
    * Assigns the leagues
    * @param leagues	Set<League>
    */
    public void setLeagues( Set<League> leagues )
    {
        this.leagues = leagues;
    }
    	
   /**
	* Returns the Tournaments
  	* @return Set<Tournament>	
	*/                    		    	    	    
	public Set<Tournament> getTournaments() 	    	   
	{
		return this.tournaments;		
	}
	
   /**
    * Assigns the tournaments
    * @param tournaments	Set<Tournament>
    */
    public void setTournaments( Set<Tournament> tournaments )
    {
        this.tournaments = tournaments;
    }
    	
   /**
	* Returns the Lanes
  	* @return Set<Lane>	
	*/                    		    	    	    
	public Set<Lane> getLanes() 	    	   
	{
		return this.lanes;		
	}
	
   /**
    * Assigns the lanes
    * @param lanes	Set<Lane>
    */
    public void setLanes( Set<Lane> lanes )
    {
        this.lanes = lanes;
    }
    	
   /**
	* Returns the alleyId
  	* @return Long	
	*/                    		    	    	    
	public Long getAlleyId() 	    	   
	{
		return this.alleyId;		
	}
	
   /**
    * Assigns the alleyId
    * @param alleyId	Long
    */
    public void setAlleyId( Long alleyId )
    {
        this.alleyId = alleyId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	Alley		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( Alley object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Alley:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.alleyId = object.getAlleyId();
        this.name = object.getName();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	Alley		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( Alley object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Alley:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( true )
        this.leagues = object.getLeagues();
        this.tournaments = object.getTournaments();
        this.lanes = object.getLanes();
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
		returnString.append( "alleyId = " + this.alleyId + ", ");
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
		StringBuilder identity = new StringBuilder( "Alley" );
		
			identity.append(  "::" );
		identity.append( alleyId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("Alley");
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
			
	    if (!(object instanceof Alley)) 
	        return false;
	        
		Alley bo = (Alley)object;
		
		return( getAlleyPrimaryKey().equals( bo.getAlleyPrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long alleyId = null;
      protected String name = null;
  @Embedded
    		  	  	      protected Set<League> leagues = null;
  @Embedded
    		  	  	      protected Set<Tournament> tournaments = null;
  @Embedded
    		  	  	      protected Set<Lane> lanes = null;
// ~AIB

}
