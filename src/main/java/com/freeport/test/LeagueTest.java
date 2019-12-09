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

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

    import com.freeport.primarykey.*;
    import com.freeport.delegate.*;
    import com.freeport.bo.*;
    
/**
 * Test League class.
 *
 * @author    Dev Team
 */
public class LeagueTest
{

// constructors

    public LeagueTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a League, through a LeagueTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on LeagueTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on LeagueTest..." );
            LOGGER.info( "**********************************************************" );
            LOGGER.info( "" );
        }
        catch( Throwable e )
        {
            throw e;
        }
        finally 
        {
        	if ( handler != null ) {
        		handler.flush();
        		LOGGER.removeHandler(handler);
        	}
        }
   }

    /** 
     * Tests creating a new League.
     *
     * @return    League
     */
    public League testCreate()
    throws Throwable
    {
        League businessObject = null;

    	{
	        LOGGER.info( "LeagueTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a League");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a League" );
	
	        try 
	        {            
	            businessObject = LeagueBusinessDelegate.getLeagueInstance().createLeague( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (LeaguePrimaryKey)businessObject.getLeaguePrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a League with primary key" + thePrimaryKey );
	        }
	        catch (Exception e) 
	        {
	            LOGGER.warning( unexpectedErrorMsg );
	            LOGGER.warning( msg.toString() + businessObject );
	            
	            throw e;
	        }
    	}
        return businessObject;
    }

    /** 
     * Tests reading a League.
     *
     * @return    League  
     */
    public League testRead()
    throws Throwable
    {
        LOGGER.info( "LeagueTest:testRead()" );
        LOGGER.info( "-- Reading a previously created League" );

        League businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read League with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = LeagueBusinessDelegate.getLeagueInstance().getLeague( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found League " + businessObject.toString() );
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( msg.toString() + " : " + e );
            
            throw e;
        }

        return( businessObject );
    }

    /** 
     * Tests updating a League.
     *
     * @return    League
     */
    public League testUpdate()
    throws Throwable
    {

        LOGGER.info( "LeagueTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a League." );

        StringBuilder msg = new StringBuilder( "Failed to update a League : " );        
        League businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created League." );
            
            // for use later on...
            thePrimaryKey = (LeaguePrimaryKey)businessObject.getLeaguePrimaryKey();
            
            LeagueBusinessDelegate proxy = LeagueBusinessDelegate.getLeagueInstance();            
            businessObject = proxy.saveLeague( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved League - " + businessObject.toString() );
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( msg.toString() + " : primarykey-" + thePrimaryKey + " : businessObject-" +  businessObject + " : " + e );
            
            throw e;
        }

        return( businessObject );
    }

    /** 
     * Tests deleting a League.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "LeagueTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created League." );
        
        try
        {
            LeagueBusinessDelegate.getLeagueInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted League with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete League with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Leagues.
     *
     * @return    Collection
     */
    public ArrayList<League> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "LeagueTest:testGetAll() - Retrieving Collection of Leagues:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all League : " );        
        ArrayList<League> collection  = null;

        try
        {
            // call the static get method on the LeagueBusinessDelegate
            collection = LeagueBusinessDelegate.getLeagueInstance().getAllLeague();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            League currentBO  = null;            
	            Iterator<League> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getLeaguePrimaryKey(), "-- value object in Collection has a null primary key" );        
	
	                LOGGER.info( " - " + currentBO.toString() );
	            }
            }
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( msg.toString() );
            
            throw e;
        }

        return( collection );
    }
    
    public LeagueTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate League
     * 
     * @return    League
     */    
    protected League getNewBO()
    {
        League newBO = new League();

// AIB : \#defaultBOOutput() 
newBO.setName(
    new String( org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) )
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected LeaguePrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(League.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
