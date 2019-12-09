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
 * Test Tournament class.
 *
 * @author    Dev Team
 */
public class TournamentTest
{

// constructors

    public TournamentTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a Tournament, through a TournamentTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on TournamentTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on TournamentTest..." );
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
     * Tests creating a new Tournament.
     *
     * @return    Tournament
     */
    public Tournament testCreate()
    throws Throwable
    {
        Tournament businessObject = null;

    	{
	        LOGGER.info( "TournamentTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a Tournament");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a Tournament" );
	
	        try 
	        {            
	            businessObject = TournamentBusinessDelegate.getTournamentInstance().createTournament( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (TournamentPrimaryKey)businessObject.getTournamentPrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a Tournament with primary key" + thePrimaryKey );
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
     * Tests reading a Tournament.
     *
     * @return    Tournament  
     */
    public Tournament testRead()
    throws Throwable
    {
        LOGGER.info( "TournamentTest:testRead()" );
        LOGGER.info( "-- Reading a previously created Tournament" );

        Tournament businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read Tournament with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = TournamentBusinessDelegate.getTournamentInstance().getTournament( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found Tournament " + businessObject.toString() );
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
     * Tests updating a Tournament.
     *
     * @return    Tournament
     */
    public Tournament testUpdate()
    throws Throwable
    {

        LOGGER.info( "TournamentTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a Tournament." );

        StringBuilder msg = new StringBuilder( "Failed to update a Tournament : " );        
        Tournament businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created Tournament." );
            
            // for use later on...
            thePrimaryKey = (TournamentPrimaryKey)businessObject.getTournamentPrimaryKey();
            
            TournamentBusinessDelegate proxy = TournamentBusinessDelegate.getTournamentInstance();            
            businessObject = proxy.saveTournament( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved Tournament - " + businessObject.toString() );
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
     * Tests deleting a Tournament.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "TournamentTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created Tournament." );
        
        try
        {
            TournamentBusinessDelegate.getTournamentInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted Tournament with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete Tournament with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Tournaments.
     *
     * @return    Collection
     */
    public ArrayList<Tournament> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "TournamentTest:testGetAll() - Retrieving Collection of Tournaments:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all Tournament : " );        
        ArrayList<Tournament> collection  = null;

        try
        {
            // call the static get method on the TournamentBusinessDelegate
            collection = TournamentBusinessDelegate.getTournamentInstance().getAllTournament();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            Tournament currentBO  = null;            
	            Iterator<Tournament> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getTournamentPrimaryKey(), "-- value object in Collection has a null primary key" );        
	
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
    
    public TournamentTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate Tournament
     * 
     * @return    Tournament
     */    
    protected Tournament getNewBO()
    {
        Tournament newBO = new Tournament();

// AIB : \#defaultBOOutput() 
newBO.setName(
    new String( org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) )
 );
newBO.setType(
    TournamentType.getDefaultValue()
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected TournamentPrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(Tournament.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
