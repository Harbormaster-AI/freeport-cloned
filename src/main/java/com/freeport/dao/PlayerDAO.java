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
 * Implements the MongoDB NoSQL persistence processing for business entity Player
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class PlayerDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public PlayerDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a Player from the persistent store, using the provided primary key. 
     * If no match is found, a null Player is returned.
     * <p>
     * @param       pk
     * @return      Player
     * @exception   ProcessingException
     */
    public Player findPlayer( PlayerPrimaryKey pk ) 
    throws ProcessingException
    {
    	Player businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("PlayerDAO.findPlayer cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( Player.class )
        	       						.field( "playerId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "PlayerDAO.findPlayer failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new Player into the persistent store.
     * @param       businessObject
     * @return      newly persisted Player
     * @exception   ProcessingException
     */
    public Player createPlayer( Player businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "playerId" ) ;
    		businessObject.setPlayerId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- PlayerDAO.createPlayer created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "PlayerDAO.createPlayer - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided Player to the persistent store.
     *
     * @param       businessObject
     * @return      Player	stored entity
     * @exception   ProcessingException 
     */
    public Player savePlayer( Player businessObject )
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
			throw new ProcessingException( "PlayerDAO:savePlayer - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a Player from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deletePlayer( PlayerPrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findPlayer( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("PlayerDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "PlayerDAO.deletePlayer failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Players
     * @return		ArrayList<Player>
     * @exception   ProcessingException
     */
    public ArrayList<Player> findAllPlayer()
    throws ProcessingException
    {
		final ArrayList<Player> list 	= new ArrayList<Player>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( Player.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "PlayerDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "PlayerDAO.findAllPlayer failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "PlayerDAO:findAllPlayers() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.Player" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "playerId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
}
