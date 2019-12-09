/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.test;

import java.util.logging.*;

/**
 * Base class for application Test classes.
 *
 * @author    Dev Team
 */
public class BaseTest
{
	/**
	 * hidden
	 */
	protected BaseTest() {
	}
	
	public static void runTheTest( Handler logHandler ) 
    {
         try {
		    new PlayerTest().setHandler(logHandler).testCRUD();
		    new LeagueTest().setHandler(logHandler).testCRUD();
		    new TournamentTest().setHandler(logHandler).testCRUD();
		    new MatchupTest().setHandler(logHandler).testCRUD();
		    new GameTest().setHandler(logHandler).testCRUD();
		    new AlleyTest().setHandler(logHandler).testCRUD();
		    new LaneTest().setHandler(logHandler).testCRUD();
        } catch( Throwable exc ) {
        	exc.printStackTrace();
        }
    }
	
}
