/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.primarykey;

import java.util.*;

/**
 * League PrimaryKey class.
 * 
 * @author    Dev Team
 */
// AIB : #getPrimaryKeyClassDecl() 
public class LeaguePrimaryKey 

    extends BasePrimaryKey
{
// ~AIB

//************************************************************************
// Public Methods
//************************************************************************

    /** 
     * default constructor - should be normally used for dynamic instantiation
     */
    public LeaguePrimaryKey() 
    {
    }    
    
    /** 
     * Constructor with all arguments relating to the primary key
     * 	        
// AIB : #getAllAttributeJavaComments( true true )
    * @param    leagueId
// ~AIB     
     * @exception IllegalArgumentException
     */
    public LeaguePrimaryKey(    
// AIB : #getAllPrimaryKeyArguments( $classObject false )
 		Object leagueId 			
// ~AIB
                         ) 
    throws IllegalArgumentException
    {
        super();
// AIB : #getKeyFieldAssignments()
		this.leagueId = leagueId != null ? new Long( leagueId.toString() ) : null;
// ~AIB 
    }   

    
//************************************************************************
// Access Methods
//************************************************************************

// AIB : #getKeyFieldAccessMethods()
   /**
	* Returns the leagueId.
	* @return    Long
    */    
	public Long getLeagueId()
	{
		return( this.leagueId );
	}            
	
   /**
	* Assigns the leagueId.
	* @return    Long
    */    
	public void setLeagueId( Long id )
	{
		this.leagueId = id;
	}            
	
// ~AIB 	         	     

    /**
     * Retrieves the value(s) as a single List
     * @return List
     */
    public List keys()
    {
		// assign the attributes to the Collection back to the parent
        ArrayList keys = new ArrayList();
        
		keys.add( leagueId );

        return( keys );
    }

	public Object getFirstKey()
	{
		return( leagueId );
	}

 
//************************************************************************
// Protected / Private Methods
//************************************************************************

    
//************************************************************************
// Attributes
//************************************************************************

 	

// DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
// WITHIN THE League class.

// AIB : #getKeyFieldDeclarations()
	public Long leagueId;
// ~AIB 	        

}
