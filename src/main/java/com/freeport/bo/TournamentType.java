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

/**
 * TournamentType enumerator class.  
 * 
 * Enumerated types are handled on behalf of Hiberate as VARCHARs.  The necessary
 * methods that implement Hibernat's UserType interface assume that Enumerated
 * types contain one or more values, each named uniquely and declared (modeled) with
 * order, although the order is assumed.
 * 
// AIB : #enumDocumentation()
     * Encapsulates data for business entity TournamentType.
    // ~AIB
 * @author Dev Team
 */
public enum TournamentType
{
				Pro,Amateur;

//************************************************************************
// Access Methods
//************************************************************************

    public static List<TournamentType> getValues()
    {
        return Arrays.asList(TournamentType.values());
    }


    public static TournamentType getDefaultValue()
    {
        return( Pro );
    }

    
//************************************************************************
// Helper Methods
//************************************************************************

//************************************************************************
// static implementations
//************************************************************************
    
    public static TournamentType whichOne( String name ) 
    {
        if ( name.equalsIgnoreCase("Pro" ) )
        {
            return (TournamentType.Pro);
        }
        if ( name.equalsIgnoreCase("Amateur" ) )
        {
            return (TournamentType.Amateur);
        }
        else
        {
            return (getDefaultValue());
        }
    }

//************************************************************************
// Protected / Private Methods
//************************************************************************
    
//************************************************************************
// Attributes
//************************************************************************

}

