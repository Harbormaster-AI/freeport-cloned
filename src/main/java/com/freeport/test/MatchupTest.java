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
 * Test Matchup class.
 *
 * @author    Dev Team
 */
public class MatchupTest
{

// constructors

    public MatchupTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a Matchup, through a MatchupTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on MatchupTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on MatchupTest..." );
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
     * Tests creating a new Matchup.
     *
     * @return    Matchup
     */
    public Matchup testCreate()
    throws Throwable
    {
        Matchup businessObject = null;

    	{
	        LOGGER.info( "MatchupTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a Matchup");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a Matchup" );
	
	        try 
	        {            
	            businessObject = MatchupBusinessDelegate.getMatchupInstance().createMatchup( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (MatchupPrimaryKey)businessObject.getMatchupPrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a Matchup with primary key" + thePrimaryKey );
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
     * Tests reading a Matchup.
     *
     * @return    Matchup  
     */
    public Matchup testRead()
    throws Throwable
    {
        LOGGER.info( "MatchupTest:testRead()" );
        LOGGER.info( "-- Reading a previously created Matchup" );

        Matchup businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read Matchup with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = MatchupBusinessDelegate.getMatchupInstance().getMatchup( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found Matchup " + businessObject.toString() );
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
     * Tests updating a Matchup.
     *
     * @return    Matchup
     */
    public Matchup testUpdate()
    throws Throwable
    {

        LOGGER.info( "MatchupTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a Matchup." );

        StringBuilder msg = new StringBuilder( "Failed to update a Matchup : " );        
        Matchup businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created Matchup." );
            
            // for use later on...
            thePrimaryKey = (MatchupPrimaryKey)businessObject.getMatchupPrimaryKey();
            
            MatchupBusinessDelegate proxy = MatchupBusinessDelegate.getMatchupInstance();            
            businessObject = proxy.saveMatchup( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved Matchup - " + businessObject.toString() );
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
     * Tests deleting a Matchup.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "MatchupTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created Matchup." );
        
        try
        {
            MatchupBusinessDelegate.getMatchupInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted Matchup with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete Matchup with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Matchups.
     *
     * @return    Collection
     */
    public ArrayList<Matchup> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "MatchupTest:testGetAll() - Retrieving Collection of Matchups:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all Matchup : " );        
        ArrayList<Matchup> collection  = null;

        try
        {
            // call the static get method on the MatchupBusinessDelegate
            collection = MatchupBusinessDelegate.getMatchupInstance().getAllMatchup();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            Matchup currentBO  = null;            
	            Iterator<Matchup> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getMatchupPrimaryKey(), "-- value object in Collection has a null primary key" );        
	
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
    
    public MatchupTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate Matchup
     * 
     * @return    Matchup
     */    
    protected Matchup getNewBO()
    {
        Matchup newBO = new Matchup();

// AIB : \#defaultBOOutput() 
newBO.setName(
    new String( org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) )
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected MatchupPrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(Matchup.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
