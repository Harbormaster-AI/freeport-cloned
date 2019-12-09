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
 * Implements the MongoDB NoSQL persistence processing for business entity League
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class LeagueDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public LeagueDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a League from the persistent store, using the provided primary key. 
     * If no match is found, a null League is returned.
     * <p>
     * @param       pk
     * @return      League
     * @exception   ProcessingException
     */
    public League findLeague( LeaguePrimaryKey pk ) 
    throws ProcessingException
    {
    	League businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("LeagueDAO.findLeague cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( League.class )
        	       						.field( "leagueId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "LeagueDAO.findLeague failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new League into the persistent store.
     * @param       businessObject
     * @return      newly persisted League
     * @exception   ProcessingException
     */
    public League createLeague( League businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "leagueId" ) ;
    		businessObject.setLeagueId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- LeagueDAO.createLeague created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "LeagueDAO.createLeague - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided League to the persistent store.
     *
     * @param       businessObject
     * @return      League	stored entity
     * @exception   ProcessingException 
     */
    public League saveLeague( League businessObject )
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
			throw new ProcessingException( "LeagueDAO:saveLeague - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a League from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deleteLeague( LeaguePrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findLeague( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("LeagueDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "LeagueDAO.deleteLeague failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Leagues
     * @return		ArrayList<League>
     * @exception   ProcessingException
     */
    public ArrayList<League> findAllLeague()
    throws ProcessingException
    {
		final ArrayList<League> list 	= new ArrayList<League>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( League.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "LeagueDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "LeagueDAO.findAllLeague failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "LeagueDAO:findAllLeagues() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.League" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "leagueId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(League.class.getName());
}
