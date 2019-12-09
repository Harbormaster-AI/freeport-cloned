/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.dao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import com.freeport.exception.*;

    import com.freeport.primarykey.*;
    import com.freeport.bo.*;
    
/** 
 * Implements the MongoDB NoSQL persistence processing for business entity Lane
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class LaneDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public LaneDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a Lane from the persistent store, using the provided primary key. 
     * If no match is found, a null Lane is returned.
     * <p>
     * @param       pk
     * @return      Lane
     * @exception   ProcessingException
     */
    public Lane findLane( LanePrimaryKey pk ) 
    throws ProcessingException
    {
    	Lane businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("LaneDAO.findLane cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( Lane.class )
        	       						.field( "laneId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "LaneDAO.findLane failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new Lane into the persistent store.
     * @param       businessObject
     * @return      newly persisted Lane
     * @exception   ProcessingException
     */
    public Lane createLane( Lane businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "laneId" ) ;
    		businessObject.setLaneId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- LaneDAO.createLane created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "LaneDAO.createLane - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided Lane to the persistent store.
     *
     * @param       businessObject
     * @return      Lane	stored entity
     * @exception   ProcessingException 
     */
    public Lane saveLane( Lane businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();    	
    	
    	try
    	{
    		dataStore.save( businessObject );
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "LaneDAO:saveLane - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a Lane from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deleteLane( LanePrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findLane( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("LaneDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "LaneDAO.deleteLane failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Lanes
     * @return		ArrayList<Lane>
     * @exception   ProcessingException
     */
    public ArrayList<Lane> findAllLane()
    throws ProcessingException
    {
		final ArrayList<Lane> list 	= new ArrayList<Lane>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( Lane.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "LaneDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "LaneDAO.findAllLane failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "LaneDAO:findAllLanes() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.Lane" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "laneId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(Lane.class.getName());
}
