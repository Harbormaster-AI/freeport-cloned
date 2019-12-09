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
 * Tournament PrimaryKey class.
 * 
 * @author    Dev Team
 */
// AIB : #getPrimaryKeyClassDecl() 
public class TournamentPrimaryKey 

    extends BasePrimaryKey
{
// ~AIB

//************************************************************************
// Public Methods
//************************************************************************

    /** 
     * default constructor - should be normally used for dynamic instantiation
     */
    public TournamentPrimaryKey() 
    {
    }    
    
    /** 
     * Constructor with all arguments relating to the primary key
     * 	        
// AIB : #getAllAttributeJavaComments( true true )
    * @param    tournamentId
// ~AIB     
     * @exception IllegalArgumentException
     */
    public TournamentPrimaryKey(    
// AIB : #getAllPrimaryKeyArguments( $classObject false )
 		Object tournamentId 			
// ~AIB
                         ) 
    throws IllegalArgumentException
    {
        super();
// AIB : #getKeyFieldAssignments()
		this.tournamentId = tournamentId != null ? new Long( tournamentId.toString() ) : null;
// ~AIB 
    }   

    
//************************************************************************
// Access Methods
//************************************************************************

// AIB : #getKeyFieldAccessMethods()
   /**
	* Returns the tournamentId.
	* @return    Long
    */    
	public Long getTournamentId()
	{
		return( this.tournamentId );
	}            
	
   /**
	* Assigns the tournamentId.
	* @return    Long
    */    
	public void setTournamentId( Long id )
	{
		this.tournamentId = id;
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
        
		keys.add( tournamentId );

        return( keys );
    }

	public Object getFirstKey()
	{
		return( tournamentId );
	}

 
//************************************************************************
// Protected / Private Methods
//************************************************************************

    
//************************************************************************
// Attributes
//************************************************************************

 	

// DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
// WITHIN THE Tournament class.

// AIB : #getKeyFieldDeclarations()
	public Long tournamentId;
// ~AIB 	        

}
