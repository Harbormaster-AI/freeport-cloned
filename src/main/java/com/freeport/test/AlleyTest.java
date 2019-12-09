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
 * Test Alley class.
 *
 * @author    Dev Team
 */
public class AlleyTest
{

// constructors

    public AlleyTest()
    {
    	LOGGER.setUseParentHandlers(false);	// only want to output to the provided LogHandler
    }

// test methods
    @Test
    /** 
     * Full Create-Read-Update-Delete of a Alley, through a AlleyTest.
     */
    public void testCRUD()
    throws Throwable
   {        
        try
        {
        	LOGGER.info( "**********************************************************" );
            LOGGER.info( "Beginning full test on AlleyTest..." );
            
            testCreate();            
            testRead();        
            testUpdate();
            testGetAll();                
            testDelete();
            
            LOGGER.info( "Successfully ran a full test on AlleyTest..." );
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
     * Tests creating a new Alley.
     *
     * @return    Alley
     */
    public Alley testCreate()
    throws Throwable
    {
        Alley businessObject = null;

    	{
	        LOGGER.info( "AlleyTest:testCreate()" );
	        LOGGER.info( "-- Attempting to create a Alley");
	
	        StringBuilder msg = new StringBuilder( "-- Failed to create a Alley" );
	
	        try 
	        {            
	            businessObject = AlleyBusinessDelegate.getAlleyInstance().createAlley( getNewBO() );
	            assertNotNull( businessObject, msg.toString() );
	
	            thePrimaryKey = (AlleyPrimaryKey)businessObject.getAlleyPrimaryKey();
	            assertNotNull( thePrimaryKey, msg.toString() + " Contains a null primary key" );
	
	            LOGGER.info( "-- Successfully created a Alley with primary key" + thePrimaryKey );
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
     * Tests reading a Alley.
     *
     * @return    Alley  
     */
    public Alley testRead()
    throws Throwable
    {
        LOGGER.info( "AlleyTest:testRead()" );
        LOGGER.info( "-- Reading a previously created Alley" );

        Alley businessObject = null;
        StringBuilder msg = new StringBuilder( "-- Failed to read Alley with primary key" );
        msg.append( thePrimaryKey );

        try
        {
            businessObject = AlleyBusinessDelegate.getAlleyInstance().getAlley( thePrimaryKey );
            
            assertNotNull( businessObject,msg.toString() );

            LOGGER.info( "-- Successfully found Alley " + businessObject.toString() );
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
     * Tests updating a Alley.
     *
     * @return    Alley
     */
    public Alley testUpdate()
    throws Throwable
    {

        LOGGER.info( "AlleyTest:testUpdate()" );
        LOGGER.info( "-- Attempting to update a Alley." );

        StringBuilder msg = new StringBuilder( "Failed to update a Alley : " );        
        Alley businessObject = null;
    
        try
        {            
            businessObject = testCreate();
            
            assertNotNull( businessObject, msg.toString() );

            LOGGER.info( "-- Now updating the created Alley." );
            
            // for use later on...
            thePrimaryKey = (AlleyPrimaryKey)businessObject.getAlleyPrimaryKey();
            
            AlleyBusinessDelegate proxy = AlleyBusinessDelegate.getAlleyInstance();            
            businessObject = proxy.saveAlley( businessObject );   
            
            assertNotNull( businessObject, msg.toString()  );

            LOGGER.info( "-- Successfully saved Alley - " + businessObject.toString() );
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
     * Tests deleting a Alley.
     */
    public void testDelete()
    throws Throwable
    {
        LOGGER.info( "AlleyTest:testDelete()" );
        LOGGER.info( "-- Deleting a previously created Alley." );
        
        try
        {
            AlleyBusinessDelegate.getAlleyInstance().delete( thePrimaryKey );
            
            LOGGER.info( "-- Successfully deleted Alley with primary key " + thePrimaryKey );            
        }
        catch ( Throwable e )
        {
            LOGGER.warning( unexpectedErrorMsg );
            LOGGER.warning( "-- Failed to delete Alley with primary key " + thePrimaryKey );
            
            throw e;
        }
    }

    /** 
     * Tests getting all Alleys.
     *
     * @return    Collection
     */
    public ArrayList<Alley> testGetAll()
    throws Throwable
    {    
        LOGGER.info( "AlleyTest:testGetAll() - Retrieving Collection of Alleys:" );

        StringBuilder msg = new StringBuilder( "-- Failed to get all Alley : " );        
        ArrayList<Alley> collection  = null;

        try
        {
            // call the static get method on the AlleyBusinessDelegate
            collection = AlleyBusinessDelegate.getAlleyInstance().getAllAlley();

            if ( collection == null || collection.size() == 0 )
            {
                LOGGER.warning( unexpectedErrorMsg );
                LOGGER.warning( "-- " + msg.toString() + " Empty collection returned."  );
            }
            else
            {
	            // Now print out the values
	            Alley currentBO  = null;            
	            Iterator<Alley> iter = collection.iterator();
					
	            while( iter.hasNext() )
	            {
	                // Retrieve the businessObject   
	                currentBO = iter.next();
	                
	                assertNotNull( currentBO,"-- null value object in Collection." );
	                assertNotNull( currentBO.getAlleyPrimaryKey(), "-- value object in Collection has a null primary key" );        
	
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
    
    public AlleyTest setHandler( Handler handler ) {
    	this.handler = handler;
    	LOGGER.addHandler(handler);	// assign so the LOGGER can only output results to the Handler
    	return this;
    }
    
    /** 
     * Returns a new populate Alley
     * 
     * @return    Alley
     */    
    protected Alley getNewBO()
    {
        Alley newBO = new Alley();

// AIB : \#defaultBOOutput() 
newBO.setName(
    new String( org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) )
 );
// ~AIB

        return( newBO );
    }
    
// attributes 

    protected AlleyPrimaryKey thePrimaryKey      = null;
	protected Properties frameworkProperties 			= null;
	private final Logger LOGGER = Logger.getLogger(Alley.class.getName());
	private Handler handler = null;
	private String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
}
