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

import java.util.*;

import com.freeport.bo.*;

/**
 * Base class for application Value Objects.
 * 
 * @author Dev Team
 */ 
public abstract class Base
{
	/**
	 * Default Constructor
	 */
	protected Base()
	{
		super();
	}
	
    public abstract String getIdentity();
    public abstract String getObjectType();
    
    public boolean equals( Object object )
    { return true; }
    public void copyShallow( Object object )
    {}
    public void copy( Object object )
    {}
	
	protected Collection<String> attributesByNameUserIdentifiesBy()
	{
		Collection<String> names = new ArrayList<String>();
		// if not overloaded, try looking for a name attribute in the hierarchy
		// if none found
		names.add( "No name assigned..." );
		return ( names );
	}	
	
	protected String attributesByNameUserIdentifiesByAsString()
	{
		Collection<String> names = attributesByNameUserIdentifiesBy();
		Iterator<String> iter = names.iterator();
		String name = "";
		
		while( iter.hasNext() )
		{
			name += iter.next();
			if ( iter.hasNext() )
				name += " ";
		}
		return( name );						
	}

}
