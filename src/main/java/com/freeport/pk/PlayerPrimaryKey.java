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
 * Player PrimaryKey class.
 * 
 * @author    Dev Team
 */
// AIB : #getPrimaryKeyClassDecl() 
public class PlayerPrimaryKey 

    extends BasePrimaryKey
{
// ~AIB

//************************************************************************
// Public Methods
//************************************************************************

    /** 
     * default constructor - should be normally used for dynamic instantiation
     */
    public PlayerPrimaryKey() 
    {
    }    
    
    /** 
     * Constructor with all arguments relating to the primary key
     * 	        
// AIB : #getAllAttributeJavaComments( true true )
    * @param    playerId
// ~AIB     
     * @exception IllegalArgumentException
     */
    public PlayerPrimaryKey(    
// AIB : #getAllPrimaryKeyArguments( $classObject false )
 		Object playerId 			
// ~AIB
                         ) 
    throws IllegalArgumentException
    {
        super();
// AIB : #getKeyFieldAssignments()
		this.playerId = playerId != null ? new Long( playerId.toString() ) : null;
// ~AIB 
    }   

    
//************************************************************************
// Access Methods
//************************************************************************

// AIB : #getKeyFieldAccessMethods()
   /**
	* Returns the playerId.
	* @return    Long
    */    
	public Long getPlayerId()
	{
		return( this.playerId );
	}            
	
   /**
	* Assigns the playerId.
	* @return    Long
    */    
	public void setPlayerId( Long id )
	{
		this.playerId = id;
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
        
		keys.add( playerId );

        return( keys );
    }

	public Object getFirstKey()
	{
		return( playerId );
	}

 
//************************************************************************
// Protected / Private Methods
//************************************************************************

    
//************************************************************************
// Attributes
//************************************************************************

 	

// DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
// WITHIN THE Player class.

// AIB : #getKeyFieldDeclarations()
	public Long playerId;
// ~AIB 	        

}
