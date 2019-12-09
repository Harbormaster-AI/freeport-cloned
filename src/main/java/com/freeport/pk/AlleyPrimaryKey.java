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
 * Alley PrimaryKey class.
 * 
 * @author    Dev Team
 */
// AIB : #getPrimaryKeyClassDecl() 
public class AlleyPrimaryKey 

    extends BasePrimaryKey
{
// ~AIB

//************************************************************************
// Public Methods
//************************************************************************

    /** 
     * default constructor - should be normally used for dynamic instantiation
     */
    public AlleyPrimaryKey() 
    {
    }    
    
    /** 
     * Constructor with all arguments relating to the primary key
     * 	        
// AIB : #getAllAttributeJavaComments( true true )
    * @param    alleyId
// ~AIB     
     * @exception IllegalArgumentException
     */
    public AlleyPrimaryKey(    
// AIB : #getAllPrimaryKeyArguments( $classObject false )
 		Object alleyId 			
// ~AIB
                         ) 
    throws IllegalArgumentException
    {
        super();
// AIB : #getKeyFieldAssignments()
		this.alleyId = alleyId != null ? new Long( alleyId.toString() ) : null;
// ~AIB 
    }   

    
//************************************************************************
// Access Methods
//************************************************************************

// AIB : #getKeyFieldAccessMethods()
   /**
	* Returns the alleyId.
	* @return    Long
    */    
	public Long getAlleyId()
	{
		return( this.alleyId );
	}            
	
   /**
	* Assigns the alleyId.
	* @return    Long
    */    
	public void setAlleyId( Long id )
	{
		this.alleyId = id;
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
        
		keys.add( alleyId );

        return( keys );
    }

	public Object getFirstKey()
	{
		return( alleyId );
	}

 
//************************************************************************
// Protected / Private Methods
//************************************************************************

    
//************************************************************************
// Attributes
//************************************************************************

 	

// DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
// WITHIN THE Alley class.

// AIB : #getKeyFieldDeclarations()
	public Long alleyId;
// ~AIB 	        

}
