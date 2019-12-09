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
 * Encapsulates data for business entity Lane.
 * 
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity( value = "com.freeport.bo.Lane", noClassnameStored = true )
 public class Lane extends Base
 {

//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public Lane() 
    {
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the LanePrimaryKey
     * @return LanePrimaryKey   
     */
    public LanePrimaryKey getLanePrimaryKey() 
    {    
    	LanePrimaryKey key = new LanePrimaryKey(); 
		key.setLaneId( this.laneId );
        return( key );
    } 

    
// AIB : #getBOAccessorMethods(true)
   /**
	* Returns the number
  	* @return java.lang.Integer	
	*/                    		    	    	    
	public java.lang.Integer getNumber() 	    	   
	{
		return this.number;		
	}
	
   /**
    * Assigns the number
    * @param number	java.lang.Integer
    */
    public void setNumber( java.lang.Integer number )
    {
        this.number = number;
    }
    	
   /**
	* Returns the laneId
  	* @return Long	
	*/                    		    	    	    
	public Long getLaneId() 	    	   
	{
		return this.laneId;		
	}
	
   /**
    * Assigns the laneId
    * @param laneId	Long
    */
    public void setLaneId( Long laneId )
    {
        this.laneId = laneId;
    }
    	
// ~AIB
 
    /**
     * Performs a shallow copy.
     * @param object 	Lane		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyShallow( Lane object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Lane:copy(..) - object cannot be null.");           
        }

        // Call base class copy
        super.copy( object );
        
        // Set member attributes
                
// AIB : #getCopyString( false )
        this.laneId = object.getLaneId();
        this.number = object.getNumber();
// ~AIB 

    }

    /**
     * Performs a deep copy.
     * @param object 	Lane		copy source
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copy( Lane object ) 
    throws IllegalArgumentException
    {
        if ( object == null )
        {
            throw new IllegalArgumentException(" Lane:copy(..) - object cannot be null.");           
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
		returnString.append( "laneId = " + this.laneId + ", ");
		returnString.append( "number = " + this.number + ", ");
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
		StringBuilder identity = new StringBuilder( "Lane" );
		
			identity.append(  "::" );
		identity.append( laneId );
	        return ( identity.toString() );
    }

    public String getObjectType()
    {
        return ("Lane");
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
			
	    if (!(object instanceof Lane)) 
	        return false;
	        
		Lane bo = (Lane)object;
		
		return( getLanePrimaryKey().equals( bo.getLanePrimaryKey() ) ); 
	}
	
	
// attributes

// AIB : #getAttributeDeclarations( true  )
@Id
 		protected Long laneId = null;
      	protected java.lang.Integer number = new java.lang.Integer("0");
// ~AIB

}
