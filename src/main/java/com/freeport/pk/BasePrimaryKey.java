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
 * Base class for application PrimaryKey classes.
 * 
 * @author    Dev Team
 */
public abstract class BasePrimaryKey
{
	// for backward compatibility
    public Collection valuesAsCollection()
    {
        return (keys());
    }
    
    public boolean hasBeenAssigned()
    {
        return (getFirstKey() != null);
    }

// abstract methods
    
    public abstract List keys();
    public abstract Object getFirstKey();
}
