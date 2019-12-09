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
 * Test Player class.
 *
 * @author    Dev Team
 */
public class PlayerTest
{

// constructors

    public PlayerTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a Player, through a PlayerTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on PlayerTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on PlayerTest..." );
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
     * Tests creating a new Player.
     *
     * @return    Player
     */
    public Player testCreate()
    throws Throwable
    {
        Player businessObject = null;

    	{
	        LOGGER.info( "PlayerTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a Player");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a Player" );
	
	        try 
	        {            
	            businessObject = PlayerBusinessDelegate.getPlayerInstance().createPlayer( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (PlayerPrimaryKey)businessObject.getPlayerPrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a Player with primary key" + thePrimaryKey );
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
     * Tests reading a Player.
     *
     * @return    Player  
     */
    public Player testRead()
    throws Throwable
    {
        LOGGER.info( "PlayerTest:testRead()" );
        LOGGER.info( "-- Reading a previously created Player" );

        Player businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read Player with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = PlayerBusinessDelegate.getPlayerInstance().getPlayer( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found Player " + businessObject.toString() );
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
     * Tests updating a Player.
     *
     * @return    Player
     */
    public Player testUpdate()
    throws Throwable
    {

        LOGGER.info( "PlayerTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a Player." );

        StringBuilder msg = new StringBuilder( "Failed to update a Player : " );        
        Player businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created Player." );
            
            // for use later on...
            thePrimaryKey = (PlayerPrimaryKey)businessObject.getPlayerPrimaryKey();
            
            PlayerBusinessDelegate proxy = PlayerBusinessDelegate.getPlayerInstance();            
            businessObject = proxy.savePlayer( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved Player - " + businessObject.toString() );
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
     * Tests deleting a Player.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "PlayerTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created Player." );
        
        try
        {
            PlayerBusinessDelegate.getPlayerInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted Player with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete Player with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Players.
     *
     * @return    Collection
     */
    public ArrayList<Player> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "PlayerTest:testGetAll() - Retrieving Collection of Players:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all Player : " );        
        ArrayList<Player> collection  = null;

        try
        {
            // call the static get method on the PlayerBusinessDelegate
            collection = PlayerBusinessDelegate.getPlayerInstance().getAllPlayer();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            Player currentBO  = null;            
	            Iterator<Player> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getPlayerPrimaryKey(), "-- value object in Collection has a null primary key" );        
	
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
    
    public PlayerTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate Player
     * 
     * @return    Player
     */    
    protected Player getNewBO()
    {
        Player newBO = new Player();

// AIB : \#defaultBOOutput() 
newBO.setName(
    new String( org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) )
 );
newBO.setDateOfBirth(
    java.util.Calendar.getInstance().getTime()
 );
newBO.setHeight(
    new java.lang.Double( new String(org.apache.commons.lang3.RandomStringUtils.randomNumeric(3) )  )
 );
newBO.setIsProfessional(
    new java.lang.Boolean( new String(org.apache.commons.lang3.RandomStringUtils.randomNumeric(3) )  )
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected PlayerPrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(Player.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
