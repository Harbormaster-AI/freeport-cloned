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
 * Test Lane class.
 *
 * @author    Dev Team
 */
public class LaneTest
{

// constructors

    public LaneTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a Lane, through a LaneTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on LaneTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on LaneTest..." );
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
     * Tests creating a new Lane.
     *
     * @return    Lane
     */
    public Lane testCreate()
    throws Throwable
    {
        Lane businessObject = null;

    	{
	        LOGGER.info( "LaneTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a Lane");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a Lane" );
	
	        try 
	        {            
	            businessObject = LaneBusinessDelegate.getLaneInstance().createLane( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (LanePrimaryKey)businessObject.getLanePrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a Lane with primary key" + thePrimaryKey );
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
     * Tests reading a Lane.
     *
     * @return    Lane  
     */
    public Lane testRead()
    throws Throwable
    {
        LOGGER.info( "LaneTest:testRead()" );
        LOGGER.info( "-- Reading a previously created Lane" );

        Lane businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read Lane with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = LaneBusinessDelegate.getLaneInstance().getLane( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found Lane " + businessObject.toString() );
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
     * Tests updating a Lane.
     *
     * @return    Lane
     */
    public Lane testUpdate()
    throws Throwable
    {

        LOGGER.info( "LaneTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a Lane." );

        StringBuilder msg = new StringBuilder( "Failed to update a Lane : " );        
        Lane businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created Lane." );
            
            // for use later on...
            thePrimaryKey = (LanePrimaryKey)businessObject.getLanePrimaryKey();
            
            LaneBusinessDelegate proxy = LaneBusinessDelegate.getLaneInstance();            
            businessObject = proxy.saveLane( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved Lane - " + businessObject.toString() );
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
     * Tests deleting a Lane.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "LaneTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created Lane." );
        
        try
        {
            LaneBusinessDelegate.getLaneInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted Lane with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete Lane with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Lanes.
     *
     * @return    Collection
     */
    public ArrayList<Lane> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "LaneTest:testGetAll() - Retrieving Collection of Lanes:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all Lane : " );        
        ArrayList<Lane> collection  = null;

        try
        {
            // call the static get method on the LaneBusinessDelegate
            collection = LaneBusinessDelegate.getLaneInstance().getAllLane();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            Lane currentBO  = null;            
	            Iterator<Lane> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getLanePrimaryKey(), "-- value object in Collection has a null primary key" );        
	
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
    
    public LaneTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate Lane
     * 
     * @return    Lane
     */    
    protected Lane getNewBO()
    {
        Lane newBO = new Lane();

// AIB : \#defaultBOOutput() 
newBO.setNumber(
    new java.lang.Integer( new String(org.apache.commons.lang3.RandomStringUtils.randomNumeric(3) )  )
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected LanePrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(Lane.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
