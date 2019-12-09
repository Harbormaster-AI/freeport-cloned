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
 * Test Game class.
 *
 * @author    Dev Team
 */
public class GameTest
{

// constructors

    public GameTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a Game, through a GameTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on GameTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on GameTest..." );
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
     * Tests creating a new Game.
     *
     * @return    Game
     */
    public Game testCreate()
    throws Throwable
    {
        Game businessObject = null;

    	{
	        LOGGER.info( "GameTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a Game");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a Game" );
	
	        try 
	        {            
	            businessObject = GameBusinessDelegate.getGameInstance().createGame( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (GamePrimaryKey)businessObject.getGamePrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a Game with primary key" + thePrimaryKey );
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
     * Tests reading a Game.
     *
     * @return    Game  
     */
    public Game testRead()
    throws Throwable
    {
        LOGGER.info( "GameTest:testRead()" );
        LOGGER.info( "-- Reading a previously created Game" );

        Game businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read Game with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = GameBusinessDelegate.getGameInstance().getGame( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found Game " + businessObject.toString() );
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
     * Tests updating a Game.
     *
     * @return    Game
     */
    public Game testUpdate()
    throws Throwable
    {

        LOGGER.info( "GameTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a Game." );

        StringBuilder msg = new StringBuilder( "Failed to update a Game : " );        
        Game businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created Game." );
            
            // for use later on...
            thePrimaryKey = (GamePrimaryKey)businessObject.getGamePrimaryKey();
            
            GameBusinessDelegate proxy = GameBusinessDelegate.getGameInstance();            
            businessObject = proxy.saveGame( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved Game - " + businessObject.toString() );
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
     * Tests deleting a Game.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "GameTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created Game." );
        
        try
        {
            GameBusinessDelegate.getGameInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted Game with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete Game with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Games.
     *
     * @return    Collection
     */
    public ArrayList<Game> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "GameTest:testGetAll() - Retrieving Collection of Games:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all Game : " );        
        ArrayList<Game> collection  = null;

        try
        {
            // call the static get method on the GameBusinessDelegate
            collection = GameBusinessDelegate.getGameInstance().getAllGame();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            Game currentBO  = null;            
	            Iterator<Game> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getGamePrimaryKey(), "-- value object in Collection has a null primary key" );        
	
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
    
    public GameTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate Game
     * 
     * @return    Game
     */    
    protected Game getNewBO()
    {
        Game newBO = new Game();

// AIB : \#defaultBOOutput() 
newBO.setFrames(
    new java.lang.Integer( new String(org.apache.commons.lang3.RandomStringUtils.randomNumeric(3) )  )
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected GamePrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(Game.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
